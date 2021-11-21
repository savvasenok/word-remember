package xyz.savvamirzoyan.wordremember.contract.viewmodel

import xyz.savvamirzoyan.wordremember.data.entity.ui.WordsListItemUI

// TODO: rethink its usefulness
interface IViewModelDeleteSwipedItem {
    fun deleteWord(wordsListItemUI: WordsListItemUI)
}