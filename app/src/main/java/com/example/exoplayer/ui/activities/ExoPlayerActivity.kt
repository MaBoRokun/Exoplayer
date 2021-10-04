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
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer


class ExoPlayerActivity : AppCompatActivity(), Player.Listener {
    private lateinit var binding: ExoplayerActivityBinding
    private lateinit var mainPlayer: SimpleExoPlayer
    private lateinit var mNotificationManagerCompat: NotificationManagerCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ExoplayerActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initNotification()

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

    private fun initNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME_1,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        val contentIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, ExoPlayerActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (isPlaying) {
            mNotificationManagerCompat = NotificationManagerCompat.from(this)
            val notification = this.let { it1 ->
                NotificationCompat.Builder(it1, CHANNEL_ID)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.drawable.exo_notification_small_icon)
                    // Add media control buttons that invoke intents in your media service
                    .addAction(R.drawable.exo_icon_previous, "Previous", null) // #0
                    .addAction(R.drawable.exo_icon_pause, "Pause", null) // #1
                    .addAction(R.drawable.exo_icon_next, "Next", null) // #2
                    // Apply the media style template
                    .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
                    .setContentTitle("Simple Title")
                    .setContentText("Simple text")
                    .setContentIntent(contentIntent)
                    .build()
            }
            mNotificationManagerCompat.notify(1, notification)
        } else {
            val notificationIntent = Intent(this, ExoPlayerActivity::class.java)
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            notificationIntent.action = Intent.ACTION_MAIN
            notificationIntent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            mNotificationManagerCompat = NotificationManagerCompat.from(this)
            val notification = this.let { it1 ->
                NotificationCompat.Builder(it1, CHANNEL_ID)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.drawable.exo_notification_small_icon)
                    // Add media control buttons that invoke intents in your media service
                    .addAction(R.drawable.exo_icon_previous, "Previous", null) // #0
                    .addAction(R.drawable.exo_icon_play, "Play", null) // #1
                    .addAction(R.drawable.exo_icon_next, "Next", null) // #2
                    // Apply the media style template
                    .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
                    .setContentTitle("Simple Title")
                    .setContentText("Simple text")
                    .setContentIntent(contentIntent)
                    .build()
            }
            mNotificationManagerCompat.notify(1, notification)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, VideoListActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mNotificationManagerCompat.cancelAll()
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