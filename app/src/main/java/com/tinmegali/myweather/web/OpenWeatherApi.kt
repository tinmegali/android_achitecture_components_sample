package com.tinmegali.myweather.web

import com.tinmegali.myweather.models.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface OpenWeatherApi {


    // Get Today's Weather by City
    @GET("weather")
    fun cityWeather(@Query("q") city: String, @Query("APPID") appId: String, @Query("units") unit: String )
            : Call<WeatherResponse>

}