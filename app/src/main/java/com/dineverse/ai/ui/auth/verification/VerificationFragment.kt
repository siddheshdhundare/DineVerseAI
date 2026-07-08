package com.dineverse.ai.ui.auth.verification

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
import com.dineverse.ai.R
import com.dineverse.ai.core.base.BaseFragment
import com.dineverse.ai.core.common.UiState
import com.dineverse.ai.databinding.FragmentVerificationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class VerificationFragment : BaseFragment<FragmentVerificationBinding>() {

    private val viewModel: VerificationViewModel by viewModels()
    private var email: String = ""

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentVerificationBinding {
        return FragmentVerificationBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments?.getString("email") ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeState()
    }

    private fun setupUI() {
        binding.tvMessage.text = getString(R.string.verification_desc, email)

        binding.btnRefresh.setOnClickListener {
            viewModel.checkVerificationStatus()
        }

        binding.btnResend.setOnClickListener {
            if (email.isNotEmpty()) {
                viewModel.resendEmail(email)
            } else {
                Toast.makeText(context, "Email address not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.verificationState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.btnRefresh.isEnabled = false
                        }
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnRefresh.isEnabled = true
                            if (state.data) {
                                Timber.d("Email verified, navigating to home")
                                findNavController().navigate(R.id.action_verificationFragment_to_homeFragment)
                            } else {
                                Toast.makeText(context, "Email still not verified", Toast.LENGTH_SHORT).show()
                            }
                        }
                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnRefresh.isEnabled = true
                            Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                        }
                        else -> Unit
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resendState.collect { state ->
                    when (state) {
                        is UiState.Loading -> binding.btnResend.isEnabled = false
                        is UiState.Success -> {
                            Toast.makeText(context, "Verification email sent!", Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Error -> {
                            binding.btnResend.isEnabled = true
                            Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                        }
                        else -> Unit
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cooldownState.collect { seconds ->
                    if (seconds > 0) {
                        binding.btnResend.isEnabled = false
                        binding.btnResend.text = getString(R.string.resend_cooldown, seconds)
                    } else {
                        binding.btnResend.isEnabled = true
                        binding.btnResend.text = getString(R.string.resend_email)
                    }
                }
            }
        }
    }
}
