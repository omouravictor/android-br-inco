package com.omouravictor.ratesnow.data.models

data class ApiResponse(
    val base: String,
    val date: String,
    val rates: Rates
)