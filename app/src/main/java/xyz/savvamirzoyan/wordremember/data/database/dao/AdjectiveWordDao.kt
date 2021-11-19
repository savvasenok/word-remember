package xyz.savvamirzoyan.wordremember.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWordData

@Dao
interface AdjectiveWordDao {

    @Query("SELECT * FROM words_adjective WHERE :id = id")
    suspend fun getWord(id: Long): AdjectiveWordData?

    @Query("SELECT * FROM words_adjective")
    suspend fun getAllWords(): List<AdjectiveWordData>

    @Query("SELECT * FROM words_adjective")
    fun allWordsFlow(): Flow<List<AdjectiveWordData>>

    @Query("SELECT * FROM words_adjective WHERE :word = word")
    suspend fun getWord(word: String): AdjectiveWordData?

    @Query("SELECT * FROM words_adjective WHERE :translation = translation")
    suspend fun getWordByTranslation(translation: String): AdjectiveWordData?

    @Insert
    suspend fun saveWord(wordData: AdjectiveWordData)

    @Query("DELETE FROM words_adjective WHERE :wordId == id")
    suspend fun deleteWord(wordId: Long)

    @Query("DELETE FROM words_adjective")
    suspend fun deleteAllWords()
}