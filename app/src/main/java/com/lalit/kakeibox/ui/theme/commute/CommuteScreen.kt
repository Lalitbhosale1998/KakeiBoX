package com.personal.kakeibox.ui.commute

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.focus.onFocusChanged
import com.personal.kakeibox.ui.commute.CommuteUiState
import com.personal.kakeibox.ui.commute.CommuteViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.activity.ComponentActivity
import com.personal.kakeibox.data.preferences.TopAppBarBackground
import com.personal.kakeibox.ui.settings.ThemeViewModel
import com.personal.kakeibox.data.preferences.NavBarStyle
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.delay
import com.personal.kakeibox.ui.components.ExpressiveSnackbarHost
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.R
import com.personal.kakeibox.data.entity.CommuteEntry
import com.personal.kakeibox.ui.components.BentoCard
import com.personal.kakeibox.ui.components.ExpressiveButton
import com.personal.kakeibox.ui.theme.LocalThemeStyle
import com.personal.kakeibox.data.preferences.ThemeStyle
import com.personal.kakeibox.ui.components.ExpressiveEmptyState
import com.personal.kakeibox.ui.components.ExpressiveOutlinedTextField
import com.personal.kakeibox.data.preferences.ThemeSettings
import com.personal.kakeibox.util.CurrencyUtils
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Train
import androidx.compose.ui.text.style.TextOverflow
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.lazy.rememberLazyListState
import com.personal.kakeibox.ui.components.ExpressiveCollapsingHeader
import com.personal.kakeibox.ui.components.ExpressiveChip

