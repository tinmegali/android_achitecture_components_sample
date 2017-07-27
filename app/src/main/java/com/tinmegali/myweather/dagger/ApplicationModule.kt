package com.tinmegali.myweather.dagger

import android.content.Context
import android.content.SharedPreferences
import com.tinmegali.myweather.App

import com.tinmegali.myweather.dagger.ForApplication

import javax.inject.Singleton

import dagger.Module
import dagger.Provides


@Module
class ApplicationModule(internal var app: App) {

    @Provides
    @Singleton
    fun application(): App {
        return app
    }

    @Provides
    @ForApplication
    @Singleton
    fun context(): Context {
        return app
    }
}
