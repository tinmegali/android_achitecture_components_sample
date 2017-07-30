package com.tinmegali.myweather.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.location.Location
import com.tinmegali.myweather.data.LocationLiveData
import com.tinmegali.myweather.data.PrefsDAO
import com.tinmegali.myweather.models.ApiResponse
import com.tinmegali.myweather.models.WeatherMain
import com.tinmegali.myweather.models.WeatherResponse
import com.tinmegali.myweather.web.OpenWeatherService
import org.jetbrains.anko.*
import javax.inject.Inject

class MainRepository
    @Inject
    constructor(
            private val openWeatherService: OpenWeatherService,
            private val prefsDAO: PrefsDAO,
            private val location: LocationLiveData
    ) : AnkoLogger
{

    fun getWeatherByCity( city: String ) : LiveData<ApiResponse<WeatherResponse>>
    {
        info("updateWeatherByCity: $city")
        return openWeatherService.getWeatherByCity(city)
    }

    fun saveWeatherMainOnPrefs( weatherMain: WeatherMain ) {
        info("saveWeatherMainOnPrefs")
        prefsDAO.saveWeatherMain( weatherMain )
    }

    fun getWeatherMainFromPrefs() : WeatherMain? {
        info("getWeatherMainFromPrefs")
        if ( prefsDAO.haveWeather() )
            return prefsDAO.getWeatherMain()
        else return null
    }

    fun getLocation() : LocationLiveData {
        info("getLocation")
        return location
    }

    fun refreshLocation() {
        info("refreshLocation")
        location.refreshLocation()
    }

    fun getWeatherByLocation( location: Location ) : LiveData<ApiResponse<WeatherResponse>>
    {
        info("getWeatherByLocation: \n$location")
        return openWeatherService.getWeatherByLocation( location )
    }

}