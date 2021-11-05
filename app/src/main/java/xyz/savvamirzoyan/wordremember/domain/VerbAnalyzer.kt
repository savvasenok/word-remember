package xyz.savvamirzoyan.wordremember.domain

import xyz.savvamirzoyan.wordremember.constants.Endings
import xyz.savvamirzoyan.wordremember.constants.Person
import xyz.savvamirzoyan.wordremember.constants.Prefix
import xyz.savvamirzoyan.wordremember.constants.Suffix

object VerbAnalyzer {

    object Builder {
        fun prasens(prefix: String, wordRoot: String, person: Person): String {
            val endingTransformer = wordEndingTransformer(wordRoot)
            return "$prefix$wordRoot${endingTransformer(person)}"
        }
    }

    fun prefix(word: String) = Prefix.all.find { word.startsWith(it) } ?: ""

    fun removePrefix(word: String) = word.removePrefix(prefix(word))

    fun removeSuffixEn(word: String): String = if (word.removeSuffix(Suffix.en) != word) {
        word.removeSuffix(Suffix.en)
    } else {
        word.removeSuffix(Suffix.n)
    }

    private fun wordEndingTransformer(word: String): (Person) -> String = when {
        word.endsWith(Endings.t) -> ::prasensEndingForTD
        word.endsWith(Endings.d) -> ::prasensEndingForTD
        word.endsWith(Endings.m) -> ::prasensEndingForMN
        word.endsWith(Endings.n) -> ::prasensEndingForMN
        word.endsWith(Endings.s) -> ::prasensEndingForSSsZ
        word.endsWith(Endings.ss) -> ::prasensEndingForSSsZ
        word.endsWith(Endings.z) -> ::prasensEndingForSSsZ
        word.endsWith(Endings.er) -> ::prasensEndingForErEl
        word.endsWith(Endings.el) -> ::prasensEndingForErEl
        else -> ::prasensEndingForUndefined
    }

    private fun prasensEndingForTD(person: Person): String = when (person) {
        Person.ICH -> "e"
        Person.DU -> "est"
        Person.MAN -> "et"
        Person.WIR -> "en"
        Person.IHR -> "et"
        Person.SIE -> "en"
    }

    private fun prasensEndingForMN(person: Person): String = when (person) {
        Person.ICH -> "e"
        Person.DU -> "est"
        Person.MAN -> "et"
        Person.WIR -> "en"
        Person.IHR -> "et"
        Person.SIE -> "en"
    }

    private fun prasensEndingForSSsZ(person: Person): String = when (person) {
        Person.ICH -> "e"
        Person.DU -> "t"
        Person.MAN -> "t"
        Person.WIR -> "en"
        Person.IHR -> "t"
        Person.SIE -> "en"
    }

    private fun prasensEndingForErEl(person: Person): String = when (person) {
        Person.ICH -> "e"
        Person.DU -> "st"
        Person.MAN -> "t"
        Person.WIR -> "n"
        Person.IHR -> "t"
        Person.SIE -> "n"
    }

    private fun prasensEndingForUndefined(person: Person): String = when (person) {
        Person.ICH -> "e"
        Person.DU -> "st"
        Person.MAN -> "t"
        Person.WIR -> "en"
        Person.IHR -> "t"
        Person.SIE -> "en"
    }
}