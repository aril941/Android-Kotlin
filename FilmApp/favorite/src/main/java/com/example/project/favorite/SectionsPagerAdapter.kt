package com.example.project.favorite

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.project.favorite.favorite.FavoriteFilmFragment

class SectionsPagerAdapter(private val mContext: Context,fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = intArrayOf(
        R.string.films
    )

    private val fragment: List<Fragment> = listOf(
        FavoriteFilmFragment(isFilm = true),
        FavoriteFilmFragment(isFilm = false),
    )

    override fun getCount() = tabTitles.size

    override fun getPageTitle(position: Int): CharSequence {
        return mContext.getString(tabTitles[position])
    }

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }
}