package xyz.savvamirzoyan.wordremember.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.contract.adapter.IWordsListRecyclerViewAdapter
import xyz.savvamirzoyan.wordremember.contract.adapter.IWordsListRecyclerViewSwipeGetWord
import xyz.savvamirzoyan.wordremember.data.entity.ui.WordsListItemUI
import xyz.savvamirzoyan.wordremember.databinding.LayoutWordsListItemAdjectiveBinding
import xyz.savvamirzoyan.wordremember.databinding.LayoutWordsListItemNounBinding
import xyz.savvamirzoyan.wordremember.databinding.LayoutWordsListItemVerbBinding

class WordsListNounViewHolder(
    private val viewBinding: LayoutWordsListItemNounBinding
) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(data: WordsListItemUI.WordsListItemNounUI) {
        data.map(viewBinding, this.itemView.context)
    }
}

class WordsListVerbViewHolder(
    private val viewBinding: LayoutWordsListItemVerbBinding
) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(data: WordsListItemUI.WordsListItemVerbUI) {
        data.map(viewBinding, this.itemView.context)
//        viewBinding.textViewWord.text = data.word
//        viewBinding.textViewTranslation.text = data.translation
//        viewBinding.textViewWordsListItemVerbPrateritum.text = data.prateritum
//        viewBinding.textViewWordsListItemVerbPerfect.text = data.perfekt

//        data.verbFormsData.apply {
//            viewBinding.textViewVerbFormIchPrasens.text = this.prasensIch
//            viewBinding.textViewVerbFormDuPrasens.text = this.prasensDu
//            viewBinding.textViewVerbFormErSieEsPrasens.text = this.prasensErSieEs
//            viewBinding.textViewVerbFormWirPrasens.text = this.prasensWir
//            viewBinding.textViewVerbFormIhrPrasens.text = this.prasensIhr
//            viewBinding.textViewVerbFormSieSiePrasens.text = this.prasensSieSie
//
//            viewBinding.textViewVerbFormIchPrateritum.text = this.prateritumIch
//            viewBinding.textViewVerbFormDuPrateritum.text = this.prateritumDu
//            viewBinding.textViewVerbFormErSieEsPrateritum.text = this.prateritumErSieEs
//            viewBinding.textViewVerbFormWirPrateritum.text = this.prateritumWir
//            viewBinding.textViewVerbFormIhrPrateritum.text = this.prateritumIhr
//            viewBinding.textViewVerbFormSieSiePrateritum.text = this.prateritumSieSie
//        }
//
//        if (data.isSubContentVisible) {
//            viewBinding.linearLayoutVerbForms.visibility = View.VISIBLE
//        }
//
//        viewBinding.cardView.setOnClickListener {
//            if (viewBinding.linearLayoutVerbForms.visibility == View.GONE) {
//                viewBinding.linearLayoutVerbForms.visibility = View.VISIBLE
//                data.isSubContentVisible = true
//            } else {
//                viewBinding.linearLayoutVerbForms.visibility = View.GONE
//                data.isSubContentVisible = false
//            }
//
//            changeNotifier()
//        }
    }
}

class WordsListAdjectiveViewHolder(
    private val viewBinding: LayoutWordsListItemAdjectiveBinding
) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(data: WordsListItemUI.WordsListItemAdjectiveUI) {
        data.map(viewBinding, null)
//            if (data.superlativ.isNotBlank()) "am ${data.superlativ}" else ""
    }
}

class WordsListRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    IWordsListRecyclerViewAdapter, IWordsListRecyclerViewSwipeGetWord {

    private companion object {
        private const val ITEM_NOUN = 0
        private const val ITEM_NOUN_PLURAL = 1
        private const val ITEM_VERB = 2
        private const val ITEM_VERB_WITH_FORMS = 3
        private const val ITEM_ADJECTIVE = 4
    }

    private val words = mutableListOf<WordsListItemUI>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_NOUN -> WordsListNounViewHolder(
                LayoutWordsListItemNounBinding.bind(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.layout_words_list_item_noun, parent, false)
                )
            )
            ITEM_VERB -> WordsListVerbViewHolder(
                LayoutWordsListItemVerbBinding.bind(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.layout_words_list_item_verb, parent, false)
                )
            )
            ITEM_ADJECTIVE -> WordsListAdjectiveViewHolder(
                LayoutWordsListItemAdjectiveBinding.bind(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.layout_words_list_item_adjective, parent, false)
                )
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WordsListNounViewHolder -> holder.bind(words[position] as WordsListItemUI.WordsListItemNounUI)
            is WordsListVerbViewHolder -> holder.bind(words[position] as WordsListItemUI.WordsListItemVerbUI)
            is WordsListAdjectiveViewHolder -> holder.bind(words[position] as WordsListItemUI.WordsListItemAdjectiveUI)
        }
    }

    override fun getItemCount(): Int = words.size

    override fun updateWords(newWords: List<WordsListItemUI>) {
        val callback = WordsListDiffCallback(words, newWords)
        val differentWords = DiffUtil.calculateDiff(callback)
        words.clear()
        words.addAll(newWords)
        Timber.i("updateWords!")
        differentWords.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int = when (words[position]) {
//        is WordsListItemUI.WordsListItemVerbWithVerbFormsUI -> ITEM_VERB_WITH_FORMS
//        is WordsListItemUI.WordsListItemNounWithPluralUI -> ITEM_NOUN_PLURAL
        is WordsListItemUI.WordsListItemAdjectiveUI -> ITEM_ADJECTIVE
        is WordsListItemUI.WordsListItemNounUI -> ITEM_NOUN
        is WordsListItemUI.WordsListItemVerbUI -> ITEM_VERB
    }

    override fun getWordByPosition(position: Int): WordsListItemUI = words[position]

    private class WordsListDiffCallback(
        private val oldList: List<WordsListItemUI>,
        private val newList: List<WordsListItemUI>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldList[oldItemPosition]
            val new = newList[newItemPosition]

            return (old == new) && (old == new)
        }
    }
}