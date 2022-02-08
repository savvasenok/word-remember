package xyz.savvamirzoyan.wordremember.data.repository

import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWordData
import xyz.savvamirzoyan.wordremember.data.database.model.NounWordData
import xyz.savvamirzoyan.wordremember.data.database.model.VerbFormData
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordData
import xyz.savvamirzoyan.wordremember.data.types.WordGender
import xyz.savvamirzoyan.wordremember.domain.model.VerbFormHelper

object AddWordRepository : Repository(), IAddWordRepository {

    override suspend fun saveWord(
        gender: WordGender,
        word: String,
        plural: String?,
        translation: String
    ) {
        db.nounWordDao.saveWord(
            NounWordData(
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
            NounWordData(
                gender = null,
                word = null,
                plural = plural,
                translation = translation,
                isOnlyPlural = true
            )
        )
    }

    override suspend fun saveWordAdjective(
        word: String,
        translation: String,
        komparativ: String,
        superlativ: String
    ) {
        db.adjectiveWordDao.saveWord(
            AdjectiveWordData(
                word = word,
                translation = translation,
                komparativ = komparativ,
                superlativ = superlativ
            )
        )
    }

    override suspend fun saveWordVerb(word: String, translation: String, verbForm: VerbFormHelper) {
        val verbId = db.verbWordDao.saveWord(VerbWordData(word = word, translation = translation))
        db.verbFormDao.saveForm(verbForm.toVerbWord(verbId))
    }

    private fun VerbFormHelper.toVerbWord(verbWordId: Long): VerbFormData = VerbFormData(
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