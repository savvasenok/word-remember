package xyz.savvamirzoyan.wordremember.data.state

import androidx.annotation.StringRes
import xyz.savvamirzoyan.wordremember.data.entity.QuestionAnswer

sealed class LearnWordStatus {

    class TranslateFullWord(
        @StringRes val questionStringId: Int,
        val word: String,
        val answers: List<QuestionAnswer>
    ) : LearnWordStatus()
}