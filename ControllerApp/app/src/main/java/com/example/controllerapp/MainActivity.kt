package com.example.controllerapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    // Reference to Firebase database
    private lateinit var database: DatabaseReference
    private var musicOn = false
    private var lightsOn = false
    private var volume = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Connect to Firebase
        database = FirebaseDatabase.getInstance().reference

        // Set initial values in Firebase
        database.child("car").child("music").setValue("OFF")
        database.child("car").child("lights").setValue("OFF")
        database.child("car").child("volume").setValue(volume)

        // music toggle button
        findViewById<Button>(R.id.btnMusic).setOnClickListener {
            musicOn = !musicOn
            val state = if (musicOn) "ON" else "OFF"
            database.child("car").child("music").setValue(state)
        }

        // lights toggle button
        findViewById<Button>(R.id.btnLights).setOnClickListener {
            lightsOn = !lightsOn
            val state = if (lightsOn) "ON" else "OFF"
            database.child("car").child("lights").setValue(state)
        }

        // Volume up
        findViewById<Button>(R.id.btnVolUp).setOnClickListener {
            if (volume < 100) {
                volume += 5
                database.child("car").child("volume").setValue(volume)
            }
        }

        // Volume down
        findViewById<Button>(R.id.btnVolDown).setOnClickListener {
            if (volume > 0) {
                volume -= 5
                database.child("car").child("volume").setValue(volume)
            }
        }
    }
}