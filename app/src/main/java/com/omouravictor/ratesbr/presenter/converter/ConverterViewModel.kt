package com.omouravictor.ratesbr.presenter.converter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import java.text.DecimalFormat

class ConverterViewModel : ViewModel() {
    val getCurrency = MutableLiveData<RateUiModel>()
    val resultTextViewConverter = MutableLiveData<String>()

    private val decimalFormat = DecimalFormat("0.00")

    fun setCurrency(currency: RateUiModel) {
        getCurrency.postValue(currency)
    }

    fun calculateConversion(value: Double) {
        val result =
            value * decimalFormat.format(getCurrency.value?.unityRate).toDouble()
        resultTextViewConverter.postValue(result.toString())
    }

    fun setCurrencyStartValue() {
        resultTextViewConverter.postValue(getCurrency.value?.unityRate.toString())
    }
}