package com.example.githubuserdb.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserdb.R
import com.example.githubuserdb.adapter.FavoriteAdapter
import com.example.githubuserdb.data.User
import com.example.githubuserdb.database.FavoriteUser
import com.example.githubuserdb.databinding.ActivityFavoriteUserBinding
import com.example.githubuserdb.preferences.SettingPreferences
import com.example.githubuserdb.view.FavoriteViewModel
import com.example.githubuserdb.view.FavoriteViewModelFactory
import com.example.githubuserdb.view.SettingMainViewModel
import com.example.githubuserdb.view.SettingViewModelFactory


class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFavoriteUserBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var settingViewModel : SettingMainViewModel
    private lateinit var favViewModel : FavoriteViewModel
    private val adapter: FavoriteAdapter by lazy {
        FavoriteAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.favorite_pg)
        favViewModel = modelFavUser(this)
        adapter.notifyDataSetChanged()

        adapter.setOnClickCallback(object : FavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(data: FavoriteUser) {
                intent = Intent(this@FavoriteUserActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                intent.putExtra(DetailActivity.EXTRA_ID, data.id)
                startActivity(intent)
            }
        })
        binding.apply {
            showLoading(true)
            rvUser.layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
        }
        favViewModel.getUserFavorite()?.observe(this) {
            if (it!=null){
                val userList = mapList(it)
                adapter.setList(userList)
                showLoading(false)
            }
        }



        val pref = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))
            .get(SettingMainViewModel::class.java)
        darkMode()
    }
    private fun modelFavUser(activity: AppCompatActivity): FavoriteViewModel {
        val factoryModel = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factoryModel).get(FavoriteViewModel::class.java)
    }

    private fun mapList(users: List<FavoriteUser>): ArrayList<FavoriteUser> {
        val listUser = ArrayList<FavoriteUser>()
        for (data in users) {
            val userMap = FavoriteUser(
                data.id,
                data.login,
                data.avatarUrl,
                data.name,
                data.location,
                data.followers,
                data.company,
                data.following,
                data.publicRepo)
            listUser.add(userMap)
        }
        return  listUser
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
    private fun showLoading(state : Boolean){
        if(state){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}