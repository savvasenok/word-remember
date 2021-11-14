package xyz.savvamirzoyan.wordremember.data.entity

import xyz.savvamirzoyan.wordremember.data.database.model.VerbForm
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
        val gender: WordGender?,
        val plural: String?,
        var isSubContentVisible: Boolean = false
    ) : WordsListItem(id, word, translation)

    class WordsListItemVerb(
        override val id: Long,
        override val word: String,
        override val translation: String,
        val verbForms: VerbForm,
        var isSubContentVisible: Boolean = false
    ) : WordsListItem(id, word, translation)

    class WordsListItemAdjective(
        override val id: Long,
        override val word: String,
        override val translation: String,
        val komparativ: String,
        val superlativ: String
    ) : WordsListItem(id, word, translation)

}
