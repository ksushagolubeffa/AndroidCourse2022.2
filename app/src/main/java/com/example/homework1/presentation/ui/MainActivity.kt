package com.example.homework1.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.homework1.R
import com.example.homework1.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, SearchingFragment.newInstance())
            .commit()

    }
}
