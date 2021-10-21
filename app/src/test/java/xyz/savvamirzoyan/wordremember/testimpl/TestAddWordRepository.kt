package xyz.savvamirzoyan.wordremember.testimpl

import xyz.savvamirzoyan.wordremember.contract.repository.IAddWordRepository
import xyz.savvamirzoyan.wordremember.data.entity.VerbFormHelper
import xyz.savvamirzoyan.wordremember.data.types.WordGender

class TestAddWordRepository : IAddWordRepository {
    override suspend fun saveWord(
        gender: WordGender,
        word: String,
        plural: String?,
        translation: String
    ) {
    }

    override suspend fun saveWordPlural(plural: String, translation: String) {}

    override suspend fun saveWordAdjective(
        word: String,
        translation: String,
        komparativ: String,
        superlativ: String
    ) {

    }

    override suspend fun saveWordVerb(
        word: String,
        translation: String,
        verbForm: VerbFormHelper
    ) {
    }
}