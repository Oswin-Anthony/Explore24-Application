package com.anthony.explore24

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.anthony.explore24.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
     private lateinit var binding: ActivityMainBinding
     private lateinit var fusedLocation: FusedLocationProviderClient
     private lateinit var locationRequest: LocationRequest
     private lateinit var imgLocation: ImageView
     private lateinit var imgSearch: ImageView
     private lateinit var txtLocation: TextView
     private val permissionId = 101
     
     override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          
          binding = ActivityMainBinding.inflate(layoutInflater)
          setContentView(binding.root)
          val navView: BottomNavigationView = binding.navView
          val navController = findNavController(R.id.nav_host_fragment_activity_main)
          navView.setupWithNavController(navController)
          
          fusedLocation = LocationServices.getFusedLocationProviderClient(this)
          imgLocation = findViewById(R.id.img_toolbar_location)
          imgLocation.setOnClickListener {
               fetchLocation()
          }
          txtLocation = findViewById(R.id.txt_toolbar_location)
          txtLocation.setOnClickListener {
               fetchLocation()
          }
          
          imgSearch = findViewById(R.id.img_search)
          imgSearch.setOnClickListener {
               val intent = Intent(this@MainActivity, ListActivity::class.java)
               intent.putExtra("description", "all")
               startActivity(intent)
          }
     }
     
     override fun onResume() {
          super.onResume()
          fusedLocation = LocationServices.getFusedLocationProviderClient(this)
          fetchLocation()
     }
     
     private fun fetchLocation() {
          val task = fusedLocation.lastLocation
          if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
               ) != PackageManager.PERMISSION_GRANTED
               && ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
               ) != PackageManager.PERMISSION_GRANTED
          ) {
               ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                         android.Manifest.permission.ACCESS_FINE_LOCATION,
                         android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    permissionId
               )
               return
          } else {
               val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
               val bol = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                       || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
               if (bol) {
                    task.addOnSuccessListener {
                         if (it != null) {
                              val location: String
                              val longitude = it.longitude
                              val latitude = it.latitude
                              location = getAddress(latitude, longitude)
                              txtLocation.text = location
                         } else {
                              locationRequest = LocationRequest()
                              locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                              locationRequest.interval = 0
                              locationRequest.fastestInterval = 0
                              locationRequest.numUpdates = 2
                              fusedLocation.requestLocationUpdates(
                                   locationRequest,
                                   locationCallback,
                                   Looper.myLooper()
                              )
                         }
                    }
               } else {
                    Toast
                         .makeText(
                              this,
                              "Please turn you location setting on!",
                              Toast.LENGTH_LONG
                         )
                         .show()
               }
          }
     }
     
     private val locationCallback = object : LocationCallback() {
          override fun onLocationResult(p0: LocationResult) {
               val lastLocation: Location? = p0.lastLocation
               val location: String
               val longitude = lastLocation!!.longitude
               val latitude = lastLocation.latitude
               location = getAddress(latitude, longitude)
               txtLocation.text = location
          }
     }
     
     private fun getAddress(lat: Double, lon: Double): String {
          var address = ""
          val geoCoder = Geocoder(this, Locale.getDefault())
          val city = geoCoder.getFromLocation(lat, lon, 1)
          if (city != null) {
               address += city[0].subLocality
          }
          address += ", "
          val geoCoder2 = Geocoder(this, Locale.getDefault())
          val country = geoCoder2.getFromLocation(lat, lon, 1)
          if (country != null) {
               address += country[0].countryCode
          }
          return address
     }
}