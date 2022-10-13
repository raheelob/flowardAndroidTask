package com.floward.androidtask.view.fragment

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.floward.androidtask.view.base.BaseFragment
import com.floward.androidtask.databinding.FragmentUserListingBinding
import com.floward.androidtask.utils.ProgressDialog
import com.floward.androidtask.utils.hideLoading
import com.floward.androidtask.utils.showDialog
import com.floward.androidtask.utils.showToast
import com.floward.androidtask.view.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserListingFragment : BaseFragment<FragmentUserListingBinding, UserViewModel>(
    FragmentUserListingBinding::inflate
) {
    private lateinit var mProgressDialog: ProgressDialog

    override val viewModel: UserViewModel by activityViewModels()

    override fun initView(binding: FragmentUserListingBinding, savedInstanceState: Bundle?) {
        mProgressDialog = ProgressDialog(requireActivity())
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
                        }

                        UserViewModel.UserDataEvent.RemoteErrorByNetwork -> {
                            //try to load from local DB....
                            hideLoading()
                            showToast(requireContext(), "Checking from local data source")
                        }

                        is UserViewModel.UserDataEvent.GetUserList -> {
                            hideLoading()
                            showToast(requireContext(), "Data Loaded...${eventState.list}")
                            observeRemotePost()
                            viewModel.getPosts()
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
