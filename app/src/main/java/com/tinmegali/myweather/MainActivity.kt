package com.tinmegali.myweather

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.tinmegali.myweather.models.WeatherMain
import com.tinmegali.myweather.repository.MainRepository
import com.tinmegali.myweather.web.OpenWeatherService
import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import javax.inject.Inject

class MainActivity : LifecycleActivity(), AnkoLogger {


    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    var viewModel: MainViewModel? = null

    @Inject lateinit var weatherService: OpenWeatherService
    @Inject lateinit var mainRepository: MainRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        info("onCreate")
        // Dagger
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initModel()
        containerWeather.visibility = View.GONE

        btnGetCity.setOnClickListener {
            info("get city: ${editCity.text}")
            doAsync {
                if ( editCity.text != null ) {
                    uiThread { isLoading(true) }
                    getWeather(editCity.text.toString())
                } else {
                    uiThread {
                        toast("Please, write a city.")
                    }
                }
            }
        }

    }

    private fun getWeather(city: String) {
        info("getWeather: $city")
        viewModel!!.getWeatherByCity(this, city )
    }

    private fun isLoading( isLoading: Boolean ) {
        if ( isLoading )
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE
    }

    private fun setUI( data: WeatherMain ) {
        info("setUI")
        isLoading(false)
        containerWeather.visibility = View.VISIBLE
        txtCity.text = data.name
        txtMain.text = data.main
        txtDescription.text = data.description
        txtMin.text = data.tempMin.toString()
        txtMax.text = data.tempMax.toString()
        Glide.with(this).load(data.iconUrl()).into(imgIcon)
    }

    private fun initModel() {
        isLoading(true)
        // Get ViewModel
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MainViewModel::class.java)
        if (viewModel != null) {

            // start to observe weather
            viewModel!!.cityWeather.observe(
                    this,
                    Observer {
                        r ->
                        info("Weather received on k_main Activity:\n $r")
                        if (!r!!.hasError()) {
                            // Doesn't have any errors
                            info("weather: ${r.data}")
                            if ( r.data != null) setUI(r.data)
                        } else {
                            // error
                            error("error: ${r.error}")
                            isLoading(false)
                            if ( r.error!!.message != null )
                                Toast.makeText(this,r.error.message, Toast.LENGTH_SHORT).show()
                            else
                                Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
            )
        }

        // get cached model
        viewModel!!.getWeatherCached()
    }
}
