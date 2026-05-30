package com.personal.kakeibox.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.TransformOrigin
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import kotlin.math.roundToInt


@Composable
fun KakeiboXApp(
    viewModel: ThemeViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val themeSettings by viewModel.themeSettings.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val haptic = LocalHapticFeedback.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    // Halo ripple animation state
    var lastSelectedDestination by remember { mutableStateOf<String?>(null) }
    val haloProgress = remember { Animatable(0f) }

    LaunchedEffect(currentRoute) {
        if (currentRoute != null && currentRoute != lastSelectedDestination) {
            lastSelectedDestination = currentRoute
            haloProgress.snapTo(0f)
            haloProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
            )
        }
    }

    val bottomNavItems = themeSettings.tabOrder.mapNotNull { route ->
        when (route) {
            NavRoutes.Salary.route -> BottomNavItem(
                route = NavRoutes.Salary.route,
                labelRes = R.string.tab_salary,
                icon = Icons.Outlined.Wallet,
                selectedIcon = Icons.Filled.Wallet
            )
            NavRoutes.Spend.route -> BottomNavItem(
                route = NavRoutes.Spend.route,
                labelRes = R.string.tab_spend,
                icon = Icons.Outlined.ShoppingCart,
                selectedIcon = Icons.Filled.ShoppingCart
            )
            NavRoutes.Commute.route -> BottomNavItem(
                route = NavRoutes.Commute.route,
                labelRes = R.string.tab_commute,
                icon = Icons.Outlined.DirectionsBus,
                selectedIcon = Icons.Filled.DirectionsBus
            )
            NavRoutes.Settings.route -> BottomNavItem(
                route = NavRoutes.Settings.route,
                labelRes = R.string.tab_settings,
                icon = Icons.Outlined.Settings,
                selectedIcon = Icons.Filled.Settings
            )
            else -> null
        }
    }

    // navBackStackEntry and currentDestination defined above

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

                        val cornerRadius by animateIntAsState(
                            targetValue = if (isSelected) 28 else 16,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "corner_morph_${item.route}"
                        )

                        val itemShape = when (item.route) {
                            NavRoutes.Salary.route -> RoundedCornerShape(
                                topStart = cornerRadius.dp,
                                topEnd = cornerRadius.dp,
                                bottomStart = if (isSelected) 4.dp else cornerRadius.dp,
                                bottomEnd = if (isSelected) 4.dp else cornerRadius.dp
                            )
                            NavRoutes.Spend.route -> RoundedCornerShape(
                                topStart = cornerRadius.dp,
                                bottomEnd = cornerRadius.dp,
                                topEnd = if (isSelected) 4.dp else cornerRadius.dp,
                                bottomStart = if (isSelected) 4.dp else cornerRadius.dp
                            )
                            NavRoutes.Commute.route -> RoundedCornerShape(
                                topStart = cornerRadius.dp,
                                bottomStart = cornerRadius.dp,
                                topEnd = if (isSelected) 4.dp else cornerRadius.dp,
                                bottomEnd = if (isSelected) 4.dp else cornerRadius.dp
                            )
                            else -> RoundedCornerShape(cornerRadius.dp) // pill
                        }

                        val labelScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.05f else 0.95f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "label_scale_${item.route}"
                        )

                        val iconTranslationY by animateFloatAsState(
                            targetValue = if (isSelected && item.route == NavRoutes.Salary.route) -5f else 0f,
                            animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessLow),
                            label = "icon_y_${item.route}"
                        )
                        val iconTranslationX by animateFloatAsState(
                            targetValue = if (isSelected && item.route == NavRoutes.Commute.route) 5f else 0f,
                            animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessLow),
                            label = "icon_x_${item.route}"
                        )
                        val iconRotation by animateFloatAsState(
                            targetValue = when {
                                isSelected && item.route == NavRoutes.Spend.route -> 15f
                                isSelected && item.route == NavRoutes.Settings.route -> 360f
                                else -> 0f
                            },
                            animationSpec = spring(
                                dampingRatio = if (item.route == NavRoutes.Settings.route) 0.6f else 0.4f,
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "icon_rot_${item.route}"
                        )

                        val selectedColor = when (item.route) {
                            NavRoutes.Salary.route -> Color(0xFFFFD700)
                            NavRoutes.Spend.route -> Color(0xFFF43F5E)
                            NavRoutes.Commute.route -> Color(0xFF6EE7B7)
                            NavRoutes.Settings.route -> Color(0xFF8B5CF6)
                            else -> MaterialTheme.colorScheme.primary
                        }

                        // ── Adaptive Bento Item ──
                        val itemWeight by animateFloatAsState(
                            targetValue = if (isSelected) 2.0f else 0.8f,
                            animationSpec = spring(stiffness = Spring.StiffnessLow),
                            label = "weight_anim"
                        )

                        NavigationBarItem(
                            selected = isSelected,
                            modifier = Modifier.weight(itemWeight),
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
                                val bgAlpha by animateFloatAsState(if (isSelected) 0.15f else 0f)
                                
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth() // Take full weight of the parent
                                        .padding(horizontal = 12.dp) // Gap between pills
                                        .clip(itemShape)
                                        .background(selectedColor.copy(alpha = bgAlpha))
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            // Pulsing Halo Ring
                                            if (isSelected) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(36.dp)
                                                        .graphicsLayer {
                                                            scaleX = 1f + (haloProgress.value * 0.8f)
                                                            scaleY = 1f + (haloProgress.value * 0.8f)
                                                            alpha = 1f - haloProgress.value
                                                        }
                                                        .background(
                                                            color = selectedColor.copy(alpha = 0.35f),
                                                            shape = CircleShape
                                                        )
                                                )
                                            }

                                            Icon(
                                                imageVector = if (isSelected) item.selectedIcon else item.icon,
                                                contentDescription = stringResource(item.labelRes),
                                                tint = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(24.dp).graphicsLayer {
                                                    scaleX = iconScale
                                                    scaleY = iconScale
                                                    translationY = iconTranslationY.dp.toPx()
                                                    translationX = iconTranslationX.dp.toPx()
                                                    rotationZ = iconRotation
                                                }
                                            )
                                        }
                                        
                                        AnimatedVisibility(
                                            visible = isSelected,
                                            enter = fadeIn() + expandVertically(),
                                            exit = fadeOut() + shrinkVertically()
                                        ) {
                                            Text(
                                                text = stringResource(item.labelRes),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Black,
                                                color = selectedColor,
                                                maxLines = 1,
                                                modifier = Modifier
                                                    .padding(top = 2.dp)
                                                    .graphicsLayer {
                                                        scaleX = labelScale
                                                        scaleY = labelScale
                                                    }
                                            )
                                        }
                                    }
                                }
                            },
                            label = null,
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent
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
                startDestination = themeSettings.tabOrder.firstOrNull() ?: NavRoutes.Salary.route,
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

        // ── Floating "Bento" Navigation Bar Overlay ──
        if (themeSettings.navBarStyle == NavBarStyle.FLOATING) {
            val tabBounds = remember { mutableStateListOf<Pair<Float, Float>>() }
            val selectedIndex = remember(currentDestination, bottomNavItems) {
                bottomNavItems.indexOfFirst { item ->
                    currentDestination?.hierarchy?.any { it.route == item.route } == true
                }.coerceAtLeast(0)
            }

            val outerCornerTopStart by animateIntAsState(
                targetValue = if (selectedIndex == 0) 41 else 32,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "outer_corner_ts"
            )
            val outerCornerBottomStart by animateIntAsState(
                targetValue = if (selectedIndex == 0) 41 else 32,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "outer_corner_bs"
            )
            val outerCornerTopEnd by animateIntAsState(
                targetValue = if (selectedIndex == bottomNavItems.lastIndex) 41 else 32,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "outer_corner_te"
            )
            val outerCornerBottomEnd by animateIntAsState(
                targetValue = if (selectedIndex == bottomNavItems.lastIndex) 41 else 32,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "outer_corner_be"
            )

            val outerShape = RoundedCornerShape(
                topStart = outerCornerTopStart.dp,
                bottomStart = outerCornerBottomStart.dp,
                topEnd = outerCornerTopEnd.dp,
                bottomEnd = outerCornerBottomEnd.dp
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .height(82.dp),
                    shape = outerShape,
                    color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f),
                    tonalElevation = 0.dp,
                    shadowElevation = 10.dp,
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                    )
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // ── Morphing Sliding Background Pill ──
                        if (tabBounds.size == bottomNavItems.size) {
                            val targetBounds = tabBounds.getOrNull(selectedIndex) ?: Pair(0f, 0f)
                            val animatedX by animateFloatAsState(
                                targetValue = targetBounds.first,
                                animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow),
                                label = "pill_x"
                            )
                            val animatedWidth by animateFloatAsState(
                                targetValue = targetBounds.second,
                                animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow),
                                label = "pill_width"
                            )
                            val targetColor = when (bottomNavItems.getOrNull(selectedIndex)?.route) {
                                NavRoutes.Salary.route -> Color(0xFFFFD700).copy(alpha = 0.15f)
                                NavRoutes.Spend.route -> Color(0xFFF43F5E).copy(alpha = 0.15f)
                                NavRoutes.Commute.route -> Color(0xFF6EE7B7).copy(alpha = 0.15f)
                                NavRoutes.Settings.route -> Color(0xFF8B5CF6).copy(alpha = 0.15f)
                                else -> Color.Transparent
                            }
                            val animatedColor by animateColorAsState(targetColor, label = "pill_color")

                            val distance = targetBounds.first - animatedX
                            val absDistance = java.lang.Math.abs(distance)
                            val stretchX = 1f + (absDistance / 200f).coerceAtMost(0.25f)
                            val squashY = 1f - (absDistance / 600f).coerceAtMost(0.12f)

                            Box(
                                modifier = Modifier
                                    .padding(8.dp) // Match Row padding
                                    .offset { IntOffset(animatedX.roundToInt(), 0) }
                                    .width(with(LocalDensity.current) { animatedWidth.toDp() })
                                    .fillMaxHeight()
                                    .graphicsLayer {
                                        scaleX = stretchX
                                        scaleY = squashY
                                        transformOrigin = TransformOrigin(0.5f, 0.5f)
                                    }
                                    .background(animatedColor, RoundedCornerShape(32.dp))
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .selectableGroup()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            bottomNavItems.forEachIndexed { index, item ->
                                val isSelected = currentDestination?.hierarchy?.any {
                                    it.route == item.route
                                } == true

                                // ── Expressive Animations ──
                                
                                // 1. Adaptive Weight: Active tab expands
                                val segmentWeight by animateFloatAsState(
                                    targetValue = if (isSelected) 1.4f else 1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    ),
                                    label = "weight_anim"
                                )

                                // 2. Icon Bounce
                                val iconScale by animateFloatAsState(
                                    targetValue = if (isSelected) 1.2f else 1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    ),
                                    label = "floating_icon_scale"
                                )

                                // 3. Custom Icon Micro-interactions
                                val iconTranslationY by animateFloatAsState(
                                    targetValue = if (isSelected && item.route == NavRoutes.Salary.route) -5f else 0f,
                                    animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessLow),
                                    label = "floating_icon_y_${item.route}"
                                )
                                val iconTranslationX by animateFloatAsState(
                                    targetValue = if (isSelected && item.route == NavRoutes.Commute.route) 5f else 0f,
                                    animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessLow),
                                    label = "floating_icon_x_${item.route}"
                                )
                                val iconRotation by animateFloatAsState(
                                    targetValue = when {
                                        isSelected && item.route == NavRoutes.Spend.route -> 15f
                                        isSelected && item.route == NavRoutes.Settings.route -> 360f
                                        else -> 0f
                                    },
                                    animationSpec = spring(
                                        dampingRatio = if (item.route == NavRoutes.Settings.route) 0.6f else 0.4f,
                                        stiffness = Spring.StiffnessLow
                                    ),
                                    label = "floating_icon_rot_${item.route}"
                                )

                                val labelScale by animateFloatAsState(
                                    targetValue = if (isSelected) 1.05f else 0.95f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    ),
                                    label = "floating_label_scale_${item.route}"
                                )

                                val contentColor by animateColorAsState(
                                    targetValue = if (isSelected) {
                                        when (item.route) {
                                            NavRoutes.Salary.route -> Color(0xFFFFD700) // Vibrant Gold
                                            NavRoutes.Spend.route -> Color(0xFFF43F5E)  // Vibrant Rose
                                            NavRoutes.Commute.route -> Color(0xFF6EE7B7) // Fresh Mint
                                            NavRoutes.Settings.route -> Color(0xFF8B5CF6) // Vibrant Violet
                                            else -> MaterialTheme.colorScheme.onSurface
                                        }
                                    } else MaterialTheme.colorScheme.onSurfaceVariant,
                                    label = "content_color_anim"
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(segmentWeight) // Apply adaptive weight
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(32.dp))
                                        .onGloballyPositioned { coordinates ->
                                            val parent = coordinates.parentLayoutCoordinates
                                            if (parent != null) {
                                                val localPos = parent.localPositionOf(coordinates, Offset.Zero)
                                                val newBounds = Pair(localPos.x, coordinates.size.width.toFloat())
                                                if (index >= tabBounds.size) {
                                                    tabBounds.add(newBounds)
                                                } else if (tabBounds[index] != newBounds) {
                                                    tabBounds[index] = newBounds
                                                }
                                            }
                                        }
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
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        // Pulsing Halo Ring
                                        if (isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .graphicsLayer {
                                                        scaleX = 1f + (haloProgress.value * 0.8f)
                                                        scaleY = 1f + (haloProgress.value * 0.8f)
                                                        alpha = 1f - haloProgress.value
                                                    }
                                                    .background(
                                                        color = contentColor.copy(alpha = 0.35f),
                                                        shape = CircleShape
                                                    )
                                            )
                                        }

                                        Icon(
                                            imageVector = if (isSelected) item.selectedIcon else item.icon,
                                            contentDescription = stringResource(item.labelRes),
                                            tint = contentColor,
                                            modifier = Modifier.size(24.dp).graphicsLayer {
                                                scaleX = iconScale
                                                scaleY = iconScale
                                                translationY = iconTranslationY.dp.toPx()
                                                translationX = iconTranslationX.dp.toPx()
                                                rotationZ = iconRotation
                                            }
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = stringResource(item.labelRes),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                            fontSize = 11.sp,
                                            letterSpacing = 0.1.sp
                                        ),
                                        color = contentColor,
                                        maxLines = 1,
                                        modifier = Modifier.graphicsLayer {
                                            scaleX = labelScale
                                            scaleY = labelScale
                                        }
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
}
