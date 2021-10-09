package xyz.savvamirzoyan.wordremember.data.entity

import xyz.savvamirzoyan.wordremember.data.types.WordGender

sealed class WordsListItem(
    open val id: Long,
    open val word: String,
    open val translation: String
) {

    class WordsListItemNoun(
        override val id: Long,
        override val word: String,
        override val translation: String,
        val gender: WordGender?
    ) : WordsListItem(id, word, translation)

    class WordsListItemVerb(
        override val id: Long,
        override val word: String,
        override val translation: String,
        val prateritum: String,
        val perfect: String
    ) : WordsListItem(id, word, translation)

    class WordsListItemAdjective(
        override val id: Long,
        override val word: String,
        override val translation: String,
        val komparativ: String,
        val superlativ: String
    ) : WordsListItem(id, word, translation)

}
