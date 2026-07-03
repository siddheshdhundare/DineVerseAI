package com.dineverse.ai.ui.auth.signup
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dineverse.ai.core.base.BaseFragment
import com.dineverse.ai.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>() {
    private val viewModel: SignupViewModel by viewModels()
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSignupBinding {
        return FragmentSignupBinding.inflate(inflater, container, false)
    }
}
