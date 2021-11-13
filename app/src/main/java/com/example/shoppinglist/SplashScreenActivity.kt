package com.example.shoppinglist

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

import android.os.Bundle
import java.lang.Thread.sleep


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sleep(3000)
        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        finish()
    }
}