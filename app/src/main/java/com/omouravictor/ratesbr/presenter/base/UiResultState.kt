package com.omouravictor.ratesbr.presenter.base

sealed class UiResultState<out T> {
    object Loading : UiResultState<Nothing>()
    data class Success<out T>(val data: T) : UiResultState<T>()
    data class Error(val e: Exception) : UiResultState<Nothing>()
}
