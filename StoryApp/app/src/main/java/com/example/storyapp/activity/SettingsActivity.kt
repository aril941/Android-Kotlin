package com.example.storyapp.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivitySettingsBinding
import com.example.storyapp.preferences.SettingPreferences
import com.example.storyapp.preferences.UserPreference
import com.example.storyapp.view.SettingMainViewModel
import com.example.storyapp.view.SettingViewModelFactory

private val android.content.Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
private val android.content.Context.dataTheme: DataStore<Preferences> by preferencesDataStore(name = "theme")

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setDarkTheme()
        setLanguage()
        setLogout()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setDarkTheme() {
        val pref = SettingPreferences.getInstance(dataTheme)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))
            .get(SettingMainViewModel::class.java)
        settingViewModel.getThemeSettings().observe(this)
        { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun setLanguage() {
        binding.changeLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun setLogout() {
        binding.btnLogout.setOnClickListener {
            val logout = UserPreference(this)
            logout.deleteUser()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}