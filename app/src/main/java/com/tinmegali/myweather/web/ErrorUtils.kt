package com.tinmegali.myweather.web

import okhttp3.ResponseBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject

class ErrorUtils
    @Inject
    constructor(
            val retrofit: Retrofit
    ) : AnkoLogger {

    fun convertErrorBody( body: ResponseBody ) : ApiError? {
        info("convertErrorBody")
        var converter: Converter<ResponseBody, ApiError> =
                retrofit.responseBodyConverter(ApiError::class.java, emptyArray() )

        try {
            val error: ApiError = converter.convert( body )
            return error

        } catch(e: IOException) {
            info("convertErrorBody: error")
            e.printStackTrace()
            return null
        }
    }

}