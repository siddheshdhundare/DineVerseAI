package com.dineverse.ai.ui.splash

import androidx.lifecycle.viewModelScope
import com.dineverse.ai.core.base.BaseViewModel
import com.dineverse.ai.core.datastore.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : BaseViewModel() {

    private val _state = MutableStateFlow<SplashState>(SplashState.Idle)
    val state = _state.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            Timber.d("Checking session state")
            delay(2000) // 2 seconds delay as requested
            if (sessionManager.isLoggedIn()) {
                Timber.d("User is logged in")
                _state.value = SplashState.Authenticated
            } else {
                Timber.d("User is not logged in")
                _state.value = SplashState.Unauthenticated
            }
        }
    }
}
