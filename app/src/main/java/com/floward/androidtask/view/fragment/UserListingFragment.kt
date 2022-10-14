package com.floward.androidtask.view.fragment

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.floward.androidtask.data.response.model.UserData
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
    }

    private fun initRecyclerView() {
        mAdapter = UserListAdapter(onUserListItemClicked())
        binding.userListLayout.rvUserList.adapter = mAdapter
    }

    private fun onUserListItemClicked() = UserListAdapter.UserListItemClickListener{
        it.let {

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

    private fun manageUIState(viewNumber: Int, list: List<UserData>?) {
        binding.viewFlipper.displayedChild = viewNumber
        mAdapter.submitList(list)
    }

    private fun inflateViewWithUserList(eventState: UserViewModel.UserDataEvent.GetUserList) {
        hideLoading()
        if (eventState.list.isEmpty()) {
            manageUIState(viewNumber = 0, list = null)
        } else {
            manageUIState(viewNumber = 2, list = eventState.list)
        }
    }

    override fun observeViewModel(viewModel: UserViewModel) {
        observeRemote()

    }

    private fun observeRemote() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.usersTasksEvent.collect { eventState ->
                    when (eventState) {

                        is UserViewModel.UserDataEvent.Loading -> {
                            showLoading()
                        }

                        is UserViewModel.UserDataEvent.Error -> {
                            hideLoading()
                            showToast(requireContext(), eventState.errorData.error.toString())
                            manageUIState(viewNumber = 1, list = null)
                        }

                        UserViewModel.UserDataEvent.RemoteErrorByNetwork -> {
                            //try to load from local DB....
                            hideLoading()
                            showToast(requireContext(), "Checking from local data source")
                        }

                        is UserViewModel.UserDataEvent.GetUserList -> {
                            hideLoading()
                            if (eventState.list.isEmpty()) manageUIState(
                                viewNumber = 1,
                                list = null
                            )
                            else inflateViewWithUserList(eventState)
                        }
                    }
                }
            }
        }
    }

    private fun observeRemotePost() {
        viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.postsTasksEvent.collect { eventState ->
                when (eventState) {

                    is UserViewModel.PostDataEvent.Loading -> {
                        showLoading()
                    }

                    is UserViewModel.PostDataEvent.Error -> {
                        hideLoading()
                        showToast(requireContext(), eventState.errorData.error.toString())
                    }

                    UserViewModel.PostDataEvent.RemoteErrorByNetwork -> {
                        //try to load from local DB....
                        hideLoading()
                        showToast(requireContext(), "Checking from local data source")
                    }

                    is UserViewModel.PostDataEvent.GetPosts -> {
                        hideLoading()
                        showToast(requireContext(), "Data Loaded...${eventState.list}")
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
}
