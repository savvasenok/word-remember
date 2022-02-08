package xyz.savvamirzoyan.wordremember.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.savvamirzoyan.wordremember.contract.data.AdjectiveWordDataToDomainMapper
import xyz.savvamirzoyan.wordremember.contract.data.NounWordDataToDomainMapper
import xyz.savvamirzoyan.wordremember.contract.data.VerbWordWithVerbFormsDataToDomainMapper
import xyz.savvamirzoyan.wordremember.data.database.model.NounWordData
import xyz.savvamirzoyan.wordremember.domain.model.AdjectiveWordDomain
import xyz.savvamirzoyan.wordremember.domain.model.NounWordDomain
import xyz.savvamirzoyan.wordremember.domain.model.VerbWordWithVerbFormsDomain

interface WordsListRepository {

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

    suspend fun addRandomWords(words: List<NounWordData>)
    suspend fun deleteAllWords()

    class Base(
        private val verbWordWithVerbFormsDataToDomainMapper: VerbWordWithVerbFormsDataToDomainMapper,
        private val nounWordDataToDomainMapper: NounWordDataToDomainMapper,
        private val adjectiveWordDataToDomainMapper: AdjectiveWordDataToDomainMapper
    ) : Repository(), WordsListRepository {

        override val verbsList: Flow<List<VerbWordWithVerbFormsDomain>> =
            db.verbWordDao.allWordsFlow()
                .map { list ->
                    list.map {
                        verbWordWithVerbFormsDataToDomainMapper.map(it)
                    }
                }

        override val wordsList: Flow<List<NounWordDomain>> =
            db.nounWordDao.allWordsFlow()
                .map { list ->
                    list.map {
                        nounWordDataToDomainMapper.map(it)
                    }
                }


        override val adjectivesList: Flow<List<AdjectiveWordDomain>> =
            db.adjectiveWordDao.allWordsFlow()
                .map { list ->
                    list.map {
                        adjectiveWordDataToDomainMapper.map(it)
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

        override suspend fun addRandomWords(words: List<NounWordData>) {
            words.forEach { word ->
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
}