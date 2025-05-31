package com.example.travellerfelix.service

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.travellerfelix.MainActivity
import com.example.travellerfelix.R

class TomorrowPlanAlarmReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val tapIntent = Intent(context,MainActivity::class.java).apply {
            putExtra("SHOW_TOMORROW_PLAN",true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = context?.let {
            NotificationCompat.Builder(it,"tomorrow_plan_channel")
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle("Yarınki Planınız Hazır..")
                .setContentText("Yarın için planladığınız programlara göz atın.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
        }

        context?.let { ctx ->
            if (ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                val manager = NotificationManagerCompat.from(ctx)
                if (notification != null) {
                    manager.notify(2002, notification)
                }
            }
        }

    }
}