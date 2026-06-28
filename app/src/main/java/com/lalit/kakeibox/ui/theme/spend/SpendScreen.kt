package com.personal.kakeibox.ui.spend

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import com.personal.kakeibox.data.preferences.TopAppBarBackground
import com.personal.kakeibox.ui.settings.ThemeViewModel
import com.personal.kakeibox.ui.settings.BirthdayManagementContent
import com.personal.kakeibox.data.preferences.NavBarStyle
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import com.personal.kakeibox.ui.components.ExpressiveCategoryToggle
import com.personal.kakeibox.ui.components.ExpressiveEmptyState
import com.personal.kakeibox.ui.components.BentoCard
import com.personal.kakeibox.ui.components.ExpressiveTab
import com.personal.kakeibox.ui.components.ExpressiveSnackbarHost
import com.personal.kakeibox.ui.components.ExpressiveButton
import com.personal.kakeibox.ui.theme.LocalThemeStyle
import com.personal.kakeibox.data.preferences.ThemeStyle
import com.personal.kakeibox.ui.theme.LocalGlowIntensity
import com.personal.kakeibox.ui.theme.glow
import com.personal.kakeibox.ui.theme.expressiveBackground
import com.personal.kakeibox.data.preferences.BackdropPattern
import androidx.compose.foundation.isSystemInDarkTheme
import com.personal.kakeibox.data.preferences.GlowIntensity
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.lazy.rememberLazyListState
import com.personal.kakeibox.ui.components.ExpressiveCollapsingHeader
import com.personal.kakeibox.ui.components.ExpressiveOutlinedTextField
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.R
import com.personal.kakeibox.data.entity.SalaryEntry
import com.personal.kakeibox.data.entity.SpendCategory
import com.personal.kakeibox.data.entity.SpendEntry
import com.personal.kakeibox.data.preferences.ThemeSettings
import com.personal.kakeibox.util.CurrencyUtils
import com.personal.kakeibox.util.DateUtils
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.focus.onFocusChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendScreen(
    viewModel: SpendViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(LocalActivity.current as ComponentActivity)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val themeSettings by themeViewModel.themeSettings.collectAsStateWithLifecycle()
    val currentMonthEntries by viewModel.currentMonthEntries.collectAsStateWithLifecycle()
    val allEntries by viewModel.allEntries.collectAsStateWithLifecycle()
    val totalNeed by viewModel.totalNeedThisMonth.collectAsStateWithLifecycle()
    val totalWant by viewModel.totalWantThisMonth.collectAsStateWithLifecycle()
    val totalSpend by viewModel.totalSpendThisMonth.collectAsStateWithLifecycle()
    val totalSpendAllTime by viewModel.totalSpendAllTime.collectAsStateWithLifecycle()
    val salary by viewModel.currentSalary.collectAsStateWithLifecycle()

    val isFloatingNav = themeSettings.navBarStyle == NavBarStyle.FLOATING || themeSettings.navBarStyle == NavBarStyle.SPLIT
    val fabPadding by animateDpAsState(
        targetValue = if (isFloatingNav) 100.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "fab_padding"
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val historyBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
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

    var isExpanded by remember { mutableStateOf(true) }
    var lastScrollIndex by remember { mutableStateOf(0) }
    var lastScrollOffset by remember { mutableStateOf(0) }

    LaunchedEffect(lazyListState.firstVisibleItemIndex, lazyListState.firstVisibleItemScrollOffset) {
        val currentIndex = lazyListState.firstVisibleItemIndex
        val currentOffset = lazyListState.firstVisibleItemScrollOffset
        
        if (currentIndex == 0 && currentOffset == 0) {
            isExpanded = true
        } else if (currentIndex > lastScrollIndex || (currentIndex == lastScrollIndex && currentOffset > lastScrollOffset)) {
            isExpanded = false
        } else if (currentIndex < lastScrollIndex || (currentIndex == lastScrollIndex && currentOffset < lastScrollOffset)) {
            isExpanded = true
        }
        
        lastScrollIndex = currentIndex
        lastScrollOffset = currentOffset
    }

    val fabWidth by animateDpAsState(
        targetValue = if (isExpanded) 154.dp else 64.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "fab_width"
    )

    val isPrimaryContainer = themeSettings.topAppBarBackground == TopAppBarBackground.PRIMARY_CONTAINER
    val onContainerColor = if (isPrimaryContainer) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    val primaryTextAccent = if (isPrimaryContainer) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary
    val bentoIdleColor by animateColorAsState(
        targetValue = if (isPrimaryContainer) {
            androidx.compose.ui.graphics.lerp(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.primaryContainer,
                0.20f // 20% tint overlay for soft blending
            )
        } else {
            MaterialTheme.colorScheme.surfaceContainer
        },
        label = "bento_idle_color"
    )

    val currentColorScheme = MaterialTheme.colorScheme
    val sheetColorScheme = if (isPrimaryContainer) {
        val blendedSurface = androidx.compose.ui.graphics.lerp(
            currentColorScheme.surface,
            currentColorScheme.primaryContainer,
            0.20f
        )
        val blendedSurfaceHigh = androidx.compose.ui.graphics.lerp(
            currentColorScheme.surfaceContainerHigh,
            currentColorScheme.primaryContainer,
            0.20f
        )
        val blendedSurfaceLow = androidx.compose.ui.graphics.lerp(
            currentColorScheme.surfaceContainerLow,
            currentColorScheme.primaryContainer,
            0.20f
        )
        val blendedSurfaceLowest = androidx.compose.ui.graphics.lerp(
            currentColorScheme.surfaceContainerLowest,
            currentColorScheme.primaryContainer,
            0.20f
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

    val topAppBarContainerColor by animateColorAsState(
        targetValue = when (themeSettings.topAppBarBackground) {
            TopAppBarBackground.SURFACE -> MaterialTheme.colorScheme.surface
            TopAppBarBackground.PRIMARY_CONTAINER -> androidx.compose.ui.graphics.lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.primaryContainer, 0.35f)
        },
        label = "top_app_bar_container_color"
    )

    val filteredEntries = remember(uiState.selectedCategory, currentMonthEntries) {
        when (uiState.selectedCategory) {
            SpendCategory.NEED -> currentMonthEntries.filter { it.category == SpendCategory.NEED }
            SpendCategory.WANT -> currentMonthEntries.filter { it.category == SpendCategory.WANT }
            null -> currentMonthEntries
        }
    }

    val haptic = LocalHapticFeedback.current

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            withTimeoutOrNull(2000L) {
                snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Indefinite)
            }
            viewModel.clearSnackbar()
        }
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .expressiveBackground(
                isDark = isSystemInDarkTheme(),
                isPrimaryContainer = isPrimaryContainer,
                primaryColor = MaterialTheme.colorScheme.primary,
                containerColor = topAppBarContainerColor,
                pattern = themeSettings.backdropPattern
            )
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {},
            floatingActionButton = {
                Surface(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.openAddSheet()
                    },
                    modifier = Modifier
                        .padding(bottom = fabPadding)
                        .size(width = fabWidth, height = 64.dp),
                    shape = RoundedCornerShape(32.dp),
                    color = if (isPrimaryContainer) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (isPrimaryContainer) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer,
                    shadowElevation = 8.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "Add Spend",
                                modifier = Modifier.size(28.dp)
                            )
                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                        expandHorizontally(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy), expandFrom = Alignment.Start),
                                exit = fadeOut(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                       shrinkHorizontally(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy), shrinkTowards = Alignment.Start)
                            ) {
                                Text(
                                    text = "Add Spend",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.padding(start = 8.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Clip
                                )
                            }
                        }
                    }
                }
            },
            snackbarHost = { ExpressiveSnackbarHost(snackbarHostState) }
        ) { innerPadding ->

            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp, 
                    end = 16.dp, 
                    top = innerPadding.calculateTopPadding() + 8.dp,
                    bottom = innerPadding.calculateBottomPadding() + 80.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(150.dp + statusBarPadding))
                }
            // ── Bento Box Hero Grid ──────────────────────
            item {
                AnimatedVisibility(
                    visible = showHero,
                    enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                            slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                ) {
                    BentoHeroSection(
                        totalSpend = totalSpendAllTime ?: 0L,
                        totalNeed = totalNeed,
                        totalWant = totalWant,
                        salary = salary,
                        currentMonth = uiState.currentMonth,
                        currentYear = uiState.currentYear,
                        isPrivacyMode = themeSettings.privacyModeEnabled,
                        onPeriodClick = { /* Scroll to top or show picker if needed */ },
                        isPrimaryContainer = isPrimaryContainer,
                        bentoIdleColor = bentoIdleColor,
                        themeSettings = themeSettings,
                        selectedCategory = uiState.selectedCategory
                    )
                }
            }

            // Period Navigation Island removed


            // ── Budget Health Bar ────────────────────────
            item {
                AnimatedVisibility(
                    visible = (salary?.salaryAmount ?: 0L) > 0 && showStats,
                    enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                            slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 },
                    exit = shrinkVertically() + fadeOut()
                ) {
                    BudgetHealthBeam(
                        totalNeed = totalNeed,
                        totalWant = totalWant,
                        totalSpend = totalSpend,
                        salaryAmount = salary?.salaryAmount ?: 0L,
                        bentoIdleColor = bentoIdleColor
                    )
                }
            }

            // ── Category Tabs ─────────────────────────────
            item {
                AnimatedVisibility(
                    visible = showHistory,
                    enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                            slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                ) {
                    ExpressiveCategoryTabs(
                        selectedCategory = uiState.selectedCategory,
                        onSelectAll = { 
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            viewModel.setFilter(null) 
                        },
                        onSelectNeed = { 
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            viewModel.setFilter(SpendCategory.NEED) 
                        },
                        onSelectWant = { 
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            viewModel.setFilter(SpendCategory.WANT) 
                        }
                    )
                }
            }

            // ── List Items ───────────────────────────────
            if (filteredEntries.isEmpty()) {
                item {
                    AnimatedVisibility(
                        visible = showHistory,
                        enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                    ) {
                        ExpressiveEmptyState(
                            message = if (uiState.selectedCategory != null) "No ${uiState.selectedCategory} logs" else "No spending yet",
                            icon = if (uiState.selectedCategory == SpendCategory.NEED) "🛡️" else "✨",
                            color = onContainerColor
                        )
                    }
                }
            } else {
                items(items = filteredEntries, key = { it.id }) { entry ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.deleteEntryDirectly(entry); true
                            } else false
                        }
                    )
                    val haptic = LocalHapticFeedback.current
                    var isPressed by remember { mutableStateOf(false) }
                    val liftScale by animateFloatAsState(
                        targetValue = if (isPressed) 1.05f else 1f,
                        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
                        label = "lift_scale"
                    )
                    val liftElevation by animateDpAsState(
                        targetValue = if (isPressed) 12.dp else 0.dp,
                        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
                        label = "lift_elevation"
                    )

                    AnimatedVisibility(
                        visible = showHistory,
                        enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                    ) {
                        SwipeToDismissBox(
                            state = dismissState,
                            modifier = Modifier
                                .animateItem()
                                .graphicsLayer {
                                    scaleX = liftScale
                                    scaleY = liftScale
                                    shadowElevation = liftElevation.toPx()
                                    clip = false
                                    shape = RoundedCornerShape(28.dp)
                                }
                                .pointerInput(Unit) {
                                    awaitPointerEventScope {
                                        while (true) {
                                            val event = awaitPointerEvent()
                                            if (event.type == PointerEventType.Press) isPressed = true
                                            if (event.type == PointerEventType.Release || event.type == PointerEventType.Exit) isPressed = false
                                        }
                                    }
                                },
                            enableDismissFromStartToEnd = false,
                            backgroundContent = { SpendSwipeDeleteBackground() }
                        ) {
                            ExpressiveListItem(
                                entry = entry,
                                isPrivacyMode = themeSettings.privacyModeEnabled,
                                onEdit = { viewModel.openEditSheet(entry) },
                                onDelete = { viewModel.openDeleteDialog(entry) },
                                containerColor = bentoIdleColor,
                                themeSettings = themeSettings
                            )
                        }
                    }
                }
            }
        }
    }

    ExpressiveCollapsingHeader(
            title = "Monthly",
            subtitle = "Spending",
            scrollOffset = scrollOffset,
            maxOffset = maxOffsetPx,
            containerColor = topAppBarContainerColor,
            onContainerColor = onContainerColor,
            primaryTextAccent = primaryTextAccent,
            actions = {
                IconButton(onClick = { 
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    themeViewModel.toggleBirthdaySheet(true) 
                }) {
                    Surface(
                        shape = CircleShape,
                        color = if (isPrimaryContainer) MaterialTheme.colorScheme.surface.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Cake, 
                            contentDescription = "Birthdays Hub", 
                            modifier = Modifier.padding(8.dp),
                            tint = if (isPrimaryContainer) onContainerColor else MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                IconButton(onClick = { 
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.toggleHistorySheet() 
                }) {
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
            }
        )
    }

    // Sheets & Dialogs
    if (uiState.showAddEditSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeSheet() },
            sheetState = bottomSheetState,
            containerColor = sheetColorScheme.surfaceContainer,
            dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant) },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            MaterialTheme(colorScheme = sheetColorScheme) {
                SpendAddEditSheet(
                    uiState = uiState,
                    themeSettings = themeSettings,
                    onDescriptionChange = viewModel::updateDescription,
                    onAmountChange = viewModel::updateAmount,
                    onCategoryChange = viewModel::updateCategory,
                    onNoteChange = viewModel::updateNote,
                    onMonthChange = viewModel::updateMonth,
                    onYearChange = viewModel::updateYear,
                    onSave = viewModel::saveEntry,
                    onDismiss = viewModel::closeSheet
                )
            }
        }
    }

    if (uiState.showDeleteDialog) {
        SpendDeleteDialog(uiState.deletingEntry, viewModel::deleteEntry, viewModel::closeDeleteDialog)
    }

    if (uiState.showHistorySheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleHistorySheet() },
            sheetState = historyBottomSheetState,
            containerColor = sheetColorScheme.surfaceContainer,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            MaterialTheme(colorScheme = sheetColorScheme) {
                SpendHistoryBottomSheet(
                    entries = allEntries,
                    isPrivacyMode = themeSettings.privacyModeEnabled,
                    onEdit = viewModel::openEditSheet,
                    onDelete = viewModel::openDeleteDialog,
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
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .width(32.dp)
                        .height(4.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant, CircleShape)
                )
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

// ── UNIQUE COMPONENTS ───────────────────────────────────────

@Composable
fun BentoHeroSection(
    totalSpend: Long,
    totalNeed: Long,
    totalWant: Long,
    salary: SalaryEntry?,
    currentMonth: Int,
    currentYear: Int,
    isPrivacyMode: Boolean = false,
    onPeriodClick: () -> Unit,
    isPrimaryContainer: Boolean = false,
    bentoIdleColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    themeSettings: ThemeSettings,
    selectedCategory: SpendCategory? = null
) {
    val salaryAmount = salary?.salaryAmount ?: 0L
    val remaining = salaryAmount - totalSpend

    val leftCardWeight by animateFloatAsState(
        targetValue = if (selectedCategory == null) 1.2f else 0.001f,
        animationSpec = spring(
            dampingRatio = 0.55f,
            stiffness = 300f
        ),
        label = "left_card_weight"
    )

    val rightColWeight by animateFloatAsState(
        targetValue = if (selectedCategory == null) 1.0f else 2.0f,
        animationSpec = spring(
            dampingRatio = 0.55f,
            stiffness = 300f
        ),
        label = "right_col_weight"
    )

    val needCardWeight by animateFloatAsState(
        targetValue = when (selectedCategory) {
            SpendCategory.NEED -> 2.0f
            SpendCategory.WANT -> 0.001f
            null -> 1.0f
        },
        animationSpec = spring(
            dampingRatio = 0.55f,
            stiffness = 300f
        ),
        label = "need_card_weight"
    )

    val wantCardWeight by animateFloatAsState(
        targetValue = when (selectedCategory) {
            SpendCategory.WANT -> 2.0f
            SpendCategory.NEED -> 0.001f
            null -> 1.0f
        },
        animationSpec = spring(
            dampingRatio = 0.55f,
            stiffness = 300f
        ),
        label = "want_card_weight"
    )
    
    Row(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(leftCardWeight.coerceAtLeast(0.001f))
                .fillMaxHeight()
                .graphicsLayer {
                    alpha = ((leftCardWeight - 0.1f) / 1.1f).coerceIn(0f, 1f)
                    scaleX = (leftCardWeight / 1.2f).coerceIn(0f, 1f)
                    scaleY = (leftCardWeight / 1.2f).coerceIn(0f, 1f)
                }
        ) {
            BentoCard(
                modifier = Modifier.fillMaxSize(),
                title = "TOTAL SPENT",
                icon = Icons.Outlined.AccountBalanceWallet,
                isActive = true,
                activeContainerColor = if (isPrimaryContainer) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f) else MaterialTheme.colorScheme.primary,
                activeContentColor = if (isPrimaryContainer) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onPrimary,
                onClick = onPeriodClick
            ) {
                Column {
                    Text(
                        text = "Cumulative",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        color = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ExpressiveTotalSpentTicker(
                        totalSpend = totalSpend,
                        isPrivacyMode = isPrivacyMode,
                        themeSettings = themeSettings
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(rightColWeight.coerceAtLeast(0.001f))
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(needCardWeight.coerceAtLeast(0.001f))
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = ((needCardWeight - 0.1f) / 1.9f).coerceIn(0f, 1f)
                            scaleX = if (selectedCategory == SpendCategory.WANT) (needCardWeight / 1f).coerceIn(0f, 1f) else 1.0f
                            scaleY = if (selectedCategory == SpendCategory.WANT) (needCardWeight / 1f).coerceIn(0f, 1f) else 1.0f
                        }
                ) {
                    BentoStatSmall(
                        title = "Needs",
                        amount = totalNeed,
                        icon = Icons.Outlined.ShoppingBag,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        isPrivacyMode = isPrivacyMode,
                        modifier = Modifier.fillMaxSize(),
                        themeSettings = themeSettings
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(wantCardWeight.coerceAtLeast(0.001f))
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = ((wantCardWeight - 0.1f) / 1.9f).coerceIn(0f, 1f)
                            scaleX = if (selectedCategory == SpendCategory.NEED) (wantCardWeight / 1f).coerceIn(0f, 1f) else 1.0f
                            scaleY = if (selectedCategory == SpendCategory.NEED) (wantCardWeight / 1f).coerceIn(0f, 1f) else 1.0f
                        }
                ) {
                    BentoStatSmall(
                        title = "Wants",
                        amount = totalWant,
                        icon = Icons.Outlined.Star,
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        isPrivacyMode = isPrivacyMode,
                        modifier = Modifier.fillMaxSize(),
                        themeSettings = themeSettings
                    )
                }
            }
        }
    }
}

