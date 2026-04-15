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
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.personal.kakeibox.ui.components.ExpressiveCategoryToggle
import com.personal.kakeibox.ui.components.ExpressiveEmptyState
import com.personal.kakeibox.ui.components.ExpressiveTab
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

    val filteredEntries = remember(uiState.selectedCategory, currentMonthEntries) {
        when (uiState.selectedCategory) {
            SpendCategory.NEED -> currentMonthEntries.filter { it.category == SpendCategory.NEED }
            SpendCategory.WANT -> currentMonthEntries.filter { it.category == SpendCategory.WANT }
            null -> currentMonthEntries
        }
    }

    val haptic = LocalHapticFeedback.current

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
                        IconButton(onClick = { viewModel.toggleHistorySheet() }) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(Icons.Outlined.History, contentDescription = "History", modifier = Modifier.padding(12.dp))
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
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", modifier = Modifier.size(36.dp))
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
            // ── Bento Box Hero Grid ──────────────────────
            item {
                AnimatedContent(
                    targetState = totalSpend,
                    transitionSpec = {
                        (fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                         slideInVertically(initialOffsetY = { it / 2 }))
                        .togetherWith(fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow)))
                    },
                    label = "hero_anim"
                ) { targetTotal ->
                    BentoHeroSection(
                        totalSpend = targetTotal,
                        totalNeed = totalNeed,
                        totalWant = totalWant,
                        salary = salary,
                        currentMonth = uiState.currentMonth,
                        currentYear = uiState.currentYear,
                        onPeriodClick = { /* Scroll to top or show picker if needed */ }
                    )
                }
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
            SpendHistoryBottomSheet(allEntries, viewModel::openEditSheet, viewModel::openDeleteDialog)
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
    onPeriodClick: () -> Unit
) {
    val salaryAmount = salary?.salaryAmount ?: 0L
    val remaining = salaryAmount - totalSpend
    
    Row(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Main Balance Card (Tall Bento)
        Surface(
            modifier = Modifier.weight(1.2f).fillMaxHeight(),
            shape = RoundedCornerShape(32.dp),
            color = if (remaining >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            contentColor = if (remaining >= 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onError
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(
                        text = DateUtils.getMonthName(currentMonth).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (remaining >= 0) "LEFT" else "OVER",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                }
                
                AnimatedContent(targetState = remaining) { valRemaining ->
                    Text(
                        text = CurrencyUtils.formatYen(valRemaining),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        fontSize = 28.sp
                    )
                }
            }
        }

        // Stats Stack (Right Bento Side)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            BentoStatSmall(
                title = "Needs",
                amount = totalNeed,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f)
            )
            BentoStatSmall(
                title = "Wants",
                amount = totalWant,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BentoStatSmall(title: String, amount: Long, containerColor: Color, contentColor: Color, modifier: Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), color = containerColor, contentColor = contentColor) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
            Text(text = title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            Text(text = CurrencyUtils.formatYen(amount), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
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
        shape = RoundedCornerShape(24.dp),
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
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items((1..12).toList()) { month ->
                    val isSelected = currentMonth == month
                    val bgColor by animateColorAsState(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                    val txtColor by animateColorAsState(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                    Surface(
                        onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onMonthChange(month) },
                        color = bgColor, contentColor = txtColor, shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(36.dp)
                    ) { Box(modifier = Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center) { Text(DateUtils.getShortMonthName(month), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold) } }
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
fun ExpressiveListItem(entry: SpendEntry, onEdit: () -> Unit, onDelete: () -> Unit) {
    val isNeed = entry.category == SpendCategory.NEED
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        onClick = onEdit
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
                Text(text = entry.note.ifBlank { "No note" }, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(text = CurrencyUtils.formatYen(entry.amount), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        }
    }
}



@Composable
fun SpendSwipeDeleteBackground() {
    Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(24.dp)).background(MaterialTheme.colorScheme.errorContainer), contentAlignment = Alignment.CenterEnd) {
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

    Column(modifier = Modifier.fillMaxWidth().padding(24.dp).navigationBarsPadding().imePadding(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text(text = if (uiState.editingEntry == null) "New Expense" else "Edit Expense", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        
        // Custom Category Toggle
        ExpressiveCategoryToggle(
            selectedCategory = uiState.inputCategory.name,
            onCategoryChange = { onCategoryChange(SpendCategory.valueOf(it)) }
        )

        OutlinedTextField(
            value = uiState.inputAmount,
            onValueChange = onAmountChange,
            label = { Text("Amount (¥)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(16.dp),
            textStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black)
        )

        OutlinedTextField(value = uiState.inputDescription, onValueChange = onDescriptionChange, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp))
        OutlinedTextField(value = uiState.inputNote, onValueChange = onNoteChange, label = { Text("Note (Optional)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp))

        Button(
            onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onSave() },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Save Transaction", fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun SpendHistoryBottomSheet(entries: List<SpendEntry>, onEdit: (SpendEntry) -> Unit, onDelete: (SpendEntry) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
        Text("History", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(entries) { entry -> ExpressiveListItem(entry, { onEdit(entry) }, { /* handle delete */ }) }
        }
    }
}
