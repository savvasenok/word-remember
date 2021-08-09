package xyz.savvamirzoyan.wordremember.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verb_forms")
data class VerbForm(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val verbWordId: Long,

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

    val perfektIch: String?,
    val perfektDu: String?,
    val perfektErSieEs: String?,
    val perfektWir: String?,
    val perfektIhr: String?,
    val perfektSieSie: String?,
)
