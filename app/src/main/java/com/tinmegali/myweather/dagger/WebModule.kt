package com.tinmegali.myweather.dagger

import android.content.Context
import com.tinmegali.myweather.R
import com.tinmegali.myweather.data.PrefsDAO
import com.tinmegali.myweather.web.ErrorUtils
import com.tinmegali.myweather.web.LiveDataCallAdapterFactory
import com.tinmegali.myweather.web.OpenWeatherApi
import com.tinmegali.myweather.web.OpenWeatherService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module class WebModule( appContext: Context ) {

    var openWeatherID: String = appContext.getString(R.string.openWeather)

    @Provides
    @Singleton
    fun providerOpenWeatherID(): String {
        return openWeatherID
    }

    @Provides
    @Singleton
    fun providesRetrofit() : Retrofit {
        return Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
    }

    @Provides
    @Singleton
    fun providesErrorUtil( retrofit: Retrofit ) : ErrorUtils {
        return ErrorUtils( retrofit )
    }

    @Provides
    @Singleton
    fun providesOpenWeatherApi( retrofit: Retrofit ) : OpenWeatherApi {
        return retrofit.create( OpenWeatherApi::class.java )
    }

    @Provides
    @Singleton
    fun providesOpenWeatherService(
            api: OpenWeatherApi,
            errorUtils: ErrorUtils,
            openWeatherId: String,
            prefsDAO: PrefsDAO
    ) : OpenWeatherService {
        return OpenWeatherService( api, errorUtils, openWeatherId, prefsDAO )
    }

}