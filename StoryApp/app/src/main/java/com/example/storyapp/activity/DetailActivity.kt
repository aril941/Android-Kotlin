package com.example.storyapp.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.preferences.SettingPreferences
import com.example.storyapp.preferences.UserPreference
import com.example.storyapp.view.MainViewModel
import com.example.storyapp.view.SettingMainViewModel
import com.example.storyapp.view.SettingViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
private val Context.dataTheme: DataStore<Preferences> by preferencesDataStore(name = "theme")

class DetailActivity : AppCompatActivity() {
    private lateinit var detail: ActivityDetailBinding
    private lateinit var settingViewModel: SettingMainViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userPref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detail = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detail.root)

        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        userPref = UserPreference(this)

        setDarkMode()
        setupData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setDarkMode() {
        val pref = SettingPreferences.getInstance(dataTheme)
        settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        ).get(SettingMainViewModel::class.java)
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setupData() {
        val id = intent.getStringExtra(ID)
        with(mainViewModel) {
            val authToken = userPref.getUser().token.toString()
            setAdapter(authToken)
            get().observe(this@DetailActivity, {
                if (it != null) {
                    for (stories in it) {
                        if (stories.id == id) {
                            detail.apply {
                                name.text = stories.name
                                desc.text = stories.description
                                Glide.with(this@DetailActivity)
                                    .load(stories.photoUrl)
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(photo)
                            }
                        }
                    }
                }
            })
        }
    }


    companion object {
        const val ID = "id"
    }

}