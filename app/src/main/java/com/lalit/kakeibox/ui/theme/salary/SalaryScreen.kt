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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CurrencyYen
import androidx.compose.material.icons.outlined.SendToMobile
import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.R
import com.personal.kakeibox.data.entity.SalaryEntry
import com.personal.kakeibox.util.CurrencyUtils
import com.personal.kakeibox.util.DateUtils
import androidx.compose.foundation.layout.WindowInsets
//import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.outlined.EditNote

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryScreen(
    viewModel: SalaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val allEntries by viewModel.allEntries.collectAsStateWithLifecycle()
    val currentEntry by viewModel.currentEntry.collectAsStateWithLifecycle()
    val totalSavings by viewModel.totalSavings.collectAsStateWithLifecycle()
    //val navBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                LargeTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.salary_title),
                            fontWeight = FontWeight.ExtraBold
                        )
                    },
                    actions = {
                        FilledTonalIconButton(
                            onClick = { viewModel.toggleHistorySheet() }
                        ) {
                            Icon(
                                Icons.Filled.History,
                                contentDescription = stringResource(R.string.history)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    scrollBehavior = scrollBehavior
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 120.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                                expandVertically(spring(stiffness = Spring.StiffnessMediumLow))
                    ) {
                        HeroSummaryCard(
                            entry = currentEntry,
                            currentMonth = uiState.currentMonth,
                            currentYear = uiState.currentYear,
                            totalSavings = totalSavings ?: 0L,
                            onEdit = { currentEntry?.let { viewModel.openEditDialog(it) } },
                            onDelete = { currentEntry?.let { viewModel.openDeleteDialog(it) } },
                            allEntries = allEntries
                        )
                    }
                }

                currentEntry?.let { entry ->
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInVertically(
                                spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                            ) { it / 2 } + fadeIn()
                        ) {
                            BreakdownPillsRow(entry = entry)
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.history),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        AnimatedContent(
                            targetState = allEntries.size,
                            label = "count"
                        ) { count ->
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "$count entries",
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 4.dp
                                    ),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                if (allEntries.isEmpty()) {
                    item { ExpressiveEmptyState() }
                } else {
                    items(items = allEntries, key = { it.id }) { entry ->
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
                            backgroundContent = { SwipeDeleteBackground() }
                        ) {
                            ExpressiveSalaryCard(
                                entry = entry,
                                onEdit = { viewModel.openEditDialog(entry) },
                                onDelete = { viewModel.openDeleteDialog(entry) }
                            )
                        }
                    }
                }
            }
        }

        // FAB placed in Box — always visible above everything
        ExpressiveFab(
            onClick = { viewModel.openAddDialog() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 110.dp)
        )
    }

    // ── Add/Edit Bottom Sheet ─────────────────────────
    if (uiState.showAddEditDialog) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeDialog() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 4.dp)
                        .size(width = 40.dp, height = 4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        )
                )
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            ExpressiveAddEditSheet(
                uiState = uiState,
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

    // ── Delete Dialog ─────────────────────────────────
    if (uiState.showDeleteDialog) {
        ExpressiveDeleteDialog(
            onConfirm = viewModel::deleteEntry,
            onDismiss = viewModel::closeDeleteDialog
        )
    }

    // ── History Bottom Sheet ──────────────────────────
    if (uiState.showHistorySheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleHistorySheet() },
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            HistoryBottomSheet(
                entries = allEntries,
                onEdit = { entry ->
                    viewModel.toggleHistorySheet()
                    viewModel.openEditDialog(entry)
                },
                onDelete = { entry ->
                    viewModel.toggleHistorySheet()
                    viewModel.openDeleteDialog(entry)
                }
            )
        }
    }
}

// ── M3 Expressive FAB ────────────────────────────────────

@Composable
fun ExpressiveFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 12.dp
        ),
        icon = {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        text = {
            Text(
                text = stringResource(R.string.add_entry),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black
            )
        }
    )
}

// ── Hero Summary Card ─────────────────────────────────────

