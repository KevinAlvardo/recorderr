package com.example.rocord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class VideoAdapter(
    private val videos: List<String>,
    private val onDelete: (String) -> Unit,
    private val onPlay: (String) -> Unit // Añadimos el segundo callback para reproducir
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoName: TextView = itemView.findViewById(R.id.tvVideoName)
        val deleteButton: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoPath = videos[position]
        holder.videoName.text = File(videoPath).name

        // Acción para eliminar el video
        holder.deleteButton.setOnClickListener {
            onDelete(videoPath)
        }

        // Acción para reproducir el video
        holder.itemView.setOnClickListener {
            onPlay(videoPath)
        }
    }

    override fun getItemCount(): Int {
        return videos.size
    }
}
