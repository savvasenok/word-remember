package xyz.savvamirzoyan.wordremember.contract.adapter

import xyz.savvamirzoyan.wordremember.data.entity.WordsListItem

interface IWordsListRecyclerViewAdapter {
    fun updateWords(newWords: List<WordsListItem>)
}