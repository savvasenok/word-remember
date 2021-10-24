package xyz.savvamirzoyan.wordremember.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import xyz.savvamirzoyan.wordremember.contract.repository.IWordsListRepository
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWord
import xyz.savvamirzoyan.wordremember.data.database.model.NounWord
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordWithVerbForms
import xyz.savvamirzoyan.wordremember.data.types.WordGender

object WordsListRepository : Repository(), IWordsListRepository {

    override val verbsList: Flow<List<VerbWordWithVerbForms>> = flow {
        db.verbWordDao.allWordsFlow().collect {
            this.emit(it)
        }
    }
    override val wordsList: Flow<List<NounWord>> = flow {
        db.nounWordDao.allWordsFlow().collect {
            this.emit(it)
        }
    }
    override val adjectivesList: Flow<List<AdjectiveWord>> = flow {
        db.adjectiveWordDao.allWordsFlow().collect {
            this.emit(it)
        }
    }

    override suspend fun getNoun(nounId: Long): NounWord? {
        return db.nounWordDao.getWord(nounId)
    }

    override suspend fun getVerb(verbId: Long): VerbWordWithVerbForms? {
        return db.verbWordDao.getWord(verbId)
    }

    override suspend fun getAdjective(verbId: Long): AdjectiveWord? {
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

    override suspend fun addNoun(nounWord: NounWord) {
        db.nounWordDao.saveWord(nounWord)
    }

    override suspend fun addVerb(verbForms: VerbWordWithVerbForms) {
        db.verbFormDao.saveForm(verbForms.verbForm)
        db.verbWordDao.saveWord(verbForms.verb)
    }

    override suspend fun addAdjective(adjectiveWord: AdjectiveWord) {
        db.adjectiveWordDao.saveWord(adjectiveWord)
    }

    override suspend fun addRandomWords() {

        val allowedChars = ('A'..'Z') + ('a'..'z')

        for (i in 1..30) {

            val isOnlyPlural = listOf(true, false).random()
            val word = NounWord(
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