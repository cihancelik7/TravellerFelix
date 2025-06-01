package com.example.travellerfelix.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.travellerfelix.MainActivity
import com.example.travellerfelix.R

class TomorrowPlanAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) {
            Log.e("TomorrowAlarmReceiver", "Context is null, cannot proceed.")
            return
        }

        Log.d("TomorrowAlarmReceiver", "Receiver triggered.")

        val channelId = "tomorrow_plan_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Tomorrow Plan Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d("TomorrowAlarmReceiver", "Notification channel created.")
        }

        val tapIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("SHOW_TOMORROW_PLAN", true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setContentTitle("Yarınki Planınız Hazır")
            .setContentText("Yarın için planladığınız programlara göz atın.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            NotificationManagerCompat.from(context).notify(2002, notification)
            Log.d("TomorrowAlarmReceiver", "Bildirim gönderildi.")
        } else {
            Log.w("TomorrowAlarmReceiver", "Bildirim izni verilmemiş.")
        }

        Toast.makeText(context, "Alarm tetiklendi", Toast.LENGTH_SHORT).show()
    }
}