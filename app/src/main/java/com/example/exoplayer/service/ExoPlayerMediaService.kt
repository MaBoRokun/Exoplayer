package com.example.exoplayer.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.exoplayer.R
import com.example.exoplayer.model.Video
import com.example.exoplayer.resource.VideoCredentials.CHANNEL_ID
import com.example.exoplayer.ui.activities.ExoPlayerActivity
import com.example.exoplayer.viewmodel.RoomViewModel
import com.google.android.exoplayer2.MediaItem

class ExoPlayerMediaService : Service() {
    companion object {
        fun startService(context: Context, message: String, video: Video, position: Int) {
            val startIntent = Intent(context, ExoPlayerMediaService::class.java)
            startIntent.putExtra("position", position)
            startIntent.putExtra("player_status", message)
            startIntent.putExtra("title", video.title)
            startIntent.putExtra("subtitle", video.subtitle)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, ExoPlayerMediaService::class.java)
            context.stopService(stopIntent)
        }

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val position = intent.getIntExtra("position", 0)
        val status = intent.getStringExtra("player_status")
        val title = intent.getStringExtra("title")
        val subtitle = intent.getStringExtra("subtitle")

        val notificationClickIntent = Intent(this, ExoPlayerActivity::class.java)
        notificationClickIntent.putExtra("id", position)

        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            notificationClickIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //PlayBack next video action function
        val nextVideo = PendingIntent.getActivity(
            this,
            0,
            Intent(this, ExoPlayerActivity::class.java).putExtra("id", position + 1),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //PlayBack previous video action function
        val prevVideo = PendingIntent.getActivity(
            this,
            0,
            Intent(this, ExoPlayerActivity::class.java).putExtra("id", position - 1),
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        createNotificationChannel()

        val notification: Notification
        if (status == "true") {
            notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.exo_notification_small_icon)
                // Add media control buttons that invoke intents in your media service
                .addAction(R.drawable.exo_icon_previous, "Previous", null) // #0
                .addAction(R.drawable.exo_icon_pause, "Pause", null) // #1
                .addAction(R.drawable.exo_icon_next, "Next", null) // #2
                // Apply the media style template
                .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
                .setContentTitle(title)
                .setContentText(subtitle)
                .setContentIntent(contentIntent)
                .build()
            startForeground(1, notification)
        } else {
            notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.exo_notification_small_icon)
                // Add media control buttons that invoke intents in your media service
                .addAction(R.drawable.exo_icon_previous, "Previous", null) // #0
                .addAction(R.drawable.exo_icon_play, "Play", null) // #1
                .addAction(R.drawable.exo_icon_next, "Next", null) // #2
                // Apply the media style template
                .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
                .setContentTitle(title)
                .setContentText(subtitle)
                .setContentIntent(contentIntent)
                .build()
            startForeground(1, notification)
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}