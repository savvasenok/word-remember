package xyz.savvamirzoyan.wordremember.data.database.model

import androidx.room.Embedded
import androidx.room.Relation
import xyz.savvamirzoyan.wordremember.contract.data.IVerbWordWithVerbFormsDataToDomainMapper
import xyz.savvamirzoyan.wordremember.core.Abstract
import xyz.savvamirzoyan.wordremember.data.entity.domain.VerbWordWithVerbFormsDomain

data class VerbWordWithVerbFormsData(
    @Embedded val verbData: VerbWordData,
    @Relation(parentColumn = "verbId", entityColumn = "verbWordId")
    val verbFormData: VerbFormData
) : Abstract.Object<VerbWordWithVerbFormsDomain, IVerbWordWithVerbFormsDataToDomainMapper> {
    override fun map(mapper: IVerbWordWithVerbFormsDataToDomainMapper): VerbWordWithVerbFormsDomain {
        return mapper.map(
            word = verbData.word,
            translation = verbData.translation,
            prasensIch = verbFormData.prasensIch,
            prasensDu = verbFormData.prasensDu,
            prasensErSieEs = verbFormData.prasensErSieEs,
            prasensWir = verbFormData.prasensWir,
            prasensIhr = verbFormData.prasensIhr,
            prasensSieSie = verbFormData.prasensSieSie,
            prateritumIch = verbFormData.prateritumIch,
            prateritumDu = verbFormData.prateritumDu,
            prateritumErSieEs = verbFormData.prateritumErSieEs,
            prateritumWir = verbFormData.prateritumWir,
            prateritumIhr = verbFormData.prateritumIhr,
            prateritumSieSie = verbFormData.prateritumSieSie,
            perfekt = verbFormData.perfekt
        )
    }
}
