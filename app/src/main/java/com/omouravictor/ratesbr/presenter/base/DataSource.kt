package com.omouravictor.ratesbr.presenter.base

sealed class DataSource {
    object LOCAL : DataSource()
    object NETWORK : DataSource()
}