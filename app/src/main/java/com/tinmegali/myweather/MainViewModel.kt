package com.tinmegali.myweather

import android.arch.lifecycle.*
import com.tinmegali.myweather.data.LocationLiveData
import com.tinmegali.myweather.models.ApiResponse
import com.tinmegali.myweather.models.WeatherMain
import com.tinmegali.myweather.models.WeatherResponse
import com.tinmegali.myweather.repository.MainRepository
import org.jetbrains.anko.*
import javax.inject.Inject

class MainViewModel
@Inject
constructor(
        private val repository: MainRepository
)
    : ViewModel(), AnkoLogger {

    // errors
    private val errors: MutableLiveData<String> = MutableLiveData()

    // Location
    private val location: LocationLiveData = repository.locationLiveDa()

    // City Name
    private val cityName: MutableLiveData<String> = MutableLiveData()

    // Weather by cache
    private var weatherByCache: LiveData<WeatherMain> = MutableLiveData()

    // Weather saved on database
    private var weatherDB: LiveData<WeatherMain> = MutableLiveData()

    private var weatherByLocationResponse:
            LiveData<ApiResponse<WeatherResponse>> = Transformations.switchMap(
            location,
            {
                l ->
                info("weatherByLocation: \nlocation: $l")
                doAsync { repository.clearOldData() }
                return@switchMap repository.getWeatherByLocation(l)
            }
    )

    private var weatherByCityResponse:
            LiveData<ApiResponse<WeatherResponse>> = Transformations.switchMap(
            cityName,
            {
                city ->
                info("weatherByCityResponse: city: $city")
                doAsync { repository.clearOldData() }
                return@switchMap repository.getWeatherByCity(city)
            }
    )

    private var weatherByCityCache:
            LiveData<WeatherMain> = Transformations.switchMap(
            cityName,
            {
                city ->
                info("weatherByCityCache: city: $city")
                return@switchMap repository.getRecentWeatherForLocation(city)
            }
    )

    // Value observed by View.
    // It transforms a WeatherResponse int to a WeatherMain.
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

    // retrieve errors LiveData
    fun getErrors(): MutableLiveData<String> = errors

    private fun addWeatherSources(){
        info("addWeatherSources")
        weather.addSource(
                weatherByCityResponse,
                {
                    w ->
                    info("weatherByCityResponse:\n$w")
                    processWeatherResponse(w)
                }
        )
        weather.addSource(
                weatherByLocationResponse,
                {
                    w ->
                    info("weatherByLocationResponse:\n$w")
                    processWeatherResponse(w)
                }
        )

    }

    private fun processWeatherResponse(w: ApiResponse<WeatherResponse>?) {
        // success
        if ( w!= null ) {
            warn("addWeatherSources: weatherByLocationResponse: \n${w.data}")
            if (w.data != null) updateWeather(w.data)
            // blank response
            else {
                val msg = "No data found for '${cityName.value}'."
                warn(msg)
                apiError(msg)
            }
        }
        // error
        else {
            val msg = "Something went wrong while fetching weather for '${cityName.value}'."
            error(msg)
            apiError(msg)
        }
    }

    // Receives updated weather response,
    // send it to UI and also save it
    private fun updateWeather(w: WeatherResponse){
        info("updateWeather")
        // getting weather from today
        val weatherMain = WeatherMain.factory(w)
        // save on shared preferences
        repository.saveWeatherMainOnPrefs(weatherMain)
        // save on db
        repository.saveOnDb(weatherMain)
        // update weather value
        weather.postValue(ApiResponse(data = weatherMain))
    }

    // Called when weather api returns an error
    private fun apiError(message: String) {
        info("apiError: %s".format(message))
        errors.postValue(message)
    }

    override fun onCleared() {
        info("onCleared")
        super.onCleared()
        weather.removeSource(weatherByCityResponse)
        weather.removeSource(weatherByLocationResponse)
        weather.removeSource(weatherByCityCache)
    }

    // changes the value of weatherResponse
    // making it react to cityName.
    // update city name.
    fun weatherByCityName(city: String) {
        info("weatherByCityName: '$city'")
        updateCityName(city)
    }

    // updates the city name
    private fun updateCityName(city: String) {
        info("updateCityName: $city")
        weather.addSource(
                weatherByCityCache,
                {
                    w ->
                    info("weatherByCityCache:\n$w")
                    info("Weather taken from db for city '${cityName.value}'")
                    weather.postValue(ApiResponse(w))
                    weather.removeSource(weatherByCityCache)
                }
        )
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
        weatherDB = repository.getRecentWeather()

        weather.addSource(
                weatherDB,
                {
                    w ->
                    info("weatherDB: DB: \n$w")
                    weather.postValue(ApiResponse(data = w))
                    weather.removeSource(weatherDB)
                }
        )
    }
}