package com.omouravictor.ratesbr.presenter.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.omouravictor.ratesbr.R
import com.omouravictor.ratesbr.databinding.FragmentConverterBinding
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import java.text.NumberFormat
import java.util.*

class ConverterFragment : Fragment() {
    private lateinit var binding: FragmentConverterBinding
    private lateinit var converterViewModel: ConverterViewModel

    private val locale = Locale("pt", "BR")
    private val numberFormat = NumberFormat.getCurrencyInstance(locale)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        converterViewModel =
            ViewModelProvider(requireActivity()).get(ConverterViewModel::class.java)
        converterViewModel.setCurrencyStartValue()

        binding = FragmentConverterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textInputEditTextValueConverter.setText("1")
        getValue()
    }

    override fun onStart() {
        super.onStart()
        converterViewModel.getCurrency.observe(this, Observer {
            setCurrencyInfos(it)
        })

        converterViewModel.resultTextViewConverter.observe(this, Observer {
            binding.textViewResultValueConverter.text = numberFormat.format(it.toDouble())
        })
    }

    private fun getValue() {
        binding.textInputEditTextValueConverter.doAfterTextChanged { text ->
            try {
                if (text.toString().isNotEmpty()) {
                    converterViewModel.calculateConversion(text.toString().toDouble())
                } else {
                    getString(R.string.app_name).also { binding.textViewResultValueConverter.text = it }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    getString(R.string.app_name),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setCurrencyInfos(currency: RateUiModel) {
        binding.textViewTermConverter.text = currency.currency
        binding.textViewUnitaryValueConverter.text = numberFormat.format(currency.unityRate)
    }
}