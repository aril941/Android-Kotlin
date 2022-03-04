package com.example.githubuserapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.title = "Detail Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val image : ImageView = findViewById(R.id.img_user)
        val name : TextView = findViewById(R.id.name_user)
        val username: TextView = findViewById(R.id.username)
        val userLoc : TextView = findViewById(R.id.user_loc)
        val userJob : TextView = findViewById(R.id.user_job)
        val repository : TextView = findViewById(R.id.repo)
        val followers : TextView = findViewById(R.id.follower)
        val following : TextView = findViewById(R.id.following)

        val data = intent.getParcelableExtra<User>("DATA")
        data?.Image?.let {image.setImageResource(it)}
        name.text = data?.name
        username.text = data?.username
        userLoc.text = data?.address
        userJob.text = data?.job
        repository.text = data?.repo
        following.text = data?.following
        followers.text = data?.follower
    }
}