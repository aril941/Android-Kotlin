package com.example.githubuserapi2.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuserapi2.databinding.SplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: SplashScreenBinding
    lateinit var handler: Handler
    var timer: Long =2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },timer)


    }
}