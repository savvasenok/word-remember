package xyz.savvamirzoyan.wordremember.data.entity.domain

import xyz.savvamirzoyan.wordremember.contract.data.INounWordDomainToUiMapper
import xyz.savvamirzoyan.wordremember.core.Abstract
import xyz.savvamirzoyan.wordremember.data.entity.ui.WordsListItemUI
import xyz.savvamirzoyan.wordremember.data.types.WordGender

sealed class NounWordDomain : Abstract.Object<WordsListItemUI, INounWordDomainToUiMapper> {

    data class Success(
        val gender: WordGender?,
        val word: String?,
        val plural: String?,
        val isOnlyPlural: Boolean,
        val translation: String
    ) : NounWordDomain() {
        override fun map(mapper: INounWordDomainToUiMapper): WordsListItemUI {
            return mapper.map(gender, word, plural, isOnlyPlural, translation)
        }
    }

    data class Fail(val e: Exception) : NounWordDomain() {
        override fun map(mapper: INounWordDomainToUiMapper): WordsListItemUI = mapper.map(e)
    }
}
