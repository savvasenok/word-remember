package xyz.savvamirzoyan.wordremember.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import xyz.savvamirzoyan.wordremember.data.database.model.NounWordData

@Dao
interface NounWordDao {

    @Query("SELECT * FROM words_noun WHERE :id = id")
    suspend fun getWord(id: Long): NounWordData?

    @Query("SELECT * FROM words_noun")
    suspend fun getAllWords(): List<NounWordData>

    @Query("SELECT * FROM words_noun")
    fun allWordsFlow(): Flow<List<NounWordData>>

    @Query("SELECT * FROM words_noun WHERE :word = word")
    suspend fun getWord(word: String): NounWordData?

    @Query("SELECT * FROM words_noun WHERE :translation = translation")
    suspend fun getWordByTranslation(translation: String): NounWordData?

    @Insert
    suspend fun saveWord(wordData: NounWordData)

    @Query("DELETE FROM words_noun WHERE :wordId == id")
    suspend fun deleteWord(wordId: Long)

    @Query("DELETE FROM WORDS_NOUN")
    suspend fun deleteAllWords()
}