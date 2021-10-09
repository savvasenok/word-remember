package xyz.savvamirzoyan.wordremember.contract.adapter

import xyz.savvamirzoyan.wordremember.data.entity.WordsListItem

interface IWordsListRecyclerViewSwipeGetWord {

    fun getWordByPosition(position: Int): WordsListItem

}