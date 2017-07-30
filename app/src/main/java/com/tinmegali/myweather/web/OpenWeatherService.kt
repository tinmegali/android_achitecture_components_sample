package com.tinmegali.myweather.web

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
import android.location.Location
import com.tinmegali.myweather.data.PrefsDAO
import com.tinmegali.myweather.models.ApiResponse
import com.tinmegali.myweather.models.WeatherResponse
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import javax.inject.Inject


class OpenWeatherService
    @Inject
    constructor(
            private val api: OpenWeatherApi,
            private val errorUtils: ErrorUtils, //FIXME check errors
            private val openWeatherId: String,
            private val prefsDAO: PrefsDAO
    ) : AnkoLogger {


    // Get Weather by City name
    fun getWeatherByCity(city: String): LiveData<ApiResponse<WeatherResponse>> {
        info("updateWeatherByCity: $city")

        return (object : Mediator<ApiResponse<WeatherResponse>>() {
            override fun callApi(): LiveData<ApiResponse<WeatherResponse>> {
                return api.cityWeatherLive( openWeatherId, city, prefsDAO.getUnits() )
            }
        }).get()
    }

    // get Weather by Location
    fun getWeatherByLocation( location: Location ) : LiveData<ApiResponse<WeatherResponse>> {
        info("getWeatherByLocation")

        return (object : Mediator<ApiResponse<WeatherResponse>>() {
            override fun callApi(): LiveData<ApiResponse<WeatherResponse>> {
                return api.cityWeatherByLocationLive(
                        openWeatherId,
                        location.latitude.toString(),
                        location.longitude.toString(),
                        prefsDAO.getUnits()
                )
            }
        }).get()
    }

    abstract class Mediator<T> : AnkoLogger {
        private var result: MediatorLiveData<T> = MediatorLiveData()

        init {
            info("init")
            doAsync {
                val apiResult: LiveData<T> = callApi()
                result.addSource(
                        apiResult,
                        {
                            r ->
                            result.removeSource(apiResult)
                            result.postValue(r)
                        }
                )
            }
        }

        abstract fun callApi(): LiveData<T>

        fun get() : LiveData<T> {
            return result
        }
    }

}