@Composable
fun HeroSummaryCard(
    entry: SalaryEntry?,
    currentMonth: Int,
    currentYear: Int,
    totalSavings: Long,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    allEntries: List<SalaryEntry>
) {
    val totalSalary = allEntries.sumOf { it.salaryAmount }
    val totalRemittance = allEntries.sumOf { it.remittanceAmount }
    val totalRemaining = allEntries.sumOf { it.remainingAmount }
    val entryCount = allEntries.size

    val primary = MaterialTheme.colorScheme.primary
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        primary,
                        primary.copy(alpha = 0.85f),
                        primaryContainer
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ALL TIME OVERVIEW",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "$entryCount ${if (entryCount == 1) "Month" else "Months"} Recorded",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (allEntries.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "💰", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No entries yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Tap + Add Entry to get started",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                }
            } else {
                Text(
                    text = "TOTAL SALARY EARNED",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    letterSpacing = 2.sp
                )
                AnimatedContent(
                    targetState = totalSalary,
                    transitionSpec = {
                        slideInVertically { -it } + fadeIn() togetherWith
                                slideOutVertically { it } + fadeOut()
                    },
                    label = "total_salary"
                ) { amount ->
                    Text(
                        text = CurrencyUtils.formatYen(amount),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.AccountBalanceWallet,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Total Remaining: ${CurrencyUtils.formatYen(totalRemaining)}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            ),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Savings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Saved: ${CurrencyUtils.formatYen(totalSavings)}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            ),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.SendToMobile,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Sent: ${CurrencyUtils.formatYen(totalRemittance)}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "This month (${DateUtils.formatMonthYear(
                            currentMonth,
                            currentYear
                        )})",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (entry != null) {
                            Text(
                                text = CurrencyUtils.formatYen(entry.salaryAmount),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Surface(
                                onClick = onEdit,
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f)
                            ) {
                                Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .size(14.dp)
                                )
                            }
                            Surface(
                                onClick = onDelete,
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f)
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .size(14.dp)
                                )
                            }
                        } else {
                            Surface(
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "No entry",
                                    modifier = Modifier.padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    ),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Breakdown Pills ───────────────────────────────────────

@Composable
fun BreakdownPillsRow(entry: SalaryEntry) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BreakdownPill(
            label = stringResource(R.string.remittance_amount),
            amount = CurrencyUtils.formatYen(entry.remittanceAmount),
            icon = Icons.Outlined.SendToMobile,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.weight(1f)
        )
        BreakdownPill(
            label = stringResource(R.string.savings_amount),
            amount = CurrencyUtils.formatYen(entry.savingsAmount),
            icon = Icons.Filled.Savings,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun BreakdownPill(
    label: String,
    amount: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.8f)
            )
            Text(
                text = amount,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}

// ── Expressive History Card ───────────────────────────────

@Composable
fun ExpressiveSalaryCard(
    entry: SalaryEntry,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = DateUtils.formatMonthYear(entry.month, entry.year),
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 4.dp
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = CurrencyUtils.formatYen(entry.salaryAmount),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MiniStatChip(
                    label = "Remittance",
                    value = CurrencyUtils.formatYen(entry.remittanceAmount),
                    color = MaterialTheme.colorScheme.error
                )
                MiniStatChip(
                    label = "Savings",
                    value = CurrencyUtils.formatYen(entry.savingsAmount),
                    color = MaterialTheme.colorScheme.tertiary
                )
                MiniStatChip(
                    label = "Remaining",
                    value = CurrencyUtils.formatYen(entry.remainingAmount),
                    color = if (entry.remainingAmount >= 0)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }

            if (entry.note.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "📝 ${entry.note}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun MiniStatChip(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

// ── Expressive Empty State ────────────────────────────────

@Composable
fun ExpressiveEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = "💰", fontSize = 36.sp)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "No salary entries yet",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Tap + Add Entry to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ── Expressive Add/Edit Bottom Sheet ─────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpressiveAddEditSheet(
    uiState: SalaryUiState,
    onSalaryChange: (String) -> Unit,
    onRemittanceChange: (String) -> Unit,
    onSavingsChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onMonthChange: (Int) -> Unit,
    onYearChange: (Int) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    val isEditing = uiState.editingEntry != null
    val salary = uiState.inputSalary.toLongOrNull() ?: 0L
    val remittance = uiState.inputRemittance.toLongOrNull() ?: 0L
    val savings = uiState.inputSavings.toLongOrNull() ?: 0L
    val remaining = salary - remittance - savings

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isEditing) "Edit Entry" else "New Entry",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = if (isEditing) "Update your salary data"
                    else "Add your monthly salary",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDismiss) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            MonthDropdown(
                selectedMonth = uiState.inputMonth,
                onMonthSelected = onMonthChange,
                modifier = Modifier.weight(1f)
            )
            YearDropdown(
                selectedYear = uiState.inputYear,
                onYearSelected = onYearChange,
                modifier = Modifier.weight(1f)
            )
        }

        ExpressiveInputField(
            value = uiState.inputSalary,
            onValueChange = onSalaryChange,
            label = stringResource(R.string.salary_amount),
            icon = Icons.Outlined.CurrencyYen,
            isError = uiState.salaryError != null,
            errorText = uiState.salaryError,
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )

        ExpressiveInputField(
            value = uiState.inputRemittance,
            onValueChange = onRemittanceChange,
            label = stringResource(R.string.remittance_amount),
            icon = Icons.Outlined.SendToMobile,
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
        )

        ExpressiveInputField(
            value = uiState.inputSavings,
            onValueChange = onSavingsChange,
            label = stringResource(R.string.savings_amount),
            icon = Icons.Filled.Savings,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        )

        AnimatedContent(
            targetState = remaining,
            transitionSpec = {
                slideInVertically { -it } + fadeIn(tween(200)) togetherWith
                        slideOutVertically { it } + fadeOut(tween(200))
            },
            label = "remaining_preview"
        ) { rem ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (rem >= 0) MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.errorContainer
                    )
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Outlined.AccountBalanceWallet,
                            contentDescription = null,
                            tint = if (rem >= 0)
                                MaterialTheme.colorScheme.onSecondaryContainer
                            else MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(R.string.remaining),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (rem >= 0)
                                MaterialTheme.colorScheme.onSecondaryContainer
                            else MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                    Text(
                        text = CurrencyUtils.formatYen(rem),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (rem >= 0)
                            MaterialTheme.colorScheme.onSecondaryContainer
                        else MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }

        ExpressiveInputField(
            value = uiState.inputNote,
            onValueChange = onNoteChange,
            label = "Note (optional)",
            icon = Icons.Outlined.EditNote
        )

        val haptic = LocalHapticFeedback.current
        Surface(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onSave()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.primary
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEditing) "Update Entry"
                    else stringResource(R.string.save),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

