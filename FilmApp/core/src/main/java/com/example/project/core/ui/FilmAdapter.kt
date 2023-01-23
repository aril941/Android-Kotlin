package com.example.project.core.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.project.core.R
import com.example.project.core.databinding.ItemCardListBinding
import com.example.project.core.domain.model.Film
import com.example.project.core.utils.DiffUtils

class FilmAdapter : RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    private var listData = ArrayList<Film>()
    var onItemClick: ((Film) -> Unit)? = null

    fun setData(newListData: List<Film>?) {
        if (newListData == null) return
        val diffCallback = DiffUtils(listData, newListData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listData.clear()
        listData.addAll(newListData)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getSwipedData(swipedPosition: Int): Film = listData[swipedPosition]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val itemCardListBinding =
            ItemCardListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(itemCardListBinding)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    inner class FilmViewHolder(private val binding: ItemCardListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(film: Film) {
            with(binding) {
                title.text = film.title
                date.text = film.releaseDate
                popularity.text =
                    itemView.context.getString(
                        R.string.popularity_d,
                        film.popularity.toString()
                    )
                Glide.with(itemView.context)
                    .load(itemView.context.getString(R.string.baseUrlImage, film.posterPath))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = GONE
                            return false
                        }
                    })
                    .into(poster)
            }
        }
        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}