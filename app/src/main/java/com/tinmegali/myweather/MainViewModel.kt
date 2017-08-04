package com.tinmegali.myweather

import android.arch.lifecycle.*
import com.tinmegali.myweather.data.LocationLiveData
import com.tinmegali.myweather.models.ApiResponse
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
    private val location: LocationLiveData = repository.locationLiveDa()

    // City Name
    private val cityName: MutableLiveData<String> = MutableLiveData()

    // Weather by cache
    private var weatherByCache: LiveData<WeatherMain> = MutableLiveData()

    private var weatherByLocationResponse:
            LiveData<ApiResponse<WeatherResponse>> = Transformations.switchMap(
            location,
            {
                l ->
                info("weatherByLocation: \nlocation: $l")
                return@switchMap repository.getWeatherByLocation(l)
            }
    )

    private var weatherByCityResponse:
            LiveData<ApiResponse<WeatherResponse>> = Transformations.switchMap(
            cityName,
            {
                city ->
                info("weatherByCityResponse: city: $city")
                return@switchMap repository.getWeatherByCity(city)
            }
    )

    // Value observed by View.
    // It transform a WeatherResponse to a WeatherMain.
    private val weather:
            MediatorLiveData<ApiResponse<WeatherMain>> = MediatorLiveData()

    init {
        info("init")
        getWeatherCached()
        addWeatherSources()
    }

    // retrieve weather LiveData
    fun getWeather(): LiveData<ApiResponse<WeatherMain>> {
        info("getWeather")
        return weather
    }

    private fun addWeatherSources(){
        info("addWeatherSources")
        weather.addSource(
                weatherByCityResponse,
                {
                    w ->
                    info("addWeatherSources: \nweather: ${w!!.data!!}")
                    updateWeather(w.data!!)
                }
        )
        weather.addSource(
                weatherByLocationResponse,
                {
                    w ->
                    info("addWeatherSources: weatherByLocationResponse: \n${w!!.data!!}")
                    updateWeather(w.data!!)
                }
        )

    }

    private fun updateWeather(w: WeatherResponse){
        info("updateWeather")
        // getting weather from today
        val weatherMain = WeatherMain.factory(w)
        // save on shared preferences
        repository.saveWeatherMainOnPrefs(weatherMain)
        // update weather value
        weather.postValue(ApiResponse(data = weatherMain))
    }

    override fun onCleared() {
        info("onCleared")
        super.onCleared()
        weather.removeSource(weatherByCityResponse)
        weather.removeSource(weatherByLocationResponse)
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

    // changes the value of weatherResponse
    // making it react to location
    // update current location
    fun weatherByLocation() {
        info("weatherByLocation")
        repository.refreshLocation()
    }

    private fun getWeatherCached() {
        info("getWeatherCached")
        weatherByCache = repository.getWeatherMainFromPrefs()
        weather.addSource(
                weatherByCache,
                {
                    w ->
                    info("getWeatherCached: \n$w")
                    weather.postValue(ApiResponse(data = w))
                    weather.removeSource(weatherByCache)
                }
        )

    }
}