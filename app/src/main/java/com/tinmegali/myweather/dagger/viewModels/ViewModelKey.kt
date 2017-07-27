package com.tinmegali.myweather.dagger.viewModels

import android.arch.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention()
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)