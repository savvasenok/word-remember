package xyz.savvamirzoyan.wordremember.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.contract.adapter.IWordsListRecyclerViewAdapter
import xyz.savvamirzoyan.wordremember.contract.adapter.IWordsListRecyclerViewSwipeGetWord
import xyz.savvamirzoyan.wordremember.data.entity.WordsListItem
import xyz.savvamirzoyan.wordremember.data.types.WordGender
import xyz.savvamirzoyan.wordremember.databinding.LayoutWordsListItemAdjectiveBinding
import xyz.savvamirzoyan.wordremember.databinding.LayoutWordsListItemNounBinding
import xyz.savvamirzoyan.wordremember.databinding.LayoutWordsListItemVerbBinding

class WordsListNounViewHolder(
    private val viewBinding: LayoutWordsListItemNounBinding
) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(data: WordsListItem.WordsListItemNoun) {
        viewBinding.textViewWord.text = data.word
        viewBinding.textViewTranslation.text = data.translation
        viewBinding.textViewWordGender.apply {
            text = data.gender?.toString()?.lowercase() ?: "die"
            val color = when (data.gender) {
                WordGender.DER -> R.color.gender_man
                WordGender.DIE -> R.color.gender_woman
                WordGender.DAS -> R.color.gender_it
                null -> R.color.gender_plural
            }

            setTextColor(
                viewBinding.textViewWordGender.context.getColor(color)
            )
        }
    }
}

class WordsListVerbViewHolder(
    private val viewBinding: LayoutWordsListItemVerbBinding
) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(data: WordsListItem.WordsListItemVerb) {
        viewBinding.textViewWord.text = data.word
        viewBinding.textViewTranslation.text = data.translation
        viewBinding.textViewWordsListItemVerbPrateritum.text = data.prateritum
        viewBinding.textViewWordsListItemVerbPerfect.text = data.perfect
    }
}

class WordsListAdjectiveViewHolder(
    private val viewBinding: LayoutWordsListItemAdjectiveBinding
) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(data: WordsListItem.WordsListItemAdjective) {
        viewBinding.textViewWord.text = data.word
        viewBinding.textViewTranslation.text = data.translation
        viewBinding.textViewWordsListItemAdjectiveKomparativ.text = data.komparativ
        viewBinding.textViewWordsListItemAdjectiveSuperlativ.text =
            if (data.superlativ.isNotBlank()) "am ${data.superlativ}" else ""
    }
}

class WordsListRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    IWordsListRecyclerViewAdapter, IWordsListRecyclerViewSwipeGetWord {

    private val words = mutableListOf<WordsListItem>()

    private val ITEM_NOUN = 0
    private val ITEM_VERB = 1
    private val ITEM_ADJECTIVE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> WordsListNounViewHolder(
                LayoutWordsListItemNounBinding.bind(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.layout_words_list_item_noun, parent, false)
                )
            )
            1 -> WordsListVerbViewHolder(
                LayoutWordsListItemVerbBinding.bind(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.layout_words_list_item_verb, parent, false)
                )
            )
            2 -> WordsListAdjectiveViewHolder(
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
            is WordsListNounViewHolder -> holder.bind(words[position] as WordsListItem.WordsListItemNoun)
            is WordsListVerbViewHolder -> holder.bind(words[position] as WordsListItem.WordsListItemVerb)
            is WordsListAdjectiveViewHolder -> holder.bind(words[position] as WordsListItem.WordsListItemAdjective)
        }
    }

    override fun getItemCount(): Int = words.size

    override fun updateWords(newWords: List<WordsListItem>) {
        Timber.i("updateWords(newWords: ${newWords.joinToString(", ")})")

        val callback = WordsListDiffCallback(words, newWords)
        val differentWords = DiffUtil.calculateDiff(callback)
        words.clear()
        words.addAll(newWords)
        differentWords.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int = when (words[position]) {
        is WordsListItem.WordsListItemAdjective -> ITEM_ADJECTIVE
        is WordsListItem.WordsListItemNoun -> ITEM_NOUN
        is WordsListItem.WordsListItemVerb -> ITEM_VERB
    }

    override fun getWordByPosition(position: Int): WordsListItem = words[position]

    private class WordsListDiffCallback(
        private val oldList: List<WordsListItem>,
        private val newList: List<WordsListItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].word == newList[newItemPosition].word
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldList[oldItemPosition]
            val new = newList[newItemPosition]

            return (old.word == new.word) && (old.translation == new.translation)
        }
    }
}