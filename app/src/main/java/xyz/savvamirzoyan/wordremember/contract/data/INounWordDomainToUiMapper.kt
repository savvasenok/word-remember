package xyz.savvamirzoyan.wordremember.contract.data

import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.core.Abstract
import xyz.savvamirzoyan.wordremember.data.entity.ui.WordsListItemUI
import xyz.savvamirzoyan.wordremember.data.types.WordGender

interface INounWordDomainToUiMapper : Abstract.Mapper {

    fun map(
        gender: WordGender?,
        word: String?,
        plural: String?,
        isOnlyPlural: Boolean,
        translation: String
    ): WordsListItemUI.WordsListItemNounUI

    fun map(e: Exception): WordsListItemUI.WordsListItemNounUI

    class Base : INounWordDomainToUiMapper {
        override fun map(
            gender: WordGender?,
            word: String?,
            plural: String?,
            isOnlyPlural: Boolean,
            translation: String
        ): WordsListItemUI.WordsListItemNounUI = try {

            val colorId = when (gender) {
                WordGender.DER -> R.color.gender_man
                WordGender.DIE -> R.color.gender_woman
                WordGender.DAS -> R.color.gender_it
                null -> R.color.gender_plural
            }

            WordsListItemUI.WordsListItemNounUI.Success(
                word ?: "",
                translation,
                gender.toString().lowercase(),
                colorId
            )
        } catch (e: Exception) {
            WordsListItemUI.WordsListItemNounUI.Fail(e)
        }

        override fun map(e: Exception): WordsListItemUI.WordsListItemNounUI {
            return WordsListItemUI.WordsListItemNounUI.Fail(e)
        }
    }
}