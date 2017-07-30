package com.tinmegali.myweather

import android.arch.core.util.Function
import android.arch.lifecycle.*
import android.arch.lifecycle.Observer
import android.location.Location
import com.tinmegali.myweather.data.LocationLiveData
import com.tinmegali.myweather.models.Response
import com.tinmegali.myweather.models.WeatherMain
import com.tinmegali.myweather.models.WeatherResponse
import com.tinmegali.myweather.repository.MainRepository
import com.tinmegali.myweather.web.ApiError
import org.jetbrains.anko.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel
@Inject
constructor(
        private val repository: MainRepository
)
    : ViewModel(), AnkoLogger {

    // Location
    private var location: LocationLiveData? = null

    // City Name
    private val cityName: MutableLiveData<String> = MutableLiveData()

    // Can hold two variable with different logic,
    // weatherByCityName and weatherByLocation.
    private var weatherResponse:
            MutableLiveData<Response<WeatherResponse>> = MutableLiveData()

    // Value observed by View.
    // It transform a WeatherResponse to a WeatherMain.
    private val weather:
            MutableLiveData<Response<WeatherMain>> =
            Transformations.map(
                    weatherResponse,
                    {
                        w ->
                        if (w != null && !w.hasError()) {
                            info("getting Weather")
                            // getting weather from today
                            val weatherMain = WeatherMain.factory(w.data!!)
                            // save on shared preferences
                            repository.saveWeatherMainOnPrefs(weatherMain)
                            // update weather value
                            return@map Response(data = weatherMain)
                        } else {
                            warn("onChanged: error while fetching weather\n${w.error}")
                            return@map Response<WeatherMain>(error = w.error)
                        }
                    }) as MutableLiveData<Response<WeatherMain>>


    // observes city name changes
    // updates weather considering city
    private val cityObserver: Observer<String> = Observer {
        city ->
        info("cityObserver: $city")
        doAsync {

            val w = repository.getWeatherByCity(city!!)
            weatherResponse.postValue(w)
        }
    }

    // observes Location changes
    // updates weather considering location
    private val locationObserver: Observer<Location> = Observer {
        l ->
        info("locationObserver: \n$l")
        doAsync {
            val w = repository.getWeatherByLocation(l!!)
            weatherResponse.postValue(w)
        }
    }

    init {
        cityName.observeForever(cityObserver)
    }

    override fun onCleared() {
        super.onCleared()
        location?.removeObserver(locationObserver)
        cityName.removeObserver(cityObserver)
    }

    // changes the value of weatherResponse
    // making it react to cityName.
    // update city name.
    fun weatherByCityName(city: String) {
        info("weatherByCityName")
        updateCityName(city)
    }

    // updates the city name
    private fun updateCityName(city: String) {
        info("updateCityName: $city")
        cityName.postValue(city)
    }

    // retrieve weather LiveData
    fun getWeather(): LiveData<Response<WeatherMain>> {
        info("getWeather")
        return weather
    }

    // changes the value of weatherResponse
    // making it react to location
    // update current location
    fun weatherByLocation() {
        info("weatherByLocation")
        if (location == null) {
            location = repository.getLocation()
            location!!.observeForever(locationObserver)
        }
        doAsync {
            refreshLocation()
        }
    }

    // updates current location
    private fun refreshLocation() {
        info("refreshLocation")
        repository.refreshLocation()

    }

    fun getWeatherCached() {
        info("getWeatherCached")
        doAsync {
            val w = repository.getWeatherMainFromPrefs()
            if (w != null) {
                info("getWeatherCached: weather retrieved.")
                if (isCachedWeatherValid(w)) {
                    info("getWeatherCached: weather is valid.\n$w")
                    weather.postValue(Response(data = w))
                }
            } else {
                warn("getWeatherCached: no weather cached.")
                val e = ApiError(statusCode = 0, message = "No weather cached.")
                weather.postValue(Response(error = e))
            }
        }
    }

    private fun isCachedWeatherValid(w: WeatherMain): Boolean {
        info("isCachedWeatherValid: value saved on preferences")
        // check if is still valid
        val c: Calendar = Calendar.getInstance()
        val now = TimeUnit.MILLISECONDS.toSeconds(c.timeInMillis)
        val limit = (60 * 60) * 2 // 2 H
        return now - w.dt!! <= limit
    }
}