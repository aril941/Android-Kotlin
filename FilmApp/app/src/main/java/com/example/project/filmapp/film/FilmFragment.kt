package com.example.project.filmapp.film

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.core.data.Resource
import com.example.project.core.domain.model.Film
import com.example.project.core.ui.FilmAdapter
import com.example.project.core.utils.SortUtils
import com.example.project.filmapp.R
import com.example.project.filmapp.databinding.FragmentFilmBinding
import com.example.project.filmapp.detail.DetailActivity
import com.example.project.filmapp.home.HomeActivity
import com.example.project.filmapp.home.SearchViewModel
import com.example.project.filmapp.util.DataStatus
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
@FlowPreview
class FilmFragment : Fragment(){

    private lateinit var fragmentFilmBinding: FragmentFilmBinding
    private val binding get() = fragmentFilmBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentFilmBinding = FragmentFilmBinding.inflate(inflater, container, false)
        val toolbar: Toolbar = activity?.findViewById<View>(R.id.toolbar) as Toolbar
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        searchView = (activity as HomeActivity).findViewById(R.id.search_view)
        return binding.root
    }

    private val viewModel: FilmViewModel by viewModel()
    private lateinit var filmAdapter: FilmAdapter
    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var searchView: MaterialSearchView
    private var sort = SortUtils.RANDOM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filmAdapter = FilmAdapter()
        setList(sort)
        observeSearchQuery()
        setSearchList()

        with(binding.rvMovies) {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu.findItem(R.id.action_search)
        searchView.setMenuItem(menuItem)
    }

    private fun setList(sort: String) {
        viewModel.getFilm(sort).observe(viewLifecycleOwner, filmObserver)
    }

    private val filmObserver = Observer<Resource<List<Film>>> { film ->
        if (film != null) {
            when (film) {
                is Resource.Loading -> setDataStatus(DataStatus.LOADING)
                is Resource.Success -> {
                    setDataStatus(DataStatus.SUCCESS)
                    filmAdapter.setData(film.data)
                }
                is Resource.Error -> {
                    setDataStatus(DataStatus.ERROR)
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeSearchQuery() {
        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchViewModel.setSearch(it) }
                return true
            }
        })
    }

    private fun setSearchList() {
        searchViewModel.movieResult.observe(this) { movies ->
            if (movies.isNullOrEmpty()) {
                setDataStatus(DataStatus.ERROR)
            } else {
                setDataStatus(DataStatus.SUCCESS)
            }
            filmAdapter.setData(movies)
        }
        searchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewShown() {
                setDataStatus(DataStatus.SUCCESS)
            }

            override fun onSearchViewClosed() {
                setDataStatus(DataStatus.SUCCESS)
                setList(sort)
            }
        })
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
        searchView.setOnQueryTextListener(null)
        searchView.setOnSearchViewListener(null)
        binding.rvMovies.adapter = null
        fragmentFilmBinding
    }
}