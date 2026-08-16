package com.personal.kakeibox.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.foundation.layout.RowScope
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.material.icons.filled.Translate
import com.personal.kakeibox.ui.theme.vocab.KotobaScreen
import com.personal.kakeibox.ui.theme.getAppStrings
import com.personal.kakeibox.ui.theme.LocalThemeSettings
import com.personal.kakeibox.ui.theme.ExpressiveMotion

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
import androidx.compose.ui.draw.rotate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material.icons.outlined.FlightTakeoff
import androidx.compose.material.icons.outlined.AutoAwesome
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

    val themeSettings = LocalThemeSettings.current
    val strings = getAppStrings(themeSettings.appLanguage)

    val tabs = remember(strings) {
        listOf(
            Triple(0, strings.salary, Icons.Filled.Wallet),
            Triple(1, strings.exercise, Icons.Filled.FitnessCenter),
            Triple(2, strings.kotoba, Icons.Filled.Translate),
            Triple(3, strings.settings, Icons.Filled.Settings)
        )
    }

    val activeTab = tabs.find { it.first == currentPage } ?: tabs[0]
    val remainingTabs = tabs.filter { it.first != currentPage }

    // ── M3 Expressive Tab-Specific Shape Morphing ──────────────────────────
    // Salary (0) = Classic Capsule | Exercise (1) = Ghost-ish 👻 | Kotoba (2) = Cookie/Torii ⛩️ | Settings (3) = Arch 🏛️
    val targetTopStart = when (currentPage) {
        0 -> 24.dp
        1 -> 28.dp
        2 -> 28.dp
        else -> 28.dp
    }
    val targetTopEnd = when (currentPage) {
        0 -> 24.dp
        1 -> 28.dp
        2 -> 8.dp
        else -> 28.dp
    }
    val targetBottomStart = when (currentPage) {
        0 -> 24.dp
        1 -> 8.dp
        2 -> 28.dp
        else -> 6.dp
    }
    val targetBottomEnd = when (currentPage) {
        0 -> 24.dp
        1 -> 16.dp
        2 -> 8.dp
        else -> 6.dp
    }

    val fabTopStartAnim by animateDpAsState(targetValue = targetTopStart, animationSpec = spring(dampingRatio = 0.68f, stiffness = Spring.StiffnessLow), label = "fab_ts")
    val fabTopEndAnim by animateDpAsState(targetValue = targetTopEnd, animationSpec = spring(dampingRatio = 0.68f, stiffness = Spring.StiffnessLow), label = "fab_te")
    val fabBottomStartAnim by animateDpAsState(targetValue = targetBottomStart, animationSpec = spring(dampingRatio = 0.68f, stiffness = Spring.StiffnessLow), label = "fab_bs")
    val fabBottomEndAnim by animateDpAsState(targetValue = targetBottomEnd, animationSpec = spring(dampingRatio = 0.68f, stiffness = Spring.StiffnessLow), label = "fab_be")

    // Continuous 360° rotation animation for Morphing FAB
    val fabRotation by animateFloatAsState(
        targetValue = currentPage * 360f + (if (isExpanded) 180f else 0f),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "nav_fab_spin"
    )

    // Dynamic Corner Morphing: 32.dp when collapsed, 10.dp when expanded
    val activeEndCorner by animateDpAsState(
        targetValue = if (isExpanded) 10.dp else targetTopEnd,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "active_end_corner"
    )

    val rotationChevron by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessLow),
        label = "top_nav_chevron_rot"
    )

    if (themeSettings.navBarStyle == NavBarStyle.EXPANDED_SEGMENTED) {
        // 📱 Option B: Expanded M3 Segmented Bar (All 3 tabs visible at once)
        Surface(
            modifier = modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(start = 16.dp, end = 16.dp, top = 10.dp),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.95f),
            shadowElevation = 8.dp,
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { (index, title, icon) ->
                    val isSelected = index == currentPage
                    val tabScale by animateFloatAsState(
                        targetValue = if (isSelected) 1.03f else 1.0f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "tab_seg_s_$index"
                    )

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .graphicsLayer {
                                scaleX = tabScale
                                scaleY = tabScale
                            }
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onPageSelected(index)
                            },
                        shape = RoundedCornerShape(24.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                        border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)) else null
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    } else {
        // 🛸 Option A: Floating Capsule Dock with Morphing FAB
        Surface(
            modifier = modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(start = 20.dp, end = 20.dp, top = 10.dp),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shadowElevation = 6.dp,
            tonalElevation = 0.dp,
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(6.dp)
            ) {
                // ── Primary Action Button (Active Tab Display) ──
                val activeShape = RoundedCornerShape(
                    topStart = 32.dp,
                    bottomStart = 32.dp,
                    topEnd = activeEndCorner,
                    bottomEnd = activeEndCorner
                )

                Surface(
                    shape = activeShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .clip(activeShape)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            isExpanded = !isExpanded
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = activeTab.third,
                            contentDescription = activeTab.second,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = activeTab.second,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Toggle remaining tabs",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .size(20.dp)
                                .graphicsLayer {
                                    rotationZ = rotationChevron
                                }
                        )
                    }
                }

                // ── Secondary Action Buttons (Remaining Tabs) ──
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
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
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

                // ── 🌀 Right Side 360° Counter-Rotating Morphing FAB ──
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    modifier = Modifier
                        .size(42.dp)
                        .rotate(fabRotation)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isExpanded = !isExpanded
                        },
                    shape = RoundedCornerShape(
                        topStart = fabTopStartAnim,
                        topEnd = fabTopEndAnim,
                        bottomStart = fabBottomStartAnim,
                        bottomEnd = fabBottomEndAnim
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    shadowElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(-fabRotation),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Menu,
                            contentDescription = "Action FAB",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
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
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 6 })
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
                animationSpec = ExpressiveMotion.sheetEnterSpec()
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
            NavRoutes.Shlok.route -> BottomNavItem(
                route = NavRoutes.Shlok.route,
                labelRes = R.string.tab_shlok,
                icon = Icons.Outlined.AutoAwesome,
                selectedIcon = Icons.Filled.AutoAwesome
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
            @OptIn(ExperimentalSharedTransitionApi::class)
            SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AnimatedContent(
                        targetState = page,
                        transitionSpec = {
                            (fadeIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) +
                             scaleIn(initialScale = 0.92f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)))
                                .togetherWith(
                                    fadeOut(spring(stiffness = Spring.StiffnessMedium)) +
                                    scaleOut(targetScale = 0.92f, animationSpec = spring(stiffness = Spring.StiffnessMedium))
                                )
                        },
                        label = "main_tab_3d_morph"
                    ) { targetPage ->
                        when (targetPage) {
                            0 -> com.personal.kakeibox.ui.home.HomeScreen(onNavigateTab = { target -> coroutineScope.launch { pagerState.animateScrollToPage(target) } })
                            1 -> SalaryScreen()
                            2 -> ExerciseScreen()
                            3 -> com.personal.kakeibox.ui.shlok.ShlokScreen(
                                shlokRepository = com.personal.kakeibox.data.entity.repository.ShlokRepository(),
                                themeSettings = themeSettings
                            )
                            4 -> KotobaScreen()
                            5 -> SettingsScreen()
                        }
                    }
                }
            }

            // ── Top Left Screen Navigation Menu Pill ──
            if (themeSettings.themeStyle == com.personal.kakeibox.data.preferences.ThemeStyle.M3_EXPRESSIVE) {
                var isScreenMenuOpen by remember { mutableStateOf(false) }

                val spinAnim = remember { Animatable(0f) }
                LaunchedEffect(pagerState.currentPage) {
                    spinAnim.snapTo(0f)
                    spinAnim.animateTo(
                        targetValue = 360f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }

                val targetTopStart by animateDpAsState(
                    targetValue = when (pagerState.currentPage) {
                        0 -> 22.dp
                        1 -> 28.dp
                        2 -> 8.dp
                        3 -> 20.dp
                        4 -> 28.dp
                        5 -> 12.dp
                        else -> 22.dp
                    },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                    label = "menu_top_start"
                )
                val targetTopEnd by animateDpAsState(
                    targetValue = when (pagerState.currentPage) {
                        0 -> 22.dp
                        1 -> 8.dp
                        2 -> 28.dp
                        3 -> 20.dp
                        4 -> 28.dp
                        5 -> 12.dp
                        else -> 22.dp
                    },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                    label = "menu_top_end"
                )
                val targetBottomEnd by animateDpAsState(
                    targetValue = when (pagerState.currentPage) {
                        0 -> 22.dp
                        1 -> 28.dp
                        2 -> 8.dp
                        3 -> 20.dp
                        4 -> 6.dp
                        5 -> 12.dp
                        else -> 22.dp
                    },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                    label = "menu_bottom_end"
                )
                val targetBottomStart by animateDpAsState(
                    targetValue = when (pagerState.currentPage) {
                        0 -> 22.dp
                        1 -> 8.dp
                        2 -> 28.dp
                        3 -> 20.dp
                        4 -> 6.dp
                        5 -> 12.dp
                        else -> 22.dp
                    },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                    label = "menu_bottom_start"
                )

                val menuShape = RoundedCornerShape(
                    topStart = targetTopStart,
                    topEnd = targetTopEnd,
                    bottomEnd = targetBottomEnd,
                    bottomStart = targetBottomStart
                )

                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(top = 14.dp, start = 20.dp)
                        .align(Alignment.TopStart),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .rotate(spinAnim.value)
                            .clip(menuShape)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isScreenMenuOpen = true
                            },
                        shape = menuShape,
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 10.dp
                    ) {
                        Box(
                            modifier = Modifier.rotate(-spinAnim.value),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedContent(
                                targetState = pagerState.currentPage,
                                transitionSpec = {
                                    (fadeIn(spring(stiffness = Spring.StiffnessMedium)) + scaleIn(initialScale = 0.6f))
                                        .togetherWith(fadeOut(spring(stiffness = Spring.StiffnessMedium)) + scaleOut(targetScale = 0.6f))
                                },
                                label = "pill_content_morph"
                            ) { page ->
                                val icon = when (page) {
                                    0 -> Icons.Default.Menu
                                    1 -> Icons.Outlined.Wallet
                                    2 -> Icons.Outlined.FitnessCenter
                                    3 -> Icons.Outlined.AutoAwesome
                                    4 -> Icons.Default.Translate
                                    5 -> Icons.Outlined.Settings
                                    else -> Icons.Default.Menu
                                }
                                 val strings = com.personal.kakeibox.ui.theme.getAppStrings(themeSettings.appLanguage)
                                 val labelText = when (page) {
                                     0 -> strings.home.uppercase()
                                     1 -> strings.salary.uppercase()
                                     2 -> strings.exercise.uppercase()
                                     3 -> strings.shlok.uppercase()
                                     4 -> strings.kotoba.uppercase()
                                     5 -> strings.settings.uppercase()
                                     else -> "MENU"
                                 }
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = labelText,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = labelText,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.5.sp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                if (isScreenMenuOpen) {
                    com.personal.kakeibox.ui.components.ExpressiveEditorialMenuDrawer(
                        isOpen = isScreenMenuOpen,
                        onDismiss = { isScreenMenuOpen = false },
                        onNavigateTab = { targetTab ->
                            isScreenMenuOpen = false
                            val pageIndex = when {
                                targetTab.contains("home", ignoreCase = true) || targetTab.contains("overview", ignoreCase = true) -> 0
                                targetTab.contains("salary", ignoreCase = true) || targetTab.contains("savings", ignoreCase = true) -> 1
                                targetTab.contains("exercise", ignoreCase = true) || targetTab.contains("workout", ignoreCase = true) || targetTab.contains("gym", ignoreCase = true) -> 2
                                targetTab.contains("shlok", ignoreCase = true) || targetTab.contains("mantra", ignoreCase = true) -> 3
                                targetTab.contains("kotoba", ignoreCase = true) || targetTab.contains("vocab", ignoreCase = true) || targetTab.contains("japanese", ignoreCase = true) -> 4
                                targetTab.contains("settings", ignoreCase = true) || targetTab.contains("theme", ignoreCase = true) -> 5
                                else -> 0
                            }
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pageIndex)
                            }
                        },
                        onTogglePrivacyMode = { viewModel.setPrivacyModeEnabled(!themeSettings.privacyModeEnabled) },
                        onAddEntry = {},
                        onOpenThemeSettings = {
                            isScreenMenuOpen = false
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(5)
                            }
                        },
                        themeSettings = themeSettings
                    )
                }
            } else {
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
                }
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
