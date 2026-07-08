package com.dineverse.ai.ui.auth.forgotpassword

import androidx.lifecycle.viewModelScope
import com.dineverse.ai.core.base.BaseViewModel
import com.dineverse.ai.core.common.Resource
import com.dineverse.ai.core.common.UiState
import com.dineverse.ai.core.validation.ValidationUtils
import com.dineverse.ai.domain.usecase.ForgotPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : BaseViewModel() {

    private val _forgotPasswordState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val forgotPasswordState = _forgotPasswordState.asStateFlow()

    fun forgotPassword(email: String) {
        if (!ValidationUtils.isValidEmail(email)) {
            _forgotPasswordState.value = UiState.Error("Invalid email address")
            return
        }

        viewModelScope.launch {
            _forgotPasswordState.value = UiState.Loading
            Timber.d("Forgot password request for: $email")
            when (val result = forgotPasswordUseCase(email)) {
                is Resource.Success -> {
                    Timber.d("Forgot password email sent")
                    _forgotPasswordState.value = UiState.Success(Unit)
                }
                is Resource.Error -> {
                    Timber.e("Forgot password failure: ${result.message}")
                    _forgotPasswordState.value = UiState.Error(result.message ?: "Failed to send reset link")
                }
                else -> _forgotPasswordState.value = UiState.Idle
            }
        }
    }

    fun resetState() {
        _forgotPasswordState.value = UiState.Idle
    }
}
