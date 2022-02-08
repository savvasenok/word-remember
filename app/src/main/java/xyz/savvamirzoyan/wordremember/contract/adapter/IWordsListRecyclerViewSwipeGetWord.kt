package xyz.savvamirzoyan.wordremember.contract.adapter

import xyz.savvamirzoyan.wordremember.presentation.model.WordsListItemUI

// TODO: rethink its usefulness
interface IWordsListRecyclerViewSwipeGetWord {
    fun getWordByPosition(position: Int): WordsListItemUI
}