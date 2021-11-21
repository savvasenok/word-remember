package xyz.savvamirzoyan.wordremember.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordData
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordWithVerbFormsData

@Dao
interface VerbWordDao {

    @Transaction
    @Query("SELECT * FROM words_verb WHERE :id = verbId")
    suspend fun getWord(id: Long): VerbWordWithVerbFormsData?

    @Transaction
    @Query("SELECT * FROM words_verb WHERE :word = word")
    suspend fun getWord(word: String): VerbWordWithVerbFormsData?

    @Transaction
    @Query("SELECT * FROM words_verb")
    suspend fun getAllWords(): List<VerbWordWithVerbFormsData>

    @Transaction
    @Query("SELECT * FROM words_verb")
    fun allWordsFlow(): Flow<List<VerbWordWithVerbFormsData>>

    @Transaction
    @Query("SELECT * FROM words_verb WHERE :translation = translation")
    suspend fun getWordByTranslation(translation: String): VerbWordWithVerbFormsData?

    @Insert
    suspend fun saveWord(wordData: VerbWordData): Long

    @Query("DELETE FROM words_verb WHERE :wordId == verbId")
    suspend fun deleteWord(wordId: Long)

    @Query("DELETE FROM words_verb")
    suspend fun deleteAllWords()
}