// ── Swipe Delete Background ──────────────────────────────

@Composable
fun SwipeDeleteBackground() {
    val color by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.errorContainer,
        label = "swipe_bg"
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

// ── Expressive Input Field ────────────────────────────────

@Composable
fun ExpressiveInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isError: Boolean = false,
    errorText: String? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
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

// ── Month Dropdown ────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthDropdown(
    selectedMonth: Int,
    onMonthSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = DateUtils.getShortMonthName(selectedMonth),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.month)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            (1..12).forEach { month ->
                DropdownMenuItem(
                    text = { Text(DateUtils.getShortMonthName(month)) },
                    onClick = { onMonthSelected(month); expanded = false }
                )
            }
        }
    }
}

// ── Year Dropdown ─────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearDropdown(
    selectedYear: Int,
    onYearSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedYear.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Year") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DateUtils.getYearRange().forEach { year ->
                DropdownMenuItem(
                    text = { Text(year.toString()) },
                    onClick = { onYearSelected(year); expanded = false }
                )
            }
        }
    }
}

// ── Delete Confirm Dialog ─────────────────────────────────

@Composable
fun ExpressiveDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        title = {
            Text(
                text = "Delete Entry?",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = "This entry will be permanently removed. This action cannot be undone.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            FilledTonalButton(
                onClick = onConfirm,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    stringResource(R.string.delete_entry),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

// ── History Bottom Sheet ──────────────────────────────────

@Composable
fun HistoryBottomSheet(
    entries: List<SalaryEntry>,
    onEdit: (SalaryEntry) -> Unit,
    onDelete: (SalaryEntry) -> Unit
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
                text = stringResource(R.string.history),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "${entries.size} total",
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (entries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No history yet",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 40.dp)
            ) {
                items(items = entries, key = { it.id }) { entry ->
                    ExpressiveSalaryCard(
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