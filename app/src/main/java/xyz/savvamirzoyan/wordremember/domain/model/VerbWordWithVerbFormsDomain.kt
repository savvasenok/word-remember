package xyz.savvamirzoyan.wordremember.domain.model

data class VerbWordWithVerbFormsDomain(
    val word: String,
    val translation: String,
    val prasensIch: String?,
    val prasensDu: String?,
    val prasensErSieEs: String?,
    val prasensWir: String?,
    val prasensIhr: String?,
    val prasensSieSie: String?,
    val prateritumIch: String?,
    val prateritumDu: String?,
    val prateritumErSieEs: String?,
    val prateritumWir: String?,
    val prateritumIhr: String?,
    val prateritumSieSie: String?,
    val perfekt: String?
)