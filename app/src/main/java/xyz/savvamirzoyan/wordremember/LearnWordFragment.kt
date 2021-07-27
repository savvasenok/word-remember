package xyz.savvamirzoyan.wordremember

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import xyz.savvamirzoyan.wordremember.databinding.FragmentLearnWordBinding


class LearnWordFragment : Fragment() {

    private lateinit var binding: FragmentLearnWordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLearnWordBinding.inflate(inflater, container, false)

        return binding.root
    }
}