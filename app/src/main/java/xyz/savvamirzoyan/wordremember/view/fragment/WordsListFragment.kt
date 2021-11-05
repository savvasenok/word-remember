package xyz.savvamirzoyan.wordremember.view.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.wordremember.BuildConfig
import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.adapter.WordsListRecyclerViewAdapter
import xyz.savvamirzoyan.wordremember.contract.adapter.IWordsListRecyclerViewSwipeGetWord
import xyz.savvamirzoyan.wordremember.contract.viewmodel.IViewModelDeleteSwipedItem
import xyz.savvamirzoyan.wordremember.data.repository.WordsListRepository
import xyz.savvamirzoyan.wordremember.data.status.WordsListStatus
import xyz.savvamirzoyan.wordremember.databinding.FragmentWordsListBinding
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

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                wordsListStatusListener()
            }
        }

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

        menu.apply {
            findItem(R.id.menu_add_random_words).isVisible = BuildConfig.DEBUG
            findItem(R.id.menu_delete_all_words).isVisible = BuildConfig.DEBUG
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_sort_reverse -> {
                item.isChecked = !item.isChecked
                viewModel.reverseWordsUpdated(item.isChecked)
            }
            R.id.menu_sort_alphabetical -> {
                item.isChecked = true
                viewModel.sortOptionUpdatedToAlphabetical()
            }
            R.id.menu_sort_type_or_gender -> {
                item.isChecked = true
                viewModel.sortOptionUpdatedToWordTypeOrGender()
            }
            R.id.menu_add_random_words -> {
                viewModel.addRandomWords()
            }
            R.id.menu_delete_all_words -> {
                viewModel.deleteAllWords()
            }
            android.R.id.home -> {
                requireActivity().onBackPressed()
            }
        }

        return true
    }

    private suspend fun wordsListStatusListener() {
        viewModel.wordsListStatusFlow.collect { status ->
            when (status) {
                is WordsListStatus.Words -> {
                    binding.recyclerViewWordsList.smoothScrollToPosition(0)
                    wordsListRecyclerViewAdapter.updateWords(status.value)
                }
                is WordsListStatus.ReturnBack -> {
                    snackbar(
                        R.string.words_list_snackbar_word_is_deleted,
                        R.string.words_list_snackbar_undo,
                        Snackbar.LENGTH_SHORT
                    ) {
                        when (status) {
                            is WordsListStatus.ReturnBack.Adjective ->
                                viewModel.returnAdjectiveToDB(status.adjectiveWord)
                            is WordsListStatus.ReturnBack.Noun ->
                                viewModel.returnNounToDB(status.nounWord)
                            is WordsListStatus.ReturnBack.Verb ->
                                viewModel.returnVerbToDB(status.verb)
                            is WordsListStatus.Words -> {
                            } // bug in intellij idea
                        }
                    }
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
            val word = adapter.getWordByPosition(viewHolder.adapterPosition)
            viewModel.deleteWord(word)
        }
    }
}