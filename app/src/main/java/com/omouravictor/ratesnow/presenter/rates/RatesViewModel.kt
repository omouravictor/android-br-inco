package com.omouravictor.ratesnow.presenter.rates

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesnow.data.network.base.ResultStatus
import com.omouravictor.ratesnow.domain.model.toListRatesDto
import com.omouravictor.ratesnow.domain.usecases.GetRatesUseCase
import com.omouravictor.ratesnow.presenter.base.ResultUiState
import kotlinx.coroutines.launch

class RatesViewModel @ViewModelInject constructor(
    private val getRatesUseCase: GetRatesUseCase
) : ViewModel() {
    val rates = MutableLiveData<ResultUiState<List<RatesDto>>>()
    private val toCurrencies = "BRL,USD,EUR,JPY,GBP,CAD"
    private val listToCurrencies = toCurrencies.split(",")

    fun getRates(fromCurrency: String) {
        viewModelScope.launch {
            getRatesUseCase(fromCurrency, toCurrencies).collect {
                when (it) {
                    is ResultStatus.Success -> {
                        rates.postValue(
                            ResultUiState.Success(
                                it.data.toListRatesDto(
                                    listToCurrencies
                                )
                            )
                        )
                    }
                    is ResultStatus.Error -> {
                        rates.postValue(ResultUiState.Error(it.e))
                    }
                    is ResultStatus.Loading -> {
                        rates.postValue(ResultUiState.Loading)
                    }
                }
            }
        }
    }

//    fun convert(amountStr: String) {
//        val amount = amountStr.toFloatOrNull()
//        if (amount == null) {
//            _conversion.value = ConversionEvent.Empty
//            return
//        }
//
//        if (conversionList.value != null) {
//            conversionList.value!!.forEach { conversion -> conversion.amount = amount }
//            conversionList.postValue(conversionList.value)
//        } else {
//            _conversion.value = ConversionEvent.Failure("Verifique sua conexão :(")
//        }
//
//        _conversion.value = ConversionEvent.Success()
//    }
//
//    private suspend fun tryRatesFromApi(fromCurrency: String, amount: Float) {
//        val rates = repository.getFromDb(fromCurrency)
//        if (rates != null) {
//            replaceConversionList(fromCurrency, amount, rates)
//            _conversion.value = ConversionEvent.Success()
//        } else {
//            _conversion.value = ConversionEvent.Failure("Verifique sua conexão :(")
//        }
//
////        when (val request = repository.getAllFromApi(fromCurrency, requestCurrencies)) {
////            is Resource.Success -> {
////                val rates = getRatesEntity(request.data!!, Date())
////                repository.insertOnDb(rates)
////                replaceConversionList(fromCurrency, amount, rates)
////                _conversion.value = ConversionEvent.Success()
////            }
////            is Resource.Error -> {
////                val rates = repository.getFromDb(fromCurrency)
////                if (rates != null) {
////                    replaceConversionList(fromCurrency, amount, rates)
////                    _conversion.value = ConversionEvent.Success()
////                } else {
////                    _conversion.value = ConversionEvent.Failure("Verifique sua conexão :(")
////                }
////            }
////        }
//    }
//
//    private fun getRatesEntity(
//        requestData: SourceRequestRatesModel,
//        ratesDate: Date
//    ): RatesEntity =
//        RatesEntity(
//            requestData.base,
//            requestData.rates.uSD,
//            requestData.rates.eUR,
//            requestData.rates.jPY,
//            requestData.rates.gBP,
//            requestData.rates.cAD,
//            requestData.rates.bRL,
//            ratesDate
//        )
//
//    private fun replaceConversionList(fromCurrency: String, amount: Float, rates: RatesEntity) {
//        val list: MutableList<Conversion> = mutableListOf()
//
//        listToCurrencies.forEach {
//            if (it != fromCurrency) {
//                val rate = when (it) {
//                    "BRL" -> rates.bRL
//                    "USD" -> rates.uSD
//                    "EUR" -> rates.eUR
//                    "JPY" -> rates.jPY
//                    "GBP" -> rates.gBP
//                    "CAD" -> rates.cAD
//                    else -> 0.0
//                }
//                list.add(Conversion(fromCurrency, it, amount, rate, rates.date))
//            }
//        }
//        conversionList.postValue(list)
//    }

}