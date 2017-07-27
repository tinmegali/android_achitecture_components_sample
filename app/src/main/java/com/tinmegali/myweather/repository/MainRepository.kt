package com.tinmegali.myweather.repository

import android.arch.lifecycle.MutableLiveData
import com.tinmegali.myweather.data.PrefsDAO
import com.tinmegali.myweather.models.Response
import com.tinmegali.myweather.models.WeatherMain
import com.tinmegali.myweather.models.WeatherResponse
import com.tinmegali.myweather.web.OpenWeatherService
import org.jetbrains.anko.*
import javax.inject.Inject

class MainRepository
    @Inject
    constructor(
            private val openWeatherService: OpenWeatherService,
            private val prefsDAO: PrefsDAO
    ) : AnkoLogger
{

    fun getWeatherByCity( city: String ) : MutableLiveData<Response<WeatherResponse>>
    {
        info("getWeatherByCity: $city")
        val result: MutableLiveData<Response<WeatherResponse>> = MutableLiveData()
        val r = openWeatherService.getWeatherByCity(city)
        result.postValue(r)

        return result
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

}