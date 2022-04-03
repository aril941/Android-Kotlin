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
import com.example.githubuserdb.databinding.FollowingFragmentBinding

class FollowingFragment: Fragment(R.layout.following_fragment) {
    private var mbinding: FollowingFragmentBinding? = null
    private val binding get() = mbinding!!
    private lateinit var modelView: MainViewModel
    private lateinit var adapter: GithubAdapter
    private lateinit var username : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailActivity.EXTRA_USERNAME).toString()
        mbinding = FollowingFragmentBinding.bind(view)

        adapter = GithubAdapter()
        adapter.notifyDataSetChanged()

        showLoading(true)
        modelView = ViewModelProvider(this).get(
            MainViewModel::class.java)
        modelView.setFollowing(username)
        modelView.get().observe(viewLifecycleOwner, {
            if (it!=null){
                adapter.setList(it)
                showLoading(false)
            }
        })

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(activity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
        }
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