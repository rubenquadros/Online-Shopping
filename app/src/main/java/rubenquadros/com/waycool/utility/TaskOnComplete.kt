package rubenquadros.com.waycool.utility

import com.google.android.gms.maps.model.LatLng

interface TaskOnComplete {
    fun onResponseReceived(latitude: Double, longitude: Double, latLong: LatLng?)
}