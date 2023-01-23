package com.example.project.filmapp.detail

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.project.core.domain.model.Film
import com.example.project.filmapp.R
import com.example.project.filmapp.databinding.ActivityDetailBinding
import org.koin.android.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    companion object {
        const val _FILM = "extraFilm"
    }

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailFilm = intent.getParcelableExtra<Film>(_FILM)
        if (detailFilm != null) {
            populateDetail(detailFilm)
        }

        binding.backButton.setOnClickListener { onBackPressed() }
        binding.share.setOnClickListener { share() }
    }

    private fun populateDetail(film: Film) {
        with(binding) {
            titleDetail.text = film.title
            date.text = film.releaseDate
            overview.text = film.overview
            Glide.with(this@DetailActivity)
                .load(getString(R.string.baseUrlImage, film.posterPath))
                .into(posterTopBar)
            posterTopBar.tag = film.posterPath

            Glide.with(this@DetailActivity)
                .load(getString(R.string.baseUrlImage, film.posterPath))
                .into(subPoster)
            subPoster.tag = film.posterPath

            var favoriteState = film.favorite
            setFavoriteState(favoriteState)
            binding.favoriteButton.setOnClickListener {
                favoriteState = !favoriteState
                viewModel.setFavoriteMovie(film, favoriteState)
                Log.d("Button Cliked",favoriteState.toString())
                setFavoriteState(favoriteState)
            }
        }
    }

    private fun setFavoriteState(state: Boolean) {
        if (state) {
            binding.favoriteButton.setImageDrawable(
                ContextCompat.getDrawable(this.applicationContext, R.drawable.ic_favorite)
            )


        } else {
            binding.favoriteButton.setImageDrawable(
                ContextCompat.getDrawable(
                    this.applicationContext,
                    R.drawable.ic_favorite_border
                )
            )

        }
    }

    override fun onStop() {
        super.onStop()
        binding.posterTopBar.pause()
    }

    private fun share() {
        val formatType = "text/plain"
        ShareCompat.IntentBuilder.from(this).apply {
            setType(formatType)
            setChooserTitle(getString(R.string.shareTitle))
            setText(getString(R.string.shareBody))
            startChooser()
        }
    }
}