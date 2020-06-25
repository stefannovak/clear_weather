package com.stefanalexnovak.weatherapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private val locationRegex = "^.*\\/([^\\/]*)\$".toRegex()
//    private var lat: Double? = 0.0
//    private var lon: Double? = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        const val COARSE_REQUEST_CODE = 1
        const val FINE_REQUEST_CODE = 2
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        populateMap()

        fetchJson()

        getLocation()
    }

    private fun returnLocation() : Pair<Double, Double> {

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                val lat = location?.latitude
                val lon = location?.longitude
                println("THE LATITUDE IS: $lat\n\n THE LONGITUDE IS: $lon")
            }
        } else {
            if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) && shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this,"Location permission is required to get your local weather.", Toast.LENGTH_SHORT).show()

                println("WHYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY")
            } else {
                println("WHYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY AGAINNNNNNNNNNNNNNNNNNNNNN")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), COARSE_REQUEST_CODE)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_REQUEST_CODE)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == FINE_REQUEST_CODE && requestCode == COARSE_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    val lat = location?.latitude
                    val lon = location?.longitude
                    println("THE LATITUDE IS: $lat\n\n THE LONGITUDE IS: $lon")
                }
            } else {
                Toast.makeText(this, "Permission was not granted.", Toast.LENGTH_SHORT).show()
            }
        } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
    }

    private fun fetchJson() {

        val url =
            "https://api.openweathermap.org/data/2.5/onecall?lat=51.51&lon=-0.19&exclude=minutely&appid=ee3cc93e43ef00b96a6bb4e56902d020"
        val urlNewYork =
            "https://api.openweathermap.org/data/2.5/onecall?lat=40.785091&lon=-73.968285&exclude=minutely&appid=ee3cc93e43ef00b96a6bb4e56902d020"
        val urlLosAngeles =
            "https://api.openweathermap.org/data/2.5/onecall?lat=34.0522&lon=-118.24&exclude=minutely&appid=ee3cc93e43ef00b96a6bb4e56902d020"
        val urlLichfield =
            "https://api.openweathermap.org/data/2.5/onecall?lat=52.6816&lon=-1.8317&exclude=minutely&appid=ee3cc93e43ef00b96a6bb4e56902d020"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()

                val weatherData = gson.fromJson(body, WeatherData::class.java)

                val gcd = Geocoder(this@MainActivity, Locale.getDefault())
                val addresses = gcd.getFromLocation(weatherData.lat, weatherData.lon, 1)
                val city = addresses[0].subAdminArea



                runOnUiThread {

                    /**
                     * The celsius/fahrenheit switch
                     * I am 100% this is very redundant code. Could be improved.
                     * But for now it works.
                     */
                    temperatureSwitch.setOnClickListener {
                        if (temperatureSwitch.isChecked) {
                            tempText.text =
                                kelvinToFahrenheit(weatherData.current.temp).toString() + "°"

                            oneHighTemp.text =
                                kelvinToFahrenheit(weatherData.daily[1].temp.max).toString()
                            twoHighTemp.text =
                                kelvinToFahrenheit(weatherData.daily[2].temp.max).toString()
                            threeHighTemp.text =
                                kelvinToFahrenheit(weatherData.daily[3].temp.max).toString()
                            fourHighTemp.text =
                                kelvinToFahrenheit(weatherData.daily[4].temp.max).toString()
                            fiveHighTemp.text =
                                kelvinToFahrenheit(weatherData.daily[5].temp.max).toString()
                            sixHighTemp.text =
                                kelvinToFahrenheit(weatherData.daily[6].temp.max).toString()
                            sevenHighTemp.text =
                                kelvinToFahrenheit(weatherData.daily[7].temp.max).toString()

                            oneLowTemp.text =
                                kelvinToFahrenheit(weatherData.daily[1].temp.max).toString()
                            twoLowTemp.text =
                                kelvinToFahrenheit(weatherData.daily[2].temp.min).toString()
                            threeLowTemp.text =
                                kelvinToFahrenheit(weatherData.daily[3].temp.min).toString()
                            fourLowTemp.text =
                                kelvinToFahrenheit(weatherData.daily[4].temp.min).toString()
                            fiveLowTemp.text =
                                kelvinToFahrenheit(weatherData.daily[5].temp.min).toString()
                            sixLowTemp.text =
                                kelvinToFahrenheit(weatherData.daily[6].temp.min).toString()
                            sevenLowTemp.text =
                                kelvinToFahrenheit(weatherData.daily[7].temp.min).toString()

                            nowTemp.text = kelvinToFahrenheit(weatherData.current.temp).toString()
                            oneTemp.text = kelvinToFahrenheit(weatherData.hourly[1].temp).toString()
                            twoTemp.text = kelvinToFahrenheit(weatherData.hourly[2].temp).toString()
                            threeTemp.text =
                                kelvinToFahrenheit(weatherData.hourly[3].temp).toString()
                            fourTemp.text =
                                kelvinToFahrenheit(weatherData.hourly[4].temp).toString()
                            fiveTemp.text =
                                kelvinToFahrenheit(weatherData.hourly[5].temp).toString()
                            sixTemp.text = kelvinToFahrenheit(weatherData.hourly[6].temp).toString()
                            sevenTemp.text =
                                kelvinToFahrenheit(weatherData.hourly[7].temp).toString()
                            eightTemp.text =
                                kelvinToFahrenheit(weatherData.hourly[8].temp).toString()
                            nineTemp.text =
                                kelvinToFahrenheit(weatherData.hourly[9].temp).toString()
                            tenTemp.text =
                                kelvinToFahrenheit(weatherData.hourly[10].temp).toString()
                            elevenTemp.text =
                                kelvinToFahrenheit(weatherData.hourly[11].temp).toString()
                            twelveTemp.text =
                                kelvinToFahrenheit(weatherData.hourly[12].temp).toString()
                        } else {
                            tempText.text =
                                kelvinToCelsius(weatherData.current.temp).toString() + "°"

                            oneHighTemp.text =
                                kelvinToCelsius(weatherData.daily[1].temp.max).toString()
                            twoHighTemp.text =
                                kelvinToCelsius(weatherData.daily[2].temp.max).toString()
                            threeHighTemp.text =
                                kelvinToCelsius(weatherData.daily[3].temp.max).toString()
                            fourHighTemp.text =
                                kelvinToCelsius(weatherData.daily[4].temp.max).toString()
                            fiveHighTemp.text =
                                kelvinToCelsius(weatherData.daily[5].temp.max).toString()
                            sixHighTemp.text =
                                kelvinToCelsius(weatherData.daily[6].temp.max).toString()
                            sevenHighTemp.text =
                                kelvinToCelsius(weatherData.daily[7].temp.max).toString()

                            oneLowTemp.text =
                                kelvinToCelsius(weatherData.daily[1].temp.max).toString()
                            twoLowTemp.text =
                                kelvinToCelsius(weatherData.daily[2].temp.min).toString()
                            threeLowTemp.text =
                                kelvinToCelsius(weatherData.daily[3].temp.min).toString()
                            fourLowTemp.text =
                                kelvinToCelsius(weatherData.daily[4].temp.min).toString()
                            fiveLowTemp.text =
                                kelvinToCelsius(weatherData.daily[5].temp.min).toString()
                            sixLowTemp.text =
                                kelvinToCelsius(weatherData.daily[6].temp.min).toString()
                            sevenLowTemp.text =
                                kelvinToCelsius(weatherData.daily[7].temp.min).toString()

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
                            elevenTemp.text =
                                kelvinToCelsius(weatherData.hourly[11].temp).toString()
                            twelveTemp.text =
                                kelvinToCelsius(weatherData.hourly[12].temp).toString()
                        }
                    }

                    //Fill out info from top to bottom.
                    //Top
                    cityText.text = city
                    weatherSummaryText.text = weatherData.current.weather[0].description
                    tempText.text = kelvinToCelsius(weatherData.current.temp).toString() + "°"

                    /////////////////////////Hourly
                    currentDayText.text = getDateDay(weatherData.current.dt)

                    nowTemp.text = kelvinToCelsius(weatherData.current.temp).toString()
                    nowIcon.setImageResource(iconMap[weatherData.current.weather[0].icon]!!)

                    oneHour.text = getHourlyTime(weatherData.hourly[1].dt + 3600)
                    oneTemp.text = kelvinToCelsius(weatherData.hourly[1].temp).toString()
                    oneIcon.setImageResource(iconMap[weatherData.hourly[1].weather[0].icon]!!)

                    twoHour.text = getHourlyTime(weatherData.hourly[2].dt + 3600)
                    twoTemp.text = kelvinToCelsius(weatherData.hourly[2].temp).toString()
                    twoIcon.setImageResource(iconMap[weatherData.hourly[2].weather[0].icon]!!)

                    threeHour.text = getHourlyTime(weatherData.hourly[3].dt + 3600)
                    threeTemp.text = kelvinToCelsius(weatherData.hourly[3].temp).toString()
                    threeIcon.setImageResource(iconMap[weatherData.hourly[3].weather[0].icon]!!)

                    fourHour.text = getHourlyTime(weatherData.hourly[4].dt + 3600)
                    fourTemp.text = kelvinToCelsius(weatherData.hourly[4].temp).toString()
                    fourIcon.setImageResource(iconMap[weatherData.hourly[4].weather[0].icon]!!)

                    fiveHour.text = getHourlyTime(weatherData.hourly[5].dt + 3600)
                    fiveTemp.text = kelvinToCelsius(weatherData.hourly[5].temp).toString()
                    fiveIcon.setImageResource(iconMap[weatherData.hourly[5].weather[0].icon]!!)

                    sixHour.text = getHourlyTime(weatherData.hourly[6].dt + 3600)
                    sixTemp.text = kelvinToCelsius(weatherData.hourly[6].temp).toString()
                    sixIcon.setImageResource(iconMap[weatherData.hourly[6].weather[0].icon]!!)

                    sevenHour.text = getHourlyTime(weatherData.hourly[7].dt + 3600)
                    sevenTemp.text = kelvinToCelsius(weatherData.hourly[7].temp).toString()
                    sevenIcon.setImageResource(iconMap[weatherData.hourly[7].weather[0].icon]!!)

                    eightHour.text = getHourlyTime(weatherData.hourly[8].dt + 3600)
                    eightTemp.text = kelvinToCelsius(weatherData.hourly[8].temp).toString()
                    eightIcon.setImageResource(iconMap[weatherData.hourly[8].weather[0].icon]!!)

                    nineHour.text = getHourlyTime(weatherData.hourly[9].dt + 3600)
                    nineTemp.text = kelvinToCelsius(weatherData.hourly[9].temp).toString()
                    nineIcon.setImageResource(iconMap[weatherData.hourly[9].weather[0].icon]!!)

                    tenHour.text = getHourlyTime(weatherData.hourly[10].dt + 3600)
                    tenTemp.text = kelvinToCelsius(weatherData.hourly[10].temp).toString()
                    tenIcon.setImageResource(iconMap[weatherData.hourly[10].weather[0].icon]!!)

                    elevenHour.text = getHourlyTime(weatherData.hourly[11].dt + 3600)
                    elevenTemp.text = kelvinToCelsius(weatherData.hourly[11].temp).toString()
                    elevenIcon.setImageResource(iconMap[weatherData.hourly[11].weather[0].icon]!!)

                    twelveHour.text = getHourlyTime(weatherData.hourly[12].dt + 3600)
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


                } //end of UI thread
            } //end of onResponse()
        }) //end of newCall()


    } //end of fetchJson()

    /**
     * Converts 24 hour to 12 hour
     */
    private fun hourlyConvert(twentyFourHour: String): String {
        var hourlyTime = ""
        if (twentyFourHour == "00:00") {
            hourlyTime = "12am"
        } else if (twentyFourHour == "01:00") {
            hourlyTime = "1am"
        } else if (twentyFourHour == "02:00") {
            hourlyTime = "2am"
        } else if (twentyFourHour == "03:00") {
            hourlyTime = "3am"
        } else if (twentyFourHour == "04:00") {
            hourlyTime = "4am"
        } else if (twentyFourHour == "05:00") {
            hourlyTime = "5am"
        } else if (twentyFourHour == "06:00") {
            hourlyTime = "6am"
        } else if (twentyFourHour == "07:00") {
            hourlyTime = "7am"
        } else if (twentyFourHour == "08:00") {
            hourlyTime = "8am"
        } else if (twentyFourHour == "09:00") {
            hourlyTime = "9am"
        } else if (twentyFourHour == "10:00") {
            hourlyTime = "10am"
        } else if (twentyFourHour == "11:00") {
            hourlyTime = "11am"
        } else if (twentyFourHour == "12:00") {
            hourlyTime = "12am"
        } else if (twentyFourHour == "13:00") {
            hourlyTime = "1pm"
        } else if (twentyFourHour == "14:00") {
            hourlyTime = "2pm"
        } else if (twentyFourHour == "15:00") {
            hourlyTime = "3pm"
        } else if (twentyFourHour == "16:00") {
            hourlyTime = "4pm"
        } else if (twentyFourHour == "17:00") {
            hourlyTime = "5pm"
        } else if (twentyFourHour == "18:00") {
            hourlyTime = "6pm"
        } else if (twentyFourHour == "19:00") {
            hourlyTime = "7pm"
        } else if (twentyFourHour == "20:00") {
            hourlyTime = "8pm"
        } else if (twentyFourHour == "21:00") {
            hourlyTime = "9pm"
        } else if (twentyFourHour == "22:00") {
            hourlyTime = "10pm"
        } else if (twentyFourHour == "23:00") {
            hourlyTime = "11pm"
        }

        return hourlyTime
    }

    /**
     * I function I took from https://stackoverflow.com/questions/47804422/converting-epoch-time-to-the-hour
     * Converts the unix code into hourly figures.
     * I then use my own function to turn it from 24 hour to
     * am/pm for a better visual.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getHourlyTime(unixCode: Long): String? {
        val timeOfDay: LocalTime = Instant.ofEpochSecond(unixCode)
            .atOffset(ZoneOffset.UTC)
            .toLocalTime()
        val hourOfDay = timeOfDay.hour
        println(hourOfDay)

        return hourlyConvert(timeOfDay.toString())
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

    /**
     * Uses the current day to return the next days
     * for the weekly forecast
     */
    private fun getNextDay(day: String): String {
        var nextDay = "Test"
        if (day == "Monday") {
            nextDay = "Tuesday"
        } else if (day == "Tuesday") {
            nextDay = "Wednesday"
        } else if (day == "Wednesday") {
            nextDay = "Thursday"
        } else if (day == "Thursday") {
            nextDay = "Friday"
        } else if (day == "Friday") {
            nextDay = "Saturday"
        } else if (day == "Saturday") {
            nextDay = "Sunday"
        } else if (day == "Sunday") {
            nextDay = "Monday"
        }

        return nextDay
    }

    /**
     * Converts kelvin to fahrenheit, rounds it to a whole number
     */
    fun kelvinToFahrenheit(kelvin: Double): Int {
        return ((kelvin * 1.8) - 459.67).roundToInt()
    }

    /**
     * converts kelvin to celsius, rounded to a whole number
     */
    fun kelvinToCelsius(kelvin: Double): Int {
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