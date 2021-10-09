package xyz.savvamirzoyan.wordremember.viewmodel

import android.view.View
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.constants.Endings
import xyz.savvamirzoyan.wordremember.constants.Person
import xyz.savvamirzoyan.wordremember.constants.Prefix
import xyz.savvamirzoyan.wordremember.constants.Suffix
import xyz.savvamirzoyan.wordremember.contract.repository.IAddWordRepository
import xyz.savvamirzoyan.wordremember.data.entity.VerbFormHelper
import xyz.savvamirzoyan.wordremember.data.state.DataInputState
import xyz.savvamirzoyan.wordremember.data.state.VerbFormType
import xyz.savvamirzoyan.wordremember.data.types.WordGender
import xyz.savvamirzoyan.wordremember.data.types.WordType
import xyz.savvamirzoyan.wordremember.data.types.WordType.*

class AddWordViewModel(
    private val repository: IAddWordRepository
) : ViewModel() {

    private var wordGender: WordGender? = null
    private var word: String = ""
    private var wordType: WordType = NOUN
    private var wordPluralForm: String = ""
    private var isOnlyPlural: Boolean = false
    private val verbFormHelper: VerbFormHelper = VerbFormHelper()
    private var komparativ: String = ""
    private var superlativ: String = ""
    private var translation: String = ""

    private val initWordGenderTextInputState by lazy {
        DataInputState(true, null, R.string.add_word_required)
    }
    private val initWordTextInputState by lazy {
        DataInputState(true, null, R.string.add_word_required)
    }
    private val initPluralFormTextInputState by lazy {
        DataInputState(true, null, R.string.add_word_not_required)
    }
    private val initTranslationTextInputState by lazy {
        DataInputState(true, null, R.string.add_word_required)
    }

    private val _genderStateFlow by lazy { MutableStateFlow(initWordGenderTextInputState) }
    private val _wordStateFlow by lazy { MutableStateFlow(initWordTextInputState) }
    private val _translationStateFlow by lazy { MutableStateFlow(initTranslationTextInputState) }
    private val _wordPluralFormStateFlow by lazy { MutableStateFlow(initPluralFormTextInputState) }
    private val _verbFormsVisibilityStatusFlow by lazy { MutableStateFlow(View.GONE) }
    private val _verbFormsStatusFlow by lazy { MutableStateFlow(VerbFormStatus()) }
    private val _onlyPluralSwitchVisibilityStatusFlow by lazy { MutableStateFlow(View.VISIBLE) }
    private val _saveButtonIsEnabledFlow by lazy { MutableStateFlow(false) }
    private val _clearAllInputStatusFlow by lazy { Channel<Unit>() }
    private val _adjectiveFormsVisibilityStatusFlow by lazy { MutableStateFlow(View.GONE) }

    private val isSaveEnabled: Boolean
        get() {
            return when (wordType) {
                NOUN -> (isOnlyPlural && wordPluralForm.isNotBlank() && translation.isNotBlank()) ||
                        (wordGender != null && word.isNotBlank() && translation.isNotBlank())
                VERB -> word.isNotBlank() && translation.isNotBlank()
                ADJECTIVE -> word.isNotBlank() && translation.isNotBlank()
            }
        }

    val nounGenders = WordGender.values().map { it.toString().lowercase() }

    val genderStatusFlow by lazy { _genderStateFlow.asStateFlow() }
    val wordStatusFlow by lazy { _wordStateFlow.asStateFlow() }
    val translationStatusFlow by lazy { _translationStateFlow.asStateFlow() }
    val wordPluralFormStatusFlow by lazy { _wordPluralFormStateFlow.asStateFlow() }
    val verbFormsVisibilityStatusFlow by lazy { _verbFormsVisibilityStatusFlow.asStateFlow() }
    val verbFormsStatusFlow by lazy { _verbFormsStatusFlow.asStateFlow() }
    val onlyPluralSwitchVisibilityStatusFlow by lazy { _onlyPluralSwitchVisibilityStatusFlow.asStateFlow() }
    val saveButtonIsEnabledFlow by lazy { _saveButtonIsEnabledFlow.asStateFlow() }
    val clearAllInputStatusFlow by lazy { _clearAllInputStatusFlow.receiveAsFlow() }
    val adjectiveFormsVisibilityStatusFlow by lazy { _adjectiveFormsVisibilityStatusFlow.asStateFlow() }

    private fun updateSaveButtonStatus() {
        Timber.i("updateSaveButtonStatus()")

        _saveButtonIsEnabledFlow.value = isSaveEnabled
    }

    private fun processVerbForms(word: String): VerbFormStatus {
        val prefix = Prefix.all.find { word.startsWith(it) } ?: ""
        val wordWithoutPrefix = word.removePrefix(prefix)
        val wordRootForm = wordWithoutPrefix.removeSuffixEn()
        val endingTransformer = wordEndingTransformer(wordRootForm)

        return VerbFormStatus(
            "$prefix$wordRootForm${endingTransformer(Person.ICH)}",
            "$prefix$wordRootForm${endingTransformer(Person.DU)}",
            "$prefix$wordRootForm${endingTransformer(Person.MAN)}",
            "$prefix$wordRootForm${endingTransformer(Person.WIR)}",
            "$prefix$wordRootForm${endingTransformer(Person.IHR)}",
            "$prefix$wordRootForm${endingTransformer(Person.SIE)}",
        )
    }

    private fun wordEndingTransformer(word: String): (Person) -> String = when {
        word.endsWith(Endings.t) -> ::prasensEndingForTD
        word.endsWith(Endings.d) -> ::prasensEndingForTD
        word.endsWith(Endings.m) -> ::prasensEndingForMN
        word.endsWith(Endings.n) -> ::prasensEndingForMN
        word.endsWith(Endings.s) -> ::prasensEndingForSSsZ
        word.endsWith(Endings.ss) -> ::prasensEndingForSSsZ
        word.endsWith(Endings.z) -> ::prasensEndingForSSsZ
        word.endsWith(Endings.er) -> ::prasensEndingForErEl
        word.endsWith(Endings.el) -> ::prasensEndingForErEl
        else -> ::prasensEndingForUndefined
    }

    private fun prasensEndingForTD(person: Person): String = when (person) {
        Person.ICH -> "e"
        Person.DU -> "est"
        Person.MAN -> "et"
        Person.WIR -> "en"
        Person.IHR -> "et"
        Person.SIE -> "en"
    }.also {
        Timber.i("prasensEndingForTD was used")
    }

    private fun prasensEndingForMN(person: Person): String = when (person) {
        Person.ICH -> "e"
        Person.DU -> "est"
        Person.MAN -> "et"
        Person.WIR -> "en"
        Person.IHR -> "et"
        Person.SIE -> "en"
    }.also {
        Timber.i("prasensEndingForMN was used")
    }

    private fun prasensEndingForSSsZ(person: Person): String = when (person) {
        Person.ICH -> "e"
        Person.DU -> "t"
        Person.MAN -> "t"
        Person.WIR -> "en"
        Person.IHR -> "t"
        Person.SIE -> "en"
    }.also {
        Timber.i("prasensEndingForSSsZ was used")
    }

    private fun prasensEndingForErEl(person: Person): String = when (person) {
        Person.ICH -> "e"
        Person.DU -> "st"
        Person.MAN -> "t"
        Person.WIR -> "n"
        Person.IHR -> "t"
        Person.SIE -> "n"
    }.also {
        Timber.i("prasensEndingForErEl was used")
    }

    private fun prasensEndingForUndefined(person: Person): String = when (person) {
        Person.ICH -> "e"
        Person.DU -> "st"
        Person.MAN -> "t"
        Person.WIR -> "en"
        Person.IHR -> "t"
        Person.SIE -> "en"
    }

    fun onGenderChange(gender: String) {
        Timber.i("onGenderChange(gender:$gender)")

        if (gender.lowercase() in nounGenders) {
            wordGender = WordGender.valueOf(gender.uppercase())
            _genderStateFlow.value =
                DataInputState(true, null, R.string.add_word_required)
        } else {
            wordGender = null
            _genderStateFlow.value =
                DataInputState(true, R.string.input_error_no_gender, R.string.add_word_not_required)
        }

        updateSaveButtonStatus()
    }

    fun onWordChange(newWord: String) {
        Timber.i("onWordChange(newWord:$newWord)")

        when (wordType) {
            NOUN -> {

                word = newWord.capitalize(Locale.current)

                if (newWord.isNotBlank() && !isOnlyPlural) {
                    _wordStateFlow.value = DataInputState(true, null, R.string.add_word_required)
                } else {
                    _wordStateFlow.value = DataInputState(
                        true,
                        R.string.input_error_word_must_not_be_empty,
                        R.string.add_word_not_required
                    )
                }
            }
            VERB -> {

                word = newWord.lowercase()

                if (word.isNotBlank()) {
                    _wordStateFlow.value = DataInputState(true, null, R.string.add_word_required)
                    _verbFormsStatusFlow.value = processVerbForms(word)
                } else {
                    _wordStateFlow.value = DataInputState(
                        true,
                        R.string.input_error_word_must_not_be_empty,
                        R.string.add_word_not_required
                    )
                }
            }
            ADJECTIVE -> {
                word = newWord.capitalize(Locale.current)

                if (word.isBlank()) {
                    _wordStateFlow.value = DataInputState(
                        true,
                        R.string.input_error_word_must_not_be_empty,
                        R.string.add_word_not_required
                    )
                } else {
                    _wordStateFlow.value = DataInputState(
                        true,
                        null,
                        R.string.add_word_required
                    )
                }
            }
        }



        updateSaveButtonStatus()
    }

    fun onWordTypeNounChange(isChecked: Boolean) {
        Timber.i("onWordTypeNounChange(isChecked:$isChecked)")

        if (isChecked) {
            wordType = NOUN
            _genderStateFlow.value = DataInputState(
                true,
                if (wordGender == null) R.string.input_error_no_gender else null,
                if (wordGender == null) R.string.add_word_not_required else R.string.add_word_required
            )
            _wordPluralFormStateFlow.value =
                DataInputState(true, null, R.string.add_word_not_required)
            _verbFormsVisibilityStatusFlow.value = View.GONE
            _adjectiveFormsVisibilityStatusFlow.value = View.GONE
            _onlyPluralSwitchVisibilityStatusFlow.value = View.VISIBLE

            updateSaveButtonStatus()
        }
    }

    fun onWordTypeVerbChange(isChecked: Boolean) {
        Timber.i("onWordTypeVerbChange(isChecked:$isChecked)")

        if (isChecked) {
            wordType = VERB
            _wordStateFlow.value =
                DataInputState(
                    true,
                    if (word.isBlank()) R.string.input_error_word_must_not_be_empty else null,
                    if (word.isBlank()) R.string.add_word_not_required else R.string.add_word_required
                )
            _genderStateFlow.value =
                DataInputState(false, null, R.string.add_word_not_required, View.GONE)
            _wordPluralFormStateFlow.value =
                DataInputState(false, null, R.string.add_word_not_required, View.GONE)
            _verbFormsVisibilityStatusFlow.value = View.VISIBLE
            _adjectiveFormsVisibilityStatusFlow.value = View.GONE
            _onlyPluralSwitchVisibilityStatusFlow.value = View.GONE
            updateSaveButtonStatus()
        }
    }

    fun onWordTypeAdjectiveChange(isChecked: Boolean) {
        Timber.i("onWordTypeAdjectiveChange(isChecked:$isChecked)")

        if (isChecked) {
            wordType = ADJECTIVE
            _genderStateFlow.value =
                DataInputState(false, null, R.string.add_word_not_required, View.GONE)
            _wordPluralFormStateFlow.value =
                DataInputState(false, null, R.string.add_word_not_required, View.GONE)
            _adjectiveFormsVisibilityStatusFlow.value = View.VISIBLE
            _verbFormsVisibilityStatusFlow.value = View.GONE
            _onlyPluralSwitchVisibilityStatusFlow.value = View.GONE
            updateSaveButtonStatus()
        }
    }

    fun onWordPluralFormChange(newWordPluralForm: String) {
        Timber.i("onWordPluralFormChange(newWordPluralForm:$newWordPluralForm)")

        wordPluralForm = newWordPluralForm

        when {
            isOnlyPlural && newWordPluralForm.isBlank() -> {
                _wordPluralFormStateFlow.value = DataInputState(
                    true,
                    R.string.input_error_word_must_have_plural,
                    R.string.add_word_not_required
                )
            }
            isOnlyPlural -> {
                wordPluralForm = newWordPluralForm
                _wordPluralFormStateFlow.value = DataInputState(
                    true, null, R.string.add_word_required
                )
            }
            else -> {
                _wordPluralFormStateFlow.value = DataInputState(
                    true, null, R.string.add_word_not_required
                )
            }
        }

        updateSaveButtonStatus()
    }

    fun onOnlyPluralChange(isChecked: Boolean) {
        Timber.i("onOnlyPluralChange(isChecked:$isChecked)")

        isOnlyPlural = isChecked

        _wordStateFlow.value = if (isOnlyPlural) {
            DataInputState(false, null, R.string.add_word_not_required)
        } else {
            if (word.isBlank()) {
                DataInputState(
                    true,
                    R.string.input_error_word_must_not_be_empty,
                    R.string.add_word_not_required
                )
            } else {
                DataInputState(true, null, R.string.add_word_required)
            }
        }

        _genderStateFlow.value = if (isOnlyPlural) {
            DataInputState(false, null, R.string.add_word_not_required)
        } else {
            if (wordGender == null) {
                DataInputState(
                    true,
                    R.string.input_error_word_must_not_be_empty,
                    R.string.add_word_not_required
                )
            } else {
                DataInputState(true, null, R.string.add_word_required)
            }
        }

        _wordPluralFormStateFlow.value = when {
            isOnlyPlural && wordPluralForm.isBlank() -> DataInputState(
                true, R.string.input_error_word_must_have_plural, R.string.add_word_not_required
            )
            isOnlyPlural -> DataInputState(
                true, null, R.string.add_word_required
            )
            else -> DataInputState(
                true, null, R.string.add_word_not_required
            )
        }

        updateSaveButtonStatus()
    }

    fun onTranslationChange(newTranslation: String) {
        Timber.i("onTranslationChange(newTranslation:$newTranslation)")

        translation = newTranslation
        if (newTranslation.isNotBlank()) {
            _translationStateFlow.value = DataInputState(
                true,
                null,
                R.string.add_word_required
            )
        } else {
            _translationStateFlow.value = DataInputState(
                true,
                R.string.input_error_word_must_have_translation,
                R.string.add_word_not_required
            )
        }

        updateSaveButtonStatus()
    }

    fun onVerbFormChange(_form: String?, verbFormType: VerbFormType) {
        Timber.i("onVerbFormChange(form:\"$_form\", verbFormType:$verbFormType)")

        val form = _form
            ?.lowercase(java.util.Locale.GERMAN)

        when (verbFormType) {
            VerbFormType.PRASENS_ICH -> verbFormHelper.prasensIch = form
            VerbFormType.PRASENS_DU -> verbFormHelper.prasensDu = form
            VerbFormType.PRASENS_ER_SIE_ES -> verbFormHelper.prasensErSieEs = form
            VerbFormType.PRASENS_WIR -> verbFormHelper.prasensWir = form
            VerbFormType.PRASENS_IHR -> verbFormHelper.prasensIhr = form
            VerbFormType.PRASENS_SIE_SIE -> verbFormHelper.prasensSieSie = form
            VerbFormType.PRATERITUM_ICH -> verbFormHelper.prateritumIch = form
            VerbFormType.PRATERITUM_DU -> verbFormHelper.prateritumDu = form
            VerbFormType.PRATERITUM_ER_SIE_ES -> verbFormHelper.prateritumErSieEs = form
            VerbFormType.PRATERITUM_WIR -> verbFormHelper.prateritumWir = form
            VerbFormType.PRATERITUM_IHR -> verbFormHelper.prateritumIhr = form
            VerbFormType.PRATERITUM_SIE_SIE -> verbFormHelper.prateritumSieSie = form
            VerbFormType.PERFEKT -> verbFormHelper.perfekt = form
        }
    }

    fun onKomparativChange(newKomparativ: String?) {
        Timber.i("onKomparativChange(newKomparativ:\"$newKomparativ\")")

        komparativ = newKomparativ ?: ""
    }

    fun onSuperlativChange(newSuperlativ: String?) {
        Timber.i("newSuperlativ(newSuperlativ:\"$newSuperlativ\")")

        superlativ = newSuperlativ ?: ""
    }

    fun onButtonSaveClick() {
        Timber.i("onButtonSaveClick()")

        _saveButtonIsEnabledFlow.value = false

        viewModelScope.launch {
            _saveButtonIsEnabledFlow.value = false

            when (wordType) {
                NOUN -> if (isOnlyPlural) {
                    repository.saveWordPlural(
                        wordPluralForm.asGermanNounString(),
                        translation.asNounString()
                    )
                } else {
                    repository.saveWord(
                        wordGender!!,
                        word.asGermanNounString(),
                        wordPluralForm.asGermanNounString(),
                        translation.asNounString()
                    )
                }
                VERB -> repository.saveWordVerb(
                    word.lowercaseGerman(),
                    translation.asNounString(),
                    verbFormHelper
                )
                ADJECTIVE -> repository.saveWordAdjective(
                    word.lowercaseGerman(),
                    translation.lowercase(),
                    komparativ.lowercaseGerman(),
                    superlativ.lowercaseGerman()
                )
            }

            clearInput()
        }
    }

    private suspend fun clearInput() {
        Timber.i("clearInput()")

        _clearAllInputStatusFlow.send(Unit)
        _genderStateFlow.value = initWordGenderTextInputState
        _wordStateFlow.value = initWordTextInputState
        _translationStateFlow.value = initTranslationTextInputState
        _wordPluralFormStateFlow.value = initPluralFormTextInputState
        _verbFormsVisibilityStatusFlow.value = View.GONE
        _adjectiveFormsVisibilityStatusFlow.value = View.GONE
        _onlyPluralSwitchVisibilityStatusFlow.value = View.VISIBLE
    }

    data class VerbFormStatus(
        val prasensIch: String = "",
        val prasensDu: String = "",
        val prasensErSieEs: String = "",
        val prasensWir: String = "",
        val prasensIhr: String = "",
        val prasensSieSie: String = "",

        val prateritumIch: String = "",
        val prateritumDu: String = "",
        val prateritumErSieEs: String = "",
        val prateritumWir: String = "",
        val prateritumIhr: String = "",
        val prateritumSieSie: String = "",

        val perfekt: String = ""
    )

    private fun String.removeSuffixEn(): String = if (this.removeSuffix(Suffix.en) != this) {
        this.removeSuffix(Suffix.en)
    } else {
        this.removeSuffix(Suffix.n)
    }

    private fun String.asGermanNounString(): String = this
        .lowercase(java.util.Locale.GERMAN)
        .replaceFirstChar { it.uppercase() }

    private fun String.asNounString(): String = this
        .lowercase()
        .replaceFirstChar { it.uppercase() }

    private fun String.lowercaseGerman(): String = this.lowercase(java.util.Locale.GERMAN)
}