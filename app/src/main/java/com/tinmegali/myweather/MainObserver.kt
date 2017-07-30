package com.tinmegali.myweather

import android.arch.lifecycle.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * com.tinmegali.myweather | MyWeatherApp
 * __________________________________
 * Created by tinmegali
 * 28/07/17
 * @see <a href="http://www.tinmegali.com">tinmegali.com</a>
 * @see <a href="http://github.com/tinmegali">github</a>
 * ___________________________________
 */
class MainObserver : LifecycleObserver, AnkoLogger {

    @OnLifecycleEvent( Lifecycle.Event.ON_RESUME )
    fun onResult() {
        info("onResult")
    }

    @OnLifecycleEvent( Lifecycle.Event.ON_STOP )
    fun onStop() {
        info("onStop")
    }

    @OnLifecycleEvent( Lifecycle.Event.ON_ANY )
    fun onEvent( owner: LifecycleOwner, event: Lifecycle.Event )
    {
        info("onEvent: ownerState: ${owner.lifecycle.currentState}")
        info("onEvent: event: $event")
    }
}