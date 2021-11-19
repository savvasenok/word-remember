package xyz.savvamirzoyan.wordremember.contract.repository

import kotlinx.coroutines.flow.Flow
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWordData
import xyz.savvamirzoyan.wordremember.data.database.model.NounWordData
import xyz.savvamirzoyan.wordremember.data.entity.VerbWordWithVerbFormsBusiness

interface IWordsListRepository {

    val verbsList: Flow<List<VerbWordWithVerbFormsBusiness>>
    val wordsList: Flow<List<NounWordData>>
    val adjectivesList: Flow<List<AdjectiveWordData>>

    suspend fun getNoun(nounId: Long): NounWordData?
    suspend fun getVerb(verbId: Long): VerbWordWithVerbFormsBusiness?
    suspend fun getAdjective(verbId: Long): AdjectiveWordData?

    suspend fun deleteAdjective(id: Long)
    suspend fun deleteNoun(id: Long)
    suspend fun deleteVerb(id: Long)
    suspend fun deleteVerbForm(id: Long)

    suspend fun addNoun(nounWordData: NounWordData)
    suspend fun addVerb(verbForms: VerbWordWithVerbFormsBusiness)
    suspend fun addAdjective(adjectiveWordData: AdjectiveWordData)

    suspend fun addRandomWords()
    suspend fun deleteAllWords()
}