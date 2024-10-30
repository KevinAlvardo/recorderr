package com.example.rocord

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRecordVideo = findViewById<Button>(R.id.btnRecordVideo)
        val btnViewVideos = findViewById<Button>(R.id.btnViewVideos)
        val btnPlayLastVideo = findViewById<Button>(R.id.btnPlayLastVideo)

        // Botón para grabar video
        btnRecordVideo.setOnClickListener {
            val intent = Intent(this, RecordVideoActivity::class.java)
            startActivity(intent)
        }

        // Botón para ver la lista de videos
        btnViewVideos.setOnClickListener {
            val intent = Intent(this, VideoListActivity::class.java)
            startActivity(intent)
        }

        // Botón para reproducir el último video
        btnPlayLastVideo.setOnClickListener {
            // Inicia la actividad LastVideoPlayerActivity para reproducir el último video
            val intent = Intent(this, LastVideoPlayerActivity::class.java)
            startActivity(intent)
        }
    }
}
