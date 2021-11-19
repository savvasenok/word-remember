package xyz.savvamirzoyan.wordremember.data.entity

import androidx.room.Embedded
import androidx.room.Relation
import xyz.savvamirzoyan.wordremember.data.database.model.VerbFormData
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordData

data class VerbWordWithVerbFormsBusiness(
    @Embedded val verbData: VerbWordData,
    @Relation(parentColumn = "verbId", entityColumn = "verbWordId")
    val verbFormData: VerbFormData
)
