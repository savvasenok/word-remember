package xyz.savvamirzoyan.wordremember.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import xyz.savvamirzoyan.wordremember.view.fragment.AddWordFragment
import xyz.savvamirzoyan.wordremember.view.fragment.LearnWordFragment

class LearnWordAddWordViewPagerAdapter(
    private val pages: Int,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = pages

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> LearnWordFragment()
        1 -> AddWordFragment()
        else -> throw RuntimeException("Fragment #$itemCount does not exist")
    }
}