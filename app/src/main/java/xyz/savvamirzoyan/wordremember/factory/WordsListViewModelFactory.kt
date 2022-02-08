package xyz.savvamirzoyan.wordremember.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.savvamirzoyan.wordremember.domain.interactors.WordsListInteractor
import xyz.savvamirzoyan.wordremember.presentation.viewmodel.WordsListViewModel

class WordsListViewModelFactory(
    private val interactor: WordsListInteractor,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WordsListViewModel(interactor) as T
    }
}