package xyz.savvamirzoyan.wordremember.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words_verb")
data class VerbWord(
    @PrimaryKey(autoGenerate = true) val verbId: Long = 0L,
    val word: String,
    val translation: String
)
