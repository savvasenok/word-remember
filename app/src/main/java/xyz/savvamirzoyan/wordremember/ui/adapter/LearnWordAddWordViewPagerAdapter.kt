package xyz.savvamirzoyan.wordremember.ui.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import xyz.savvamirzoyan.wordremember.ui.view.fragment.AddWordFragment
import xyz.savvamirzoyan.wordremember.ui.view.fragment.LearnWordFragment

class LearnWordAddWordViewPagerAdapter(
    private val pages: Int,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    private val learnWordFragment = LearnWordFragment()
    private val addWordFragment = AddWordFragment()

    override fun getItemCount(): Int = pages

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> learnWordFragment
        1 -> addWordFragment
        else -> throw RuntimeException("Fragment #$itemCount does not exist")
    }
}