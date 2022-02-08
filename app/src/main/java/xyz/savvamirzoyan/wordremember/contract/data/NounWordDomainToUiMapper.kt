package xyz.savvamirzoyan.wordremember.contract.data

import android.content.Context
import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.core.Mapper
import xyz.savvamirzoyan.wordremember.data.types.WordGender
import xyz.savvamirzoyan.wordremember.domain.model.NounWordDomain
import xyz.savvamirzoyan.wordremember.presentation.model.WordsListItemUI

class NounWordDomainToUiMapper(
    private val context: Context
) : Mapper<NounWordDomain, WordsListItemUI.WordsListItemNounUI> {
    override fun map(data: NounWordDomain): WordsListItemUI.WordsListItemNounUI {

        val colorId = when (data.gender) {
            WordGender.DER -> R.color.gender_man
            WordGender.DIE -> R.color.gender_woman
            WordGender.DAS -> R.color.gender_it
            null -> R.color.gender_plural
        }

        return WordsListItemUI.WordsListItemNounUI(
            data.word ?: "",
            data.translation,
            data.gender.toString().lowercase(),
            context.getColor(colorId)
        )
    }
}