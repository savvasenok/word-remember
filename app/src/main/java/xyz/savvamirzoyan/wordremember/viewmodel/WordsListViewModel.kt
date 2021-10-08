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

    private val searchQueryFlow by lazy { MutableStateFlow("") }
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
        repository.adjectivesList,
        searchQueryFlow
    ) { nouns, verbs, adjectives, searchQuery ->
        (nouns.map { it.toWordsListItem() } +
                verbs.map { it.toWordsListItem() } +
                adjectives.map { it.toWordsListItem() })
            .filter {
                it.word.search(searchQuery)
            }
    }

    private fun NounWord.toWordsListItem(): WordsListItem.WordsListItemNoun {
        return WordsListItem.WordsListItemNoun(
            this.word ?: this.plural ?: "",
            this.translation,
            this.gender
        )
    }

    private fun VerbWordWithVerbForms.toWordsListItem(): WordsListItem.WordsListItemVerb {
        return WordsListItem.WordsListItemVerb(
            verb.word,
            verb.translation,
            verbForm.prateritumSieSie ?: "",
            verbForm.perfekt ?: "",
        )
    }

    private fun AdjectiveWord.toWordsListItem(): WordsListItem.WordsListItemAdjective {
        return WordsListItem.WordsListItemAdjective(
            word,
            translation,
            komparativ,
            superlativ
        )
    }

    private fun String.search(query: String): Boolean {
        return this.lowercase().contains(query.lowercase())
    }

    fun performSearch(search: String) {
        Timber.i("performSearch(search:\"$search\")")

        viewModelScope.launch { searchQueryFlow.emit(search) }
    }
}