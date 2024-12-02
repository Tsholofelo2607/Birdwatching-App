package com.example.birdapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class observation : AppCompatActivity() {

    private lateinit var observationList: MutableList<Observation>
    private lateinit var observationAdapter: ObservationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_observation)

// Initialize UI elements
        val speciesNameEditText = findViewById<EditText>(R.id.speciesNameEditText)
        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val locationEditText = findViewById<EditText>(R.id.locationEditText)
        val notesEditText = findViewById<EditText>(R.id.notesEditText)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val uploadImageButton = findViewById<Button>(R.id.uploadImageButton)

        // Initialize list and adapter
        observationList = mutableListOf()
        observationAdapter = ObservationAdapter(observationList)



        // Set current date as minimum date for date picker
        val today = Calendar.getInstance()
        datePicker.minDate = today.timeInMillis

        // Submit button logic
        submitButton.setOnClickListener {
            val speciesName = speciesNameEditText.text.toString()
            val location = locationEditText.text.toString()
            val notes = notesEditText.text.toString()

            // Get date from DatePicker
            val day = datePicker.dayOfMonth
            val month = datePicker.month
            val year = datePicker.year
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day)
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)

            // Get time from TimePicker
            val hour = timePicker.hour
            val minute = timePicker.minute
            val time = String.format("%02d:%02d", hour, minute)

            // Add the new observation to the list
            val newObservation = Observation(speciesName, date, time, location, notes)
            observationList.add(newObservation)
            observationAdapter.notifyDataSetChanged()

            // Clear fields after submission
            speciesNameEditText.text.clear()
            locationEditText.text.clear()
            notesEditText.text.clear()

            Toast.makeText(this, "Observation added successfully!", Toast.LENGTH_SHORT).show()
        }
        // Handle image upload (dummy action for now)
        uploadImageButton.setOnClickListener {
            Toast.makeText(this, "Image upload feature not yet implemented!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    data class Observation(
        val speciesName: String,
        val date: String,
        val time: String,
        val location: String,
        val notes: String
    )

    class ObservationAdapter(private val observations: List<Observation>) :
        RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder>() {

        class ObservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val speciesNameTextView: TextView = itemView.findViewById(R.id.speciesNameTextView)
            val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
            val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
            val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
            val notesTextView: TextView = itemView.findViewById(R.id.notesTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObservationViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_observation, parent, false)
            return ObservationViewHolder(view)
        }


        override fun onBindViewHolder(holder: ObservationViewHolder, position: Int) {
            val observation = observations[position]
            holder.speciesNameTextView.text = observation.speciesName
            holder.dateTextView.text = observation.date
            holder.timeTextView.text = observation.time
            holder.locationTextView.text = observation.location
            holder.notesTextView.text = observation.notes
        }

        override fun getItemCount(): Int = observations.size
    }
}

