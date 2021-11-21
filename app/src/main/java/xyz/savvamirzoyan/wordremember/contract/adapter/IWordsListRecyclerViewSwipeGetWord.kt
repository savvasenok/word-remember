package xyz.savvamirzoyan.wordremember.contract.adapter

import xyz.savvamirzoyan.wordremember.data.entity.ui.WordsListItemUI

// TODO: rethink its usefulness
interface IWordsListRecyclerViewSwipeGetWord {
    fun getWordByPosition(position: Int): WordsListItemUI
}