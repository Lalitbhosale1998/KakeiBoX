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
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.R
import com.personal.kakeibox.data.entity.SalaryEntry
import com.personal.kakeibox.util.CurrencyUtils
import com.personal.kakeibox.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryScreen(
    viewModel: SalaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val allEntries by viewModel.allEntries.collectAsStateWithLifecycle()
    val currentEntry by viewModel.currentEntry.collectAsStateWithLifecycle()
    val totalSavings by viewModel.totalSavings.collectAsStateWithLifecycle()
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

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
                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                Text(
                                    text = "Monthly",
                                    style = MaterialTheme.typography.displaySmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Salary",
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
                                        Icons.Outlined.History,
                                        contentDescription = "History",
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                            actionIconContentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        scrollBehavior = scrollBehavior
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            floatingActionButton = {
                // Large, Expressive FAB correctly placed by Scaffold
                LargeFloatingActionButton(
                    onClick = { viewModel.openAddDialog() },
                    shape = RoundedCornerShape(28.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 8.dp,
                    bottom = innerPadding.calculateBottomPadding() + 80.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // ── Hero Section ──────────────
                item {
                    ExpressiveHeroCard(
                        entry = currentEntry,
                        currentMonth = uiState.currentMonth,
                        currentYear = uiState.currentYear,
                        onEdit = { currentEntry?.let { viewModel.openEditDialog(it) } }
                    )
                }

                // ── Detailed Stats ───────────
                item {
                    currentEntry?.let { entry ->
                        ExpressiveStatsGrid(entry = entry)
                    } ?: ExpressiveEmptyHero(onAdd = { viewModel.openAddDialog() })
                }

                // ── History Header ───────────
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent History",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black
                        )
                        TextButton(onClick = { viewModel.toggleHistorySheet() }) {
                            Text("See All")
                        }
                    }
                }

                // ── History List ─────────────
                if (allEntries.isEmpty()) {
                    item { ExpressiveEmptyState() }
                } else {
                    items(
                        items = allEntries.take(3),
                        key = { it.id }
                    ) { entry ->
                        ExpressiveSalaryCard(
                            entry = entry,
                            onEdit = { viewModel.openEditDialog(entry) },
                            onDelete = { viewModel.openDeleteDialog(entry) }
                        )
                    }
                }
            }
        }

        // LargeFloatingActionButton was here, but now moved to Scaffold's fab slot
    }

    // Sheets & Dialogs (Update to Tonal Backgrounds)
    if (uiState.showAddEditDialog) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeDialog() },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant) },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
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

    if (uiState.showDeleteDialog) {
        ExpressiveDeleteDialog(
            onConfirm = viewModel::deleteEntry,
            onDismiss = viewModel::closeDeleteDialog
        )
    }

    if (uiState.showHistorySheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleHistorySheet() },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant) },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            HistoryBottomSheet(
                entries = allEntries,
                onEdit = { entry -> viewModel.toggleHistorySheet(); viewModel.openEditDialog(entry) },
                onDelete = { entry -> viewModel.toggleHistorySheet(); viewModel.openDeleteDialog(entry) }
            )
        }
    }
}

@Composable
fun ExpressiveHeroCard(
    entry: SalaryEntry?,
    currentMonth: Int,
    currentYear: Int,
    onEdit: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
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
                    text = DateUtils.formatMonthYear(currentMonth, currentYear).uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (entry != null) CurrencyUtils.formatYen(entry.salaryAmount) else "No Data",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Net Take Home Salary",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
                
                if (entry != null) {
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
                        Text("Edit Details", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // ── Animated donut arc ─────────────────────────────
            if (entry != null) {
                val savingsRatio = if (entry.salaryAmount > 0) 
                    (entry.savingsAmount.toFloat() / entry.salaryAmount).coerceIn(0f, 1f)
                else 0f
                
                val animatedProgress by animateFloatAsState(
                    targetValue = savingsRatio,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "savings_progress"
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(90.dp)
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
                        Text(
                            text = "${(savingsRatio * 100).toInt()}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "SAVED",
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

@Composable
fun ExpressiveStatsGrid(entry: SalaryEntry) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExpressiveStatCard(
            title = "Savings Goal",
            amount = entry.savingsAmount,
            icon = Icons.Outlined.Savings,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            accentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
        ExpressiveStatCard(
            title = "Remittance",
            amount = entry.remittanceAmount,
            icon = Icons.AutoMirrored.Outlined.ExitToApp,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            accentColor = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ExpressiveStatCard(
    title: String,
    amount: Long,
    icon: ImageVector,
    containerColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        color = containerColor,
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = accentColor.copy(alpha = 0.15f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon, 
                        contentDescription = null, 
                        modifier = Modifier.size(20.dp),
                        tint = accentColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title, 
                style = MaterialTheme.typography.labelMedium, 
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = CurrencyUtils.formatYen(amount),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ExpressiveSalaryCard(
    entry: SalaryEntry,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        onClick = onEdit
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
                    text = CurrencyUtils.formatYen(entry.salaryAmount),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black
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
                        text = "Saved: ${CurrencyUtils.formatYen(entry.savingsAmount)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
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
fun ExpressiveEmptyState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "No history yet",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

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
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .navigationBarsPadding()
            .imePadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (uiState.editingEntry == null) "Add Salary" else "Edit Salary",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black
            )
            
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        // Period Selection (Month & Year)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Selection Period",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items((1..12).toList()) { month ->
                        val isSelected = uiState.inputMonth == month
                        Surface(
                            onClick = { 
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onMonthChange(month) 
                            },
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHighest,
                            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.size(width = 60.dp, height = 40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    DateUtils.getShortMonthName(month),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(DateUtils.getYearRange()) { year ->
                        val isSelected = uiState.inputYear == year
                        Surface(
                            onClick = { 
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onYearChange(year) 
                            },
                            color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceContainerHighest,
                            contentColor = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = year.toString(),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = uiState.inputSalary,
            onValueChange = onSalaryChange,
            label = { Text("Net Salary Amount (¥)", fontWeight = FontWeight.Bold) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(20.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = uiState.inputSavings,
                onValueChange = onSavingsChange,
                label = { Text("Savings Goal") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = uiState.inputRemittance,
                onValueChange = onRemittanceChange,
                label = { Text("Remittance") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.inputNote,
            onValueChange = onNoteChange,
            label = { Text("Add a note (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            placeholder = { Text("Bonus, overtime, etc.") }
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onSave()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Save Salary Entry", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ExpressiveDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Entry?", fontWeight = FontWeight.Bold) },
        text = { Text("This action cannot be undone.") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
fun HistoryBottomSheet(
    entries: List<SalaryEntry>,
    onEdit: (SalaryEntry) -> Unit,
    onDelete: (SalaryEntry) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
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
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(entries) { entry ->
                ExpressiveSalaryCard(
                    entry = entry,
                    onEdit = { onEdit(entry) },
                    onDelete = { onDelete(entry) }
                )
            }
        }
    }
}
