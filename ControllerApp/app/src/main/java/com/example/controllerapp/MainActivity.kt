package com.example.controllerapp

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    // Reference to Firebase database
    private lateinit var database: DatabaseReference
    private var musicOn = false
    private var lightsOn = false
    private var volume = 50
    private var doorLocked  = true
    private var acTemp = 22

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Connect to Firebase
        database = FirebaseDatabase.getInstance().reference

        val btnMusic= findViewById<Button>(R.id.btnMusic);
        val btnLights= findViewById<Button>(R.id.btnLights);
        val btnVolUp= findViewById<Button>(R.id.btnVolUp);
        val btnVolDown= findViewById<Button>(R.id.btnVolDown);
        val btnLock= findViewById<Button>(R.id.btnLock);
        val btnACUp= findViewById<Button>(R.id.btnACUp);
        val btnACDown= findViewById<Button>(R.id.btnACDown);
        val tvStatus= findViewById<TextView>(R.id.tvStatus);

        // Read current state from firebase
        database.child("car").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                musicOn    = snapshot.child("music").getValue(String::class.java) == "ON"
                lightsOn   = snapshot.child("lights").getValue(String::class.java) == "ON"
                doorLocked = snapshot.child("lock").getValue(String::class.java) != "UNLOCKED"
                volume     = snapshot.child("volume").getValue(Int::class.java) ?: 50
                acTemp     = snapshot.child("ac_temp").getValue(Int::class.java) ?: 22

                updateButton(btnMusic,  musicOn,  "🎵 Music ON",    "🎵 Music OFF")
                updateButton(btnLights, lightsOn, "💡 Lights ON",   "💡 Lights OFF")
                updateButton(btnLock,   !doorLocked, "🔓 Door Unlocked", "🔒 Door Locked")
                tvStatus.text = "Volume: $volume  |  AC: ${acTemp}°C"
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        // music toggle button
        btnMusic.setOnClickListener {
            musicOn = !musicOn
            val state = if (musicOn) "ON" else "OFF"
            database.child("car").child("music").setValue(state)
        }

        // lights toggle button
        btnLights.setOnClickListener {
            lightsOn = !lightsOn
            val state = if (lightsOn) "ON" else "OFF"
            database.child("car").child("lights").setValue(state)
        }

        // Volume up
        btnVolUp.setOnClickListener {
            if (volume < 100) {
                volume += 5
                database.child("car").child("volume").setValue(volume)
            }
        }

        // Volume down
        btnVolDown.setOnClickListener {
            if (volume > 0) {
                volume -= 5
                database.child("car").child("volume").setValue(volume)
            }
        }

        // lock toggle button
        btnLock.setOnClickListener {
            doorLocked  = !doorLocked
            val state = if (doorLocked ) "LOCKED" else "UNLOCKED"
            database.child("car").child("lock").setValue(state)
        }

        // AC temp increase
        btnACUp.setOnClickListener {
            if (acTemp < 30) {
                acTemp += 1
                database.child("car").child("ac_temp").setValue(acTemp)
            }
        }

        // AC temp decrease
        btnACDown.setOnClickListener {
            if (acTemp > 16) {
                acTemp -= 1
                database.child("car").child("ac_temp").setValue(acTemp)
            }
        }
    }
    //green = ON/active, red = OFF/inactive
    private fun updateButton(btn: Button, isActive: Boolean, activeText: String, inactiveText: String){
        btn.text= if (isActive) activeText else inactiveText
        btn.setBackgroundColor(if (isActive) Color.parseColor("#2E7D32") else Color.parseColor("#C62828"))
    }
}