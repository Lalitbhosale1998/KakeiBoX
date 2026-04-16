package com.personal.kakeibox.ui.spend

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import com.personal.kakeibox.ui.settings.ThemeViewModel
import com.personal.kakeibox.data.preferences.NavBarStyle
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.animateFloatAsState
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
import kotlinx.coroutines.withTimeoutOrNull
import com.personal.kakeibox.ui.components.ExpressiveCategoryToggle
import com.personal.kakeibox.ui.components.ExpressiveEmptyState
import com.personal.kakeibox.ui.components.BentoCard
import com.personal.kakeibox.ui.components.ExpressiveTab
import com.personal.kakeibox.ui.components.ExpressiveSnackbarHost
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.R
import com.personal.kakeibox.data.entity.SalaryEntry
import com.personal.kakeibox.data.entity.SpendCategory
import com.personal.kakeibox.data.entity.SpendEntry
import com.personal.kakeibox.util.CurrencyUtils
import com.personal.kakeibox.util.DateUtils
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.focus.onFocusChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendScreen(
    viewModel: SpendViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
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

    val isFloatingNav = themeSettings.navBarStyle == NavBarStyle.FLOATING
    val fabPadding by animateDpAsState(
        targetValue = if (isFloatingNav) 100.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "fab_padding"
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val historyBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            Box(modifier = Modifier.background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = themeSettings.topBarAlpha.coerceIn(0f, 1f)),
                        MaterialTheme.colorScheme.surfaceContainerLow
                    )
                )
            )) {
                LargeTopAppBar(
                    title = {
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(
                                text = "Monthly",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Spending",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { 
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.toggleHistorySheet() 
                        }) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.History, 
                                    contentDescription = "History", 
                                    modifier = Modifier.padding(8.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
                )
            }
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { 
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.openAddSheet() 
                },
                modifier = Modifier.padding(bottom = fabPadding),
                shape = RoundedCornerShape(28.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", modifier = Modifier.size(36.dp))
            }
        },
        snackbarHost = { ExpressiveSnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp, 
                end = 16.dp, 
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // ── Bento Box Hero Grid ──────────────────────
            item {
                BentoHeroSection(
                    totalSpend = totalSpendAllTime ?: 0L,
                    totalNeed = totalNeed,
                    totalWant = totalWant,
                    salary = salary,
                    currentMonth = uiState.currentMonth,
                    currentYear = uiState.currentYear,
                    isPrivacyMode = themeSettings.privacyModeEnabled,
                    onPeriodClick = { /* Scroll to top or show picker if needed */ }
                )
            }

            // ── Period Navigation Island ──────────────────
            item {
                ExpressivePeriodIsland(
                    currentMonth = uiState.currentMonth,
                    currentYear = uiState.currentYear,
                    onMonthChange = viewModel::updateViewedMonth,
                    onYearChange = viewModel::updateViewedYear
                )
            }

            // ── Budget Health Bar ────────────────────────
            item {
                AnimatedVisibility(
                    visible = (salary?.salaryAmount ?: 0L) > 0,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    BudgetHealthBeam(
                        totalNeed = totalNeed,
                        totalWant = totalWant,
                        totalSpend = totalSpend,
                        salaryAmount = salary?.salaryAmount ?: 0L
                    )
                }
            }

            // ── Category Tabs ─────────────────────────────
            item {
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

            // ── List Items ───────────────────────────────
            if (filteredEntries.isEmpty()) {
                item {
                    ExpressiveEmptyState(
                        message = if (uiState.selectedCategory != null) "No ${uiState.selectedCategory} logs" else "No spending yet",
                        icon = if (uiState.selectedCategory == SpendCategory.NEED) "🛡️" else "✨"
                    )
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
                    SwipeToDismissBox(
                        state = dismissState,
                        modifier = Modifier.animateItem(),
                        enableDismissFromStartToEnd = false,
                        backgroundContent = { SpendSwipeDeleteBackground() }
                    ) {
                        ExpressiveListItem(
                            entry = entry,
                            isPrivacyMode = themeSettings.privacyModeEnabled,
                            onEdit = { viewModel.openEditSheet(entry) },
                            onDelete = { viewModel.openDeleteDialog(entry) }
                        )
                    }
                }
            }
        }
    }

    // Sheets & Dialogs
    if (uiState.showAddEditSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeSheet() },
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant) },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            SpendAddEditSheet(
                uiState = uiState,
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

    if (uiState.showDeleteDialog) {
        SpendDeleteDialog(uiState.deletingEntry, viewModel::deleteEntry, viewModel::closeDeleteDialog)
    }

    if (uiState.showHistorySheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleHistorySheet() },
            sheetState = historyBottomSheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            SpendHistoryBottomSheet(
                entries = allEntries,
                isPrivacyMode = themeSettings.privacyModeEnabled,
                onEdit = viewModel::openEditSheet,
                onDelete = viewModel::openDeleteDialog
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
    onPeriodClick: () -> Unit
) {
    val salaryAmount = salary?.salaryAmount ?: 0L
    val remaining = salaryAmount - totalSpend
    
    Row(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Main Balance Card (Tall Bento)
        BentoCard(
            modifier = Modifier.weight(1.2f).fillMaxHeight(),
            title = "TOTAL SPENT",
            icon = Icons.Outlined.AccountBalanceWallet,
            isActive = true,
            activeContainerColor = MaterialTheme.colorScheme.primary,
            activeContentColor = MaterialTheme.colorScheme.onPrimary,
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
                    isPrivacyMode = isPrivacyMode
                )
            }
        }

        // Stats Stack (Right Bento Side)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            BentoStatSmall(
                title = "Needs",
                amount = totalNeed,
                icon = Icons.Outlined.ShoppingBag,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                isPrivacyMode = isPrivacyMode,
                modifier = Modifier.weight(1f)
            )
            BentoStatSmall(
                title = "Wants",
                amount = totalWant,
                icon = Icons.Outlined.Star,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                isPrivacyMode = isPrivacyMode,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ExpressiveTotalSpentTicker(
    totalSpend: Long,
    isPrivacyMode: Boolean
) {
    val formattedTotal = CurrencyUtils.formatYen(totalSpend, isPrivacyMode)
    
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
                            fadeIn(animationSpec = tween(150))
                                .togetherWith(fadeOut(animationSpec = tween(150)))
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
    modifier: Modifier
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
            isPrivacyMode = isPrivacyMode
        )
    }
}

