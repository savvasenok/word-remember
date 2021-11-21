package xyz.savvamirzoyan.wordremember.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import xyz.savvamirzoyan.wordremember.contract.data.IAdjectiveWordDataToDomainMapper
import xyz.savvamirzoyan.wordremember.contract.data.INounWordDataToDomainMapper
import xyz.savvamirzoyan.wordremember.contract.data.IVerbWordWithVerbFormsDataToDomainMapper
import xyz.savvamirzoyan.wordremember.contract.repository.IWordsListRepository
import xyz.savvamirzoyan.wordremember.data.database.model.NounWordData
import xyz.savvamirzoyan.wordremember.data.entity.domain.AdjectiveWordDomain
import xyz.savvamirzoyan.wordremember.data.entity.domain.NounWordDomain
import xyz.savvamirzoyan.wordremember.data.entity.domain.VerbWordWithVerbFormsDomain
import xyz.savvamirzoyan.wordremember.data.types.WordGender

object WordsListRepository : Repository(), IWordsListRepository {

    override val verbsList: Flow<List<VerbWordWithVerbFormsDomain>> =
        db.verbWordDao.allWordsFlow()
            .map { list ->
                Timber.i("GOT VERBS")
                list.map {
                    it.map(IVerbWordWithVerbFormsDataToDomainMapper.Base())
                }
            }

    override val wordsList: Flow<List<NounWordDomain>> =
        db.nounWordDao.allWordsFlow()
            .map { list ->
                Timber.i("GOT NOUNS")
                list.map {
                    it.map(INounWordDataToDomainMapper.Base())
                }
            }


    override val adjectivesList: Flow<List<AdjectiveWordDomain>> =
        db.adjectiveWordDao.allWordsFlow()
            .map { list ->
                Timber.i("GOT ADJECTIVES")
                list.map {
                    it.map(IAdjectiveWordDataToDomainMapper.Base())
                }
            }

    override suspend fun getNoun(nounId: Long): NounWordDomain? {
        TODO("Not yet implemented")
    }

    override suspend fun getVerb(verbId: Long): VerbWordWithVerbFormsDomain? {
        TODO("Not yet implemented")
    }

    override suspend fun getAdjective(verbId: Long): AdjectiveWordDomain? {
        TODO("Not yet implemented")
    }


//    override suspend fun getNoun(nounId: Long): NounWordData? {
//        return db.nounWordDao.getWord(nounId)
//    }
//
//    override suspend fun getVerb(verbId: Long): VerbWordWithVerbFormsData? {
//        return db.verbWordDao.getWord(verbId)
//    }
//
//    override suspend fun getAdjective(verbId: Long): AdjectiveWordData? {
//        return db.adjectiveWordDao.getWord(verbId)
//    }

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

    override suspend fun addNoun(nounWordData: NounWordDomain) {
        TODO("Not yet implemented")
    }

    override suspend fun addVerb(verbForms: VerbWordWithVerbFormsDomain) {
        TODO("Not yet implemented")
    }

    override suspend fun addAdjective(adjectiveWordData: AdjectiveWordDomain) {
        TODO("Not yet implemented")
    }

//    override suspend fun addNoun(nounWordData: NounWordData) {
//        db.nounWordDao.saveWord(nounWordData)
//    }
//
//    override suspend fun addVerb(verbForms: VerbWordWithVerbFormsData) {
//        db.verbFormDao.saveForm(verbForms.verbFormData)
//        db.verbWordDao.saveWord(verbForms.verbData)
//    }
//
//    override suspend fun addAdjective(adjectiveWordData: AdjectiveWordData) {
//        db.adjectiveWordDao.saveWord(adjectiveWordData)
//    }

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