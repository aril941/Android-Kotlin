package com.example.githubuserdb.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserdb.R
import com.example.githubuserdb.adapter.GithubAdapter
import com.example.githubuserdb.data.User
import com.example.githubuserdb.databinding.ActivityMainBinding
import com.example.githubuserdb.preferences.SettingPreferences
import com.example.githubuserdb.view.MainViewModel
import com.example.githubuserdb.view.SettingMainViewModel
import com.example.githubuserdb.view.SettingViewModelFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel : MainViewModel
    private val adapter: GithubAdapter by lazy {
        GithubAdapter()
    }
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var settingViewModel : SettingMainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter.notifyDataSetChanged()
        adapter.setOnClickCallback(object : GithubAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                intent.putExtra(DetailActivity.EXTRA_ID, data.id)
                intent.putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
                startActivity(intent)
            }

        })

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter

            btnSearch.setOnClickListener {
                search()
            }

            searchUsername.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    search()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.get().observe(this, {
            if (it!=null){
                adapter.setList(it)
                showLoading(false)
            }
        })

        val pref = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this,SettingViewModelFactory(pref))
            .get(SettingMainViewModel::class.java)
        darkMode()
    }

    private fun search(){
        binding.apply {
            val input = searchUsername.text.toString()
            if (input.isEmpty()) return
            showLoading(true)
            viewModel.setSearch(input)
        }
    }
    private fun showLoading(state : Boolean){
        if(state){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun darkMode(){
        settingViewModel.getThemeSettings().observe(this)
        { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
               val f = Intent(this,FavoriteUserActivity::class.java)
                startActivity(f)
                return true
            }
            R.id.settings -> {
                val i = Intent(this, SettingsActivity::class.java)
                startActivity(i)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}