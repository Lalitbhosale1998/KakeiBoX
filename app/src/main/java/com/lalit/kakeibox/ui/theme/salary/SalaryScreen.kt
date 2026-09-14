@file:Suppress("DEPRECATION")
package com.personal.kakeibox.ui.salary

import com.personal.kakeibox.ui.theme.getAppStrings
import com.personal.kakeibox.ui.theme.LocalThemeSettings
import com.personal.kakeibox.ui.components.elasticClick
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.animateContentSize
import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.composed
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.interaction.PressInteraction
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.runtime.State
import androidx.annotation.RequiresApi
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import com.personal.kakeibox.data.preferences.ThemeStyle
import com.personal.kakeibox.ui.components.EditorialQuoteFooter
import com.personal.kakeibox.ui.components.EditorialSegment
import com.personal.kakeibox.ui.components.InlineEditorialSentence
import com.personal.kakeibox.ui.components.StackedCondensedHeader
import com.personal.kakeibox.ui.salary.SalaryUiState
import com.personal.kakeibox.ui.salary.SalaryViewModel
import com.personal.kakeibox.ui.theme.NunitoFontFamily
import com.personal.kakeibox.ui.theme.FredokaFontFamily
import com.personal.kakeibox.ui.theme.ComfortaaFontFamily
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.asComposePath
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.Morph
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import java.util.Locale
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import androidx.compose.ui.focus.onFocusChanged
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import com.personal.kakeibox.data.preferences.TopAppBarBackground
import com.personal.kakeibox.ui.settings.ThemeViewModel
import com.personal.kakeibox.ui.settings.BirthdayManagementContent
import com.personal.kakeibox.data.preferences.NavBarStyle
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.lazy.rememberLazyListState
import com.personal.kakeibox.ui.components.ExpressiveCollapsingHeader
import com.personal.kakeibox.ui.components.ExpressiveOutlinedTextField
import com.personal.kakeibox.ui.components.BentoCard
import com.personal.kakeibox.ui.components.ExpressiveEmptyState
import com.personal.kakeibox.ui.components.ExpressivePeriodSelector
import com.personal.kakeibox.ui.components.ExpressiveSnackbarHost
import com.personal.kakeibox.ui.components.ExpressiveChip
import com.personal.kakeibox.ui.components.ExpressiveButton
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.R
import com.personal.kakeibox.data.entity.SalaryEntry
import com.personal.kakeibox.data.preferences.ThemeSettings
import com.personal.kakeibox.ui.components.toShape
import com.personal.kakeibox.util.CurrencyUtils
import com.personal.kakeibox.util.DateUtils
import com.personal.kakeibox.ui.theme.LocalGlowIntensity
import com.personal.kakeibox.ui.theme.glow
import com.personal.kakeibox.ui.theme.OutfitFontFamily
import com.personal.kakeibox.ui.theme.expressiveBackground
import com.personal.kakeibox.data.preferences.BackdropPattern
import com.personal.kakeibox.ui.components.RoundedPolygonShape
import com.personal.kakeibox.ui.components.CookieShape
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Easing
import androidx.compose.foundation.isSystemInDarkTheme
import com.personal.kakeibox.data.preferences.GlowIntensity
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.ui.graphics.TransformOrigin
import kotlin.math.cos
import kotlin.math.sin

