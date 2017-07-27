package com.tinmegali.myweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Rain(
        @SerializedName("3h")
        @Expose
        var _3h: Int? = null
)