package xyz.savvamirzoyan.wordremember.data.status

import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWord
import xyz.savvamirzoyan.wordremember.data.database.model.NounWord
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWordWithVerbForms
import xyz.savvamirzoyan.wordremember.data.entity.WordsListItem

sealed class WordsListStatus {

    class Words(val value: List<WordsListItem>) : WordsListStatus()

    sealed class ReturnBack : WordsListStatus() {
        class Noun(val nounWord: NounWord) : ReturnBack()
        class Verb(val verb: VerbWordWithVerbForms) : ReturnBack()
        class Adjective(val adjectiveWord: AdjectiveWord) : ReturnBack()
    }

}
