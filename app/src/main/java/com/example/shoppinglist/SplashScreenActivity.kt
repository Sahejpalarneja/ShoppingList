package com.example.shoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

import android.os.Bundle




public class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        finish()
    }
}