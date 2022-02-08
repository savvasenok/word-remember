package xyz.savvamirzoyan.wordremember.contract.adapter

import xyz.savvamirzoyan.wordremember.presentation.model.WordsListItemUI

interface IWordsListRecyclerViewAdapter {
    fun updateWords(newWords: List<WordsListItemUI>)
}