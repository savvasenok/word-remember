package xyz.savvamirzoyan.wordremember.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.savvamirzoyan.wordremember.contract.repository.IWordsListRepository
import xyz.savvamirzoyan.wordremember.contract.viewmodel.IViewModelDeleteSwipedItem
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWord
import xyz.savvamirzoyan.wordremember.data.database.model.NounWord
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordWithVerbForms
import xyz.savvamirzoyan.wordremember.data.entity.WordsListItem
import xyz.savvamirzoyan.wordremember.data.status.WordsListStatus
import xyz.savvamirzoyan.wordremember.extension.combine

class WordsListViewModel(
    private val repository: IWordsListRepository
) : ViewModel(), IViewModelDeleteSwipedItem {

    private val searchQueryFlow by lazy { MutableStateFlow("") }
    private val sortFlow by lazy { MutableStateFlow<SortOrder>(SortOrder.Alphabetical) }
    private val isReversedFlow by lazy { MutableStateFlow(false) }
    private val _wordsListStatusFlow by lazy { Channel<WordsListStatus>() }

    val wordsListStatusFlow by lazy { _wordsListStatusFlow.receiveAsFlow() }

    init {
        Timber.i("WordsListViewModel created")

        viewModelScope.launch(Dispatchers.IO) {
            receiveWordsFlow()
                .collect {
                    sendStatus(WordsListStatus.Words(it))
                }
        }
    }

    override fun deleteWord(wordsListItem: WordsListItem) {
        Timber.i("deleteWord(wordsListItem:$wordsListItem)")

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
                        repository.deleteVerb(it.verb.verbId)
                        repository.deleteVerbForm(it.verb.verbId)
                        _wordsListStatusFlow.send(WordsListStatus.ReturnBack.Verb(it))
                    }
                }
            }
        }
    }

    private fun sendStatus(status: WordsListStatus) {
        Timber.i("sendStatus(status:$status)")

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
        Timber.i("isReversed:$isReversed")

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

    private fun NounWord.toWordsListItem(): WordsListItem.WordsListItemNoun {
        return WordsListItem.WordsListItemNoun(
            id,
            word ?: plural ?: "",
            translation,
            gender
        )
    }

    private fun VerbWordWithVerbForms.toWordsListItem(): WordsListItem.WordsListItemVerb {
        return WordsListItem.WordsListItemVerb(
            verb.verbId, // only verbId, because verbForms id is the same
            verb.word,
            verb.translation,
            verbForm.prateritumSieSie ?: "",
            verbForm.perfekt ?: "",
        )
    }

    private fun AdjectiveWord.toWordsListItem(): WordsListItem.WordsListItemAdjective {
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
        Timber.i("performSearch(search:\"$search\")")

        viewModelScope.launch { searchQueryFlow.emit(search) }
    }

    fun returnNounToDB(nounWord: NounWord) {
        Timber.i("returnNounToDB(nounWord:$nounWord)")

        viewModelScope.launch(Dispatchers.IO) {
            repository.addNoun(nounWord)
        }
    }

    fun returnAdjectiveToDB(adjectiveWord: AdjectiveWord) {
        Timber.i("returnAdjectiveToDB(adjectiveWord:$adjectiveWord)")

        viewModelScope.launch(Dispatchers.IO) {
            repository.addAdjective(adjectiveWord)
        }
    }

    fun returnVerbToDB(verb: VerbWordWithVerbForms) {
        Timber.i("returnVerbToDB(verb:$verb)")

        viewModelScope.launch(Dispatchers.IO) {
            repository.addVerb(verb)
        }
    }

    fun reverseWordsUpdated(isChecked: Boolean) {
        Timber.i("reverseWordsUpdated(isChecked: $isChecked")

        isReversedFlow.value = isChecked
    }

    fun sortOptionUpdatedToAlphabetical() {
        Timber.i("sortOptionUpdatedToAlphabetical()")

        sortFlow.value = SortOrder.Alphabetical
    }

    fun sortOptionUpdatedToWordTypeOrGender() {
        Timber.i("sortOptionUpdatedToWordTypeOrGender()")

        sortFlow.value = SortOrder.WordTypeOrGender
    }

    fun addRandomWords() {
        Timber.i("addRandomWords()")

        viewModelScope.launch(Dispatchers.IO) {
            repository.addRandomWords()
        }
    }

    fun deleteAllWords() {
        Timber.i("deleteAllWords()")

        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllWords()
        }
    }

    private sealed class SortOrder {
        object Alphabetical : SortOrder()
        object WordTypeOrGender : SortOrder()
    }
}