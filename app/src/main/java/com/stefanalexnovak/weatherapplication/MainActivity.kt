package com.stefanalexnovak.weatherapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private val locationRegex = "^.*\\/([^\\/]*)\$".toRegex()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val APIkey = "ee3cc93e43ef00b96a6bb4e56902d020"

        fetchJson()
    }

    private fun fetchJson() {

        val url = "https://api.openweathermap.org/data/2.5/onecall?lat=51.51&lon=-0.19&exclude=minutely&appid=ee3cc93e43ef00b96a6bb4e56902d020"

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

                /**
                 * ERROR: High/Low temps not registering.
                 * android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
                 * Sometimes works, sometimes doesn't???
                 */

                runOnUiThread {
                    //Fill out info from top to bottom.
                    //Top
                    cityText.text = getLocalLocation(weatherData.timezone)
                    weatherSummaryText.text = weatherData.current.weather[0].description
                    tempText.text = kelvinToCelsius(weatherData.current.temp).toString() + "°"

                    /////////////////////////Hourly
                    currentDayText.text = getDateDay(weatherData.current.dt)

                    nowTemp.text = kelvinToCelsius(weatherData.current.temp).toString()

                    oneTemp.text = kelvinToCelsius(weatherData.hourly[1].temp).toString()

                    twoTemp.text = kelvinToCelsius(weatherData.hourly[2].temp).toString()

                    threeTemp.text = kelvinToCelsius(weatherData.hourly[3].temp).toString()

                    fourTemp.text = kelvinToCelsius(weatherData.hourly[4].temp).toString()

                    fiveTemp.text = kelvinToCelsius(weatherData.hourly[5].temp).toString()

                    sixTemp.text = kelvinToCelsius(weatherData.hourly[6].temp).toString()

                    sevenTemp.text = kelvinToCelsius(weatherData.hourly[7].temp).toString()

                    eightTemp.text = kelvinToCelsius(weatherData.hourly[8].temp).toString()

                    nineTemp.text = kelvinToCelsius(weatherData.hourly[9].temp).toString()

                    tenTemp.text = kelvinToCelsius(weatherData.hourly[10].temp).toString()

                    elevenTemp.text = kelvinToCelsius(weatherData.hourly[11].temp).toString()

                    twelveTemp.text = kelvinToCelsius(weatherData.hourly[12].temp).toString()


                    /////////////////////////Daily
                    oneDay.text = getNextDay(currentDayText.text.toString())
                    oneWeather.text = weatherData.daily[1].weather[0].main
                    oneHighTemp.text = kelvinToCelsius(weatherData.daily[1].temp.max).toString()
                    oneLowTemp.text = kelvinToCelsius(weatherData.daily[1].temp.min).toString()

                    twoDay.text = getNextDay(oneDay.text.toString())
                    twoWeather.text = weatherData.daily[2].weather[0].main
                    twoHighTemp.text = kelvinToCelsius(weatherData.daily[2].temp.max).toString()
                    twoLowTemp.text = kelvinToCelsius(weatherData.daily[2].temp.min).toString()

                    threeDay.text = getNextDay(twoDay.text.toString())
                    threeWeather.text = weatherData.daily[3].weather[0].main
                    threeHighTemp.text = kelvinToCelsius(weatherData.daily[3].temp.max).toString()
                    threeLowTemp.text = kelvinToCelsius(weatherData.daily[3].temp.min).toString()

                    fourDay.text = getNextDay(threeDay.text.toString())
                    fourWeather.text = weatherData.daily[4].weather[0].main
                    fourHighTemp.text = kelvinToCelsius(weatherData.daily[4].temp.max).toString()
                    fourLowTemp.text = kelvinToCelsius(weatherData.daily[4].temp.min).toString()

                    fiveDay.text = getNextDay(fourDay.text.toString())
                    fiveWeather.text = weatherData.daily[5].weather[0].main
                    fiveHighTemp.text = kelvinToCelsius(weatherData.daily[5].temp.max).toString()
                    fiveLowTemp.text = kelvinToCelsius(weatherData.daily[5].temp.min).toString()

                    sixDay.text = getNextDay(fiveDay.text.toString())
                    sixWeather.text = weatherData.daily[6].weather[0].main
                    sixHighTemp.text = kelvinToCelsius(weatherData.daily[6].temp.max).toString()
                    sixLowTemp.text = kelvinToCelsius(weatherData.daily[6].temp.min).toString()

                    sevenDay.text = getNextDay(sixDay.text.toString())
                    sevenWeather.text = weatherData.daily[7].weather[0].main
                    sevenHighTemp.text = kelvinToCelsius(weatherData.daily[7].temp.max).toString()
                    sevenLowTemp.text = kelvinToCelsius(weatherData.daily[7].temp.min).toString()

                }
            }
        })


    }

    /**
     * Uses Regex to get the word after a "/", using the timezone info from WeatherData.
     */
    fun getLocalLocation(timezoneString: String) : String {
        return locationRegex.matchEntire(timezoneString)?.groups?.get(1)?.value.toString()
    }

    /**
     * Gets the day from the unix timestamp
     */
    private fun getDateDay(unixCode: Long): String? {
        try {
            val sdf = SimpleDateFormat("EEEE")
            val netDate = Date(unixCode.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    private fun getNextDay(day: String) : String {
        var nextDay = "Test"
        if(day == "Monday") {nextDay = "Tuesday"}
        else if(day == "Tuesday") {nextDay = "Wednesday"}
        else if(day == "Wednesday") {nextDay = "Thursday"}
        else if(day == "Thursday") {nextDay = "Friday"}
        else if(day == "Friday") {nextDay = "Saturday"}
        else if(day == "Saturday") {nextDay = "Sunday"}
        else if(day == "Sunday") {nextDay = "Monday"}

        return nextDay
    }

    /**
     * Converts celcius to fahrenheit, rounds it to a whole number
     */

    fun celsiusToFahrenheit(celcius: Int) : Int {
        return (celcius * 1.8 + 32).roundToInt()
    }

    /**
     * converts kelvin to celsius, rounded to a whole number
     */
    fun kelvinToCelsius(kelvin: Double) : Int {
        return (kelvin - 273.15).roundToInt()
    }
}

data class WeatherData(val lat: Double, val lon: Double, val timezone: String, val timezone_offset: Long,
                       val current: Current, val hourly: List<Hourly>, val daily: List<Daily>)

data class Current(val dt: Long, val sunrise: Long, val sunset: Long, val temp: Double, val feels_like: Double,
                   val uvi: Double, val clouds: Double, val visibility: Double, val wind_speed: Double,
                   val wind_deg: Int, val weather: List<Weather>)

data class Weather(val id: Long, val main: String, val description: String, val icon: String)

data class Hourly(val dt: Long, val temp: Double, val feels_like: Double, val pressure: Int, val humidity: Int, val dew_point: Double,
                  val clouds: Int, val wind_speed: Double, val wind_deg: Int, val weather: List<Weather>)

data class Daily(val dt: Long, val sunrise: Long, val sunset: Long, val temp: Temp, val feels_like: FeelsLike,
                 val pressure: Int, val humidity: Int, val dew_point: Double, val wind_speed: Double, val wind_deg: Int,
                 val weather: List<Weather>, val clouds: Int, val uvi: Double)

data class Temp(val day: Double, val min: Double, val max: Double, val night: Double, val eve: Double, val morn: Double)

data class FeelsLike(val day: Double, val night: Double, val eve: Double, val morn: Double)