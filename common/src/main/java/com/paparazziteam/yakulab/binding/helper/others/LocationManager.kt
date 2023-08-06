package com.paparazziteam.yakulab.binding.helper.others

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationManager(private val context:Context) : LifecycleObserver {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // Handle location updates here
            //TODO: DO SOMETHING WITH THE LOCATION ** locationCallback
        }

        override fun onLocationAvailability(locationAvailability: LocationAvailability) {
            // Handle location availability changes here
            //TODO: DO SOMETHING WITH THE LOCATION ** locationAvailability
        }
    }


    fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions if not granted
            // You should implement the permission request flow here
            return
        }

        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL)

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }


    fun removeLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun getLastKnownLocation(listener: OnLocationReceivedListener) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions if not granted
            // You should implement the permission request flow here
            return
        }

        fusedLocationClient.lastLocation
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val lastKnownLocation: Location? = task.result
                    listener.onLocationReceived(lastKnownLocation)
                } else {
                    // Handle failure to get last known location
                    listener.onLocationFailed()
                }
            }
    }

    fun getLastKnownLocationOnlyOnce(listener: OnLocationReceivedListener) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions if not granted
            // You should implement the permission request flow here
            listener.onLocationFailed()
            return
        }

        fusedLocationClient.lastLocation
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val lastKnownLocation: Location? = task.result
                    listener.onLocationReceived(lastKnownLocation)
                } else {
                    // Handle failure to get last known location
                    listener.onLocationFailed()
                }
                //Remove location updates for only once
                removeLocationUpdates()
            }
    }

    interface OnLocationReceivedListener {
        fun onLocationReceived(location: Location?)
        fun onLocationFailed()
        fun onPermissionFailed()
    }

    companion object {
        private const val UPDATE_INTERVAL: Long = 10 * 1000 // 10 seconds
        private const val FASTEST_INTERVAL: Long = 5 * 1000 // 5 seconds
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        removeLocationUpdates()
    }

}