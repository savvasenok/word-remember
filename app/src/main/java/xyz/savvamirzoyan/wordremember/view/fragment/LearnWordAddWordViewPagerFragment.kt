package xyz.savvamirzoyan.wordremember.view.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.adapter.viewpager.LearnWordAddWordViewPagerAdapter
import xyz.savvamirzoyan.wordremember.databinding.FragmentLearnWordAddWordViewPagerBinding

private const val PAGES = 2

class LearnWordAddWordViewPagerFragment : Fragment() {

    private var _binding: FragmentLearnWordAddWordViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLearnWordAddWordViewPagerBinding.inflate(inflater, container, false)

        binding.viewPager.adapter = LearnWordAddWordViewPagerAdapter(PAGES, this)

        setTabLayoutMediator()
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun setTabLayoutMediator() {
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager,
            true
        ) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.learn_word_fragment_title)
                1 -> getString(R.string.add_word_fragment_title)
                else -> throw RuntimeException("Fragment #$position does not have a name")
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_words_list -> {
                val action = LearnWordAddWordViewPagerFragmentDirections.toWordsListFragment()
                Navigation.findNavController(binding.root).navigate(action)
                true
            }
            R.id.menu_info -> {
                val action = LearnWordAddWordViewPagerFragmentDirections.toAppInfo()
                Navigation.findNavController(binding.root).navigate(action)
                true
            }

            else -> false
        }
    }
}