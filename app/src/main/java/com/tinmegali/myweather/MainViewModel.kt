package com.tinmegali.myweather

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import com.tinmegali.myweather.models.Response
import com.tinmegali.myweather.models.WeatherMain
import com.tinmegali.myweather.repository.MainRepository
import com.tinmegali.myweather.web.ApiError
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.warn
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel
    @Inject
    constructor(
            private val repository: MainRepository
    )
    : ViewModel(), AnkoLogger {

    val cityWeather: MutableLiveData<Response<WeatherMain>> = MutableLiveData()

    fun getWeatherLiveData() : MutableLiveData<Response<WeatherMain>> {
        return cityWeather
    }

    fun getWeatherCached() {
        info("getWeatherCached")
        doAsync {
            val w = repository.getWeatherMainFromPrefs()
            if ( w != null ) {
                info("getWeatherCached: weather retrieved.")
                if ( isCachedWeatherValid( w ) ) {
                    info("getWeatherCached: weather is valid.\n$w")
                    cityWeather.postValue(Response(data = w))
                }
            } else {
                warn("getWeatherCached: no weather cached.")
                val e = ApiError(statusCode = 0, message = "No weather cached.")
                cityWeather.postValue(Response(error = e))
            }
        }
    }

    fun getWeatherByCity( owner: LifecycleOwner, city: String ) {
        info("getWeatherByCity: $city")

        doAsync {
            // check if weather is saved on cache
            val w = getWeatherOnCache( city )
            if ( w != null ) {
                if ( isCachedWeatherValid( w ) ) {
                    info("getWeatherByCity: recovering cached weather, without calling api")
                    cityWeather.postValue( Response( data = w) )
                    return@doAsync
                } else {
                    info("getWeatherByCity: recovering cached weather and calling api to refresh it")
                    cityWeather.postValue( Response( data = w) )
                }
            }

            info("getWeatherByCity: making repository call...")
            repository.getWeatherByCity(city)
                    .observe(owner,
                            Observer {
                                r ->
                                if ( !r!!.hasError() ) {
                                    if (r.data!!.weather!!.isNotEmpty()) {
                                        // getting weather from today
                                        val weatherMain = WeatherMain.factory(r.data!!)
                                        // save on shared preferences
                                        repository.saveWeatherMainOnPrefs(weatherMain)
                                        // update weather value
                                        cityWeather.postValue(Response(data = weatherMain))
                                    }
                                } else {
                                    warn("getWeatherByCity: error while fetching weather\n${r.error}")
                                    cityWeather.postValue(Response(error = r.error))
                                }
                            })

        }
    }

    fun isCachedWeatherValid(w: WeatherMain): Boolean {
        info("isCachedWeatherValid: value saved on preferences")
        // check if is still valid
        val c: Calendar = Calendar.getInstance()
        val now = TimeUnit.MILLISECONDS.toSeconds(c.timeInMillis)
        val limit = (60 * 60) * 2 // 2 H
        return now - w.dt!! <= limit
    }

    fun getWeatherOnCache( city: String ) : WeatherMain? {
        info("isWeatherOnCache: $city")
        val w = repository.getWeatherMainFromPrefs()
        if ( w != null ) {
            if ( w.name == city ) {
                info( "isWeatherOnCache: cached weather from '$city'")
                return w
            }
            else {
                info( "isWeatherOnCache: cached weather from '$city' not found.")
                return null
            }
        } else {
            info( "isWeatherOnCache: no weather cached")
            return null
        }
    }

}