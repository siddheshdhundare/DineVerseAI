package com.dineverse.ai.ui.auth.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dineverse.ai.R
import com.dineverse.ai.core.base.BaseFragment
import com.dineverse.ai.core.common.UiState
import com.dineverse.ai.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>() {

    private val viewModel: SignupViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSignupBinding {
        return FragmentSignupBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeState()
    }

    private fun setupUI() {
        binding.btnSignup.setOnClickListener {
            if (!binding.cbTerms.isChecked) {
                Toast.makeText(context, "Please accept the Terms and Conditions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirm = binding.etConfirmPassword.text.toString()
            
            viewModel.signup(name, email, password, confirm)
        }

        binding.btnLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signupState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.btnSignup.isEnabled = false
                        }
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnSignup.isEnabled = true
                            val email = binding.etEmail.text.toString().trim()
                            val bundle = bundleOf("email" to email)
                            findNavController().navigate(R.id.action_signupFragment_to_verificationFragment, bundle)
                        }
                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnSignup.isEnabled = true
                            Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}
