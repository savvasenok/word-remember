package xyz.savvamirzoyan.wordremember.data.repository

import xyz.savvamirzoyan.wordremember.contract.repository.IAddWordRepository
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWord
import xyz.savvamirzoyan.wordremember.data.database.model.NounWord
import xyz.savvamirzoyan.wordremember.data.database.model.VerbForm
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWord
import xyz.savvamirzoyan.wordremember.data.entity.VerbFormHelper
import xyz.savvamirzoyan.wordremember.data.types.WordGender

object AddWordRepository : Repository(), IAddWordRepository {

    override suspend fun saveWord(
        gender: WordGender,
        word: String,
        plural: String?,
        translation: String
    ) {
        db.nounWordDao.saveWord(
            NounWord(
                gender = gender,
                word = word,
                plural = plural.stringOrNull(),
                translation = translation,
                isOnlyPlural = false
            )
        )
    }

    override suspend fun saveWordPlural(plural: String, translation: String) {
        db.nounWordDao.saveWord(
            NounWord(
                gender = null,
                word = null,
                plural = plural,
                translation = translation,
                isOnlyPlural = true
            )
        )
    }

    override suspend fun saveWordAdjective(word: String, translation: String) {
        db.adjectiveWordDao.saveWord(
            AdjectiveWord(
                word = word,
                translation = translation
            )
        )
    }

    override suspend fun saveWordVerb(word: String, translation: String, verbForm: VerbFormHelper) {
        val verbId = db.verbWordDao.saveWord(VerbWord(word = word, translation = translation))
        db.verbFormDao.saveForm(verbForm.toVerbWord(verbId))
    }

    private fun VerbFormHelper.toVerbWord(verbWordId: Long): VerbForm = VerbForm(
        verbWordId = verbWordId,
        prasensIch = prasensIch.stringOrNull(),
        prasensDu = prasensDu.stringOrNull(),
        prasensErSieEs = prasensErSieEs.stringOrNull(),
        prasensWir = prasensWir.stringOrNull(),
        prasensIhr = prasensIhr.stringOrNull(),
        prasensSieSie = prasensSieSie.stringOrNull(),
        prateritumIch = prateritumIch.stringOrNull(),
        prateritumDu = prateritumDu.stringOrNull(),
        prateritumErSieEs = prateritumErSieEs.stringOrNull(),
        prateritumWir = prateritumWir.stringOrNull(),
        prateritumIhr = prateritumIhr.stringOrNull(),
        prateritumSieSie = prateritumSieSie.stringOrNull(),
        perfekt = perfekt.stringOrNull()
    )

    private fun String?.stringOrNull(): String? = if (this?.isBlank() == true) null else this
}