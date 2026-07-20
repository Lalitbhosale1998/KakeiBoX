package com.personal.kakeibox.ui.navigation

sealed class NavRoutes(val route: String) {
    object Salary   : NavRoutes("salary")
    object Spend    : NavRoutes("spend")
    object Exercise : NavRoutes("exercise")
    object Settings : NavRoutes("settings")
    object Journeys : NavRoutes("journeys")
    object TransactionDetail : NavRoutes("transaction_detail/{transactionId}") {
        fun createRoute(transactionId: Int) = "transaction_detail/$transactionId"
    }
}