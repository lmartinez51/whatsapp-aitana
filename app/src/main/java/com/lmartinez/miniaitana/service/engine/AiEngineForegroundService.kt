package com.lmartinez.miniaitana.service.engine

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * PHASE 4 MIGRATION TARGET
 *
 * This stub satisfies the Manifest declaration so the project compiles cleanly.
 * Replace body with your existing AiEngineForegroundService.kt logic during Phase 4.
 *
 * Responsibilities (from spec):
 *  - Start as a FOREGROUND_SERVICE_TYPE_SPECIAL_USE foreground service
 *  - Acquire AiEngineLeaseManager lease before loading the .litertlm model
 *  - Host the LiteRT inference coroutine dispatcher
 *  - Release lease and unload model on stop/onTrimMemory(COMPLETE)
 */
@AndroidEntryPoint
class AiEngineForegroundService : Service() {

    companion object {
        const val CHANNEL_ID  = "aitana_engine_channel"
        const val NOTIF_ID    = 1001
        const val ACTION_START = "com.lmartinez.miniaitana.ACTION_START_ENGINE"
        const val ACTION_STOP  = "com.lmartinez.miniaitana.ACTION_STOP_ENGINE"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIF_ID, buildNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> stopSelf()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO Phase 4: AiEngineLeaseManager.releaseLease()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(com.lmartinez.miniaitana.R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_LOW,
        ).apply {
            description = getString(com.lmartinez.miniaitana.R.string.notification_channel_desc)
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(com.lmartinez.miniaitana.R.string.app_name))
            .setContentText("Auto-Pilot active")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .build()
}
