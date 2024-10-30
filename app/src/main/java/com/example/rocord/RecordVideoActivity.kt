package com.example.rocord

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordVideoActivity : AppCompatActivity() {

    private lateinit var viewFinder: PreviewView
    private var videoCapture: VideoCapture<Recorder>? = null
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private var activeRecording: androidx.camera.video.Recording? = null
    private var isRecording = false

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_video)

        viewFinder = findViewById(R.id.viewFinder)
        val btnRecord = findViewById<Button>(R.id.btnRecord)

        // Solicitar permisos antes de iniciar la cámara
        requestPermissions()

        btnRecord.setOnClickListener {
            if (videoCapture != null) {
                if (!isRecording) {
                    captureVideo() // Iniciar la grabación
                } else {
                    stopRecording() // Detener la grabación
                }
            }
        }
    }

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HD))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, videoCapture
                )
            } catch (exc: Exception) {
                Log.e("RecordVideoActivity", "Error al iniciar la cámara", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureVideo() {
        val videoCapture = this.videoCapture ?: return

        val videoFile = File(getOutputDirectory(), generateFileName())
        val outputOptions = FileOutputOptions.Builder(videoFile).build()

        // Iniciar la grabación
        activeRecording = videoCapture.output
            .prepareRecording(this, outputOptions)
            .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        isRecording = true
                        Toast.makeText(this, "Grabación iniciada", Toast.LENGTH_SHORT).show()
                    }
                    is VideoRecordEvent.Finalize -> {
                        isRecording = false
                        if (recordEvent.error == VideoRecordEvent.Finalize.ERROR_NONE) {
                            Toast.makeText(this, "Video guardado: ${videoFile.absolutePath}", Toast.LENGTH_SHORT).show()
                            Log.d("RecordVideoActivity", "Video guardado en: ${videoFile.absolutePath}")
                        } else {
                            Log.e("RecordVideoActivity", "Error al grabar el video: ${recordEvent.error}")
                            Toast.makeText(this, "Error al grabar el video", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    private fun stopRecording() {
        // Detener la grabación activa
        activeRecording?.stop()
        activeRecording = null // Liberar la referencia
        isRecording = false
        Toast.makeText(this, "Grabación detenida", Toast.LENGTH_SHORT).show()
    }

    // Función para obtener el directorio donde se guardarán los videos
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    // Genera un nombre único para cada video
    private fun generateFileName(): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        return "VIDEO_$timeStamp.mp4"
    }

    private fun requestPermissions() {
        if (allPermissionsGranted()) {
            startCamera() // Si los permisos ya fueron concedidos, iniciar la cámara
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Solicitar permisos de medios específicos en Android 13
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_MEDIA_VIDEO
                    ),
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                // Para versiones anteriores de Android
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }
    }


    private fun allPermissionsGranted(): Boolean {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                startCamera()
            } else {
                // Si no se conceden los permisos, muestra un mensaje y cierra la actividad
                Toast.makeText(this, "Permisos no concedidos", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
