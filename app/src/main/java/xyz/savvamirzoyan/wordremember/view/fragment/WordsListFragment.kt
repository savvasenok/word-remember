package xyz.savvamirzoyan.wordremember.view.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.adapter.WordsListRecyclerViewAdapter
import xyz.savvamirzoyan.wordremember.contract.adapter.IWordsListRecyclerViewSwipeGetWord
import xyz.savvamirzoyan.wordremember.contract.viewmodel.IViewModelDeleteSwipedItem
import xyz.savvamirzoyan.wordremember.data.repository.WordsListRepository
import xyz.savvamirzoyan.wordremember.databinding.FragmentWordsListBinding
import xyz.savvamirzoyan.wordremember.extension.flowListen
import xyz.savvamirzoyan.wordremember.extension.snackbar
import xyz.savvamirzoyan.wordremember.factory.WordsListViewModelFactory
import xyz.savvamirzoyan.wordremember.viewmodel.WordsListViewModel

class WordsListFragment : Fragment() {

    private var _binding: FragmentWordsListBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: WordsListViewModel? = null
    private val viewModel get() = _viewModel!!

    private var menuSearchView: SearchView? = null

    private val wordsListRecyclerViewAdapter = WordsListRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWordsListBinding.inflate(inflater, container, false)

        _viewModel = ViewModelProvider(
            this,
            WordsListViewModelFactory(WordsListRepository)
        ).get(WordsListViewModel::class.java)

        lifecycleScope
            .flowListen(::updateWordsList, viewLifecycleOwner)
            .flowListen(::showUndoSnackbarFlow, viewLifecycleOwner)

        binding.recyclerViewWordsList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewWordsList.adapter = wordsListRecyclerViewAdapter
        ItemTouchHelper(
            SwipeToDeleteWordCallback(
                wordsListRecyclerViewAdapter,
                viewModel
            )
        )
            .attachToRecyclerView(binding.recyclerViewWordsList)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Timber.i("onCreateOptionsMenu()")

        inflater.inflate(R.menu.words_list_menu, menu)

        menuSearchView = (menu.findItem(R.id.menu_search).actionView as SearchView).apply {
            isIconified = true
            maxWidth = Int.MAX_VALUE
            queryHint = getString(R.string.words_list_search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = true

                override fun onQueryTextChange(newText: String?): Boolean {

                    viewModel.performSearch(newText ?: "")

                    return true
                }
            })
        }
    }

    private suspend fun updateWordsList() {
        Timber.i("updateWordsList() called")

        viewModel.wordsListFlow.collect { words ->
            Timber.i("Collected words: ${words.size}")

            wordsListRecyclerViewAdapter.updateWords(words)
        }
    }

    private suspend fun showUndoSnackbarFlow() {
        Timber.i("showUndoSnackbarFlow()")

        viewModel.showUndoSnackbarFlow.collect { event ->
            Timber.i("showUndoSnackbarFlow() -> collect: $event")

            snackbar(
                R.string.words_list_snackbar_word_is_deleted,
                R.string.words_list_snackbar_undo,
                Snackbar.LENGTH_SHORT
            ) {
                when (event) {
                    is WordsListViewModel.WordsListEvent.ReturnBackAdjective ->
                        viewModel.returnAdjectiveToDB(event.adjectiveWord)
                    is WordsListViewModel.WordsListEvent.ReturnBackNoun ->
                        viewModel.returnNounToDB(event.nounWord)
                    is WordsListViewModel.WordsListEvent.ReturnBackVerb ->
                        viewModel.returnVerbToDB(event.verb)
                }
            }
        }
    }

    private class SwipeToDeleteWordCallback(
        private val adapter: IWordsListRecyclerViewSwipeGetWord,
        private val viewModel: IViewModelDeleteSwipedItem
    ) :
        ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT + ItemTouchHelper.RIGHT
        ) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            Timber.i("onSwiped()")

            val word = adapter.getWordByPosition(viewHolder.adapterPosition)
            viewModel.deleteWord(word)
        }
    }
}