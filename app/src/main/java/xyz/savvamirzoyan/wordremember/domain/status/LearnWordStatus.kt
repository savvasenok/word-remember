package xyz.savvamirzoyan.wordremember.domain.status

import androidx.annotation.StringRes
import xyz.savvamirzoyan.wordremember.domain.model.QuestionAnswer

sealed class LearnWordStatus {

    class TranslateFullWord(
        @StringRes val questionStringId: Int,
        val word: String,
        val answers: List<QuestionAnswer>
    ) : LearnWordStatus()

}