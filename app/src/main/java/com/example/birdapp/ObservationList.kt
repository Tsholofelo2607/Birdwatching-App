package com.example.birdapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ObservationListActivity : AppCompatActivity() {

    private lateinit var observationsRecyclerView: RecyclerView
    private lateinit var observationAdapter: ObservationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_observation_list)



        // Sample data for testing
        val observations = listOf(
            Observation("Sparrow", "2024-11-05", "08:00", "Central Park", "Observed in a tree"),
            Observation("Robin", "2024-11-05", "09:30", "Greenwood Park", "Observed near a pond"),
            Observation("Eagle", "2024-11-05", "12:00", "Mountain Range", "Soaring high above")
        )

        observationsRecyclerView = findViewById(R.id.observationsRecyclerView)
        observationsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with sample data
        observationAdapter = ObservationAdapter(observations)
        observationsRecyclerView.adapter = observationAdapter
    }
}
