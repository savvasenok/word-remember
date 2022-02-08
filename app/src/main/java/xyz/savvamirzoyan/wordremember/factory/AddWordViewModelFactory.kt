package xyz.savvamirzoyan.wordremember.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.savvamirzoyan.wordremember.data.repository.IAddWordRepository
import xyz.savvamirzoyan.wordremember.presentation.viewmodel.AddWordViewModel

class AddWordViewModelFactory(
    private val repository: IAddWordRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddWordViewModel(repository) as T
    }
}