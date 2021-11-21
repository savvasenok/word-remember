package xyz.savvamirzoyan.wordremember.contract.data

import xyz.savvamirzoyan.wordremember.core.Abstract
import xyz.savvamirzoyan.wordremember.data.entity.domain.AdjectiveWordDomain

interface IAdjectiveWordDataToDomainMapper : Abstract.Mapper {

    fun map(
        word: String,
        translation: String,
        komparativ: String,
        superlativ: String
    ): AdjectiveWordDomain

    class Base : IAdjectiveWordDataToDomainMapper {
        override fun map(
            word: String,
            translation: String,
            komparativ: String,
            superlativ: String
        ): AdjectiveWordDomain = try {
            AdjectiveWordDomain.Success(word, translation, komparativ, superlativ)
        } catch (e: Exception) {
            AdjectiveWordDomain.Fail(e)
        }
    }
}