package xyz.savvamirzoyan.wordremember.contract.data

import xyz.savvamirzoyan.wordremember.core.Mapper
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWordData
import xyz.savvamirzoyan.wordremember.domain.model.AdjectiveWordDomain

class AdjectiveWordDataToDomainMapper : Mapper<AdjectiveWordData, AdjectiveWordDomain> {
    override fun map(data: AdjectiveWordData): AdjectiveWordDomain =
        AdjectiveWordDomain(data.word, data.translation, data.komparativ, data.superlativ)
}