package com.tyas.smartfarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Ambil data yang dikirimkan melalui intent
        val notificationId = intent.getIntExtra("notificationId", 0)
        val message = intent.getStringExtra("message") ?: "Waktunya merawat tanaman!"

        // Tampilkan notifikasi
        val notification = NotificationCompat.Builder(context, "PLANT_REMINDER_CHANNEL")
            .setSmallIcon(R.drawable.ic_lay_plant)
            .setContentTitle("Pengingat Tanaman")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notification)
        }

        // Tampilkan Toast (opsional)
        Toast.makeText(context, "Pengingat aktif: $message", Toast.LENGTH_SHORT).show()
    }
}
