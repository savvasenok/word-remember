package xyz.savvamirzoyan.wordremember.contract.data

import xyz.savvamirzoyan.wordremember.core.Abstract
import xyz.savvamirzoyan.wordremember.data.entity.domain.NounWordDomain
import xyz.savvamirzoyan.wordremember.data.types.WordGender

interface INounWordDataToDomainMapper : Abstract.Mapper {

    fun map(
        gender: WordGender?,
        word: String?,
        plural: String?,
        isOnlyPlural: Boolean,
        translation: String
    ): NounWordDomain

    class Base : INounWordDataToDomainMapper {
        override fun map(
            gender: WordGender?,
            word: String?,
            plural: String?,
            isOnlyPlural: Boolean,
            translation: String
        ): NounWordDomain = try {
            NounWordDomain.Success(gender, word, plural, isOnlyPlural, translation)
        } catch (e: Exception) {
            NounWordDomain.Fail(e)
        }
    }
}