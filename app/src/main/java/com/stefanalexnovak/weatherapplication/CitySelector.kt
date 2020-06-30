package com.stefanalexnovak.weatherapplication

import android.R.attr.country
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.second_fragment.*


class CitySelector : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val cities = arrayOf("London", "Lichfield", "Los Angeles")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_fragment)

//        val spinner = findViewById<Spinner>(R.id.citySpinner)
        citySpinner.onItemSelectedListener = this

//        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        val arrayAdapter = ArrayAdapter.createFromResource(this, R.array.city_list, android.R.layout.simple_spinner_dropdown_item)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = arrayAdapter
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        println("Nothing is selected")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(getApplicationContext(),cities[position] , Toast.LENGTH_LONG).show();
    }

}