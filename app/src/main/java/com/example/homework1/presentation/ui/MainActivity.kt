package com.example.homework1.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.homework1.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, SearchingFragment.newInstance())
            .commit()

    }
}
