package com.ergea.moveflix.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ergea.moveflix.databinding.FragmentHomeBinding
import com.ergea.moveflix.presentation.home.adapter.GenreAdapter
import com.ergea.moveflix.utils.Rid
import com.ergea.moveflix.utils.proceedWhen
import com.ergea.moveflix.utils.show
import com.ergea.moveflix.utils.showSnackBar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val genreAdapter: GenreAdapter by lazy {
        GenreAdapter { data ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToMovieListFragment(
                    data.id.toString(),
                    data.name
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        setRecyclerViewGenre()
    }

    private fun fetchData() = with(viewModel) {
        getGenre()
    }

    private fun setRecyclerViewGenre() {
        binding.rvGenre.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@HomeFragment.genreAdapter
        }
        setObserveDataGenre()
    }

    private fun setObserveDataGenre() = binding.run {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.genreResponse.collect {
                    it.proceedWhen(
                        doOnSuccess = { result ->
                            rvGenre.show(true)
                            pbLoading.show(false)
                            result.payload?.let { payload ->
                                genreAdapter.setItems(payload)
                            }
                        },
                        doOnLoading = {
                            rvGenre.show(false)
                            pbLoading.show(true)
                        },
                        doOnError = { err ->
                            rvGenre.show(false)
                            pbLoading.show(false)
                            root.showSnackBar(err.exception?.message.orEmpty() ?: "Check ur connection please..")
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