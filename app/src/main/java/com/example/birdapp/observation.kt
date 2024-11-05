package com.example.birdapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class observation : AppCompatActivity() {
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

            // Handle the collected data (e.g., save it to database or display it in a toast)
            Toast.makeText(this, "Observation Submitted:\nSpecies: $speciesName\nDate: $date\nTime: $time\nLocation: $location\nNotes: $notes", Toast.LENGTH_LONG).show()

            // Clear fields after submission
            speciesNameEditText.text.clear()
            locationEditText.text.clear()
            notesEditText.text.clear()
        }
    }
}