@Composable
fun ExpressiveSmallStatTicker(
    amount: Long,
    isPrivacyMode: Boolean
) {
    val formattedTotal = CurrencyUtils.formatYen(amount, isPrivacyMode)
    
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
                            fadeIn(animationSpec = tween(150))
                                .togetherWith(fadeOut(animationSpec = tween(150)))
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
fun BudgetHealthBeam(totalNeed: Long, totalWant: Long, totalSpend: Long, salaryAmount: Long) {
    val needRatio = if (totalSpend > 0) totalNeed.toFloat() / totalSpend else 0f
    val wantRatio = if (totalSpend > 0) totalWant.toFloat() / totalSpend else 0f
    
    val animatedNeed by animateFloatAsState(targetValue = needRatio, animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
    val animatedWant by animateFloatAsState(targetValue = wantRatio, animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh
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
fun ExpressivePeriodIsland(currentMonth: Int, currentYear: Int, onMonthChange: (Int) -> Unit, onYearChange: (Int) -> Unit) {
    val haptic = LocalHapticFeedback.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
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



@Composable
fun ExpressiveCategoryTabs(
    selectedCategory: SpendCategory?,
    onSelectAll: () -> Unit,
    onSelectNeed: () -> Unit,
    onSelectWant: () -> Unit
) {
    val allWeight by animateFloatAsState(
        targetValue = if (selectedCategory == null) 1.5f else 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        label = "all_weight"
    )
    val needWeight by animateFloatAsState(
        targetValue = if (selectedCategory == SpendCategory.NEED) 1.5f else 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        label = "need_weight"
    )
    val wantWeight by animateFloatAsState(
        targetValue = if (selectedCategory == SpendCategory.WANT) 1.5f else 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        label = "want_weight"
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        ExpressiveTab(
            text = "All",
            isSelected = selectedCategory == null,
            selectedColor = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.weight(allWeight),
            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = onSelectAll
        )
        ExpressiveTab(
            text = "Needs",
            isSelected = selectedCategory == SpendCategory.NEED,
            selectedColor = MaterialTheme.colorScheme.errorContainer,
            modifier = Modifier.weight(needWeight),
            selectedTextColor = MaterialTheme.colorScheme.onErrorContainer,
            onClick = onSelectNeed
        )
        ExpressiveTab(
            text = "Wants",
            isSelected = selectedCategory == SpendCategory.WANT,
            selectedColor = MaterialTheme.colorScheme.tertiaryContainer,
            modifier = Modifier.weight(wantWeight),
            selectedTextColor = MaterialTheme.colorScheme.onTertiaryContainer,
            onClick = onSelectWant
        )
    }
}

@Composable
fun ExpressiveListItem(
    entry: SpendEntry,
    isPrivacyMode: Boolean = false,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val isNeed = entry.category == SpendCategory.NEED
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
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
            Text(text = CurrencyUtils.formatYen(entry.amount, isPrivacyMode), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
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
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete?", fontWeight = FontWeight.Black) },
        text = { Text("Remove \"${entry?.description}\"?") },
        confirmButton = { Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) { Text("Delete") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        shape = RoundedCornerShape(28.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendAddEditSheet(
    uiState: SpendUiState,
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
    var showNoteField by remember { mutableStateOf(uiState.inputNote.isNotBlank()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
                                "¥", 
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
                    onCategoryChange = { 
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onCategoryChange(it) 
                    }
                )
            }
        }

        // 3. Description & Note Island
        val descElevation by animateDpAsState(if (isDescFocused) 8.dp else 0.dp)
        val descScale by animateFloatAsState(if (isDescFocused) 1.02f else 1f)

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(28.dp),
            tonalElevation = descElevation,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(scaleX = descScale, scaleY = descScale)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = uiState.inputDescription,
                    onValueChange = onDescriptionChange,
                    label = { Text("What did you buy?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isDescFocused = it.isFocused },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                // Expanding Note Drawer
                AnimatedVisibility(
                    visible = showNoteField,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    OutlinedTextField(
                        value = uiState.inputNote,
                        onValueChange = onNoteChange,
                        label = { Text("Extra details...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
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
            }
        }

        Button(
            onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onSave() },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = RoundedCornerShape(28.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 4.dp)
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
    onDelete: (SpendEntry) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
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
                    onDelete = { onDelete(entry) }
                )
            }
        }
    }
}
