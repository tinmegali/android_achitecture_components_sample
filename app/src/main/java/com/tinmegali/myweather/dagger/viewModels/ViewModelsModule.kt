package com.tinmegali.myweather.dagger.viewModels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tinmegali.myweather.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * com.tinmegali.myweather.dagger.viewModels | MyWeatherApp
 * __________________________________
 * Created by tinmegali
 * 26/07/17
 * @see <a href="http://www.tinmegali.com">tinmegali.com</a>
 * @see <a href="http://github.com/tinmegali">github</a>
 * ___________________________________
 */
@Module
abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey( MainViewModel::class )
    abstract fun bindMainViewModel( mainViewModel: MainViewModel ) : ViewModel

    @Binds
    abstract fun bindViewModelFactory( factory: ViewModelFactory ) : ViewModelProvider.Factory

}