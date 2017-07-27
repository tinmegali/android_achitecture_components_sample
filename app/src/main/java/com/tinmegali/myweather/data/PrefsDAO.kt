package com.tinmegali.myweather.data

import android.content.SharedPreferences
import com.tinmegali.myweather.models.WeatherMain
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.warn
import javax.inject.Inject

/**
 * com.tinmegali.myweather.data | MyWeatherApp
 * __________________________________
 * Created by tinmegali
 * 27/07/17
 * @see <a href="http://www.tinmegali.com">tinmegali.com</a>
 * @see <a href="http://github.com/tinmegali">github</a>
 * ___________________________________
 */
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
        prefs.edit()
                .putLong(k_date, w.dt!! )
                .putString(k_cityName, w.name )
                .putLong(k_min, w.tempMin!!.toLong() )
                .putLong(k_max, w.tempMax!!.toLong() )
                .putString(k_main, w.main )
                .putString(k_description, w.description )
                .putString(k_icon, w.icon )
                .putString(k_iconUrl, w.iconUrl() )
                .putBoolean(k_haveWeather, true )
                .apply()
    }

    fun haveWeather() : Boolean {
        return prefs.getBoolean( k_haveWeather , false )
    }

    fun getWeatherMain(): WeatherMain {
        return WeatherMain(
                dt = prefs.getLong(k_date, -1),
                name = prefs.getString(k_cityName, null),
                tempMin = prefs.getLong(k_min, -999).toDouble(),
                tempMax = prefs.getLong(k_max, -999).toDouble(),
                main = prefs.getString(k_main, null),
                description = prefs.getString(k_description, null),
                icon = prefs.getString(k_icon, null)
        )
    }

    private val metric = "metric"
    private val imperial = "imperial"

    fun getUnits() : String {
        return prefs.getString(k_units, metric)
    }

    fun setMetric() {
        warn("Set METRIC as standard unit.")
        prefs.edit().putString(k_units, metric).apply()
    }
    fun setImperial() {
        warn("Set IMPERIAL as standard unit.")
        prefs.edit().putString(k_units, imperial).apply()
    }



}