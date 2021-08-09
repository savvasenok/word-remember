package xyz.savvamirzoyan.wordremember.contract.repository

import xyz.savvamirzoyan.wordremember.data.entity.VerbFormHelper
import xyz.savvamirzoyan.wordremember.data.types.WordGender

interface IAddWordRepository {

    // Noun
    suspend fun saveWord(
        gender: WordGender,
        word: String,
        plural: String?,
        translation: String
    )

    // Noun Plural
    suspend fun saveWordPlural(
        plural: String,
        translation: String
    )

    // Adjective
    suspend fun saveWordAdjective(
        word: String,
        translation: String
    )

    // Verb
    suspend fun saveWordVerb(
        word: String,
        translation: String,
        verbForm: VerbFormHelper
    )
}