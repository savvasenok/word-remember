package xyz.savvamirzoyan.wordremember.contract.adapter

import xyz.savvamirzoyan.wordremember.data.entity.ui.WordsListItemUI

interface IWordsListRecyclerViewAdapter {
    fun updateWords(newWords: List<WordsListItemUI>)
}