enum class CommuteFilter { ALL, OFFICE, WFH }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommuteScreen(
    viewModel: CommuteViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(LocalActivity.current as ComponentActivity)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val themeSettings by themeViewModel.themeSettings.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current

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

    val isFloatingNav = themeSettings.navBarStyle == NavBarStyle.FLOATING || themeSettings.navBarStyle == NavBarStyle.SPLIT
    val fabPadding by animateDpAsState(
        targetValue = if (isFloatingNav) 100.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "fab_padding"
    )

    val snackbarHostState = remember { SnackbarHostState() }

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

    val topAppBarContainerColor by animateColorAsState(
        targetValue = when (themeSettings.topAppBarBackground) {
            TopAppBarBackground.SURFACE -> MaterialTheme.colorScheme.surface
            TopAppBarBackground.PRIMARY_CONTAINER -> MaterialTheme.colorScheme.primaryContainer
        },
        label = "top_app_bar_container_color"
    )

    val bentoIdleColor = if (isPrimaryContainer) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceContainer

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            // Snappier 2-second timeout for Expressive Snackbars
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
    var currentFilter by remember { mutableStateOf(CommuteFilter.ALL) }
    val filteredCommuteHistory = remember(currentFilter, uiState.history) {
        when (currentFilter) {
            CommuteFilter.ALL -> uiState.history
            CommuteFilter.OFFICE -> uiState.history.filter { it.totalCommuteDays > 0 }
            CommuteFilter.WFH -> uiState.history.filter { it.wfhDays > 0 }
        }
    }
    LaunchedEffect(Unit) {
        showHero = true
        delay(100)
        showStats = true
        delay(100)
        showHistory = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = topAppBarContainerColor,
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
                    color = if (isPrimaryContainer) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = if (isPrimaryContainer) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onTertiaryContainer,
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
                                contentDescription = "Add Commute",
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
                                    text = "Add Commute",
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
                    start = 16.dp, end = 16.dp,
                    top = innerPadding.calculateTopPadding() + 8.dp,
                    bottom = innerPadding.calculateBottomPadding() + 80.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(150.dp + statusBarPadding))
                }
                item {
                    AnimatedVisibility(
                        visible = showHero,
                        enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                    ) {
                        CommuteHeroSection(
                            totalCost = uiState.totalCostAllTime, 
                            isPrivacyMode = themeSettings.privacyModeEnabled,
                            isPrimaryContainer = isPrimaryContainer,
                            themeSettings = themeSettings
                        )
                    }
                }

                if (uiState.latestEntry == null) {
                    item {
                        AnimatedVisibility(
                            visible = showHistory,
                            enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                    slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                        ) {
                            ExpressiveEmptyState(
                                message = "No commute logs yet",
                                icon = "🚌",
                                color = onContainerColor
                            )
                        }
                    }
                } else {
                    item {
                        AnimatedVisibility(
                            visible = showStats,
                            enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                    slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                        ) {
                            CommuteDetailsBento(
                                entry = uiState.latestEntry!!,
                                bentoIdleColor = bentoIdleColor
                            )
                        }
                    }

                    item {
                        AnimatedVisibility(
                            visible = showHistory,
                            enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                    slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                        ) {
                            Text(
                                text = "History",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }

                    items(
                        items = uiState.history,
                        key = { it.id }
                    ) { entry ->
                        val swipeState = rememberSwipeToDismissBoxState()

                        LaunchedEffect(swipeState.currentValue) {
                            if (swipeState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.openDeleteDialog(entry)
                                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
                            }
                        }

                        AnimatedVisibility(
                            visible = showHistory,
                            enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                                    slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                        ) {
                            SwipeToDismissBox(
                                state = swipeState,
                                enableDismissFromStartToEnd = false,
                                backgroundContent = { CommuteSwipeDeleteBackground() },
                                content = {
                                    CommuteHistoryItem(
                                        entry = entry,
                                        isPrivacyMode = themeSettings.privacyModeEnabled,
                                        onDelete = { viewModel.openDeleteDialog(entry) },
                                        containerColor = bentoIdleColor,
                                        themeSettings = themeSettings
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        ExpressiveCollapsingHeader(
            title = "Work",
            subtitle = "Commute",
            scrollOffset = scrollOffset,
            maxOffset = maxOffsetPx,
            containerColor = topAppBarContainerColor,
            onContainerColor = onContainerColor,
            primaryTextAccent = primaryTextAccent,
            actions = {
                IconButton(onClick = { 
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.toggleHistory() 
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

    if (uiState.showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeAddSheet() },
            containerColor = bentoIdleColor,
            dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant) },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            CommuteAddEditSheet(
                uiState = uiState,
                themeSettings = themeSettings,
                onFareChange = viewModel::updateFare,
                onHolidaysChange = viewModel::updateHolidays,
                onWfhChange = viewModel::updateWfhDays,
                onSave = viewModel::saveEntry,
                onDismiss = viewModel::closeAddSheet
            )
        }
    }

    if (uiState.showDeleteDialog) {
        CommuteDeleteDialog(
            onConfirm = { viewModel.deleteEntry(uiState.deletingEntry!!) },
            onDismiss = { viewModel.closeDeleteDialog() }
        )
    }

    if (uiState.showHistorySheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleHistory() },
            containerColor = bentoIdleColor,
            dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant) },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            CommuteHistoryBottomSheet(
                entries = uiState.history,
                isPrivacyMode = themeSettings.privacyModeEnabled,
                onDelete = { viewModel.openDeleteDialog(it) },
                themeSettings = themeSettings
            )
        }
    }
}

@Composable
fun CommuteHeroSection(
    totalCost: Long, 
    isPrivacyMode: Boolean = false,
    isPrimaryContainer: Boolean = false,
    themeSettings: ThemeSettings
) {
    BentoCard(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        title = "TOTAL COMMUTE COST",
        icon = Icons.Outlined.Train,
        isActive = true,
        activeContainerColor = if (isPrimaryContainer) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f) else MaterialTheme.colorScheme.tertiary,
        activeContentColor = if (isPrimaryContainer) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onTertiary
    ) {
        Column {
            Text(
                text = "Cumulative",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = LocalContentColor.current.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            ExpressiveCommuteCostTicker(
                totalCost = totalCost,
                isPrivacyMode = isPrivacyMode,
                themeSettings = themeSettings
            )
        }
    }
}

@Composable
fun ExpressiveCommuteCostTicker(
    totalCost: Long,
    isPrivacyMode: Boolean,
    themeSettings: ThemeSettings
) {
    val haptic = LocalHapticFeedback.current
    val formattedTotal = CurrencyUtils.formatAmount(totalCost, themeSettings.currencySymbol, isPrivacyMode)
    
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isPrivacyMode) {
            Text(
                text = formattedTotal,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black
            )
        } else {
            formattedTotal.forEachIndexed { index, char ->
                val isDigit = char.isDigit()
                
                AnimatedContent(
                    targetState = char,
                    transitionSpec = {
                        if (isDigit) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            (slideInVertically(animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it } + fadeIn())
                                .togetherWith(slideOutVertically(animationSpec = spring(stiffness = Spring.StiffnessLow)) { -it } + fadeOut())
                        } else {
                            fadeIn(animationSpec = tween(150))
                                .togetherWith(fadeOut(animationSpec = tween(150)))
                        }
                    },
                    label = "commute_cost_ticker_$index"
                ) { digitChar ->
                    Text(
                        text = digitChar.toString(),
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
fun CommuteDetailsBento(
    entry: CommuteEntry,
    bentoIdleColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    currentFilter: CommuteFilter = CommuteFilter.ALL
) {
    val officeWeight by animateFloatAsState(
        targetValue = when (currentFilter) {
            CommuteFilter.OFFICE -> 2.0f
            CommuteFilter.WFH -> 0.001f
            CommuteFilter.ALL -> 1.0f
        },
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f),
        label = "commute_office_weight"
    )

    val wfhWeight by animateFloatAsState(
        targetValue = when (currentFilter) {
            CommuteFilter.WFH -> 2.0f
            CommuteFilter.OFFICE -> 0.001f
            CommuteFilter.ALL -> 1.0f
        },
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f),
        label = "commute_wfh_weight"
    )

    Row(
        modifier = Modifier.fillMaxWidth().height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(officeWeight.coerceAtLeast(0.001f))
                .fillMaxHeight()
                .graphicsLayer {
                    alpha = ((officeWeight - 0.1f) / 1.9f).coerceIn(0f, 1f)
                    scaleX = if (currentFilter == CommuteFilter.WFH) (officeWeight / 1f).coerceIn(0f, 1f) else 1.0f
                    scaleY = if (currentFilter == CommuteFilter.WFH) (officeWeight / 1f).coerceIn(0f, 1f) else 1.0f
                }
        ) {
            BentoCard(
                modifier = Modifier.fillMaxSize(),
                title = "Office Days",
                icon = Icons.Outlined.Business,
                idleContainerColor = bentoIdleColor,
                idleContentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Text(
                    text = entry.totalCommuteDays.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(wfhWeight.coerceAtLeast(0.001f))
                .fillMaxHeight()
                .graphicsLayer {
                    alpha = ((wfhWeight - 0.1f) / 1.9f).coerceIn(0f, 1f)
                    scaleX = if (currentFilter == CommuteFilter.OFFICE) (wfhWeight / 1f).coerceIn(0f, 1f) else 1.0f
                    scaleY = if (currentFilter == CommuteFilter.OFFICE) (wfhWeight / 1f).coerceIn(0f, 1f) else 1.0f
                }
        ) {
            BentoCard(
                modifier = Modifier.fillMaxSize(),
                title = "WFH Days",
                icon = Icons.Outlined.Home,
                idleContainerColor = bentoIdleColor,
                idleContentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Text(
                    text = entry.wfhDays.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun CommuteSwipeDeleteBackground() {
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
            modifier = Modifier.padding(end = 24.dp),
            tint = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Composable
fun CommuteDeleteDialog(
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

@Composable
fun CommuteHistoryItem(
    entry: CommuteEntry,
    isPrivacyMode: Boolean = false,
    onDelete: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    themeSettings: ThemeSettings
) {
    val haptic = LocalHapticFeedback.current
    val date = remember(entry.createdAt) {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(entry.createdAt))
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = containerColor,
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            // If there's an edit function, call it here. Currently only delete is shown.
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Outlined.Train,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = CurrencyUtils.formatAmount(entry.totalCost, themeSettings.currencySymbol, isPrivacyMode),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (entry.oneWayFare > 0) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Fare: ${CurrencyUtils.formatAmount(entry.oneWayFare, themeSettings.currencySymbol, isPrivacyMode)} (One-way)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${entry.totalCommuteDays} Days",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Office",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CommuteHistoryBottomSheet(
    entries: List<CommuteEntry>,
    isPrivacyMode: Boolean = false,
    onDelete: (CommuteEntry) -> Unit,
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
                text = "History",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "${entries.size} Records",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
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
                    backgroundContent = { CommuteSwipeDeleteBackground() },
                    content = {
                        CommuteHistoryItem(
                            entry = entry,
                            isPrivacyMode = isPrivacyMode,
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

@Composable
fun CommuteAddEditSheet(
    uiState: CommuteUiState,
    themeSettings: ThemeSettings,
    onFareChange: (String) -> Unit,
    onHolidaysChange: (String) -> Unit,
    onWfhChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    
    // Focus states for animations
    var isFareFocused by remember { mutableStateOf(false) }
    var isHolidaysFocused by remember { mutableStateOf(false) }
    var isWfhFocused by remember { mutableStateOf(false) }

    val holidaysWeight by animateFloatAsState(
        targetValue = if (isHolidaysFocused) 1.5f else if (isWfhFocused) 0.6f else 1.0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f),
        label = "commute_holidays_weight"
    )
    val wfhWeight by animateFloatAsState(
        targetValue = if (isWfhFocused) 1.5f else if (isHolidaysFocused) 0.6f else 1.0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f),
        label = "commute_wfh_weight"
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
            .imePadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Estimate Commute",
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
        
        // Hero Amount Island for Fare
        val fareElevation by animateDpAsState(if (isFareFocused) 12.dp else 0.dp)
        val fareScale by animateFloatAsState(if (isFareFocused) 1.04f else 1f)
        
        Surface(
            color = if (isFareFocused) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(28.dp),
            shadowElevation = fareElevation,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(scaleX = fareScale, scaleY = fareScale)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "ONE-WAY FARE",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isFareFocused) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp
                )
                
                BasicTextField(
                    value = uiState.inputOneWayFare,
                    onValueChange = onFareChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .onFocusChanged { 
                            if (it.isFocused != isFareFocused) {
                                isFareFocused = it.isFocused 
                                if (it.isFocused) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        },
                    textStyle = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = if (isFareFocused) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
                    decorationBox = { innerTextField ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = themeSettings.currencySymbol,
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Black,
                                color = if (isFareFocused) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                if (uiState.inputOneWayFare.isEmpty()) {
                                    Text(
                                        text = "0",
                                        style = MaterialTheme.typography.displayMedium,
                                        fontWeight = FontWeight.Black,
                                        color = if (isFareFocused) MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.3f) 
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
        
        Spacer(modifier = Modifier.height(24.dp))

        // Bento Island for Days
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Monthly Adjustments",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(holidaysWeight.coerceAtLeast(0.001f))
                    ) {
                        ExpressiveOutlinedTextField(
                            value = uiState.inputHolidays,
                            onValueChange = onHolidaysChange,
                            label = { Text("Holidays") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { isHolidaysFocused = it.isFocused },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = { Icon(Icons.Outlined.EventBusy, contentDescription = null) }
                        )
                        AnimatedVisibility(
                            visible = isHolidaysFocused,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Text(
                                text = "Excludes weekends",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.weight(wfhWeight.coerceAtLeast(0.001f))
                    ) {
                        ExpressiveOutlinedTextField(
                            value = uiState.inputWfhDays,
                            onValueChange = onWfhChange,
                            label = { Text("WFH Days") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { isWfhFocused = it.isFocused },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = { Icon(Icons.Outlined.Home, contentDescription = null) }
                        )
                        AnimatedVisibility(
                            visible = isWfhFocused,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Text(
                                text = "Saves fare costs",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        val isInputValid = uiState.inputOneWayFare.isNotBlank() && uiState.inputOneWayFare.toDoubleOrNull() != null

        ExpressiveButton(
            onClick = {
                if (isInputValid) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSave()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isInputValid,
            backgroundColor = MaterialTheme.colorScheme.tertiary
        ) {
            AnimatedContent(
                targetState = isInputValid,
                transitionSpec = {
                    (fadeIn() + slideInVertically { it / 2 }).togetherWith(fadeOut() + slideOutVertically { -it / 2 })
                },
                label = "save_button_content"
            ) { valid ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (valid) Icons.Default.Calculate else Icons.Default.Lock, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        if (valid) "Calculate & Save" else "Enter Fare to Calculate",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}
