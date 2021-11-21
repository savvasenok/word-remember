package xyz.savvamirzoyan.wordremember.contract.repository

import kotlinx.coroutines.flow.Flow
import xyz.savvamirzoyan.wordremember.data.entity.domain.AdjectiveWordDomain
import xyz.savvamirzoyan.wordremember.data.entity.domain.NounWordDomain
import xyz.savvamirzoyan.wordremember.data.entity.domain.VerbWordWithVerbFormsDomain

interface IWordsListRepository {

    val verbsList: Flow<List<VerbWordWithVerbFormsDomain>>
    val wordsList: Flow<List<NounWordDomain>>
    val adjectivesList: Flow<List<AdjectiveWordDomain>>

    suspend fun getNoun(nounId: Long): NounWordDomain?
    suspend fun getVerb(verbId: Long): VerbWordWithVerbFormsDomain?
    suspend fun getAdjective(verbId: Long): AdjectiveWordDomain?

    suspend fun deleteAdjective(id: Long)
    suspend fun deleteNoun(id: Long)
    suspend fun deleteVerb(id: Long)
    suspend fun deleteVerbForm(id: Long)

    suspend fun addNoun(nounWordData: NounWordDomain)
    suspend fun addVerb(verbForms: VerbWordWithVerbFormsDomain)
    suspend fun addAdjective(adjectiveWordData: AdjectiveWordDomain)

    suspend fun addRandomWords()
    suspend fun deleteAllWords()
}