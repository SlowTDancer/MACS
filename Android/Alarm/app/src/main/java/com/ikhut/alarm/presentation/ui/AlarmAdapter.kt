package com.ikhut.alarm.presentation.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ikhut.alarm.databinding.AlarmItemBinding
import com.ikhut.alarm.domain.model.Alarm
import java.util.Calendar

class AlarmAdapter(
    private val onLongClick: (Alarm) -> Unit, private val onToggle: (Alarm) -> Unit
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {
    private val alarms: MutableList<Alarm> = mutableListOf()

    inner class AlarmViewHolder(private val binding: AlarmItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("DefaultLocale")
        fun bind(alarm: Alarm) {
            val formattedTime = alarm.getFormattedTime()

            binding.timeTextView.text = formattedTime
            binding.timeSwitch.setOnCheckedChangeListener(null)
            binding.timeSwitch.isChecked = alarm.isOn

            val hasTimePassed = isAlarmTimePassed(alarm)

            binding.timeSwitch.isEnabled = !(hasTimePassed && !alarm.isOn)

            binding.timeSwitch.alpha = if (hasTimePassed && !alarm.isOn) 0.5f else 1.0f
            binding.timeTextView.alpha = if (hasTimePassed && !alarm.isOn) 0.6f else 1.0f

            binding.timeSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && hasTimePassed) {
                    binding.timeSwitch.isChecked = false
                    return@setOnCheckedChangeListener
                }

                val updatedAlarm = alarm.copy(isOn = isChecked)
                onToggle(updatedAlarm)
            }

            binding.root.setOnLongClickListener {
                onLongClick(alarm)
                true
            }
        }

        private fun isAlarmTimePassed(alarm: Alarm): Boolean {
            val currentTime = Calendar.getInstance()
            val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val currentMinute = currentTime.get(Calendar.MINUTE)
            val currentTotalMinutes = Alarm.toMinutes(currentHour, currentMinute)

            return currentTotalMinutes >= alarm.minutes
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = AlarmItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarms[position]
        holder.bind(alarm)
    }

    override fun getItemCount(): Int = alarms.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newAlarms: List<Alarm>) {
        alarms.clear()
        alarms.addAll(newAlarms)
        notifyDataSetChanged()
    }
}