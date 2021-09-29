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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.exoplayer.DAO.VideoDatabase
import com.example.exoplayer.R
import com.example.exoplayer.databinding.ExoplayerActivityBinding
import com.example.exoplayer.model.Video
import com.example.exoplayer.network.RetrofitService
import com.example.exoplayer.repository.VidRepository
import com.example.exoplayer.viewmodel.VidViewModel
import com.example.exoplayer.viewmodel.VidViewModelFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource

import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DataSource

import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import android.R.string
import com.google.android.exoplayer2.C


class ExoPlayerActivity : AppCompatActivity(), Player.Listener {
    private lateinit var binding: ExoplayerActivityBinding
    private lateinit var MainPlayer: SimpleExoPlayer
    private lateinit var KEY_PLAYER_POSITION: String
    private lateinit var KEY_PLAYER_PLAY_WHEN_READY: String
    private lateinit var mNotificationManagerCompat: NotificationManagerCompat
    lateinit var vidViewModel: VidViewModel
    private var CHANNEL_ID: String = "channel1"
    private var CHANNEL_NAME_1: String = "FIRSTCHANNEL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ExoplayerActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initNotification()

        val Intent = intent
        val id = Intent.getIntExtra("id",0)
        MainPlayer = SimpleExoPlayer.Builder(this).build()

        binding.MainActivityPlayer.player = MainPlayer


        vidViewModel =
            ViewModelProvider(this, VidViewModelFactory(VidRepository(RetrofitService)))
                .get(
                    VidViewModel::class.java
                )
        vidViewModel.getVideos()
        vidViewModel.videoList.observe(this, Observer {
//                it.forEach {
//                    val item = MediaItem.fromUri(it.sources.joinToString(""))
//                    MainPlayer.addMediaItem(item)
//                }
            val item = MediaItem.fromUri(it[id].sources.joinToString(""))
            MainPlayer.setMediaItem(item)
        })
        MainPlayer.prepare()
        MainPlayer.addListener(this)
        MainPlayer.play()

        getDataFromDBbyId()
    }

    fun getDataFromDBbyId() {
        CoroutineScope(Dispatchers.Default).launch {
            val db = Room.databaseBuilder(
                applicationContext,
                VideoDatabase::class.java, "database-name"
            ).fallbackToDestructiveMigration()
                .build()
            val db_data = db.videoDao()
            val Videos: List<Video> = db_data!!.getAll()
            Log.d("Room",Videos.toString())
        }
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
            Intent(this, ExoPlayerActivity::class.java), PendingIntent.FLAG_CANCEL_CURRENT
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
        KEY_PLAYER_POSITION = "pause"
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