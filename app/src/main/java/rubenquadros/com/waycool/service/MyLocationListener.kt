package rubenquadros.com.waycool.service

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import rubenquadros.com.waycool.utility.ApplicationConstants
import rubenquadros.com.waycool.utility.TaskOnComplete

private lateinit var mListener: TaskOnComplete

class MyLocationListener: LocationListener {
    override fun onLocationChanged(location: Location?) {
        mListener.onResponseReceived(location!!.latitude, location.longitude, LatLng(location.latitude, location.longitude) )
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d(ApplicationConstants.TAG, "LOL")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.d(ApplicationConstants.TAG, "LOL")
    }

    override fun onProviderDisabled(provider: String?) {
        Log.d(ApplicationConstants.TAG, "LOL")
    }

    fun setListener(listener: TaskOnComplete) {
        mListener = listener
    }
}