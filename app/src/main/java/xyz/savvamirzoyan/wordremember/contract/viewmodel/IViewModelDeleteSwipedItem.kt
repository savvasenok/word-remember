package xyz.savvamirzoyan.wordremember.contract.viewmodel

import xyz.savvamirzoyan.wordremember.data.entity.WordsListItem

// TODO: rethink its usefulness
interface IViewModelDeleteSwipedItem {
    fun deleteWord(wordsListItem: WordsListItem)
}