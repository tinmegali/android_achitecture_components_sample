package com.tinmegali.myweather.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
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
    private val keyDate = "WeatherDate"
    private val keyCityName = "CityName"
    private val keyMin = "Min"
    private val keyMax = "Max"
    private val keyMain = "Main"
    private val keyDescription = "Description"
    private val keyIcon = "Icon"
    private val keyIconUrl = "IconUrl"
    private val keyHaveWeather = "HaveWeather"
    private val keyUnits = "Units"

    fun saveWeatherMain( w: WeatherMain ) {
        info("saveWeatherMain")
        doAsync {
            prefs.edit()
                    .putLong(keyDate, w.dt!!)
                    .putString(keyCityName, w.name)
                    .putLong(keyMin, w.tempMin!!.toLong())
                    .putLong(keyMax, w.tempMax!!.toLong())
                    .putString(keyMain, w.main)
                    .putString(keyDescription, w.description)
                    .putString(keyIcon, w.icon)
                    .putString(keyIconUrl, w.iconUrl())
                    .putBoolean(keyHaveWeather, true)
                    .apply()
        }
    }

    private fun haveWeather() : Boolean {
        info("haveWeather")
        return prefs.getBoolean(keyHaveWeather, false )
    }

    fun getWeatherMain(): LiveData<WeatherMain> {
        info("getWeatherMain")
        return (object: Mediator<WeatherMain>() {
            override fun callCache(): LiveData<WeatherMain> {
                val data: MutableLiveData<WeatherMain> = MutableLiveData()
                data.postValue(
                        WeatherMain(
                                dt = prefs.getLong(keyDate, -1),
                                name = prefs.getString(keyCityName, null),
                                tempMin = prefs.getLong(keyMin, -999).toDouble(),
                                tempMax = prefs.getLong(keyMax, -999).toDouble(),
                                main = prefs.getString(keyMain, null),
                                description = prefs.getString(keyDescription, null),
                                icon = prefs.getString(keyIcon, null)
                ))
                return data
            }

            override fun isCacheValid(): Boolean {
                info("isCacheValid")
                val exist = haveWeather()
                if ( exist ) {
                    val now = TimeUnit.MILLISECONDS.toSeconds(Date().time)
                    val cacheDate = prefs.getLong(keyDate,0L)
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
        return prefs.getString(keyUnits, metric)
    }

    fun setMetric() {
        warn("Set METRIC as standard unit.")
        doAsync {
            prefs.edit().putString(keyUnits, metric).apply()
        }
    }
    fun setImperial() {
        warn("Set IMPERIAL as standard unit.")
        // TODO units settings
        doAsync {
            prefs.edit().putString(keyUnits, imperial).apply()
        }

    }



}