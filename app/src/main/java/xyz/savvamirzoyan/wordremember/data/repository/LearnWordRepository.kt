package xyz.savvamirzoyan.wordremember.data.repository

import xyz.savvamirzoyan.wordremember.contract.repository.ILearnWordRepository
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWord
import xyz.savvamirzoyan.wordremember.data.database.model.NounWord
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordWithVerbForms

object LearnWordRepository : Repository(), ILearnWordRepository {
    override suspend fun getAllNouns(): List<NounWord> = db.nounWordDao.getAllWords()

    override suspend fun getAllVerbs(): List<VerbWordWithVerbForms> = db.verbWordDao.getAllWords()

    override suspend fun getAllAdjectives(): List<AdjectiveWord> = db.adjectiveWordDao.getAllWords()
}