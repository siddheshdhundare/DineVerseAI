package com.dineverse.ai.ui.splash

sealed interface SplashState {
    object Idle : SplashState
    object Authenticated : SplashState
    object Unauthenticated : SplashState
}
