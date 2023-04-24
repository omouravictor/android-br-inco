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
        converterViewModel.rate.observe(this) {
            setRateInfo(it)
        }

        converterViewModel.result.observe(this) {
            converterBinding.textViewResultValue.text = numberFormat.format(it)
        }
    }

    private fun initTextInputEditTextValueConverter() {
        converterBinding.textInputEditTextValueConverter.setText("1")
        converterBinding.textInputEditTextValueConverter.doAfterTextChanged {
            converterViewModel.calculateConversion(it.toString())
        }
    }

    private fun setRateInfo(rateUiModel: RateUiModel) {
        converterBinding.textViewCurrencyName.text = rateUiModel.currencyName
        converterBinding.textViewCurrencyTerm.text = rateUiModel.currencyTerm
        converterBinding.textViewUnitaryRateValue.text =
            numberFormat.format(rateUiModel.unitaryRate)
    }
}