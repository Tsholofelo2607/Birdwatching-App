package com.example.birdapp

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.collections.Map

class Map : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Get the button by its ID
        val buttonNavigate = findViewById<Button>(R.id.button)

        // Set an onClickListener on the button
        buttonNavigate.setOnClickListener {
            // Create an intent to navigate to SecondActivity
            val intent = Intent(this, Observation::class.java)
            startActivity(intent)
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)  // This line initializes the map asynchronously
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Example: Set a marker for a sample bird-watching site
        val birdWatchingSite = LatLng(40.7128, -74.0060)  // Replace with actual coordinates
        map.addMarker(MarkerOptions().position(birdWatchingSite).title("Bird Watching Site"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(birdWatchingSite, 10f))

        // Further implementation for user location and more markers can go here
    }

    // Function to calculate distance between two points
    fun calculateDistance(userLat: Double, userLng: Double, siteLat: Double, siteLng: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(userLat, userLng, siteLat, siteLng, results)
        return results[0] // Distance in meters
    }
}
