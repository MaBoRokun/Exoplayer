package com.example.exoplayer.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.exoplayer.R
import com.example.exoplayer.resource.VideoCredentials.CHANNEL_ID
import com.example.exoplayer.ui.activities.ExoPlayerActivity

class ExoPlayerMediaService : Service() {
    companion object {
        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, ExoPlayerMediaService::class.java)
            startIntent.putExtra("player_status", message)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, ExoPlayerMediaService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val contentIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, ExoPlayerActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
        )
        //do heavy work on a background thread
        val status = intent?.getStringExtra("player_status")
        createNotificationChannel()
//        val notificationIntent = Intent(this, ExoPlayerActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            0, notificationIntent, 0
//        )
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
                .setContentTitle("Simple Title")
                .setContentText("Simple text")
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
                .setContentTitle("Simple Title")
                .setContentText("Simple text")
                .setContentIntent(contentIntent)
                .build()
            startForeground(1, notification)
        }
        //stopSelf();
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