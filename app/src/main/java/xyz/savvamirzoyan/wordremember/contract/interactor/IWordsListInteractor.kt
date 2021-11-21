package xyz.savvamirzoyan.wordremember.contract.interactor

import kotlinx.coroutines.flow.Flow
import xyz.savvamirzoyan.wordremember.data.entity.ui.WordsListItemUI

interface IWordsListInteractor {
    fun wordsFlow(): Flow<List<WordsListItemUI>>
}