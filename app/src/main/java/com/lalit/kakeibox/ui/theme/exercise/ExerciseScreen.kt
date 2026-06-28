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

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = topAppBarContainerColor,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {},
            floatingActionButton = {
                if (!uiState.isRestDay) {
                    Surface(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.openAddSheet()
                        },
                        modifier = Modifier
                            .padding(bottom = fabPadding)
                            .size(width = fabWidth, height = 64.dp),
                        shape = RoundedCornerShape(32.dp),
                        color = if (isPrimaryContainer) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primaryContainer,
                        contentColor = if (isPrimaryContainer) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer,
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
                                    contentDescription = "Add Exercise",
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
                                        text = "Add Workout",
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
                }
            }
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
                            containerColor = if (isRest) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceContainer
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
            containerColor = bentoIdleColor,
            dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant) },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
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

@Composable
fun RestDayDashboard(dayName: String) {
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

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    BentoCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        title = "RECOVERY DAY",
        icon = Icons.Outlined.Coffee,
        isActive = true,
        activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
        activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                // Pulsing Aura Ring
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .graphicsLayer {
                            scaleX = pulseScale
                            scaleY = pulseScale
                            alpha = pulseAlpha
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

                // Central Coffee/Rest Icon
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(80.dp),
                    shadowElevation = 6.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Coffee,
                            contentDescription = null,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = quote,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Recovery is where the magic happens. Hydrate, eat well, sleep, and let your body adapt to your hard work.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@Composable
fun WorkoutItem(
    exercise: ExerciseEntry,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    containerColor: Color
) {
    val haptic = LocalHapticFeedback.current
    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.M3_EXPRESSIVE && false

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

    val itemShape = if (isSpaceTerminal) RoundedCornerShape(12.dp) else RoundedCornerShape(24.dp)
    val itemBorder = if (isSpaceTerminal) {
        BorderStroke(1.5.dp, categoryColor)
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
    }

    val formattedName = remember(exercise.name) {
        exercise.name.trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = itemShape,
        color = containerColor,
        border = itemBorder,
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onEdit()
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
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
                // Category Tag
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = categoryColor.copy(alpha = 0.1f),
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Text(
                        text = categoryName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            fontFamily = if (isSpaceTerminal) FontFamily.Monospace else FontFamily.Default,
                            letterSpacing = 1.sp
                        ),
                        color = categoryColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Exercise Title
                Text(
                    text = formattedName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontFamily = if (isSpaceTerminal) FontFamily.Monospace else FontFamily.Default
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (exercise.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Vertical accent indicator bar
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(16.dp)
                                .background(categoryColor, RoundedCornerShape(1.dp))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = exercise.description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                fontFamily = if (isSpaceTerminal) FontFamily.Monospace else FontFamily.Default
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
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
                                color = categoryColor,
                                fontFamily = if (isSpaceTerminal) FontFamily.Monospace else FontFamily.Default
                            ),
                            lineHeight = 16.sp
                        )
                        Text(
                            text = "SETS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                fontFamily = if (isSpaceTerminal) FontFamily.Monospace else FontFamily.Default
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
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = if (isSpaceTerminal) FontFamily.Monospace else FontFamily.Default
                            ),
                            lineHeight = 16.sp
                        )
                        Text(
                            text = "REPS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                fontFamily = if (isSpaceTerminal) FontFamily.Monospace else FontFamily.Default
                            ),
                            lineHeight = 10.sp
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
            .navigationBarsPadding()
            .imePadding()
    ) {
        Text(
            text = if (exercise == null) "New Exercise" else "Edit Exercise",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        ExpressiveOutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Exercise Name", fontWeight = FontWeight.Bold) },
            placeholder = { Text("e.g. Bench Press", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sets picker
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Sets", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Total number of sets", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        if (sets > 1) sets--
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHighest, CircleShape)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease sets")
                }
                Text(
                    text = sets.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.width(36.dp),
                    textAlign = TextAlign.Center
                )
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        if (sets < 100) sets++
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHighest, CircleShape)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase sets")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reps picker
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Reps", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Repetitions per set", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        if (reps > 1) reps--
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHighest, CircleShape)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease reps")
                }
                Text(
                    text = reps.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.width(36.dp),
                    textAlign = TextAlign.Center
                )
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        if (reps < 100) reps++
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHighest, CircleShape)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase reps")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExpressiveOutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Instructions", fontWeight = FontWeight.Bold) },
            placeholder = { Text("Describe how to perform this exercise...", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(32.dp))

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