@Composable
fun ExpressiveTotalSpentTicker(
    totalSpend: Long,
    isPrivacyMode: Boolean,
    themeSettings: ThemeSettings
) {
    val formattedTotal = CurrencyUtils.formatAmount(totalSpend, themeSettings.currencySymbol, isPrivacyMode)
    
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isPrivacyMode) {
            Text(
                text = formattedTotal,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                fontSize = 28.sp
            )
        } else {
            formattedTotal.forEachIndexed { index, char ->
                val isDigit = char.isDigit()
                
                AnimatedContent(
                    targetState = char,
                    transitionSpec = {
                        if (isDigit) {
                            (slideInVertically(animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it } + fadeIn())
                                .togetherWith(slideOutVertically(animationSpec = spring(stiffness = Spring.StiffnessLow)) { -it } + fadeOut())
                        } else {
                            // M3 Expressive: Morphing Currency Symbol
                            (fadeIn(animationSpec = tween(400)) + scaleIn(initialScale = 0.8f))
                                .togetherWith(fadeOut(animationSpec = tween(400)) + scaleOut(targetScale = 1.2f))
                        }
                    },
                    label = "spend_digit_ticker_$index"
                ) { targetChar ->
                    Text(
                        text = targetChar.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        fontSize = 28.sp,
                        softWrap = false
                    )
                }
            }
        }
    }
}

