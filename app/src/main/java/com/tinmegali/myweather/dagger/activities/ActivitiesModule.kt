package com.tinmegali.myweather.dagger.activities

import com.tinmegali.myweather.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module( subcomponents = arrayOf(
        MainActivitySubComponent::class
))
abstract class ActivitiesModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun providesMainActivity(): MainActivity

}