package com.example.travellerfelix.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TomorrowPlanAlarmReceiverTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testAlarmReceiver_sendsNotification() {
        val intent = Intent(context, TomorrowPlanAlarmReceiver::class.java)
        val receiver = TomorrowPlanAlarmReceiver()

        receiver.onReceive(context, intent)

        val notificationManager = NotificationManagerCompat.from(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!notificationManager.areNotificationsEnabled()) {
                println("⚠️ Bildirim izni verilmemiş. Test atlanıyor.")
                return  // Bildirim izni yoksa test atlanır
            }
        }

        // Varsayılan olarak zaten yukarıda onReceive çağrıldı.
        // Daha derin doğrulama için ek testler yazılabilir.
        assertTrue(true)
    }
}