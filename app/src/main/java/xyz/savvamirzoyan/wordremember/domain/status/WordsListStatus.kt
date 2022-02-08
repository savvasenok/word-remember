package xyz.savvamirzoyan.wordremember.domain.status

import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWordData
import xyz.savvamirzoyan.wordremember.data.database.model.NounWordData
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordWithVerbFormsData
import xyz.savvamirzoyan.wordremember.presentation.model.WordsListItemUI

sealed class WordsListStatus {

    class Words(val value: List<WordsListItemUI>) : WordsListStatus()

    sealed class ReturnBack : WordsListStatus() {
        class Noun(val nounWordData: NounWordData) : ReturnBack()
        class Verb(val verb: VerbWordWithVerbFormsData) : ReturnBack()
        class Adjective(val adjectiveWordData: AdjectiveWordData) : ReturnBack()
    }
}
