package xyz.savvamirzoyan.wordremember.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words_adjective")
data class AdjectiveWord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val word: String,
    val translation: String
)
