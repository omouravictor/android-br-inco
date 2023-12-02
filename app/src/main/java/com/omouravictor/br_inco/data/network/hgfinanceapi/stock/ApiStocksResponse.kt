package com.omouravictor.br_inco.data.network.hgfinanceapi.stock

import com.google.gson.annotations.SerializedName
import com.omouravictor.br_inco.data.local.entity.StockEntity
import com.omouravictor.br_inco.presenter.stocks.model.StockUiModel
import com.omouravictor.br_inco.util.StringUtils
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
            StringUtils.getCountryInPortuguese(countryLocation),
            StringUtils.getCityInPortuguese(cityLocation),
            stocksMapValue.points,
            stocksMapValue.variation,
            stockDate
        )
    }
}