package com.floward.androidtask.view.fragment

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.floward.androidtask.view.base.BaseFragment
import com.floward.androidtask.databinding.FragmentUserListingBinding
import com.floward.androidtask.view.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListingFragment : BaseFragment<FragmentUserListingBinding, UserViewModel>(
    FragmentUserListingBinding::inflate
) {
    override val viewModel: UserViewModel by activityViewModels()

    override fun initView(binding: FragmentUserListingBinding, savedInstanceState: Bundle?) {

    }

    override fun observeViewModel(viewModel: UserViewModel) {
       
    }
}
