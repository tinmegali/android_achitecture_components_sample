package com.tinmegali.myweather.models

import com.tinmegali.myweather.web.ApiError

class ApiResponse<T>
    constructor(
    val data: T? = null,
    val error: ApiError? = null) {

    fun hasError() : Boolean {
        return error != null
    }

    override fun toString(): String {
        val str: StringBuilder = StringBuilder()
        str.append( "Response" )
        str.append("\n---- data: ${data.toString()}")
        str.append("\n---- error:${error.toString()}")
        return str.toString()
    }

}
