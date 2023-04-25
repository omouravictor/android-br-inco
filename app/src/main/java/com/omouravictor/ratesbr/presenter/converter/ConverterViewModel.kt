package com.omouravictor.ratesbr.presenter.converter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import com.omouravictor.ratesbr.util.Numbers.getRoundedDouble

class ConverterViewModel : ViewModel() {

    val rate = MutableLiveData<RateUiModel>()
    val result = MutableLiveData<Double>()
    private var unitaryRate: Double = 0.0

    fun setInitialValues(rateUiModel: RateUiModel) {
        rate.postValue(rateUiModel)
        result.postValue(rateUiModel.unitaryRate)
        unitaryRate = getRoundedDouble(rateUiModel.unitaryRate)
    }

    fun calculateConversion(value: String) {
        try {
            val conversionResult = value.toDouble() * unitaryRate
            result.postValue(conversionResult)
        } catch (e: Exception) {
            result.postValue(0.0)
        }
    }
}