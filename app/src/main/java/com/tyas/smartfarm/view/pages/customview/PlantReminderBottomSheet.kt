package com.tyas.smartfarm.view.pages.customview

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tyas.smartfarm.R
import com.tyas.smartfarm.ReminderReceiver
import java.util.*

class PlantReminderBottomSheet : BottomSheetDialogFragment() {

    private var selectedHour: Int = 0
    private var selectedMinute: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plant_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi NumberPicker untuk Jam dan Menit
        val numberPickerHour: NumberPicker = view.findViewById(R.id.numberPickerHour)
        val numberPickerMinute: NumberPicker = view.findViewById(R.id.numberPickerMinute)

        // Konfigurasi NumberPicker untuk Jam (0 - 23)
        numberPickerHour.minValue = 0
        numberPickerHour.maxValue = 23
        numberPickerHour.value = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        // Konfigurasi NumberPicker untuk Menit (0 - 59)
        numberPickerMinute.minValue = 0
        numberPickerMinute.maxValue = 59
        numberPickerMinute.value = Calendar.getInstance().get(Calendar.MINUTE)

        // Tangkap waktu yang dipilih user
        numberPickerHour.setOnValueChangedListener { _, _, newVal ->
            selectedHour = newVal
        }

        numberPickerMinute.setOnValueChangedListener { _, _, newVal ->
            selectedMinute = newVal
        }

        // Set tombol untuk menyimpan pengingat
        val buttonSetReminderTime: Button = view.findViewById(R.id.buttonSetReminderTime)
        buttonSetReminderTime.setOnClickListener {
            setReminder(selectedHour, selectedMinute)
            dismiss() // Tutup BottomSheet setelah pengingat diset
        }
    }

    private fun setReminder(hour: Int, minute: Int) {
        // Simpan waktu pengingat ke SharedPreferences
        saveReminderTime(hour, minute)

        // Mengatur pengingat 24 jam ke depan
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            // Jika waktu yang dipilih sudah lewat untuk hari ini, atur untuk hari berikutnya
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Buat intent untuk ReminderReceiver
        val intent = Intent(requireContext(), ReminderReceiver::class.java).apply {
            putExtra("notificationId", 1)
            putExtra("message", "Waktunya merawat tanaman!")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        Toast.makeText(
            requireContext(),
            "Pengingat diset untuk ${String.format("%02d:%02d", hour, minute)}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun saveReminderTime(hour: Int, minute: Int) {
        val sharedPreferences = requireContext().getSharedPreferences("PlantReminderPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("reminder_hour", hour)
            putInt("reminder_minute", minute)
            apply()
        }
    }

    private fun getReminderTime(): Pair<Int, Int> {
        val sharedPreferences = requireContext().getSharedPreferences("PlantReminderPrefs", Context.MODE_PRIVATE)
        val hour = sharedPreferences.getInt("reminder_hour", -1)
        val minute = sharedPreferences.getInt("reminder_minute", -1)
        return Pair(hour, minute)
    }
}
