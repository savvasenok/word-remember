package xyz.savvamirzoyan.wordremember.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.contract.repository.IAddWordRepository
import xyz.savvamirzoyan.wordremember.data.entity.VerbForm
import xyz.savvamirzoyan.wordremember.data.state.DataInputState
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
    private var translation: String = ""

    private val initWordGenderTextInputState =
        DataInputState(true, null, R.string.add_word_required)
    private val initWordTextInputState =
        DataInputState(true, null, R.string.add_word_required)
    private val initPluralFormTextInputState =
        DataInputState(true, null, R.string.add_word_not_required)
    private val initTranslationTextInputState =
        DataInputState(true, null, R.string.add_word_required)

    private val _genderStateFlow = MutableStateFlow(initWordGenderTextInputState)
    private val _wordStateFlow = MutableStateFlow(initWordTextInputState)
    private val _translationStateFlow = MutableStateFlow(initTranslationTextInputState)
    private val _wordPluralFormStateFlow = MutableStateFlow(initPluralFormTextInputState)
    private val _verbFormsVisibilityStatusFlow = MutableStateFlow(View.GONE)
    private val _onlyPluralSwitchVisibilityStatusFlow = MutableStateFlow(View.VISIBLE)
    private val _saveButtonIsEnabledFlow = MutableStateFlow(false)

    private val isSaveEnabled: Boolean
        get() {
            return when (wordType) {
                NOUN -> (isOnlyPlural && wordPluralForm.isNotBlank() && translation.isNotBlank()) ||
                        (wordGender != null && word.isNotBlank() && translation.isNotBlank())
                VERB -> word.isNotBlank() && translation.isNotBlank()
                ADJECTIVE -> word.isNotBlank() && translation.isNotBlank()
            }
        }

    val nounGenders = WordGender.values().map { it.toString().uppercase() }

    val genderStatusFlow = _genderStateFlow.asStateFlow()
    val wordStatusFlow = _wordStateFlow.asStateFlow()
    val translationStatusFlow = _translationStateFlow.asStateFlow()
    val wordPluralFormStatusFlow = _wordPluralFormStateFlow.asStateFlow()
    val verbFormsVisibilityStatusFlow = _verbFormsVisibilityStatusFlow.asStateFlow()
    val onlyPluralSwitchVisibilityStatusFlow = _onlyPluralSwitchVisibilityStatusFlow.asStateFlow()
    val saveButtonIsEnabledFlow = _saveButtonIsEnabledFlow.asStateFlow()

    private fun updateSaveButtonStatus() {
        Timber.i("updateSaveButtonStatus()")

        _saveButtonIsEnabledFlow.value = isSaveEnabled
    }

    fun onGenderChange(gender: String) {
        Timber.i("onGenderChange(gender:$gender)")

        if (gender.uppercase() in nounGenders) {
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

        word = newWord
        if (newWord.isNotBlank() && !isOnlyPlural) {
            _wordStateFlow.value = DataInputState(true, null, R.string.add_word_required)
        } else {
            _wordStateFlow.value = DataInputState(
                true,
                R.string.input_error_word_must_not_be_empty,
                R.string.add_word_not_required
            )
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

    fun onButtonSaveClick() {
        Timber.i("onButtonSaveClick()")

        _saveButtonIsEnabledFlow.value = false

        viewModelScope.launch {
            _saveButtonIsEnabledFlow.emit(false)

            when (wordType) {
                NOUN -> if (isOnlyPlural) {
                    repository.saveWordPlural(wordPluralForm, translation)
                } else {
                    repository.saveWord(wordGender!!, word, wordPluralForm, translation)
                }
                VERB -> repository.saveWordVerb(word, VerbForm.Prasens(), translation)
                ADJECTIVE -> repository.saveWordAdjective(word, translation)
            }

            _saveButtonIsEnabledFlow.emit(true)
        }
    }
}