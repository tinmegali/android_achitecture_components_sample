package com.tinmegali.myweather.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.tinmegali.myweather.models.WeatherMain

@Dao
interface WeatherDAO {

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    fun insert( w: WeatherMain )

    @Delete
    fun remove( w: WeatherMain )

    @Query( "SELECT * FROM weather " +
            "ORDER BY id DESC LIMIT 1" )
    fun findLast(): LiveData<WeatherMain>

    @Query("SELECT * FROM weather " +
            "WHERE city LIKE :city " +
            "ORDER BY date DESC LIMIT 1")
    fun findByCity(city: String ): LiveData<WeatherMain>

    @Query("SELECT * FROM weather " +
            "WHERE date < :date " +
            "ORDER BY date ASC LIMIT 1" )
    fun findByDate( date: Long ): List<WeatherMain>
}