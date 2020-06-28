package com.stefanalexnovak.weatherapplication.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.stefanalexnovak.weatherapplication.MainActivity

interface LocationGetter {

    fun hasPermissions(context: Context, callback: (Boolean) -> Unit)

    fun getLocation(callback: (Location) -> Unit)
}

class DefaultLocationGetter : LocationGetter, GoogleApiClient.ConnectionCallbacks {
    private var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback : LocationCallback
    private  var currentLocation : Location? = null
    private var callback: ((Location) -> Unit)? = null

    constructor(context: Context) {
        GoogleApiClient.Builder(context)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener { result -> println("$result") }
            .build()
            .connect()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                println("HELOOOOOOOOOOOOOOOOOOOOOOOOOO")
                println(locationResult)
                var l = locationResult?.lastLocation
                if (l != null) {
                    currentLocation = Location(l.longitude, l.latitude)
                    if(callback != null) {
                        callback?.let { it(currentLocation!!) }
                    }
                }
            }
        }
    }

    override fun hasPermissions(context: Context, callback: (Boolean) -> Unit) {
        callback(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    override fun getLocation(callback: (Location) -> Unit) {
        this.callback = callback
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        var locationRequest = LocationRequest.create()
        locationRequest.numUpdates = 100
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 2000
        locationRequest.fastestInterval = 1000
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        fusedLocationClient.lastLocation.addOnSuccessListener { location : android.location.Location? ->
            // Got last known location. In some rare situations this can be null.
            if(location != null) {
                currentLocation = Location(location.longitude, location.latitude)
                if(callback != null) {
                    callback?.let { it(currentLocation!!) }
                }
            }

            println("THE LATItttttttttttttttttttttttttttttTUDE IS: ${currentLocation?.longitude}\n\n THE LONGggggggggggggggggITUDE IS: ${currentLocation?.latitude}")
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        println("CONNECTION HAS BEEN SUSPENDED")
    }

}

class MockLocationGetter : LocationGetter {
    override fun hasPermissions(context: Context, callback: (Boolean) -> Unit) {
        callback(true)
    }

    override fun getLocation(callback: (Location) -> Unit) {
        callback(Location(53.4808, 2.2426))
    }

}