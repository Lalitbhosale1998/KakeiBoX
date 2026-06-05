package com.personal.kakeibox.ui.navigation

sealed class NavRoutes(val route: String) {
    object Salary   : NavRoutes("salary")
    object Spend    : NavRoutes("spend")
    object Exercise : NavRoutes("exercise")
    object Settings : NavRoutes("settings")
}