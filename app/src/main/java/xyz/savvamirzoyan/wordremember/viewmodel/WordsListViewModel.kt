package xyz.savvamirzoyan.wordremember.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.savvamirzoyan.wordremember.contract.repository.IWordsListRepository
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWord
import xyz.savvamirzoyan.wordremember.data.database.model.NounWord
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordWithVerbForms
import xyz.savvamirzoyan.wordremember.data.entity.WordsListItem

class WordsListViewModel(
    private val repository: IWordsListRepository
) : ViewModel() {

    private val _wordsListFlow by lazy { MutableStateFlow(listOf<WordsListItem>()) }

    val wordsListFlow by lazy { _wordsListFlow.asStateFlow() }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            receiveWordsFlow()
                .collect {
                    _wordsListFlow.emit(it)
                }
        }
    }

    private fun receiveWordsFlow() = combine(
        repository.wordsList,
        repository.verbsList,
        repository.adjectivesList
    ) { nouns, verbs, adjectives ->

        Timber.i("Got all 3!")

        nouns.map { it.toWordsListItem() } +
                verbs.map { it.toWordsListItem() } +
                adjectives.map { it.toWordsListItem() }
                    .sortedBy { it.word }
    }

    private fun NounWord.toWordsListItem(): WordsListItem {
        return WordsListItem(
            this.word ?: this.plural ?: "",
            this.translation
        )
    }

    private fun VerbWordWithVerbForms.toWordsListItem(): WordsListItem {
        return WordsListItem(
            verb.word,
            verb.translation
        )
    }

    private fun AdjectiveWord.toWordsListItem(): WordsListItem {
        return WordsListItem(
            word,
            translation
        )
    }
}