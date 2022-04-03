package com.example.githubuserdb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserdb.R
import com.example.githubuserdb.data.User
import com.example.githubuserdb.database.FavoriteUser
import com.example.githubuserdb.databinding.ItemFavoriteBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private val listUser = ArrayList<FavoriteUser>()
    private var onItemClickCallback: OnItemClickCallback? = null
    private lateinit var binding : ItemFavoriteBinding

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setList(users: ArrayList<FavoriteUser>){
        listUser.clear()
        listUser.addAll(users)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: FavoriteUser){
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(user)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .placeholder(R.drawable.ic_following)
                    .error(R.drawable.ic_broken_image)
                    .circleCrop()
                    .into(imageUser)
                tvName.text = user.name
                tvUsername.text = user.login
                tvCompany.text = user.company
                tvLocation.text = user.location
                repository.text = user.publicRepo
                tvFollower.text = user.followers
                tvFollowing.text = user.following
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.ViewHolder {
        val view = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.ViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback{
        fun onItemClicked(data: FavoriteUser)
    }
}