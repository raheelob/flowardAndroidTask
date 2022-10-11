package com.floward.androidtask.view.fragment

import android.os.Bundle
import com.floward.androidtask.view.base.BaseFragment
import com.floward.androidtask.databinding.FragmentUserListingBinding
import com.floward.androidtask.view.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListingFragment : BaseFragment<FragmentUserListingBinding, UserViewModel>(
    FragmentUserListingBinding::inflate
) {
    override val viewModel: UserViewModel
        get() = TODO("Not yet implemented")

    override fun initView(binding: FragmentUserListingBinding, savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun observeViewModel(viewModel: UserViewModel) {
        TODO("Not yet implemented")
    }
}
