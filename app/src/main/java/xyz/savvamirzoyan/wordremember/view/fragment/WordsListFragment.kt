package xyz.savvamirzoyan.wordremember.view.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.adapter.WordsListRecyclerViewAdapter
import xyz.savvamirzoyan.wordremember.data.repository.WordsListRepository
import xyz.savvamirzoyan.wordremember.databinding.FragmentWordsListBinding
import xyz.savvamirzoyan.wordremember.extension.flowListen
import xyz.savvamirzoyan.wordremember.factory.WordsListViewModelFactory
import xyz.savvamirzoyan.wordremember.viewmodel.WordsListViewModel

class WordsListFragment : Fragment() {

    private var _binding: FragmentWordsListBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: WordsListViewModel? = null
    private val viewModel get() = _viewModel!!

    private var menuSearchView: SearchView? = null

    private var wordsListRecyclerViewAdapter =
        WordsListRecyclerViewAdapter()

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

        binding.recyclerViewWordsList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewWordsList.adapter = wordsListRecyclerViewAdapter

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
    }

    private suspend fun updateWordsList() {
        Timber.i("updateWordsList() called")

        viewModel.wordsListFlow.collect { words ->
            Timber.i("Collected words: ${words.size}")

            wordsListRecyclerViewAdapter.updateWords(words)
        }
    }
}