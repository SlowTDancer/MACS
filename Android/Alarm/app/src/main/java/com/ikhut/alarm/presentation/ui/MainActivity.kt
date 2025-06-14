package com.ikhut.alarm.presentation.ui

import android.Manifest
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.ikhut.alarm.R
import com.ikhut.alarm.data.AlarmDataStore
import com.ikhut.alarm.data.AlarmScheduler
import com.ikhut.alarm.data.ThemePreference
import com.ikhut.alarm.databinding.ActivityMainBinding
import com.ikhut.alarm.domain.model.Alarm
import com.ikhut.alarm.domain.repository.AlarmRepository
import com.ikhut.alarm.domain.repository.ThemeRepository
import com.ikhut.alarm.presentation.viewmodel.AlarmViewModel
import com.ikhut.alarm.presentation.viewmodel.ThemeViewModel
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var themeViewModel: ThemeViewModel
    private lateinit var adapter: AlarmAdapter
    private lateinit var alarmScheduler: AlarmScheduler

    private val exactAlarmPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmScheduler.canScheduleExactAlarms()) {
            Toast.makeText(this, "Exact alarm permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Exact alarm permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAlarmScheduler()
        setupViewModels()
        setupAdapters()
        addListeners()
        addObservers()
        requestPermissions()
    }

    private fun setupAlarmScheduler() {
        alarmScheduler = AlarmScheduler(this)
    }

    private fun setupViewModels() {
        val alarmDataStore = AlarmDataStore(applicationContext)
        val alarmRepository = AlarmRepository(alarmDataStore)

        alarmViewModel = ViewModelProvider(
            this, AlarmViewModel.create(alarmRepository)
        )[AlarmViewModel::class.java]

        val themePreferenceDataStore = ThemePreference(applicationContext)
        val themeRepository = ThemeRepository(themePreferenceDataStore)
        themeViewModel = ViewModelProvider(
            this, ThemeViewModel.create(themeRepository)
        )[ThemeViewModel::class.java]
    }

    private fun setupAdapters() {
        adapter = AlarmAdapter(onLongClick = { alarm -> showDeleteDialog(alarm) },
            onToggle = { updatedAlarm -> handleAlarmToggle(updatedAlarm) })
        binding.alarmsRecyclerView.adapter = adapter
    }

    private fun addListeners() {
        binding.addAlarmButton.setOnClickListener {
            showTimePicker()
        }

        binding.switchTheme.setOnClickListener {
            themeViewModel.toggleTheme()
        }
    }

    private fun addObservers() {
        alarmViewModel.alarms.observe(this) { alarms ->
            adapter.updateList(alarms)
        }

        themeViewModel.isDarkTheme.observe(this) { isDarkTheme ->
            applyTheme(isDarkTheme)
        }
    }

    private fun showTimePicker() {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)
        val totalCurrentMinutes = Alarm.toMinutes(hour, minute)

        val timePicker = TimePickerDialog(
            this, { _, selectedHour, selectedMinute ->
                val totalMinutes = Alarm.toMinutes(selectedHour, selectedMinute)
                if (totalCurrentMinutes >= totalMinutes) {
                    return@TimePickerDialog
                }

                val newAlarm = Alarm.fromHourMinute(selectedHour, selectedMinute)

                alarmViewModel.insertAlarm(newAlarm)
                scheduleAlarm(newAlarm)
            }, hour, minute, true
        )

        timePicker.show()
    }

    private fun handleAlarmToggle(updatedAlarm: Alarm) {
        alarmViewModel.updateAlarm(updatedAlarm)

        if (updatedAlarm.isOn) {
            scheduleAlarm(updatedAlarm)
        } else {
            cancelAlarm(updatedAlarm)
        }
    }

    private fun scheduleAlarm(alarm: Alarm) {
        if (checkAlarmPermissions()) {
            alarmScheduler.scheduleAlarm(alarm)
        } else {
            requestExactAlarmPermission()
        }
    }

    private fun cancelAlarm(alarm: Alarm) {
        alarmScheduler.cancelAlarm(alarm)
    }

    private fun showDeleteDialog(alarm: Alarm) {
        AlertDialog.Builder(this).setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Yes") { _, _ ->
                cancelAlarm(alarm)
                alarmViewModel.deleteAlarm(alarm)
            }.setNegativeButton("No", null).show()
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmScheduler.canScheduleExactAlarms()) {
                showExactAlarmPermissionDialog()
            }
        }
    }

    private fun checkAlarmPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmScheduler.canScheduleExactAlarms()
        } else {
            true
        }
    }

    private fun showExactAlarmPermissionDialog() {
        AlertDialog.Builder(this).setTitle("Exact Alarm Permission Required")
            .setMessage("To ensure your alarms trigger on time, please allow this app to schedule exact alarms in the next screen.")
            .setPositiveButton("Grant Permission") { _, _ ->
                requestExactAlarmPermission()
            }.setNegativeButton("Cancel", null).show()
    }

    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:$packageName")
            }
            exactAlarmPermissionLauncher.launch(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            NOTIFICATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun changeToDarkTheme(): Int {
        binding.switchTheme.text = binding.root.context.getString(R.string.switch_to_light)
        return AppCompatDelegate.MODE_NIGHT_YES
    }

    private fun changeToLightTheme(): Int {
        binding.switchTheme.text = binding.root.context.getString(R.string.switch_to_dark)
        return AppCompatDelegate.MODE_NIGHT_NO
    }

    private fun applyTheme(isDarkTheme: Boolean) {
        val defaultNightMode = if (isDarkTheme) {
            changeToDarkTheme()
        } else {
            changeToLightTheme()
        }

        if (AppCompatDelegate.getDefaultNightMode() != defaultNightMode) {
            AppCompatDelegate.setDefaultNightMode(defaultNightMode)
            recreate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }
}