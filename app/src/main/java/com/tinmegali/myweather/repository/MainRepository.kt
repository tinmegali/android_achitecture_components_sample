package com.tinmegali.myweather.repository

import android.arch.lifecycle.LiveData
import android.location.Location
import com.tinmegali.myweather.data.LocationLiveData
import com.tinmegali.myweather.data.PrefsDAO
import com.tinmegali.myweather.data.WeatherDAO
import com.tinmegali.myweather.models.ApiResponse
import com.tinmegali.myweather.models.WeatherMain
import com.tinmegali.myweather.models.WeatherResponse
import com.tinmegali.myweather.web.OpenWeatherService
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import java.util.*
import javax.inject.Inject

class MainRepository
    @Inject
    constructor(
            private val openWeatherService: OpenWeatherService,
            private val prefsDAO: PrefsDAO,
            private val weatherDAO: WeatherDAO,
            private val location: LocationLiveData
    ) : AnkoLogger
{

    fun getWeatherByCity( city: String ) : LiveData<ApiResponse<WeatherResponse>>
    {
        info("getWeatherByCity: $city")
        return openWeatherService.getWeatherByCity(city)
    }

    fun saveOnDb( weatherMain: WeatherMain ) {
        info("saveOnDb:\n$weatherMain")
        weatherDAO.insert( weatherMain )
    }

    fun getRecentWeather(): LiveData<WeatherMain> {
        info("getRecentWeather")
        return weatherDAO.findLast()
    }

    fun getRecentWeatherForLocation(location: String): LiveData<WeatherMain> {
        info("getWeatherByDateAndLocation")
        return weatherDAO.findByCity(location)
    }

    fun clearOldData(){
        info("clearOldData")
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, -1)
        // get weather data from 2 days ago
        val oldData = weatherDAO.findByDate(c.timeInMillis)
        oldData.forEach{ w ->
            info("Removing data for '${w.name}':${w.dt}")
            weatherDAO.remove(w)
        }
    }

    fun saveWeatherMainOnPrefs( weatherMain: WeatherMain ) {
        info("saveWeatherMainOnPrefs")
        prefsDAO.saveWeatherMain( weatherMain )
    }

    fun getWeatherMainFromPrefs(): LiveData<WeatherMain> {
        info("getWeatherMainFromPrefs")
        return prefsDAO.getWeatherMain()
    }

    fun locationLiveDa() : LocationLiveData {
        info("locationLiveDa")
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