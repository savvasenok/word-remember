package xyz.savvamirzoyan.wordremember.domain.interactors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import xyz.savvamirzoyan.wordremember.contract.data.IAdjectiveWordDomainToUiMapper
import xyz.savvamirzoyan.wordremember.contract.data.INounWordDomainToUiMapper
import xyz.savvamirzoyan.wordremember.contract.data.IVerbWordWithVerbFormsDomainToUiMapper
import xyz.savvamirzoyan.wordremember.contract.interactor.IWordsListInteractor
import xyz.savvamirzoyan.wordremember.contract.repository.IWordsListRepository
import xyz.savvamirzoyan.wordremember.data.database.model.NounWordData
import xyz.savvamirzoyan.wordremember.data.entity.ui.WordsListItemUI
import xyz.savvamirzoyan.wordremember.data.types.WordGender

class WordsListInteractor(
    private val repository: IWordsListRepository
) : IWordsListInteractor {

    private val chars = ('A'..'Z') + ('a'..'z')

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

    override suspend fun addRandomWords() {
        val words = Array(100) {
            // TODO: Domain knows about Data layer?
            NounWordData(
                gender = listOf(WordGender.DER, WordGender.DIE, WordGender.DAS).random(),
                word = generateRandomString(),
                plural = generateRandomString(),
                isOnlyPlural = listOf(true, false).random(),
                translation = generateRandomString()
            )
        }.toList()

        repository.addRandomWords(words)
    }

    override suspend fun deleteAllWords() {
        repository.deleteAllWords()
    }

    private fun generateRandomString(): String {
        var string = ""
        repeat(10) { string += chars.random() }

        return string
    }
}