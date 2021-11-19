package xyz.savvamirzoyan.wordremember.data.status

import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWordData
import xyz.savvamirzoyan.wordremember.data.database.model.NounWordData
import xyz.savvamirzoyan.wordremember.data.entity.VerbWordWithVerbFormsBusiness
import xyz.savvamirzoyan.wordremember.data.entity.WordsListItem

sealed class WordsListStatus {

    class Words(val value: List<WordsListItem>) : WordsListStatus()

    sealed class ReturnBack : WordsListStatus() {
        class Noun(val nounWordData: NounWordData) : ReturnBack()
        class Verb(val verb: VerbWordWithVerbFormsBusiness) : ReturnBack()
        class Adjective(val adjectiveWordData: AdjectiveWordData) : ReturnBack()
    }

}
