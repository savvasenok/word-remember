package xyz.savvamirzoyan.wordremember.domain.interactors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import xyz.savvamirzoyan.wordremember.contract.data.IAdjectiveWordDomainToUiMapper
import xyz.savvamirzoyan.wordremember.contract.data.INounWordDomainToUiMapper
import xyz.savvamirzoyan.wordremember.contract.data.IVerbWordWithVerbFormsDomainToUiMapper
import xyz.savvamirzoyan.wordremember.contract.interactor.IWordsListInteractor
import xyz.savvamirzoyan.wordremember.contract.repository.IWordsListRepository
import xyz.savvamirzoyan.wordremember.data.entity.ui.WordsListItemUI

class WordsListInteractor(
    private val repository: IWordsListRepository
) : IWordsListInteractor {

    override fun wordsFlow(
    ): Flow<List<WordsListItemUI>> = combine(
        repository.wordsList,
        repository.verbsList,
        repository.adjectivesList
    ) { nouns, verbs, adjectives ->
        nouns.map { it.map(INounWordDomainToUiMapper.Base()) } +
                adjectives.map { it.map(IAdjectiveWordDomainToUiMapper.Base()) } +
                verbs.map { it.map(IVerbWordWithVerbFormsDomainToUiMapper.Base()) }
    }
}