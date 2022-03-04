package com.example.githubuserapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListGithubAdapter(private val listGithub:ArrayList<User>) : RecyclerView.Adapter<ListGithubAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivImage: ImageView = itemView.findViewById(R.id.imageUser)
        var tvName: TextView = itemView.findViewById(R.id.name)
        var tvUsername: TextView = itemView.findViewById(R.id.username)
        var tvJob: TextView = itemView.findViewById(R.id.work)
        var tvAddress: TextView = itemView.findViewById(R.id.location)
        var tvRepo: TextView = itemView.findViewById(R.id.repository)
        var tvFollower: TextView = itemView.findViewById(R.id.flwer)
        var tvFollowing: TextView = itemView.findViewById(R.id.flwing)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (Image, name, username, job, address, repository, follower, following) = listGithub[position]
        holder.ivImage.setImageResource(Image)
        holder.tvName.text = name
        holder.tvUsername.text = username
        holder.tvJob.text = job
        holder.tvAddress.text = address
        holder.tvRepo.text = repository
        holder.tvFollower.text = follower
        holder.tvFollowing.text = following
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listGithub[holder.adapterPosition])
        }
    }
    override fun getItemCount(): Int = listGithub.size
    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}

