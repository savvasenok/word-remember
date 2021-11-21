package xyz.savvamirzoyan.wordremember.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.savvamirzoyan.wordremember.contract.repository.ILearnWordRepository
import xyz.savvamirzoyan.wordremember.presentation.viewmodel.LearnWordViewModel

class LearnWordViewModelFactory(private val repository: ILearnWordRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LearnWordViewModel(repository) as T
    }
}