package com.example.rocord

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

import android.widget.Button

class VideoListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var videoList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)

        // Inicializamos el RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Cargamos los videos guardados en una lista
        videoList = getSavedVideos().toMutableList()

        // Inicializamos el Adapter pasándole la lista de videos y las funciones para eliminar y reproducir videos
        videoAdapter = VideoAdapter(videoList, { videoPath ->
            // Acción para eliminar el video, pero ahora con confirmación
            showDeleteConfirmationDialog(videoPath)
        }, { videoPath ->
            // Acción para reproducir el video
            val intent = Intent(this, PlayVideoActivity::class.java)
            intent.putExtra("videoPath", videoPath)  // Pasamos la ruta del video a la actividad de reproducción
            startActivity(intent)
        })

        // Asignamos el Adapter al RecyclerView
        recyclerView.adapter = videoAdapter

        // Configuramos el botón de actualización
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        btnUpdate.setOnClickListener {
            updateVideoList() // Actualiza la lista cuando se presiona el botón
            Toast.makeText(this, "Lista actualizada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        updateVideoList() // Actualiza la lista cada vez que la actividad se reanuda
    }

    // Función para obtener los videos guardados
    private fun getSavedVideos(): List<String> {
        val videoDirectory = File("/storage/emulated/0/Android/media/com.example.rocord/Rocord")
        if (videoDirectory.exists()) {
            val videoFiles = videoDirectory.listFiles()?.map { it.absolutePath }?.toList() ?: emptyList()
            Log.d("VideoListActivity", "Videos encontrados: $videoFiles")
            return videoFiles
        } else {
            Log.d("VideoListActivity", "El directorio de videos no existe")
            return emptyList()
        }
    }

    // Función para actualizar la lista de videos y notificar al Adapter
    private fun updateVideoList() {
        videoList.clear()
        videoList.addAll(getSavedVideos())
        videoAdapter.notifyDataSetChanged()
    }

    // Mostrar cuadro de confirmación para eliminar el video
    private fun showDeleteConfirmationDialog(videoPath: String) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Eliminar video")
        builder.setMessage("¿Estás seguro de que deseas eliminar este video?")

        builder.setPositiveButton("Eliminar") { _, _ ->
            val file = File(videoPath)
            if (file.exists()) {
                file.delete()
                Toast.makeText(this, "Video eliminado", Toast.LENGTH_SHORT).show()
                updateVideoList() // Actualiza la lista después de eliminar
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss() // Cierra el diálogo sin hacer nada
        }

        builder.create().show() // Muestra el cuadro de diálogo
    }
}
