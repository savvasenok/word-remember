package xyz.savvamirzoyan.wordremember.domain.model

import xyz.savvamirzoyan.wordremember.data.types.WordGender

data class NounWordDomain(
    val gender: WordGender?,
    val word: String?,
    val plural: String?,
    val isOnlyPlural: Boolean,
    val translation: String
)
