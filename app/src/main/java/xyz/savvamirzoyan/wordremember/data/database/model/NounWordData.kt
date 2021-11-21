package xyz.savvamirzoyan.wordremember.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import xyz.savvamirzoyan.wordremember.contract.data.INounWordDataToDomainMapper
import xyz.savvamirzoyan.wordremember.core.Abstract
import xyz.savvamirzoyan.wordremember.data.entity.domain.NounWordDomain
import xyz.savvamirzoyan.wordremember.data.types.WordGender

@Entity(tableName = "words_noun")
data class NounWordData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val gender: WordGender?,
    val word: String?,
    val plural: String?,
    val isOnlyPlural: Boolean,
    val translation: String
) : Abstract.Object<NounWordDomain, INounWordDataToDomainMapper> {

    override fun map(mapper: INounWordDataToDomainMapper): NounWordDomain {
        return mapper.map(gender, word, plural, isOnlyPlural, translation)
    }

    fun wordWithGender(): String? =
        if (isOnlyPlural) null else "${gender?.name?.lowercase()} $word".trim()
}