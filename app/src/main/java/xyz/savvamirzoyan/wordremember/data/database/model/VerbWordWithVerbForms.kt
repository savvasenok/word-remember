package xyz.savvamirzoyan.wordremember.data.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class VerbWordWithVerbForms(
    @Embedded val verb: VerbWord,
    @Relation(parentColumn = "verbId", entityColumn = "verbWordId")
    val verbForm: VerbForm
)
