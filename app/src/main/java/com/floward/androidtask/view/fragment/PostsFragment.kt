package com.floward.androidtask.view.fragment

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.floward.androidtask.data.response.model.UserAndTheirPostsData
import com.floward.androidtask.databinding.FragmentPostsBinding
import com.floward.androidtask.view.adapter.PostsAdapter
import com.floward.androidtask.view.base.BaseFragment
import com.floward.androidtask.view.viewmodel.UserViewModel

class PostsFragment : BaseFragment<FragmentPostsBinding, UserViewModel>(
    FragmentPostsBinding::inflate
) {
    private var data: UserAndTheirPostsData? = null
    private lateinit var mAdapter: PostsAdapter
    private lateinit var mLayoutManager: LinearLayoutManager

    override val viewModel: UserViewModel by activityViewModels()

    override fun initView(binding: FragmentPostsBinding, savedInstanceState: Bundle?) {
        initRecyclerView()
        initAdapter()
        arguments?.let {
            data = it.getParcelable("Data")
            binding.data = data
            data?.let { User ->
                mAdapter.submitList(User.userPostsData)
            }
        }
    }

    private fun initRecyclerView() {
        mAdapter = PostsAdapter(onPostItemClicked())
        binding.rvPostList.adapter = mAdapter
    }

    private fun onPostItemClicked() = PostsAdapter.PostsItemClickListener {
        it.let {

        }
    }

    private fun initAdapter() {
        mLayoutManager = LinearLayoutManager(requireContext())
        binding.rvPostList.layoutManager = mLayoutManager
        binding.rvPostList.adapter = mAdapter
    }

    override fun observeViewModel(viewModel: UserViewModel) {
    }


}