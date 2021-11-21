package xyz.savvamirzoyan.wordremember.contract.data

import xyz.savvamirzoyan.wordremember.core.Abstract
import xyz.savvamirzoyan.wordremember.data.entity.ui.WordsListItemUI

interface IVerbWordWithVerbFormsDomainToUiMapper : Abstract.Mapper {

    fun map(
        word: String,
        translation: String,
        prateritum: String,
        perfekt: String
    ): WordsListItemUI.WordsListItemVerbUI

    fun map(e: Exception): WordsListItemUI.WordsListItemVerbUI

    class Base : IVerbWordWithVerbFormsDomainToUiMapper {
        override fun map(
            word: String,
            translation: String,
            prateritum: String,
            perfekt: String
        ): WordsListItemUI.WordsListItemVerbUI = try {
            WordsListItemUI.WordsListItemVerbUI.Success(
                word, translation, prateritum, perfekt
            )
        } catch (e: Exception) {
            WordsListItemUI.WordsListItemVerbUI.Fail(e)
        }

        override fun map(e: Exception): WordsListItemUI.WordsListItemVerbUI {
            return WordsListItemUI.WordsListItemVerbUI.Fail(e)
        }
    }

//    class Base : INounWordDomainToUiMapper {
//        override fun map(
//            gender: WordGender?,
//            word: String?,
//            plural: String?,
//            isOnlyPlural: Boolean,
//            translation: String
//        ): WordsListItemUI.WordsListItemNounUI = try {
//
//            val colorId = when (gender) {
//                WordGender.DER -> R.color.gender_man
//                WordGender.DIE -> R.color.gender_woman
//                WordGender.DAS -> R.color.gender_it
//                null -> R.color.gender_plural
//            }
//
//            WordsListItemUI.WordsListItemNounUI.Success(
//                word ?: "",
//                translation,
//                gender.toString().lowercase(),
//                colorId
//            )
//        } catch (e: Exception) {
//            WordsListItemUI.WordsListItemNounUI.Fail(e)
//        }
//
//        override fun map(e: Exception): WordsListItemUI.WordsListItemNounUI {
//            return WordsListItemUI.WordsListItemNounUI.Fail(e)
//        }
//    }
}