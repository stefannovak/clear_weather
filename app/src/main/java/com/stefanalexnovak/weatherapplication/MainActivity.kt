package com.stefanalexnovak.weatherapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private val locationRegex = "^.*\\/([^\\/]*)\$".toRegex()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val APIkey = "ee3cc93e43ef00b96a6bb4e56902d020"

        fetchJson()

        populateMap()
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
                    nowIcon.setImageResource(iconMap[weatherData.hourly[0].weather[0].icon]!!)

                    oneTemp.text = kelvinToCelsius(weatherData.hourly[1].temp).toString()
                    oneIcon.setImageResource(iconMap[weatherData.hourly[1].weather[0].icon]!!)

                    twoTemp.text = kelvinToCelsius(weatherData.hourly[2].temp).toString()
                    twoIcon.setImageResource(iconMap[weatherData.hourly[2].weather[0].icon]!!)

                    threeTemp.text = kelvinToCelsius(weatherData.hourly[3].temp).toString()
                    threeIcon.setImageResource(iconMap[weatherData.hourly[3].weather[0].icon]!!)

                    fourTemp.text = kelvinToCelsius(weatherData.hourly[4].temp).toString()
                    fourIcon.setImageResource(iconMap[weatherData.hourly[4].weather[0].icon]!!)

                    fiveTemp.text = kelvinToCelsius(weatherData.hourly[5].temp).toString()
                    fiveIcon.setImageResource(iconMap[weatherData.hourly[5].weather[0].icon]!!)

                    sixTemp.text = kelvinToCelsius(weatherData.hourly[6].temp).toString()
                    sixIcon.setImageResource(iconMap[weatherData.hourly[6].weather[0].icon]!!)

                    sevenTemp.text = kelvinToCelsius(weatherData.hourly[7].temp).toString()
                    sevenIcon.setImageResource(iconMap[weatherData.hourly[7].weather[0].icon]!!)

                    eightTemp.text = kelvinToCelsius(weatherData.hourly[8].temp).toString()
                    eightIcon.setImageResource(iconMap[weatherData.hourly[8].weather[0].icon]!!)

                    nineTemp.text = kelvinToCelsius(weatherData.hourly[9].temp).toString()
                    nineIcon.setImageResource(iconMap[weatherData.hourly[9].weather[0].icon]!!)

                    tenTemp.text = kelvinToCelsius(weatherData.hourly[10].temp).toString()
                    tenIcon.setImageResource(iconMap[weatherData.hourly[10].weather[0].icon]!!)

                    elevenTemp.text = kelvinToCelsius(weatherData.hourly[11].temp).toString()
                    elevenIcon.setImageResource(iconMap[weatherData.hourly[11].weather[0].icon]!!)

                    twelveTemp.text = kelvinToCelsius(weatherData.hourly[12].temp).toString()
                    twelveIcon.setImageResource(iconMap[weatherData.hourly[12].weather[0].icon]!!)


                    /////////////////////////Daily
                    oneDay.text = getNextDay(currentDayText.text.toString())
                    dayOneIcon.setImageResource(iconMap[weatherData.daily[1].weather[0].icon]!!)
                    oneHighTemp.text = kelvinToCelsius(weatherData.daily[1].temp.max).toString()
                    oneLowTemp.text = kelvinToCelsius(weatherData.daily[1].temp.min).toString()

                    twoDay.text = getNextDay(oneDay.text.toString())
                    dayTwoIcon.setImageResource(iconMap[weatherData.daily[2].weather[0].icon]!!)
                    twoHighTemp.text = kelvinToCelsius(weatherData.daily[2].temp.max).toString()
                    twoLowTemp.text = kelvinToCelsius(weatherData.daily[2].temp.min).toString()

                    threeDay.text = getNextDay(twoDay.text.toString())
                    dayThreeIcon.setImageResource(iconMap[weatherData.daily[3].weather[0].icon]!!)
                    threeHighTemp.text = kelvinToCelsius(weatherData.daily[3].temp.max).toString()
                    threeLowTemp.text = kelvinToCelsius(weatherData.daily[3].temp.min).toString()

                    fourDay.text = getNextDay(threeDay.text.toString())
                    dayFourIcon.setImageResource(iconMap[weatherData.daily[4].weather[0].icon]!!)
                    fourHighTemp.text = kelvinToCelsius(weatherData.daily[4].temp.max).toString()
                    fourLowTemp.text = kelvinToCelsius(weatherData.daily[4].temp.min).toString()

                    fiveDay.text = getNextDay(fourDay.text.toString())
                    dayFiveIcon.setImageResource(iconMap[weatherData.daily[5].weather[0].icon]!!)
                    fiveHighTemp.text = kelvinToCelsius(weatherData.daily[5].temp.max).toString()
                    fiveLowTemp.text = kelvinToCelsius(weatherData.daily[5].temp.min).toString()

                    sixDay.text = getNextDay(fiveDay.text.toString())
                    daySixIcon.setImageResource(iconMap[weatherData.daily[6].weather[0].icon]!!)
                    sixHighTemp.text = kelvinToCelsius(weatherData.daily[6].temp.max).toString()
                    sixLowTemp.text = kelvinToCelsius(weatherData.daily[6].temp.min).toString()

                    sevenDay.text = getNextDay(sixDay.text.toString())
                    daySevenIcon.setImageResource(iconMap[weatherData.daily[7].weather[0].icon]!!)
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

    //Icon map
    var iconMap = HashMap<String, Int>()
    private fun populateMap() {
        iconMap["01d"] = R.drawable.a01d
        iconMap["01n"] = R.drawable.a01n
        iconMap["02d"] = R.drawable.a02d
        iconMap["02n"] = R.drawable.a02n
        iconMap["03d"] = R.drawable.a03d
        iconMap["03n"] = R.drawable.a03n
        iconMap["04d"] = R.drawable.a04d
        iconMap["04n"] = R.drawable.a04n
        iconMap["09d"] = R.drawable.a09d
        iconMap["09n"] = R.drawable.a09n
        iconMap["10d"] = R.drawable.a10d
        iconMap["10n"] = R.drawable.a10n
        iconMap["11d"] = R.drawable.a11d
        iconMap["11n"] = R.drawable.a11n
        iconMap["13d"] = R.drawable.a13d
        iconMap["13n"] = R.drawable.a13n
        iconMap["50d"] = R.drawable.a50d
        iconMap["50n"] = R.drawable.a50n
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