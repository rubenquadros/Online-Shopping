@file:Suppress("NAME_SHADOWING")

package rubenquadros.com.waycool.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color.colorToHSV
import android.graphics.Color.parseColor
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import rubenquadros.com.waycool.R
import rubenquadros.com.waycool.service.MyLocationListener
import rubenquadros.com.waycool.utility.ApplicationConstants
import rubenquadros.com.waycool.utility.ApplicationUtility
import rubenquadros.com.waycool.utility.TaskOnComplete
import java.util.*


class Address : AppCompatActivity(), OnMapReadyCallback, TaskOnComplete, PlaceSelectionListener {

    private lateinit var markerOptions: MarkerOptions
    private lateinit var locationService: MyLocationListener
    private lateinit var manager: LocationManager
    private lateinit var addressTv: TextView
    private val REQUEST_CODE = 8888
    private val AUTOCOMPLETE_REQUEST_CODE = 23487
    private lateinit var mMap: GoogleMap
    private var userAddress: String = ""
    private lateinit var mIntent: Intent
    private lateinit var mProgressBar: ProgressBar
    private lateinit var parentLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        initializeToolBar()
        parentLayout = findViewById(R.id.container)
        mProgressBar = findViewById(R.id.progressBar)
        mProgressBar.visibility = View.VISIBLE
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        addressTv = findViewById(R.id.addressTextView)
        addressTv.typeface = ApplicationUtility.fontRegular(this)
        addressTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        doneButton.isEnabled = false
        doneButton.typeface = ApplicationUtility.fontBold(this)
        doneButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_BOLD_SIZE)
        markerOptions = MarkerOptions()
        markerOptions.draggable(true)
        manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!Places.isInitialized()) {
            Places.initialize(this, this.getString(R.string.google_maps_key))
        }
        Places.createClient(this)
        AutocompleteSessionToken.newInstance()
        val fields = Arrays.asList(
            com.google.android.libraries.places.api.model.Place.Field.ID,
            com.google.android.libraries.places.api.model.Place.Field.NAME,
            com.google.android.libraries.places.api.model.Place.Field.LAT_LNG
        )

        val autocompleteFragment: AutocompleteSupportFragment =
            supportFragmentManager.findFragmentById(R.id.mSearchView) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(fields)
        autocompleteFragment.setCountry("IN")
        autocompleteFragment.setOnPlaceSelectedListener(this)
        autocompleteFragment.view!!.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }
        doneButton.setOnClickListener {
            if(userAddress!= "") {
                mIntent = Intent(this@Address, HomeScreen::class.java)
                mIntent.putExtra(ApplicationConstants.ADDRESS, userAddress)
            }
            startActivity(mIntent)
        }
    }

    private fun initializeToolBar() {
        setSupportActionBar(toolbar)
        toolbarTitle.typeface = ApplicationUtility.fontBold(this)
        toolbarTitle.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    addressTv.text = place.address
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11F))
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    Log.d(ApplicationConstants.TAG, "Auto complete error")
                }
                AutocompleteActivity.RESULT_CANCELED -> {
                    Log.d(ApplicationConstants.TAG, "Auto complete cancelled")
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        locationService = MyLocationListener()
        locationService.setListener(this@Address)
        if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET),
                    REQUEST_CODE)
            } else {
                mProgressBar.visibility = View.GONE
                addressTv.text = this.resources.getString(R.string.no_gps)
                Snackbar.make(parentLayout, this.resources.getString(R.string.grant_permission), Snackbar.LENGTH_LONG).show()
            }
        }else{
            if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET),
                    REQUEST_CODE
                )
            }else{
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500000L, 0f, locationService)
            }
        }
        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragEnd(p0: Marker?) {
                getCurrentAddress(p0!!.position.latitude, p0.position.longitude)
            }

            override fun onMarkerDragStart(p0: Marker?) {
                Log.d(ApplicationConstants.TAG, "Drag started")
            }

            override fun onMarkerDrag(p0: Marker?) {
                Log.d(ApplicationConstants.TAG, "Dragging marker")
            }

        })
        mMap.setOnMapClickListener { p0 ->
            markerOptions.position(p0)
            mMap.addMarker(markerOptions)

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(p0))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11F))
            getCurrentAddress(p0.latitude, p0.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_CODE ->{
                if(grantResults.isEmpty() || (grantResults[0]!= PackageManager.PERMISSION_GRANTED) || grantResults[1] != PackageManager.PERMISSION_GRANTED){
                    Snackbar.make(parentLayout, this.resources.getString(R.string.pls_grant_permission), Snackbar.LENGTH_LONG).show()
                }else{
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500000L, 0f, locationService)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getCurrentAddress(latitude: Double, longitude: Double) {
        val geoCoder = Geocoder(this, Locale("en-us", "IN"))
        val addresses: List<Address>
        try {
            addresses = geoCoder.getFromLocation(latitude, longitude, 1)
            if (addresses.isNotEmpty()) {
                val address = addresses[0].getAddressLine(0)
                var locality = addresses[0].locality
                var subLocality = addresses[0].subLocality
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                var postalCode = addresses[0].postalCode
                var street = addresses[0].thoroughfare
                if (street.isNullOrEmpty()) {
                    street = ""
                }
                if (subLocality.isNullOrEmpty()) {
                    subLocality = ""
                }
                if (locality.isNullOrEmpty()) {
                    locality = ""
                }
                if (postalCode.isNullOrEmpty()) {
                    postalCode = ""
                }

                Log.d(
                    ApplicationConstants.TAG,
                    address + locality + subLocality + state + country + postalCode + street
                )
                userAddress = "$street $subLocality $locality $postalCode"
                addressTv.text =
                    this.resources.getString(R.string.cur_add) + "$street $subLocality $locality $postalCode"
            }
        } catch (e: Exception) {
            Snackbar.make(parentLayout, this.resources.getString(R.string.no_gps_or_net), Snackbar.LENGTH_LONG).show()
            addressTv.text = this.resources.getString(R.string.address_err)
        }
    }

    override fun onResponseReceived(latitude: Double, longitude: Double, latLong: LatLng?) {
        mProgressBar.visibility = View.GONE
        if(latLong == null){
            Snackbar.make(parentLayout, this.resources.getString(R.string.grant_permission), Snackbar.LENGTH_LONG).show()
        }else {
            doneButton.isEnabled = true
            markerOptions.position(latLong)
            markerOptions.icon(getMarkerIcon("#2A87C8"))
            mMap.addMarker(markerOptions)

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLong))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15F))
            getCurrentAddress(latitude, longitude)
        }
    }

    private fun getMarkerIcon(color: String): BitmapDescriptor {
        val hsv = FloatArray(3)
        colorToHSV(parseColor(color), hsv)
        return BitmapDescriptorFactory.defaultMarker(hsv[0])
    }

    override fun onPlaceSelected(p0: com.google.android.libraries.places.api.model.Place) {
        addressTv.text = p0.address
        mMap.moveCamera(CameraUpdateFactory.newLatLng(p0.latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11F))
    }

    override fun onError(p0: Status) {
        Log.d(ApplicationConstants.TAG, "Error when getting places")
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }
}
