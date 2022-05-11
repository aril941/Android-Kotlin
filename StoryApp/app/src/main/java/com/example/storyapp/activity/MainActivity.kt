package com.example.storyapp.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.adapter.ListAdapter
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.preferences.SettingPreferences
import com.example.storyapp.preferences.UserPreference
import com.example.storyapp.view.MainViewModel
import com.example.storyapp.view.SettingMainViewModel
import com.example.storyapp.view.SettingViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
private val Context.dataTheme: DataStore<Preferences> by preferencesDataStore(name = "theme")

class MainActivity : AppCompatActivity() {
    private lateinit var main: ActivityMainBinding
    private lateinit var settingViewModel: SettingMainViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main = ActivityMainBinding.inflate(layoutInflater)
        setContentView(main.root)

        adapter = ListAdapter()
        adapter.notifyDataSetChanged()

        setupView()
        setDarkMode()
        setupAction()
        setupViewModel()

    }

    private fun setupAction() {
        main.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
            add.setOnClickListener {
                startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
            }
        }
    }

    private fun setupViewModel() {
        val userPref = UserPreference(this)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        with(mainViewModel) {
            val authToken = userPref.getUser().token.toString()
            setAdapter(authToken)
            showLoading(true)
            get().observe(this@MainActivity, {
                if (it != null) {
                    adapter.setList(it)
                    showLoading(false)
                }
            })
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
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

    private fun showLoading(state: Boolean) {
        if (state) {
            main.progressBar.visibility = View.VISIBLE
        } else {
            main.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val s = Intent(this, SettingsActivity::class.java)
                startActivity(s)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}