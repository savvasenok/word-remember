package xyz.savvamirzoyan.wordremember.data.entity.domain

import xyz.savvamirzoyan.wordremember.contract.data.IAdjectiveWordDomainToUiMapper
import xyz.savvamirzoyan.wordremember.core.Abstract
import xyz.savvamirzoyan.wordremember.data.entity.ui.WordsListItemUI

sealed class AdjectiveWordDomain :
    Abstract.Object<WordsListItemUI.WordsListItemAdjectiveUI, IAdjectiveWordDomainToUiMapper> {

    data class Success(
        val word: String,
        val translation: String,
        val komparativ: String,
        val superlativ: String
    ) : AdjectiveWordDomain() {
        override fun map(mapper: IAdjectiveWordDomainToUiMapper): WordsListItemUI.WordsListItemAdjectiveUI {
            return mapper.map(word, translation, komparativ, superlativ)
        }
    }

    data class Fail(val e: Exception) : AdjectiveWordDomain() {
        override fun map(mapper: IAdjectiveWordDomainToUiMapper): WordsListItemUI.WordsListItemAdjectiveUI {
            return mapper.map(e)
        }
    }
}
