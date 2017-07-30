package com.tinmegali.myweather.data

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import javax.inject.Inject

class LocationLiveData
    @Inject
    constructor(
            context: Context
    ) : LiveData<Location>(), LocationListener, AnkoLogger {

    private val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override fun onActive() {
        info("onActive")
        getLocation()
    }

    @SuppressLint("MissingPermission")
    override fun onInactive() {
        info("onInactive")
        locationManager.removeUpdates(this)
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        info("getLocation")
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null )
    }

    fun refreshLocation() {
        info("refreshLocation")
        getLocation()
    }

    override fun onLocationChanged(p0: Location?) {
        info("onLocationChanged:\n$p0")
        value = p0
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        info("onStatusChanged:\n$p0\n$p1\n$p2")
    }

    override fun onProviderEnabled(p0: String?) {
        info("onProviderEnabled: $p0")
    }

    override fun onProviderDisabled(p0: String?) {
        info("onProviderDisabled: $p0")
    }
}