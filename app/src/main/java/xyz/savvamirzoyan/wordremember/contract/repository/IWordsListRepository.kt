package xyz.savvamirzoyan.wordremember.contract.repository

import kotlinx.coroutines.flow.Flow
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWord
import xyz.savvamirzoyan.wordremember.data.database.model.NounWord
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordWithVerbForms

interface IWordsListRepository {

    val verbsList: Flow<List<VerbWordWithVerbForms>>
    val wordsList: Flow<List<NounWord>>
    val adjectivesList: Flow<List<AdjectiveWord>>

    suspend fun getNoun(nounId: Long): NounWord?
    suspend fun getVerb(verbId: Long): VerbWordWithVerbForms?
    suspend fun getAdjective(verbId: Long): AdjectiveWord?

    suspend fun deleteAdjective(id: Long)
    suspend fun deleteNoun(id: Long)
    suspend fun deleteVerb(id: Long)
    suspend fun deleteVerbForm(id: Long)

    suspend fun addNoun(nounWord: NounWord)
    suspend fun addVerb(verbForms: VerbWordWithVerbForms)
    suspend fun addAdjective(adjectiveWord: AdjectiveWord)

    suspend fun addRandomWords()
    suspend fun deleteAllWords()
}