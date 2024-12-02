package com.example.birdapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ObservationListActivity : AppCompatActivity() {

    private lateinit var observationsRecyclerView: RecyclerView
    private lateinit var observationAdapter: observation.ObservationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.observation_list)

        // Sample data for testing
        val observations = listOf(
            observation.Observation(
                "Sparrow",
                "2024-11-05",
                "08:00",
                "Central Park",
                "Observed in a tree"
            ),
            observation.Observation(
                "Robin",
                "2024-11-05",
                "09:30",
                "Greenwood Park",
                "Observed near a pond"
            ),
            observation.Observation(
                "Eagle",
                "2024-11-05",
                "12:00",
                "Mountain Range",
                "Soaring high above"
            )
        )

        // Set up RecyclerView
        observationsRecyclerView = findViewById(R.id.observationsRecyclerView)
        observationsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with sample data
        observationAdapter = observation.ObservationAdapter(observations)
        observationsRecyclerView.adapter = observationAdapter
    }
}