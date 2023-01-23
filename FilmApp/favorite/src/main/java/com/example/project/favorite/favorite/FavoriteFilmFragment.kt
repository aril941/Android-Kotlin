package com.example.project.favorite.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.core.domain.model.Film
import com.example.project.core.ui.FilmAdapter
import com.example.project.core.utils.SortUtils
import com.example.project.favorite.R
import com.example.project.favorite.databinding.FragmentFavoriteFilmBinding
import com.example.project.favorite.utils.ItemSwipeHelper
import com.example.project.favorite.utils.OnItemSwiped
import com.example.project.filmapp.detail.DetailActivity
import com.example.project.filmapp.util.DataStatus
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteFilmFragment(private val isFilm: Boolean)  : Fragment(){

    private lateinit var favoriteFilmBinding: FragmentFavoriteFilmBinding
    private val binding get() = favoriteFilmBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favoriteFilmBinding =
            FragmentFavoriteFilmBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var filmAdapter: FilmAdapter
    private val viewModel: FavoriteViewModel by viewModel()
    private var sort = SortUtils.RANDOM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemTouchHelper.attachToRecyclerView(binding.rvFavoriteFilm)
        filmAdapter = FilmAdapter()
        setDataStatus(DataStatus.LOADING)
        setList(sort)

        with(binding.rvFavoriteFilm) {
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)
            this.adapter = filmAdapter
        }

        filmAdapter.onItemClick = { selectedData ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity._FILM, selectedData)
            startActivity(intent)
        }
    }



    private val itemTouchHelper = ItemSwipeHelper(object : OnItemSwiped {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder) {
            if (view != null) {
                val swipedPosition = viewHolder.adapterPosition
                val movie = filmAdapter.getSwipedData(swipedPosition)
                var state = movie.favorite
                viewModel.setFavorite(movie, !state)
                state = !state
                val snackBar =
                    Snackbar.make(view as View, R.string.message_undo, Snackbar.LENGTH_LONG)
                snackBar.setAction(R.string.message_ok) {
                    viewModel.run {
                        setFavorite(movie,!state) }
                }
                snackBar.show()
            }
        }
    })

    private val filmObserver = Observer<List<Film>> { film ->
        if (film.isNullOrEmpty()) {
            setDataStatus(DataStatus.ERROR)
        } else {
            setDataStatus(DataStatus.SUCCESS)
        }
        filmAdapter.setData(film)
    }

    private fun setDataStatus(state: DataStatus) {
        when (state) {
            DataStatus.ERROR -> {
                binding.progressBar.visibility = View.GONE
                binding.notFoundText.visibility = View.VISIBLE
            }
            DataStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.notFoundText.visibility = View.GONE
            }
            DataStatus.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                binding.notFoundText.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvFavoriteFilm.adapter
        favoriteFilmBinding
    }

    private fun setList(sort: String) {
        if (isFilm) {
            viewModel.getFavoriteFilm(sort).observe(viewLifecycleOwner, filmObserver)
        }
    }

}