package com.example.rocord

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class PlayVideoActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)

        videoView = findViewById(R.id.videoView)

        // Obtener la ruta del video desde el intent
        val videoPath = intent.getStringExtra("videoPath")

        if (videoPath != null) {
            try {
                // Cargar el archivo de video desde la ruta y generar la URI segura
                val videoFile = File(videoPath)

                // Usar FileProvider para obtener la URI segura
                val videoUri: Uri = FileProvider.getUriForFile(
                    this,
                    applicationContext.packageName + ".fileprovider", // Autoridad del FileProvider
                    videoFile
                )

                // Configurar el VideoView para reproducir el video desde la URI
                videoView.setVideoURI(videoUri)

                // Agregar controles de reproducción
                val mediaController = MediaController(this)
                mediaController.setAnchorView(videoView)
                videoView.setMediaController(mediaController)

                // Iniciar la reproducción del video
                videoView.start()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error al cargar el video", Toast.LENGTH_SHORT).show()
            }
        }

        // Listener para manejar errores en la reproducción del video
        videoView.setOnErrorListener { _, _, _ ->
            Toast.makeText(this, "Error al reproducir el video", Toast.LENGTH_SHORT).show()
            true // Devuelve true para indicar que el error ha sido manejado
        }

        // Listener para detectar cuando el video ha finalizado
        videoView.setOnCompletionListener {
            Toast.makeText(this, "Reproducción completada", Toast.LENGTH_SHORT).show()
        }
    }

    // Opcional: Pausar la reproducción cuando la actividad entra en pausa
    override fun onPause() {
        super.onPause()
        if (videoView.isPlaying) {
            videoView.pause()
        }
    }

    // Opcional: Reanudar la reproducción cuando la actividad vuelve a ser visible
    override fun onResume() {
        super.onResume()
        videoView.start()
    }
}
