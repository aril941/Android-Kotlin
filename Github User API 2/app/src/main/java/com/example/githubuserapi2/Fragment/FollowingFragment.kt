package com.example.githubuserapi2.Fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi2.Activity.DetailActivity
import com.example.githubuserapi2.Adapter.GithubAdapter
import com.example.githubuserapi2.Adapter.MainViewModel
import com.example.githubuserapi2.R
import com.example.githubuserapi2.databinding.FollowingFragmentBinding

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
        modelView = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
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