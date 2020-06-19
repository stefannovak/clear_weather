package com.stefanalexnovak.weatherapplication

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

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

                val gson = GsonBuilder().create()

                val weatherData = gson.fromJson(body, WeatherData::class.java)

                var locationText = weatherData.name + ", " + weatherData.sys.country
                val tempNumber = (weatherData.main.temp - 273.15).roundToInt()
                val time = getDateTime(weatherData.dt.toString())
                val weatherDescription = weatherData.weather[0].description

                cityText.text = locationText
                tempText.text = tempNumber.toString() + "c"
                hourlyTest.text = time.toString()
                weatherSummaryText.text = weatherDescription
            }
        })

    }

    private fun getDateTime(s: String): String? {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate =Date(s.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

}

data class WeatherData(val coord: Coord, val weather: List<Weather>, val base: String, val stations: String,
                  val main: Main, val visibility: Int, val wind: Wind,
                  val clouds: Clouds, val dt: Long, val sys : Sys, val timezone: Int,
                  val id : Long, val name: String, val cod: Int)
data class Coord(val lon: Double, val lat: Double)
data class Weather(val id: Int, val main: String, val description: String, val icon: String)
data class Main(val temp: Double, val feels_like: Double, val temp_min: Double, val temp_max: Double, val pressure: Double, val humidity: Double)
data class Wind(val speed: Double, val deg: Double)
data class Clouds(val all: Int)
data class Sys(val type : Int, val id: Int, val country: String, val sunrise : Int, val sunset: Int)

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