@Composable
fun BentoStatSmall(
    title: String, 
    amount: Long, 
    icon: ImageVector,
    containerColor: Color, 
    contentColor: Color,
    isPrivacyMode: Boolean = false,
    modifier: Modifier,
    themeSettings: ThemeSettings
) {
    BentoCard(
        modifier = modifier,
        title = title,
        icon = icon,
        isActive = false,
        idleContainerColor = containerColor,
        idleContentColor = contentColor
    ) {
        ExpressiveSmallStatTicker(
            amount = amount,
            isPrivacyMode = isPrivacyMode,
            themeSettings = themeSettings
        )
    }
}

@Composable
fun ExpressiveSmallStatTicker(
    amount: Long,
    isPrivacyMode: Boolean,
    themeSettings: ThemeSettings
) {
    val formattedTotal = CurrencyUtils.formatAmount(amount, themeSettings.currencySymbol, isPrivacyMode)
    
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isPrivacyMode) {
            Text(
                text = formattedTotal,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black
            )
        } else {
            formattedTotal.forEachIndexed { index, char ->
                val isDigit = char.isDigit()
                
                AnimatedContent(
                    targetState = char,
                    transitionSpec = {
                        if (isDigit) {
                            (slideInVertically(animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it } + fadeIn())
                                .togetherWith(slideOutVertically(animationSpec = spring(stiffness = Spring.StiffnessLow)) { -it } + fadeOut())
                        } else {
                            // M3 Expressive: Morphing Currency Symbol
                            (fadeIn(animationSpec = tween(400)) + scaleIn(initialScale = 0.8f))
                                .togetherWith(fadeOut(animationSpec = tween(400)) + scaleOut(targetScale = 1.2f))
                        }
                    },
                    label = "small_digit_ticker_$index"
                ) { targetChar ->
                    Text(
                        text = targetChar.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        softWrap = false
                    )
                }
            }
        }
    }
}

