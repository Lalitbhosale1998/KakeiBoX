package com.personal.kakeibox.ui.salary

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import com.personal.kakeibox.ui.salary.SalaryUiState
import com.personal.kakeibox.ui.salary.SalaryViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.R
import com.personal.kakeibox.data.entity.SalaryEntry
import com.personal.kakeibox.data.preferences.ThemeSettings
import com.personal.kakeibox.util.CurrencyUtils
import com.personal.kakeibox.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryScreen(
    viewModel: SalaryViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val haptic = LocalHapticFeedback.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val themeSettings by themeViewModel.themeSettings.collectAsStateWithLifecycle()
    val allEntries by viewModel.allEntries.collectAsStateWithLifecycle()
    val currentEntry by viewModel.currentEntry.collectAsStateWithLifecycle()
    val totalSavings by viewModel.totalSavings.collectAsStateWithLifecycle()
    val totalSalary by viewModel.totalSalary.collectAsStateWithLifecycle()
    val totalRemittance by viewModel.totalRemittance.collectAsStateWithLifecycle()
    
    val isFloatingNav = themeSettings.navBarStyle == NavBarStyle.FLOATING || themeSettings.navBarStyle == NavBarStyle.SPLIT
    val fabPadding by animateDpAsState(
        targetValue = if (isFloatingNav) 100.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "fab_padding"
    )

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
            TopAppBarBackground.SURFACE -> MaterialTheme.colorScheme.surface
            TopAppBarBackground.PRIMARY_CONTAINER -> MaterialTheme.colorScheme.primaryContainer
        },
        label = "top_app_bar_container_color"
    )

    val bentoIdleColor = if (isPrimaryContainer) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceContainer

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

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            // Snappier 2-second timeout for Expressive Snackbars
            withTimeoutOrNull(2000L) {
                snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Indefinite)
            }
            viewModel.clearSnackbar()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = topAppBarContainerColor,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {},
            snackbarHost = { ExpressiveSnackbarHost(snackbarHostState) },
            floatingActionButton = {
                // Large, Expressive FAB pushed up if Nav is floating
                val haptic = LocalHapticFeedback.current
                LargeFloatingActionButton(
                    onClick = { 
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.openAddDialog() 
                    },
                    modifier = Modifier.padding(bottom = fabPadding),
                    shape = RoundedCornerShape(28.dp),
                    containerColor = if (isPrimaryContainer) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (isPrimaryContainer) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add Entry",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
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
                // Header spacer to place list items under collapsible header
                item {
                    Spacer(modifier = Modifier.height(150.dp + statusBarPadding))
                }
                // ── Hero Section ──────────────
                item {
                    AnimatedVisibility(
                        visible = showHero,
                        enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                    ) {
                        ExpressiveHeroCard(
                            totalSalary = totalSalary ?: 0L,
                            totalSavings = totalSavings ?: 0L,
                            currentEntry = currentEntry,
                            currentMonth = uiState.currentMonth,
                            currentYear = uiState.currentYear,
                            isPrivacyMode = themeSettings.privacyModeEnabled,
                            onEdit = { currentEntry?.let { viewModel.openEditDialog(it) } },
                            isPrimaryContainer = isPrimaryContainer,
                            themeSettings = themeSettings,
                            donutMode = uiState.donutMode,
                            onDonutClick = viewModel::toggleDonutMode
                        )
                    }
                }

                // ── Detailed Stats ───────────
                item {
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
                }

                // ── History Header & Filters ──
                item {
                    AnimatedVisibility(
                        visible = showHistory,
                        enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "History",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Black
                                )
                                TextButton(onClick = { viewModel.toggleHistorySheet() }) {
                                    Text("See All")
                                }
                            }
                            
                            // Contextual Chip Filtering (Material 3 Expressive Animated Weights)
                            val allWeight by animateFloatAsState(
                                targetValue = if (uiState.currentFilter == SalaryFilter.ALL) 1.6f else 0.8f,
                                animationSpec = spring(
                                    dampingRatio = 0.6f,
                                    stiffness = Spring.StiffnessLow
                                ),
                                label = "all_weight"
                            )
                            val yearWeight by animateFloatAsState(
                                targetValue = if (uiState.currentFilter == SalaryFilter.THIS_YEAR) 1.6f else 0.8f,
                                animationSpec = spring(
                                    dampingRatio = 0.6f,
                                    stiffness = Spring.StiffnessLow
                                ),
                                label = "year_weight"
                            )
                            val savingsWeight by animateFloatAsState(
                                targetValue = if (uiState.currentFilter == SalaryFilter.HIGH_SAVINGS) 1.6f else 0.8f,
                                animationSpec = spring(
                                    dampingRatio = 0.6f,
                                    stiffness = Spring.StiffnessLow
                                ),
                                label = "savings_weight"
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                ExpressiveChip(
                                    text = "All Time",
                                    isSelected = uiState.currentFilter == SalaryFilter.ALL,
                                    onClick = { viewModel.setFilter(SalaryFilter.ALL) },
                                    shapeType = "arch",
                                    modifier = Modifier.weight(allWeight)
                                )
                                ExpressiveChip(
                                    text = "This Year",
                                    isSelected = uiState.currentFilter == SalaryFilter.THIS_YEAR,
                                    onClick = { viewModel.setFilter(SalaryFilter.THIS_YEAR) },
                                    shapeType = "slanted",
                                    modifier = Modifier.weight(yearWeight)
                                )
                                ExpressiveChip(
                                    text = "High Savings",
                                    isSelected = uiState.currentFilter == SalaryFilter.HIGH_SAVINGS,
                                    onClick = { viewModel.setFilter(SalaryFilter.HIGH_SAVINGS) },
                                    shapeType = "clamshell",
                                    modifier = Modifier.weight(savingsWeight)
                                )
                            }
                        }
                    }
                }

                // ── History List (Organic Bento Flow) ──
                val filteredEntries = when (uiState.currentFilter) {
                    SalaryFilter.ALL -> allEntries
                    SalaryFilter.THIS_YEAR -> allEntries.filter { it.year == DateUtils.getCurrentYear() }
                    SalaryFilter.HIGH_SAVINGS -> allEntries.filter { 
                        it.salaryAmount > 0 && (it.savingsAmount.toFloat() / it.salaryAmount) >= 0.25f 
                    }
                }

                if (filteredEntries.isEmpty()) {
                    item {
                        AnimatedVisibility(
                            visible = showHistory,
                            enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                    slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                        ) {
                            ExpressiveEmptyState(
                                message = "No records found",
                                icon = "🔍",
                                color = onContainerColor
                            )
                        }
                    }
                } else {
                    val historyEntries = filteredEntries.take(6)
                    
                    item {
                        AnimatedVisibility(
                            visible = showHistory,
                            enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                    slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Organic Bento Flow Logic
                                // 1. First item is prominent
                                historyEntries.firstOrNull()?.let { first ->
                                    ExpressiveHistoryBentoBox(
                                        entry = first,
                                        isPrivacyMode = themeSettings.privacyModeEnabled,
                                        onEdit = { viewModel.openEditDialog(first) },
                                        modifier = Modifier.fillMaxWidth().height(140.dp),
                                        themeSettings = themeSettings,
                                        isLarge = true
                                    )
                                }
                                
                                // 2. Remaining items in pairs
                                val remaining = historyEntries.drop(1)
                                remaining.chunked(2).forEach { rowEntries ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        rowEntries.forEach { entry ->
                                            ExpressiveHistoryBentoBox(
                                                entry = entry,
                                                isPrivacyMode = themeSettings.privacyModeEnabled,
                                                onEdit = { viewModel.openEditDialog(entry) },
                                                modifier = Modifier.weight(1f),
                                                themeSettings = themeSettings
                                            )
                                        }
                                        if (rowEntries.size == 1) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        ExpressiveCollapsingHeader(
            title = "Monthly",
            subtitle = "Salary",
            scrollOffset = scrollOffset,
            maxOffset = maxOffsetPx,
            containerColor = topAppBarContainerColor,
            onContainerColor = onContainerColor,
            primaryTextAccent = primaryTextAccent,
            actions = {
                IconButton(
                    onClick = { 
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.toggleHistorySheet() 
                    }
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (isPrimaryContainer) MaterialTheme.colorScheme.surface.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Outlined.History,
                            contentDescription = "History",
                            modifier = Modifier.padding(8.dp),
                            tint = if (isPrimaryContainer) onContainerColor else MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                IconButton(
                    onClick = { 
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.addDummyData() 
                    }
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (isPrimaryContainer) MaterialTheme.colorScheme.surface.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.Upload,
                            contentDescription = "Add Dummy Data",
                            modifier = Modifier.padding(8.dp),
                            tint = if (isPrimaryContainer) onContainerColor else MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        )
    }

    // Sheets & Dialogs (Update to Tonal Backgrounds)
    if (uiState.showAddEditDialog) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeDialog() },
            containerColor = bentoIdleColor,
            dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant) },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
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

    if (uiState.showDeleteDialog) {
        ExpressiveDeleteDialog(
            onConfirm = viewModel::deleteEntry,
            onDismiss = viewModel::closeDeleteDialog
        )
    }

    if (uiState.showHistorySheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleHistorySheet() },
            containerColor = bentoIdleColor,
            dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant) },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
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
    onDonutClick: () -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = if (isPrimaryContainer) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f) else MaterialTheme.colorScheme.primary,
        contentColor = if (isPrimaryContainer) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onPrimary,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "TOTAL EARNINGS",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                ExpressiveTotalEarningsTicker(
                    totalSalary = totalSalary,
                    isPrivacyMode = isPrivacyMode,
                    themeSettings = themeSettings
                )

                Text(
                    text = "Cumulative Net Income",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
                
                if (currentEntry != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    FilledTonalButton(
                        onClick = { 
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onEdit() 
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f),
                            contentColor = MaterialTheme.colorScheme.onPrimary
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

            // ── Interactive Donut Engagement ─────────────────────
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
                    modifier = Modifier.size(100.dp).padding(4.dp)
                ) {
                    val trackColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f)
                    val progressColor = MaterialTheme.colorScheme.onPrimary
                    
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 10.dp.toPx()
                        drawArc(
                            color = trackColor,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
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
                                            fontWeight = FontWeight.Black
                                        )
                                        Text(
                                            text = "SAVED",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 8.sp
                                        )
                                    }
                                    DonutDisplayMode.ABSOLUTE -> {
                                        Text(
                                            text = CurrencyUtils.formatAmount(totalSavings, themeSettings.currencySymbol, isPrivacyMode, compact = true),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Black
                                        )
                                        Text(
                                            text = "TOTAL",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 8.sp
                                        )
                                    }
                                    DonutDisplayMode.REMAINING_DAYS -> {
                                        val today = LocalDate.now()
                                        val nextMonth = today.withDayOfMonth(1).plusMonths(1)
                                        val days = ChronoUnit.DAYS.between(today, nextMonth)
                                        Text(
                                            text = days.toString(),
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Black
                                        )
                                        Text(
                                            text = "DAYS LEFT",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 8.sp
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
fun ExpressiveStatsGrid(
    totalSavings: Long, 
    totalRemittance: Long, 
    isPrivacyMode: Boolean = false,
    onRemittanceClick: () -> Unit = {},
    bentoIdleColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    themeSettings: ThemeSettings
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BentoCard(
            title = "Total Savings",
            icon = Icons.Outlined.Savings,
            idleContainerColor = bentoIdleColor,
            idleContentColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = CurrencyUtils.formatAmount(totalSavings, themeSettings.currencySymbol, isPrivacyMode),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        BentoCard(
            title = "Total Remittance",
            icon = Icons.AutoMirrored.Outlined.ExitToApp,
            idleContainerColor = bentoIdleColor,
            idleContentColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
            onClick = onRemittanceClick
        ) {
            Text(
                text = CurrencyUtils.formatAmount(totalRemittance, themeSettings.currencySymbol, isPrivacyMode),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
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
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        color = if (isLarge) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceContainerHigh,
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onEdit()
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isLarge) Arrangement.SpaceBetween else Arrangement.Center
        ) {
            Column(
                horizontalAlignment = if (isLarge) Alignment.Start else Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = if (isLarge) Modifier.weight(1f) else Modifier
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = DateUtils.getShortMonthName(entry.month).uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = CurrencyUtils.formatAmount(entry.salaryAmount, themeSettings.currencySymbol, isPrivacyMode),
                    style = if (isLarge) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                val savingsRatio = if (entry.salaryAmount > 0) 
                    (entry.savingsAmount.toFloat() / entry.salaryAmount.toFloat())
                else 0f
                val savingsPercent = (savingsRatio * 100).toInt()
                
                val animatedSavingsProgress by animateFloatAsState(
                    targetValue = savingsRatio,
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    label = "bento_progress"
                )

                Column(
                    modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
                    horizontalAlignment = if (isLarge) Alignment.Start else Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(if (isLarge) 0.85f else 0.9f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Savings",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$savingsPercent%",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Black
                        )
                    }
                    
                    // Premium Bento Progress Bar
                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .fillMaxWidth(if (isLarge) 0.85f else 0.9f)
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedSavingsProgress.coerceAtLeast(0.01f))
                                .fillMaxHeight()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                                    ),
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
            
            if (isLarge) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(12.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
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
                        Text(
                            text = DateUtils.getShortMonthName(entry.month).uppercase(),
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
            Button(
                onClick = onAdd,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(20.dp)
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
    themeSettings: ThemeSettings
) {
    val haptic = LocalHapticFeedback.current
    val formattedTotal = CurrencyUtils.formatAmount(totalSalary, themeSettings.currencySymbol, isPrivacyMode)
    
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isPrivacyMode) {
            Text(
                text = formattedTotal,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black
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
                            fadeIn(animationSpec = tween(150))
                                .togetherWith(fadeOut(animationSpec = tween(150)))
                        }
                    },
                    label = "digit_ticker_$index"
                ) { targetChar ->
                    Text(
                        text = targetChar.toString(),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
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
    var showNoteField by remember { mutableStateOf(uiState.inputNote.isNotBlank()) }

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
            .imePadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (uiState.editingEntry == null) "Add Salary" else "Edit Salary",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black
            )
            
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHighest, CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // 1. Period Island (Bento Selection)
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Pay Period",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
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
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 2. Hero Amount Island (Bento Card, Focused Scaling)
        val salaryElevation by animateDpAsState(if (isSalaryFocused) 12.dp else 0.dp)
        val salaryScale by animateFloatAsState(if (isSalaryFocused) 1.04f else 1f)
        
        Surface(
            color = if (isSalaryFocused) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(28.dp),
            shadowElevation = salaryElevation,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(scaleX = salaryScale, scaleY = salaryScale)
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "TOTAL EARNINGS",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSalaryFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp
                )
                
                BasicTextField(
                    value = uiState.inputSalary,
                    onValueChange = onSalaryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .onFocusChanged { 
                            if (it.isFocused != isSalaryFocused) {
                                isSalaryFocused = it.isFocused 
                                if (it.isFocused) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        },
                    textStyle = MaterialTheme.typography.displayLarge.copy(
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
                                color = if (isSalaryFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(contentAlignment = Alignment.Center) {
                                if (uiState.inputSalary.isEmpty()) {
                                    Text(
                                        text = "0",
                                        style = MaterialTheme.typography.displayLarge,
                                        fontWeight = FontWeight.Black,
                                        color = if (isSalaryFocused) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f) 
                                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    )
                                }
                                innerTextField()
                            }
                        }
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

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
                        text = "Net Remaining: ${themeSettings.currencySymbol}$remainingText",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (remaining >= 0) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        // Bento Island for Allocations (Savings & Remittance & Notes)
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Allocations",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ExpressiveOutlinedTextField(
                        value = uiState.inputSavings,
                        onValueChange = onSavingsChange,
                        label = { Text("Savings") },
                        modifier = Modifier.weight(1f),
                        leadingIcon = { Icon(Icons.Outlined.Savings, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    ExpressiveOutlinedTextField(
                        value = uiState.inputRemittance,
                        onValueChange = onRemittanceChange,
                        label = { Text("Remittance") },
                        modifier = Modifier.weight(1f),
                        leadingIcon = { Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }

                // Expanding Note Drawer (Merged into Allocations Card for space efficiency)
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
                            label = { Text("Notes (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = { Icon(Icons.Outlined.NoteAlt, contentDescription = null) },
                            placeholder = { Text("Bonus, overtime, etc.") }
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
                        Text("Add Note", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        val isInputValid = uiState.inputSalary.isNotBlank() && uiState.inputSalary.toDoubleOrNull() != null
        
        Button(
            onClick = {
                if (isInputValid) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSave()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            enabled = isInputValid,
            shape = RoundedCornerShape(28.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isInputValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isInputValid) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
            )
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
                        if (valid) "Confirm Entry" else "Enter Salary to Save",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
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

        Surface(
            modifier = Modifier
                .graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(28.dp)) {
                Text(
                    text = "Delete Entry?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "This action cannot be undone.",
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
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Delete", fontWeight = FontWeight.ExtraBold)
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
            Text(
                text = stringResource(R.string.history),
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

                SwipeToDismissBox(
                    state = swipeState,
                    enableDismissFromStartToEnd = false,
                    backgroundContent = {
                        SalarySwipeDeleteBackground()
                    },
                    content = {
                        ExpressiveSalaryCard(
                            entry = entry,
                            isPrivacyMode = isPrivacyMode,
                            onEdit = { onEdit(entry) },
                            onDelete = { onDelete(entry) },
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            themeSettings = themeSettings
                        )
                    }
                )
            }
        }
    }
}
