package xyz.savvamirzoyan.wordremember.contract.viewmodel

import xyz.savvamirzoyan.wordremember.presentation.model.WordsListItemUI

// TODO: rethink its usefulness
interface IViewModelDeleteSwipedItem {
    fun deleteWord(wordsListItemUI: WordsListItemUI)
}