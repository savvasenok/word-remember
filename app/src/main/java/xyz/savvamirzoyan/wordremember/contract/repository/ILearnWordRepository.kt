package xyz.savvamirzoyan.wordremember.contract.repository

import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWordData
import xyz.savvamirzoyan.wordremember.data.database.model.NounWordData
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordWithVerbFormsData

interface ILearnWordRepository {
    suspend fun getAllNouns(): List<NounWordData>
    suspend fun getAllVerbs(): List<VerbWordWithVerbFormsData>
    suspend fun getAllAdjectives(): List<AdjectiveWordData>
}