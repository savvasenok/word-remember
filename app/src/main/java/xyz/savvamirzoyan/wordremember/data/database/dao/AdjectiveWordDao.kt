package xyz.savvamirzoyan.wordremember.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWord

@Dao
interface AdjectiveWordDao {

    @Query("SELECT * FROM words_adjective WHERE :id = id")
    suspend fun getWord(id: Long): AdjectiveWord?

    @Query("SELECT * FROM words_adjective")
    suspend fun getAllWords(): List<AdjectiveWord>

    @Query("SELECT * FROM words_adjective WHERE :word = word")
    suspend fun getWord(word: String): AdjectiveWord?

    @Query("SELECT * FROM words_adjective WHERE :translation = translation")
    suspend fun getWordByTranslation(translation: String): AdjectiveWord?

    @Insert
    suspend fun saveWord(word: AdjectiveWord)
}