@Composable
fun BudgetHealthBeam(
    totalNeed: Long, 
    totalWant: Long, 
    totalSpend: Long, 
    salaryAmount: Long,
    bentoIdleColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh
) {
    val needRatio = if (totalSpend > 0) totalNeed.toFloat() / totalSpend else 0f
    val wantRatio = if (totalSpend > 0) totalWant.toFloat() / totalSpend else 0f
    
    val animatedNeed by animateFloatAsState(targetValue = needRatio, animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
    val animatedWant by animateFloatAsState(targetValue = wantRatio, animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = bentoIdleColor
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Spending Balance", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black)
                Text("${( (totalSpend.toFloat()/(if(salaryAmount>0) salaryAmount else 1L).toFloat()) * 100).toInt()}% of Salary", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.fillMaxWidth().height(24.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceContainerHighest)) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxHeight().weight(if(animatedNeed > 0) animatedNeed else 0.001f).background(MaterialTheme.colorScheme.error))
                    Box(modifier = Modifier.fillMaxHeight().weight(if(animatedWant > 0) animatedWant else 0.001f).background(MaterialTheme.colorScheme.tertiary))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                LegendItem("Needs", MaterialTheme.colorScheme.error)
                LegendItem("Wants", MaterialTheme.colorScheme.tertiary)
            }
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun ExpressivePeriodIsland(
    currentMonth: Int, 
    currentYear: Int, 
    onMonthChange: (Int) -> Unit, 
    onYearChange: (Int) -> Unit,
    bentoIdleColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh
) {
    val haptic = LocalHapticFeedback.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = bentoIdleColor,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items((1..12).toList()) { month ->
                    val isSelected = currentMonth == month
                    
                    val targetWidth by animateDpAsState(
                        targetValue = if (isSelected) 80.dp else 64.dp,
                        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                        label = "month_width"
                    )
                    
                    val bgColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        label = "month_bg"
                    )
                    val txtColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        label = "month_txt"
                    )

                    Surface(
                        onClick = { 
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onMonthChange(month) 
                        },
                        color = bgColor,
                        contentColor = txtColor,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(40.dp)
                            .width(targetWidth)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = DateUtils.getShortMonthName(month),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}



data class SpendTabInfo(
    val category: SpendCategory?,
    val label: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun ExpressiveCategoryTabs(
    selectedCategory: SpendCategory?,
    onSelectAll: () -> Unit,
    onSelectNeed: () -> Unit,
    onSelectWant: () -> Unit
) {
    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.M3_EXPRESSIVE && false
    val haptic = LocalHapticFeedback.current
    val glowIntensity = LocalGlowIntensity.current

    val tabs = remember {
        listOf(
            SpendTabInfo(null, "All", Icons.Outlined.ShoppingBag, Color(0xFF6366F1)),
            SpendTabInfo(SpendCategory.NEED, "Needs", Icons.Outlined.Home, Color(0xFF10B981)),
            SpendTabInfo(SpendCategory.WANT, "Wants", Icons.Outlined.FavoriteBorder, Color(0xFFF43F5E))
        )
    }

    val selectedIndex = tabs.indexOfFirst { it.category == selectedCategory }.coerceAtLeast(0)
    val tabBounds = remember { mutableStateListOf<Pair<Float, Float>>() }

    // Morphing Outer Corners based on selection
    val outerCornerTopStart by animateIntAsState(
        targetValue = if (selectedIndex == 0) 36 else 28,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "spend_outer_corner_ts"
    )
    val outerCornerBottomStart by animateIntAsState(
        targetValue = if (selectedIndex == 0) 36 else 28,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "spend_outer_corner_bs"
    )
    val outerCornerTopEnd by animateIntAsState(
        targetValue = if (selectedIndex == tabs.lastIndex) 36 else 28,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "spend_outer_corner_te"
    )
    val outerCornerBottomEnd by animateIntAsState(
        targetValue = if (selectedIndex == tabs.lastIndex) 36 else 28,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "spend_outer_corner_be"
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
        MaterialTheme.colorScheme.surfaceContainer
    }

    val containerBorder = if (isSpaceTerminal) {
        BorderStroke(1.dp, Color(0xFF46C2B4).copy(alpha = 0.2f))
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f))
    }

    val shadowElevation = 0.dp

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = outerShape,
        color = containerBg,
        border = containerBorder,
        shadowElevation = shadowElevation,
        tonalElevation = 0.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Sliding background pill
            if (tabBounds.size == tabs.size) {
                val targetBounds = tabBounds.getOrNull(selectedIndex) ?: Pair(0f, 0f)
                val animatedX by animateFloatAsState(
                    targetValue = targetBounds.first,
                    animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow),
                    label = "spend_pill_x"
                )
                val animatedWidth by animateFloatAsState(
                    targetValue = targetBounds.second,
                    animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow),
                    label = "spend_pill_width"
                )

                val activeTabColor = tabs[selectedIndex].color
                val targetColor = if (isSpaceTerminal) {
                    Color(0xFF46C2B4)
                } else {
                    activeTabColor
                }
                val animatedColor by animateColorAsState(targetColor, label = "spend_pill_color")

                val distance = targetBounds.first - animatedX
                val absDistance = kotlin.math.abs(distance)
                val stretchX = 1f + (absDistance / 200f).coerceAtMost(0.25f)
                val squashY = 1f - (absDistance / 600f).coerceAtMost(0.12f)

                val pillShape = RoundedCornerShape(26.dp)

                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .offset { IntOffset(kotlin.math.round(animatedX).toInt(), 0) }
                        .width(with(LocalDensity.current) { animatedWidth.toDp() })
                        .fillMaxHeight()
                        .graphicsLayer {
                            scaleX = stretchX
                            scaleY = squashY
                            transformOrigin = TransformOrigin(0.5f, 0.5f)
                        }
                        .then(
                            if (glowIntensity != GlowIntensity.OFF && isSpaceTerminal) {
                                Modifier.glow(
                                    color = targetColor,
                                    radius = 8.dp,
                                    intensity = glowIntensity,
                                    shape = pillShape
                                )
                            } else Modifier
                        )
                        .background(animatedColor, pillShape)
                )
            }

            // Tabs Content Row
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .selectableGroup()
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = selectedIndex == index

                    val segmentWeight by animateFloatAsState(
                        targetValue = if (isSelected) 1.5f else 1.0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "spend_segment_weight"
                    )

                    val iconScale by animateFloatAsState(
                        targetValue = if (isSelected) 1.2f else 1.0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "spend_icon_scale"
                    )

                    val iconRotation by animateFloatAsState(
                        targetValue = if (isSelected) {
                            when (tab.category) {
                                SpendCategory.WANT -> 12f
                                null -> -10f
                                else -> 0f
                            }
                        } else 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "spend_icon_rotation"
                    )

                    val iconTranslationY by animateFloatAsState(
                        targetValue = if (isSelected) -8f else 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "spend_icon_translation"
                    )

                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected) {
                            if (isSpaceTerminal) Color(0xFF0F172A)
                            else MaterialTheme.colorScheme.onPrimary
                        } else {
                            if (isSpaceTerminal) Color(0xFF46C2B4).copy(alpha = 0.6f)
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        label = "spend_content_color"
                    )

                    Column(
                        modifier = Modifier
                            .weight(segmentWeight)
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
                                when (tab.category) {
                                    null -> onSelectAll()
                                    SpendCategory.NEED -> onSelectNeed()
                                    SpendCategory.WANT -> onSelectWant()
                                }
                            },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            modifier = Modifier
                                .size(24.dp)
                                .graphicsLayer {
                                    scaleX = iconScale
                                    scaleY = iconScale
                                    rotationZ = iconRotation
                                    translationY = iconTranslationY
                                    transformOrigin = TransformOrigin(0.5f, 0.5f)
                                },
                            tint = contentColor
                        )
                        
                        val textScale by animateFloatAsState(
                            targetValue = if (isSelected) 1f else 0.82f,
                            label = "spend_text_scale"
                        )
                        val textAlpha by animateFloatAsState(
                            targetValue = if (isSelected) 1f else 0.7f,
                            label = "spend_text_alpha"
                        )

                        Text(
                            text = tab.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = contentColor,
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .graphicsLayer {
                                    scaleX = textScale
                                    scaleY = textScale
                                    alpha = textAlpha
                                    transformOrigin = TransformOrigin(0.5f, 0f)
                                },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpressiveListItem(
    entry: SpendEntry,
    isPrivacyMode: Boolean = false,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    themeSettings: ThemeSettings
) {
    val haptic = LocalHapticFeedback.current
    val isNeed = entry.category == SpendCategory.NEED
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = containerColor,
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onEdit()
        }
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isNeed) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        if (isNeed) Icons.Default.Home else Icons.Default.Favorite,
                        contentDescription = null,
                        tint = if (isNeed) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = entry.description, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = entry.note.ifBlank { "No note" }, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Text(text = CurrencyUtils.formatAmount(entry.amount, themeSettings.currencySymbol, isPrivacyMode), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        }
    }
}



