package com.tinmegali.myweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Wind(
        @SerializedName("speed")
        @Expose
        private var speed: Double? = null,

        @SerializedName("deg")
        @Expose
        private var deg: Double? = null
)