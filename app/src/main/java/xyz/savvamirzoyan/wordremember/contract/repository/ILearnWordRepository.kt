package xyz.savvamirzoyan.wordremember.contract.repository

import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWord
import xyz.savvamirzoyan.wordremember.data.database.model.NounWord
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordWithVerbForms

interface ILearnWordRepository {

    suspend fun getAllNouns(): List<NounWord>

    suspend fun getAllVerbs(): List<VerbWordWithVerbForms>

    suspend fun getAllAdjectives(): List<AdjectiveWord>
}