package xyz.savvamirzoyan.wordremember.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import xyz.savvamirzoyan.wordremember.data.database.model.NounWord

@Dao
interface NounWordDao {

    @Query("SELECT * FROM words_noun WHERE :id = id")
    suspend fun getWord(id: Long): NounWord?

    @Query("SELECT * FROM words_noun")
    suspend fun getAllWords(): List<NounWord>

    @Query("SELECT * FROM words_noun")
    fun allWordsFlow(): Flow<List<NounWord>>

    @Query("SELECT * FROM words_noun WHERE :word = word")
    suspend fun getWord(word: String): NounWord?

    @Query("SELECT * FROM words_noun WHERE :translation = translation")
    suspend fun getWordByTranslation(translation: String): NounWord?

    @Insert
    suspend fun saveWord(word: NounWord)

    @Query("DELETE FROM words_noun WHERE :wordId == id")
    suspend fun deleteWord(wordId: Long)
}