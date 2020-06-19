package com.stefanalexnovak.weatherapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val APIkey = "ee3cc93e43ef00b96a6bb4e56902d020"

        fetchJson()
    }

    private fun fetchJson() {
        val url =
            "https://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=ee3cc93e43ef00b96a6bb4e56902d020"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)
            }
        })

    }

}

//????clouds
data class WeatherData(val coords: Pair<Float, Float>, val weather: Weather, val main: Main,
                       val visibility: Int, val wind: Pair<Double, Double>,
                       val clouds: Int, val sysInfo : SysInfo, val timezone: Int,
                       val cityId : Int, val cityName: String)
data class Weather(val Id: Int, val mainWeather: String, val description: String, val weatherIcon: String)
data class Main(val temp: Double, val feelsLike: Double, val minTemp: Double, val maxTemp: Double, val pressure: Double, val humidity: Double)
data class SysInfo(val type : Int, val Id: Int, val country: String, val sunrise : Int, val sunset: Int)

//{
// "coord":{"lon":-0.13,"lat":51.51},
// "weather": [
// {
    // "id":803,
    // "main": "Clouds",
    // "description":"broken clouds",
    // "icon":"04d"
// }
// ],
// "base":"stations",
// "main":{
    // "temp":289.87,
    // "feels_like":285.38,
    // "temp_min":288.71,
    // "temp_max":290.93,
    // "pressure":1014,
    // "humidity":67
// },
// "visibility":10000,
// "wind":{
    // "speed":6.7,
    // "deg":230
// },
// "clouds":{"all":75},
// "dt":1592563468,
// "sys":{
    // "type":1,
    // "id":1414,
    // "country":"GB",
    // "sunrise":1592538170,
    // "sunset":1592598063
// },
// "timezone":3600,
// "id":2643743,
// "name":"London",
// "cod":200
// }