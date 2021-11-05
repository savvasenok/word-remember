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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.wordremember.data.repository.AddWordRepository
import xyz.savvamirzoyan.wordremember.data.state.VerbFormType
import xyz.savvamirzoyan.wordremember.data.status.AddWordStatus
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
            AddWordViewModelFactory(AddWordRepository)
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
        setOnAdjectiveFormsChangeListener()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addWordStatusListener()
            }
        }

        return binding.root
    }

    private suspend fun addWordStatusListener() {
        viewModel.addWordStatusFlow.collect { status ->
            when (status) {
                is AddWordStatus.Repeatable.AdjectiveFormsVisibility -> {
                    binding.linearLayoutAdjectiveForms.visibility = status.value
                }

                is AddWordStatus.Repeatable.Gender -> binding.textInputLayoutWordGender.apply {
                    isEnabled = status.value.isEnabled
                    error = status.value.error?.let { getString(it) }
                    helperText = getString(status.value.helperText)
                    visibility = status.value.visibility
                }

                is AddWordStatus.Repeatable.OnlyPluralSwitchVisibility -> {
                    binding.switchOnlyPlural.visibility = status.value
                }

                is AddWordStatus.Repeatable.Plural -> binding.textInputLayoutWordPlural.apply {
                    isEnabled = status.value.isEnabled
                    error = status.value.error?.let { getString(it) }
                    helperText = getString(status.value.helperText)
                    visibility = status.value.visibility
                }

                is AddWordStatus.Repeatable.SaveButtonIsEnabled -> if (binding.buttonSave.isEnabled != status.value) {
                    binding.buttonSave.isEnabled = status.value
                }

                is AddWordStatus.Repeatable.Translation -> binding.textInputLayoutTranslation.apply {
                    isEnabled = status.value.isEnabled
                    error = status.value.error?.let { getString(it) }
                    helperText = getString(status.value.helperText)
                    visibility = status.value.visibility
                }

                is AddWordStatus.Repeatable.VerbForms -> {
                    binding.textInputPrasensIch.setText(status.value.prasensIch)
                    binding.textInputPrasensDu.setText(status.value.prasensDu)
                    binding.textInputPrasensErSieEs.setText(status.value.prasensErSieEs)
                    binding.textInputPrasensWir.setText(status.value.prasensWir)
                    binding.textInputPrasensIhr.setText(status.value.prasensIhr)
                    binding.textInputPrasensSieSie.setText(status.value.prasensSieSie)
                    binding.textInputPrateritumIch.setText(status.value.prateritumIch)
                    binding.textInputPrateritumDu.setText(status.value.prateritumDu)
                    binding.textInputPrateritumErSieEs.setText(status.value.prateritumErSieEs)
                    binding.textInputPrateritumWir.setText(status.value.prateritumWir)
                    binding.textInputPrateritumIhr.setText(status.value.prateritumIhr)
                    binding.textInputPrateritumSieSie.setText(status.value.prateritumSieSie)
                    binding.textInputPerfect.setText(status.value.perfekt)
                }

                is AddWordStatus.Repeatable.VerbFormsVisibility ->
                    binding.linearLayoutVerbForms.visibility = status.value

                is AddWordStatus.Repeatable.Word -> binding.textInputLayoutWord.apply {
                    isEnabled = status.value.isEnabled
                    error = status.value.error?.let { getString(it) }
                    helperText = getString(status.value.helperText)
                    visibility = status.value.visibility
                }

                AddWordStatus.Unrepeatable.ClearAllInput -> {
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
                    binding.textInputPerfect.text = null
                    binding.textInputKomparativ.text = null
                    binding.textInputSuperlativ.text = null
                }
            }
        }
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

    private fun setOnWordChangeListener() {
        binding.textInputWord.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.onWordChange(s?.toString() ?: "")
            }
        })
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

    private fun setOnWordPluralFormChangeListener() {
        binding.textInputWordPlural.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.onWordPluralFormChange(s?.toString() ?: "")
            }
        })
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

    private fun setOnButtonSaveClickListener() {
        binding.buttonSave.setOnClickListener {
            viewModel.onButtonSaveClick()
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

        binding.textInputPerfect.addTextChangedListener(textWatcher(VerbFormType.PERFEKT))
    }

    private fun setOnAdjectiveFormsChangeListener() {
        binding.textInputKomparativ.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.onKomparativChange(s?.toString())
            }
        })

        binding.textInputSuperlativ.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.onSuperlativChange(s?.toString())
            }
        })
    }

    private fun textWatcher(forType: VerbFormType) = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            viewModel.onVerbFormChange(s?.toString(), forType)
        }
    }
}
