package com.tinmegali.myweather.dagger.activities

import com.tinmegali.myweather.MainActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface MainActivitySubComponent : AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class builder : AndroidInjector.Builder<MainActivity>()

}