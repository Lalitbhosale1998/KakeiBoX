package com.personal.kakeibox.ui.exercise

import androidx.activity.ComponentActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.R
import com.personal.kakeibox.data.entity.ExerciseEntry
import com.personal.kakeibox.data.preferences.NavBarStyle
import com.personal.kakeibox.data.preferences.ThemeSettings
import com.personal.kakeibox.data.preferences.TopAppBarBackground
import com.personal.kakeibox.data.preferences.ThemeStyle
import com.personal.kakeibox.ui.theme.LocalThemeStyle
import com.personal.kakeibox.ui.theme.expressiveBackground
import com.personal.kakeibox.data.preferences.BackdropPattern
import androidx.compose.foundation.isSystemInDarkTheme
import com.personal.kakeibox.ui.components.*
import com.personal.kakeibox.ui.settings.ThemeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen(
    viewModel: ExerciseViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val themeSettings by themeViewModel.themeSettings.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current

    val completedSetsMap = remember(uiState.selectedDay) { mutableStateMapOf<Int, Set<Int>>() }
    
    val totalTargetSets = remember(uiState.exercises) {
        uiState.exercises.sumOf { it.sets }
    }
    
    val totalCompletedSets by remember {
        derivedStateOf {
            uiState.exercises.sumOf { exercise ->
                completedSetsMap[exercise.id]?.size ?: 0
            }
        }
    }

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

    LaunchedEffect(Unit) {
        themeViewModel.onAddActionButtonClicked.collect { route ->
            if (route == "exercise" && !uiState.isRestDay) {
                viewModel.openAddSheet()
            }
        }
    }

    val isPrimaryContainer = themeSettings.topAppBarBackground == TopAppBarBackground.PRIMARY_CONTAINER
    val onContainerColor = if (isPrimaryContainer) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    val primaryTextAccent = if (isPrimaryContainer) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary

    val topAppBarContainerColor by animateColorAsState(
        targetValue = when (themeSettings.topAppBarBackground) {
            TopAppBarBackground.SURFACE -> MaterialTheme.colorScheme.background
            TopAppBarBackground.PRIMARY_CONTAINER -> androidx.compose.ui.graphics.lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.primaryContainer, 0.35f)
        },
        label = "top_app_bar_container_color"
    )

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

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

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
            topBar = {}
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

                // ── Day of Week Selector Card ───────────
                item {
                    val scrollState = rememberScrollState()
                    val dayBounds = remember {
                        mutableStateListOf<Pair<Float, Float>>().apply {
                            repeat(daysOfWeek.size) { add(Pair(0f, 0f)) }
                        }
                    }
                    val selectedIndex = daysOfWeek.indexOf(uiState.selectedDay).coerceAtLeast(0)
                    val targetBounds = dayBounds.getOrNull(selectedIndex) ?: Pair(0f, 0f)

                    LaunchedEffect(selectedIndex, targetBounds) {
                        if (targetBounds.second > 0f) {
                            val viewportWidth = scrollState.viewportSize
                            if (viewportWidth > 0) {
                                val centerScroll = targetBounds.first - (viewportWidth / 2f) + (targetBounds.second / 2f)
                                scrollState.animateScrollTo(centerScroll.roundToInt().coerceIn(0, scrollState.maxValue))
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(28.dp))
                            .background(bentoIdleColor)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "WEEKLY SCHEDULE",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(scrollState)
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                // ── Morphing Sliding Background ──
                                if (targetBounds.second > 0f) {
                                    val animatedX by animateFloatAsState(
                                        targetValue = targetBounds.first,
                                        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow),
                                        label = "day_pill_x"
                                    )
                                    val animatedWidth by animateFloatAsState(
                                        targetValue = targetBounds.second,
                                        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow),
                                        label = "day_pill_width"
                                    )

                                    val distance = targetBounds.first - animatedX
                                    val absDistance = java.lang.Math.abs(distance)
                                    val stretchX = 1f + (absDistance / 200f).coerceAtMost(0.25f)
                                    val squashY = 1f - (absDistance / 600f).coerceAtMost(0.12f)

                                    Box(
                                        modifier = Modifier
                                            .offset { IntOffset(animatedX.roundToInt(), 0) }
                                            .width(with(LocalDensity.current) { animatedWidth.toDp() })
                                            .height(76.dp)
                                            .graphicsLayer {
                                                scaleX = stretchX
                                                scaleY = squashY
                                                transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0.5f)
                                            }
                                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(20.dp))
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    daysOfWeek.forEachIndexed { index, day ->
                                        val isSelected = day == uiState.selectedDay
                                        val isRest = uiState.restDays.contains(day)
                                        val scale by animateFloatAsState(
                                            targetValue = if (isSelected) 1.05f else 1.0f,
                                            animationSpec = spring(stiffness = Spring.StiffnessMedium),
                                            label = "day_scale"
                                        )

                                        val containerColor = when {
                                            isSelected -> Color.Transparent
                                            isRest -> MaterialTheme.colorScheme.surfaceVariant
                                            else -> MaterialTheme.colorScheme.surfaceContainerHighest
                                        }

                                        val contentColor = when {
                                            isSelected -> MaterialTheme.colorScheme.onPrimary
                                            isRest -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                                        }

                                        Surface(
                                            onClick = {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                viewModel.selectDay(day)
                                            },
                                            shape = RoundedCornerShape(20.dp),
                                            color = containerColor,
                                            contentColor = contentColor,
                                            modifier = Modifier
                                                .graphicsLayer(scaleX = scale, scaleY = scale)
                                                .width(76.dp)
                                                .height(76.dp)
                                                .onGloballyPositioned { coordinates ->
                                                    val parent = coordinates.parentLayoutCoordinates
                                                    if (parent != null) {
                                                        val localPos = parent.localPositionOf(coordinates, Offset.Zero)
                                                        val newBounds = Pair(localPos.x, coordinates.size.width.toFloat())
                                                        if (index < dayBounds.size && dayBounds[index] != newBounds) {
                                                            dayBounds[index] = newBounds
                                                        }
                                                    }
                                                },
                                            border = if (isRest && !isSelected) {
                                                BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                            } else null
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center,
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                Text(
                                                    text = day.substring(0, 3).uppercase(),
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Black
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Icon(
                                                    imageVector = if (isRest) Icons.Outlined.Coffee else Icons.Outlined.FitnessCenter,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp),
                                                    tint = contentColor.copy(alpha = 0.8f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // ── Rest Day toggle for current day ────────
                item {
                    val isRest = uiState.isRestDay
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isRest) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f) else bentoIdleColor
                        ),
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.toggleRestDay(uiState.selectedDay)
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isRest) Icons.Filled.Coffee else Icons.Outlined.Coffee,
                                contentDescription = null,
                                tint = if (isRest) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isRest) "Rest & Recovery Mode" else "Active Training Mode",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (isRest) "Tap to schedule workouts for ${uiState.selectedDay}" else "Tap to mark ${uiState.selectedDay} as a Rest Day",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                            Switch(
                                checked = isRest,
                                onCheckedChange = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.toggleRestDay(uiState.selectedDay)
                                }
                            )
                        }
                    }
                }

                // ── Dashboard Content ──
                if (uiState.isRestDay) {
                    item {
                        RestDayDashboard(dayName = uiState.selectedDay)
                    }
                } else if (uiState.exercises.isEmpty()) {
                    item {
                        ExpressiveEmptyState(
                            message = "No workouts logged for ${uiState.selectedDay}",
                            icon = "🏋️",
                            color = onContainerColor
                        )
                    }
                } else {
                    item {
                        DailyProgressDashboard(
                            completedSets = totalCompletedSets,
                            totalSets = totalTargetSets,
                            exercises = uiState.exercises,
                            containerColor = bentoIdleColor
                        )
                    }

                    item {
                        Text(
                            text = "WORKOUT ROUTINE",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }

                    items(
                        items = uiState.exercises,
                        key = { it.id }
                    ) { exercise ->
                        val swipeState = rememberSwipeToDismissBoxState()

                        LaunchedEffect(swipeState.currentValue) {
                            if (swipeState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.deleteExercise(exercise)
                                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
                            }
                        }

                        SwipeToDismissBox(
                            state = swipeState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(28.dp))
                                        .background(MaterialTheme.colorScheme.errorContainer),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        modifier = Modifier.padding(end = 24.dp),
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            },
                            content = {
                                WorkoutItem(
                                    exercise = exercise,
                                    completedSets = completedSetsMap[exercise.id] ?: emptySet(),
                                    onToggleSet = { setNum ->
                                        val currentCompleted = completedSetsMap[exercise.id] ?: emptySet()
                                        val newCompleted = if (currentCompleted.contains(setNum)) {
                                            currentCompleted - setNum
                                        } else {
                                            currentCompleted + setNum
                                        }
                                        completedSetsMap[exercise.id] = newCompleted
                                    },
                                    onEdit = { viewModel.openEditSheet(exercise) },
                                    onDelete = { viewModel.deleteExercise(exercise) },
                                    containerColor = bentoIdleColor
                                )
                            }
                        )
                    }
                }
            }
        }

        ExpressiveCollapsingHeader(
            title = "Workout",
            subtitle = "Tracker",
            scrollOffset = scrollOffset,
            maxOffset = maxOffsetPx,
            containerColor = topAppBarContainerColor,
            onContainerColor = onContainerColor,
            primaryTextAccent = primaryTextAccent,
            actions = {}
        )
    }

    if (uiState.showAddEditSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeAddEditSheet() },
            containerColor = sheetColorScheme.surfaceContainer,
            dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant) },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            MaterialTheme(colorScheme = sheetColorScheme) {
                ExerciseAddEditSheet(
                    exercise = uiState.selectedExercise,
                    selectedDay = uiState.selectedDay,
                    onSave = { name, sets, reps, description, day ->
                        viewModel.saveExercise(name, sets, reps, description, day)
                    },
                    onDismiss = { viewModel.closeAddEditSheet() }
                )
            }
        }
    }
}

