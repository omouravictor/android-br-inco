package com.omouravictor.ratesbr.presenter.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.omouravictor.ratesbr.R
import com.omouravictor.ratesbr.databinding.FragmentConverterBinding
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import java.text.NumberFormat
import java.util.*

class ConverterFragment : Fragment() {

    private lateinit var converterBinding: FragmentConverterBinding
    private lateinit var converterViewModel: ConverterViewModel
    private val locale = Locale("pt", "BR")
    private val numberFormat = NumberFormat.getCurrencyInstance(locale)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        converterViewModel = ViewModelProvider(requireActivity())[ConverterViewModel::class.java]
        converterViewModel.setInitialResult()

        converterBinding = FragmentConverterBinding.inflate(layoutInflater, container, false)
        return converterBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        converterBinding.textInputEditTextValueConverter.setText("1")
        initTextInputEditTextValueConverter()
    }

    override fun onStart() {
        super.onStart()
        converterViewModel.rate.observe(this) {
            setRateInfo(it)
        }

        converterViewModel.result.observe(this) {
            converterBinding.textViewResultValueConverter.text = numberFormat.format(it.toDouble())
        }
    }

    private fun initTextInputEditTextValueConverter() {
        converterBinding.textInputEditTextValueConverter.doAfterTextChanged {
            if (it.toString().isNotEmpty() && it.toString() != ".")
                converterViewModel.calculateConversion(it.toString().toDouble())
            else
                converterBinding.textViewResultValueConverter.text = getString(R.string.brl_symbol)
        }
    }

    private fun setRateInfo(rateUiModel: RateUiModel) {
        converterBinding.textViewConverterTitle.text = rateUiModel.currencyName
        converterBinding.textViewTermConverter.text = rateUiModel.currencyTerm
        converterBinding.textViewUnitaryValueConverter.text =
            numberFormat.format(rateUiModel.unityRate)
    }
}