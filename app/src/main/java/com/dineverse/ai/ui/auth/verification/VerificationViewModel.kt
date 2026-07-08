package com.dineverse.ai.ui.auth.verification

import androidx.lifecycle.viewModelScope
import com.dineverse.ai.core.base.BaseViewModel
import com.dineverse.ai.core.common.Resource
import com.dineverse.ai.core.common.UiState
import com.dineverse.ai.domain.usecase.ResendVerificationEmailUseCase
import com.dineverse.ai.domain.usecase.VerifyEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val resendVerificationEmailUseCase: ResendVerificationEmailUseCase
) : BaseViewModel() {

    private val _verificationState = MutableStateFlow<UiState<Boolean>>(UiState.Idle)
    val verificationState = _verificationState.asStateFlow()

    private val _resendState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val resendState = _resendState.asStateFlow()

    private val _cooldownState = MutableStateFlow(0)
    val cooldownState = _cooldownState.asStateFlow()

    private var cooldownJob: Job? = null

    fun checkVerificationStatus() {
        viewModelScope.launch {
            _verificationState.value = UiState.Loading
            when (val result = verifyEmailUseCase()) {
                is Resource.Success -> {
                    val isVerified = result.data ?: false
                    if (isVerified) {
                        Timber.d("Email verification confirmed")
                    } else {
                        Timber.w("Email not verified yet")
                    }
                    _verificationState.value = UiState.Success(isVerified)
                }
                is Resource.Error -> {
                    Timber.e("Verification check failed: ${result.message}")
                    _verificationState.value = UiState.Error(result.message ?: "Verification check failed")
                }
                else -> _verificationState.value = UiState.Idle
            }
        }
    }

    fun resendEmail(email: String) {
        if (_cooldownState.value > 0) return

        viewModelScope.launch {
            _resendState.value = UiState.Loading
            when (val result = resendVerificationEmailUseCase(email)) {
                is Resource.Success -> {
                    Timber.d("Verification email resent to: $email")
                    _resendState.value = UiState.Success(Unit)
                    startCooldown()
                }
                is Resource.Error -> {
                    Timber.e("Resend email failed: ${result.message}")
                    _resendState.value = UiState.Error(result.message ?: "Failed to resend email")
                }
                else -> _resendState.value = UiState.Idle
            }
        }
    }

    private fun startCooldown() {
        cooldownJob?.cancel()
        cooldownJob = viewModelScope.launch {
            for (i in 60 downTo 0) {
                _cooldownState.value = i
                delay(1000)
            }
        }
    }
}
