package com.tinmegali.myweather.web

import com.tinmegali.myweather.data.PrefsDAO
import com.tinmegali.myweather.models.Response
import com.tinmegali.myweather.models.WeatherResponse
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import javax.inject.Inject


class OpenWeatherService
    @Inject
    constructor(
            private val api: OpenWeatherApi,
            private val errorUtils: ErrorUtils,
            private val openWeatherId: String,
            private val prefsDAO: PrefsDAO
    ) : AnkoLogger {


    // Get Weather by City name
    fun getWeatherByCity(city: String): Response<WeatherResponse> {
        info("getWeatherByCity: $city")

        val call = api.cityWeather( city, openWeatherId, prefsDAO.getUnits() )
        val response = call.execute()

        if (response.isSuccessful) {
            info("getWeatherByCity: success:\n${response.body()}")
            return Response( data = response.body() )
        } else {
            info("getWeatherByCity: error:\n${response.errorBody()}")
            return Response( error = errorUtils.convertErrorBody(response.errorBody()!!))
        }
    }

}