package com.personal.kakeibox.ui.commute

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
import com.personal.kakeibox.ui.components.ExpressiveSnackbarHost
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.R
import com.personal.kakeibox.data.entity.CommuteEntry
import com.personal.kakeibox.ui.components.BentoCard
import com.personal.kakeibox.ui.components.ExpressiveEmptyState
import com.personal.kakeibox.util.CurrencyUtils
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Train
import androidx.compose.ui.text.style.TextOverflow
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommuteScreen(
    viewModel: CommuteViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val themeSettings by themeViewModel.themeSettings.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val haptic = LocalHapticFeedback.current

    val isFloatingNav = themeSettings.navBarStyle == NavBarStyle.FLOATING
    val fabPadding by animateDpAsState(
        targetValue = if (isFloatingNav) 100.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "fab_padding"
    )

    val snackbarHostState = remember { SnackbarHostState() }

    val topAppBarContainerColor by animateColorAsState(
        targetValue = when (themeSettings.topAppBarBackground) {
            TopAppBarBackground.SURFACE -> MaterialTheme.colorScheme.surface
            TopAppBarBackground.PRIMARY_CONTAINER -> MaterialTheme.colorScheme.primaryContainer
            TopAppBarBackground.SURFACE_CONTAINER -> MaterialTheme.colorScheme.surfaceContainer
        },
        label = "top_app_bar_container_color"
    )

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            // Snappier 2-second timeout for Expressive Snackbars
            withTimeoutOrNull(2000L) {
                snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Indefinite)
            }
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = topAppBarContainerColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            LargeTopAppBar(
                title = {
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = "Work",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Commute",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = { 
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.toggleHistory() 
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topAppBarContainerColor,
                    scrolledContainerColor = topAppBarContainerColor
                )
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { 
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.openAddSheet() 
                },
                modifier = Modifier.padding(bottom = fabPadding),
                shape = RoundedCornerShape(28.dp),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", modifier = Modifier.size(36.dp))
            }
        },
        snackbarHost = { ExpressiveSnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp,
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                CommuteHeroSection(uiState.totalCostAllTime, isPrivacyMode = themeSettings.privacyModeEnabled)
            }

            if (uiState.latestEntry == null) {
                item {
                    ExpressiveEmptyState(
                        message = "No commute logs yet",
                        icon = "🚌"
                    )
                }
            } else {
                item {
                    CommuteDetailsBento(uiState.latestEntry!!)
                }

                item {
                    Text(
                        text = "History",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
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

                    SwipeToDismissBox(
                        state = swipeState,
                        enableDismissFromStartToEnd = false,
                        backgroundContent = { CommuteSwipeDeleteBackground() },
                        content = {
                            CommuteHistoryItem(
                                entry = entry,
                                isPrivacyMode = themeSettings.privacyModeEnabled,
                                onDelete = { viewModel.openDeleteDialog(entry) }
                            )
                        }
                    )
                }
            }
        }
    }

    if (uiState.showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeAddSheet() },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant) },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            CommuteAddEditSheet(
                uiState = uiState,
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
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant) },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            CommuteHistoryBottomSheet(
                entries = uiState.history,
                isPrivacyMode = themeSettings.privacyModeEnabled,
                onDelete = { viewModel.openDeleteDialog(it) }
            )
        }
    }
}

@Composable
fun CommuteHeroSection(totalCost: Long, isPrivacyMode: Boolean = false) {
    BentoCard(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        title = "TOTAL COMMUTE COST",
        icon = Icons.Outlined.Train,
        isActive = true,
        activeContainerColor = MaterialTheme.colorScheme.tertiary,
        activeContentColor = MaterialTheme.colorScheme.onTertiary
    ) {
        Column {
            Text(
                text = "Cumulative",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = LocalContentColor.current.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = CurrencyUtils.formatYen(totalCost, isPrivacyMode),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
fun CommuteDetailsBento(entry: CommuteEntry) {
    Row(
        modifier = Modifier.fillMaxWidth().height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BentoCard(
            title = "Office Days",
            icon = Icons.Outlined.Business,
            idleContainerColor = MaterialTheme.colorScheme.primaryContainer,
            idleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = entry.totalCommuteDays.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black
            )
        }
        BentoCard(
            title = "WFH Days",
            icon = Icons.Outlined.Home,
            idleContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            idleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = entry.wfhDays.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black
            )
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
fun CommuteHistoryItem(
    entry: CommuteEntry,
    isPrivacyMode: Boolean = false,
    onDelete: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val date = remember(entry.createdAt) {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(entry.createdAt))
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
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
                    text = CurrencyUtils.formatYen(entry.totalCost, isPrivacyMode),
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
                        text = "Fare: ${CurrencyUtils.formatYen(entry.oneWayFare, isPrivacyMode)} (One-way)",
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
    onDelete: (CommuteEntry) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
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
                            onDelete = { onDelete(entry) }
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
                                text = "¥",
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
        val daysFocused = isHolidaysFocused || isWfhFocused
        val daysElevation by animateDpAsState(if (daysFocused) 8.dp else 0.dp)
        val daysScale by animateFloatAsState(if (daysFocused) 1.02f else 1f)

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(28.dp),
            tonalElevation = daysElevation,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(scaleX = daysScale, scaleY = daysScale)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Monthly Adjustments",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = uiState.inputHolidays,
                        onValueChange = onHolidaysChange,
                        label = { Text("Holidays") },
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged { isHolidaysFocused = it.isFocused },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = { Icon(Icons.Outlined.EventBusy, contentDescription = null) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                    OutlinedTextField(
                        value = uiState.inputWfhDays,
                        onValueChange = onWfhChange,
                        label = { Text("WFH Days") },
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged { isWfhFocused = it.isFocused },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = { Icon(Icons.Outlined.Home, contentDescription = null) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        val isInputValid = uiState.inputOneWayFare.isNotBlank() && uiState.inputOneWayFare.toDoubleOrNull() != null

        Button(
            onClick = {
                if (isInputValid) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSave()
                }
            },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            enabled = isInputValid,
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isInputValid) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isInputValid) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 4.dp)
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
