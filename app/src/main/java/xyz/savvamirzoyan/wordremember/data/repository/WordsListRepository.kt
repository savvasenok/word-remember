package xyz.savvamirzoyan.wordremember.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import xyz.savvamirzoyan.wordremember.contract.repository.IWordsListRepository
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWordData
import xyz.savvamirzoyan.wordremember.data.database.model.NounWordData
import xyz.savvamirzoyan.wordremember.data.entity.VerbWordWithVerbFormsBusiness
import xyz.savvamirzoyan.wordremember.data.types.WordGender

object WordsListRepository : Repository(), IWordsListRepository {

    override val verbsList: Flow<List<VerbWordWithVerbFormsBusiness>> = flow {
        db.verbWordDao.allWordsFlow().collect {
            this.emit(it)
        }
    }
    override val wordsList: Flow<List<NounWordData>> = flow {
        db.nounWordDao.allWordsFlow().collect {
            this.emit(it)
        }
    }
    override val adjectivesList: Flow<List<AdjectiveWordData>> = flow {
        db.adjectiveWordDao.allWordsFlow().collect {
            this.emit(it)
        }
    }

    override suspend fun getNoun(nounId: Long): NounWordData? {
        return db.nounWordDao.getWord(nounId)
    }

    override suspend fun getVerb(verbId: Long): VerbWordWithVerbFormsBusiness? {
        return db.verbWordDao.getWord(verbId)
    }

    override suspend fun getAdjective(verbId: Long): AdjectiveWordData? {
        return db.adjectiveWordDao.getWord(verbId)
    }

    override suspend fun deleteAdjective(id: Long) {
        db.adjectiveWordDao.deleteWord(id)
    }

    override suspend fun deleteNoun(id: Long) {
        db.nounWordDao.deleteWord(id)
    }

    override suspend fun deleteVerb(id: Long) {
        db.verbWordDao.deleteWord(id)
    }

    override suspend fun deleteVerbForm(id: Long) {
        db.verbFormDao.deleteWord(id)
    }

    override suspend fun addNoun(nounWordData: NounWordData) {
        db.nounWordDao.saveWord(nounWordData)
    }

    override suspend fun addVerb(verbForms: VerbWordWithVerbFormsBusiness) {
        db.verbFormDao.saveForm(verbForms.verbFormData)
        db.verbWordDao.saveWord(verbForms.verbData)
    }

    override suspend fun addAdjective(adjectiveWordData: AdjectiveWordData) {
        db.adjectiveWordDao.saveWord(adjectiveWordData)
    }

    override suspend fun addRandomWords() {

        val allowedChars = ('A'..'Z') + ('a'..'z')

        for (i in 1..30) {

            val isOnlyPlural = listOf(true, false).random()
            val word = NounWordData(
                gender = listOf(WordGender.DER, WordGender.DIE, WordGender.DAS).random(),
                isOnlyPlural = isOnlyPlural,
                plural = (1..15)
                    .map { allowedChars.random() }
                    .joinToString(""),
                word = if (!isOnlyPlural) (1..15)
                    .map { allowedChars.random() }
                    .joinToString("") else null,
                translation = (1..15)
                    .map { allowedChars.random() }
                    .joinToString("")
            )

            db.nounWordDao.saveWord(word)
        }
    }

    override suspend fun deleteAllWords() {
        db.nounWordDao.deleteAllWords()
        db.verbWordDao.deleteAllWords()
        db.verbFormDao.deleteAllWords()
        db.adjectiveWordDao.deleteAllWords()
    }
}