package com.ergea.moveflix.presentation.home.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ergea.moveflix.databinding.ItemGenreBinding
import com.ergea.moveflix.model.Genre


class GenreAdapter(private val itemClick: (Genre) -> Unit) :
    RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    private var items: MutableList<Genre> = mutableListOf()

    fun setItems(items: List<Genre>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size


    class GenreViewHolder(private val binding: ItemGenreBinding, val itemClick: (Genre) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Genre) {
            with(item) {
                itemView.setOnClickListener { itemClick(this) }
                binding.run {
                    tvGenre.text = name
                }
            }

        }
    }

}