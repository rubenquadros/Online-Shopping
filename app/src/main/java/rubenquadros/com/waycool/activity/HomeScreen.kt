package rubenquadros.com.waycool.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import rubenquadros.com.waycool.R
import rubenquadros.com.waycool.custom.BottomNavigationBehaviour
import rubenquadros.com.waycool.fragments.Cart
import rubenquadros.com.waycool.fragments.Categories
import rubenquadros.com.waycool.fragments.Dashboard
import rubenquadros.com.waycool.fragments.Shopping
import rubenquadros.com.waycool.service.LocationService
import rubenquadros.com.waycool.utility.ApplicationConstants
import rubenquadros.com.waycool.utility.TaskOnComplete
import java.util.*

class HomeScreen : AppCompatActivity(), TaskOnComplete {

    private lateinit var manager: LocationManager
    private val REQUEST_CODE = 9999
    private val REQUEST_CHECK_SETTINGS_GPS = 0x1
    private lateinit var curLocation: ImageView
    private lateinit var addressText: TextView
    private lateinit var parentLayout: CoordinatorLayout
    private lateinit var bottomBar: BottomNavigationView
    private lateinit var locationService: LocationService

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        lateinit var fragment: Fragment
        when (item.itemId) {
            R.id.navigation_home -> {
                fragment = Shopping()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_cart -> {
                fragment = Cart()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_categories -> {
                fragment = Categories()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                fragment = Dashboard()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        locationService = LocationService(this@HomeScreen, null)
        locationService.setListener(this@HomeScreen)
        manager = getSystemService( Context.LOCATION_SERVICE ) as LocationManager
        if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET),
                    REQUEST_CODE)
            } else {
                if(intent.getStringExtra(ApplicationConstants.ADDRESS).isNullOrEmpty()) {
                    locationService.init()
                }
            }
        }else{
            if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET),
                    REQUEST_CODE
                )
            }else {
                if (intent.getStringExtra(ApplicationConstants.ADDRESS).isNullOrEmpty()) {
                    locationService.init()
                }
            }
        }

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navView.selectedItemId = R.id.navigation_home

        val layoutParams: CoordinatorLayout.LayoutParams = navView.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = BottomNavigationBehaviour()
        curLocation = findViewById(R.id.gps)
        curLocation.visibility = View.VISIBLE
        parentLayout = findViewById(R.id.container)
        bottomBar = findViewById(R.id.nav_view)
        addressText = findViewById(R.id.toolbarTitle)
        addressText.setSingleLine(false)
        if(!intent.getStringExtra(ApplicationConstants.ADDRESS).isNullOrEmpty()) {
            addressText.text = intent.getStringExtra(ApplicationConstants.ADDRESS)
        }
        addressText.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                startActivity(Intent(this, rubenquadros.com.waycool.activity.Address::class.java))
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET),
                    REQUEST_CODE)
            }
        }

        curLocation.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                startActivity(Intent(this, rubenquadros.com.waycool.activity.Address::class.java))
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET),
                    REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_CODE ->{
                if(grantResults.isEmpty() || (grantResults[0]!= PackageManager.PERMISSION_GRANTED) || grantResults[1] != PackageManager.PERMISSION_GRANTED){
                    Snackbar.make(parentLayout, this.resources.getString(R.string.pls_grant_permission), Snackbar.LENGTH_LONG).apply {view.layoutParams = (view.layoutParams as CoordinatorLayout.LayoutParams).apply {setMargins(leftMargin, topMargin, rightMargin, bottomBar.height)}}.show()
                }else{
                    locationService.init()
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CHECK_SETTINGS_GPS -> {
                locationService.onActivityResult(requestCode, resultCode, data)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun getCurrentAddress(latitude:Double, longitude:Double) {
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
                if(street.isNullOrEmpty()){
                    street = ""
                }
                if(subLocality.isNullOrEmpty()){
                    subLocality = ""
                }
                if(locality.isNullOrEmpty()){
                    locality = ""
                }
                if(postalCode.isNullOrEmpty()) {
                    postalCode = ""
                }

                Log.d(ApplicationConstants.TAG, address + locality + subLocality + state + country + postalCode + street)
                addressText.text = "$street $subLocality $locality $postalCode"
        }
        }catch (e: Exception) {
            Snackbar.make(parentLayout, this.resources.getString(R.string.no_internet), Snackbar.LENGTH_LONG).apply {view.layoutParams = (view.layoutParams as CoordinatorLayout.LayoutParams).apply {setMargins(leftMargin, topMargin, rightMargin, bottomBar.height)}}.show()
        }
    }

    override fun onResponseReceived(latitude: Double, longitude: Double, latLong: LatLng?) {
        if (latLong == null){
            Snackbar.make(parentLayout, this.resources.getString(R.string.grant_permission), Snackbar.LENGTH_LONG).apply {view.layoutParams = (view.layoutParams as CoordinatorLayout.LayoutParams).apply {setMargins(leftMargin, topMargin, rightMargin, bottomBar.height)}}.show()
        }else {
            getCurrentAddress(latitude, longitude)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}
