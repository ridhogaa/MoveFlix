package com.ergea.moveflix.presentation.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ergea.moveflix.R
import com.ergea.moveflix.databinding.ActivityDetailBinding
import com.ergea.moveflix.databinding.FragmentHomeBinding
import com.ergea.moveflix.model.Movie
import com.ergea.moveflix.presentation.detail.adapter.ReviewAdapter
import com.ergea.moveflix.presentation.detail.adapter.TrailerAdapter
import com.ergea.moveflix.presentation.home.HomeViewModel
import com.ergea.moveflix.utils.commonImageUrl
import com.ergea.moveflix.utils.loadImage
import com.ergea.moveflix.utils.proceedWhen
import com.ergea.moveflix.utils.show
import com.ergea.moveflix.utils.showSnackBar
import com.ergea.moveflix.utils.toDateFormat
import com.ergea.moveflix.utils.transparentStatusBar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private val viewModel: DetailViewModel by viewModel()
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    private val movieId by lazy {
        intent.getIntExtra(EXTRA_MOVIE_ID, 0)
    }

    private val reviewAdapter: ReviewAdapter by lazy {
        ReviewAdapter {}
    }

    private val trailerAdapter: TrailerAdapter by lazy {
        TrailerAdapter {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarDetail.setNavigationOnClickListener {
            onBackPressed()
        }
        this.transparentStatusBar()
        fetchData()
        observeDataDetailMovie()
        setRecyclerViewReview()
        setRecyclerViewMovieVideo()
    }

    private fun fetchData() = with(viewModel) {
        getMovieById(movieId)
        getReviewsByMovieId(movieId)
        getMovieVideo(movieId)
    }

    private fun observeDataDetailMovie() = binding.run {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieResponse.collect {
                    it.proceedWhen(
                        doOnSuccess = { result ->
                            pbLoading.show(false)
                            result.payload?.let { movie -> bindToDetailView(movie) }
                        },
                        doOnLoading = {
                            pbLoading.show(true)
                        },
                        doOnError = { err ->
                            pbLoading.show(false)
                            binding.root.showSnackBar(err.message ?: "Check ur connection please..")
                        }
                    )
                }
            }
        }
    }

    private fun bindToDetailView(movie: Movie) = binding.run {
        with(movie) {
            collapsingToolbar.title = name
            imgMoviePoster.loadImage(this@DetailActivity, img.commonImageUrl())
            imgDetailBackground.loadImage(this@DetailActivity, img.commonImageUrl(), true)
            tvOverview.text = overview
            tvRatingCount.text = voteAverage.toString()
            tvRatersCount.text = voteCount.toString()
            tvMinOrEpisode.text = runtime.toString()
            tvReleaseDate.text = releaseDate.toDateFormat()
        }
    }

    private fun setRecyclerViewReview() {
        binding.rvReviews.apply {
            layoutManager =
                LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@DetailActivity.reviewAdapter
        }
        observeDataReview()
    }

    private fun observeDataReview() = binding.run {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reviewResponse.collectLatest {
                    it.proceedWhen(
                        doOnSuccess = { result ->
                            pbLoadingReview.show(false)
                            rvReviews.show(true)
                            tvEmptyReview.show(false)
                            result.payload?.let { data ->
                                reviewAdapter.setItems(data)
                            }
                        },
                        doOnLoading = {
                            pbLoadingReview.show(true)
                            rvReviews.show(false)
                            tvEmptyReview.show(false)
                        },
                        doOnError = { err ->
                            rvReviews.show(false)
                            pbLoadingReview.show(false)
                            tvEmptyReview.show(false)
                            binding.root.showSnackBar(err.message ?: "Check ur connection please..")
                        },
                        doOnEmpty = {
                            pbLoadingReview.show(false)
                            rvReviews.show(false)
                            tvEmptyReview.show(true)
                        }
                    )
                }
            }
        }
    }

    private fun setRecyclerViewMovieVideo() {
        binding.rvTrailer.apply {
            layoutManager =
                LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@DetailActivity.trailerAdapter
        }
        observeDataMovieVideo()
    }

    private fun observeDataMovieVideo() = binding.run {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieVideoResponse.collectLatest {
                    it.proceedWhen(
                        doOnSuccess = { result ->
                            pbLoadingTrailer.show(false)
                            rvTrailer.show(true)
                            result.payload?.let { data ->
                                trailerAdapter.setItems(data)
                            }
                        },
                        doOnLoading = {
                            pbLoadingTrailer.show(true)
                            rvTrailer.show(false)
                        },
                        doOnError = { err ->
                            rvTrailer.show(false)
                            pbLoadingTrailer.show(false)
                            binding.root.showSnackBar(err.message ?: "Check ur connection please..")
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val EXTRA_MOVIE_ID = "extra_movie_id"

        fun startActivity(
            context: Context,
            movieId: Int
        ) {
            Intent(context, DetailActivity::class.java).apply {
                putExtra(EXTRA_MOVIE_ID, movieId)
            }.run { context.startActivity(this) }
        }
    }
}