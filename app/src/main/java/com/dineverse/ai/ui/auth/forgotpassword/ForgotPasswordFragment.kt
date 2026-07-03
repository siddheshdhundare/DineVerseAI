package com.dineverse.ai.ui.auth.forgotpassword
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dineverse.ai.core.base.BaseFragment
import com.dineverse.ai.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {
    private val viewModel: ForgotPasswordViewModel by viewModels()
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentForgotPasswordBinding {
        return FragmentForgotPasswordBinding.inflate(inflater, container, false)
    }
}
