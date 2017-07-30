package com.tinmegali.myweather.dagger

import android.content.Context
import android.content.SharedPreferences
import com.tinmegali.myweather.data.LocationLiveData
import com.tinmegali.myweather.data.PrefsDAO
import com.tinmegali.myweather.repository.MainRepository
import com.tinmegali.myweather.web.OpenWeatherService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule( val context: Context ) {

    @Provides
    @Singleton
    fun accountPrefs(): SharedPreferences {
        return context.getSharedPreferences( "accountSharedPrefs", Context.MODE_PRIVATE )
    }


    @Provides
    @Singleton
    fun providesPrefsDAO(
            sharedPreferences: SharedPreferences
    ) : PrefsDAO {
        return PrefsDAO( sharedPreferences )
    }

    @Provides
    @Singleton
    fun providesLocationData(
    ) : LocationLiveData {
        return LocationLiveData( context )
    }

    @Provides
    @Singleton
    fun providesMainRepository(
            openWeatherService: OpenWeatherService,
            prefsDAO: PrefsDAO,
            locationLiveData: LocationLiveData
    ) : MainRepository {
        return MainRepository(
                openWeatherService,
                prefsDAO,
                locationLiveData
        )
    }

}