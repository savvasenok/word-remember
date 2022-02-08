package xyz.savvamirzoyan.wordremember.contract.data

import xyz.savvamirzoyan.wordremember.core.Mapper
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordWithVerbFormsData
import xyz.savvamirzoyan.wordremember.domain.model.VerbWordWithVerbFormsDomain

class VerbWordWithVerbFormsDataToDomainMapper : Mapper<VerbWordWithVerbFormsData, VerbWordWithVerbFormsDomain> {
    override fun map(data: VerbWordWithVerbFormsData): VerbWordWithVerbFormsDomain =
        VerbWordWithVerbFormsDomain(
            data.verbData.word,
            data.verbData.translation,
            data.verbFormData.prasensIch, data.verbFormData.prasensDu, data.verbFormData.prasensErSieEs,
            data.verbFormData.prasensWir, data.verbFormData.prasensIhr, data.verbFormData.prasensSieSie,
            data.verbFormData.prateritumIch, data.verbFormData.prateritumDu, data.verbFormData.prateritumErSieEs,
            data.verbFormData.prateritumWir, data.verbFormData.prateritumIhr, data.verbFormData.prateritumSieSie,
            data.verbFormData.perfekt
        )
}