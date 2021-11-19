package xyz.savvamirzoyan.wordremember.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import xyz.savvamirzoyan.wordremember.data.types.WordGender

@Entity(tableName = "words_noun")
data class NounWordData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val gender: WordGender?,
    val word: String?,
    val plural: String?,
    val isOnlyPlural: Boolean,
    val translation: String
) {
    fun wordWithGender(): String? =
        if (isOnlyPlural) null else "${gender?.name?.lowercase()} $word".trim()
}