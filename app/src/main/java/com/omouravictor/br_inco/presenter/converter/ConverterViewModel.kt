package com.omouravictor.br_inco.presenter.converter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConverterViewModel : ViewModel() {

    val conversionResult = MutableLiveData<Double>()
    var unitaryRate: Double = 0.0

    fun calculateConversion(amount: Double) {
        conversionResult.postValue(amount * unitaryRate)
    }
}