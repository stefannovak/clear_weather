package com.stefanalexnovak.weatherapplication.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.stefanalexnovak.weatherapplication.MainActivity

interface LocationGetter {

    fun hasPermissions(context: Context, callback: (Boolean) -> Unit)

    fun getLocation(callback: (Location) -> Unit)
}



class DefaultLocationGetter : LocationGetter {
    override fun hasPermissions(context: Context, callback: (Boolean) -> Unit) {
        callback(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    override fun getLocation(callback: (Location) -> Unit) {
        TODO("Not yet implemented")
    }

}