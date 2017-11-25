package com.tinmegali.myweather.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.tinmegali.myweather.models.WeatherMain

@Database( entities = arrayOf(WeatherMain::class), version = 2 )
abstract class Database : RoomDatabase() {
    abstract fun weatherDAO(): WeatherDAO
}