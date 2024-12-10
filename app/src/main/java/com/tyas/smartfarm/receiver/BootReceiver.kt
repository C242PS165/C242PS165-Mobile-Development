package com.tyas.smartfarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tyas.smartfarm.helper.AlarmManagerHelper

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Ambil waktu pengingat dari SharedPreferences
            val sharedPreferences = context.getSharedPreferences("PlantReminderPrefs", Context.MODE_PRIVATE)
            val hour = sharedPreferences.getInt("reminder_hour", -1)
            val minute = sharedPreferences.getInt("reminder_minute", -1)

            if (hour != -1 && minute != -1) {
                // Jadwalkan ulang pengingat
                val alarmManagerHelper = AlarmManagerHelper(context)
                alarmManagerHelper.scheduleReminder(hour, minute)
            }
        }
    }
}
