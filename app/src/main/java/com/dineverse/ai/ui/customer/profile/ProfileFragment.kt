package com.dineverse.ai.ui.customer.profile
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dineverse.ai.core.base.BaseFragment
import com.dineverse.ai.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val viewModel: ProfileViewModel by viewModels()
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }
}
