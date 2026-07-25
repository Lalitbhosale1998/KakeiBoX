package com.personal.kakeibox.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material.icons.outlined.FlightTakeoff
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
import com.personal.kakeibox.ui.salary.SalaryScreen
import com.personal.kakeibox.ui.settings.SettingsScreen
import com.personal.kakeibox.ui.settings.ThemeViewModel
import com.personal.kakeibox.ui.exercise.ExerciseScreen
import com.personal.kakeibox.ui.components.ExpressiveAnimatedIcon
import com.personal.kakeibox.ui.spend.SpendScreen
import com.personal.kakeibox.ui.theme.spend.TransactionDetailScreen
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


import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

@Composable
fun KakeiboXApp(
    viewModel: ThemeViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    windowSizeClass: WindowSizeClass? = null
) {
    val themeSettings by viewModel.themeSettings.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val haptic = LocalHapticFeedback.current

    val navBarColor by animateColorAsState(
        targetValue = if (themeSettings.topAppBarBackground == com.personal.kakeibox.data.preferences.TopAppBarBackground.PRIMARY_CONTAINER) {
            androidx.compose.ui.graphics.lerp(
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.colorScheme.primaryContainer,
                0.20f // 20% tint overlay for soft blending
            )
        } else {
            MaterialTheme.colorScheme.surfaceContainer
        },
        label = "nav_bar_container_color"
    )

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
            NavRoutes.Exercise.route -> BottomNavItem(
                route = NavRoutes.Exercise.route,
                labelRes = R.string.tab_exercise,
                icon = Icons.Outlined.FitnessCenter,
                selectedIcon = Icons.Filled.FitnessCenter
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

    val isActionEnabled = currentRoute == NavRoutes.Salary.route ||
            currentRoute == NavRoutes.Exercise.route ||
            currentRoute == NavRoutes.Spend.route ||
            currentRoute == "commute"

    val actionButtonScale by animateFloatAsState(
        targetValue = if (isActionEnabled) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "action_button_scale"
    )

    val actionButtonWeight by animateFloatAsState(
        targetValue = if (isActionEnabled) 1f else 0.001f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "action_button_weight"
    )

    val actionColorStart by animateColorAsState(
        targetValue = when (currentRoute) {
            NavRoutes.Salary.route -> Color(0xFFFFD700)
            NavRoutes.Exercise.route -> Color(0xFF10B981)
            NavRoutes.Spend.route -> Color(0xFF0D9488)

            "commute" -> Color(0xFF0284C7)
            else -> MaterialTheme.colorScheme.primary
        },
        label = "action_color_start"
    )

    val actionColorEnd by animateColorAsState(
        targetValue = when (currentRoute) {
            NavRoutes.Salary.route -> Color(0xFFF59E0B)
            NavRoutes.Exercise.route -> Color(0xFF059669)
            NavRoutes.Spend.route -> Color(0xFF0F766E)

            "commute" -> Color(0xFF0369A1)
            else -> MaterialTheme.colorScheme.secondary
        },
        label = "action_color_end"
    )

    val actionIconColor by animateColorAsState(
        targetValue = when (currentRoute) {
            NavRoutes.Salary.route -> Color(0xFF1E293B)
            else -> Color.White
        },
        label = "action_icon_color"
    )

    val isExpandedScreen = windowSizeClass?.widthSizeClass == WindowWidthSizeClass.Expanded

    Row(modifier = Modifier.fillMaxSize()) {
        if (isExpandedScreen) {
            androidx.compose.material3.NavigationRail(
                modifier = Modifier.fillMaxHeight(),
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                bottomNavItems.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                    androidx.compose.material3.NavigationRailItem(
                        selected = isSelected,
                        onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
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
                                contentDescription = stringResource(item.labelRes)
                            )
                        },
                        label = { Text(stringResource(item.labelRes)) }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (!isExpandedScreen) {
                    if (themeSettings.navBarStyle == NavBarStyle.FULL_WIDTH) {
                val tabBounds = remember { mutableStateListOf<Pair<Float, Float>>() }
                val selectedIndex = remember(currentDestination, bottomNavItems) {
                    bottomNavItems.indexOfFirst { item ->
                        currentDestination?.hierarchy?.any { it.route == item.route } == true
                    }.coerceAtLeast(0)
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = navBarColor,
                    tonalElevation = 0.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .height(80.dp)
                    ) {
                        // ── Morphing Sliding Background Pill ──
                        if (tabBounds.size == bottomNavItems.size) {
                            val targetBounds = tabBounds.getOrNull(selectedIndex) ?: Pair(0f, 0f)
                            val animatedX by animateFloatAsState(
                                targetValue = targetBounds.first,
                                animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow),
                                label = "full_pill_x"
                            )
                            val animatedWidth by animateFloatAsState(
                                targetValue = targetBounds.second,
                                animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow),
                                label = "full_pill_width"
                            )
                            val targetColor = MaterialTheme.colorScheme.secondaryContainer
                            val animatedColor by animateColorAsState(targetColor, label = "full_pill_color")

                            val distance = targetBounds.first - animatedX
                            val absDistance = java.lang.Math.abs(distance)
                            val stretchX = 1f + (absDistance / 200f).coerceAtMost(0.25f)
                            val squashY = 1f - (absDistance / 600f).coerceAtMost(0.12f)

                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .offset { IntOffset(animatedX.roundToInt(), 0) }
                                    .width(with(LocalDensity.current) { animatedWidth.toDp() })
                                    .fillMaxHeight()
                                    .graphicsLayer {
                                        scaleX = stretchX
                                        scaleY = squashY
                                        transformOrigin = TransformOrigin(0.5f, 0.5f)
                                    }
                                    .background(animatedColor, RoundedCornerShape(24.dp))
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .selectableGroup()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val midIndex = (bottomNavItems.size + 1) / 2
                            bottomNavItems.forEachIndexed { index, item ->
                                if (index == midIndex) {
                                    CentralActionButton(
                                        currentRoute = currentRoute,
                                        themeViewModel = viewModel,
                                        haptic = haptic,
                                        scale = actionButtonScale,
                                        weight = actionButtonWeight,
                                        brush = Brush.linearGradient(listOf(actionColorStart, actionColorEnd)),
                                        iconColor = actionIconColor
                                    )
                                }
                                val isSelected = currentDestination?.hierarchy?.any {
                                    it.route == item.route
                                } == true

                                val itemWeight by animateFloatAsState(
                                    targetValue = if (isSelected) 1.3f else 1.0f,
                                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                                    label = "full_weight_anim"
                                )

                                val iconScale by animateFloatAsState(
                                    targetValue = if (isSelected) 1.25f else 1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    ),
                                    label = "full_icon_scale_${item.route}"
                                )

                                // Pulsing halo config
                                val haloProgress = remember { Animatable(0f) }
                                LaunchedEffect(isSelected) {
                                    if (isSelected) {
                                        haloProgress.snapTo(0f)
                                        haloProgress.animateTo(
                                            targetValue = 1f,
                                            animationSpec = tween(durationMillis = 650, easing = LinearOutSlowInEasing)
                                        )
                                    }
                                }

                                val iconTranslationY by animateFloatAsState(
                                    targetValue = if (isSelected && item.route == NavRoutes.Salary.route) -5f else 0f,
                                    animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessLow),
                                    label = "full_icon_y_${item.route}"
                                )
                                val iconTranslationX by animateFloatAsState(
                                    targetValue = if (isSelected && item.route == NavRoutes.Exercise.route) 5f else 0f,
                                    animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessLow),
                                    label = "full_icon_x_${item.route}"
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
                                    label = "full_icon_rot_${item.route}"
                                )
                                val labelScale by animateFloatAsState(
                                    targetValue = if (isSelected) 1.05f else 0.95f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    ),
                                    label = "full_label_scale_${item.route}"
                                )

                                val selectedColor = MaterialTheme.colorScheme.onSecondaryContainer

                                Column(
                                    modifier = Modifier
                                        .weight(itemWeight)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(24.dp))
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
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        // Pulsing Halo Ring
                                        if (isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
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

                                        ExpressiveAnimatedIcon(
                                            icon = item.icon,
                                            selectedIcon = item.selectedIcon,
                                            isSelected = isSelected,
                                            tint = selectedColor,
                                            unselectedTint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(22.dp).graphicsLayer {
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
                                        color = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        modifier = Modifier
                                            .graphicsLayer {
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
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SharedTransitionLayout {
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
            composable(NavRoutes.Exercise.route) {
                ExerciseScreen()
            }
            composable(NavRoutes.Settings.route) {
                SettingsScreen()
            }
            composable(NavRoutes.Spend.route) {
                SpendScreen(
                    animatedVisibilityScope = this@composable,
                    onNavigateToTransactionDetail = { id ->
                        navController.navigate(NavRoutes.TransactionDetail.createRoute(id))
                    }
                )
            }
            composable(NavRoutes.TransactionDetail.route) {
                val transactionId = it.arguments?.getString("transactionId")?.toIntOrNull()
                if (transactionId != null) {
                    TransactionDetailScreen(
                        transactionId = transactionId,
                        animatedVisibilityScope = this@composable,
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            }
        }

        // ── Floating "Bento" Navigation Bar Overlay ──
        if (!isExpandedScreen && themeSettings.navBarStyle == NavBarStyle.FLOATING) {
            val tabBounds = remember { mutableStateListOf<Pair<Float, Float>>() }
            val selectedIndex = remember(currentDestination, bottomNavItems) {
                bottomNavItems.indexOfFirst { item ->
                    currentDestination?.hierarchy?.any { it.route == item.route } == true
                }.coerceAtLeast(0)
            }

            val outerShape = RoundedCornerShape(percent = 50)

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
                    color = navBarColor,
                    tonalElevation = 8.dp,
                    shadowElevation = 16.dp,
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.08f)
                    )
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // ── Morphing Sliding Background Pill ──
                        if (tabBounds.size == bottomNavItems.size) {
                            val targetBounds = tabBounds.getOrNull(selectedIndex) ?: Pair(0f, 0f)
                            val animatedX by animateFloatAsState(
                                targetValue = targetBounds.first,
                                animationSpec = spring(
                                    dampingRatio = 0.5f, // Bouncier for lava fluid effect
                                    stiffness = Spring.StiffnessLow
                                ),
                                label = "pill_x"
                            )
                            val animatedWidth by animateFloatAsState(
                                targetValue = targetBounds.second,
                                animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
                                label = "pill_width"
                            )
                            val targetColor = MaterialTheme.colorScheme.secondaryContainer
                            val animatedColor by animateColorAsState(targetColor, label = "pill_color")

                            val distance = targetBounds.first - animatedX
                            val absDistance = java.lang.Math.abs(distance)
                            // Lava lamp oozing stretch effect
                            val stretchX = 1f + (absDistance / 100f).coerceAtMost(0.8f) // High stretch
                            val squashY = 1f - (absDistance / 300f).coerceAtMost(0.4f)  // High squeeze
                            
                            // Shift the transform origin to make it stretch from the leading edge
                            val direction = if (distance > 0) 1f else if (distance < 0) 0f else 0.5f

                            Box(
                                modifier = Modifier
                                    .padding(8.dp) // Match Row padding
                                    .offset { IntOffset(animatedX.roundToInt(), 0) }
                                    .width(with(LocalDensity.current) { animatedWidth.toDp() })
                                    .fillMaxHeight()
                                    .graphicsLayer {
                                        scaleX = stretchX
                                        scaleY = squashY
                                        transformOrigin = TransformOrigin(direction, 0.5f)
                                    }
                                    .background(animatedColor, RoundedCornerShape(percent = 50))
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
                            val midIndex = (bottomNavItems.size + 1) / 2
                            bottomNavItems.forEachIndexed { index, item ->
                                if (index == midIndex) {
                                    CentralActionButton(
                                        currentRoute = currentRoute,
                                        themeViewModel = viewModel,
                                        haptic = haptic,
                                        scale = actionButtonScale,
                                        weight = actionButtonWeight,
                                        brush = Brush.linearGradient(listOf(actionColorStart, actionColorEnd)),
                                        iconColor = actionIconColor
                                    )
                                }
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

                                // 2. Icon Bounce & Twist
                                val iconScale by animateFloatAsState(
                                    targetValue = if (isSelected) 1.35f else 0.85f,
                                    animationSpec = spring(
                                        dampingRatio = 0.5f, // Bouncier
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
                                    targetValue = if (isSelected && item.route == NavRoutes.Exercise.route) 5f else 0f,
                                    animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessLow),
                                    label = "floating_icon_x_${item.route}"
                                )
                                val iconRotation by animateFloatAsState(
                                    targetValue = if (isSelected) 360f else 0f,
                                    animationSpec = spring(
                                        dampingRatio = 0.6f,
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
                                            NavRoutes.Exercise.route -> Color(0xFF10B981) // Emerald Green

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

                                        ExpressiveAnimatedIcon(
                                            icon = item.icon,
                                            selectedIcon = item.selectedIcon,
                                            isSelected = isSelected,
                                            tint = contentColor,
                                            unselectedTint = MaterialTheme.colorScheme.onSurfaceVariant,
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

        if (themeSettings.navBarStyle == NavBarStyle.SPLIT) {
            val leftItems = bottomNavItems.filter { it.route != NavRoutes.Settings.route }
            val rightItems = bottomNavItems.filter { it.route == NavRoutes.Settings.route }
            val leftTabBounds = remember { mutableStateListOf<Pair<Float, Float>>() }
            val leftSelectedIndex = remember(currentDestination, leftItems) {
                leftItems.indexOfFirst { item ->
                    currentDestination?.hierarchy?.any { it.route == item.route } == true
                }
            }

            val leftOuterCornerTopStart by animateIntAsState(
                targetValue = if (leftSelectedIndex == 0) 41 else 32,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "left_outer_corner_ts"
            )
            val leftOuterCornerBottomStart by animateIntAsState(
                targetValue = if (leftSelectedIndex == 0) 41 else 32,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "left_outer_corner_bs"
            )
            val leftOuterCornerTopEnd by animateIntAsState(
                targetValue = if (leftSelectedIndex == leftItems.lastIndex) 41 else 32,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "left_outer_corner_te"
            )
            val leftOuterCornerBottomEnd by animateIntAsState(
                targetValue = if (leftSelectedIndex == leftItems.lastIndex) 41 else 32,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "left_outer_corner_be"
            )

            val leftOuterShape = RoundedCornerShape(
                topStart = leftOuterCornerTopStart.dp,
                bottomStart = leftOuterCornerBottomStart.dp,
                topEnd = leftOuterCornerTopEnd.dp,
                bottomEnd = leftOuterCornerBottomEnd.dp
            )

            val isRightSelected = currentDestination?.hierarchy?.any {
                it.route == NavRoutes.Settings.route
            } == true

            val rightOuterCornerTopStart by animateIntAsState(
                targetValue = if (isRightSelected) 12 else 32,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "right_outer_corner_ts"
            )
            val rightOuterCornerTopEnd by animateIntAsState(
                targetValue = if (isRightSelected) 32 else 32,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "right_outer_corner_te"
            )
            val rightOuterCornerBottomStart by animateIntAsState(
                targetValue = if (isRightSelected) 32 else 32,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "right_outer_corner_bs"
            )
            val rightOuterCornerBottomEnd by animateIntAsState(
                targetValue = if (isRightSelected) 12 else 32,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "right_outer_corner_be"
            )

            val rightOuterShape = RoundedCornerShape(
                topStart = rightOuterCornerTopStart.dp,
                topEnd = rightOuterCornerTopEnd.dp,
                bottomStart = rightOuterCornerBottomStart.dp,
                bottomEnd = rightOuterCornerBottomEnd.dp
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ── Left Dock Pill ──
                Surface(
                    modifier = Modifier
                        .height(82.dp)
                        .weight(1f)
                        .padding(end = 12.dp),
                    shape = leftOuterShape,
                    color = navBarColor,
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f)
                    )
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Morphing Jelly Sliding Indicator for Left Dock
                        if (leftSelectedIndex != -1 && leftTabBounds.size == leftItems.size) {
                            val targetBounds = leftTabBounds.getOrNull(leftSelectedIndex) ?: Pair(0f, 0f)
                            val animatedX by animateFloatAsState(
                                targetValue = targetBounds.first,
                                animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow),
                                label = "left_pill_x"
                            )
                            val animatedWidth by animateFloatAsState(
                                targetValue = targetBounds.second,
                                animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow),
                                label = "left_pill_width"
                            )
                            val targetColor = when (leftItems.getOrNull(leftSelectedIndex)?.route) {
                                NavRoutes.Salary.route -> Color(0xFFFFD700).copy(alpha = 0.15f)
                                NavRoutes.Exercise.route -> Color(0xFF10B981).copy(alpha = 0.15f)
                                else -> Color.Transparent
                            }
                            val animatedColor by animateColorAsState(targetColor, label = "left_pill_color")

                            val distance = targetBounds.first - animatedX
                            val absDistance = java.lang.Math.abs(distance)
                            val stretchX = 1f + (absDistance / 200f).coerceAtMost(0.25f)
                            val squashY = 1f - (absDistance / 600f).coerceAtMost(0.12f)

                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
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
                            leftItems.forEachIndexed { index, item ->
                                val isSelected = currentDestination?.hierarchy?.any {
                                    it.route == item.route
                                } == true

                                val segmentWeight by animateFloatAsState(
                                    targetValue = if (isSelected) 1.4f else 1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    ),
                                    label = "left_weight_anim"
                                )

                                val iconScale by animateFloatAsState(
                                    targetValue = if (isSelected) 1.2f else 1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    ),
                                    label = "left_icon_scale"
                                )

                                val iconTranslationY by animateFloatAsState(
                                    targetValue = if (isSelected && item.route == NavRoutes.Salary.route) -5f else 0f,
                                    animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessLow),
                                    label = "left_icon_y_${item.route}"
                                )
                                val iconTranslationX by animateFloatAsState(
                                    targetValue = if (isSelected && item.route == NavRoutes.Exercise.route) 5f else 0f,
                                    animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessLow),
                                    label = "left_icon_x_${item.route}"
                                )
                                val iconRotation by animateFloatAsState(
                                    targetValue = 0f,
                                    animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessLow),
                                    label = "left_icon_rot_${item.route}"
                                )

                                val labelScale by animateFloatAsState(
                                    targetValue = if (isSelected) 1.05f else 0.95f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    ),
                                    label = "left_label_scale_${item.route}"
                                )

                                val contentColor by animateColorAsState(
                                    targetValue = if (isSelected) {
                                        when (item.route) {
                                            NavRoutes.Salary.route -> Color(0xFFFFD700)
                                            NavRoutes.Exercise.route -> Color(0xFF10B981)
                                            else -> MaterialTheme.colorScheme.onSurface
                                        }
                                    } else MaterialTheme.colorScheme.onSurfaceVariant,
                                    label = "left_content_color"
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(segmentWeight)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(32.dp))
                                        .onGloballyPositioned { coordinates ->
                                            val parent = coordinates.parentLayoutCoordinates
                                            if (parent != null) {
                                                val localPos = parent.localPositionOf(coordinates, Offset.Zero)
                                                val newBounds = Pair(localPos.x, coordinates.size.width.toFloat())
                                                if (index >= leftTabBounds.size) {
                                                    leftTabBounds.add(newBounds)
                                                } else if (leftTabBounds[index] != newBounds) {
                                                    leftTabBounds[index] = newBounds
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

                                        ExpressiveAnimatedIcon(
                                            icon = item.icon,
                                            selectedIcon = item.selectedIcon,
                                            isSelected = isSelected,
                                            tint = contentColor,
                                            unselectedTint = MaterialTheme.colorScheme.onSurfaceVariant,
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

                // Central Action Button (floating capsule)
                if (actionButtonScale > 0.01f) {
                    val splitActionBrush = Brush.linearGradient(listOf(actionColorStart, actionColorEnd))
                    Surface(
                        onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            currentRoute?.let { viewModel.triggerAddActionButton(it) }
                        },
                        modifier = Modifier
                            .height(82.dp)
                            .width(94.dp)
                            .padding(end = 12.dp)
                            .graphicsLayer {
                                scaleX = actionButtonScale
                                scaleY = actionButtonScale
                            },
                        shape = RoundedCornerShape(32.dp),
                        color = navBarColor,
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp,
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f)
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .clip(RoundedCornerShape(32.dp))
                                .background(splitActionBrush),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = actionIconColor,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                // ── Right Dock Pill (Settings Capsule) ──
                rightItems.firstOrNull()?.let { item ->
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.route == item.route
                    } == true

                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected) Color(0xFF8B5CF6) else MaterialTheme.colorScheme.onSurfaceVariant,
                        label = "right_content_color"
                    )

                    val bgAlpha by animateFloatAsState(
                        targetValue = if (isSelected) 0.15f else 0f,
                        label = "right_bg_alpha"
                    )

                    val rightIconScale by animateFloatAsState(
                        targetValue = if (isSelected) 1.25f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "right_icon_scale"
                    )

                    val rightIconRotation by animateFloatAsState(
                        targetValue = if (isSelected) 360f else 0f,
                        animationSpec = spring(
                            dampingRatio = 0.6f,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "right_icon_rot"
                    )

                    Surface(
                        modifier = Modifier
                            .height(82.dp)
                            .width(82.dp)
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
                        shape = rightOuterShape,
                        color = navBarColor,
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp,
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f)
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .clip(RoundedCornerShape(32.dp))
                                .background(contentColor.copy(alpha = bgAlpha)),
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

                                    ExpressiveAnimatedIcon(
                                        icon = item.icon,
                                        selectedIcon = item.selectedIcon,
                                        isSelected = isSelected,
                                        tint = contentColor,
                                        unselectedTint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(24.dp).graphicsLayer {
                                            scaleX = rightIconScale
                                            scaleY = rightIconScale
                                            rotationZ = rightIconRotation
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
                                        scaleX = if (isSelected) 1.05f else 0.95f
                                        scaleY = if (isSelected) 1.05f else 0.95f
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

@Composable
fun RowScope.CentralActionButton(
    currentRoute: String?,
    themeViewModel: ThemeViewModel,
    haptic: androidx.compose.ui.hapticfeedback.HapticFeedback,
    scale: Float,
    weight: Float,
    brush: Brush,
    iconColor: Color
) {
    if (weight > 0.01f) {
        val scaleAnim = remember { Animatable(1f) }
        LaunchedEffect(currentRoute) {
            if (currentRoute != null) {
                scaleAnim.animateTo(
                    targetValue = 0.8f,
                    animationSpec = tween(durationMillis = 80, easing = FastOutLinearInEasing)
                )
                scaleAnim.animateTo(
                    targetValue = 1.15f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
                )
                scaleAnim.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium)
                )
            }
        }

        val rotationAnim by animateFloatAsState(
            targetValue = when (currentRoute) {
                NavRoutes.Salary.route -> 0f
                NavRoutes.Exercise.route -> 180f
                NavRoutes.Spend.route -> 360f
                "commute" -> 540f
                else -> 0f
            },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "fab_rotation"
        )

        val isPressed = remember { mutableStateOf(false) }
        val pressureScale by animateFloatAsState(
            targetValue = if (isPressed.value) 0.85f else 1f,
            animationSpec = spring(dampingRatio = if (isPressed.value) 0.9f else Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
            label = "pressure_scale"
        )
        val combinedScale = scale * scaleAnim.value * pressureScale
        val view = androidx.compose.ui.platform.LocalView.current

        Box(
            modifier = Modifier
                .weight(weight)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            if (combinedScale > 0.01f) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .graphicsLayer {
                            scaleX = combinedScale
                            scaleY = combinedScale
                        }
                        .pointerInput(currentRoute) {
                            detectTapGestures(
                                onPress = {
                                    isPressed.value = true
                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                    tryAwaitRelease()
                                    isPressed.value = false
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                                        view.performHapticFeedback(android.view.HapticFeedbackConstants.CONFIRM)
                                    } else {
                                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                    }
                                },
                                onTap = {
                                    currentRoute?.let { themeViewModel.triggerAddActionButton(it) }
                                }
                            )
                        }
                        .semantics { 
                            contentDescription = "Add new ${currentRoute ?: "item"}"
                            role = androidx.compose.ui.semantics.Role.Button 
                        }
                        .background(brush, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier
                            .size(32.dp)
                            .graphicsLayer {
                                rotationZ = rotationAnim
                            },
                        tint = iconColor
                    )
                }
            }
        }
    }
}
