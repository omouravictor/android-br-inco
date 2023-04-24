package com.omouravictor.ratesbr.presenter.converter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import kotlin.math.round

class ConverterViewModel : ViewModel() {

    val rate = MutableLiveData<RateUiModel>()
    val result = MutableLiveData<Double>()

    fun setInitialRateAndResultValues(rateUiModel: RateUiModel) {
        rate.postValue(rateUiModel)
        result.postValue(rateUiModel.unitaryRate)
    }

    fun calculateConversion(strValue: String) {
        try {
            val value = strValue.toDouble()
            val unitaryRate = round(rate.value?.unitaryRate!! * 100) / 100
            val conversionResult = value * unitaryRate
            result.postValue(conversionResult)
        } catch (e: Exception) {
            result.postValue(0.0)
        }
    }
}