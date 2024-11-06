package com.example.birdapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Map : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionRequestCode = 1001
    private var userLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val buttonNavigate = findViewById<Button>(R.id.button)
        buttonNavigate.setOnClickListener {
            val intent = Intent(this, Observation::class.java)
            startActivity(intent)
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionRequestCode)
        }

        // Add bird-watching hotspots
        val hotspots = listOf(
            LatLng(40.730610, -73.935242),  // Example hotspot 1
            LatLng(40.748817, -73.985428),  // Example hotspot 2
            LatLng(40.785091, -73.968285)   // Example hotspot 3
        )

        for (hotspot in hotspots) {
            map.addMarker(MarkerOptions().position(hotspot).title("Bird Watching Site"))
        }
    }

    private fun enableUserLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        userLocation = LatLng(location.latitude, location.longitude)

                        // Store userLocation in a local variable to guarantee it's non-null
                        val safeUserLocation = userLocation

                        // Use the local variable (which is guaranteed to be non-null)
                        safeUserLocation?.let { userLoc ->
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 12f))
                            showNearbyHotspots(userLoc)
                        } ?: run {
                            Toast.makeText(this, "Unable to get your location.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, "Location permission is required to show nearby bird-watching sites.", Toast.LENGTH_SHORT).show()
        }
    }



    private fun showNearbyHotspots(userLoc: LatLng) {
        val hotspots = listOf(
            LatLng(40.730610, -73.935242),
            LatLng(40.748817, -73.985428),
            LatLng(40.785091, -73.968285)
        )

        for (hotspot in hotspots) {
            val distance = calculateDistance(userLoc.latitude, userLoc.longitude, hotspot.latitude, hotspot.longitude)
            if (distance <= 5000) {  // Show hotspots within 5 km
                map.addMarker(MarkerOptions().position(hotspot).title("Nearby Bird Watching Site"))
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)  // Ensure calling super
        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation()
            } else {
                Toast.makeText(this, "Location permission is required to show nearby bird-watching sites.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateDistance(userLat: Double, userLng: Double, siteLat: Double, siteLng: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(userLat, userLng, siteLat, siteLng, results)
        return results[0] // Distance in meters
    }
}
