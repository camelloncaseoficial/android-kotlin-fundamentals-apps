package com.camelloncase.marsrealestate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * Our MainActivity is only responsible for setting the content view that contains the
 * Navigation Host.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}