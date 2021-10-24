package xyz.savvamirzoyan.wordremember.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import xyz.savvamirzoyan.wordremember.data.database.model.VerbForm

@Dao
interface VerbFormDao {

    @Query("SELECT * FROM verb_forms WHERE :id = id")
    suspend fun getWord(id: Long): VerbForm?

    @Update
    suspend fun update(form: VerbForm)

    @Insert
    suspend fun saveForm(form: VerbForm): Long

    @Query("DELETE FROM verb_forms WHERE :wordId == id")
    suspend fun deleteWord(wordId: Long)

    @Query("DELETE FROM verb_forms")
    suspend fun deleteAllWords()
}