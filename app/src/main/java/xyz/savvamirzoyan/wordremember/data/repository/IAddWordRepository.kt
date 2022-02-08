package xyz.savvamirzoyan.wordremember.data.repository

import xyz.savvamirzoyan.wordremember.data.types.WordGender
import xyz.savvamirzoyan.wordremember.domain.model.VerbFormHelper

interface IAddWordRepository {

    suspend fun saveWord(
        gender: WordGender,
        word: String,
        plural: String?,
        translation: String
    )

    suspend fun saveWordPlural(
        plural: String,
        translation: String
    )

    suspend fun saveWordAdjective(
        word: String,
        translation: String,
        komparativ: String,
        superlativ: String
    )

    suspend fun saveWordVerb(
        word: String,
        translation: String,
        verbForm: VerbFormHelper
    )
}