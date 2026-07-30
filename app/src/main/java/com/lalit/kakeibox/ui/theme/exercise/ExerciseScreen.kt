package com.personal.kakeibox.ui.exercise

import com.personal.kakeibox.ui.theme.getAppStrings
import com.personal.kakeibox.ui.theme.LocalThemeSettings
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
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.personal.kakeibox.ui.components.RoundedPolygonShape
import com.personal.kakeibox.ui.components.CookieShape
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.LinearEasing
import com.personal.kakeibox.ui.theme.OutfitFontFamily
import com.personal.kakeibox.ui.theme.PlayfairFontFamily
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
    val strings = getAppStrings(themeSettings.appLanguage)

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
            TopAppBarBackground.SURFACE -> MaterialTheme.colorScheme.primaryContainer
            TopAppBarBackground.PRIMARY_CONTAINER -> MaterialTheme.colorScheme.primaryContainer
        },
        label = "top_app_bar_container_color"
    )

    val bentoIdleColor by animateColorAsState(
        targetValue = if (isPrimaryContainer) {
            androidx.compose.ui.graphics.lerp(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.primaryContainer,
                0.35f // 35% tint overlay for soft blending
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
            0.35f
        )
        val blendedSurfaceHigh = androidx.compose.ui.graphics.lerp(
            currentColorScheme.surfaceContainerHigh,
            currentColorScheme.primaryContainer,
            0.35f
        )
        val blendedSurfaceLow = androidx.compose.ui.graphics.lerp(
            currentColorScheme.surfaceContainerLow,
            currentColorScheme.primaryContainer,
            0.35f
        )
        val blendedSurfaceLowest = androidx.compose.ui.graphics.lerp(
            currentColorScheme.surfaceContainerLowest,
            currentColorScheme.primaryContainer,
            0.35f
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

    val daysOfWeek = listOf(
        strings.dayMonday,
        strings.dayTuesday,
        strings.dayWednesday,
        strings.dayThursday,
        strings.dayFriday,
        strings.daySaturday,
        strings.daySunday
    )

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
                    bottom = innerPadding.calculateBottomPadding() + 150.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(statusBarPadding + 76.dp))
                }

                // ── M3 Expressive Active Day Hero Header ───────────
                item {
                    ExpressiveExerciseHeroHeader(
                        selectedDay = uiState.selectedDay,
                        daysOfWeek = daysOfWeek,
                        restDays = uiState.restDays,
                        isRestDay = uiState.isRestDay,
                        exercises = uiState.exercises,
                        completedSetsMap = completedSetsMap,
                        containerColor = bentoIdleColor,
                        onSelectDay = { day -> viewModel.selectDay(day) },
                        onToggleRestDay = { day -> viewModel.toggleRestDay(day) },
                        onAddWorkout = { viewModel.openAddSheet() }
                    )
                }

                // ── Dashboard Content ──
                if (uiState.isRestDay) {
                    item {
                        RestDayDashboard(dayName = uiState.selectedDay, containerColor = bentoIdleColor)
                    }
                } else if (uiState.exercises.isEmpty()) {
                    item {
                        ExpressiveEmptyState(
                            message = "${strings.noWorkoutsLogged} ${uiState.selectedDay}",
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
                        WorkoutItemCard(
                            exercise = exercise,
                            completedSets = completedSetsMap[exercise.id] ?: emptySet(),
                            appLanguage = themeSettings.appLanguage,
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
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                }
            }
        }

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
                    appLanguage = themeSettings.appLanguage,
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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
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
                // Floating/Rotating 6-sided Cookie Shape in background of text
                val infiniteTransition = rememberInfiniteTransition(label = "cookie_pulse")
                val cookieRotation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(25000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "cookie_rot"
                )
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .graphicsLayer { rotationZ = cookieRotation }
                        .background(
                            color = primaryColor.copy(alpha = 0.08f),
                            shape = CookieShape(petals = 6)
                        )
                        .border(1.dp, primaryColor.copy(alpha = 0.15f), CookieShape(petals = 6))
                )

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
fun RestDayDashboard(
    dayName: String,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLow
) {
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
            colors = CardDefaults.cardColors(containerColor = containerColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
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
fun ExpressiveExerciseHeroHeader(
    selectedDay: String,
    daysOfWeek: List<String>,
    restDays: List<String>,
    isRestDay: Boolean,
    exercises: List<ExerciseEntry>,
    completedSetsMap: Map<Int, Set<Int>>,
    containerColor: Color,
    onSelectDay: (String) -> Unit,
    onToggleRestDay: (String) -> Unit,
    onAddWorkout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()

    val dayBounds = remember {
        mutableStateListOf<Pair<Float, Float>>().apply {
            repeat(daysOfWeek.size) { add(Pair(0f, 0f)) }
        }
    }
    val selectedIndex = daysOfWeek.indexOf(selectedDay).coerceAtLeast(0)
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

    val strings = getAppStrings(LocalThemeSettings.current.appLanguage)

    // Detect predominant active focus for selected day
    val activeFocusBadge = remember(isRestDay, exercises, strings) {
        if (isRestDay) {
            "😴 REST & RECOVERY DAY"
        } else if (exercises.isEmpty()) {
            "🏋️ " + strings.scheduleWorkouts
        } else {
            val categories = exercises.map { ex ->
                val nameLower = ex.name.lowercase()
                when {
                    nameLower.contains("run") || nameLower.contains("cardio") -> "CARDIO"
                    nameLower.contains("squat") || nameLower.contains("leg") -> "LEGS"
                    nameLower.contains("pushup") || nameLower.contains("press") || nameLower.contains("chest") -> "CHEST"
                    nameLower.contains("pullup") || nameLower.contains("row") || nameLower.contains("back") -> "BACK"
                    else -> "STRENGTH"
                }
            }.distinct()
            "🔥 " + categories.joinToString(" & ") + " DAY"
        }
    }

    val totalWorkouts = exercises.size
    val totalCompletedSets = exercises.sumOf { (completedSetsMap[it.id] ?: emptySet()).size }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Top Header Row: Focus Badge + Quick Add Button ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Focus Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isRestDay) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = activeFocusBadge,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        ),
                        color = if (isRestDay) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                // Add Workout Quick Button
                Surface(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onAddWorkout()
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add workout",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = strings.save,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // ── Glanceable Day Summary Stats ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Workouts Count Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FitnessCenter,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "$totalWorkouts ${strings.workouts}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isRestDay) "Rest Day" else "$totalCompletedSets ${strings.setsDone}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                // Rest Mode Quick Switch Pill
                Surface(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onToggleRestDay(selectedDay)
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isRestDay) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isRestDay) Icons.Filled.Coffee else Icons.Outlined.Coffee,
                                contentDescription = null,
                                tint = if (isRestDay) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isRestDay) "Rest Mode" else strings.active,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Switch(
                            checked = isRestDay,
                            onCheckedChange = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onToggleRestDay(selectedDay)
                            },
                            modifier = Modifier.graphicsLayer { scaleX = 0.8f; scaleY = 0.8f }
                        )
                    }
                }
            }

            // ── Shape-Morphing Day Pill Selector ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    // Morphing Sliding Background
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
                                .height(72.dp)
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
                            val isSelected = day == selectedDay
                            val isRest = restDays.contains(day)
                            val scale by animateFloatAsState(
                                targetValue = if (isSelected) 1.05f else 1.0f,
                                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                                label = "day_scale"
                            )

                            val pillContainerColor = when {
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
                                    onSelectDay(day)
                                },
                                shape = RoundedCornerShape(20.dp),
                                color = pillContainerColor,
                                contentColor = contentColor,
                                modifier = Modifier
                                    .graphicsLayer(scaleX = scale, scaleY = scale)
                                    .width(72.dp)
                                    .height(72.dp)
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
}

