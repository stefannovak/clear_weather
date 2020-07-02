package com.stefanalexnovak.weatherapplication.Data

import com.stefanalexnovak.weatherapplication.WeatherData

interface WeatherListener {

    fun onLocationReceived(data: WeatherData)
}