package xyz.savvamirzoyan.wordremember.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.contract.repository.ILearnWordRepository
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWordData
import xyz.savvamirzoyan.wordremember.data.database.model.NounWordData
import xyz.savvamirzoyan.wordremember.data.entity.QuestionAnswer
import xyz.savvamirzoyan.wordremember.data.entity.VerbWordWithVerbFormsBusiness
import xyz.savvamirzoyan.wordremember.data.status.LearnWordStatus
import kotlin.math.min

class LearnWordViewModel(
    private val repository: ILearnWordRepository
) : ViewModel() {

    private val nouns = runBlocking { repository.getAllNouns() }
    private val verbs = runBlocking { repository.getAllVerbs() }
    private val adjectives = runBlocking { repository.getAllAdjectives() }

    private val _learnWordStatusFlow: MutableStateFlow<LearnWordStatus?> by lazy {
        MutableStateFlow(
            null
        )
    }

    val learnWordStatusFlow by lazy { _learnWordStatusFlow.asStateFlow() }

    init {
        viewModelScope.launch {
            _learnWordStatusFlow.value = getRandomWord()
        }
    }

    private fun getRandomWord(): LearnWordStatus? {
        val random = (0..2).random()
        var response: LearnWordStatus? = null

        for (i in 0..2) {
            val tmp = learnWordStatusFromWords(
                when ((random + i) % 3) {
                    0 -> nouns.toList()//repository.getAllNouns()
                    1 -> verbs.toList()//repository.getAllVerbs()
                    else -> adjectives.toList()//repository.getAllAdjectives()
                }
            )

            if (tmp != null) {
                response = tmp
                break
            }
        }

        return response
    }

    private fun <T> MutableList<T>.getRandomItems(amount: Int): List<T> {
        return (0 until amount).map {
            this.removeAt((this.indices).random())
        }
    }

    private fun <T> learnWordStatusFromWords(items: List<T>): LearnWordStatus? {
        return if (items.size >= 2) {
            val words = (items as MutableList<T>).getRandomItems(min(items.size, 4))
            val questionWord = words.random()
            val answers = words.shuffled().map {

                when (it) {
                    is NounWordData -> QuestionAnswer(
                        it.translation,
                        it == questionWord
                    )
                    is VerbWordWithVerbFormsBusiness -> QuestionAnswer(
                        it.verbData.translation,
                        it == questionWord
                    )
                    is AdjectiveWordData -> QuestionAnswer(
                        it.translation,
                        it == questionWord
                    )
                    else -> throw RuntimeException()
                }
            }

            when (questionWord) {
                is NounWordData -> LearnWordStatus.TranslateFullWord(
                    R.string.learn_word_question_translate,
                    questionWord.wordWithGender() ?: questionWord.plural ?: "",
                    answers
                )
                is VerbWordWithVerbFormsBusiness -> LearnWordStatus.TranslateFullWord(
                    R.string.learn_word_question_translate,
                    questionWord.verbData.word,
                    answers
                )
                is AdjectiveWordData -> LearnWordStatus.TranslateFullWord(
                    R.string.learn_word_question_translate,
                    questionWord.word,
                    answers
                )
                else -> throw RuntimeException()
            }
        } else {
            null
        }
    }

    fun onAnswerButtonClick(isCorrect: Boolean) {
        if (isCorrect) {
            viewModelScope.launch {
                _learnWordStatusFlow.value = getRandomWord()
            }
        }
    }
}