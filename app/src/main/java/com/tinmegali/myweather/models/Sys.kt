package com.tinmegali.myweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Sys(
        @SerializedName("type")
        @Expose
        var type: Int? = null,

        @SerializedName("id")
        @Expose
        var id: Int? = null,

        @SerializedName("message")
        @Expose
        var message: Double? = null,

        @SerializedName("country")
        @Expose
        var country: String? = null,

        @SerializedName("sunrise")
        @Expose
        var sunrise: Double? = null,

        @SerializedName("sunset")
        @Expose
        var sunset: Double? = null
)