@Composable
fun DailyProgressDashboard(
    completedSets: Int,
    totalSets: Int,
    exercises: List<ExerciseEntry>,
    containerColor: Color
) {
    val progress = if (totalSets > 0) completedSets.toFloat() / totalSets else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "progress_gauge"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val trackColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp)),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Radial Progress Circle Gauge
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(100.dp)
            ) {
                androidx.compose.foundation.Canvas(modifier = Modifier.size(90.dp)) {
                    // Track Circle
                    drawCircle(
                        color = trackColor,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 8.dp.toPx())
                    )
                    // Progress Arc
                    drawArc(
                        color = primaryColor,
                        startAngle = -90f,
                        sweepAngle = animatedProgress * 360f,
                        useCenter = false,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 8.dp.toPx(),
                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                    )
                }
                
                // Percentage Text
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${(progress * 100).roundToInt()}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "DONE",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            // Details and statistics
            Column(
                modifier = Modifier.weight(1f)
            ) {
                val focusArea = remember(exercises) {
                    val categories = exercises.map { exercise ->
                        val nameLower = exercise.name.lowercase()
                        when {
                            nameLower.contains("run") || nameLower.contains("jog") || nameLower.contains("cardio") || nameLower.contains("treadmill") -> "Cardio"
                            nameLower.contains("squat") || nameLower.contains("leg") || nameLower.contains("lunge") -> "Legs"
                            nameLower.contains("pushup") || nameLower.contains("press") || nameLower.contains("chest") || nameLower.contains("bench") -> "Chest"
                            nameLower.contains("pullup") || nameLower.contains("row") || nameLower.contains("back") || nameLower.contains("deadlift") -> "Back"
                            nameLower.contains("stretch") || nameLower.contains("yoga") || nameLower.contains("flex") || nameLower.contains("warm") -> "Flexibility"
                            else -> "Strength"
                        }
                    }
                    categories.groupBy { it }
                        .maxByOrNull { it.value.size }?.key ?: "Strength"
                }

                val statusText = when {
                    progress == 0f -> "Time to crush it! ⚡"
                    progress < 0.4f -> "Warmup finished, let's go! 🔥"
                    progress < 1.0f -> "More than halfway there! Keep pushing! 💪"
                    else -> "Routine completed! Hero status! 🏆"
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = primaryColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "$focusArea Focus".uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = primaryColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = statusText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Completed $completedSets of $totalSets sets across ${exercises.size} workouts today.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun RestDayDashboard(dayName: String) {
    val haptic = LocalHapticFeedback.current
    val recoveryQuotes = remember {
        listOf(
            "Rest is not quitting. It is the fuel for your next climb.",
            "Your muscles grow when you rest, not when you work.",
            "Recovery is a key part of the training cycle, not a break from it.",
            "Listen to your body. Today is for recovery and rebuild.",
            "Sometimes, the most productive thing you can do is rest.",
            "Grow stronger in the stillness.",
            "Champions know when to train, and when to rest."
        )
    }

    val quote = remember(dayName) {
        recoveryQuotes[Math.abs(dayName.hashCode()) % recoveryQuotes.size]
    }

    var isBreathingActive by remember { mutableStateOf(false) }
    var breatheStage by remember { mutableStateOf("READY TO BREATHE") }
    var scaleTarget by remember { mutableStateOf(1f) }

    val animatedBreatheScale by animateFloatAsState(
        targetValue = scaleTarget,
        animationSpec = tween(durationMillis = 4000, easing = EaseInOutSine),
        label = "breathe_scale"
    )

    LaunchedEffect(isBreathingActive) {
        if (isBreathingActive) {
            while (true) {
                breatheStage = "INHALE..."
                scaleTarget = 1.7f
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                delay(4000)

                breatheStage = "HOLD..."
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                delay(4000)

                breatheStage = "EXHALE..."
                scaleTarget = 1.0f
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                delay(4000)

                breatheStage = "HOLD..."
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                delay(4000)
            }
        } else {
            breatheStage = "TAP START TO BEGIN"
            scaleTarget = 1.0f
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Recovery Quote card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f))
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Coffee,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "RECOVERY INSIGHT",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = quote,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Breathing Coach bento card
        BentoCard(
            modifier = Modifier.fillMaxWidth(),
            title = "BOX BREATHING COACH",
            icon = Icons.Default.Spa,
            isActive = true,
            activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
            activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                // Interactive Breathing Circle
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(180.dp)
                ) {
                    // Pulsing Aura Ring
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .graphicsLayer {
                                scaleX = animatedBreatheScale
                                scaleY = animatedBreatheScale
                                alpha = if (isBreathingActive) 0.35f else 0.15f
                            }
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    // Central Button / Pulse Circle
                    Surface(
                        shape = CircleShape,
                        color = if (isBreathingActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .graphicsLayer {
                                val s = if (isBreathingActive) 1f + (animatedBreatheScale - 1f) * 0.2f else 1f
                                scaleX = s
                                scaleY = s
                            }
                            .size(90.dp),
                        shadowElevation = 8.dp
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = if (isBreathingActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = breatheStage,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Box Breathing (4s Inhale, 4s Hold, 4s Exhale, 4s Hold) activates the parasympathetic nervous system, lowering heart rate and speeding up muscle tissue recovery.",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ExpressiveButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        isBreathingActive = !isBreathingActive
                    },
                    modifier = Modifier.width(160.dp)
                ) {
                    Text(
                        text = if (isBreathingActive) "Pause Coach" else "Start Coach",
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutItem(
    exercise: ExerciseEntry,
    completedSets: Set<Int>,
    onToggleSet: (Int) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    containerColor: Color
) {
    val haptic = LocalHapticFeedback.current
    var isExpanded by remember { mutableStateOf(false) }

    val nameLower = exercise.name.lowercase()
    val (categoryName, categoryColor, categoryEmoji) = remember(nameLower) {
        when {
            nameLower.contains("run") || nameLower.contains("jog") || nameLower.contains("cardio") || nameLower.contains("treadmill") -> {
                Triple("CARDIO", Color(0xFF00B0FF), "🏃")
            }
            nameLower.contains("squat") || nameLower.contains("leg") || nameLower.contains("lunge") -> {
                Triple("LEGS", Color(0xFFFF9100), "🦵")
            }
            nameLower.contains("pushup") || nameLower.contains("press") || nameLower.contains("chest") || nameLower.contains("bench") -> {
                Triple("CHEST", Color(0xFFFF1744), "💪")
            }
            nameLower.contains("pullup") || nameLower.contains("row") || nameLower.contains("back") || nameLower.contains("deadlift") -> {
                Triple("BACK", Color(0xFF00E676), "🏋️")
            }
            nameLower.contains("stretch") || nameLower.contains("yoga") || nameLower.contains("flex") || nameLower.contains("warm") -> {
                Triple("FLEXIBILITY", Color(0xFFD500F9), "🧘")
            }
            nameLower.contains("cycle") || nameLower.contains("bike") -> {
                Triple("CYCLING", Color(0xFFFFEA00), "🚴")
            }
            nameLower.contains("swim") -> {
                Triple("SWIMMING", Color(0xFF00E5FF), "🏊")
            }
            else -> {
                Triple("STRENGTH", Color(0xFFFF3D00), "🏋️")
            }
        }
    }

    val isAllCompleted = completedSets.size >= exercise.sets
    val itemShape = RoundedCornerShape(24.dp)
    
    // Glowing/Accent Border Color
    val itemBorder = if (isAllCompleted) {
        BorderStroke(2.dp, categoryColor.copy(alpha = 0.8f))
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
    }

    val formattedName = remember(exercise.name) {
        exercise.name.trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = itemShape,
        color = if (isAllCompleted) {
            categoryColor.copy(alpha = 0.04f)
        } else {
            containerColor
        },
        border = itemBorder,
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            isExpanded = !isExpanded
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row (Always visible)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Icon Badge (Circular / Glow styled)
                Surface(
                    shape = CircleShape,
                    color = categoryColor.copy(alpha = 0.12f),
                    modifier = Modifier.size(52.dp),
                    border = BorderStroke(1.dp, categoryColor.copy(alpha = 0.25f))
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = categoryEmoji,
                            fontSize = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Exercise Details Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    // Category Tag and Status
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = categoryColor.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = categoryName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                ),
                                color = categoryColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        if (isAllCompleted) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "DONE",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black
                                        ),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Exercise Title
                    Text(
                        text = formattedName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Beautiful tactile bento stats capsule (sets & reps)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.4f),
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(54.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        // Sets Block
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = exercise.sets.toString(),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = categoryColor
                                ),
                                lineHeight = 16.sp
                            )
                            Text(
                                text = "SETS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                ),
                                lineHeight = 10.sp
                            )
                        }

                        // Divider
                        VerticalDivider(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .height(20.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )

                        // Reps Block
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = exercise.reps.toString(),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                lineHeight = 16.sp
                            )
                            Text(
                                text = "REPS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                ),
                                lineHeight = 10.sp
                            )
                        }
                    }
                }
            }

            // Expanded content with sets bubbles check-off and instructions
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(12.dp))

                // Description/Instructions section
                if (exercise.description.isNotBlank()) {
                    Text(
                        text = "INSTRUCTIONS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = exercise.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Interactive check-off selector
                Text(
                    text = "TAP COMPLETED SETS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Set Selector Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        for (setIndex in 1..exercise.sets) {
                            val isSetDone = completedSets.contains(setIndex)
                            val bubbleScale by animateFloatAsState(
                                targetValue = if (isSetDone) 1.1f else 1.0f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
                                label = "bubble_scale_$setIndex"
                            )

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = bubbleScale
                                        scaleY = bubbleScale
                                    }
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSetDone) categoryColor else MaterialTheme.colorScheme.surfaceContainerHighest
                                    )
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onToggleSet(setIndex)
                                    }
                            ) {
                                if (isSetDone) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Completed set $setIndex",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Text(
                                        text = setIndex.toString(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    // Edit button inside the expanded content
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onEdit()
                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit workout",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseAddEditSheet(
    exercise: ExerciseEntry?,
    selectedDay: String,
    onSave: (name: String, sets: Int, reps: Int, description: String, day: String) -> Unit,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    var name by remember { mutableStateOf(exercise?.name ?: "") }
    var sets by remember { mutableStateOf(exercise?.sets ?: 3) }
    var reps by remember { mutableStateOf(exercise?.reps ?: 10) }
    var description by remember { mutableStateOf(exercise?.description ?: "") }
    var dayOfWeek by remember { mutableStateOf(exercise?.dayOfWeek ?: selectedDay) }

    // Dynamic Category Detection
    val detectedCategory = remember(name) {
        val nameLower = name.lowercase()
        when {
            nameLower.contains("run") || nameLower.contains("jog") || nameLower.contains("cardio") || nameLower.contains("treadmill") -> Pair("CARDIO", Pair(Color(0xFF00B0FF), "🏃"))
            nameLower.contains("squat") || nameLower.contains("leg") || nameLower.contains("lunge") -> Pair("LEGS", Pair(Color(0xFFFF9100), "🦵"))
            nameLower.contains("pushup") || nameLower.contains("press") || nameLower.contains("chest") || nameLower.contains("bench") -> Pair("CHEST", Pair(Color(0xFFFF1744), "💪"))
            nameLower.contains("pullup") || nameLower.contains("row") || nameLower.contains("back") || nameLower.contains("deadlift") -> Pair("BACK", Pair(Color(0xFF00E676), "🏋️"))
            nameLower.contains("stretch") || nameLower.contains("yoga") || nameLower.contains("flex") || nameLower.contains("warm") -> Pair("FLEXIBILITY", Pair(Color(0xFFD500F9), "🧘"))
            nameLower.contains("cycle") || nameLower.contains("bike") -> Pair("CYCLING", Pair(Color(0xFFFFEA00), "🚴"))
            nameLower.contains("swim") -> Pair("SWIMMING", Pair(Color(0xFF00E5FF), "🏊"))
            name.isBlank() -> null
            else -> Pair("STRENGTH", Pair(Color(0xFFFF3D00), "🏋️"))
        }
    }

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
            .navigationBarsPadding()
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header with dynamic Category badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (exercise == null) "New Workout" else "Edit Workout",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Define workout target & parameters",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            // Real-time Glowing Category Badge
            AnimatedVisibility(
                visible = detectedCategory != null,
                enter = fadeIn() + slideInHorizontally { it / 2 },
                exit = fadeOut() + slideOutHorizontally { it / 2 }
            ) {
                detectedCategory?.let { (catName, style) ->
                    val (catColor, catEmoji) = style
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = catColor.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, catColor.copy(alpha = 0.35f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = catEmoji, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = catName,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = catColor,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }

        // Exercise Name Input
        ExpressiveOutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Workout Name", fontWeight = FontWeight.Bold) },
            placeholder = { Text("e.g. Incline Bench Press", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Sets & Reps Stepper Block (Glass capsules)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sets Capsule Stepper
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SETS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                if (sets > 1) sets--
                            },
                            modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.surfaceContainerHighest, CircleShape)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease sets", modifier = Modifier.size(16.dp))
                        }
                        
                        Text(
                            text = sets.toString(),
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.width(32.dp),
                            textAlign = TextAlign.Center
                        )

                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                if (sets < 100) sets++
                            },
                            modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.surfaceContainerHighest, CircleShape)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase sets", modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            // Reps Capsule Stepper
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "REPS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                if (reps > 1) reps--
                            },
                            modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.surfaceContainerHighest, CircleShape)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease reps", modifier = Modifier.size(16.dp))
                        }
                        
                        Text(
                            text = reps.toString(),
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.width(32.dp),
                            textAlign = TextAlign.Center
                        )

                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                if (reps < 100) reps++
                            },
                            modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.surfaceContainerHighest, CircleShape)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase reps", modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Instructions/Description text field
        ExpressiveOutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Instructions / Notes", fontWeight = FontWeight.Bold) },
            placeholder = { Text("e.g. Keep chest up, squeeze shoulder blades together", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        // Day of Week selector row (Inline day switcher!)
        Column {
            Text(
                text = "SCHEDULE DAY",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(daysOfWeek) { day ->
                    val isSelected = day == dayOfWeek
                    val dayColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHighest
                    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    
                    Surface(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            dayOfWeek = day
                        },
                        shape = RoundedCornerShape(14.dp),
                        color = dayColor,
                        contentColor = textColor,
                        border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)) else null
                    ) {
                        Text(
                            text = day.substring(0, 3).uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Save & Cancel Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel", fontWeight = FontWeight.Black)
            }
            ExpressiveButton(
                onClick = {
                    if (name.isNotBlank()) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSave(name, sets, reps, description, dayOfWeek)
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = name.isNotBlank()
            ) {
                Text("Save", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
