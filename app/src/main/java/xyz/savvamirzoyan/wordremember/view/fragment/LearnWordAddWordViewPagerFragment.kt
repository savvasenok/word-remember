package xyz.savvamirzoyan.wordremember.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber
import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.adapter.viewpager.LearnWordAddWordViewPagerAdapter
import xyz.savvamirzoyan.wordremember.databinding.FragmentLearnWordAddWordViewPagerBinding

private const val PAGES = 2

class LearnWordAddWordViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentLearnWordAddWordViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLearnWordAddWordViewPagerBinding.inflate(inflater, container, false)

        binding.viewPager.adapter = LearnWordAddWordViewPagerAdapter(PAGES, this)

        setTabLayoutMediator()

        return binding.root
    }

    private fun setTabLayoutMediator() {
        Timber.i("setTabLayoutMediator() called")

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
}