package xyz.savvamirzoyan.wordremember.data.entity.ui

import android.content.Context
import androidx.annotation.ColorRes
import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.contract.presentation.IModelUiToViewMapper
import xyz.savvamirzoyan.wordremember.core.Abstract
import xyz.savvamirzoyan.wordremember.databinding.LayoutWordsListItemAdjectiveBinding
import xyz.savvamirzoyan.wordremember.databinding.LayoutWordsListItemNounBinding
import xyz.savvamirzoyan.wordremember.databinding.LayoutWordsListItemVerbBinding

sealed class WordsListItemUI : Abstract.Object<Unit, Abstract.Mapper.Empty> {

    sealed class WordsListItemNounUI : WordsListItemUI(),
        IModelUiToViewMapper<LayoutWordsListItemNounBinding> {

        override fun map(mapper: Abstract.Mapper.Empty) {
            // It does not maps to anything
        }

        data class Success(
            private val word: String,
            private val translation: String,
            private val gender: String,
            @ColorRes val color: Int
        ) : WordsListItemNounUI() {

            override fun map(view: LayoutWordsListItemNounBinding, context: Context?) {
                view.apply {
                    textViewWord.text = word
                    textViewWordGender.text = gender
                    textViewTranslation.text = translation
                    context?.getColor(color)?.let { textViewWordGender.setTextColor(it) }
                }
            }
        }

        data class Fail(private val e: Exception) : WordsListItemNounUI() {
            override fun map(view: LayoutWordsListItemNounBinding, context: Context?) {}
        }
    }

//    sealed class WordsListItemNounWithPluralUI : WordsListItemUI() {
//
//        data class Success(
//            private val word: String,
//            private val translation: String,
//            private val gender: String,
//            @ColorRes private val colorId: Int,
//            private val plural: String
//        ) : WordsListItemNounWithPluralUI() {
//            override fun map(mapper: Abstract.Mapper.Empty) {
//                // It does not maps to anything
//            }
//        }
//
//        data class Fail(private val e: Exception) : WordsListItemNounWithPluralUI() {
//            override fun map(mapper: Abstract.Mapper.Empty) {
//                // It does not maps to anything
//            }
//        }
//    }

    sealed class WordsListItemVerbUI : WordsListItemUI(),
        IModelUiToViewMapper<LayoutWordsListItemVerbBinding> {

        override fun map(mapper: Abstract.Mapper.Empty) {}

        data class Success(
            private val word: String,
            private val translation: String,
            private val prateritum: String,
            private val perfekt: String
        ) : WordsListItemVerbUI() {
            override fun map(view: LayoutWordsListItemVerbBinding, context: Context?) {
                view.apply {
                    textViewWord.text = word
                    textViewTranslation.text = word
                    textViewWordsListItemVerbPrateritum.text = prateritum
                    textViewWordsListItemVerbPerfect.text = perfekt
                }
            }
        }

        data class Fail(private val e: Exception) : WordsListItemVerbUI() {
            override fun map(view: LayoutWordsListItemVerbBinding, context: Context?) {}
        }

    }

//    data class WordsListItemVerbWithVerbFormsUI(
//        val word: String,
//        val translation: String,
//        val prateritum: String,
//        val perfekt: String,
//        val verbFormsData: VerbFormData
//    ) : WordsListItemUI() {
//        override fun map(mapper: Abstract.Mapper.Empty) {
//            // It does not maps to anything
//        }
//    }

    sealed class WordsListItemAdjectiveUI : WordsListItemUI(),
        IModelUiToViewMapper<LayoutWordsListItemAdjectiveBinding> {

        override fun map(mapper: Abstract.Mapper.Empty) {
            // It does not maps to anything
        }

        data class Success(
            private val word: String,
            private val translation: String,
            private val komparativ: String,
            private val superlativ: String
        ) : WordsListItemAdjectiveUI() {

            override fun map(view: LayoutWordsListItemAdjectiveBinding, context: Context?) {
                view.apply {
                    textViewWord.text = word
                    textViewTranslation.text = translation
                    textViewWordsListItemAdjectiveKomparativ.text = komparativ

                    if (context == null) {
                        textViewWordsListItemAdjectiveSuperlativ.text = superlativ
                    } else {
                        val superlativWithPrefix =
                            context.getString(R.string.words_list_superlativ_prefix, superlativ)
                        textViewWordsListItemAdjectiveSuperlativ.text = superlativWithPrefix
                    }
                }
            }
        }

        data class Fail(private val e: Exception) : WordsListItemAdjectiveUI() {
            override fun map(view: LayoutWordsListItemAdjectiveBinding, context: Context?) {}
        }
    }
}
