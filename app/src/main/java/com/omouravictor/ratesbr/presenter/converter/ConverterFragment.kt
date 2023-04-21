package com.omouravictor.ratesbr.presenter.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.omouravictor.ratesbr.R
import com.omouravictor.ratesbr.databinding.FragmentConverterBinding
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import java.text.NumberFormat
import java.util.*

class ConverterFragment : Fragment() {

    private lateinit var converterBinding: FragmentConverterBinding
    private val converterViewModel: ConverterViewModel by activityViewModels()
    private val locale = Locale("pt", "BR")
    private val numberFormat = NumberFormat.getCurrencyInstance(locale)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        converterBinding = FragmentConverterBinding.inflate(layoutInflater, container, false)
        return converterBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTextInputEditTextValueConverter()
    }

    override fun onStart() {
        super.onStart()
        converterViewModel.rateResult.observe(this) {
            setRateInfo(it)
        }

        converterViewModel.conversionResult.observe(this) {
            converterBinding.textViewResultValue.text = numberFormat.format(it.toDouble())
        }
    }

    private fun initTextInputEditTextValueConverter() {
        converterBinding.textInputEditTextValueConverter.setText("1")
        converterBinding.textInputEditTextValueConverter.doAfterTextChanged {
            if (it.toString().isNotEmpty() && it.toString() != ".")
                converterViewModel.calculateConversion(it.toString().toDouble())
            else
                converterBinding.textViewResultValue.text = getString(R.string.brl_symbol)
        }
    }

    private fun setRateInfo(rateUiModel: RateUiModel) {
        converterBinding.textViewResultTittle.text = rateUiModel.currencyName
        converterBinding.textViewCurrencyTerm.text = rateUiModel.currencyTerm
        converterBinding.textViewUnitaryRateValue.text =
            numberFormat.format(rateUiModel.unitaryRate)
    }
}