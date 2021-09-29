package com.example.exoplayer.ui.activities

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.exoplayer.R
import com.example.exoplayer.Resource
import com.example.exoplayer.databinding.ExoplayerActivityBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource

import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DataSource

import com.google.android.exoplayer2.upstream.DefaultHttpDataSource



class ExoPlayerActivity : AppCompatActivity(), Player.Listener {
    private lateinit var binding: ExoplayerActivityBinding
    private lateinit var MainPlayer: SimpleExoPlayer
    private lateinit var KEY_PLAYER_POSITION:String
    private lateinit var KEY_PLAYER_PLAY_WHEN_READY:String
    private lateinit var mNotificationManagerCompat: NotificationManagerCompat
    private var CHANNEL_ID:String = "channel1"
    private var CHANNEL_NAME_1:String = "FIRSTCHANNEL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ExoplayerActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initNotification()

        val Intent = intent
        val id = Intent.getStringExtra("id")

        MainPlayer = SimpleExoPlayer.Builder(this).build()

        binding.MainActivityPlayer.player = MainPlayer

        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        val data = Resource.createDataSet()
        if (id != null) {
            val mediaSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri
                    (data[id.toInt()].sources))
            MainPlayer.setMediaSource(mediaSource)
        }
        MainPlayer.prepare()
        MainPlayer.addListener(this)
        MainPlayer.play()
    }

    private fun initNotification(){
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME_1,
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean){
        val contentIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, ExoPlayerActivity::class.java),  PendingIntent.FLAG_CANCEL_CURRENT
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
        KEY_PLAYER_POSITION =  "pause"
        KEY_PLAYER_PLAY_WHEN_READY = "true"
        outState.putLong(KEY_PLAYER_POSITION, MainPlayer.contentPosition)
        outState.putBoolean(KEY_PLAYER_PLAY_WHEN_READY, MainPlayer.playWhenReady)
        MainPlayer.pause()
        Log.d("ExoPlayerState", MainPlayer.playbackState.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.let {
            MainPlayer.seekTo(it.getLong("pause"))
            MainPlayer.playWhenReady = it.getBoolean("true")
        }
        Log.d("ExoPlayerState", MainPlayer.playbackState.toString())
    }

}