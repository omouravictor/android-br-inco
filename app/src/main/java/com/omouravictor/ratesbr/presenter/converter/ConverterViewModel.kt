package com.omouravictor.ratesbr.presenter.converter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import java.text.DecimalFormat

class ConverterViewModel : ViewModel() {

    val rateResult = MutableLiveData<RateUiModel>()
    val conversionResult = MutableLiveData<String>()
    private val decimalFormat = DecimalFormat("0.00")

    fun setInitialRateAndConversionResults(rateUiModel: RateUiModel) {
        rateResult.postValue(rateUiModel)
        conversionResult.postValue(rateUiModel.unitaryRate.toString())
    }

    fun calculateConversion(value: Double) {
        val resultValue = value * decimalFormat.format(rateResult.value?.unitaryRate).toDouble()
        conversionResult.postValue(resultValue.toString())
    }
}