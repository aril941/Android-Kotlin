package com.example.githubuserapi2.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi2.Adapter.GithubAdapter
import com.example.githubuserapi2.Adapter.MainViewModel
import com.example.githubuserapi2.Data.User
import com.example.githubuserapi2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: GithubAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = GithubAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnClickCallback(object : GithubAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
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

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        viewModel.get().observe(this, {
            if (it!=null){
                adapter.setList(it)
                showLoading(false)
            }
        })
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
}