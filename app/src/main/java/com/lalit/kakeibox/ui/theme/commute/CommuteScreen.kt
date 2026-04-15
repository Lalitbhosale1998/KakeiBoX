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
import com.personal.kakeibox.ui.settings.ThemeViewModel
import com.personal.kakeibox.data.preferences.NavBarStyle
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.R
import com.personal.kakeibox.data.entity.CommuteEntry
import com.personal.kakeibox.ui.components.ExpressiveEmptyState
import com.personal.kakeibox.util.CurrencyUtils
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

    val isFloatingNav = themeSettings.navBarStyle == NavBarStyle.FLOATING
    val fabPadding by animateDpAsState(
        targetValue = if (isFloatingNav) 100.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "fab_padding"
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
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
                    IconButton(onClick = { viewModel.toggleHistory() }) {
                        Icon(Icons.Outlined.History, contentDescription = "History")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { viewModel.openAddSheet() },
                modifier = Modifier.padding(bottom = fabPadding),
                shape = RoundedCornerShape(28.dp),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", modifier = Modifier.size(36.dp))
            }
        }
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
                CommuteHeroSection(uiState.latestEntry)
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
            }
        }
    }

    if (uiState.showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeAddSheet() },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            CommuteAddEditSheet(
                uiState = uiState,
                onFareChange = viewModel::updateFare,
                onHolidaysChange = viewModel::updateHolidays,
                onWfhChange = viewModel::updateWfhDays,
                onSave = viewModel::saveEntry
            )
        }
    }
}

@Composable
fun CommuteHeroSection(entry: CommuteEntry?) {
    val cost = entry?.totalCost ?: 0L
    
    Surface(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary
    ) {
        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(
                    text = "ESTIMATED COST",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Current Period",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = LocalContentColor.current.copy(alpha = 0.7f)
                )
            }
            
            Text(
                text = CurrencyUtils.formatYen(cost),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
fun CommuteDetailsBento(entry: CommuteEntry) {
    Row(
        modifier = Modifier.fillMaxWidth().height(140.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CommuteStatCard(
            title = "Office Days",
            value = entry.totalCommuteDays.toString(),
            icon = Icons.Outlined.Business,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.weight(1f)
        )
        CommuteStatCard(
            title = "WFH Days",
            value = entry.wfhDays.toString(),
            icon = Icons.Outlined.Home,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CommuteStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(24.dp),
        color = containerColor
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Column {
                Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Text(text = title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
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
    onSave: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp).navigationBarsPadding().imePadding(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Estimate Commute", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        
        OutlinedTextField(
            value = uiState.inputOneWayFare,
            onValueChange = onFareChange,
            label = { Text("One-way Fare (¥)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(16.dp),
            isError = uiState.fareError != null
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = uiState.inputHolidays,
                onValueChange = onHolidaysChange,
                label = { Text("Holidays") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = uiState.inputWfhDays,
                onValueChange = onWfhChange,
                label = { Text("WFH Days") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(16.dp)
            )
        }

        Button(
            onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onSave() },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
        ) {
            Text("Calculate & Save", fontWeight = FontWeight.Black)
        }
    }
}
