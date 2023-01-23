package com.example.project.core.utils

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.example.project.core.domain.model.Film

class DiffUtils(private val oldList: List<Film>, private val newList: List<Film>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        val (overview,
            releaseDate,
            popularity,
            _,
            title,
            posterPath,
            favorite) = oldList[oldPosition]

        val (overview1,
            releaseDate1,
            popularity1,
            _,
            title1,
            posterPath1,
            favorite1) = newList[newPosition]

        return (overview == overview1
                && releaseDate == releaseDate1
                && popularity == popularity1
                && title == title1
                && posterPath == posterPath1
                && favorite == favorite1)
    }

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }
}