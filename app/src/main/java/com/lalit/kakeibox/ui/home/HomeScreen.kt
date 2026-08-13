package com.personal.kakeibox.ui.home

import androidx.activity.ComponentActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.data.entity.VocabEntry
import com.personal.kakeibox.data.preferences.ThemeStyle
import com.personal.kakeibox.data.preferences.TopAppBarBackground
import com.personal.kakeibox.ui.components.RoundedPolygonShape
import com.personal.kakeibox.ui.salary.SalaryViewModel
import com.personal.kakeibox.ui.settings.ThemeViewModel
import com.personal.kakeibox.ui.vocab.VocabViewModel
import com.personal.kakeibox.ui.theme.LocalThemeSettings
import com.personal.kakeibox.ui.theme.expressiveBackground
import com.personal.kakeibox.util.CurrencyUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    salaryViewModel: SalaryViewModel = hiltViewModel(),
    vocabViewModel: VocabViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    onNavigateTab: (Int) -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current
    val themeSettings by themeViewModel.themeSettings.collectAsStateWithLifecycle()
    val totalSalary by salaryViewModel.totalSalary.collectAsStateWithLifecycle()
    val allVocab by vocabViewModel.allEntries.collectAsStateWithLifecycle()
    val isPrivacyMode = themeSettings.privacyModeEnabled
    val isDark = isSystemInDarkTheme()

    val density = androidx.compose.ui.platform.LocalDensity.current
    val statusBarsPadding = with(density) { WindowInsets.statusBars.getTop(this).toDp() }

    val formattedTotalEarnings = remember(totalSalary, isPrivacyMode) {
        CurrencyUtils.formatAmount(totalSalary ?: 5806060L, themeSettings.currencySymbol, isPrivacyMode, compact = false)
    }

    val featuredWord = remember(allVocab) {
        allVocab.firstOrNull() ?: VocabEntry(
            id = 1,
            kanjiWord = "改善",
            furiganaReading = "かいぜん",
            meaning = "Kaizen (Improvement)",
            category = "Vocabulary",
            studyTag = "JLPT N1",
            exampleSentence = "毎日少しずつ業務を改善していく。"
        )
    }

    // Interactive States
    var isKanjiCardFlipped by remember { mutableStateOf(false) }
    var isWorkoutLoggedToday by remember { mutableStateOf(false) }
    var workoutStreakCount by remember { mutableIntStateOf(14) }

    // Pulsing animations
    val infiniteTransition = rememberInfiniteTransition(label = "hero_pulse")
    val flameScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(tween(800, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "flame_scale"
    )

    // Continuous M3 Expressive Wavy Arc Phase Animation
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing)),
        label = "m3_wavy_phase"
    )

    // Top App Bar & Background Setup
    val isPrimaryContainer = themeSettings.topAppBarBackground == TopAppBarBackground.PRIMARY_CONTAINER
    val topAppBarContainerColor by animateColorAsState(
        targetValue = when (themeSettings.topAppBarBackground) {
            TopAppBarBackground.SURFACE -> MaterialTheme.colorScheme.primaryContainer
            TopAppBarBackground.PRIMARY_CONTAINER -> MaterialTheme.colorScheme.primaryContainer
        },
        label = "home_top_app_bar_color"
    )

    val chalkBg = if (isDark) MaterialTheme.colorScheme.surfaceContainerLow else MaterialTheme.colorScheme.surface
    val neonMint = MaterialTheme.colorScheme.primary
    val mintText = MaterialTheme.colorScheme.onSurface
    val textSecondary = MaterialTheme.colorScheme.onSurfaceVariant
    val chalkBorder = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)

    val cardGradientColors = if (isDark) {
        listOf(Color(0xFF19201C), chalkBg)
    } else {
        listOf(MaterialTheme.colorScheme.surfaceContainerLowest, MaterialTheme.colorScheme.surfaceContainerLow)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .expressiveBackground(
                isDark = isDark,
                isPrimaryContainer = isPrimaryContainer,
                primaryColor = MaterialTheme.colorScheme.primary,
                containerColor = topAppBarContainerColor,
                pattern = themeSettings.backdropPattern
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = statusBarsPadding + 64.dp)
                .padding(bottom = 60.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── 1. MONUMENTAL HERO GAUGE CARD (Payday + Financial Pulse Wheel) ──
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                color = chalkBg,
                border = BorderStroke(1.5.dp, chalkBorder),
                shadowElevation = 12.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(colors = cardGradientColors))
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNavigateTab(1)
                        }
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Header Badge
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Shield,
                                    contentDescription = "Shield",
                                    tint = neonMint,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "VITTA COMMAND LAB",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 2.sp,
                                    color = mintText
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = neonMint.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, neonMint.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "⚡ LIVE GAUGE",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    color = neonMint
                                )
                            }
                        }

                        // Central Circular Progress Gauge Wheel
                        Box(
                            modifier = Modifier.size(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val primaryColor = MaterialTheme.colorScheme.primary
                            val secondaryColor = MaterialTheme.colorScheme.secondary
                            val tertiaryColor = MaterialTheme.colorScheme.tertiary
                            val outlineColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                // 1. Smooth Flat Background Track Arc (Official M3 Spec)
                                val trackStrokeWidth = 14.dp.toPx()
                                val arcSize = size.width - trackStrokeWidth
                                val topLeft = Offset(trackStrokeWidth / 2f, trackStrokeWidth / 2f)

                                drawArc(
                                    color = outlineColor,
                                    startAngle = 135f,
                                    sweepAngle = 270f,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = Size(arcSize, arcSize),
                                    style = Stroke(width = trackStrokeWidth, cap = StrokeCap.Round)
                                )

                                // 2. Active Thick Bubbly Wavy Progress Arc Riding On Top (Official M3 Spec)
                                val center = Offset(size.width / 2f, size.height / 2f)
                                val baseRadius = arcSize / 2f
                                val activeProgress = 0.53f
                                val waveAmplitude = 4.dp.toPx()
                                val numWaves = 4.5
                                val startAngleRad = Math.toRadians(135.0)
                                val activeSweepAngleRad = Math.toRadians(270.0 * activeProgress)
                                val steps = 60

                                val points = ArrayList<Offset>()
                                for (i in 0..steps) {
                                    val t = i.toFloat() / steps
                                    val angle = startAngleRad + t * activeSweepAngleRad
                                    val waveOffset = (kotlin.math.sin(t * numWaves * 2 * Math.PI + wavePhase)).toFloat() * waveAmplitude
                                    val r = baseRadius + waveOffset
                                    val x = center.x + r * kotlin.math.cos(angle).toFloat()
                                    val y = center.y + r * kotlin.math.sin(angle).toFloat()
                                    points.add(Offset(x, y))
                                }

                                val activeWavyPath = androidx.compose.ui.graphics.Path()
                                if (points.isNotEmpty()) {
                                    activeWavyPath.moveTo(points[0].x, points[0].y)
                                    for (i in 1 until points.size - 1) {
                                        val p0 = points[i]
                                        val p1 = points[i + 1]
                                        val midX = (p0.x + p1.x) / 2f
                                        val midY = (p0.y + p1.y) / 2f
                                        activeWavyPath.quadraticTo(p0.x, p0.y, midX, midY)
                                    }
                                    activeWavyPath.lineTo(points.last().x, points.last().y)
                                }

                                drawPath(
                                    path = activeWavyPath,
                                    brush = Brush.sweepGradient(
                                        colors = listOf(primaryColor, secondaryColor, tertiaryColor, primaryColor)
                                    ),
                                    style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }

                            // Inner Gauge Text
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "🔥 14 DAYS",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    color = neonMint,
                                    modifier = Modifier.graphicsLayer {
                                        scaleX = flameScale
                                        scaleY = flameScale
                                    }
                                )
                                Text(
                                    text = formattedTotalEarnings,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = (-1).sp,
                                    color = mintText
                                )
                                Text(
                                    text = "TILL NEXT PAYDAY",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = textSecondary
                                )
                            }
                        }
                    }
                }
            }

            // ── 2. ASYMMETRIC 2-COLUMN BENTO GRID ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── LEFT COLUMN: 3D Flip Kanji Art Poster (Razor-Sharp Borders & Zine Gradient Depth) ──
                val cardRotationY by animateFloatAsState(
                    targetValue = if (isKanjiCardFlipped) 180f else 0f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                    label = "kanji_flip_rotation"
                )

                Surface(
                    modifier = Modifier
                        .weight(1.1f)
                        .height(280.dp)
                        .graphicsLayer {
                            rotationY = cardRotationY
                            cameraDistance = 12 * density.density
                        },
                    shape = RoundedCornerShape(32.dp),
                    color = chalkBg,
                    border = BorderStroke(1.5.dp, chalkBorder),
                    shadowElevation = 10.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.verticalGradient(colors = cardGradientColors))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isKanjiCardFlipped = !isKanjiCardFlipped
                            }
                            .padding(18.dp)
                    ) {
                        if (cardRotationY <= 90f) {
                            // FRONT: Vertical Japanese Art Poster
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Translate,
                                        contentDescription = "Kotoba",
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
                                    ) {
                                        Text(
                                            text = featuredWord.studyTag.ifEmpty { "JLPT N1" },
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.Start) {
                                    Text(
                                        text = featuredWord.furiganaReading,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textSecondary
                                    )
                                    Text(
                                        text = featuredWord.kanjiWord,
                                        fontSize = 44.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 2.sp,
                                        lineHeight = 48.sp,
                                        color = mintText
                                    )
                                }

                                Column {
                                    Text(
                                        text = featuredWord.meaning.uppercase(),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = mintText,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "TAP TO FLIP 🔄",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        } else {
                            // BACK: Example Sentence & Navigation
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer { rotationY = 180f },
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "例文 EXAMPLE",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = featuredWord.exampleSentence.ifEmpty { "毎日少しずつ業務を改善していく。" },
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 18.sp,
                                        color = mintText
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onNavigateTab(3) }
                                ) {
                                    Text(
                                        text = "OPEN DECK ➔",
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                // ── RIGHT COLUMN: 2 Stacked Cards (Workout & Quick Actions with Razor-Sharp Borders) ──
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(280.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Top Right Cell: Gym Streak Card (Razor-Sharp Borders)
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        color = chalkBg,
                        border = BorderStroke(1.5.dp, chalkBorder),
                        shadowElevation = 8.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.verticalGradient(colors = cardGradientColors))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isWorkoutLoggedToday = !isWorkoutLoggedToday
                                    if (isWorkoutLoggedToday) workoutStreakCount += 1 else workoutStreakCount = maxOf(14, workoutStreakCount - 1)
                                }
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.FitnessCenter,
                                        contentDescription = "Workout",
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isWorkoutLoggedToday) Color(0xFF00E676) else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f))
                                    ) {
                                        Text(
                                            text = if (isWorkoutLoggedToday) "LOGGED! 🔥" else "LOG ➔",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (isWorkoutLoggedToday) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.tertiary
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = "WORKOUT",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                    Text(
                                        text = "🔥 $workoutStreakCount DAYS",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        color = mintText
                                    )
                                }
                            }
                        }
                    }

                    // Bottom Right Cell: Quick Salary Jump Card (Razor-Sharp Borders)
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        color = chalkBg,
                        border = BorderStroke(1.5.dp, chalkBorder),
                        shadowElevation = 8.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.verticalGradient(colors = cardGradientColors))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onNavigateTab(1)
                                }
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Wallet,
                                        contentDescription = "Salary",
                                        tint = neonMint,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "Jump",
                                        tint = neonMint,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "SALARY LAB",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp,
                                        color = neonMint
                                    )
                                    Text(
                                        text = "VIEW LEDGER",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Black,
                                        color = mintText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ── 3. HIGH-CHROMA DOCK ACTION PILLS ──
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "COMMAND LAUNCHPAD",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp,
                    color = neonMint
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Salary Pill Button
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onNavigateTab(1)
                            },
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 14.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Wallet,
                                contentDescription = "Salary",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SALARY",
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    // Kotoba Pill Button
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onNavigateTab(3)
                            },
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 14.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Translate,
                                contentDescription = "Kotoba",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "KOTOBA",
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    // Settings Pill Button
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onNavigateTab(4)
                            },
                        shape = RoundedCornerShape(20.dp),
                        color = neonMint,
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 14.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Settings",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SETTINGS",
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
