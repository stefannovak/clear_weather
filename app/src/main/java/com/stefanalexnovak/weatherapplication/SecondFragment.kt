package com.stefanalexnovak.weatherapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.second_fragment.*

class SecondFragment : Fragment() {

    companion object {
        const val ARG_POSITION = "position"

        fun getInstance(position: Int) : Fragment {
            val weatherFragment = SecondFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_POSITION, position)
            weatherFragment.arguments = bundle
            return weatherFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.second_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val position = requireArguments().getInt(ARG_POSITION)

        textView.text = "AYYYYYYYYYYYYYYYYYYYYYYYYYYYY $position"
    }
}