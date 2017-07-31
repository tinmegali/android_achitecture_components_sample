package com.tinmegali.myweather.dagger.viewModels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tinmegali.myweather.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey( MainViewModel::class )
    abstract fun bindMainViewModel( mainViewModel: MainViewModel ) : ViewModel

    @Binds
    abstract fun bindViewModelFactory( factory: ViewModelFactory ) : ViewModelProvider.Factory

}