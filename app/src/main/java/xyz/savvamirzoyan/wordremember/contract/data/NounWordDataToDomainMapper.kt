package xyz.savvamirzoyan.wordremember.contract.data

import xyz.savvamirzoyan.wordremember.core.Mapper
import xyz.savvamirzoyan.wordremember.data.database.model.NounWordData
import xyz.savvamirzoyan.wordremember.domain.model.NounWordDomain

class NounWordDataToDomainMapper : Mapper<NounWordData, NounWordDomain> {
    override fun map(data: NounWordData): NounWordDomain =
        NounWordDomain(data.gender, data.word, data.plural, data.isOnlyPlural, data.translation)
}