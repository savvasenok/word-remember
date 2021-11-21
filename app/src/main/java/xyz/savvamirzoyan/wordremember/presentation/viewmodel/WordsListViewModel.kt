package xyz.savvamirzoyan.wordremember.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.wordremember.contract.interactor.IWordsListInteractor
import xyz.savvamirzoyan.wordremember.contract.viewmodel.IViewModelDeleteSwipedItem
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWordData
import xyz.savvamirzoyan.wordremember.data.database.model.NounWordData
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordWithVerbFormsData
import xyz.savvamirzoyan.wordremember.data.entity.ui.WordsListItemUI
import xyz.savvamirzoyan.wordremember.data.status.WordsListStatus
import xyz.savvamirzoyan.wordremember.utils.constants.SortOrder

class WordsListViewModel(
    private val interactor: IWordsListInteractor
) : ViewModel(), IViewModelDeleteSwipedItem {

    private val searchQueryFlow by lazy { MutableStateFlow("") }
    private val sortOrderFlow by lazy { MutableStateFlow<SortOrder>(SortOrder.Alphabetical) }
    private val isReversedFlow by lazy { MutableStateFlow(false) }
    private val _wordsListStatusFlow by lazy { Channel<WordsListStatus>() }

    val wordsListStatusFlow by lazy { _wordsListStatusFlow.receiveAsFlow() }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.wordsFlow(
//                searchQueryFlow,
//                sortOrderFlow,
//                isReversedFlow
            ).collect {
                _wordsListStatusFlow.send(WordsListStatus.Words(it))
            }
        }
    }

    override fun deleteWord(wordsListItemUI: WordsListItemUI) {
        // TODO()
//        viewModelScope.launch(Dispatchers.IO) {
//            when (wordsListItemUI) {
//                is WordsListItemUI.WordsListItemAdjectiveUI -> {
//                    repository.getAdjective(wordsListItemUI.id)?.let {
//                        repository.deleteAdjective(it.id)
//                        _wordsListStatusFlow.send(WordsListStatus.ReturnBack.Adjective(it))
//                    }
//                }
//                is WordsListItemUI.WordsListItemNounUI -> {
//                    repository.getNoun(wordsListItemUI.id)?.let {
//                        repository.deleteNoun(it.id)
//                        _wordsListStatusFlow.send(WordsListStatus.ReturnBack.Noun(it))
//                    }
//                }
//                is WordsListItemUI.WordsListItemVerbUI -> {
//                    repository.getVerb(wordsListItemUI.id)?.let {
//                        repository.deleteVerb(it.verbData.verbId)
//                        repository.deleteVerbForm(it.verbData.verbId)
//                        _wordsListStatusFlow.send(WordsListStatus.ReturnBack.Verb(it))
//                    }
//                }
//            }
//        }
    }

//    private fun NounWordData.toWordsListItem(): WordsListItemUI.WordsListItemNounUI {
//        return WordsListItemUI.WordsListItemNounUI(
//            id,
//            word ?: plural ?: "",
//            translation,
//            gender,
//            plural
//        )
//    }
//
//    private fun VerbWordWithVerbFormsDomain.toWordsListItem(): WordsListItemUI.WordsListItemVerbUI {
//        return WordsListItemUI.WordsListItemVerbUI(
//            verbData.verbId, // only verbId, because verbForms id is the same
//            verbData.word,
//            verbData.translation,
//            verbFormData
//        )
//    }
//
//    private fun AdjectiveWordData.toWordsListItem(): WordsListItemUI.WordsListItemAdjectiveUI {
//        return WordsListItemUI.WordsListItemAdjectiveUI(
//            word,
//            translation,
//            komparativ,
//            superlativ
//        )
//    }

    private fun String.search(query: String): Boolean {
        return this.lowercase().contains(query.lowercase())
    }

    fun performSearch(search: String) {
        viewModelScope.launch { searchQueryFlow.emit(search) }
    }

    fun returnNounToDB(nounWordData: NounWordData) {
        viewModelScope.launch(Dispatchers.IO) {
//            TODO() interactor.addNoun(nounWordData)
        }
    }

    fun returnAdjectiveToDB(adjectiveWordData: AdjectiveWordData) {
        viewModelScope.launch(Dispatchers.IO) {
//            TODO() interactor.addAdjective(adjectiveWordData)
        }
    }

    fun returnVerbToDB(verb: VerbWordWithVerbFormsData) {
        viewModelScope.launch(Dispatchers.IO) {
//            TODO() interactor.addVerb(verb)
        }
    }

    fun reverseWordsUpdated(isChecked: Boolean) {
        isReversedFlow.value = isChecked
    }

    fun sortOptionUpdatedToAlphabetical() {
        sortOrderFlow.value = SortOrder.Alphabetical
    }

    fun sortOptionUpdatedToWordTypeOrGender() {
        sortOrderFlow.value = SortOrder.WordTypeOrGender
    }

    fun addRandomWords() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.addRandomWords()
        }
    }

    fun deleteAllWords() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.deleteAllWords()
        }
    }
}