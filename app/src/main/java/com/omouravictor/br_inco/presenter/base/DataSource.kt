package com.omouravictor.br_inco.presenter.base

sealed class DataSource {
    object LOCAL : DataSource()
    object NETWORK : DataSource()
}