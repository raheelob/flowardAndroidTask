package com.floward.androidtask.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.floward.androidtask.data.response.model.UserPostsData
import com.floward.androidtask.databinding.ItemPostsBinding

class PostsAdapter(private val mPostsItemClickListener: PostsItemClickListener) :
    ListAdapter<UserPostsData, RecyclerView.ViewHolder>(Companion) {

    inner class PostsViewHolder(val binding: ItemPostsBinding) :
        RecyclerView.ViewHolder(binding.root)

    class PostsItemClickListener(val userItemClickListener: (mItem: UserPostsData) -> Unit) {
        fun onClick(mItem: UserPostsData) = userItemClickListener(mItem)
    }

    companion object : DiffUtil.ItemCallback<UserPostsData>() {
        override fun areItemsTheSame(
            oldItem: UserPostsData,
            newItem: UserPostsData
        ): Boolean = oldItem.userId == newItem.userId

        override fun areContentsTheSame(
            oldItem: UserPostsData,
            newItem: UserPostsData
        ): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPostsBinding.inflate(layoutInflater, parent, false)
        return PostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, itemPosition: Int) {
        val mItem = getItem(itemPosition)
        with(holder as PostsViewHolder) {
            with(binding) {
                data = mItem
                postItemClickListener = mPostsItemClickListener
                position = itemPosition
                executePendingBindings()
            }
        }
    }
}