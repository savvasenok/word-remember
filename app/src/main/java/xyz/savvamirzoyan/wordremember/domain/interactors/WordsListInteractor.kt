package xyz.savvamirzoyan.wordremember.domain.interactors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import xyz.savvamirzoyan.wordremember.contract.data.AdjectiveWordDomainToUiMapper
import xyz.savvamirzoyan.wordremember.contract.data.NounWordDomainToUiMapper
import xyz.savvamirzoyan.wordremember.contract.data.VerbWordWithVerbFormsDomainToNoFormsUiMapper
import xyz.savvamirzoyan.wordremember.data.database.model.NounWordData
import xyz.savvamirzoyan.wordremember.data.repository.WordsListRepository
import xyz.savvamirzoyan.wordremember.data.types.WordGender
import xyz.savvamirzoyan.wordremember.presentation.model.WordsListItemUI
import xyz.savvamirzoyan.wordremember.utils.constants.SortOrder

interface WordsListInteractor {
    suspend fun wordsFlow(
        sortOrderFlow: Flow<SortOrder>
    ): Flow<List<WordsListItemUI>>

    suspend fun addRandomWords()
    suspend fun deleteAllWords()

    class Base(
        private val repository: WordsListRepository,
        private val nounWordDomainToUiMapper: NounWordDomainToUiMapper,
        private val adjectiveWordDomainToUiMapper: AdjectiveWordDomainToUiMapper,
        private val verbWordWithVerbFormsDomainToNoFormsUiMapper: VerbWordWithVerbFormsDomainToNoFormsUiMapper
    ) : WordsListInteractor {

        private val chars = ('A'..'Z') + ('a'..'z')

        override suspend fun wordsFlow(
            sortOrderFlow: Flow<SortOrder>
        ): Flow<List<WordsListItemUI>> = combine(
            repository.wordsList,
            repository.verbsList,
            repository.adjectivesList,
            sortOrderFlow
        ) { nouns, verbs, adjectives, order ->
            val words = (nouns.map { nounWordDomainToUiMapper.map(it) } +
                    adjectives.map { adjectiveWordDomainToUiMapper.map(it) } +
                    verbs.map { verbWordWithVerbFormsDomainToNoFormsUiMapper.map(it) })

            when (order) {
                SortOrder.Alphabetical -> words.sortedBy { it.alphabeticalValue() }
                SortOrder.WordTypeOrGender -> words
            }
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
}