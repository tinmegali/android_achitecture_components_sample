package com.tinmegali.myweather.web

data class ApiError(
        val statusCode: Int,
        val message: String?
)