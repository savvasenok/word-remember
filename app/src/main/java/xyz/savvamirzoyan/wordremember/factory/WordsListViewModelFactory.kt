package xyz.savvamirzoyan.wordremember.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.savvamirzoyan.wordremember.contract.interactor.IWordsListInteractor
import xyz.savvamirzoyan.wordremember.presentation.viewmodel.WordsListViewModel

class WordsListViewModelFactory(
    private val interactor: IWordsListInteractor,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WordsListViewModel(interactor) as T
    }
}