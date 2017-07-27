package com.tinmegali.myweather.dagger

import com.tinmegali.myweather.App
import com.tinmegali.myweather.dagger.activities.ActivitiesModule
import com.tinmegali.myweather.dagger.viewModels.ViewModelsModule

import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        ApplicationModule::class,
        WebModule::class,
        DataModule::class,
        ViewModelsModule::class,
        ActivitiesModule::class
))
interface ApplicationComponent {

    fun inject(app: App)

}
