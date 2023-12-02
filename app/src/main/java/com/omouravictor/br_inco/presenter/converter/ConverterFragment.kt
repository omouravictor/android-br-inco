package com.omouravictor.br_inco.presenter.converter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.omouravictor.br_inco.databinding.FragmentConverterBinding
import com.omouravictor.br_inco.presenter.rates.model.RateUiModel
import com.omouravictor.br_inco.util.FormatUtils
import com.omouravictor.br_inco.util.SystemServiceUtils

class ConverterFragment : Fragment() {

    private lateinit var binding: FragmentConverterBinding
    private val converterViewModel: ConverterViewModel by activityViewModels()
    private val rateUiModelArg by lazy {
        ConverterFragmentArgs.fromBundle(requireArguments()).rateUiModelArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConverterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareForConversion(rateUiModelArg)
    }

    override fun onStart() {
        super.onStart()

        converterViewModel.conversionResult.observe(this) { conversionResult ->
            binding.textViewResultValue.text =
                FormatUtils.BrazilianFormats.brCurrencyFormat.format(conversionResult)
        }
    }

    override fun onStop() {
        super.onStop()
        SystemServiceUtils.hideKeyboard(requireActivity(), requireView())
    }

    private fun prepareForConversion(rateUiModel: RateUiModel) {
        converterViewModel.unitaryRate = rateUiModel.unitaryRate

        binding.textViewCurrencyName.text = rateUiModel.currencyName
        binding.textViewCurrencyTerm.text = rateUiModel.currencyTerm
        binding.textViewUnitaryRateValue.text =
            FormatUtils.BrazilianFormats.brCurrencyFormat.format(rateUiModel.unitaryRate)

        setupTextInputEditTextAmount()
    }

    private fun setupTextInputEditTextAmount() {
        with(binding.textInputEditTextAmount) {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(cs: CharSequence?, s: Int, c: Int, a: Int) {}

                override fun onTextChanged(
                    text: CharSequence, start: Int, before: Int, count: Int
                ) {
                    removeTextChangedListener(this)

                    val cleanText = text.replace("[,.]".toRegex(), "")
                    val amount = cleanText.toDoubleOrNull()?.div(100) ?: 0.0
                    val formattedAmount =
                        FormatUtils.BrazilianFormats.brDecimalFormat.format(amount)

                    converterViewModel.calculateConversion(amount)
                    setText(formattedAmount)
                    setSelection(formattedAmount.length)

                    addTextChangedListener(this)
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            setText("1,00")
            SystemServiceUtils.showKeyboard(requireActivity(), this)
        }
    }

}