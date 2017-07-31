package com.tinmegali.myweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Response
import java.util.function.Consumer


data class WeatherResponse(

        @SerializedName("coord")
        @Expose
        var coord: Coord? = null,

        @SerializedName("weather")
        @Expose
        var weather: List<Weather>? = null,

        @SerializedName("base")
        @Expose
        var base: String? = null,

        @SerializedName("main")
        @Expose
        var main: Main? = null,

        @SerializedName("wind")
        @Expose
        var wind: Wind? = null,

        @SerializedName("clouds")
        @Expose
        var clouds: Clouds? = null,

        @SerializedName("rain")
        @Expose
        var rain: Rain? = null,

        @SerializedName("dt")
        @Expose
        var dt: Long? = null,

        @SerializedName("sys")
        @Expose
        var sys: Sys? = null,

        @SerializedName("id")
        @Expose
        var id: Int? = null,

        @SerializedName("name")
        @Expose
        var name: String? = null,

        @SerializedName("cod")
        @Expose
        var cod: Int? = null

) : AnkoLogger {


    override fun toString(): String {
        val str: StringBuilder = StringBuilder()
        str.append("WeatherResponse:\n")
        str.append("name:$name\n")
        str.append("coord:[${coord!!.lat},${coord!!.lon}]\n")
        str.append("weather:\n")
        for ( w in weather!! ) {
            str.append("id:${w.id}\n")
            str.append("--description: ${w.description}\n")
            str.append("--main: ${w.main}\n")
        }
        return str.toString()
    }
}