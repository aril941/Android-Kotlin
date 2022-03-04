package com.example.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var rvUser:RecyclerView
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvUser = findViewById(R.id.rv_user)
        rvUser.setHasFixedSize(true)

        list.addAll(userGithub)
        showRecyclerList()
    }

    private val userGithub: ArrayList<User>
    get() {
        val dataPhoto = resources.obtainTypedArray(R.array.avatar)
        val dataName = resources.getStringArray(R.array.name)
        val dataUsername = resources.getStringArray(R.array.username)
        val dataJob = resources.getStringArray(R.array.company)
        val dataAddress = resources.getStringArray(R.array.location)
        val dataRepository = resources.getStringArray(R.array.repository)
        val dataFollowers = resources.getStringArray(R.array.followers)
        val dataFollowing = resources.getStringArray(R.array.following)
        val listUser = ArrayList<User>()
        for (i in dataName.indices){
            val user = User(dataPhoto.getResourceId(i,-1),
                dataName[i],dataUsername[i],dataJob[i],dataAddress[i],
                dataRepository[i],dataFollowers[i],dataFollowing[i])
            listUser.add(user)
        }
        return listUser
    }
    private fun showRecyclerList(){
        rvUser.layoutManager = LinearLayoutManager(this)
        val listGithubAdapter = ListGithubAdapter(list)
        rvUser.adapter = listGithubAdapter

        listGithubAdapter.setOnItemClickCallback(object:ListGithubAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val i = Intent(this,DetailActivity::class.java)
        i.putExtra("DATA",user)
        startActivity(i)
    }

}