package com.ergea.moveflix.presentation.detail.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ergea.moveflix.databinding.ItemReviewBinding
import com.ergea.moveflix.model.Review
import com.ergea.moveflix.utils.commonImageUrl
import com.ergea.moveflix.utils.loadImage


class ReviewAdapter(private val itemClick: (Review) -> Unit) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {


    private var items: MutableList<Review> = mutableListOf()

    fun setItems(items: List<Review>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size


    class ReviewViewHolder(
        private val binding: ItemReviewBinding,
        val itemClick: (Review) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Review) {
            with(item) {
                itemView.setOnClickListener { itemClick(this) }
                binding.run {
                    tvMessage.text = content
                    tvUsernameReview.text = "@${authorDetails?.username.orEmpty()}"
                    tvNameReview.text = authorDetails?.name.orEmpty()
                    imgProfileReview.loadImage(
                        itemView.context,
                        authorDetails?.avatarPath?.commonImageUrl()
                    )
                    ratingBar.apply {
                        rating = authorDetails?.rating?.toFloat() ?: 0f
                    }
                }
            }

        }
    }

}