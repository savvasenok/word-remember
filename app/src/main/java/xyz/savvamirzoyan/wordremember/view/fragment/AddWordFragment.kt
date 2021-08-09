package xyz.savvamirzoyan.wordremember.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.wordremember.data.repository.AddWordRepository
import xyz.savvamirzoyan.wordremember.data.state.VerbFormType
import xyz.savvamirzoyan.wordremember.databinding.FragmentAddWordBinding
import xyz.savvamirzoyan.wordremember.factory.AddWordViewModelFactory
import xyz.savvamirzoyan.wordremember.viewmodel.AddWordViewModel

class AddWordFragment : Fragment() {

    private lateinit var binding: FragmentAddWordBinding
    private lateinit var viewModel: AddWordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddWordBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            AddWordViewModelFactory(
                AddWordRepository()
            )
        ).get(AddWordViewModel::class.java)

        setNounGenderOptions(viewModel.nounGenders)
        setOnNounGenderChangeListener()
        setOnWordChangeListener()
        setOnWordTypeRadioButtonGroupChangeListener()
        setOnWordPluralFormChangeListener()
        setOnOnlyPluralSwitchChangeListener()
        setOnTranslationChangeListener()
        setOnButtonSaveClickListener()
        setOnVerbFormsChangeListeners()

        lifecycleScope
            .flowListen(::setOnNounGenderStatusChangeListener)
            .flowListen(::setOnWordStatusChangeListener)
            .flowListen(::setOnVerbFormsVisibilityStatusChangeListener)
            .flowListen(::setOnWordPluralFormStatusChangeListener)
            .flowListen(::setOnTranslationStatusChangeListener)
            .flowListen(::setOnSaveButtonIsEnabledChangeListener)
            .flowListen(::setOnSwitchOnlyPluralVisibilityListener)
            .flowListen(::setOnClearAllInputStatusChangeListener)

        return binding.root
    }

    private fun CoroutineScope.flowListen(function: suspend () -> Unit): CoroutineScope {
        launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                function()
            }
        }

        return this
    }

    private fun setNounGenderOptions(items: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        (binding.textInputWordGender).setAdapter(adapter)
    }

    private fun setOnNounGenderChangeListener() {
        binding.textInputWordGender.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.onGenderChange(s?.toString() ?: "")
            }
        })
    }

    private suspend fun setOnNounGenderStatusChangeListener() {
        viewModel.genderStatusFlow.collect { status ->
            binding.textInputLayoutWordGender.apply {
                isEnabled = status.isEnabled
                error = status.error?.let { getString(it) }
                helperText = getString(status.helperText)
                visibility = status.visibility
            }
        }
    }

    private fun setOnWordChangeListener() {
        binding.textInputWord.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.onWordChange(s?.toString() ?: "")
            }
        })
    }

    private suspend fun setOnWordStatusChangeListener() {
        viewModel.wordStatusFlow.collect { status ->
            binding.textInputLayoutWord.apply {
                isEnabled = status.isEnabled
                error = status.error?.let { getString(it) }
                helperText = getString(status.helperText)
                visibility = status.visibility
            }
        }
    }

    private fun setOnWordTypeRadioButtonGroupChangeListener() {
        binding.radioButtonWordTypeNoun.setOnCheckedChangeListener { _, isChecked ->
            binding.switchOnlyPlural.isChecked = false
            viewModel.onWordTypeNounChange(isChecked)
        }
        binding.radioButtonWordTypeVerb.setOnCheckedChangeListener { _, isChecked ->
            binding.switchOnlyPlural.isChecked = false
            viewModel.onWordTypeVerbChange(isChecked)
        }
        binding.radioButtonWordTypeAdjective.setOnCheckedChangeListener { _, isChecked ->
            binding.switchOnlyPlural.isChecked = false
            viewModel.onWordTypeAdjectiveChange(isChecked)
        }
    }

    private suspend fun setOnVerbFormsVisibilityStatusChangeListener() {
        viewModel.verbFormsVisibilityStatusFlow.collect { visibility ->
            binding.linearLayoutVerbForms.visibility = visibility
        }
    }

    private fun setOnWordPluralFormChangeListener() {
        binding.textInputWordPlural.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.onWordPluralFormChange(s?.toString() ?: "")
            }
        })
    }

    private suspend fun setOnWordPluralFormStatusChangeListener() {
        viewModel.wordPluralFormStatusFlow.collect { status ->
            binding.textInputLayoutWordPlural.apply {
                isEnabled = status.isEnabled
                error = status.error?.let { getString(it) }
                helperText = getString(status.helperText)
                visibility = status.visibility
            }
        }
    }

    private fun setOnOnlyPluralSwitchChangeListener() {
        binding.switchOnlyPlural.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onOnlyPluralChange(isChecked)
        }
    }

    private fun setOnTranslationChangeListener() {
        binding.textInputTranslation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.onTranslationChange(s?.toString() ?: "")
            }
        })
    }

    private suspend fun setOnTranslationStatusChangeListener() {
        viewModel.translationStatusFlow.collect { status ->
            binding.textInputLayoutTranslation.apply {
                isEnabled = status.isEnabled
                error = status.error?.let { getString(it) }
                helperText = getString(status.helperText)
                visibility = status.visibility
            }
        }
    }

    private suspend fun setOnSaveButtonIsEnabledChangeListener() {
        viewModel.saveButtonIsEnabledFlow.collect {
            if (binding.buttonSave.isEnabled != it) {
                binding.buttonSave.isEnabled = it
            }
        }
    }

    private fun setOnButtonSaveClickListener() {
        binding.buttonSave.setOnClickListener {
            viewModel.onButtonSaveClick()
        }
    }

    private suspend fun setOnSwitchOnlyPluralVisibilityListener() {
        viewModel.onlyPluralSwitchVisibilityStatusFlow.collect {
            binding.switchOnlyPlural.visibility = it
        }
    }

    private suspend fun setOnClearAllInputStatusChangeListener() {
        viewModel.clearAllInputStatusFlow.collect {
            binding.textInputWordGender.text = null
            binding.textInputWord.text = null
            binding.radioButtonWordTypeNoun.isChecked = true
            binding.textInputWordPlural.text = null
            binding.switchOnlyPlural.isChecked = false
            binding.textInputTranslation.text = null
            binding.textInputPrasensIch.text = null
            binding.textInputPrasensDu.text = null
            binding.textInputPrasensErSieEs.text = null
            binding.textInputPrasensWir.text = null
            binding.textInputPrasensIhr.text = null
            binding.textInputPrasensSieSie.text = null
            binding.textInputPrateritumIch.text = null
            binding.textInputPrateritumDu.text = null
            binding.textInputPrateritumErSieEs.text = null
            binding.textInputPrateritumWir.text = null
            binding.textInputPrateritumIhr.text = null
            binding.textInputPrateritumSieSie.text = null
            binding.textInputPerfectIch.text = null
            binding.textInputPerfectDu.text = null
            binding.textInputPerfectErSieEs.text = null
            binding.textInputPerfectWir.text = null
            binding.textInputPerfectIhr.text = null
            binding.textInputPerfectSieSie.text = null
        }
    }

    private fun setOnVerbFormsChangeListeners() {
        binding.textInputPrasensIch.addTextChangedListener(textWatcher(VerbFormType.PRASENS_ICH))
        binding.textInputPrasensDu.addTextChangedListener(textWatcher(VerbFormType.PRASENS_DU))
        binding.textInputPrasensErSieEs.addTextChangedListener(textWatcher(VerbFormType.PRASENS_ER_SIE_ES))
        binding.textInputPrasensWir.addTextChangedListener(textWatcher(VerbFormType.PRASENS_WIR))
        binding.textInputPrasensIhr.addTextChangedListener(textWatcher(VerbFormType.PRASENS_IHR))
        binding.textInputPrasensSieSie.addTextChangedListener(textWatcher(VerbFormType.PRASENS_SIE_SIE))

        binding.textInputPrateritumIch.addTextChangedListener(textWatcher(VerbFormType.PRATERITUM_ICH))
        binding.textInputPrateritumDu.addTextChangedListener(textWatcher(VerbFormType.PRATERITUM_DU))
        binding.textInputPrateritumErSieEs.addTextChangedListener(textWatcher(VerbFormType.PRATERITUM_ER_SIE_ES))
        binding.textInputPrateritumWir.addTextChangedListener(textWatcher(VerbFormType.PRATERITUM_WIR))
        binding.textInputPrateritumIhr.addTextChangedListener(textWatcher(VerbFormType.PRATERITUM_IHR))
        binding.textInputPrateritumSieSie.addTextChangedListener(textWatcher(VerbFormType.PRATERITUM_SIE_SIE))

        binding.textInputPerfectIch.addTextChangedListener(textWatcher(VerbFormType.PERFEKT_ICH))
        binding.textInputPerfectDu.addTextChangedListener(textWatcher(VerbFormType.PERFEKT_DU))
        binding.textInputPerfectErSieEs.addTextChangedListener(textWatcher(VerbFormType.PERFEKT_ER_SIE_ES))
        binding.textInputPerfectWir.addTextChangedListener(textWatcher(VerbFormType.PERFEKT_WIR))
        binding.textInputPerfectIhr.addTextChangedListener(textWatcher(VerbFormType.PERFEKT_IHR))
        binding.textInputPerfectSieSie.addTextChangedListener(textWatcher(VerbFormType.PERFEKT_SIE_SIE))
    }

    private fun textWatcher(forType: VerbFormType) = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            viewModel.onVerbFormChange(s?.toString(), forType)
        }
    }
}