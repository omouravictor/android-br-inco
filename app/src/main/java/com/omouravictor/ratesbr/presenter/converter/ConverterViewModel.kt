package com.omouravictor.ratesbr.presenter.converter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import java.text.DecimalFormat

class ConverterViewModel : ViewModel() {
    val rate = MutableLiveData<RateUiModel>()
    val result = MutableLiveData<String>()
    private val decimalFormat = DecimalFormat("0.00")

    fun setRateAndResult(rateUiModel: RateUiModel) {
        rate.postValue(rateUiModel)
        result.postValue(rateUiModel.unityRate.toString())
    }

    fun calculateConversion(value: Double) {
        val resultValue = value * decimalFormat.format(rate.value?.unityRate).toDouble()
        result.postValue(resultValue.toString())
    }
}