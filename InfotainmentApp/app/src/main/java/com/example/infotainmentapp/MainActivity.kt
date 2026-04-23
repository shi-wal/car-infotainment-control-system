package com.example.infotainmentapp

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
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().reference

        val tvMusic=findViewById<TextView>(R.id.tvMusic)
        val tvLights=findViewById<TextView>(R.id.tvLights)
        val tvVolume=findViewById<TextView>(R.id.tvVolume)

        //Listen for music changes in real-time
        database.child("car").child("music").
        addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(String::class.java) ?: "OFF"
                tvMusic.text = "🎵 Music: $value"
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        //Listen for light changes
        database.child("car").child("lights").
        addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(String::class.java) ?: "OFF"
                tvLights.text = "🎵 Lights: $value"
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        //Listen for volume changes
        database.child("car").child("volume").
        addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Int::class.java) ?: 0
                tvVolume.text = "🎵 Volume: $value"
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}