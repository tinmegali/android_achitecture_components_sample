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
import org.jetbrains.anko.uiThread
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

        // Create a Mediator object
        // make api calls
        // return the object as LiveData
        return (object : Mediator<ApiResponse<WeatherResponse>>() {
            override fun callApi(): LiveData<ApiResponse<WeatherResponse>> {
                info("callApi")
                return api.cityWeatherLive( openWeatherId, city, prefsDAO.getUnits() )
            }
        }).get()
    }

    // get Weather by Location
    fun getWeatherByLocation( location: Location ) : LiveData<ApiResponse<WeatherResponse>> {
        info("getWeatherByLocation")

        // Create a Mediator object
        // make api calls
        // return the object as LiveData
        return (object : Mediator<ApiResponse<WeatherResponse>>() {
            override fun callApi(): LiveData<ApiResponse<WeatherResponse>> {
                info("callApi")
                return api.cityWeatherByLocationLive(
                        openWeatherId,
                        location.latitude.toString(),
                        location.longitude.toString(),
                        prefsDAO.getUnits()
                )
            }
        }).get()
    }

    // Mediator Object
    // Create a MediatorLiveData<T> to handle results
    // monitors api calls
    // make api calls on worker thread
    // return a LiveData<T> containing the result
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
                            result.postValue(r)
                            uiThread {
                                result.removeSource(apiResult)
                            }
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