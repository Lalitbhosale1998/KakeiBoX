package com.personal.kakeibox.ui.spend

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.R
import com.personal.kakeibox.data.entity.SalaryEntry
import com.personal.kakeibox.data.entity.SpendCategory
import com.personal.kakeibox.data.entity.SpendEntry
import com.personal.kakeibox.util.CurrencyUtils
import com.personal.kakeibox.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendScreen(
    viewModel: SpendViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentMonthEntries by viewModel.currentMonthEntries.collectAsStateWithLifecycle()
    val allEntries by viewModel.allEntries.collectAsStateWithLifecycle()
    val totalNeed by viewModel.totalNeedThisMonth.collectAsStateWithLifecycle()
    val totalWant by viewModel.totalWantThisMonth.collectAsStateWithLifecycle()
    val totalSpend by viewModel.totalSpendThisMonth.collectAsStateWithLifecycle()
    val salary by viewModel.currentSalary.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val historyBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val needEntries = remember(currentMonthEntries) {
        currentMonthEntries.filter { it.category == SpendCategory.NEED }
    }
    val wantEntries = remember(currentMonthEntries) {
        currentMonthEntries.filter { it.category == SpendCategory.WANT }
    }
    val filteredEntries = remember(uiState.selectedCategory, currentMonthEntries) {
        when (uiState.selectedCategory) {
            SpendCategory.NEED -> needEntries
            SpendCategory.WANT -> wantEntries
            null -> currentMonthEntries
        }
    }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentWindowInsets = WindowInsets.navigationBars,
        topBar = {
            Box(modifier = Modifier.background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.surfaceContainerLow
                    )
                )
            )) {
                LargeTopAppBar(
                    title = {
                        Column {
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
                        IconButton(
                            onClick = { viewModel.toggleHistorySheet() },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    Icons.Filled.History,
                                    contentDescription = "History",
                                    modifier = Modifier.padding(12.dp)
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
                onClick = { viewModel.openAddSheet() },
                shape = RoundedCornerShape(28.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
            // ── Chunky Hero Card ──────────────────────────
            item {
                ExpressiveHeroCard(
                    totalSpend = totalSpend ?: 0L,
                    totalNeed = totalNeed ?: 0L,
                    totalWant = totalWant ?: 0L,
                    salary = salary
                )
            }

            // ── Quick Add Buttons ─────────────────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ExpressiveQuickAddButton(
                        title = "Need",
                        subtitle = "Essential",
                        icon = Icons.Filled.Home,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        onClick = { viewModel.openAddSheet(SpendCategory.NEED) },
                        modifier = Modifier.weight(1f)
                    )
                    ExpressiveQuickAddButton(
                        title = "Want",
                        subtitle = "Lifestyle",
                        icon = Icons.Filled.Favorite,
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        onClick = { viewModel.openAddSheet(SpendCategory.WANT) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // ── Chunky Category Tabs ──────────────────────
            item {
                ExpressiveCategoryTabs(
                    selectedCategory = uiState.selectedCategory,
                    onSelectAll = { viewModel.setFilter(null) },
                    onSelectNeed = { viewModel.setFilter(SpendCategory.NEED) },
                    onSelectWant = { viewModel.setFilter(SpendCategory.WANT) }
                )
            }

            // ── Expressive List Items ─────────────────────
            if (filteredEntries.isEmpty()) {
                item {
                    ExpressiveEmptyState(category = uiState.selectedCategory)
                }
            } else {
                items(
                    items = filteredEntries,
                    key = { it.id }
                ) { entry ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
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
                            onEdit = { viewModel.openEditSheet(entry) },
                            onDelete = { viewModel.openDeleteDialog(entry) }
                        )
                    }
                }
            }
        }
    }

    // ── Expressive Add/Edit Sheet ─────────────────────
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

    // ── Expressive Delete Dialog ──────────────────────
    if (uiState.showDeleteDialog) {
        SpendDeleteDialog(
            entry = uiState.deletingEntry,
            onConfirm = viewModel::deleteEntry,
            onDismiss = viewModel::closeDeleteDialog
        )
    }

    if (uiState.showHistorySheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleHistorySheet() },
            sheetState = historyBottomSheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant) },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            SpendHistoryBottomSheet(
                entries = allEntries,
                onEdit = { entry ->
                    viewModel.toggleHistorySheet()
                    viewModel.openEditSheet(entry)
                },
                onDelete = { entry ->
                    viewModel.toggleHistorySheet()
                    viewModel.openDeleteDialog(entry)
                }
            )
        }
    }
}

