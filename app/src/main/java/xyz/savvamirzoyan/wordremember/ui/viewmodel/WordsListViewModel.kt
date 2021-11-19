package xyz.savvamirzoyan.wordremember.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.wordremember.contract.repository.IWordsListRepository
import xyz.savvamirzoyan.wordremember.contract.viewmodel.IViewModelDeleteSwipedItem
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWordData
import xyz.savvamirzoyan.wordremember.data.database.model.NounWordData
import xyz.savvamirzoyan.wordremember.data.entity.VerbWordWithVerbFormsBusiness
import xyz.savvamirzoyan.wordremember.data.entity.WordsListItem
import xyz.savvamirzoyan.wordremember.data.status.WordsListStatus
import xyz.savvamirzoyan.wordremember.utils.extension.combine

class WordsListViewModel(
    private val repository: IWordsListRepository
) : ViewModel(), IViewModelDeleteSwipedItem {

    private val searchQueryFlow by lazy { MutableStateFlow("") }
    private val sortFlow by lazy { MutableStateFlow<SortOrder>(SortOrder.Alphabetical) }
    private val isReversedFlow by lazy { MutableStateFlow(false) }
    private val _wordsListStatusFlow by lazy { Channel<WordsListStatus>() }

    val wordsListStatusFlow by lazy { _wordsListStatusFlow.receiveAsFlow() }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            receiveWordsFlow()
                .collect {
                    sendStatus(WordsListStatus.Words(it))
                }
        }
    }

    override fun deleteWord(wordsListItem: WordsListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            when (wordsListItem) {
                is WordsListItem.WordsListItemAdjective -> {
                    repository.getAdjective(wordsListItem.id)?.let {
                        repository.deleteAdjective(it.id)
                        _wordsListStatusFlow.send(WordsListStatus.ReturnBack.Adjective(it))
                    }
                }
                is WordsListItem.WordsListItemNoun -> {
                    repository.getNoun(wordsListItem.id)?.let {
                        repository.deleteNoun(it.id)
                        _wordsListStatusFlow.send(WordsListStatus.ReturnBack.Noun(it))
                    }
                }
                is WordsListItem.WordsListItemVerb -> {
                    repository.getVerb(wordsListItem.id)?.let {
                        repository.deleteVerb(it.verbData.verbId)
                        repository.deleteVerbForm(it.verbData.verbId)
                        _wordsListStatusFlow.send(WordsListStatus.ReturnBack.Verb(it))
                    }
                }
            }
        }
    }

    private fun sendStatus(status: WordsListStatus) {
        viewModelScope.launch {
            _wordsListStatusFlow.send(status)
        }
    }

    private fun receiveWordsFlow() = combine(
        repository.wordsList,
        repository.verbsList,
        repository.adjectivesList,
        searchQueryFlow,
        sortFlow,
        isReversedFlow
    ) { nouns, verbs, adjectives, searchQuery, sortType, isReversed ->
        val list = when (sortType) {
            SortOrder.Alphabetical -> {
                (nouns.map { it.toWordsListItem() } +
                        verbs.map { it.toWordsListItem() } +
                        adjectives.map { it.toWordsListItem() })
                    .sortedBy { it.word }
                    .filter { it.word.search(searchQuery) }
            }
            SortOrder.WordTypeOrGender -> {
                (nouns
                    .map { it.toWordsListItem() }
                    .sortedWith(compareBy({ it.gender }, { it.word })) +
                        verbs.map { it.toWordsListItem() } +
                        adjectives.map { it.toWordsListItem() })
                    .filter { it.word.search(searchQuery) }
            }
        }

        if (isReversed) list.reversed() else list
    }

    private fun NounWordData.toWordsListItem(): WordsListItem.WordsListItemNoun {
        return WordsListItem.WordsListItemNoun(
            id,
            word ?: plural ?: "",
            translation,
            gender,
            plural
        )
    }

    private fun VerbWordWithVerbFormsBusiness.toWordsListItem(): WordsListItem.WordsListItemVerb {
        return WordsListItem.WordsListItemVerb(
            verbData.verbId, // only verbId, because verbForms id is the same
            verbData.word,
            verbData.translation,
            verbFormData
        )
    }

    private fun AdjectiveWordData.toWordsListItem(): WordsListItem.WordsListItemAdjective {
        return WordsListItem.WordsListItemAdjective(
            id,
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
        viewModelScope.launch { searchQueryFlow.emit(search) }
    }

    fun returnNounToDB(nounWordData: NounWordData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNoun(nounWordData)
        }
    }

    fun returnAdjectiveToDB(adjectiveWordData: AdjectiveWordData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAdjective(adjectiveWordData)
        }
    }

    fun returnVerbToDB(verb: VerbWordWithVerbFormsBusiness) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addVerb(verb)
        }
    }

    fun reverseWordsUpdated(isChecked: Boolean) {
        isReversedFlow.value = isChecked
    }

    fun sortOptionUpdatedToAlphabetical() {
        sortFlow.value = SortOrder.Alphabetical
    }

    fun sortOptionUpdatedToWordTypeOrGender() {
        sortFlow.value = SortOrder.WordTypeOrGender
    }

    fun addRandomWords() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRandomWords()
        }
    }

    fun deleteAllWords() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllWords()
        }
    }

    private sealed class SortOrder {
        object Alphabetical : SortOrder()
        object WordTypeOrGender : SortOrder()
    }
}