package com.example.rocord

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class LastVideoPlayerActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var btnClose: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last_video_player)

        videoView = findViewById(R.id.videoView)
        btnClose = findViewById(R.id.btnClose)

        // Buscar el último video guardado y reproducirlo
        val lastVideoFile = getLastVideoFile()

        if (lastVideoFile != null) {
            try {
                // Usar FileProvider para obtener la URI segura
                val videoUri: Uri = FileProvider.getUriForFile(
                    this,
                    applicationContext.packageName + ".fileprovider", // Autoridad del FileProvider
                    lastVideoFile
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
        } else {
            Toast.makeText(this, "No hay videos disponibles", Toast.LENGTH_SHORT).show()
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

        // Configuración del botón para cerrar la actividad y regresar a la MainActivity
        btnClose.setOnClickListener {
            finish() // Regresa a la actividad anterior (MainActivity)
        }
    }

    // Función para obtener el último archivo de video guardado
    private fun getLastVideoFile(): File? {
        val videoDirectory = File("/storage/emulated/0/Android/media/com.example.rocord/Rocord")
        if (videoDirectory.exists()) {
            val videoFiles = videoDirectory.listFiles()?.sortedByDescending { it.lastModified() }
            if (!videoFiles.isNullOrEmpty()) {
                return videoFiles[0] // Devuelve el archivo de video más reciente
            }
        }
        return null
    }
}
