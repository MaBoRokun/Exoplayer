package com.example.exoplayer.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.exoplayer.databinding.ExoplayerActivityBinding
import com.example.exoplayer.model.Video
import com.example.exoplayer.service.ExoPlayerMediaService
import com.example.exoplayer.viewmodel.RoomViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer


class ExoPlayerActivity : AppCompatActivity(), Player.Listener {
    private lateinit var binding: ExoplayerActivityBinding
    private lateinit var mainPlayer: SimpleExoPlayer
    private lateinit var roomViewModel: RoomViewModel
    private lateinit var videoList:List<Video>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ExoplayerActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val id = intent.getIntExtra("id",0)
        val list:MutableList<MediaItem> = arrayListOf()


        mainPlayer = SimpleExoPlayer.Builder(this).build()

        binding.MainActivityPlayer.player = mainPlayer

        roomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)
        roomViewModel.getAllRecords()
        roomViewModel.DBLocalData.observe(this, {
            it.forEach {
                val item = MediaItem.fromUri(it.sources.joinToString(""))
                list.add(item)
            }
            mainPlayer.setMediaItems(list)
            mainPlayer.seekTo(id,0)
            mainPlayer.prepare()
            mainPlayer.addListener(this)
            mainPlayer.play()
            videoList=it
        })
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        val video:Video
        if (isPlaying) {
            video=videoList[mainPlayer.currentWindowIndex]
            video.let {
                ExoPlayerMediaService.startService(this, "true",
                    it,mainPlayer.currentWindowIndex)
            }
        } else {
            video=videoList[mainPlayer.currentWindowIndex]
            video.let {
                ExoPlayerMediaService.startService(this, "false",
                    it,mainPlayer.currentWindowIndex)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, VideoListActivity::class.java)
        ExoPlayerMediaService.stopService(this)
        startActivity(intent)

    }
    override fun onStop() {
        super.onStop()
        mainPlayer.stop()
    }
    override fun onDestroy() {
        super.onDestroy()
        ExoPlayerMediaService.stopService(this)
        mainPlayer.stop()
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