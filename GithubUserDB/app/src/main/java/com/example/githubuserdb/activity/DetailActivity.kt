package com.example.githubuserdb.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuserdb.R
import com.example.githubuserdb.adapter.PagerAdapter
import com.example.githubuserdb.database.FavoriteUser
import com.example.githubuserdb.databinding.ActivityDetailBinding
import com.example.githubuserdb.preferences.SettingPreferences
import com.example.githubuserdb.view.*
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: MainViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var settingViewModel : SettingMainViewModel
    private lateinit var dataFavorit : FavoriteUser
    private lateinit var favModel : FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID,0)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        showLoading(true)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.setdetail(username.toString())
        viewModel.getDetail().observe(this) {
            if (it != null) {
                binding.apply {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvFollowers.text = it.followers.toString()
                    tvFollowing.text = it.following.toString()
                    tvRepository.text = it.publicRepo
                    tvCompany.text = if (it.company != null) it.company else " - "
                    tvLocation.text = if (it.location != null) it.location else " - "
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .circleCrop()
                        .into(imgItemReceived)
                    showLoading(false)
                }
            }
        }
        favModel = modelFavUser(this)
        var checkedFav =false
        CoroutineScope(Dispatchers.IO).launch {
            val favUser = favModel.userCheck(id)
            withContext(Dispatchers.Main){
                if(favUser!=null){
                    if(favUser>0){
                        binding.favButton.isChecked = true
                        checkedFav = true
                    }else{
                        binding.favButton.isChecked = false
                        checkedFav = false
                    }
                }
            }
        }
        binding.favButton.setOnClickListener {
            checkedFav =! checkedFav
            if (checkedFav){
                favModel.addFavUser(dataFavorit)
                Toast.makeText(applicationContext, "Successfully Added ${username} to Favorite!", Toast.LENGTH_SHORT).show()
            }else{
                favModel.deleteFavorite(id)
                Toast.makeText(applicationContext, "Successfully Deleted ${username} from Favorite!", Toast.LENGTH_SHORT).show()
            }
            binding.favButton.isChecked =checkedFav
        }

        viewModel.getDetail().observe(this) {
            if (it != null) {
                dataFavorit = FavoriteUser(
                    id,
                    it.login,
                    it.avatarUrl,
                    it.name,
                    it.location,
                    it.followers.toString(),
                    it.company,
                    it.following.toString(),
                    it.publicRepo
                )
            }
        }

        val pagerAdapter = PagerAdapter(this, bundle)
        binding.apply {
            viewPager.adapter = pagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()

            supportActionBar?.elevation = 0f
        }
        val pref = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))
            .get(SettingMainViewModel::class.java)
        darkMode()
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
    private fun modelFavUser(activity: AppCompatActivity): FavoriteViewModel {
        val factoryModel = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factoryModel).get(FavoriteViewModel::class.java)
    }


    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"
    }
}