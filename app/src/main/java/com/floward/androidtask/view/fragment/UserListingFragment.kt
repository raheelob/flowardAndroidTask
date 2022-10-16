package com.floward.androidtask.view.fragment

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import com.floward.androidtask.data.events.PostDataEvent
import com.floward.androidtask.data.events.UserAndTheirPostEvent
import com.floward.androidtask.data.events.UserDataEvent
import com.floward.androidtask.data.response.model.UserAndTheirPostsData
import com.floward.androidtask.view.base.BaseFragment
import com.floward.androidtask.databinding.FragmentUserListingBinding
import com.floward.androidtask.utils.ProgressDialog
import com.floward.androidtask.utils.hideLoading
import com.floward.androidtask.utils.showDialog
import com.floward.androidtask.utils.showToast
import com.floward.androidtask.view.adapter.UserListAdapter
import com.floward.androidtask.view.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserListingFragment : BaseFragment<FragmentUserListingBinding, UserViewModel>(
    FragmentUserListingBinding::inflate
) {

    private lateinit var mAdapter: UserListAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mProgressDialog: ProgressDialog

    override val viewModel: UserViewModel by activityViewModels()

    override fun initView(binding: FragmentUserListingBinding, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        mProgressDialog = ProgressDialog(requireActivity())
        initRecyclerView()
        initAdapter()
        initSwipeToRefresh()
        if (viewModel.getConfig()) {
            viewModel.retrieveList()
            viewModel.handleConfig(false)
        }
    }

    private fun initRecyclerView() {
        mAdapter = UserListAdapter(onUserListItemClicked())
        binding.userListLayout.rvUserList.adapter = mAdapter
    }

    private fun onUserListItemClicked() = UserListAdapter.UserListItemClickListener {
        it.let { data ->
            viewModel.handleConfig(true)
            val direction = UserListingFragmentDirections.actionUserListingFragmentToPostsFragment(data)
            findNavController().navigate(direction)
        }
    }

    private fun initAdapter() {
        mLayoutManager = LinearLayoutManager(requireContext())
        binding.userListLayout.rvUserList.layoutManager = mLayoutManager
        binding.userListLayout.rvUserList.adapter = mAdapter
    }

    private fun initSwipeToRefresh() {
        binding.userListLayout.swipeToRefresh.setOnRefreshListener {
            viewModel.getUserList()
            binding.userListLayout.swipeToRefresh.isRefreshing = false
        }
    }

    private fun manageUIState(viewNumber: Int, list: List<UserAndTheirPostsData>?) {
        binding.viewFlipper.displayedChild = viewNumber
        mAdapter.submitList(list)
    }

    private fun inflateViewWithUserList(eventState: UserAndTheirPostEvent.UserWithTheirPosts) {
        hideLoading()
        if (eventState.list.isEmpty()) {
            manageUIState(viewNumber = 0, list = null)
        } else {
            manageUIState(viewNumber = 2, list = eventState.list)
            with(eventState.list){
                this.let {
                    if (this.isEmpty()) {
                        if (viewModel.getLastVisiblePosition() <= this.size) {
                            binding.userListLayout.rvUserList.smoothScrollToPosition(
                                viewModel.getLastVisiblePosition()
                            )
                        }
                    }
                }
            }
        }
    }

    override fun observeViewModel(viewModel: UserViewModel) {
        observeUserList()
        observePost()
        observeUserAndTheirPost()
    }

    private fun observeUserList() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.usersTasksEvent.collect { eventState ->
                    when (eventState) {

                        is UserDataEvent.Loading -> {
                            showLoading()
                        }

                        is UserDataEvent.Error -> {
                            hideLoading()
                            showToast(requireContext(), eventState.errorData.error.toString())
                            manageUIState(viewNumber = 1, list = null)
                        }

                        UserDataEvent.RemoteErrorByNetwork -> {
                            //try to load from local DB....
                            hideLoading()
                            showToast(requireContext(), "fetch from local data source")
                            viewModel.getUserAndTheirPosts()
                        }

                        is UserDataEvent.GetUserList -> {
                            hideLoading()
                            showToast(requireContext(), "Users Loaded...")
                        }
                    }
                }
            }
        }
    }

    private fun observePost() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.postsTasksEvent.collect { eventState ->
                    when (eventState) {

                        is PostDataEvent.Loading -> {
                            showLoading()
                        }

                        is PostDataEvent.Error -> {
                            hideLoading()
                            showToast(requireContext(), eventState.errorData.error.toString())
                        }

                        PostDataEvent.RemoteErrorByNetwork -> {
                            //try to load from local DB....
                            hideLoading()
                            showToast(requireContext(), "fetch from local data source")
                            viewModel.getUserAndTheirPosts()
                        }

                        is PostDataEvent.GetPosts -> {
                            hideLoading()
                            showToast(requireContext(), "Posts Loaded...")
                        }
                    }
                }
            }
        }
    }

    private fun observeUserAndTheirPost() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userAndTheirPostTasksEvent.collect { eventState ->
                    when (eventState) {
                        is UserAndTheirPostEvent.Loading -> {
                            showLoading()
                        }
                        is UserAndTheirPostEvent.Error -> {
                            hideLoading()
                            eventState.exception.message?.let { showToast(requireContext(), it) }
                            manageUIState(viewNumber = 1, list = null)
                        }
                        is UserAndTheirPostEvent.UserWithTheirPosts -> {
                            hideLoading()
                            if (eventState.list.isEmpty())
                                manageUIState(viewNumber = 1, list = null)
                            else
                                inflateViewWithUserList(eventState)
                        }
                    }
                }
            }
        }
    }

    private fun hideLoading() {
        hideLoading(mProgressDialog)
    }

    private fun showLoading() {
        showDialog(mProgressDialog)
    }

    override fun onDestroyView() {
        viewModel.setLastVisiblePosition(mLayoutManager.findLastCompletelyVisibleItemPosition())
        super.onDestroyView()
    }

    override fun onPause() {
        viewModel.setLastVisiblePosition(mLayoutManager.findLastCompletelyVisibleItemPosition())
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.handleConfig( true)
        super.onDestroy()
    }
}
