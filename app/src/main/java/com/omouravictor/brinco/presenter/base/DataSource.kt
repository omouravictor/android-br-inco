package com.omouravictor.brinco.presenter.base

sealed class DataSource {
    object LOCAL : DataSource()
    object NETWORK : DataSource()
}