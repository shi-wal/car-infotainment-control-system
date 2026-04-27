package com.example.infotainmentapp

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().reference

        val tvMusic = findViewById<TextView>(R.id.tvMusic)
        val tvLights = findViewById<TextView>(R.id.tvLights)
        val tvVolume = findViewById<TextView>(R.id.tvVolume)
        val tvLock = findViewById<TextView>(R.id.tvLock)
        val tvAC = findViewById<TextView>(R.id.tvAC)
        val progressVol = findViewById<ProgressBar>(R.id.progressVolume)

        val green = Color.parseColor("#2E7D32")
        val red = Color.parseColor("#C62828")
        val cyan = Color.parseColor("#00E5FF")

        //Listen for music changes in real-time
        database.child("car").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val music = snapshot.child("music").getValue(String::class.java) ?: "OFF"
                val lights = snapshot.child("lights").getValue(String::class.java) ?: "OFF"
                val lock = snapshot.child("lock").getValue(String::class.java) ?: "LOCKED"
                val ac = snapshot.child("ac_temp").getValue(Int::class.java) ?: 22
                val vol = snapshot.child("volume").getValue(Int::class.java) ?: 50
                tvMusic.text = music
                tvMusic.setTextColor(if (music == "ON") green else red)

                tvLights.text = lights
                tvLights.setTextColor(if (lights == "ON") green else red)

                tvLock.text = lock
                tvLock.setTextColor(if (lock == "LOCKED") red else green)

                tvAC.text = "${ac}°C"
                tvAC.setTextColor(cyan)

                tvVolume.text = "$vol"
                progressVol.progress = vol
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}