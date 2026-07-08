package com.dineverse.ai.ui.auth.signup

import androidx.lifecycle.viewModelScope
import com.dineverse.ai.core.base.BaseViewModel
import com.dineverse.ai.core.common.Resource
import com.dineverse.ai.core.common.UiState
import com.dineverse.ai.core.validation.ValidationUtils
import com.dineverse.ai.domain.usecase.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase
) : BaseViewModel() {

    private val _signupState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val signupState = _signupState.asStateFlow()

    fun signup(fullName: String, email: String, password: String, confirm: String) {
        if (!ValidationUtils.isValidName(fullName)) {
            _signupState.value = UiState.Error("Full name must be at least 2 characters")
            return
        }

        if (!ValidationUtils.isValidEmail(email)) {
            _signupState.value = UiState.Error("Invalid email address")
            return
        }

        if (!ValidationUtils.isValidPassword(password)) {
            _signupState.value = UiState.Error("Password must be at least 8 characters, include uppercase, lowercase, number, and special character")
            return
        }

        if (!ValidationUtils.isPasswordMatching(password, confirm)) {
            _signupState.value = UiState.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            _signupState.value = UiState.Loading
            Timber.d("Signing up user: $email")
            when (val result = signupUseCase(email, password, fullName)) {
                is Resource.Success -> {
                    Timber.d("Signup success")
                    _signupState.value = UiState.Success(Unit)
                }
                is Resource.Error -> {
                    Timber.e("Signup failure: ${result.message}")
                    _signupState.value = UiState.Error(result.message ?: "Signup failed")
                }
                else -> _signupState.value = UiState.Idle
            }
        }
    }

    fun resetState() {
        _signupState.value = UiState.Idle
    }
}
