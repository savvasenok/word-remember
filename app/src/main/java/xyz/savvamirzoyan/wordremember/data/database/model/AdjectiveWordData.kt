package xyz.savvamirzoyan.wordremember.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import xyz.savvamirzoyan.wordremember.contract.data.IAdjectiveWordDataToDomainMapper
import xyz.savvamirzoyan.wordremember.core.Abstract
import xyz.savvamirzoyan.wordremember.data.entity.domain.AdjectiveWordDomain

@Entity(tableName = "words_adjective")
data class AdjectiveWordData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val word: String,
    val translation: String,
    val komparativ: String,
    val superlativ: String
) : Abstract.Object<AdjectiveWordDomain, IAdjectiveWordDataToDomainMapper> {
    override fun map(mapper: IAdjectiveWordDataToDomainMapper): AdjectiveWordDomain {
        return mapper.map(word, translation, komparativ, superlativ)
    }
}
