package com.personal.kakeibox.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.personal.kakeibox.R
import com.personal.kakeibox.data.preferences.NavBarStyle
import com.personal.kakeibox.ui.navigation.BottomNavItem
import com.personal.kakeibox.ui.navigation.NavRoutes
import com.personal.kakeibox.ui.commute.CommuteScreen
import com.personal.kakeibox.ui.salary.SalaryScreen
import com.personal.kakeibox.ui.settings.SettingsScreen
import com.personal.kakeibox.ui.settings.ThemeViewModel
import com.personal.kakeibox.ui.spend.SpendScreen
import androidx.activity.ComponentActivity


@Composable
fun KakeiboXApp(
    viewModel: ThemeViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val themeSettings by viewModel.themeSettings.collectAsStateWithLifecycle()
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
            if (themeSettings.navBarStyle == NavBarStyle.FULL_WIDTH) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    tonalElevation = 0.dp,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        } == true

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
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = NavRoutes.Salary.route,
                modifier = Modifier.fillMaxSize(),
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

        // ── Floating "Default" Navigation Bar Overlay ──
        if (themeSettings.navBarStyle == NavBarStyle.FLOATING) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 16.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp),
                    shape = RoundedCornerShape(42.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.92f),
                    tonalElevation = 8.dp,
                    shadowElevation = 12.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .selectableGroup()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        bottomNavItems.forEach { item ->
                            val isSelected = currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true

                            val iconScale by animateFloatAsState(
                                targetValue = if (isSelected) 1.2f else 1f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                label = "floating_icon_scale"
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(24.dp))
                                    .selectable(
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
                                        role = Role.Tab
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.size(width = 64.dp, height = 32.dp)
                                ) {
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                        )
                                    }

                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.icon,
                                        contentDescription = stringResource(item.labelRes),
                                        tint = if (isSelected)
                                            MaterialTheme.colorScheme.onSecondaryContainer
                                        else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.graphicsLayer {
                                            scaleX = iconScale
                                            scaleY = iconScale
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = stringResource(item.labelRes),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.ExtraBold,
                                        fontSize = 12.sp,
                                        letterSpacing = 0.4.sp
                                    ),
                                    color = if (isSelected) MaterialTheme.colorScheme.onSurface
                                            else MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
}
