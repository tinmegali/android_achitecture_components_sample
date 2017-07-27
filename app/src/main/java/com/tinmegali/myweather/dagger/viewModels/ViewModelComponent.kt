package com.tinmegali.myweather.dagger.viewModels

import com.tinmegali.myweather.MainViewModel
import dagger.Component

@Component( modules = arrayOf(
        ViewModelsModule::class
))
interface ViewModelComponent {

    fun inject( mainViewModel: MainViewModel )

}