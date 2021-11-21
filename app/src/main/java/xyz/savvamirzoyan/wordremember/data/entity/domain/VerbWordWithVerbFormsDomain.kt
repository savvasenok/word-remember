package xyz.savvamirzoyan.wordremember.data.entity.domain

import xyz.savvamirzoyan.wordremember.contract.data.IVerbWordWithVerbFormsDomainToUiMapper
import xyz.savvamirzoyan.wordremember.core.Abstract
import xyz.savvamirzoyan.wordremember.data.entity.ui.WordsListItemUI

sealed class VerbWordWithVerbFormsDomain :
    Abstract.Object<WordsListItemUI.WordsListItemVerbUI, IVerbWordWithVerbFormsDomainToUiMapper> {

    class Success(
        private val word: String,
        private val translation: String,
        private val prasensIch: String?,
        private val prasensDu: String?,
        private val prasensErSieEs: String?,
        private val prasensWir: String?,
        private val prasensIhr: String?,
        private val prasensSieSie: String?,
        private val prateritumIch: String?,
        private val prateritumDu: String?,
        private val prateritumErSieEs: String?,
        private val prateritumWir: String?,
        private val prateritumIhr: String?,
        private val prateritumSieSie: String?,
        private val perfekt: String?
    ) : VerbWordWithVerbFormsDomain() {
        override fun map(mapper: IVerbWordWithVerbFormsDomainToUiMapper): WordsListItemUI.WordsListItemVerbUI {
            return try {
                WordsListItemUI.WordsListItemVerbUI.Success(
                    word, translation, prateritumSieSie ?: "", perfekt ?: ""
                )
            } catch (e: Exception) {
                WordsListItemUI.WordsListItemVerbUI.Fail(e)
            }
        }
    }

    class Fail(private val e: Exception) : VerbWordWithVerbFormsDomain() {
        override fun map(mapper: IVerbWordWithVerbFormsDomainToUiMapper): WordsListItemUI.WordsListItemVerbUI {
            return WordsListItemUI.WordsListItemVerbUI.Fail(e)
        }
    }
}