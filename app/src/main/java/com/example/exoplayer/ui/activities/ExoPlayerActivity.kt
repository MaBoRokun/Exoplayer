package com.example.exoplayer.ui.activities

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.exoplayer.R
import com.example.exoplayer.databinding.ExoplayerActivityBinding
import com.example.exoplayer.resource.VideoCredentials.CHANNEL_ID
import com.example.exoplayer.resource.VideoCredentials.CHANNEL_NAME_1
import com.example.exoplayer.service.NotificationService
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer


class ExoPlayerActivity : AppCompatActivity(), Player.Listener {
    private lateinit var binding: ExoplayerActivityBinding
    private lateinit var mainPlayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ExoplayerActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val activityIntent = intent
        val url = activityIntent.getStringExtra("url")

        mainPlayer = SimpleExoPlayer.Builder(this).build()

        binding.MainActivityPlayer.player = mainPlayer

//            videoViewModel =
//                ViewModelProvider(this, VideoViewModelFactory(VideoRepository(RetrofitService)))
//                    .get(
//                        VideoViewModel::class.java
//                    )
//            videoViewModel.getVideos()
//            videoViewModel.videoList.observe(this, Observer {
//                it.forEach {
//                    val item = MediaItem.fromUri(it.sources.joinToString(""))
//                    mainPlayer.addMediaItem(item)
//                }

        val item = url?.let { MediaItem.fromUri(it) }
        if (item != null) {
            mainPlayer.setMediaItem(item)
        }
        mainPlayer.prepare()
        mainPlayer.addListener(this)
        mainPlayer.play()

    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if (isPlaying) {
            NotificationService.startService(this, "true")
        } else {
            NotificationService.startService(this, "false")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, VideoListActivity::class.java)
        NotificationService.stopService(this)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        NotificationService.stopService(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mainPlayer.pause()
        outState.putLong("pause", mainPlayer.contentPosition)
        outState.putBoolean("true", mainPlayer.playWhenReady)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val timestamp: Long
        val isReady: Boolean
        savedInstanceState.let {
            timestamp = it.getLong("pause")
            isReady = it.getBoolean("true")
        }
        mainPlayer.seekTo(timestamp)
        mainPlayer.playWhenReady = isReady
    }

}