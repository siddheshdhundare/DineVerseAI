package com.dineverse.ai.ui.auth.login

import androidx.lifecycle.viewModelScope
import com.dineverse.ai.core.base.BaseViewModel
import com.dineverse.ai.core.common.Resource
import com.dineverse.ai.core.common.UiState
import com.dineverse.ai.core.validation.ValidationUtils
import com.dineverse.ai.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        if (!ValidationUtils.isValidEmail(email)) {
            _loginState.value = UiState.Error("Invalid email address")
            return
        }

        if (password.isEmpty()) {
            _loginState.value = UiState.Error("Password cannot be empty")
            return
        }

        viewModelScope.launch {
            _loginState.value = UiState.Loading
            Timber.d("Logging in user: $email")
            when (val result = loginUseCase(email, password)) {
                is Resource.Success -> {
                    Timber.d("Login success")
                    _loginState.value = UiState.Success(Unit)
                }
                is Resource.Error -> {
                    Timber.e("Login failure: ${result.message}")
                    _loginState.value = UiState.Error(result.message ?: "Login failed")
                }
                else -> _loginState.value = UiState.Idle
            }
        }
    }
    
    fun resetState() {
        _loginState.value = UiState.Idle
    }
}
