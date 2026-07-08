package com.dineverse.ai.ui.auth.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dineverse.ai.core.base.BaseFragment
import com.dineverse.ai.core.common.UiState
import com.dineverse.ai.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentForgotPasswordBinding {
        return FragmentForgotPasswordBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeState()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSend.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            viewModel.forgotPassword(email)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.forgotPasswordState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.btnSend.isEnabled = false
                        }
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnSend.isEnabled = true
                            Toast.makeText(context, "Reset link sent to your email", Toast.LENGTH_LONG).show()
                            findNavController().popBackStack()
                        }
                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnSend.isEnabled = true
                            Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}
