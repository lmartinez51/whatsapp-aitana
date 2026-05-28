package com.lmartinez.miniaitana.ui.navigation

sealed class Screen(val route: String) {
    data object Whitelist : Screen("whitelist")
    data object Prompt    : Screen("prompt")
    data object Model     : Screen("model")
}
