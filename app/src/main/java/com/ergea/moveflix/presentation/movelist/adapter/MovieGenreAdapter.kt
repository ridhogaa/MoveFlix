package com.ergea.moveflix.presentation.movelist.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ergea.moveflix.databinding.ItemMovieBinding
import com.ergea.moveflix.model.Movie
import com.ergea.moveflix.utils.commonImageUrl
import com.ergea.moveflix.utils.loadImage


class MovieGenreAdapter(private val itemClick: (Movie) -> Unit) :
    RecyclerView.Adapter<MovieGenreAdapter.MovieGenreViewHolder>() {


    private var items: MutableList<Movie> = mutableListOf()

    fun setItems(items: List<Movie>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieGenreViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieGenreViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: MovieGenreViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size


    class MovieGenreViewHolder(
        private val binding: ItemMovieBinding,
        val itemClick: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Movie) {
            with(item) {
                itemView.setOnClickListener { itemClick(this) }
                binding.run {
                    tvTitle.text = name
                    imgMovie.loadImage(itemView.context, img.commonImageUrl())
                }
            }

        }
    }

}