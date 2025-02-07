package com.example.videoplayer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var videoAdapter: VideoAdapter
    private val videoList = mutableListOf(
        VideoItem("Saved Video", Uri.parse("android.resource://com.example.videoplayer/" + R.raw.video1)),
        VideoItem("Saved Video", Uri.parse("android.resource://com.example.videoplayer/" + R.raw.puzzle)),
        VideoItem("Saved Video", Uri.parse("android.resource://com.example.videoplayer/" + R.raw.jelly_fish)),
        VideoItem("Saved Video", Uri.parse("android.resource://com.example.videoplayer/" + R.raw.video2)),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        val addButton = findViewById<Button>(R.id.addVideoButton)

        recyclerView.layoutManager = LinearLayoutManager(this)
        videoAdapter = VideoAdapter(videoList) { video ->
            val intent = Intent(this, VideoPlayerActivity::class.java)
            intent.putExtra("videoUri", video.videoUri.toString())
            startActivity(intent)
        }
        recyclerView.adapter = videoAdapter

        addButton.setOnClickListener {
            pickVideoFromGallery()
        }
    }

    private fun pickVideoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_VIDEO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_VIDEO && resultCode == Activity.RESULT_OK) {
            data?.data?.let { videoUri ->
                val newVideo = VideoItem("Saved Video", videoUri)
                videoList.add(newVideo)
                videoAdapter.notifyItemInserted(videoList.size - 1)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PICK_VIDEO = 1001
    }
}
