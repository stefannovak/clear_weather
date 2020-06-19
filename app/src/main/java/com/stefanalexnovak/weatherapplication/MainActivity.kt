package com.stefanalexnovak.weatherapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val APIkey = "ee3cc93e43ef00b96a6bb4e56902d020"
    }
}