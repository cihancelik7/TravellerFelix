package com.example.travellerfelix.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.*

object TomorrowPlanNotificationScheduler {

    fun scheduleNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.w("TomorrowPlan", "Exact alarm için izin yok. Alarm ayarlanmadı.")
                return
            }
        }

        val intent = Intent(context, TomorrowPlanAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.DATE, 0) // sadece bugünü test ediyoruz
            set(Calendar.HOUR_OF_DAY, 22)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val triggerTime = calendar.timeInMillis
        val now = System.currentTimeMillis()

        if (triggerTime <= now) {
            Log.w("TomorrowPlan", "Alarm geçmişe ayarlanmış: ${calendar.time}")
            return
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )

        Log.d("TomorrowPlan", "Alarm başarıyla ayarlandı: ${calendar.time}")
    }
}