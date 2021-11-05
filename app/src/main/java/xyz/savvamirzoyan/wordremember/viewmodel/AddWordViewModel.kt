package xyz.savvamirzoyan.wordremember.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.constants.Person
import xyz.savvamirzoyan.wordremember.contract.repository.IAddWordRepository
import xyz.savvamirzoyan.wordremember.data.entity.VerbFormHelper
import xyz.savvamirzoyan.wordremember.data.state.DataInputState
import xyz.savvamirzoyan.wordremember.data.state.VerbFormType
import xyz.savvamirzoyan.wordremember.data.status.AddWordStatus
import xyz.savvamirzoyan.wordremember.data.types.WordGender
import xyz.savvamirzoyan.wordremember.data.types.WordType
import xyz.savvamirzoyan.wordremember.data.types.WordType.*
import xyz.savvamirzoyan.wordremember.domain.VerbAnalyzer
import java.util.*

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

    private val _addWordStatusFlow by lazy { MutableStateFlow<AddWordStatus?>(null) }
    val addWordStatusFlow by lazy { _addWordStatusFlow.asStateFlow() }

    private fun updateSaveButtonStatus() {
        sendStatus(AddWordStatus.Repeatable.SaveButtonIsEnabled(isSaveEnabled))
    }

    private fun processVerbForms(word: String): VerbFormStatus {
        val prefix = VerbAnalyzer.prefix(word)
        val wordWithoutPrefix = VerbAnalyzer.removePrefix(word)
        val wordRootForm = VerbAnalyzer.removeSuffixEn(wordWithoutPrefix)

        return VerbFormStatus(
            VerbAnalyzer.Builder.prasens(prefix, wordRootForm, Person.ICH),
            VerbAnalyzer.Builder.prasens(prefix, wordRootForm, Person.DU),
            VerbAnalyzer.Builder.prasens(prefix, wordRootForm, Person.MAN),
            VerbAnalyzer.Builder.prasens(prefix, wordRootForm, Person.WIR),
            VerbAnalyzer.Builder.prasens(prefix, wordRootForm, Person.IHR),
            VerbAnalyzer.Builder.prasens(prefix, wordRootForm, Person.SIE)
        )
    }

    fun onGenderChange(gender: String) {
        if (gender.lowercase() in nounGenders) {
            wordGender = WordGender.valueOf(gender.uppercase())
            sendStatus(
                AddWordStatus.Repeatable.Gender(
                    DataInputState(
                        true,
                        null,
                        R.string.add_word_required
                    )
                )
            )
        } else {
            wordGender = null
            sendStatus(
                AddWordStatus.Repeatable.Gender(
                    DataInputState(
                        true,
                        R.string.input_error_no_gender,
                        R.string.add_word_not_required
                    )
                )
            )
        }

        updateSaveButtonStatus()
    }

    fun onWordChange(newWord: String) {
        when (wordType) {
            NOUN -> {

                word =
                    newWord.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

                if (newWord.isNotBlank() && !isOnlyPlural) {
                    sendStatus(
                        AddWordStatus.Repeatable.Word(
                            DataInputState(
                                true,
                                null,
                                R.string.add_word_required
                            )
                        )
                    )
                } else {
                    sendStatus(
                        AddWordStatus.Repeatable.Word(
                            DataInputState(
                                true,
                                R.string.input_error_word_must_not_be_empty,
                                R.string.add_word_not_required
                            )
                        )
                    )
                }
            }
            VERB -> {
                word = newWord.lowercase()

                if (word.isNotBlank()) {
                    sendStatus(
                        AddWordStatus.Repeatable.Word(
                            DataInputState(
                                true,
                                null,
                                R.string.add_word_required
                            )
                        )
                    )
                    sendStatus(AddWordStatus.Repeatable.VerbForms(processVerbForms(word)))
                } else {
                    sendStatus(
                        AddWordStatus.Repeatable.Word(
                            DataInputState(
                                true,
                                R.string.input_error_word_must_not_be_empty,
                                R.string.add_word_not_required
                            )
                        )
                    )
                }
            }
            ADJECTIVE -> {
                word =
                    newWord.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

                if (word.isBlank()) {
                    sendStatus(
                        AddWordStatus.Repeatable.Word(
                            DataInputState(
                                true,
                                R.string.input_error_word_must_not_be_empty,
                                R.string.add_word_not_required
                            )
                        )
                    )
                } else {
                    sendStatus(
                        AddWordStatus.Repeatable.Word(
                            DataInputState(
                                true,
                                null,
                                R.string.add_word_required
                            )
                        )
                    )
                }
            }
        }

        updateSaveButtonStatus()
    }

    fun onWordTypeNounChange(isChecked: Boolean) {
        if (isChecked) {
            wordType = NOUN
            sendStatus(
                AddWordStatus.Repeatable.Gender(
                    DataInputState(
                        true,
                        if (wordGender == null) R.string.input_error_no_gender else null,
                        if (wordGender == null) R.string.add_word_not_required else R.string.add_word_required
                    )
                )
            )
            sendStatus(
                AddWordStatus.Repeatable.Word(
                    DataInputState(
                        true,
                        null,
                        R.string.add_word_not_required
                    )
                )
            )
            sendStatus(AddWordStatus.Repeatable.VerbFormsVisibility(View.GONE))
            sendStatus(AddWordStatus.Repeatable.AdjectiveFormsVisibility(View.GONE))
            sendStatus(AddWordStatus.Repeatable.OnlyPluralSwitchVisibility(View.VISIBLE))

            updateSaveButtonStatus()
        }
    }

    fun onWordTypeVerbChange(isChecked: Boolean) {
        if (isChecked) {
            wordType = VERB
            sendStatus(
                AddWordStatus.Repeatable.Word(
                    DataInputState(
                        true,
                        if (word.isBlank()) R.string.input_error_word_must_not_be_empty else null,
                        if (word.isBlank()) R.string.add_word_not_required else R.string.add_word_required
                    )
                )
            )
            sendStatus(
                AddWordStatus.Repeatable.Gender(
                    DataInputState(
                        false,
                        null,
                        R.string.add_word_not_required,
                        View.GONE
                    )
                )
            )
            sendStatus(
                AddWordStatus.Repeatable.Plural(
                    DataInputState(
                        false,
                        null,
                        R.string.add_word_not_required,
                        View.GONE
                    )
                )
            )
            sendStatus(AddWordStatus.Repeatable.VerbFormsVisibility(View.VISIBLE))
            sendStatus(AddWordStatus.Repeatable.AdjectiveFormsVisibility(View.GONE))
            sendStatus(AddWordStatus.Repeatable.OnlyPluralSwitchVisibility(View.GONE))

            updateSaveButtonStatus()
        }
    }

    fun onWordTypeAdjectiveChange(isChecked: Boolean) {
        if (isChecked) {
            wordType = ADJECTIVE
            sendStatus(
                AddWordStatus.Repeatable.Gender(
                    DataInputState(false, null, R.string.add_word_not_required, View.GONE)
                )
            )
            sendStatus(
                AddWordStatus.Repeatable.Plural(
                    DataInputState(false, null, R.string.add_word_not_required, View.GONE)
                )
            )
            sendStatus(AddWordStatus.Repeatable.AdjectiveFormsVisibility(View.VISIBLE))
            sendStatus(AddWordStatus.Repeatable.VerbFormsVisibility(View.GONE))
            sendStatus(AddWordStatus.Repeatable.OnlyPluralSwitchVisibility(View.GONE))
            updateSaveButtonStatus()
        }
    }

    fun onWordPluralFormChange(newWordPluralForm: String) {
        wordPluralForm = newWordPluralForm

        when {
            isOnlyPlural && newWordPluralForm.isBlank() -> {
                sendStatus(
                    AddWordStatus.Repeatable.Plural(
                        DataInputState(
                            true,
                            R.string.input_error_word_must_have_plural,
                            R.string.add_word_not_required
                        )
                    )
                )
            }
            isOnlyPlural -> {
                wordPluralForm = newWordPluralForm
                sendStatus(
                    AddWordStatus.Repeatable.Plural(
                        DataInputState(
                            true, null, R.string.add_word_required
                        )
                    )
                )
            }
            else -> {
                sendStatus(
                    AddWordStatus.Repeatable.Plural(
                        DataInputState(
                            true, null, R.string.add_word_not_required
                        )
                    )
                )
            }
        }

        updateSaveButtonStatus()
    }

    fun onOnlyPluralChange(isChecked: Boolean) {
        isOnlyPlural = isChecked

        val wordState = if (isOnlyPlural) {
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

        val genderState = if (isOnlyPlural) {
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

        val pluralState = when {
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

        sendStatus(AddWordStatus.Repeatable.Word(wordState))
        sendStatus(AddWordStatus.Repeatable.Gender(genderState))
        sendStatus(AddWordStatus.Repeatable.Plural(pluralState))

        updateSaveButtonStatus()
    }

    fun onTranslationChange(newTranslation: String) {
        translation = newTranslation
        if (newTranslation.isNotBlank()) {
            sendStatus(
                AddWordStatus.Repeatable.Translation(
                    DataInputState(
                        true,
                        null,
                        R.string.add_word_required
                    )
                )
            )
        } else {
            sendStatus(
                AddWordStatus.Repeatable.Translation(
                    DataInputState(
                        true,
                        R.string.input_error_word_must_have_translation,
                        R.string.add_word_not_required
                    )
                )
            )
        }

        updateSaveButtonStatus()
    }

    fun onVerbFormChange(_form: String?, verbFormType: VerbFormType) {
        val form = _form?.lowercase(Locale.GERMAN)

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
        komparativ = newKomparativ ?: ""
    }

    fun onSuperlativChange(newSuperlativ: String?) {
        superlativ = newSuperlativ ?: ""
    }

    fun onButtonSaveClick() {
        sendStatus(AddWordStatus.Repeatable.SaveButtonIsEnabled(false))

        viewModelScope.launch {
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
                    translation.lowercaseGerman(),
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

    private fun clearInput() {
        sendStatus(AddWordStatus.Unrepeatable.ClearAllInput)
        sendStatus(AddWordStatus.Repeatable.Gender(initWordGenderTextInputState))
        sendStatus(AddWordStatus.Repeatable.Word(initWordTextInputState))
        sendStatus(AddWordStatus.Repeatable.Translation(initTranslationTextInputState))
        sendStatus(AddWordStatus.Repeatable.Plural(initPluralFormTextInputState))
        sendStatus(AddWordStatus.Repeatable.VerbFormsVisibility(View.GONE))
        sendStatus(AddWordStatus.Repeatable.AdjectiveFormsVisibility(View.GONE))
        sendStatus(AddWordStatus.Repeatable.OnlyPluralSwitchVisibility(View.VISIBLE))
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

    private fun String.asGermanNounString(): String = this
        .lowercase(Locale.GERMAN)
        .replaceFirstChar { it.uppercase() }

    private fun String.asNounString(): String = this
        .lowercase()
        .replaceFirstChar { it.uppercase() }

    private fun String.lowercaseGerman(): String = this.lowercase(Locale.GERMAN)

    private fun sendStatus(status: AddWordStatus) {
        viewModelScope.launch {
            _addWordStatusFlow.emit(status)
        }
    }
}