// ── M3 Expressive Components ─────────────────────────────────

@Composable
fun ExpressiveHeroCard(
    totalSpend: Long,
    totalNeed: Long,
    totalWant: Long,
    salary: SalaryEntry?
) {
    val salaryAmount = salary?.salaryAmount ?: 0L
    val remaining = salaryAmount - totalSpend
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (remaining >= 0) "Remaining" else "Overspent",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = CurrencyUtils.formatYen(remaining),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = if (remaining >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
                
                // ── Animated donut arc ─────────────────────────────
                val startAngle = -90f
                val sweepAngle = 360f
                val animatedProgress by animateFloatAsState(
                    targetValue = if (salaryAmount > 0)
                        (totalSpend.toFloat() / salaryAmount).coerceIn(0f, 1f)
                    else 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "arc_progress"
                )
                
                val primaryColor = MaterialTheme.colorScheme.primary
                val errorColor = MaterialTheme.colorScheme.error
                
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(100.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 12.dp.toPx()
                        drawArc(
                            color = Color.LightGray.copy(alpha = 0.2f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = if (remaining >= 0) primaryColor else errorColor,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle * animatedProgress,
                            useCenter = false,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            androidx.compose.material3.HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Needs", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = CurrencyUtils.formatYen(totalNeed),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Black
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Wants", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = CurrencyUtils.formatYen(totalWant),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
fun ExpressiveListItem(
    entry: SpendEntry,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isNeed = entry.category == SpendCategory.NEED
    val iconColor = if (isNeed) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
    val iconContainerColor = if (isNeed) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.tertiaryContainer

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        onClick = onEdit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = iconContainerColor,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isNeed) Icons.Filled.Home else Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (entry.note.isNotBlank()) {
                    Text(
                        text = entry.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = CurrencyUtils.formatYen(entry.amount),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpressiveQuickAddButton(
    title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector,
    containerColor: Color, contentColor: Color, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(28.dp),
        color = containerColor,
        contentColor = contentColor,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.weight(1f))
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = contentColor.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun ExpressiveCategoryTabs(
    selectedCategory: SpendCategory?,
    onSelectAll: () -> Unit,
    onSelectNeed: () -> Unit,
    onSelectWant: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allWeight by animateFloatAsState(targetValue = if (selectedCategory == null) 1.4f else 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow), label = "weight_all")
    val needWeight by animateFloatAsState(targetValue = if (selectedCategory == SpendCategory.NEED) 1.4f else 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow), label = "weight_need")
    val wantWeight by animateFloatAsState(targetValue = if (selectedCategory == SpendCategory.WANT) 1.4f else 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow), label = "weight_want")

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ExpressiveTab(
            text = "All",
            isSelected = selectedCategory == null,
            selectedColor = MaterialTheme.colorScheme.primaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = onSelectAll,
            modifier = Modifier.weight(allWeight)
        )
        ExpressiveTab(
            text = "Needs",
            isSelected = selectedCategory == SpendCategory.NEED,
            selectedColor = MaterialTheme.colorScheme.errorContainer,
            selectedTextColor = MaterialTheme.colorScheme.onErrorContainer,
            onClick = onSelectNeed,
            modifier = Modifier.weight(needWeight)
        )
        ExpressiveTab(
            text = "Wants",
            isSelected = selectedCategory == SpendCategory.WANT,
            selectedColor = MaterialTheme.colorScheme.tertiaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onTertiaryContainer,
            onClick = onSelectWant,
            modifier = Modifier.weight(wantWeight)
        )
    }
}

@Composable
fun ExpressiveTab(
    text: String,
    isSelected: Boolean,
    selectedColor: Color,
    selectedTextColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(targetValue = if (isSelected) selectedColor else MaterialTheme.colorScheme.surfaceContainerHigh, animationSpec = tween(durationMillis = 300), label = "color_bg")
    val contentColor by animateColorAsState(targetValue = if (isSelected) selectedTextColor else MaterialTheme.colorScheme.onSurfaceVariant, animationSpec = tween(durationMillis = 300), label = "color_content")
    val textScale by animateFloatAsState(targetValue = if (isSelected) 1.15f else 1.0f, animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium), label = "scale_text")

    Surface(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = CircleShape,
        color = backgroundColor,
        contentColor = contentColor
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
                modifier = Modifier.graphicsLayer { scaleX = textScale; scaleY = textScale }
            )
        }
    }
}

