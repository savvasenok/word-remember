package xyz.savvamirzoyan.wordremember.data.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class VerbWordWithVerbFormsData(
    @Embedded
    val verbData: VerbWordData,

    @Relation(parentColumn = "verbId", entityColumn = "verbWordId")
    val verbFormData: VerbFormData
)