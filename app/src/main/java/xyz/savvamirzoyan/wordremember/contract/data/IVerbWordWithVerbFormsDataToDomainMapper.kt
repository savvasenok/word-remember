package xyz.savvamirzoyan.wordremember.contract.data

import xyz.savvamirzoyan.wordremember.core.Abstract
import xyz.savvamirzoyan.wordremember.data.entity.domain.VerbWordWithVerbFormsDomain

interface IVerbWordWithVerbFormsDataToDomainMapper : Abstract.Mapper {

    fun map(
        word: String,
        translation: String,
        prasensIch: String?,
        prasensDu: String?,
        prasensErSieEs: String?,
        prasensWir: String?,
        prasensIhr: String?,
        prasensSieSie: String?,
        prateritumIch: String?,
        prateritumDu: String?,
        prateritumErSieEs: String?,
        prateritumWir: String?,
        prateritumIhr: String?,
        prateritumSieSie: String?,
        perfekt: String?
    ): VerbWordWithVerbFormsDomain

    class Base : IVerbWordWithVerbFormsDataToDomainMapper {
        override fun map(
            word: String,
            translation: String,
            prasensIch: String?,
            prasensDu: String?,
            prasensErSieEs: String?,
            prasensWir: String?,
            prasensIhr: String?,
            prasensSieSie: String?,
            prateritumIch: String?,
            prateritumDu: String?,
            prateritumErSieEs: String?,
            prateritumWir: String?,
            prateritumIhr: String?,
            prateritumSieSie: String?,
            perfekt: String?
        ): VerbWordWithVerbFormsDomain = try {
            VerbWordWithVerbFormsDomain.Success(
                word,
                translation,
                prasensIch, prasensDu, prasensErSieEs,
                prasensWir, prasensIhr, prasensSieSie,
                prateritumIch, prateritumDu, prateritumErSieEs,
                prateritumWir, prateritumIhr, prateritumSieSie,
                perfekt
            )
        } catch (e: Exception) {
            VerbWordWithVerbFormsDomain.Fail(e)
        }
    }
}