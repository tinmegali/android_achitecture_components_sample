package com.tinmegali.myweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Main(
        @SerializedName("temp")
        @Expose
        var temp: Double? = null,

        @SerializedName("pressure")
        @Expose
        var pressure: Float? = null,

        @SerializedName("humidity")
        @Expose
        var humidity: Float? = null,

        @SerializedName("temp_min")
        @Expose
        var tempMin: Double? = null,

        @SerializedName("temp_max")
        @Expose
        var tempMax: Double? = null
        )