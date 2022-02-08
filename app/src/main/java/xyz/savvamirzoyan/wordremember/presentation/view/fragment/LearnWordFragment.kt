package xyz.savvamirzoyan.wordremember.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.wordremember.R
import xyz.savvamirzoyan.wordremember.data.repository.LearnWordRepository
import xyz.savvamirzoyan.wordremember.databinding.FragmentLearnWordBinding
import xyz.savvamirzoyan.wordremember.domain.status.LearnWordStatus
import xyz.savvamirzoyan.wordremember.factory.LearnWordViewModelFactory
import xyz.savvamirzoyan.wordremember.presentation.viewmodel.LearnWordViewModel


class LearnWordFragment : Fragment() {

    private lateinit var binding: FragmentLearnWordBinding
    private lateinit var viewModel: LearnWordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLearnWordBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            LearnWordViewModelFactory(
                LearnWordRepository
            )
        ).get(LearnWordViewModel::class.java)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                learnWordStatusListener()
            }
        }

        return binding.root
    }

    private suspend fun learnWordStatusListener() {
        viewModel.learnWordStatusFlow.collect { status ->

            when (status) {
                is LearnWordStatus.TranslateFullWord -> {
                    binding.textViewTask.text = getString(status.questionStringId)
                    binding.textViewWord.text = status.word
                    binding.linearLayoutAnswerOptions.removeAllViews()
                    status.answers.forEach { answer ->
                        binding.linearLayoutAnswerOptions.addView(
                            MaterialButton(requireContext()).apply {
                                text = answer.text
                                setOnClickListener {
                                    viewModel.onAnswerButtonClick(answer.isCorrect)
                                }
                            }
                        )
                    }
                }
                null -> {
                    binding.textViewWord.text =
                        getString(R.string.output_error_not_enough_words)
                }
            }
        }
    }
}
