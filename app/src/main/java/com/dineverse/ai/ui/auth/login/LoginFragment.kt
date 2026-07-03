package com.dineverse.ai.ui.auth.login
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dineverse.ai.core.base.BaseFragment
import com.dineverse.ai.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }
}