@Composable
fun WorkoutItemCard(
    exercise: ExerciseEntry,
    completedSets: Set<Int>,
    appLanguage: com.personal.kakeibox.data.preferences.AppLanguage,
    onToggleSet: (Int) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val strings = getAppStrings(appLanguage)
    var isExpanded by remember { mutableStateOf(false) }

    val nameLower = exercise.name.lowercase()
    val (categoryName, categoryColor, categoryEmoji) = remember(nameLower) {
        when {
            nameLower.contains("run") || nameLower.contains("jog") || nameLower.contains("cardio") || nameLower.contains("treadmill") -> Triple("CARDIO", Color(0xFF00B0FF), "🏃")
            nameLower.contains("squat") || nameLower.contains("leg") || nameLower.contains("lunge") -> Triple("LEGS", Color(0xFFFF9100), "🦵")
            nameLower.contains("pushup") || nameLower.contains("press") || nameLower.contains("chest") || nameLower.contains("bench") -> Triple("CHEST", Color(0xFFFF1744), "💪")
            nameLower.contains("pullup") || nameLower.contains("row") || nameLower.contains("back") || nameLower.contains("deadlift") -> Triple("BACK", Color(0xFF00E676), "🏋️")
            nameLower.contains("stretch") || nameLower.contains("yoga") || nameLower.contains("flex") || nameLower.contains("warm") -> Triple("FLEXIBILITY", Color(0xFFD500F9), "🧘")
            nameLower.contains("cycle") || nameLower.contains("bike") -> Triple("CYCLING", Color(0xFFFFEA00), "🚴")
            nameLower.contains("swim") -> Triple("SWIMMING", Color(0xFF00E5FF), "🏊")
            else -> Triple("STRENGTH", Color(0xFFFF3D00), "🏋️")
        }
    }

    val isAllCompleted = completedSets.size >= exercise.sets
    val itemShape = RoundedCornerShape(26.dp)

    val itemBorder = if (isAllCompleted) {
        BorderStroke(2.dp, categoryColor.copy(alpha = 0.9f))
    } else {
        BorderStroke(1.5.dp, categoryColor.copy(alpha = 0.4f))
    }

    val formattedName = remember(exercise.name) {
        exercise.name.trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = itemShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        ),
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
                // Category Icon Badge
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

                // Bento stats capsule (sets & reps)
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
                                text = strings.sets,
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
                                text = strings.reps,
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

            // Expanded section
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(12.dp))

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
                                        contentDescription = "Set $setIndex done",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Text(
                                        text = setIndex.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    // Action buttons (Edit & Delete)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onEdit()
                            },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Edit workout",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onDelete()
                            },
                            modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete workout",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
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
    appLanguage: com.personal.kakeibox.data.preferences.AppLanguage,
    onSave: (name: String, sets: Int, reps: Int, description: String, day: String) -> Unit,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val strings = getAppStrings(appLanguage)

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

    val daysOfWeek = listOf(
        strings.dayMonday,
        strings.dayTuesday,
        strings.dayWednesday,
        strings.dayThursday,
        strings.dayFriday,
        strings.daySaturday,
        strings.daySunday
    )

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
                    text = if (exercise == null) strings.newWorkout else strings.editWorkout,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = strings.defineWorkoutDesc,
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
            label = { Text(strings.workoutName, fontWeight = FontWeight.Bold) },
            placeholder = { Text("e.g. Incline Bench Press", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Sets & Reps Stepper Block (High-Contrast M3 Cards)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sets Capsule Stepper
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
                shadowElevation = 0.dp,
                tonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SETS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                if (sets > 1) sets--
                            },
                            modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease sets", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(18.dp))
                        }
                        
                        Text(
                            text = sets.toString(),
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 26.sp),
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.width(36.dp),
                            textAlign = TextAlign.Center
                        )

                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                if (sets < 100) sets++
                            },
                            modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase sets", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            // Reps Capsule Stepper
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
                shadowElevation = 0.dp,
                tonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "REPS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                if (reps > 1) reps--
                            },
                            modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease reps", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(18.dp))
                        }
                        
                        Text(
                            text = reps.toString(),
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 26.sp),
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.width(36.dp),
                            textAlign = TextAlign.Center
                        )

                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                if (reps < 100) reps++
                            },
                            modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase reps", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // Instructions/Description text field
        ExpressiveOutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(strings.instructions, fontWeight = FontWeight.Bold) },
            placeholder = { Text("e.g. Keep chest up, squeeze shoulder blades together", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        // Day of Week selector row (Inline day switcher!)
        Column {
            Text(
                text = strings.scheduleDay,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(daysOfWeek) { day ->
                    val isSelected = day == dayOfWeek
                    val dayBg = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh
                    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    
                    Surface(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            dayOfWeek = day
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = dayBg,
                        contentColor = textColor,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)
                        )
                    ) {
                        Text(
                            text = day.substring(0, 3).uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
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
                Text(strings.cancel, fontWeight = FontWeight.Black)
            }
            ExpressiveButton(
                onClick = {
                    if (name.isNotBlank()) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSave(name.trim(), sets, reps, description.trim(), dayOfWeek)
                    }
                },
                enabled = name.isNotBlank(),
                modifier = Modifier.weight(1f)
            ) {
                Text(strings.save, fontWeight = FontWeight.Black)
            }
        }
    }
}
