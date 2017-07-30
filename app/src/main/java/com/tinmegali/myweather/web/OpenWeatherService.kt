package com.tinmegali.myweather.web

import android.location.Location
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
        info("updateWeatherByCity: $city")

        val call = api.cityWeather( openWeatherId, city, prefsDAO.getUnits() )
        val response = call.execute()

        if (response.isSuccessful) {
            info("updateWeatherByCity: success:\n${response.body()}")
            return Response( data = response.body() )
        } else {
            info("updateWeatherByCity: error:\n${response.errorBody()}")
            return Response( error = errorUtils.convertErrorBody(response.errorBody()!!))
        }
    }

    // get Weather by Location
    fun getWeatherByLocation( location: Location ) : Response<WeatherResponse> {
        info("updateWeatherByLocation")

        val call = api.cityWeatherByLocation(
                openWeatherId,
                location.latitude.toString(),
                location.longitude.toString(),
                prefsDAO.getUnits()
        )
        val response = call.execute()

        if ( response.isSuccessful ) {
            info("updateWeatherByLocation: success:\n${response.body()}")
            return Response( data = response.body() )
        } else {
            info("updateWeatherByLocation: error:\n${response.errorBody()}")
            return Response( error = errorUtils.convertErrorBody(response.errorBody()!!))
        }
    }

}