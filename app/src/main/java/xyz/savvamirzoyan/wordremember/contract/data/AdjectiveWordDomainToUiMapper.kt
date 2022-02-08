package xyz.savvamirzoyan.wordremember.contract.data

import xyz.savvamirzoyan.wordremember.core.Mapper
import xyz.savvamirzoyan.wordremember.domain.model.AdjectiveWordDomain
import xyz.savvamirzoyan.wordremember.presentation.model.WordsListItemUI

private const val EMPTY_STRING = ""

class AdjectiveWordDomainToUiMapper : Mapper<AdjectiveWordDomain, WordsListItemUI.WordsListItemAdjectiveUI> {
    override fun map(data: AdjectiveWordDomain): WordsListItemUI.WordsListItemAdjectiveUI =
        WordsListItemUI.WordsListItemAdjectiveUI(
            data.word,
            data.translation,
            data.komparativ,
            if (data.superlativ.isEmpty()) EMPTY_STRING else "am ${data.superlativ}"
        )
}