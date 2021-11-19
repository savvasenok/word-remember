package xyz.savvamirzoyan.wordremember.contract.adapter

import xyz.savvamirzoyan.wordremember.data.entity.WordsListItem

// TODO: rethink its usefulness
interface IWordsListRecyclerViewSwipeGetWord {
    fun getWordByPosition(position: Int): WordsListItem
}