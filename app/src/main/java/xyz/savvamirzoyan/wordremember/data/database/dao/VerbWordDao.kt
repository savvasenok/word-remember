package xyz.savvamirzoyan.wordremember.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWord
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordWithVerbForms

@Dao
interface VerbWordDao {

    @Transaction
    @Query("SELECT * FROM words_verb WHERE :id = verbId")
    suspend fun getWord(id: Long): VerbWordWithVerbForms?

    @Transaction
    @Query("SELECT * FROM words_verb WHERE :word = word")
    suspend fun getWord(word: String): VerbWordWithVerbForms?

    @Transaction
    @Query("SELECT * FROM words_verb WHERE :translation = translation")
    suspend fun getWordByTranslation(translation: String): VerbWordWithVerbForms?

    @Insert
    suspend fun saveWord(word: VerbWord): Long
}