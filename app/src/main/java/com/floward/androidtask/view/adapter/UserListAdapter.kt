package com.floward.androidtask.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.floward.androidtask.data.response.model.UserAndTheirPostsData
import com.floward.androidtask.databinding.ItemUserBinding

class UserListAdapter(private val mUserItemClickListener: UserListItemClickListener) :
    ListAdapter<UserAndTheirPostsData, RecyclerView.ViewHolder>(Companion) {

    inner class UserListViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    class UserListItemClickListener(val userItemClickListener: (mItem: UserAndTheirPostsData) -> Unit) {
        fun onClick(mItem: UserAndTheirPostsData) = userItemClickListener(mItem)
    }

    companion object : DiffUtil.ItemCallback<UserAndTheirPostsData>() {
        override fun areItemsTheSame(
            oldItem: UserAndTheirPostsData,
            newItem: UserAndTheirPostsData
        ): Boolean = oldItem.userData.userId == newItem.userData.userId

        override fun areContentsTheSame(
            oldItem: UserAndTheirPostsData,
            newItem: UserAndTheirPostsData
        ): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(layoutInflater, parent, false)
        return UserListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, itemPosition: Int) {
        val mItem = getItem(itemPosition)
        with(holder as UserListViewHolder) {
            with(binding) {
                data = mItem
                userItemClickListener = mUserItemClickListener
                position = itemPosition
                executePendingBindings()
            }
        }
    }
}