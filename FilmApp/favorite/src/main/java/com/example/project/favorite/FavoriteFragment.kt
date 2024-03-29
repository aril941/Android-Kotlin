package com.example.project.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project.favorite.databinding.FragmentFavoriteBinding
import com.example.project.favorite.favorite.favoriteModule
import org.koin.core.context.loadKoinModules

class FavoriteFragment : Fragment() {

    private lateinit var fragmentFavoriteBinding: FragmentFavoriteBinding
    private val binding get() = fragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadKoinModules(favoriteModule)

        val sectionsPagerAdapter = SectionsPagerAdapter(context as Context, childFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tab.setupWithViewPager(binding.viewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentFavoriteBinding
        binding.viewPager.adapter
    }

}