data class SalaryTabInfo(
    val filter: SalaryFilter,
    val label: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun SalaryFilterTabRow(
    selectedFilter: SalaryFilter,
    onFilterSelected: (SalaryFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSpaceTerminal = false
    val haptic = LocalHapticFeedback.current
    val glowIntensity = LocalGlowIntensity.current

    val primaryColor = MaterialTheme.colorScheme.primary
    val themeSettings = LocalThemeSettings.current
    val strings = getAppStrings(themeSettings.appLanguage)
    val tabs = remember(primaryColor, strings) {
        listOf(
            SalaryTabInfo(SalaryFilter.ALL, strings.allTime, Icons.Outlined.History, primaryColor),
            SalaryTabInfo(SalaryFilter.THIS_YEAR, strings.thisYear, Icons.Outlined.CalendarMonth, primaryColor),
            SalaryTabInfo(SalaryFilter.HIGH_SAVINGS, strings.highSavings, Icons.AutoMirrored.Outlined.TrendingUp, primaryColor)
        )
    }

    val selectedIndex = tabs.indexOfFirst { it.filter == selectedFilter }.coerceAtLeast(0)
    val tabBounds = remember { mutableStateListOf<Pair<Float, Float>>() }

    // Morphing Outer Corners based on selection
    val outerCornerTopStart by animateIntAsState(
        targetValue = if (selectedIndex == 0) 36 else 28,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "salary_outer_corner_ts"
    )
    val outerCornerBottomStart by animateIntAsState(
        targetValue = if (selectedIndex == 0) 36 else 28,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "salary_outer_corner_bs"
    )
    val outerCornerTopEnd by animateIntAsState(
        targetValue = if (selectedIndex == tabs.lastIndex) 36 else 28,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "salary_outer_corner_te"
    )
    val outerCornerBottomEnd by animateIntAsState(
        targetValue = if (selectedIndex == tabs.lastIndex) 36 else 28,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "salary_outer_corner_be"
    )

    val outerShape = RoundedCornerShape(
        topStart = outerCornerTopStart.dp,
        bottomStart = outerCornerBottomStart.dp,
        topEnd = outerCornerTopEnd.dp,
        bottomEnd = outerCornerBottomEnd.dp
    )

    val containerBg = if (isSpaceTerminal) {
        Color(0xFF0F172A)
    } else {
        MaterialTheme.colorScheme.surfaceContainerLow
    }

    val containerBorder = if (isSpaceTerminal) {
        BorderStroke(1.dp, Color(0xFF46C2B4).copy(alpha = 0.2f))
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    }

    val shadowElevation = 8.dp

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(32.dp),
        color = containerBg,
        border = containerBorder,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Jelly Spring Sliding Background Pill
            if (tabBounds.size == tabs.size) {
                val targetBounds = tabBounds.getOrNull(selectedIndex) ?: Pair(0f, 0f)
                val animatedX by animateFloatAsState(
                    targetValue = targetBounds.first,
                    animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow),
                    label = "salary_pill_x"
                )
                val animatedWidth by animateFloatAsState(
                    targetValue = targetBounds.second,
                    animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow),
                    label = "salary_pill_width"
                )

                val activeTabColor = MaterialTheme.colorScheme.primaryContainer
                val animatedColor by animateColorAsState(activeTabColor, label = "salary_pill_color")

                val distance = targetBounds.first - animatedX
                val absDistance = kotlin.math.abs(distance)
                val stretchX = 1f + (absDistance / 200f).coerceAtMost(0.15f)
                val squashY = 1f - (absDistance / 600f).coerceAtMost(0.08f)

                val pillShape = RoundedCornerShape(26.dp)

                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .offset { IntOffset(kotlin.math.round(animatedX).toInt(), 0) }
                        .width(with(LocalDensity.current) { animatedWidth.toDp() })
                        .fillMaxHeight()
                        .graphicsLayer {
                            scaleX = stretchX
                            scaleY = squashY
                            transformOrigin = TransformOrigin(0.5f, 0.5f)
                        }
                        .background(animatedColor, pillShape)
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f), pillShape)
                )
            }

            // Tabs Content Row: Side-by-Side Icon + Text
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .selectableGroup()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = selectedIndex == index

                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                        label = "salary_content_color"
                    )

                    val iconScale by animateFloatAsState(
                        targetValue = if (isSelected) 1.15f else 1.0f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "salary_icon_scale"
                    )

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(26.dp))
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
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onFilterSelected(tab.filter)
                            }
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            modifier = Modifier
                                .size(20.dp)
                                .graphicsLayer {
                                    scaleX = iconScale
                                    scaleY = iconScale
                                },
                            tint = contentColor
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = tab.label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = contentColor,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryScreen(
    viewModel: SalaryViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val haptic = LocalHapticFeedback.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val themeSettings by themeViewModel.themeSettings.collectAsStateWithLifecycle()
    val strings = getAppStrings(themeSettings.appLanguage)
    val allEntries by viewModel.allEntries.collectAsStateWithLifecycle()
    val currentEntry by viewModel.currentEntry.collectAsStateWithLifecycle()
    val totalSavings by viewModel.totalSavings.collectAsStateWithLifecycle()
    val totalSalary by viewModel.totalSalary.collectAsStateWithLifecycle()
    val totalRemittance by viewModel.totalRemittance.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        themeViewModel.onAddActionButtonClicked.collect { route ->
            if (route == "salary") {
                viewModel.openAddDialog()
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()
    val density = LocalDensity.current
    val statusBarPadding = with(density) { WindowInsets.statusBars.getTop(this).toDp() }
    val maxOffsetPx = with(density) { 70.dp.toPx() }
    val scrollOffset by remember {
        derivedStateOf {
            if (lazyListState.firstVisibleItemIndex > 0) maxOffsetPx
            else lazyListState.firstVisibleItemScrollOffset.toFloat().coerceAtMost(maxOffsetPx)
        }
    }

    val isPrimaryContainer = themeSettings.topAppBarBackground == TopAppBarBackground.PRIMARY_CONTAINER
    val onContainerColor = if (isPrimaryContainer) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    val primaryTextAccent = if (isPrimaryContainer) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary

    val topAppBarContainerColor by animateColorAsState(
        targetValue = when (themeSettings.topAppBarBackground) {
            TopAppBarBackground.SURFACE -> MaterialTheme.colorScheme.primaryContainer
            TopAppBarBackground.PRIMARY_CONTAINER -> MaterialTheme.colorScheme.primaryContainer
        },
        label = "top_app_bar_container_color"
    )

    val bentoIdleColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.surfaceContainerLow,
        label = "bento_idle_color"
    )

    val currentColorScheme = MaterialTheme.colorScheme
    val sheetColorScheme = if (isPrimaryContainer) {
        val blendedSurface = androidx.compose.ui.graphics.lerp(
            currentColorScheme.surface,
            currentColorScheme.primaryContainer,
            0.35f
        )
        val blendedSurfaceHigh = androidx.compose.ui.graphics.lerp(
            currentColorScheme.surfaceContainerHigh,
            currentColorScheme.primaryContainer,
            0.35f
        )
        val blendedSurfaceLow = androidx.compose.ui.graphics.lerp(
            currentColorScheme.surfaceContainerLow,
            currentColorScheme.primaryContainer,
            0.35f
        )
        val blendedSurfaceLowest = androidx.compose.ui.graphics.lerp(
            currentColorScheme.surfaceContainerLowest,
            currentColorScheme.primaryContainer,
            0.35f
        )
        currentColorScheme.copy(
            surface = blendedSurface,
            surfaceContainer = blendedSurface,
            surfaceContainerHigh = blendedSurfaceHigh,
            surfaceContainerLow = blendedSurfaceLow,
            surfaceContainerLowest = blendedSurfaceLowest
        )
    } else {
        currentColorScheme
    }

    // Staggered Entrance State
    var showHero by remember { mutableStateOf(false) }
    var showStats by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        showHero = true
        delay(100)
        showStats = true
        delay(100)
        showHistory = true
    }

    LaunchedEffect(uiState.snackbarMessage, themeSettings.appLanguage) {
        uiState.snackbarMessage?.let { message ->
            val isJapanese = themeSettings.appLanguage == com.personal.kakeibox.data.preferences.AppLanguage.JAPANESE
            val localizedMessage = when {
                message.startsWith("Updated ") && message.endsWith(" salary entries") -> {
                    val count = message.removePrefix("Updated ").removeSuffix(" salary entries")
                    if (isJapanese) "${count}件の給与記録を更新しました" else message
                }
                message == "Entry updated" -> if (isJapanese) "給与記録を更新しました" else message
                message == "Entry saved" -> if (isJapanese) "給与記録を保存しました" else message
                message == "Entry deleted" -> if (isJapanese) "記録を削除しました" else message
                else -> message
            }
            withTimeoutOrNull(2000L) {
                snackbarHostState.showSnackbar(localizedMessage, duration = SnackbarDuration.Indefinite)
            }
            viewModel.clearSnackbar()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .expressiveBackground(
                isDark = isSystemInDarkTheme(),
                isPrimaryContainer = isPrimaryContainer,
                primaryColor = MaterialTheme.colorScheme.primary,
                containerColor = Color.Unspecified,
                pattern = themeSettings.backdropPattern,
                backgroundCanvasStyle = themeSettings.backgroundCanvasStyle
            )
    ) {
        Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = {},
                snackbarHost = { ExpressiveSnackbarHost(snackbarHostState) }
            ) { innerPadding ->
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 8.dp,
                    bottom = innerPadding.calculateBottomPadding() + 80.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Header top spacing
                item {
                    val isExpressive = true
                    Spacer(modifier = Modifier.height(statusBarPadding + 64.dp))
                }

                // 🎭 100% PURE EDITORIAL POSTER MODE FOR SALARY
                if (themeSettings.themeStyle == com.personal.kakeibox.data.preferences.ThemeStyle.EDITORIAL_POSTER) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            StackedCondensedHeader(
                                lines = listOf("SALARY,", "EARNINGS AND", "WEALTH"),
                                fontSize = 42,
                                lineHeight = 44,
                                color = MaterialTheme.colorScheme.primary
                            )

                            InlineEditorialSentence(
                                textSegments = listOf(
                                    EditorialSegment.Text("TRACKING YOUR"),
                                    EditorialSegment.Badge(
                                        "EARNINGS",
                                        badgeColor = MaterialTheme.colorScheme.tertiaryContainer,
                                        textColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            viewModel.openAddDialog()
                                        }
                                    ),
                                    EditorialSegment.Text("&"),
                                    EditorialSegment.Badge(
                                        "SAVINGS",
                                        badgeColor = MaterialTheme.colorScheme.primaryContainer,
                                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            viewModel.openAddDialog()
                                        }
                                    )
                                )
                            )

                            val isJapanese = themeSettings.appLanguage == com.personal.kakeibox.data.preferences.AppLanguage.JAPANESE
                            val currentAmount = currentEntry?.salaryAmount ?: 0L
                            val remittanceAmount = currentEntry?.remittanceAmount ?: 0L
                            val savingsAmount = currentEntry?.savingsAmount ?: 0L
                            val totalSalaryVal = totalSalary ?: 0L

                            Column(
                                modifier = Modifier.elasticClick {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.openAddDialog()
                                },
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = strings.salary.uppercase(),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = com.personal.kakeibox.util.CurrencyUtils.formatAmount(
                                        currentAmount,
                                        themeSettings.currencySymbol,
                                        themeSettings.privacyModeEnabled,
                                        compact = false
                                    ),
                                    fontSize = 44.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = (-2).sp,
                                    lineHeight = 48.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            InlineEditorialSentence(
                                textSegments = listOf(
                                    EditorialSegment.Text("TOTAL CUMULATIVE"),
                                    EditorialSegment.Badge(
                                        com.personal.kakeibox.util.CurrencyUtils.formatAmount(
                                            totalSalaryVal,
                                            themeSettings.currencySymbol,
                                            themeSettings.privacyModeEnabled,
                                            compact = true
                                        ),
                                        badgeColor = MaterialTheme.colorScheme.tertiaryContainer,
                                        textColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            viewModel.toggleHistorySheet()
                                        }
                                    )
                                )
                            )

                            // Rich Information Block A: Monthly Income Allocation
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "MONTHLY ALLOCATION BREAKDOWN",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                InlineEditorialSentence(
                                    textSegments = listOf(
                                        EditorialSegment.Text("REMITTANCE"),
                                        EditorialSegment.Badge(
                                            com.personal.kakeibox.util.CurrencyUtils.formatAmount(remittanceAmount, themeSettings.currencySymbol, themeSettings.privacyModeEnabled, compact = true),
                                            badgeColor = MaterialTheme.colorScheme.primaryContainer,
                                            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                            onClick = {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                viewModel.openAddDialog()
                                            }
                                        ),
                                        EditorialSegment.Text("DIRECT SAVINGS"),
                                        EditorialSegment.Badge(
                                            com.personal.kakeibox.util.CurrencyUtils.formatAmount(savingsAmount, themeSettings.currencySymbol, themeSettings.privacyModeEnabled, compact = true),
                                            badgeColor = MaterialTheme.colorScheme.secondaryContainer,
                                            textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                            onClick = {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                viewModel.openAddDialog()
                                            }
                                        )
                                    )
                                )
                            }

                            EditorialQuoteFooter(
                                quote = if (isJapanese) "金銭の管理は心に平穏をもたらします。" else "Financial peace of mind comes from consistent awareness.",
                                author = "KakeiBoX Salary",
                                quoteColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isPrimaryContainer) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerHigh
                            },
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                            ),
                            shadowElevation = 0.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                IconButton(
                                    onClick = { 
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        themeViewModel.toggleBirthdaySheet(true) 
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.Cake,
                                        contentDescription = "Birthdays Hub",
                                        modifier = Modifier.size(18.dp),
                                        tint = if (isPrimaryContainer) onContainerColor else MaterialTheme.colorScheme.tertiary
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(16.dp)
                                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                )

                                IconButton(
                                    onClick = { 
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.toggleHistorySheet() 
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.History,
                                        contentDescription = "History",
                                        modifier = Modifier.size(18.dp),
                                        tint = if (isPrimaryContainer) onContainerColor else MaterialTheme.colorScheme.secondary
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(16.dp)
                                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                )

                                IconButton(
                                    onClick = { 
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.addDummyData() 
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Upload,
                                        contentDescription = "Add Dummy Data",
                                        modifier = Modifier.size(18.dp),
                                        tint = if (isPrimaryContainer) onContainerColor else MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }

                // ── MODULE 1: HERO POSTER CAROUSEL SLATE ──
                item {
                    AnimatedVisibility(
                        visible = showHero,
                        enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                    ) {
                        val currentDate = remember { java.time.LocalDate.now() }
                        val currentYearVal = remember(currentDate) { currentDate.year }
                        val currentMonthVal = remember(currentDate) { currentDate.monthValue }

                        val monthEntries = remember(allEntries, currentEntry, currentYearVal, currentMonthVal) {
                            if (allEntries.isEmpty()) {
                                listOf(
                                    currentEntry ?: com.personal.kakeibox.data.entity.SalaryEntry(
                                        id = -currentMonthVal,
                                        year = currentYearVal,
                                        month = currentMonthVal,
                                        salaryAmount = 0L,
                                        savingsAmount = 0L,
                                        remittanceAmount = 0L,
                                        remainingAmount = 0L,
                                        note = ""
                                    )
                                )
                            } else {
                                val sortedReal = allEntries.sortedByDescending { it.year * 100 + it.month }
                                val containsCurrent = sortedReal.any { it.month == currentMonthVal && it.year == currentYearVal }
                                if (containsCurrent) {
                                    sortedReal
                                } else {
                                    val currentPlaceholder = currentEntry ?: com.personal.kakeibox.data.entity.SalaryEntry(
                                        id = -currentMonthVal,
                                        year = currentYearVal,
                                        month = currentMonthVal,
                                        salaryAmount = 0L,
                                        savingsAmount = 0L,
                                        remittanceAmount = 0L,
                                        remainingAmount = 0L,
                                        note = ""
                                    )
                                    (listOf(currentPlaceholder) + sortedReal).sortedByDescending { it.year * 100 + it.month }
                                }
                            }
                        }

                        val initialPage = remember(monthEntries, currentMonthVal, currentYearVal) {
                            val idx = monthEntries.indexOfFirst { it.month == currentMonthVal && it.year == currentYearVal }
                            if (idx >= 0) idx else 0
                        }

                        val pagerState = rememberPagerState(
                            initialPage = initialPage,
                            pageCount = { monthEntries.size }
                        )

                        HorizontalPager(
                            state = pagerState,
                            contentPadding = PaddingValues(horizontal = 0.dp),
                            pageSpacing = 10.dp,
                            userScrollEnabled = monthEntries.size > 1,
                            modifier = Modifier.fillMaxWidth()
                        ) { page ->
                            val entry = monthEntries[page]
                            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                            val absPageOffset = kotlin.math.abs(pageOffset)
                            val cardScale = 1f - (absPageOffset * 0.12f).coerceAtMost(0.18f)
                            val cardAlpha = 1f - (absPageOffset * 0.35f).coerceAtMost(0.5f)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer {
                                        scaleX = cardScale
                                        scaleY = cardScale
                                        alpha = cardAlpha
                                    }
                            ) {
                                com.personal.kakeibox.ui.components.ExpressiveEditorialPosterCard(
                                    totalSalary = totalSalary ?: 0L,
                                    thisMonthSalary = entry.salaryAmount,
                                    currentEntry = entry,
                                    isPrivacyMode = themeSettings.privacyModeEnabled,
                                    onEdit = {
                                        if (entry.id > 0) viewModel.openEditDialog(entry)
                                        else viewModel.openAddDialog()
                                    },
                                    themeSettings = themeSettings,
                                    onTogglePrivacyMode = { themeViewModel.setPrivacyModeEnabled(!themeSettings.privacyModeEnabled) }
                                )
                            }
                        }
                    }
                }
            }

            if (themeSettings.themeStyle != com.personal.kakeibox.data.preferences.ThemeStyle.EDITORIAL_POSTER) {
                // ── STATS & ANALYTICS CANVAS SLATE ──
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // MODULE 2: STATS OVERVIEW GRID
                        AnimatedVisibility(
                            visible = showStats,
                            enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                    slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                        ) {
                            ExpressiveStatsGrid(
                                totalSavings = totalSavings ?: 0L,
                                totalRemittance = totalRemittance ?: 0L,
                                isPrivacyMode = themeSettings.privacyModeEnabled,
                                onRemittanceClick = { viewModel.openAddDialog() },
                                bentoIdleColor = bentoIdleColor,
                                themeSettings = themeSettings
                            )
                        }

                        // MODULE 3: INTERACTIVE ANALYTICS BAR CHART
                        AnimatedVisibility(
                            visible = showStats,
                            enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                    slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                        ) {
                            InteractiveAnalyticsChart(
                                entries = allEntries,
                                isPrivacyMode = themeSettings.privacyModeEnabled,
                                themeSettings = themeSettings,
                                onMonthSelected = { entry ->
                                    viewModel.openEditDialog(entry)
                                }
                            )
                        }
                    }
                }



                // ── SINGLE BANK PASSBOOK LEDGER SLATE CONTAINER (通帳 Style) ──
                item {
                    val filteredEntries = when (uiState.currentFilter) {
                        SalaryFilter.ALL -> allEntries
                        SalaryFilter.THIS_YEAR -> allEntries.filter { it.year == DateUtils.getCurrentYear() }
                        SalaryFilter.HIGH_SAVINGS -> allEntries.filter { 
                            it.salaryAmount > 0 && (it.savingsAmount.toFloat() / it.salaryAmount) >= 0.25f 
                        }
                    }

                    AnimatedVisibility(
                        visible = showHistory,
                        enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(28.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                            shadowElevation = 0.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp)
                            ) {
                                // Header inside Slate: Passbook Ledger Title + See All Button
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = strings.history,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Black
                                    )
                                    TextButton(onClick = { viewModel.toggleHistorySheet() }) {
                                        Text(strings.seeAll, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                SalaryFilterTabRow(
                                    selectedFilter = uiState.currentFilter,
                                    onFilterSelected = { viewModel.setFilter(it) },
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                if (filteredEntries.isEmpty()) {
                                    ExpressiveEmptyState(
                                        message = "No records found",
                                        icon = "🔍",
                                        color = onContainerColor
                                    )
                                } else {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        filteredEntries.take(6).forEach { entry ->
                                            ExpressiveHistoryBentoBox(
                                                entry = entry,
                                                isPrivacyMode = themeSettings.privacyModeEnabled,
                                                onEdit = { viewModel.openEditDialog(entry) },
                                                modifier = Modifier.fillMaxWidth(),
                                                themeSettings = themeSettings
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
}

    // Sheets & Dialogs (Update to Tonal Backgrounds)
    if (uiState.showAddEditDialog) {
        val salarySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeDialog() },
            sheetState = salarySheetState,
            containerColor = sheetColorScheme.surfaceContainerHigh,
            dragHandle = {
                Surface(
                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp),
                    shape = CircleShape,
                    color = Color(0xFF10B981)
                ) {
                    Box(modifier = Modifier.size(width = 36.dp, height = 5.dp))
                }
            },
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            MaterialTheme(colorScheme = sheetColorScheme) {
                ExpressiveAddEditSheet(
                    uiState = uiState,
                    themeSettings = themeSettings,
                    onSalaryChange = viewModel::updateSalary,
                    onRemittanceChange = viewModel::updateRemittance,
                    onSavingsChange = viewModel::updateSavings,
                    onNoteChange = viewModel::updateNote,
                    onMonthChange = viewModel::updateMonth,
                    onYearChange = viewModel::updateYear,
                    onSave = viewModel::saveEntry,
                    onDismiss = viewModel::closeDialog
                )
            }
        }
    }

    if (uiState.showDeleteDialog) {
        ExpressiveDeleteDialog(
            onConfirm = viewModel::deleteEntry,
            onDismiss = viewModel::closeDeleteDialog
        )
    }

    if (uiState.showHistorySheet) {
        val historySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleHistorySheet() },
            sheetState = historySheetState,
            containerColor = sheetColorScheme.surfaceContainerHigh,
            dragHandle = {
                Surface(
                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Box(modifier = Modifier.size(width = 36.dp, height = 5.dp))
                }
            },
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            MaterialTheme(colorScheme = sheetColorScheme) {
                HistoryBottomSheet(
                    entries = allEntries,
                    isPrivacyMode = themeSettings.privacyModeEnabled,
                    onEdit = { entry -> viewModel.toggleHistorySheet(); viewModel.openEditDialog(entry) },
                    onDelete = { entry -> viewModel.toggleHistorySheet(); viewModel.openDeleteDialog(entry) },
                    themeSettings = themeSettings
                )
            }
        }
    }

    val showBirthdaySheet by themeViewModel.showBirthdaySheet.collectAsStateWithLifecycle()
    val birthdays by themeViewModel.birthdays.collectAsStateWithLifecycle()

    if (showBirthdaySheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { themeViewModel.toggleBirthdaySheet(false) },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            dragHandle = {
                Surface(
                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.tertiary
                ) {
                    Box(modifier = Modifier.size(width = 36.dp, height = 5.dp))
                }
            }
        ) {
            BirthdayManagementContent(
                birthdays = birthdays,
                onAdd = themeViewModel::addBirthday,
                onDelete = themeViewModel::deleteBirthday,
                onToggleEnabled = { birthday ->
                    themeViewModel.updateBirthday(birthday.copy(isEnabled = !birthday.isEnabled))
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
object AgslShaderHelper {
    fun createShaderBrush(
        shaderCode: String,
        time: Float,
        c1: FloatArray,
        c2: FloatArray
    ): ShaderBrush {
        val runtimeShader = RuntimeShader(shaderCode)
        runtimeShader.setFloatUniform("uTime", time)
        runtimeShader.setFloatUniform("uColor1", c1[0], c1[1], c1[2])
        runtimeShader.setFloatUniform("uColor2", c2[0], c2[1], c2[2])
        return object : ShaderBrush() {
            override fun createShader(size: androidx.compose.ui.geometry.Size): android.graphics.Shader {
                runtimeShader.setFloatUniform("uSize", size.width, size.height)
                return runtimeShader
            }
        }
    }
}

@Composable
fun AuraExpressiveHeroCard(
    totalSalary: Long,
    thisMonthSalary: Long,
    currentEntry: SalaryEntry?,
    isPrivacyMode: Boolean = false,
    onEdit: () -> Unit,
    isPrimaryContainer: Boolean = false,
    themeSettings: ThemeSettings
) {
    val haptic = LocalHapticFeedback.current
    val paydayInfo = remember { DateUtils.calculatePaydayProgress() }

    // 🌊 Payday Progress Arc calculation
    val animatedProgress by animateFloatAsState(
        targetValue = paydayInfo.progressRatio,
        animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "payday_progress"
    )

    // 🌊 Dynamic This Month Progress calculation
    val targetMonthlySalary = if (totalSalary > 0L) (totalSalary / 12f).coerceAtLeast(300000f) else 300000f
    val thisMonthRatio = if (thisMonthSalary > 0L) {
        (thisMonthSalary.toFloat() / targetMonthlySalary).coerceIn(0.05f, 1f)
    } else {
        0f
    }
    val animatedThisMonthProgress by animateFloatAsState(
        targetValue = thisMonthRatio,
        animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "this_month_progress"
    )

    // ⚡ Elastic Micro-Press interaction
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 400f),
        label = "hero_card_scale"
    )

    // 🎆 Approved Feature 1: AGSL Fluid Aurora Shader & Gyroscope Mesh Aura
    val timeState by rememberInfiniteTransition(label = "agsl_time").animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "uTime"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    val agslBrush = remember(timeState, primaryColor, tertiaryColor) {
        val shaderCode = """
            uniform float2 uSize;
            uniform float uTime;
            uniform float3 uColor1;
            uniform float3 uColor2;
            half4 main(float2 fragCoord) {
                float2 uv = fragCoord / uSize;
                float wave = sin(uv.x * 5.0 + uTime * 1.2) * cos(uv.y * 5.0 + uTime * 0.9) * 0.5 + 0.5;
                float3 color = mix(uColor1, uColor2, wave);
                return half4(color, 0.04);
            }
        """.trimIndent()
        AgslShaderHelper.createShaderBrush(
            shaderCode,
            timeState,
            floatArrayOf(primaryColor.red, primaryColor.green, primaryColor.blue),
            floatArrayOf(tertiaryColor.red, tertiaryColor.green, tertiaryColor.blue)
        )
    }

    // 💎 Approved Feature 3: Holographic Chromatic Prism Foil Sheen
    val holoTransition = rememberInfiniteTransition(label = "holo_prism")
    val holoTranslateX by holoTransition.animateFloat(
        initialValue = -300f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "holo_x"
    )

    val holoPrismBrush = remember(holoTranslateX, primaryColor, tertiaryColor, secondaryColor) {
        Brush.linearGradient(
            colors = listOf(
                primaryColor,
                tertiaryColor,
                secondaryColor,
                primaryColor
            ),
            start = Offset(holoTranslateX, 0f),
            end = Offset(holoTranslateX + 350f, 100f)
        )
    }

    // 🔮 Approved Feature 5: Organic Starburst Shape Morphing on Payday Week
    val isPaydayWeek = paydayInfo.daysRemaining <= 5L
    val paydayCornerRadius by animateDpAsState(
        targetValue = if (isPaydayWeek) 6.dp else 20.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "payday_shape_morph"
    )
    val paydayBadgeShape = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = paydayCornerRadius,
        bottomEnd = 20.dp,
        bottomStart = paydayCornerRadius
    )

    val cardBg = if (isPrimaryContainer) {
        androidx.compose.ui.graphics.lerp(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.primaryContainer,
            0.20f
        )
    } else {
        MaterialTheme.colorScheme.surfaceContainerLow
    }

    // 🌊 Living Payday Progress Pulse
    val paydayPulseTransition = rememberInfiniteTransition(label = "payday_pulse")
    val paydayPulseScale by paydayPulseTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "payday_scale_pulse"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            },
        shape = RoundedCornerShape(28.dp),
        color = cardBg,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        shadowElevation = 8.dp,
        tonalElevation = 4.dp
    ) {
        Box(modifier = Modifier.fillMaxWidth().background(agslBrush)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // ── Top Row: Header & Payday Edit Action ──────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "TOTAL CUMULATIVE EARNINGS",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                            if (isPaydayWeek) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Payday Week Active",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        val headlineStyle = if (!isPrivacyMode) {
                            MaterialTheme.typography.headlineLarge.copy(brush = holoPrismBrush, alpha = 1.0f)
                        } else {
                            MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                        }

                        // 🎬 Approved Feature 4: Number-Scrolling Currency Animation (Odometer Effect)
                        AnimatedContent(
                            targetState = totalSalary,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    (slideInVertically { height -> height } + fadeIn())
                                        .togetherWith(slideOutVertically { height -> -height } + fadeOut())
                                } else {
                                    (slideInVertically { height -> -height } + fadeIn())
                                        .togetherWith(slideOutVertically { height -> height } + fadeOut())
                                }
                            },
                            label = "total_salary_odometer"
                        ) { targetTotal ->
                            Text(
                                text = CurrencyUtils.formatAmount(targetTotal, themeSettings.currencySymbol, isPrivacyMode),
                                style = headlineStyle,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (currentEntry != null) {
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onEdit()
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Record",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // ── Bottom Metrics Grid: Symmetrical Bento Cards with Micro-Padding Tweak ──────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Metric 1: This Month's Salary (Symmetrical Bento Card with 24.dp rounded corners & 18.dp padding)
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Payments,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                
                                val nowDate = remember { java.time.LocalDate.now() }
                                val nowYear = remember(nowDate) { nowDate.year }
                                val nowMonth = remember(nowDate) { nowDate.monthValue }
                                val isCurrentMonth = currentEntry?.let { it.month == nowMonth && it.year == nowYear } ?: true
                                val monthName = remember(currentEntry) {
                                    val m = currentEntry?.month ?: nowMonth
                                    when (m) {
                                        1 -> "JANUARY"
                                        2 -> "FEBRUARY"
                                        3 -> "MARCH"
                                        4 -> "APRIL"
                                        5 -> "MAY"
                                        6 -> "JUNE"
                                        7 -> "JULY"
                                        8 -> "AUGUST"
                                        9 -> "SEPTEMBER"
                                        10 -> "OCTOBER"
                                        11 -> "NOVEMBER"
                                        12 -> "DECEMBER"
                                        else -> "THIS MONTH"
                                    }
                                }
                                val dynamicMonthLabel = if (isCurrentMonth) "THIS MONTH" else "$monthName SALARY"

                                Text(
                                    text = dynamicMonthLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            // 🎬 Number-Scrolling Odometer Animation for This Month Amount
                            AnimatedContent(
                                targetState = thisMonthSalary,
                                transitionSpec = {
                                    (fadeIn() + slideInVertically { it / 2 })
                                        .togetherWith(fadeOut() + slideOutVertically { -it / 2 })
                                },
                                label = "this_month_odometer"
                            ) { targetAmount ->
                                Text(
                                    text = CurrencyUtils.formatAmount(targetAmount, themeSettings.currencySymbol, isPrivacyMode, compact = true),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                            ) {
                                if (animatedThisMonthProgress > 0f) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(animatedThisMonthProgress)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(
                                                Brush.horizontalGradient(
                                                    listOf(
                                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                                        MaterialTheme.colorScheme.primary
                                                    )
                                                )
                                            )
                                    )
                                }
                            }

                            Text(
                                text = if (thisMonthSalary == 0L) "0% Logged This Month" else "${(thisMonthRatio * 100).toInt()}% of Monthly Target",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                        }
                    }

                    // Metric 2: Payday Countdown (Symmetrical Bento Card with 24.dp rounded corners & 18.dp padding)
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = paydayBadgeShape,
                        color = if (isPaydayWeek) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.55f) else MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.35f),
                        border = BorderStroke(
                            width = if (isPaydayWeek) 1.5.dp else 1.dp,
                            color = if (isPaydayWeek) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isPaydayWeek) Icons.Default.AutoAwesome else Icons.Outlined.Schedule,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isPaydayWeek) "PAYDAY WEEK!" else "NEXT PAYDAY",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                            if (false) {
                                Text(
                                    text = String.format("%03d", paydayInfo.daysRemaining),
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = (-1.5).sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "DAYS TILL PAYDAY 🔥 RIGHT?!",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            } else {
                                Text(
                                    text = if (paydayInfo.daysRemaining == 0L) "Payday Today! 🎉" else "${paydayInfo.daysRemaining} Days Left",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            
                            // 🎬 Approved Feature 5: Living Payday Progress Pulse
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(animatedProgress)
                                        .clip(RoundedCornerShape(3.dp))
                                        .graphicsLayer {
                                            scaleY = paydayPulseScale
                                        }
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(
                                                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
                                                    MaterialTheme.colorScheme.tertiary
                                                )
                                            )
                                        )
                                )
                            }

                            Text(
                                text = "${paydayInfo.nextPayday.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}, ${paydayInfo.nextPayday.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }} ${paydayInfo.nextPayday.dayOfMonth} (${(paydayInfo.progressRatio * 100).toInt()}%)",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpressiveHeroCard(
    totalSalary: Long,
    totalSavings: Long,
    currentEntry: SalaryEntry?,
    currentMonth: Int,
    currentYear: Int,
    isPrivacyMode: Boolean = false,
    onEdit: () -> Unit,
    isPrimaryContainer: Boolean = false,
    themeSettings: ThemeSettings,
    donutMode: DonutDisplayMode = DonutDisplayMode.PERCENTAGE,
    onDonutClick: () -> Unit = {},
    isHighSavingsActive: Boolean = false
) {
    val haptic = LocalHapticFeedback.current
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "hero_card_flip"
    )

    val animatedWeight by animateIntAsState(
        targetValue = if (rotation < 90f) 900 else 400,
        animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "ticker_weight"
    )
    val tickerFontWeight = FontWeight(animatedWeight)

    val leftWeight by animateFloatAsState(
        targetValue = if (isHighSavingsActive) 0.001f else 1.0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f),
        label = "salary_hero_left_weight"
    )

    val rightWeight by animateFloatAsState(
        targetValue = if (isHighSavingsActive) 2.0f else 1.0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f),
        label = "salary_hero_right_weight"
    )

    val donutSize by animateDpAsState(
        targetValue = if (isHighSavingsActive) 150.dp else 100.dp,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f),
        label = "salary_hero_donut_size"
    )

    val heroShape = themeSettings.earningsCardShape.toShape(isPressed = false)
    val currentDensityVal = LocalDensity.current.density

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (rotation != 0f) {
                    Modifier.graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 12f * currentDensityVal
                    }
                } else Modifier
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                isFlipped = !isFlipped
            },
        shape = heroShape,
        color = if (isPrimaryContainer) {
            androidx.compose.ui.graphics.lerp(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.primaryContainer,
                0.20f
            )
        } else {
            MaterialTheme.colorScheme.surfaceContainerLow
        },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        shadowElevation = 8.dp,
        tonalElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp)
                .padding(vertical = 36.dp, horizontal = 24.dp)
        ) {
            val isBack = rotation > 90f
            Box(
                modifier = Modifier.graphicsLayer {
                    if (isBack) rotationY = 180f
                }
            ) {
                if (!isBack) {
                    // Front Content
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                        Box(
                            modifier = Modifier
                                .weight(leftWeight.coerceAtLeast(0.001f))
                                .graphicsLayer {
                                    alpha = ((leftWeight - 0.1f) / 0.9f).coerceIn(0f, 1f)
                                    scaleX = (leftWeight / 1.0f).coerceIn(0f, 1f)
                                    scaleY = (leftWeight / 1.0f).coerceIn(0f, 1f)
                                }
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "TOTAL EARNINGS",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 2.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                ExpressiveTotalEarningsTicker(
                                    totalSalary = totalSalary,
                                    isPrivacyMode = isPrivacyMode,
                                    themeSettings = themeSettings,
                                    fontWeight = tickerFontWeight
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Outlined.TrendingUp,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Cumulative Net Income",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }
                                
                                if (currentEntry != null) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    FilledTonalButton(
                                        onClick = { 
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            onEdit() 
                                        },
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Edit ${DateUtils.getShortMonthName(currentEntry.month)} Record", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // Donut
                        val savingsRatio = if (totalSalary > 0) 
                            (totalSavings.toFloat() / totalSalary).coerceIn(0f, 1f)
                        else 0f
                        
                        val animatedProgress by animateFloatAsState(
                            targetValue = savingsRatio,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "savings_progress"
                        )

                        Box(
                            modifier = Modifier.weight(rightWeight.coerceAtLeast(0.001f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onDonutClick()
                                },
                                color = Color.Transparent,
                                shape = CircleShape
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.size(donutSize).padding(4.dp)
                                ) {
                                    val trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                    val progressColor = MaterialTheme.colorScheme.primary
                                    
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val stroke = 10.dp.toPx()
                                        
                                        // Track
                                        drawArc(
                                            color = trackColor,
                                            startAngle = 0f,
                                            sweepAngle = 360f,
                                            useCenter = false,
                                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                                        )
                                        
                                        // Progress
                                        drawArc(
                                            color = progressColor,
                                            startAngle = -90f,
                                            sweepAngle = 360f * animatedProgress,
                                            useCenter = false,
                                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                                        )
                                    }
                                    
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        AnimatedContent(
                                            targetState = donutMode,
                                            transitionSpec = {
                                                (fadeIn() + slideInVertically { it / 2 }).togetherWith(fadeOut() + slideOutVertically { -it / 2 })
                                            },
                                            label = "donut_mode_ticker"
                                        ) { mode ->
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                when (mode) {
                                                    DonutDisplayMode.PERCENTAGE -> {
                                                        Text(
                                                            text = "${(savingsRatio * 100).toInt()}%",
                                                            style = MaterialTheme.typography.titleLarge,
                                                            fontWeight = FontWeight.Black,
                                                            color = MaterialTheme.colorScheme.onSurface
                                                        )
                                                        Text(
                                                            text = "SAVED",
                                                            style = MaterialTheme.typography.labelSmall,
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 8.sp,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                    DonutDisplayMode.ABSOLUTE -> {
                                                        Text(
                                                            text = CurrencyUtils.formatAmount(totalSavings, themeSettings.currencySymbol, isPrivacyMode, compact = true),
                                                            style = MaterialTheme.typography.titleMedium,
                                                            fontWeight = FontWeight.Black,
                                                            color = MaterialTheme.colorScheme.onSurface
                                                        )
                                                        Text(
                                                            text = "TOTAL",
                                                            style = MaterialTheme.typography.labelSmall,
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 8.sp,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                    DonutDisplayMode.REMAINING_DAYS -> {
                                                        val today = LocalDate.now()
                                                        val nextMonth = today.withDayOfMonth(1).plusMonths(1)
                                                        val days = ChronoUnit.DAYS.between(today, nextMonth)
                                                        Text(
                                                            text = days.toString(),
                                                            style = MaterialTheme.typography.titleLarge,
                                                            fontWeight = FontWeight.Black,
                                                            color = MaterialTheme.colorScheme.onSurface
                                                        )
                                                        Text(
                                                            text = "DAYS LEFT",
                                                            style = MaterialTheme.typography.labelSmall,
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 8.sp,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    
                    // Glanceable Insights
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Insight 1: This Month
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.CalendarToday, 
                                    contentDescription = null, 
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                val thisMonthVal = currentEntry?.salaryAmount ?: 0L
                                Text(
                                    text = "This Month: ${CurrencyUtils.formatAmount(thisMonthVal, themeSettings.currencySymbol, isPrivacyMode, compact = true)}", 
                                    style = MaterialTheme.typography.labelSmall, 
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Insight 2: Top Source
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Outlined.WorkOutline, 
                                    contentDescription = null, 
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Top Source: Salary", 
                                    style = MaterialTheme.typography.labelSmall, 
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
                } else {
                    // Back Content
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "WEALTH SPLIT METRICS",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.5.sp,
                                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "SAVED FUNDS",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = CurrencyUtils.formatAmount(totalSavings, themeSettings.currencySymbol, isPrivacyMode),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "NET DISPOSABLE",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = CurrencyUtils.formatAmount(totalSalary - totalSavings, themeSettings.currencySymbol, isPrivacyMode),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "💡 Tap card to flip back to main view",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpressiveStatsGrid(
    totalSavings: Long, 
    totalRemittance: Long, 
    isPrivacyMode: Boolean = false,
    onRemittanceClick: () -> Unit = {},
    bentoIdleColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    themeSettings: ThemeSettings
) {
    val strings = getAppStrings(themeSettings.appLanguage)
    Row(
        modifier = Modifier.fillMaxWidth().height(175.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Savings Bento Card with Custom Sparkline & Trend Badge
        BentoCard(
            title = strings.totalSavings,
            icon = Icons.Outlined.Savings,
            idleContainerColor = bentoIdleColor,
            idleContentColor = MaterialTheme.colorScheme.onSurface,
            cardShapePreference = themeSettings.savingsCardShape,
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = CurrencyUtils.formatAmount(totalSavings, themeSettings.currencySymbol, isPrivacyMode),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    // Live Trend Delta Badge
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "▲ +8.4%",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(2.dp))
                
                // Sparkline representation of savings growth (Neon Glow Bezier + Gradient Fill)
                val sparklineColor = Color(0xFF34D399)
                val sparklineBrush = Brush.verticalGradient(
                    colors = listOf(sparklineColor.copy(alpha = 0.45f), Color.Transparent)
                )
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(start = 6.dp, end = 6.dp, top = 4.dp)
                ) {
                    val points = listOf(0.15f, 0.35f, 0.2f, 0.55f, 0.45f, 0.85f)
                    if (points.size >= 2) {
                        val chartWidth = size.width
                        val chartHeight = size.height
                        val stepX = chartWidth / (points.size - 1)

                        val path = androidx.compose.ui.graphics.Path()
                        val fillPath = androidx.compose.ui.graphics.Path()

                        val calculatedPoints = points.mapIndexed { idx, ratio ->
                            Offset(idx * stepX, chartHeight - (ratio * chartHeight))
                        }

                        path.moveTo(calculatedPoints[0].x, calculatedPoints[0].y)
                        fillPath.moveTo(calculatedPoints[0].x, calculatedPoints[0].y)

                        for (i in 0 until calculatedPoints.size - 1) {
                            val p0 = calculatedPoints[i]
                            val p1 = calculatedPoints[i + 1]
                            val controlX = (p0.x + p1.x) / 2
                            path.cubicTo(
                                controlX, p0.y,
                                controlX, p1.y,
                                p1.x, p1.y
                            )
                            fillPath.cubicTo(
                                controlX, p0.y,
                                controlX, p1.y,
                                p1.x, p1.y
                            )
                        }

                        fillPath.lineTo(chartWidth, chartHeight)
                        fillPath.lineTo(0f, chartHeight)
                        fillPath.close()

                        drawPath(path = fillPath, brush = sparklineBrush)

                        drawPath(
                            path = path,
                            color = sparklineColor,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Glowing Neon Endpoint Dot
                        val lastPoint = calculatedPoints.last()
                        drawCircle(color = sparklineColor, radius = 4.dp.toPx(), center = lastPoint)
                        drawCircle(color = Color.White, radius = 2.dp.toPx(), center = lastPoint)
                    }
                }
            }
        }

        // Remittance Bento Card with animated Outflow line & Trend Badge
        BentoCard(
            title = strings.totalRemittance,
            icon = Icons.AutoMirrored.Outlined.ExitToApp,
            idleContainerColor = bentoIdleColor,
            idleContentColor = MaterialTheme.colorScheme.onSurface,
            cardShapePreference = themeSettings.remittanceCardShape,
            modifier = Modifier.weight(1f),
            onClick = onRemittanceClick
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = CurrencyUtils.formatAmount(totalRemittance, themeSettings.currencySymbol, isPrivacyMode),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Live Outflow Badge
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "🔄 Active",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(2.dp))
                
                // Wavy out-transfer line (Neon Bezier + Gradient Fill decay)
                val outflowColor = Color(0xFF38BDF8)
                val outflowBrush = Brush.verticalGradient(
                    colors = listOf(outflowColor.copy(alpha = 0.45f), Color.Transparent)
                )
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(start = 6.dp, end = 6.dp, top = 4.dp)
                ) {
                    val points = listOf(0.85f, 0.7f, 0.55f, 0.4f, 0.25f, 0.1f)
                    if (points.size >= 2) {
                        val chartWidth = size.width
                        val chartHeight = size.height
                        val stepX = chartWidth / (points.size - 1)

                        val path = androidx.compose.ui.graphics.Path()
                        val fillPath = androidx.compose.ui.graphics.Path()

                        val calculatedPoints = points.mapIndexed { idx, ratio ->
                            Offset(idx * stepX, chartHeight - (ratio * chartHeight))
                        }

                        path.moveTo(calculatedPoints[0].x, calculatedPoints[0].y)
                        fillPath.moveTo(calculatedPoints[0].x, calculatedPoints[0].y)

                        for (i in 0 until calculatedPoints.size - 1) {
                            val p0 = calculatedPoints[i]
                            val p1 = calculatedPoints[i + 1]
                            val controlX = (p0.x + p1.x) / 2
                            path.cubicTo(
                                controlX, p0.y,
                                controlX, p1.y,
                                p1.x, p1.y
                            )
                            fillPath.cubicTo(
                                controlX, p0.y,
                                controlX, p1.y,
                                p1.x, p1.y
                            )
                        }

                        fillPath.lineTo(chartWidth, chartHeight)
                        fillPath.lineTo(0f, chartHeight)
                        fillPath.close()

                        drawPath(path = fillPath, brush = outflowBrush)

                        drawPath(
                            path = path,
                            color = outflowColor,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Glowing Neon Endpoint Dot
                        val lastPoint = calculatedPoints.last()
                        drawCircle(color = outflowColor, radius = 4.dp.toPx(), center = lastPoint)
                        drawCircle(color = Color.White, radius = 2.dp.toPx(), center = lastPoint)
                    }
                }
            }
        }
    }
}

@Composable
fun ExpressiveHistoryBentoBox(
    entry: SalaryEntry,
    isPrivacyMode: Boolean,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
    themeSettings: ThemeSettings,
    isLarge: Boolean = false
) {
    val haptic = LocalHapticFeedback.current
    var isExpanded by remember { mutableStateOf(false) }

    val savingsRatio = if (entry.salaryAmount > 0) 
        (entry.savingsAmount.toFloat() / entry.salaryAmount.toFloat())
    else 0f
    val savingsPercent = (savingsRatio * 100).toInt()

    val animatedSavingsProgress by animateFloatAsState(
        targetValue = savingsRatio,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "bento_progress"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val warningColor = Color(0xFFF59E0B)

    val indicatorColor = when {
        savingsPercent >= 25 -> MaterialTheme.colorScheme.tertiary
        savingsPercent >= 10 -> MaterialTheme.colorScheme.secondary
        else -> warningColor
    }

    val topCornerRadius by animateDpAsState(
        targetValue = if (isExpanded) 8.dp else 28.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "top_corners"
    )
    val bottomCornerRadius by animateDpAsState(
        targetValue = if (isExpanded) 24.dp else 28.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "bottom_corners"
    )
    val cardShape = RoundedCornerShape(
        topStart = topCornerRadius,
        topEnd = topCornerRadius,
        bottomStart = bottomCornerRadius,
        bottomEnd = bottomCornerRadius
    )

    val shadowElevationAnimated by animateDpAsState(
        targetValue = 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "shadow_elevation_anim"
    )
    val tonalElevationAnimated by animateDpAsState(
        targetValue = 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "tonal_elevation_anim"
    )

    @OptIn(ExperimentalSharedTransitionApi::class)
    SharedTransitionLayout {
        AnimatedContent(
            targetState = isExpanded,
            label = "ledger_transform"
        ) { expanded ->
            Surface(
                modifier = modifier
                    .sharedElement(
                        rememberSharedContentState(key = "card_${entry.id}"),
                        animatedVisibilityScope = this@AnimatedContent
                    ),
                shape = cardShape,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                color = if (expanded) MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.35f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = if (expanded) 0.3f else 0.2f)),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    isExpanded = !isExpanded
                }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(5.dp)
                                    .height(38.dp)
                                    .clip(CircleShape)
                                    .background(indicatorColor)
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                ) {
                                    val itemMonthLocale = if (themeSettings.appLanguage == com.personal.kakeibox.data.preferences.AppLanguage.JAPANESE) java.util.Locale.JAPAN else java.util.Locale.ENGLISH
                                    Text(
                                        text = DateUtils.getShortMonthName(entry.month, itemMonthLocale).uppercase(),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(4.dp))
                                
                                Text(
                                    text = CurrencyUtils.formatAmount(entry.salaryAmount, themeSettings.currencySymbol, isPrivacyMode),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    if (expanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                val strings = getAppStrings(themeSettings.appLanguage)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = strings.totalSavings.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                        letterSpacing = 1.sp
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = CurrencyUtils.formatAmount(entry.savingsAmount, themeSettings.currencySymbol, isPrivacyMode),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(50), // Pill Shape
                                            color = indicatorColor.copy(alpha = 0.12f),
                                            border = BorderStroke(1.dp, indicatorColor.copy(alpha = 0.25f))
                                        ) {
                                            Text(
                                                text = "$savingsPercent%",
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Black,
                                                color = indicatorColor
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(animatedSavingsProgress.coerceAtLeast(0.01f))
                                            .fillMaxHeight()
                                            .background(indicatorColor, CircleShape)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val strings = getAppStrings(themeSettings.appLanguage)
                                Column {
                                    Text(
                                        text = strings.remittanceLabel.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = CurrencyUtils.formatAmount(entry.remittanceAmount, themeSettings.currencySymbol, isPrivacyMode),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = strings.spent.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                    )
                                    val spent = entry.salaryAmount - entry.savingsAmount - entry.remittanceAmount
                                    Text(
                                        text = CurrencyUtils.formatAmount(spent, themeSettings.currencySymbol, isPrivacyMode),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            if (entry.note.isNotBlank()) {
                                val strings = getAppStrings(themeSettings.appLanguage)
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = strings.note.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                    )
                                    Text(
                                        text = entry.note,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                val strings = getAppStrings(themeSettings.appLanguage)
                                OutlinedButton(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onEdit()
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(strings.editRecord, style = MaterialTheme.typography.labelMedium)
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
fun ExpressiveSalaryCard(
    entry: SalaryEntry,
    isPrivacyMode: Boolean = false,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    themeSettings: ThemeSettings
) {
    val haptic = LocalHapticFeedback.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = containerColor,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        shadowElevation = 8.dp,
        tonalElevation = 4.dp,
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onEdit()
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val itemMonthLocale = if (themeSettings.appLanguage == com.personal.kakeibox.data.preferences.AppLanguage.JAPANESE) java.util.Locale.JAPAN else java.util.Locale.ENGLISH
                        Text(
                            text = DateUtils.getShortMonthName(entry.month, itemMonthLocale).uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = entry.year.toString().takeLast(2),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = CurrencyUtils.formatAmount(entry.salaryAmount, themeSettings.currencySymbol, isPrivacyMode),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Savings, 
                        contentDescription = null, 
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Saved: ${CurrencyUtils.formatAmount(entry.savingsAmount, themeSettings.currencySymbol, isPrivacyMode)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (entry.note.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = entry.note,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            IconButton(onClick = onEdit) {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun ExpressiveEmptyHero(onAdd: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Outlined.Payments,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "No Salary Data", 
                style = MaterialTheme.typography.headlineSmall, 
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Track your monthly income and savings to get the full picture of your finances.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))
            ExpressiveButton(
                onClick = onAdd,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Salary Now", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ExpressiveTotalEarningsTicker(
    totalSalary: Long,
    isPrivacyMode: Boolean,
    themeSettings: ThemeSettings,
    fontWeight: FontWeight = FontWeight.Black
) {
    val haptic = LocalHapticFeedback.current
    val formattedTotal = CurrencyUtils.formatAmount(totalSalary, themeSettings.currencySymbol, isPrivacyMode)
    
    val textStyle = when {
        formattedTotal.length <= 6 -> MaterialTheme.typography.displayLarge
        formattedTotal.length <= 9 -> MaterialTheme.typography.displayMedium
        formattedTotal.length <= 12 -> MaterialTheme.typography.displaySmall
        else -> MaterialTheme.typography.headlineLarge
    }.copy(
        letterSpacing = (-2).sp
    )
    
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isPrivacyMode) {
            Text(
                text = formattedTotal,
                style = textStyle,
                fontWeight = fontWeight
            )
        } else {
            // Split the formatted string into characters to animate each digit separately
            formattedTotal.forEachIndexed { index, char ->
                val isDigit = char.isDigit()
                
                AnimatedContent(
                    targetState = char,
                    transitionSpec = {
                        if (isDigit) {
                            // Micro-Haptic "Ticker" Feedback
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
 
                            // The "Physical Drum" Roll effect for digits
                            (slideInVertically(animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it } + fadeIn())
                                .togetherWith(slideOutVertically(animationSpec = spring(stiffness = Spring.StiffnessLow)) { -it } + fadeOut())
                        } else {
                            // Standard fade for symbols and separators
                            fadeIn(animationSpec = com.personal.kakeibox.ui.theme.ExpressiveMotion.standardSpec())
                                .togetherWith(fadeOut(animationSpec = com.personal.kakeibox.ui.theme.ExpressiveMotion.standardSpec()))
                        }
                    },
                    label = "digit_ticker_$index"
                ) { targetChar ->
                    Text(
                        text = targetChar.toString(),
                        style = textStyle,
                        fontWeight = fontWeight,
                        softWrap = false
                    )
                }
            }
        }
    }
}

@Composable
fun ExpressiveAddEditSheet(
    uiState: SalaryUiState,
    themeSettings: ThemeSettings,
    onSalaryChange: (String) -> Unit,
    onRemittanceChange: (String) -> Unit,
    onSavingsChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onMonthChange: (Int) -> Unit,
    onYearChange: (Int) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    
    // Focus states for animations
    var isSalaryFocused by remember { mutableStateOf(false) }
    var isSavingsFocused by remember { mutableStateOf(false) }
    var isRemittanceFocused by remember { mutableStateOf(false) }
    var showNoteField by remember { mutableStateOf(uiState.inputNote.isNotBlank()) }

    val strings = getAppStrings(themeSettings.appLanguage)
    val isJapanese = themeSettings.appLanguage == com.personal.kakeibox.data.preferences.AppLanguage.JAPANESE

    val savingsWeight by animateFloatAsState(
        targetValue = if (isSavingsFocused) 1.5f else if (isRemittanceFocused) 0.6f else 1.0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f),
        label = "salary_savings_weight"
    )
    val remittanceWeight by animateFloatAsState(
        targetValue = if (isRemittanceFocused) 1.5f else if (isSavingsFocused) 0.6f else 1.0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f),
        label = "salary_remittance_weight"
    )

    var animateIn by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { animateIn = true }

    val slideY by animateDpAsState(
        targetValue = if (animateIn) 0.dp else 100.dp,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow),
        label = "sheet_slide_in"
    )
    val sheetAlpha by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0f,
        animationSpec = tween(300),
        label = "sheet_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .navigationBarsPadding()
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (themeSettings.themeStyle == com.personal.kakeibox.data.preferences.ThemeStyle.EDITORIAL_POSTER) {
                    StackedCondensedHeader(
                        lines = if (uiState.editingEntry == null) listOf("NEW SALARY,", "EARNINGS AND", "ALLOCATION") else listOf("EDIT SALARY,", "WEALTH AND", "RECORD"),
                        fontSize = 28,
                        lineHeight = 30,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    InlineEditorialSentence(
                        textSegments = listOf(
                            EditorialSegment.Text("RECORDING YOUR"),
                            EditorialSegment.Badge("SALARY", badgeColor = MaterialTheme.colorScheme.tertiaryContainer, textColor = MaterialTheme.colorScheme.onTertiaryContainer),
                            EditorialSegment.Text("&"),
                            EditorialSegment.Badge("SAVINGS", badgeColor = MaterialTheme.colorScheme.primaryContainer, textColor = MaterialTheme.colorScheme.onPrimaryContainer)
                        )
                    )
                } else {
                    Text(
                        text = if (uiState.editingEntry == null) strings.addSalary else strings.editSalary,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = if (isJapanese) "月次の収入と配分を管理します" else "Track your monthly earnings & splits",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }
            
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHighest, CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        // 1. Period Island (Bento Selection)
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = strings.payPeriod,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                )
                ExpressivePeriodSelector(
                    selectedMonth = uiState.inputMonth,
                    selectedYear = uiState.inputYear,
                    onMonthChange = onMonthChange,
                    onYearChange = onYearChange
                )
            }
        }
        
        // 2. Hero Amount Island (Bento Card, Focused Scaling with High-Contrast M3 Theme)
        val salaryElevation by animateDpAsState(if (isSalaryFocused) 8.dp else 2.dp, label = "salary_elevation")
        val salaryScale by animateFloatAsState(if (isSalaryFocused) 1.02f else 1f, label = "salary_scale")
        
        Surface(
            color = if (isSalaryFocused) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(28.dp),
            shadowElevation = 0.dp,
            tonalElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(scaleX = salaryScale, scaleY = salaryScale),
            border = BorderStroke(
                width = if (isSalaryFocused) 2.dp else 1.dp,
                color = if (isSalaryFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(vertical = 28.dp, horizontal = 24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = strings.totalEarnings,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSalaryFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp
                )
                
                BasicTextField(
                    value = uiState.inputSalary,
                    onValueChange = onSalaryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .onFocusChanged { 
                            if (it.isFocused != isSalaryFocused) {
                                isSalaryFocused = it.isFocused 
                                if (it.isFocused) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        },
                    textStyle = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        color = if (isSalaryFocused) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = themeSettings.currencySymbol,
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Black,
                                color = if (isSalaryFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(contentAlignment = Alignment.Center) {
                                if (uiState.inputSalary.isEmpty()) {
                                    Text(
                                        text = "0",
                                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 42.sp),
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
                                    )
                                }
                                innerTextField()
                            }
                        }
                    }
                )
            }
        }
        
        // Visual Math feedback (The "Remaining" Pill)
        AnimatedVisibility(
            visible = uiState.inputSalary.isNotBlank(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            val salary = uiState.inputSalary.toDoubleOrNull() ?: 0.0
            val savings = uiState.inputSavings.toDoubleOrNull() ?: 0.0
            val remittance = uiState.inputRemittance.toDoubleOrNull() ?: 0.0
            val remaining = salary - savings - remittance

            Surface(
                color = if (remaining >= 0) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f) 
                        else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                shape = CircleShape,
                border = BorderStroke(1.dp, if (remaining >= 0) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val remainingText = try {
                        String.format(Locale.getDefault(), "%,.0f", remaining)
                    } catch (e: Exception) {
                        "0"
                    }
                    
                    Icon(
                        imageVector = if (remaining >= 0) Icons.Outlined.AccountBalanceWallet else Icons.Outlined.Info,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (remaining >= 0) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${strings.netRemaining}: ${themeSettings.currencySymbol}$remainingText",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (remaining >= 0) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }

        // Bento Island for Allocations (Savings & Remittance & Notes)
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = strings.allocations,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(savingsWeight.coerceAtLeast(0.001f))
                    ) {
                        ExpressiveOutlinedTextField(
                            value = uiState.inputSavings,
                            onValueChange = onSavingsChange,
                            label = { Text(strings.savingsLabel) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { isSavingsFocused = it.isFocused },
                            leadingIcon = { Icon(Icons.Outlined.Savings, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        AnimatedVisibility(
                            visible = isSavingsFocused,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Text(
                                text = if (isJapanese) "目標: 20%以上" else "Target: 20%+",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.weight(remittanceWeight.coerceAtLeast(0.001f))
                    ) {
                        ExpressiveOutlinedTextField(
                            value = uiState.inputRemittance,
                            onValueChange = onRemittanceChange,
                            label = { Text(strings.remittanceLabel) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { isRemittanceFocused = it.isFocused },
                            leadingIcon = { Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        AnimatedVisibility(
                            visible = isRemittanceFocused,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Text(
                                text = if (isJapanese) "仕送り・家族送金" else "Transfers/Family",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Dynamic Allocation segmented graph
                val salary = uiState.inputSalary.toDoubleOrNull() ?: 0.0
                val savings = uiState.inputSavings.toDoubleOrNull() ?: 0.0
                val remittance = uiState.inputRemittance.toDoubleOrNull() ?: 0.0

                val savingsRatio = if (salary > 0) (savings / salary).coerceIn(0.0, 1.0) else 0.0
                val remittanceRatio = if (salary > 0) (remittance / salary).coerceIn(0.0, 1.0) else 0.0
                val remainingRatio = (1.0 - savingsRatio - remittanceRatio).coerceAtLeast(0.0)

                if (salary > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        // Segmented progress bar
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth().height(10.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                if (savingsRatio > 0.001) {
                                    Box(
                                        modifier = Modifier
                                            .weight(savingsRatio.toFloat())
                                            .fillMaxHeight()
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                }
                                if (remittanceRatio > 0.001) {
                                    Box(
                                        modifier = Modifier
                                            .weight(remittanceRatio.toFloat())
                                            .fillMaxHeight()
                                            .background(Color(0xFF8B5CF6)) // Premium Purple
                                    )
                                }
                                if (remainingRatio > 0.001) {
                                    Box(
                                        modifier = Modifier
                                            .weight(remainingRatio.toFloat())
                                            .fillMaxHeight()
                                            .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Legends
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(6.dp).background(MaterialTheme.colorScheme.primary, CircleShape))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${strings.saved}: ${(savingsRatio * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(6.dp).background(Color(0xFF8B5CF6), CircleShape))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${strings.remittanceLabel}: ${(remittanceRatio * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(6.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), CircleShape))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${strings.spent}: ${(remainingRatio * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Expanding Note Drawer (Merged into Allocations Card)
                AnimatedVisibility(
                    visible = showNoteField,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        ExpressiveOutlinedTextField(
                            value = uiState.inputNote,
                            onValueChange = onNoteChange,
                            label = { Text(strings.noteLabel) },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = { Icon(Icons.Outlined.NoteAlt, contentDescription = null) },
                            placeholder = { Text(if (isJapanese) "ボーナス、残業代など" else "Bonus, overtime, etc.") }
                        )
                    }
                }

                if (!showNoteField) {
                    TextButton(
                        onClick = { showNoteField = true },
                        modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isJapanese) "メモを追加" else "Add Note", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        val isInputValid = uiState.inputSalary.isNotBlank() && uiState.inputSalary.toDoubleOrNull() != null
        
        ExpressiveButton(
            onClick = {
                if (isInputValid) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSave()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isInputValid
        ) {
            AnimatedContent(
                targetState = isInputValid,
                transitionSpec = {
                    (fadeIn() + slideInVertically { it / 2 }).togetherWith(fadeOut() + slideOutVertically { -it / 2 })
                },
                label = "save_button_content"
            ) { valid ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (valid) Icons.Default.Check else Icons.Default.Lock, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        if (valid) strings.saveRecord else if (isJapanese) "金額を入力してください" else "Enter Salary to Save",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
        
        if (themeSettings.themeStyle == com.personal.kakeibox.data.preferences.ThemeStyle.EDITORIAL_POSTER) {
            Spacer(modifier = Modifier.height(12.dp))
            EditorialQuoteFooter(
                quote = if (isJapanese) "金銭的な平穏は、継続的な意識から生まれます。" else "Financial peace of mind comes from consistent awareness.",
                author = "KakeiBoX Wealth Editorial",
                quoteColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Composable
fun ExpressiveDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        var animateTrigger by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { animateTrigger = true }

        val scale by animateFloatAsState(
            targetValue = if (animateTrigger) 1f else 0.8f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "dialog_scale"
        )
        val alpha by animateFloatAsState(
            targetValue = if (animateTrigger) 1f else 0f,
            animationSpec = tween(200),
            label = "dialog_alpha"
        )

        val infiniteTransition = rememberInfiniteTransition(label = "danger_aura_inf")
        val auraPulseScale by infiniteTransition.animateFloat(
            initialValue = 1.01f,
            targetValue = 1.06f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "aura_scale"
        )
        val auraPulseAlpha by infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 0.55f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "aura_alpha"
        )

        Box(contentAlignment = Alignment.Center) {
            // Pulsing Danger Aura Glow
            Box(
                modifier = Modifier
                    .graphicsLayer(scaleX = scale * auraPulseScale, scaleY = scale * auraPulseScale, alpha = alpha * auraPulseAlpha)
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 36.dp, bottomEnd = 36.dp, topEnd = 12.dp, bottomStart = 12.dp))
                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.4f))
            )

            Surface(
                modifier = Modifier
                    .graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(topStart = 32.dp, bottomEnd = 32.dp, topEnd = 10.dp, bottomStart = 10.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.6f)),
                tonalElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(28.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.Warning,
                                    contentDescription = "Danger",
                                    tint = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Text(
                            text = "Delete Entry?",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "This action cannot be undone. All associated ledger records will be permanently removed.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        ExpressiveButton(
                            onClick = onConfirm,
                            backgroundColor = MaterialTheme.colorScheme.error
                        ) {
                            Text("Delete", fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SalarySwipeDeleteBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.errorContainer),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier.padding(end = 24.dp), tint = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Composable
fun HistoryBottomSheet(
    entries: List<SalaryEntry>,
    isPrivacyMode: Boolean = false,
    onEdit: (SalaryEntry) -> Unit,
    onDelete: (SalaryEntry) -> Unit,
    themeSettings: ThemeSettings
) {
    val haptic = LocalHapticFeedback.current

    var animateIn by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { animateIn = true }

    val slideY by animateDpAsState(
        targetValue = if (animateIn) 0.dp else 100.dp,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow),
        label = "sheet_slide_in"
    )
    val sheetAlpha by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0f,
        animationSpec = tween(300),
        label = "sheet_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(translationY = slideY.value, alpha = sheetAlpha)
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val strings = getAppStrings(themeSettings.appLanguage)
            Text(
                text = strings.salaryHistory,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "${entries.size} Records",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(
                items = entries,
                key = { it.id }
            ) { entry ->
                val swipeState = rememberSwipeToDismissBoxState()
                
                LaunchedEffect(swipeState.currentValue) {
                    if (swipeState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onDelete(entry)
                        swipeState.snapTo(SwipeToDismissBoxValue.Settled)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp, horizontal = 2.dp)
                ) {
                    SwipeToDismissBox(
                        state = swipeState,
                        enableDismissFromStartToEnd = false,
                        modifier = Modifier.fillMaxWidth(),
                        backgroundContent = {
                            if (swipeState.dismissDirection != SwipeToDismissBoxValue.Settled) {
                                SalarySwipeDeleteBackground()
                            }
                        },
                        content = {
                            ExpressiveSalaryCard(
                                entry = entry,
                                isPrivacyMode = isPrivacyMode,
                                onEdit = { onEdit(entry) },
                                onDelete = { onDelete(entry) },
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                themeSettings = themeSettings
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InteractiveAnalyticsChart(
    entries: List<SalaryEntry>,
    isPrivacyMode: Boolean,
    themeSettings: ThemeSettings,
    onMonthSelected: (SalaryEntry) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val last6 = remember(entries) {
        entries.sortedWith(compareBy<SalaryEntry> { it.year }.thenBy { it.month }).takeLast(6)
    }

    if (last6.isEmpty()) return

    val maxAmount = remember(last6) {
        last6.maxOfOrNull { it.salaryAmount }?.coerceAtLeast(1L) ?: 1L
    }

    var selectedIndex by remember { mutableStateOf(-1) }

    val scaleAnims = last6.mapIndexed { idx, _ ->
        var trigger by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(idx * 75L)
            trigger = true
        }
        animateFloatAsState(
            targetValue = if (trigger) 1f else 0f,
            animationSpec = spring(
                stiffness = 80f,
                dampingRatio = 0.45f
            ),
            label = "bar_scale_$idx"
        )
    }

    Surface(
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
        shadowElevation = 0.dp,
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            val strings = getAppStrings(themeSettings.appLanguage)
            val primaryColor = MaterialTheme.colorScheme.primary
            val tertiaryColor = MaterialTheme.colorScheme.tertiary

            // Header + Active Selected Month Tooltip Badge
            val activeIdx = if (selectedIndex in last6.indices) selectedIndex else last6.lastIndex
            val activeEntry = last6.getOrNull(activeIdx)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.incomeVsSavingsTrend,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = primaryColor,
                    letterSpacing = 1.8.sp
                )

                if (activeEntry != null) {
                    val savingsPercent = if (activeEntry.salaryAmount > 0) {
                        ((activeEntry.savingsAmount.toDouble() / activeEntry.salaryAmount.toDouble()) * 100).toInt().coerceIn(0, 100)
                    } else 0

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${DateUtils.getShortMonthName(activeEntry.month)}: ",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                            Text(
                                text = if (isPrivacyMode) "••••" else "${CurrencyUtils.formatAmount(activeEntry.salaryAmount, themeSettings.currencySymbol, isPrivacyMode, compact = true)} / ${savingsPercent}% ${strings.saved}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp,
                                color = primaryColor
                            )
                        }
                    }
                }
            }

            // Canvas Chart (Capsule Pills & Expressive Motion)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val padding = 10.dp.toPx()
                    val chartWidth = size.width
                    val chartHeight = size.height
                    val barGroupCount = last6.size
                    val groupWidth = chartWidth / barGroupCount
                    val barWidth = (groupWidth * 0.28f).coerceAtMost(22.dp.toPx())
                    val gap = 6.dp.toPx()

                    last6.forEachIndexed { idx, entry ->
                        val groupCenterX = idx * groupWidth + (groupWidth / 2)
                        val scale = scaleAnims[idx].value

                        val rawSalaryHeight = (entry.salaryAmount.toFloat() / maxAmount.toFloat()) * (chartHeight - padding * 2) * scale
                        val rawSavingsHeight = (entry.savingsAmount.toFloat() / maxAmount.toFloat()) * (chartHeight - padding * 2) * scale

                        val minPillHeight = barWidth
                        val salaryHeight = rawSalaryHeight.coerceAtLeast(minPillHeight)
                        val savingsHeight = if (entry.savingsAmount > 0) rawSavingsHeight.coerceAtLeast(minPillHeight) else minPillHeight

                        // Coordinates
                        val salaryX = groupCenterX - barWidth - gap / 2
                        val savingsX = groupCenterX + gap / 2

                        val salaryY = chartHeight - padding - salaryHeight
                        val savingsY = chartHeight - padding - savingsHeight

                        // Selection & Color Tokens
                        val isSelected = activeIdx == idx
                        val salaryBarColor = if (isSelected) primaryColor else primaryColor.copy(alpha = 0.55f)
                        val savingsBarColor = if (entry.savingsAmount > 0) {
                            if (isSelected) tertiaryColor else tertiaryColor.copy(alpha = 0.55f)
                        } else {
                            tertiaryColor.copy(alpha = 0.2f) // Clean zero-savings placeholder cap
                        }

                        val capsuleCorner = androidx.compose.ui.geometry.CornerRadius(barWidth / 2f, barWidth / 2f)

                        // Draw Salary Capsule Bar
                        drawRoundRect(
                            color = salaryBarColor,
                            topLeft = Offset(salaryX, salaryY),
                            size = androidx.compose.ui.geometry.Size(barWidth, salaryHeight),
                            cornerRadius = capsuleCorner
                        )

                        // Draw Savings Capsule Bar
                        drawRoundRect(
                            color = savingsBarColor,
                            topLeft = Offset(savingsX, savingsY),
                            size = androidx.compose.ui.geometry.Size(barWidth, savingsHeight),
                            cornerRadius = capsuleCorner
                        )
                    }
                }

                // Overlay Row to capture tap interactions
                Row(modifier = Modifier.fillMaxSize()) {
                    last6.forEachIndexed { idx, entry ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedIndex = idx
                                    onMonthSelected(entry)
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Month Labels Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                last6.forEachIndexed { idx, entry ->
                    val isSelected = activeIdx == idx
                    val chartMonthLocale = if (themeSettings.appLanguage == com.personal.kakeibox.data.preferences.AppLanguage.JAPANESE) java.util.Locale.JAPAN else java.util.Locale.ENGLISH
                    Text(
                        text = DateUtils.getShortMonthName(entry.month, chartMonthLocale),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                        color = if (isSelected) primaryColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // M3 Legend
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(8.dp).background(primaryColor, CircleShape))
                Spacer(modifier = Modifier.width(6.dp))
                Text(strings.income, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(20.dp))
                Box(modifier = Modifier.size(8.dp).background(tertiaryColor, CircleShape))
                Spacer(modifier = Modifier.width(6.dp))
                Text(strings.totalSavings, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}



fun Color.harmonizeWith(primary: Color): Color {
    return androidx.compose.ui.graphics.lerp(this, primary, 0.15f)
}

class ExpressiveTouchIndication(private val bloomColor: Color) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return object : Modifier.Node(), DrawModifierNode {
            private var isPressed by androidx.compose.runtime.mutableStateOf(false)

            override fun onAttach() {
                super.onAttach()
                coroutineScope.launch {
                    interactionSource.interactions.collect { interaction ->
                        when (interaction) {
                            is PressInteraction.Press -> isPressed = true
                            is PressInteraction.Release,
                            is PressInteraction.Cancel -> isPressed = false
                        }
                    }
                }
            }

            override fun ContentDrawScope.draw() {
                drawContent()
                if (isPressed) {
                    drawRect(
                        color = bloomColor.copy(alpha = 0.15f),
                        size = size
                    )
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExpressiveTouchIndication) return false
        return bloomColor == other.bloomColor
    }

    override fun hashCode(): Int = bloomColor.hashCode()
}

fun Modifier.expressiveClickable(
    interactionSource: androidx.compose.foundation.interaction.MutableInteractionSource,
    onClick: () -> Unit
): Modifier = composed {
    val isPressed = interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed.value) 0.98f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "click_scale"
    )
    val primaryColor = MaterialTheme.colorScheme.primary
    val expressiveIndication = remember(primaryColor) { ExpressiveTouchIndication(primaryColor) }
    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = expressiveIndication,
            onClick = onClick
        )
}

// Helper to convert KMP Morph to Compose Path
fun androidx.graphics.shapes.Morph.toComposePath(progress: Float): androidx.compose.ui.graphics.Path {
    val path = androidx.compose.ui.graphics.Path()
    var first = true
    this.forEachCubic(progress) { cubic ->
        if (first) {
            path.moveTo(cubic.anchor0X, cubic.anchor0Y)
            first = false
        }
        path.cubicTo(
            cubic.control0X, cubic.control0Y,
            cubic.control1X, cubic.control1Y,
            cubic.anchor1X, cubic.anchor1Y
        )
    }
    path.close()
    return path
}
