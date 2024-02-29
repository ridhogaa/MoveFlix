package com.ergea.moveflix.presentation.detail.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.RecyclerView
import com.ergea.moveflix.databinding.ItemTrailerBinding
import com.ergea.moveflix.model.MovieVideo
import com.ergea.moveflix.utils.commonYoutubeUrl


class TrailerAdapter(private val itemClick: (MovieVideo) -> Unit) :
    RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>() {


    private var items: MutableList<MovieVideo> = mutableListOf()

    fun setItems(items: List<MovieVideo>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val binding = ItemTrailerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrailerViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size


    class TrailerViewHolder(
        private val binding: ItemTrailerBinding,
        val itemClick: (MovieVideo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: MovieVideo) {
            with(item) {
                itemView.setOnClickListener { itemClick(this) }
                binding.wvTrailer.apply {
                    settings.javaScriptEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    webChromeClient = WebChromeClient()
                    loadUrl(key.commonYoutubeUrl())
                }
            }

        }
    }

}