@Composable
fun ExpressiveEmptyState(category: SpendCategory?) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            modifier = Modifier.size(100.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = when (category) { SpendCategory.NEED -> "💡"; SpendCategory.WANT -> "🛍️"; null -> "💸" }, fontSize = 48.sp)
            }
        }
        Text(
            text = "Nothing tracked yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Tap 'New Expense' to start logging.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ── Expressive Sheet & Dialogs ────────────────────────────────

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
    val isEditing = uiState.editingEntry != null
    val isNeed = uiState.inputCategory == SpendCategory.NEED

    val activeContainerColor = if (isNeed) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.tertiaryContainer
    val activeContentColor = if (isNeed) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onTertiaryContainer
    val activeColor = if (isNeed) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isEditing) "Edit Expense" else "New Expense",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHighest, CircleShape)
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
            }
        }

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            listOf(SpendCategory.NEED, SpendCategory.WANT).forEachIndexed { index, category ->
                val catIsNeed = category == SpendCategory.NEED
                SegmentedButton(
                    selected = uiState.inputCategory == category,
                    onClick = { onCategoryChange(category) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = 2),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = activeContainerColor,
                        activeContentColor = activeContentColor
                    )
                ) {
                    Text(
                        text = if (catIsNeed) "Need" else "Want",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Clean, flat expressive input bubble
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            color = activeContainerColor
        ) {
            androidx.compose.material3.TextField(
                value = uiState.inputAmount,
                onValueChange = onAmountChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    color = activeContentColor
                ),
                placeholder = {
                    Text(
                        text = "0",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        color = activeContentColor.copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                leadingIcon = {
                    Text(
                        text = "¥",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        color = activeContentColor.copy(alpha = 0.5f),
                        modifier = Modifier.padding(start = 24.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = androidx.compose.material3.TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = activeContentColor
                )
            )
        }

        SpendInputField(
            value = uiState.inputDescription,
            onValueChange = onDescriptionChange,
            placeholder = "What did you buy?",
            icon = Icons.Outlined.ShoppingBag,
            isError = uiState.descriptionError != null,
            errorText = uiState.descriptionError
        )

        SpendInputField(
            value = uiState.inputNote,
            onValueChange = onNoteChange,
            placeholder = "Optional note...",
            icon = Icons.Outlined.EditNote
        )

        Spacer(modifier = Modifier.height(16.dp))

        val haptic = LocalHapticFeedback.current
        Surface(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onSave()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = CircleShape,
            color = activeColor,
            contentColor = if (isNeed) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onTertiary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = if (isEditing) "Update Expense" else "Save Expense",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun SpendDeleteDialog(entry: SpendEntry?, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(32.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        title = { Text("Delete Expense?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black) },
        text = { Text("Remove \"${entry?.description ?: ""}\"? This cannot be undone.", style = MaterialTheme.typography.bodyLarge) },
        confirmButton = {
            FilledTonalButton(
                onClick = onConfirm,
                shape = CircleShape,
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.error, contentColor = MaterialTheme.colorScheme.onError)
            ) { Text("Delete", fontWeight = FontWeight.Bold) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel", fontWeight = FontWeight.Bold) } }
    )
}

// ── Swipe Delete Background ───────────────────────────────

@Composable
fun SpendSwipeDeleteBackground() {
    val color by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.errorContainer,
        label = "spend_swipe_bg"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.medium)
            .background(color),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete",
            tint = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.padding(end = 24.dp)
        )
    }
}

// ── Spend Expressive Input Field ────────────────────────────────

@Composable
fun SpendInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isError: Boolean = false,
    errorText: String? = null
) {
    Column {
        androidx.compose.material3.OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontWeight = FontWeight.Medium) },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            },
            singleLine = true,
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp)
        )
        AnimatedVisibility(
            visible = isError && errorText != null,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Text(
                text = errorText ?: "",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

// ── History Bottom Sheet ──────────────────────────────────

@Composable
fun SpendHistoryBottomSheet(
    entries: List<SpendEntry>,
    onEdit: (SpendEntry) -> Unit,
    onDelete: (SpendEntry) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "History",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "${entries.size} total",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (entries.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No history yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 40.dp)
            ) {
                items(items = entries, key = { it.id }) { entry ->
                    ExpressiveListItem(
                        entry = entry,
                        onEdit = { onEdit(entry) },
                        onDelete = { onDelete(entry) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}