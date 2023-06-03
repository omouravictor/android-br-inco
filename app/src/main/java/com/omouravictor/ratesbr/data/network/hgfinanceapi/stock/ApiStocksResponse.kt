package com.omouravictor.ratesbr.data.network.hgfinanceapi.stock

import com.google.gson.annotations.SerializedName
import com.omouravictor.ratesbr.data.local.entity.StockEntity
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
import com.omouravictor.ratesbr.util.StringUtils.getCityInPortuguese
import com.omouravictor.ratesbr.util.StringUtils.getCountryInPortuguese
import java.util.*

data class ApiStocksResponse(
    @SerializedName("results")
    val results: ApiStocksResultsResponse,

    var stockDate: Date
)

fun ApiStocksResponse.toStocksEntityList(): List<StockEntity> {
    return results.stocks.map { (stocksMapKey, stocksMapValue) ->
        val fullLocationList = stocksMapValue.location.split(", ")
        val countryLocation = fullLocationList.last()
        val cityLocation = fullLocationList.first()

        StockEntity(
            stocksMapKey,
            stocksMapValue.name,
            countryLocation,
            cityLocation,
            stocksMapValue.points,
            stocksMapValue.variation,
            stockDate
        )
    }
}

fun ApiStocksResponse.toStocksUiModelList(): List<StockUiModel> {
    return results.stocks.map { (stocksMapKey, stocksMapValue) ->
        val fullLocationList = stocksMapValue.location.split(", ")
        val countryLocation = fullLocationList.last()
        val cityLocation = fullLocationList.first()

        StockUiModel(
            stocksMapKey,
            stocksMapValue.name,
            getCountryInPortuguese(countryLocation),
            getCityInPortuguese(cityLocation),
            stocksMapValue.points,
            stocksMapValue.variation,
            stockDate
        )
    }
}