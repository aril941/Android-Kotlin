package com.example.githubuserdb.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserdb.R
import com.example.githubuserdb.activity.DetailActivity
import com.example.githubuserdb.adapter.GithubAdapter
import com.example.githubuserdb.view.MainViewModel
import com.example.githubuserdb.databinding.FollowersFragmentBinding

class FollowerFragment : Fragment(R.layout.followers_fragment){
    private var mbinding : FollowersFragmentBinding? = null
    private val binding get() = mbinding!!
    private lateinit var modelView: MainViewModel
    private lateinit var adapter: GithubAdapter
    private lateinit var username : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailActivity.EXTRA_USERNAME).toString()
        mbinding = FollowersFragmentBinding.bind(view)

        adapter = GithubAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(activity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
        }

        showLoading(true)
        modelView = ViewModelProvider(this).get(MainViewModel::class.java)

        modelView.setFollowers(username)
        modelView.get().observe(viewLifecycleOwner, {
            if (it!=null){
                adapter.setList(it)
                showLoading(false)
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        mbinding = null
    }

    private fun showLoading(state : Boolean){
        if(state){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}