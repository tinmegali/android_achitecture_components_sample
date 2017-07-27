package com.tinmegali.myweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Weather {

    @SerializedName("id")
    @Expose
    private var id: Int? = null

    @SerializedName("main")
    @Expose
    private var main: String? = null

    @SerializedName("description")
    @Expose
    private var description: String? = null

    @SerializedName("icon")
    @Expose
    private var icon: String? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getMain(): String? {
        return main
    }

    fun setMain(main: String) {
        this.main = main
    }

    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getIcon(): String? {
        return icon
    }

    fun setIcon(icon: String) {
        this.icon = icon
    }


}