package xyz.savvamirzoyan.wordremember.contract.data

import xyz.savvamirzoyan.wordremember.core.Abstract
import xyz.savvamirzoyan.wordremember.data.entity.ui.WordsListItemUI

interface IAdjectiveWordDomainToUiMapper : Abstract.Mapper {

    fun map(
        word: String,
        translation: String,
        komparativ: String,
        superlativ: String
    ): WordsListItemUI.WordsListItemAdjectiveUI

    fun map(
        e: Exception
    ): WordsListItemUI.WordsListItemAdjectiveUI

    class Base : IAdjectiveWordDomainToUiMapper {
        override fun map(
            word: String,
            translation: String,
            komparativ: String,
            superlativ: String
        ): WordsListItemUI.WordsListItemAdjectiveUI = try {
            WordsListItemUI.WordsListItemAdjectiveUI.Success(
                word,
                translation,
                komparativ,
                superlativ
            )
        } catch (e: Exception) {
            WordsListItemUI.WordsListItemAdjectiveUI.Fail(e)
        }

        override fun map(e: Exception): WordsListItemUI.WordsListItemAdjectiveUI {
            return WordsListItemUI.WordsListItemAdjectiveUI.Fail(e)
        }
    }
}