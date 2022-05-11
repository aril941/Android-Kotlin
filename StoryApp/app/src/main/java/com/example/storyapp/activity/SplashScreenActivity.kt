package com.example.storyapp.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.databinding.ActivitySplashscreenBinding
import com.example.storyapp.preferences.UserPreference

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashscreenBinding
    private lateinit var handler: Handler
    private lateinit var userPref: SharedPreferences
    val timer: Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        userPref = getSharedPreferences(UserPreference.PREF_NAME, MODE_PRIVATE)
        val name = userPref.getString(UserPreference.NAME, null)
        if (name != null) {
            handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, timer)
        } else {
            handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, timer)
        }
    }
}