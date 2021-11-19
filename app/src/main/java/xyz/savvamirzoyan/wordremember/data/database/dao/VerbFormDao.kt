package xyz.savvamirzoyan.wordremember.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import xyz.savvamirzoyan.wordremember.data.database.model.VerbFormData

@Dao
interface VerbFormDao {

    @Query("SELECT * FROM verb_forms WHERE :id = id")
    suspend fun getWord(id: Long): VerbFormData?

    @Update
    suspend fun update(formData: VerbFormData)

    @Insert
    suspend fun saveForm(formData: VerbFormData): Long

    @Query("DELETE FROM verb_forms WHERE :wordId == id")
    suspend fun deleteWord(wordId: Long)

    @Query("DELETE FROM verb_forms")
    suspend fun deleteAllWords()
}