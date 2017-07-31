package com.tinmegali.myweather.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import android.util.TimeUtils
import com.tinmegali.myweather.models.WeatherMain
import org.jetbrains.anko.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PrefsDAO
    @Inject
    constructor(
            val prefs: SharedPreferences
    ) : AnkoLogger
{
    // Keys
    private val k_date = "WeatherDate"
    private val k_cityName = "CityName"
    private val k_min = "Min"
    private val k_max = "Max"
    private val k_main = "Main"
    private val k_description = "Description"
    private val k_icon = "Icon"
    private val k_iconUrl = "IconUrl"
    private val k_haveWeather = "HaveWeather"
    private val k_units = "Units"

    fun saveWeatherMain( w: WeatherMain ) {
        info("saveWeatherMain")
        doAsync {
            prefs.edit()
                    .putLong(k_date, w.dt!!)
                    .putString(k_cityName, w.name)
                    .putLong(k_min, w.tempMin!!.toLong())
                    .putLong(k_max, w.tempMax!!.toLong())
                    .putString(k_main, w.main)
                    .putString(k_description, w.description)
                    .putString(k_icon, w.icon)
                    .putString(k_iconUrl, w.iconUrl())
                    .putBoolean(k_haveWeather, true)
                    .apply()
        }
    }

    private fun haveWeather() : Boolean {
        info("haveWeather")
        return prefs.getBoolean( k_haveWeather , false )
    }

    fun getWeatherMain(): LiveData<WeatherMain> {
        info("getWeatherMain")
        return (object: Mediator<WeatherMain>() {
            override fun callCache(): LiveData<WeatherMain> {
                val data: MutableLiveData<WeatherMain> = MutableLiveData()
                data.postValue(
                        WeatherMain(
                                dt = prefs.getLong(k_date, -1),
                                name = prefs.getString(k_cityName, null),
                                tempMin = prefs.getLong(k_min, -999).toDouble(),
                                tempMax = prefs.getLong(k_max, -999).toDouble(),
                                main = prefs.getString(k_main, null),
                                description = prefs.getString(k_description, null),
                                icon = prefs.getString(k_icon, null)
                ))
                return data
            }

            override fun isCacheValid(): Boolean {
                info("isCacheValid")
                val exist = haveWeather()
                if ( exist ) {
                    val now = TimeUnit.MILLISECONDS.toSeconds(Date().time)
                    val cacheDate = prefs.getLong(k_date,0L)
                    return now - cacheDate <= TimeUnit.HOURS.toSeconds(2)
                } else return false
            }
        }).get()
    }

    abstract class Mediator<T> : AnkoLogger {
        private var result: MediatorLiveData<T> = MediatorLiveData()

        init {
            info("init")
            doAsync {
                if ( isCacheValid() ) {
                    val cacheResult = callCache()
                    result.addSource(
                            cacheResult,
                            {
                                w ->
                                result.postValue(w)
                                uiThread { result.removeSource(cacheResult) }
                            }
                    )
                }
            }
        }

        abstract fun callCache() : LiveData<T>
        abstract fun isCacheValid() : Boolean

        fun get(): LiveData<T> {
            return result
        }
    }

    private val metric = "metric"
    private val imperial = "imperial"

    fun getUnits() : String {
        info("getUnits")
        return prefs.getString(k_units, metric)
    }

    fun setMetric() {
        warn("Set METRIC as standard unit.")
        doAsync {
            prefs.edit().putString(k_units, metric).apply()
        }
    }
    fun setImperial() {
        warn("Set IMPERIAL as standard unit.")
        // TODO units settings
        doAsync {
            prefs.edit().putString(k_units, imperial).apply()
        }

    }



}