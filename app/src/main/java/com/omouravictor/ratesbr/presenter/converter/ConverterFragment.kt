package com.omouravictor.ratesbr.presenter.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.omouravictor.ratesbr.databinding.FragmentConverterBinding
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import com.omouravictor.ratesbr.util.FormatUtils.BrazilianFormats.brCurrencyFormat

class ConverterFragment : Fragment() {

    private lateinit var binding: FragmentConverterBinding
    private val converterViewModel: ConverterViewModel by activityViewModels()

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
        initTextInputEditTextValueConverter()
    }

    override fun onStart() {
        super.onStart()
        converterViewModel.rate.observe(this) {
            setRateInfo(it)
        }

        converterViewModel.result.observe(this) {
            binding.textViewResultValue.text = brCurrencyFormat.format(it)
        }
    }

    private fun initTextInputEditTextValueConverter() {
        binding.textInputEditTextValueConverter.setText("1")
        binding.textInputEditTextValueConverter.doAfterTextChanged {
            converterViewModel.calculateConversion(it.toString())
        }
    }

    private fun setRateInfo(rateUiModel: RateUiModel) {
        binding.textViewCurrencyName.text = rateUiModel.currencyName
        binding.textViewCurrencyTerm.text = rateUiModel.currencyTerm
        binding.textViewUnitaryRateValue.text = brCurrencyFormat.format(rateUiModel.unitaryRate)
    }
}