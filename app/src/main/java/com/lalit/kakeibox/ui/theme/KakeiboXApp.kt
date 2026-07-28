package com.personal.kakeibox.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.foundation.layout.RowScope
import androidx.compose.animation.*
import androidx.compose.animation.core.*

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.width
import androidx.compose.ui.draw.clip

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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.rememberCoroutineScope
import com.personal.kakeibox.ui.components.ExpressivePolygonLoadingIndicator
import com.personal.kakeibox.ui.components.ContainedLoadingIndicator
import kotlinx.coroutines.launch
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import com.personal.kakeibox.data.preferences.NavAnimationPreference
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
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.animation.SharedTransitionLayout
@Composable
fun TopNavSplitButton(
    currentPage: Int,
    onPageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var isExpanded by remember { mutableStateOf(false) }

    val tabs = remember {
        listOf(
            Triple(0, "Salary", Icons.Filled.Wallet),
            Triple(1, "Exercise", Icons.Filled.FitnessCenter),
            Triple(2, "Settings", Icons.Filled.Settings)
        )
    }

    val activeTab = tabs.find { it.first == currentPage } ?: tabs[0]
    val remainingTabs = tabs.filter { it.first != currentPage }

    // Dynamic Corner Morphing: 32.dp when collapsed, 10.dp when expanded
    val activeEndCorner by animateDpAsState(
        targetValue = if (isExpanded) 10.dp else 32.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "active_end_corner"
    )

    Surface(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(start = 20.dp, end = 20.dp, top = 10.dp),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.95f),
        tonalElevation = 8.dp,
        shadowElevation = 10.dp,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(6.dp)
        ) {
            // ── Primary Action Button (Active Tab Display - BIG) ──
            val activeShape = RoundedCornerShape(
                topStart = 32.dp,
                bottomStart = 32.dp,
                topEnd = activeEndCorner,
                bottomEnd = activeEndCorner
            )

            Surface(
                shape = activeShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .clip(activeShape)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        isExpanded = !isExpanded
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = activeTab.third,
                        contentDescription = activeTab.second,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = activeTab.second,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Toggle remaining tabs",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // ── Secondary Action Buttons (Remaining Tabs - BIG with Horizontal Scroll) ──
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandHorizontally(
                    expandFrom = Alignment.Start,
                    clip = false,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
                exit = fadeOut() + shrinkHorizontally(
                    shrinkTowards = Alignment.Start,
                    clip = false,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.width(6.dp))
                    remainingTabs.forEachIndexed { index, tab ->
                        val isLast = index == remainingTabs.size - 1
                        val tabShape = RoundedCornerShape(
                            topStart = 10.dp,
                            bottomStart = 10.dp,
                            topEnd = if (isLast) 32.dp else 10.dp,
                            bottomEnd = if (isLast) 32.dp else 10.dp
                        )

                        Surface(
                            shape = tabShape,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier
                                .clip(tabShape)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isExpanded = false
                                    onPageSelected(tab.first)
                                }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Icon(
                                    imageVector = tab.third,
                                    contentDescription = tab.second,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = tab.second,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }

                        if (!isLast) {
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpressiveActionLoadingFab(
    currentPage: Int,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var isLoading by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val fabColor by animateColorAsState(
        targetValue = when (currentPage) {
            0 -> MaterialTheme.colorScheme.primary
            1 -> Color(0xFFE11D48)
            else -> MaterialTheme.colorScheme.tertiary
        },
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "fab_color"
    )

    val iconColor by animateColorAsState(
        targetValue = when (currentPage) {
            0 -> MaterialTheme.colorScheme.onPrimary
            1 -> Color.White
            else -> MaterialTheme.colorScheme.onTertiary
        },
        label = "fab_icon_color"
    )

    val scale by animateFloatAsState(
        targetValue = if (isLoading) 0.94f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "fab_scale"
    )

    Surface(
        modifier = modifier
            .size(52.dp)
            .shadow(10.dp, CircleShape)
            .clip(CircleShape)
            .clickable {
                if (!isLoading) {
                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    coroutineScope.launch {
                        isLoading = true
                        onActionClick()
                        kotlinx.coroutines.delay(1200)
                        isLoading = false
                        isSuccess = true
                        kotlinx.coroutines.delay(800)
                        isSuccess = false
                    }
                }
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = CircleShape,
        color = fabColor,
        contentColor = iconColor,
        border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.4f))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = Triple(isLoading, isSuccess, currentPage),
                transitionSpec = {
                    (fadeIn(spring(stiffness = Spring.StiffnessMedium)) + scaleIn(initialScale = 0.8f))
                        .togetherWith(fadeOut(spring(stiffness = Spring.StiffnessMedium)) + scaleOut(targetScale = 0.8f))
                },
                label = "fab_content_transition"
            ) { (loading, success, page) ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (success) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Success",
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        ContainedLoadingIndicator(
                            modifier = Modifier.size(38.dp),
                            containerColor = Color.White.copy(alpha = 0.25f),
                            indicatorColor = iconColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KakeiboXApp(
    viewModel: ThemeViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    windowSizeClass: WindowSizeClass? = null
) {
    val themeSettings by viewModel.themeSettings.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val haptic = LocalHapticFeedback.current
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    val navBarColor by animateColorAsState(
        targetValue = if (themeSettings.topAppBarBackground == com.personal.kakeibox.data.preferences.TopAppBarBackground.PRIMARY_CONTAINER) {
            MaterialTheme.colorScheme.primaryContainer
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
            NavRoutes.Salary.route -> Color(0xFF8B5CF6)
            NavRoutes.Exercise.route -> Color(0xFF8B5CF6)
            NavRoutes.Spend.route -> Color(0xFF0D9488)

            "commute" -> Color(0xFF0284C7)
            else -> MaterialTheme.colorScheme.primary
        },
        label = "action_color_start"
    )

    val actionColorEnd by animateColorAsState(
        targetValue = when (currentRoute) {
            NavRoutes.Salary.route -> Color(0xFF7C3AED)
            NavRoutes.Exercise.route -> Color(0xFF7C3AED)
            NavRoutes.Spend.route -> Color(0xFF0F766E)

            "commute" -> Color(0xFF0369A1)
            else -> MaterialTheme.colorScheme.secondary
        },
        label = "action_color_end"
    )

    val actionIconColor by animateColorAsState(
        targetValue = Color.White,
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
            bottomBar = {}
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> SalaryScreen()
                    1 -> ExerciseScreen()
                    2 -> SettingsScreen()
                }
            }

            // Top Header Suite: Left-Aligned Split Button Navigation + Living ContainedLoadingIndicator FAB
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 12.dp, start = 20.dp, end = 20.dp)
                    .align(Alignment.TopStart),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TopNavSplitButton(
                    currentPage = pagerState.currentPage,
                    onPageSelected = { targetPage ->
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(targetPage)
                        }
                    }
                )

                Spacer(modifier = Modifier.width(10.dp))

                ExpressiveActionLoadingFab(
                    currentPage = pagerState.currentPage,
                    onActionClick = {
                        // Quick Action Triggered!
                    }
                )
            }
        }
    }
}
}



@Composable
fun CentralActionButton(
    currentRoute: String?,
    themeViewModel: ThemeViewModel,
    haptic: androidx.compose.ui.hapticfeedback.HapticFeedback,
    scale: Float,
    weight: Float,
    brush: Brush,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
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

        val cornerPercent by animateIntAsState(
            targetValue = if (isPressed.value) 28 else 50,
            animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMedium),
            label = "fab_corner_morph"
        )
        val fabShape = RoundedCornerShape(percent = cornerPercent)

        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            if (combinedScale > 0.01f) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .graphicsLayer {
                            scaleX = combinedScale
                            scaleY = combinedScale
                            shadowElevation = 14.dp.toPx()
                            shape = fabShape
                            clip = false
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
                        .background(brush, fabShape),
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
