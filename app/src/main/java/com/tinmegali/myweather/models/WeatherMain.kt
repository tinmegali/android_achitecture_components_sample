package com.tinmegali.myweather.models

data class WeatherMain(
        val dt: Long?,
        val name: String?,
        val tempMin: Double?,
        val tempMax: Double?,
        val main: String?,
        val description: String?,
        val icon: String?
) {

    companion object {
        fun factory( weatherResponse: WeatherResponse ) : WeatherMain {
            return WeatherMain(
                    dt = weatherResponse.dt!!,
                    name = weatherResponse.name,
                    tempMin = weatherResponse.main!!.tempMin,
                    tempMax = weatherResponse.main!!.tempMax,
                    main = weatherResponse.weather!![0].main,
                    description = weatherResponse.weather!![0].description,
                    icon = weatherResponse.weather!![0].icon
            )
        }
    }

    fun iconUrl() : String {
        return "http://openweathermap.org/img/w/$icon.png"
    }

}