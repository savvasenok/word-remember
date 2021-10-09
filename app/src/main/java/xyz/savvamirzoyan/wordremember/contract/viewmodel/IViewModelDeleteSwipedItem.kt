package xyz.savvamirzoyan.wordremember.contract.viewmodel

import xyz.savvamirzoyan.wordremember.data.entity.WordsListItem

interface IViewModelDeleteSwipedItem {

    fun deleteWord(wordsListItem: WordsListItem)

}