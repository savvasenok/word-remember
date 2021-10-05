package xyz.savvamirzoyan.wordremember.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import xyz.savvamirzoyan.wordremember.contract.repository.IWordsListRepository
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWord
import xyz.savvamirzoyan.wordremember.data.database.model.NounWord
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordWithVerbForms

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

    override suspend fun addItem(item: NounWord) {
        db.nounWordDao.saveWord(item)
    }
}