package com.omouravictor.ratesbr.presenter.converter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import java.text.DecimalFormat

class ConverterViewModel : ViewModel() {
    val rate = MutableLiveData<RateUiModel>()
    val result = MutableLiveData<String>()
    private val decimalFormat = DecimalFormat("0.00")

    fun setRate(rateUiModel: RateUiModel) {
        rate.postValue(rateUiModel)
    }

    fun setInitialResult() {
        result.postValue(rate.value?.unityRate.toString())
    }

    fun calculateConversion(value: Double) {
        val resultValue = value * decimalFormat.format(rate.value?.unityRate).toDouble()
        result.postValue(resultValue.toString())
    }
}