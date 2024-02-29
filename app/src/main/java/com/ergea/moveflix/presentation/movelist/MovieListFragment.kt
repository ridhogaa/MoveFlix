package com.ergea.moveflix.presentation.movelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.ergea.moveflix.databinding.FragmentMovieListBinding
import com.ergea.moveflix.presentation.detail.DetailActivity
import com.ergea.moveflix.presentation.movelist.adapter.MovieGenreAdapter
import com.ergea.moveflix.utils.proceedWhen
import com.ergea.moveflix.utils.show
import com.ergea.moveflix.utils.showSnackBar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieListFragment : Fragment() {

    private val viewModel: MovieListViewModel by viewModel()
    private var _binding: FragmentMovieListBinding? = null
    private val navArgs: MovieListFragmentArgs by navArgs()
    private val binding get() = _binding!!
    private val movieAdapter: MovieGenreAdapter by lazy {
        MovieGenreAdapter { data ->
            DetailActivity.startActivity(requireContext(), data.id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvGenre.text = navArgs.genreName
        fetchData()
        setRecyclerViewMovie()
    }

    private fun fetchData() = with(viewModel) {
        getMovieByGenre(navArgs.genreName)
    }

    private fun setRecyclerViewMovie() {
        binding.rvMovie.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@MovieListFragment.movieAdapter
        }
        setObserveDataMovie()
    }

    private fun setObserveDataMovie() = binding.run {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieResponse.collect {
                    it.proceedWhen(
                        doOnSuccess = { result ->
                            rvMovie.show(true)
                            pbLoading.show(false)
                            result.payload?.let { payload ->
                                movieAdapter.setItems(payload.shuffled())
                            }
                        },
                        doOnLoading = {
                            rvMovie.show(false)
                            pbLoading.show(true)
                        },
                        doOnError = { err ->
                            rvMovie.show(false)
                            pbLoading.show(false)
                            root.showSnackBar(err.message ?: "Check ur connection please..")
                        }
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}