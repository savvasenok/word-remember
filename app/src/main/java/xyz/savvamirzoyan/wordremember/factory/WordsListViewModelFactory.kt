package xyz.savvamirzoyan.wordremember.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.savvamirzoyan.wordremember.contract.repository.IWordsListRepository
import xyz.savvamirzoyan.wordremember.viewmodel.WordsListViewModel

class WordsListViewModelFactory(
    private val repository: IWordsListRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WordsListViewModel(repository) as T
    }
}