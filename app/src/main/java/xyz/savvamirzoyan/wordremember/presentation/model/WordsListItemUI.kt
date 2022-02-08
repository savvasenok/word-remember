package xyz.savvamirzoyan.wordremember.presentation.model

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import xyz.savvamirzoyan.wordremember.data.database.model.VerbFormData

sealed class WordsListItemUI {

    abstract fun alphabeticalValue(): String

    data class WordsListItemAdjectiveUI(
        val word: String,
        val translation: String,
        val komparativ: String,
        val superlativ: String
    ) : WordsListItemUI() {
        override fun alphabeticalValue() = word
    }

    data class WordsListItemNounUI(
        val word: String,
        val translation: String,
        val gender: String,
        @ColorInt val color: Int
    ) : WordsListItemUI() {
        override fun alphabeticalValue() = word
    }

    data class WordsListItemNounWithPluralUI(
        val word: String,
        val translation: String,
        val gender: String,
        @ColorRes val colorId: Int,
        val plural: String
    ) : WordsListItemUI() {
        override fun alphabeticalValue() = word
    }

    data class WordsListItemVerbUI(
        val word: String,
        val translation: String,
        val prateritum: String,
        val perfekt: String
    ) : WordsListItemUI() {
        override fun alphabeticalValue() = word
    }

    data class WordsListItemVerbWithVerbFormsUI(
        val word: String,
        val translation: String,
        val prateritum: String,
        val perfekt: String,
        val verbFormsData: VerbFormData
    ) : WordsListItemUI() {
        override fun alphabeticalValue() = word
    }
}