@Composable
fun SpendSwipeDeleteBackground() {
    Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(28.dp)).background(MaterialTheme.colorScheme.errorContainer), contentAlignment = Alignment.CenterEnd) {
        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.padding(end = 24.dp), tint = MaterialTheme.colorScheme.onErrorContainer)
    }
}

@Composable
fun SpendDeleteDialog(entry: SpendEntry?, onConfirm: () -> Unit, onDismiss: () -> Unit) {
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
                    text = "Delete?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Remove \"${entry?.description}\"?",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendAddEditSheet(
    uiState: SpendUiState,
    themeSettings: ThemeSettings,
    onDescriptionChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategoryChange: (SpendCategory) -> Unit,
    onNoteChange: (String) -> Unit,
    onMonthChange: (Int) -> Unit,
    onYearChange: (Int) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    
    // Animation/Focus States
    var isAmountFocused by remember { mutableStateOf(false) }
    var isDescFocused by remember { mutableStateOf(false) }
    var isNoteFocused by remember { mutableStateOf(false) }
    var showNoteField by remember { mutableStateOf(uiState.inputNote.isNotBlank()) }

    val descWeight by animateFloatAsState(
        targetValue = if (isDescFocused) 1.5f else if (isNoteFocused) 0.6f else 1.0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f),
        label = "spend_desc_weight"
    )
    val noteWeight by animateFloatAsState(
        targetValue = if (isNoteFocused) 1.5f else if (isDescFocused) 0.6f else 1.0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f),
        label = "spend_note_weight"
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
            .graphicsLayer(translationY = slideY.value, alpha = sheetAlpha)
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
            .navigationBarsPadding()
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (uiState.editingEntry == null) "New Expense" else "Edit Expense",
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
        
        // 1. Hero Amount Island (Bento Card, Focused Scaling)
        val amountElevation by animateDpAsState(if (isAmountFocused) 12.dp else 0.dp)
        val amountScale by animateFloatAsState(if (isAmountFocused) 1.04f else 1f)
        val amountBgColor by animateColorAsState(
            if (isAmountFocused) MaterialTheme.colorScheme.primaryContainer 
            else MaterialTheme.colorScheme.surfaceContainerHigh
        )

        Surface(
            color = amountBgColor,
            shape = RoundedCornerShape(28.dp),
            shadowElevation = amountElevation,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(scaleX = amountScale, scaleY = amountScale)
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "AMOUNT SPENT",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isAmountFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp
                )
                
                BasicTextField(
                    value = uiState.inputAmount,
                    onValueChange = { if (it.length <= 9) onAmountChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .onFocusChanged { 
                            if (it.isFocused != isAmountFocused) {
                                isAmountFocused = it.isFocused 
                                if (it.isFocused) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        },
                    textStyle = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        color = if (isAmountFocused) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
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
                                themeSettings.currencySymbol,
                                style = MaterialTheme.typography.displaySmall, 
                                fontWeight = FontWeight.Black,
                                color = if (isAmountFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            ) 
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(contentAlignment = Alignment.Center) {
                                if (uiState.inputAmount.isEmpty()) {
                                    Text(
                                        "0", 
                                        style = MaterialTheme.typography.displayLarge,
                                        fontWeight = FontWeight.Black,
                                        color = if (isAmountFocused) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f)
                                                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                                    ) 
                                }
                                innerTextField()
                            }
                        }
                    }
                )
            }
        }

        // 2. Category Island (Bento Selection)
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                ExpressiveCategoryBento(
                    selectedCategory = uiState.inputCategory,
                    onCategoryChange = { category ->
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onCategoryChange(category) 
                    }
                )
            }
        }

        // 3. Description & Note Island
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(descWeight.coerceAtLeast(0.001f))
            ) {
                ExpressiveOutlinedTextField(
                    value = uiState.inputDescription,
                    onValueChange = onDescriptionChange,
                    label = { Text("What did you buy?") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isDescFocused = it.isFocused }
                )
                AnimatedVisibility(
                    visible = isDescFocused,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Text(
                        text = "e.g., Groceries, Coffee",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (showNoteField) {
                Column(
                    modifier = Modifier.weight(noteWeight.coerceAtLeast(0.001f))
                ) {
                    ExpressiveOutlinedTextField(
                        value = uiState.inputNote,
                        onValueChange = onNoteChange,
                        label = { Text("Extra details...") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isNoteFocused = it.isFocused }
                    )
                    AnimatedVisibility(
                        visible = isNoteFocused,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Text(
                            text = "Add tags or store name",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (!showNoteField) {
            TextButton(
                onClick = { showNoteField = true },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Note", style = MaterialTheme.typography.labelLarge)
            }
        }

        ExpressiveButton(
            onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onSave() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Confirm Expense", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        }
    }
}


@Composable
fun ExpressiveCategoryBento(
    selectedCategory: SpendCategory,
    onCategoryChange: (SpendCategory) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Needs Bento Card
        CategoryBentoItem(
            modifier = Modifier.weight(1f),
            title = "Need",
            isSelected = selectedCategory == SpendCategory.NEED,
            icon = Icons.Outlined.Home,
            selectedColor = MaterialTheme.colorScheme.errorContainer,
            selectedContentColor = MaterialTheme.colorScheme.onErrorContainer,
            onClick = { onCategoryChange(SpendCategory.NEED) }
        )

        // Wants Bento Card
        CategoryBentoItem(
            modifier = Modifier.weight(1f),
            title = "Want",
            isSelected = selectedCategory == SpendCategory.WANT,
            icon = Icons.Outlined.Star,
            selectedColor = MaterialTheme.colorScheme.tertiaryContainer,
            selectedContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            onClick = { onCategoryChange(SpendCategory.WANT) }
        )
    }
}

@Composable
fun CategoryBentoItem(
    modifier: Modifier,
    title: String,
    isSelected: Boolean,
    icon: ImageVector,
    selectedColor: Color,
    selectedContentColor: Color,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    val elevation by animateDpAsState(if (isSelected) 6.dp else 0.dp)

    BentoCard(
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .fillMaxHeight(),
        title = title,
        icon = icon,
        isActive = isSelected,
        activeContainerColor = selectedColor,
        activeContentColor = selectedContentColor,
        idleContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        idleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        onClick = onClick
    )
}

@Composable
fun SpendHistoryBottomSheet(
    entries: List<SpendEntry>,
    isPrivacyMode: Boolean = false,
    onEdit: (SpendEntry) -> Unit,
    onDelete: (SpendEntry) -> Unit,
    themeSettings: ThemeSettings
) {
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
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "History",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black
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
            items(entries) { entry ->
                ExpressiveListItem(
                    entry = entry,
                    isPrivacyMode = isPrivacyMode,
                    onEdit = { onEdit(entry) },
                    onDelete = { onDelete(entry) },
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    themeSettings = themeSettings
                )
            }
        }
    }
}
