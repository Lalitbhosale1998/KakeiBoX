package com.personal.kakeibox.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.personal.kakeibox.R
import com.personal.kakeibox.ui.navigation.BottomNavItem
import com.personal.kakeibox.ui.navigation.NavRoutes
import com.personal.kakeibox.ui.commute.CommuteScreen
import com.personal.kakeibox.ui.salary.SalaryScreen
import com.personal.kakeibox.ui.settings.SettingsScreen
import com.personal.kakeibox.ui.spend.SpendScreen


@Composable
fun KakeiboXApp() {
    val navController = rememberNavController()
    val haptic = LocalHapticFeedback.current

    val bottomNavItems = listOf(
        BottomNavItem(
            route = NavRoutes.Salary.route,
            labelRes = R.string.tab_salary,
            icon = Icons.Outlined.Wallet,
            selectedIcon = Icons.Filled.Wallet
        ),
        BottomNavItem(
            route = NavRoutes.Spend.route,
            labelRes = R.string.tab_spend,
            icon = Icons.Outlined.ShoppingCart,
            selectedIcon = Icons.Filled.ShoppingCart
        ),
        BottomNavItem(
            route = NavRoutes.Commute.route,
            labelRes = R.string.tab_commute,
            icon = Icons.Outlined.DirectionsBus,
            selectedIcon = Icons.Filled.DirectionsBus
        ),
        BottomNavItem(
            route = NavRoutes.Settings.route,
            labelRes = R.string.tab_settings,
            icon = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                tonalElevation = 0.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                bottomNavItems.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.route == item.route
                    } == true

                    // ── Spring bounce on icon when selected ──
                    val iconScale by animateFloatAsState(
                        targetValue = if (isSelected) 1.25f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "icon_scale_${item.route}"
                    )

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.icon,
                                contentDescription = stringResource(item.labelRes),
                                modifier = Modifier.graphicsLayer {
                                    scaleX = iconScale
                                    scaleY = iconScale
                                }
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(item.labelRes),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.SemiBold
                                else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Salary.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            // ── Screen transition animations ──────────────
            enterTransition = {
                fadeIn(tween(280)) + slideInVertically(tween(280)) { it / 8 }
            },
            exitTransition = {
                fadeOut(tween(200))
            },
            popEnterTransition = {
                fadeIn(tween(280)) + slideInVertically(tween(280)) { -(it / 8) }
            },
            popExitTransition = {
                fadeOut(tween(200))
            }
        ) {
            composable(NavRoutes.Salary.route) {
                SalaryScreen()
            }
            composable(NavRoutes.Spend.route) {
                SpendScreen()
            }
            composable(NavRoutes.Commute.route) {
                CommuteScreen()
            }
            composable(NavRoutes.Settings.route) {
                SettingsScreen()
            }
        }
    }
}