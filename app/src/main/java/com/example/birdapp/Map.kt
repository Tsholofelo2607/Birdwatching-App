package com.example.birdapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
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
import com.google.android.gms.maps.model.PolylineOptions


class Map : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionRequestCode = 1001
    private var userLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }
    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Check location permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            enableUserLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionRequestCode
            )
        }

        // Add bird-watching hotspots
        val hotspots = listOf(
            LatLng(-26.1613, 28.0103),  // Delta Park
            LatLng(-26.1770, 27.9490),  // Melville Koppies Nature Reserve
            // Add more hotspots as needed...
        )

        for (hotspot in hotspots) {
            map.addMarker(MarkerOptions().position(hotspot).title("Bird Watching Site"))
        }

        // Set a click listener for markers
        map.setOnMarkerClickListener { marker ->
            showNavigationOptions(marker.position)
            true
        }
    }




    private fun showNavigationOptions(destination: LatLng) {
        val options = arrayOf("Walking", "Driving")
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Choose Navigation Mode")
        builder.setItems(options) { _, which ->
            val mode = if (which == 0) "walking" else "driving"
            openGoogleMapsNavigation(destination, mode)
        }
        builder.show()
    }

    private fun openGoogleMapsNavigation(destination: LatLng, mode: String) {
        val gmmIntentUri = android.net.Uri.parse(
            "google.navigation:q=${destination.latitude},${destination.longitude}&mode=${mode[0]}"
        )
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(this, "Google Maps is not installed on this device.", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun enableUserLocation() {
        try {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
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
                            Toast.makeText(this, "Unable to get your location.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        } catch (e: SecurityException) {
            Toast.makeText(
                this,
                "Location permission is required to show nearby bird-watching sites.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun showNearbyHotspots(userLoc: LatLng) {
        val hotspots = listOf(
            LatLng(-26.1613, 28.0103),  // Delta Park
            LatLng(-26.1770, 27.9490),  // Melville Koppies Nature Reserve
            LatLng(-26.1717, 28.0167),  // Johannesburg Botanical Gardens (Emmarentia)
            LatLng(-26.1467, 27.9094),  // Kloofendal Nature Reserve
            LatLng(-26.1080, 28.0492),  // Innesfree Park (Sandton)
            LatLng(-25.9925, 27.8744),  // Ruimsig Nature Reserve
            LatLng(-26.2883, 27.8894),  // Soweto Bird Sanctuary
            LatLng(-25.9986, 27.8669),  // Walter Sisulu National Botanical Garden
            LatLng(-26.5203, 27.8612),  // Suikerbosrand Nature Reserve
            LatLng(-25.8683, 28.3060),  // Rietvlei Nature Reserve
            LatLng(-25.7463, 28.2194),  // Groenkloof Nature Reserve
            LatLng(-25.9744, 27.9027),  // Lion & Safari Park
            LatLng(-25.9911, 27.9211),  // Hennops River Valley
            LatLng(-25.9270, 27.6612),  // Magaliesberg Biosphere Reserve
            LatLng(-25.8558, 28.2867),  // Moreleta Kloof Nature Reserve
            LatLng(-25.8440, 28.1537)   // Zwartkops Nature Reserve
        )

        // Clear any existing markers and polylines
        map.clear()

        var nearbyHotspotFound = false

        // Iterate over each hotspot to calculate distance and show markers
        for (hotspot in hotspots) {
            val distance = calculateDistance(
                userLoc.latitude,
                userLoc.longitude,
                hotspot.latitude,
                hotspot.longitude
            )

            if (distance <= 15000) {  // Check for hotspots within 15 km
                nearbyHotspotFound = true

                // Add a marker at the hotspot with distance in snippet
                map.addMarker(
                    MarkerOptions().position(hotspot)
                        .title("Nearby Bird Watching Site")
                        .snippet("Distance: ${distance / 1000} km")  // Display distance in km
                )

                // Draw a polyline from the user's location to the hotspot
                val polylineOptions = PolylineOptions()
                    .add(userLoc)  // User's location
                    .add(hotspot)  // Hotspot location
                    .width(5f)      // Polyline width
                    .color(Color.BLUE)  // Polyline color
                map.addPolyline(polylineOptions)
            }
        }

        // Show a message if no nearby hotspots are found within 5 km
        if (!nearbyHotspotFound) {
            Toast.makeText(this, "No bird-watching hotspots found within 15 km", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )  // Ensure calling super
        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation()
            } else {
                Toast.makeText(
                    this,
                    "Location permission is required to show nearby bird-watching sites.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun calculateDistance(
        userLat: Double,
        userLng: Double,
        siteLat: Double,
        siteLng: Double
    ): Float {
        val results = FloatArray(1)
        Location.distanceBetween(userLat, userLng, siteLat, siteLng, results)
        return results[0] // Distance in meters
    }
}