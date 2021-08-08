package xyz.savvamirzoyan.wordremember.data.repository

import xyz.savvamirzoyan.wordremember.contract.repository.IAddWordRepository
import xyz.savvamirzoyan.wordremember.data.entity.VerbForm
import xyz.savvamirzoyan.wordremember.data.types.WordGender

class AddWordRepository : IAddWordRepository {

    override suspend fun saveWord(
        gender: WordGender,
        word: String,
        plural: String?,
        translation: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun saveWordPlural(plural: String, translation: String) {
        TODO("Not yet implemented")
    }

    override suspend fun saveWordAdjective(word: String, translation: String) {
        TODO("Not yet implemented")
    }

    override suspend fun saveWordVerb(word: String, verbForm: VerbForm, translation: String) {
        TODO("Not yet implemented")
    }
}