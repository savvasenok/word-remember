package xyz.savvamirzoyan.wordremember.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.contract.adapter.IWordsListRecyclerViewAdapter
import xyz.savvamirzoyan.wordremember.data.entity.WordsListItem
import xyz.savvamirzoyan.wordremember.databinding.LayoutWordsListItemBinding

class WordsListViewHolder(
    private val viewBinding: LayoutWordsListItemBinding
) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(data: WordsListItem) {
        viewBinding.textViewWord.text = data.word
        viewBinding.textViewTranslation.text = data.translation
    }
}

class WordsListRecyclerViewAdapter : RecyclerView.Adapter<WordsListViewHolder>(),
    IWordsListRecyclerViewAdapter {

    private val words = mutableListOf<WordsListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsListViewHolder {
        return WordsListViewHolder(
            LayoutWordsListItemBinding.bind(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.layout_words_list_item, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: WordsListViewHolder, position: Int) {
        holder.bind(words[position])
    }

    override fun getItemCount(): Int = words.size

    override fun updateWords(newWords: List<WordsListItem>) {
        val callback = WordsListDiffCallback(words, newWords)
        val differentWords = DiffUtil.calculateDiff(callback)
        words.clear()
        words.addAll(newWords)
        differentWords.dispatchUpdatesTo(this)
    }

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
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}