package com.ikhut.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.ikhut.weatherapp.databinding.ActivityMainBinding
import com.ikhut.weatherapp.databinding.TabItemLayoutBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabBinding = TabItemLayoutBinding.inflate(LayoutInflater.from(this))

            when (position) {
                0 -> tabBinding.tabIcon.setImageResource(R.drawable.today)
                1 -> tabBinding.tabIcon.setImageResource(R.drawable.hourly)
            }

            tab.customView = tabBinding.root
        }.attach()

    }
}


class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val fragments = listOf(DailyWeatherFragment(), WeatherForecastFragment())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
