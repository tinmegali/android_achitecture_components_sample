package com.tinmegali.myweather.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity( tableName = "weather" )
data class WeatherMain(

        @ColumnInfo( name = "date" )
        var dt: Long?,

        @ColumnInfo( name = "city" )
        var name: String?,

        @ColumnInfo(name = "temp_min" )
        var tempMin: Double?,

        @ColumnInfo(name = "temp_max" )
        var tempMax: Double?,

        @ColumnInfo( name = "main" )
        var main: String?,

        @ColumnInfo( name = "description" )
        var description: String?,

        @ColumnInfo( name = "icon" )
        var icon: String?
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

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