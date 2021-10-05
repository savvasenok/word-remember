package xyz.savvamirzoyan.wordremember.contract.repository

import kotlinx.coroutines.flow.Flow
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWord
import xyz.savvamirzoyan.wordremember.data.database.model.NounWord
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordWithVerbForms

interface IWordsListRepository {

    val verbsList: Flow<List<VerbWordWithVerbForms>>
    val wordsList: Flow<List<NounWord>>
    val adjectivesList: Flow<List<AdjectiveWord>>

    suspend fun addItem(item: NounWord)
}