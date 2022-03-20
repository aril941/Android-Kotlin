package com.example.githubuserapi2.Activity

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.example.githubuserapi2.Adapter.MainViewModel
import com.example.githubuserapi2.Adapter.PagerAdapter
import com.example.githubuserapi2.R
import com.example.githubuserapi2.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: MainViewModel

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        showLoading(true)
        viewModel = ViewModelProvider(this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
        viewModel.setdetail(username.toString())
        viewModel.getDetail().observe(this, {
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
        })

        val PagerAdapter = PagerAdapter(this, bundle)
        binding.apply {
            viewPager.adapter = PagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()

            supportActionBar?.elevation = 0f
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