package rubenquadros.com.waycool.service

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import rubenquadros.com.waycool.utility.ApplicationConstants.Companion.TAG
import rubenquadros.com.waycool.utility.TaskOnComplete

private val REQUEST_CHECK_SETTINGS_GPS = 0x1
private var googleApiClient: GoogleApiClient? = null
private lateinit var mListener: TaskOnComplete

@Suppress("NAME_SHADOWING")
class LocationService(val activity: Activity, val mMap: GoogleMap?): GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
    LocationListener {

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d(TAG, "Connection Failed")
    }

    override fun onConnected(p0: Bundle?) {
        locationRequest()
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.d(TAG, "Connection Suspended")
    }

    override fun onLocationChanged(p0: Location?) {
        val latitude = p0!!.latitude
        val longitude = p0.longitude
        val latLng = LatLng(latitude, longitude)
        //stop location updates
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
        }
        mListener.onResponseReceived(latitude,longitude, latLng)
    }

    private fun locationRequest() {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 1
        mLocationRequest.fastestInterval = 1
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
        builder.setAlwaysShow(true)

        val result: PendingResult<LocationSettingsResult> = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    // All location settings are satisfied.
                    // You can initialize location requests here.
                    val permissionLocation = ContextCompat
                        .checkSelfPermission(activity,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this@LocationService)
                    }
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                    // Location settings are not satisfied.
                    // But could be fixed by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        // Ask to turn on GPS automatically
                        status.startResolutionForResult(activity,
                            REQUEST_CHECK_SETTINGS_GPS)
                    } catch (e: Exception) {
                        // Ignore the error.
                    }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
            }// Location settings are not satisfied. However, we have no way to fix the
            // settings so we won't show the dialog.
            //finish();
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_CHECK_SETTINGS_GPS -> {
                when(resultCode){
                    Activity.RESULT_OK -> locationRequest()
                    Activity.RESULT_CANCELED -> mListener.onResponseReceived(0.0,0.0,null)
                }
            }
        }
    }

    fun setListener(listener: TaskOnComplete) {
        mListener = listener
    }

    fun init() {
        googleApiClient = GoogleApiClient.Builder(activity)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        if(googleApiClient != null){
            googleApiClient!!.connect()
        }
    }
}