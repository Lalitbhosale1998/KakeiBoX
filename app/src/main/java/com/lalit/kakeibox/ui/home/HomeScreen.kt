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

    val paydayInfo = remember { com.personal.kakeibox.util.DateUtils.calculatePaydayProgress() }

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

    val currentColorScheme = MaterialTheme.colorScheme
    val sheetColorScheme = if (!isDark) {
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

    MaterialTheme(colorScheme = sheetColorScheme) {
        val chalkBg = MaterialTheme.colorScheme.surfaceContainerLow
        val neonMint = MaterialTheme.colorScheme.primary
        val mintText = MaterialTheme.colorScheme.onSurface
        val textSecondary = MaterialTheme.colorScheme.onSurfaceVariant
        val chalkBorder = Color.Transparent
        val cardGradientColors = listOf(chalkBg, chalkBg)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
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
            val isSthapatyaTheme = themeSettings.themeFlavor == com.personal.kakeibox.data.preferences.ThemeFlavor.STHAPATYA
            val heroCardShape = if (isSthapatyaTheme) com.personal.kakeibox.ui.theme.SthapatyaShapes.ToranaArchShape else RoundedCornerShape(28.dp)

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = heroCardShape,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shadowElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNavigateTab(1)
                        }
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        // ── Editorial Top Header Row ──
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
                                    imageVector = Icons.Outlined.Shield,
                                    contentDescription = "Shield",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = if (isSthapatyaTheme) "🛕 VITTA COMMAND LAB" else "VITTA COMMAND LAB",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 2.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Surface(
                                shape = if (isSthapatyaTheme) com.personal.kakeibox.ui.theme.SthapatyaShapes.PadmaChipShape else CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "⚡ LIVE GAUGE",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        // ── Editorial Zine Hero Content (Giant Numeral + Stacked Text) ──
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            // Left: Massive Hero Numeral 14 with Flame Accent
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                                modifier = Modifier.padding(2.dp)
                            ) {
                                Box(
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Outlined.LocalFireDepartment,
                                            contentDescription = "Flame",
                                            tint = MaterialTheme.colorScheme.tertiary,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .graphicsLayer {
                                                    scaleX = flameScale
                                                    scaleY = flameScale
                                                }
                                        )
                                        Text(
                                            text = "${paydayInfo.daysRemaining}",
                                            fontSize = 64.sp,
                                            fontWeight = FontWeight.Black,
                                            lineHeight = 64.sp,
                                            letterSpacing = (-2).sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }

                            // Right: Asymmetric Editorial Typography Stack
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "DAYS TILL PAYDAY",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.5.sp,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                                Text(
                                    text = formattedTotalEarnings,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = (-1).sp,
                                    lineHeight = 32.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "REMAINING COMMAND BALANCE",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // ── Bottom: Full-Width Wavy Payday Timeline Track ──
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "⚡ PAYDAY COUNTDOWN TIMELINE",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "${(paydayInfo.progressRatio * 100).toInt()}% ELAPSED",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            com.personal.kakeibox.ui.components.ExpressiveWavyProgressIndicator(
                                progress = paydayInfo.progressRatio,
                                strokeWidth = 8.dp,
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                modifier = Modifier.fillMaxWidth()
                            )
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
                    shape = if (isSthapatyaTheme) com.personal.kakeibox.ui.theme.SthapatyaShapes.PancharathaCardShape else RoundedCornerShape(32.dp),
                    color = chalkBg,
                    border = BorderStroke(1.5.dp, if (isSthapatyaTheme) Color(0xFFD4AF37) else chalkBorder),
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
                                        tint = if (isSthapatyaTheme) Color(0xFFD4AF37) else MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Surface(
                                        shape = if (isSthapatyaTheme) com.personal.kakeibox.ui.theme.SthapatyaShapes.PadmaChipShape else RoundedCornerShape(10.dp),
                                        color = if (isSthapatyaTheme) Color(0xFFD4AF37).copy(alpha = 0.15f) else MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, if (isSthapatyaTheme) Color(0xFFD4AF37).copy(alpha = 0.6f) else MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
                                    ) {
                                        Text(
                                            text = featuredWord.studyTag.ifEmpty { "JLPT N1" },
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (isSthapatyaTheme) Color(0xFFD4AF37) else MaterialTheme.colorScheme.secondary
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
                        shape = if (isSthapatyaTheme) com.personal.kakeibox.ui.theme.SthapatyaShapes.PancharathaCardShape else RoundedCornerShape(28.dp),
                        color = chalkBg,
                        border = BorderStroke(1.5.dp, if (isSthapatyaTheme) Color(0xFFD4AF37) else chalkBorder),
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
                                        tint = if (isSthapatyaTheme) Color(0xFFD4AF37) else MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Surface(
                                        shape = if (isSthapatyaTheme) com.personal.kakeibox.ui.theme.SthapatyaShapes.PadmaChipShape else RoundedCornerShape(10.dp),
                                        color = if (isWorkoutLoggedToday) Color(0xFF00E676) else if (isSthapatyaTheme) Color(0xFFD4AF37).copy(alpha = 0.15f) else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, if (isSthapatyaTheme) Color(0xFFD4AF37).copy(alpha = 0.6f) else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f))
                                    ) {
                                        Text(
                                            text = if (isWorkoutLoggedToday) "LOGGED! 🔥" else "LOG ➔",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (isWorkoutLoggedToday) MaterialTheme.colorScheme.onPrimary else if (isSthapatyaTheme) Color(0xFFD4AF37) else MaterialTheme.colorScheme.tertiary
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
                                        color = if (isSthapatyaTheme) Color(0xFFD4AF37) else MaterialTheme.colorScheme.tertiary
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
                        shape = if (isSthapatyaTheme) com.personal.kakeibox.ui.theme.SthapatyaShapes.PancharathaCardShape else RoundedCornerShape(28.dp),
                        color = chalkBg,
                        border = BorderStroke(1.5.dp, if (isSthapatyaTheme) Color(0xFFD4AF37) else chalkBorder),
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
                                        tint = if (isSthapatyaTheme) Color(0xFFD4AF37) else neonMint,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "Jump",
                                        tint = if (isSthapatyaTheme) Color(0xFFD4AF37) else neonMint,
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
                                        color = if (isSthapatyaTheme) Color(0xFFD4AF37) else neonMint
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
                    color = if (isSthapatyaTheme) Color(0xFFD4AF37) else MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val pillShape = if (isSthapatyaTheme) com.personal.kakeibox.ui.theme.SthapatyaShapes.PadmaChipShape else RoundedCornerShape(20.dp)
                    val pillBorder = if (isSthapatyaTheme) BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.8f)) else null

                    // Salary Pill Button
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(pillShape)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onNavigateTab(1)
                            },
                        shape = pillShape,
                        color = if (isSthapatyaTheme) Color(0xFF5E2B13) else MaterialTheme.colorScheme.secondaryContainer,
                        border = pillBorder,
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
                                tint = if (isSthapatyaTheme) Color(0xFFFFDBCB) else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SALARY",
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp,
                                color = if (isSthapatyaTheme) Color(0xFFFFDBCB) else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    // Kotoba Pill Button
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(pillShape)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onNavigateTab(3)
                            },
                        shape = pillShape,
                        color = if (isSthapatyaTheme) Color(0xFF5E2B13) else MaterialTheme.colorScheme.secondaryContainer,
                        border = pillBorder,
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
                                tint = if (isSthapatyaTheme) Color(0xFFFFDBCB) else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "KOTOBA",
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp,
                                color = if (isSthapatyaTheme) Color(0xFFFFDBCB) else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    // Settings Pill Button
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(pillShape)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onNavigateTab(4)
                            },
                        shape = pillShape,
                        color = if (isSthapatyaTheme) Color(0xFFD4AF37) else neonMint,
                        border = pillBorder,
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
                                tint = if (isSthapatyaTheme) Color(0xFF3B2E00) else MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SETTINGS",
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp,
                                color = if (isSthapatyaTheme) Color(0xFF3B2E00) else MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }

            // ── 4. DAILY SNAPSHOT & QUICK ACTIVITY BENTO ROW ──
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "DAILY SNAPSHOT & ACTIVITY",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp,
                    color = if (isSthapatyaTheme) Color(0xFFD4AF37) else MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Card 1: Today's Budget Status
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onNavigateTab(1)
                            },
                        shape = RoundedCornerShape(24.dp),
                        color = chalkBg,
                        border = BorderStroke(1.5.dp, if (isSthapatyaTheme) Color(0xFFD4AF37) else chalkBorder),
                        shadowElevation = 6.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.verticalGradient(colors = cardGradientColors))
                                .padding(14.dp)
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
                                        contentDescription = "Budget",
                                        tint = if (isSthapatyaTheme) Color(0xFFD4AF37) else neonMint,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "ON TRACK",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isSthapatyaTheme) Color(0xFFD4AF37) else neonMint
                                    )
                                }
                                Column {
                                    Text(
                                        text = "MONTHLY BUDGET",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textSecondary
                                    )
                                    Text(
                                        text = "78% SAVED",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        color = mintText
                                    )
                                }
                            }
                        }
                    }

                    // Card 2: Kotoba Mastery Queue
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onNavigateTab(3)
                            },
                        shape = RoundedCornerShape(24.dp),
                        color = chalkBg,
                        border = BorderStroke(1.5.dp, if (isSthapatyaTheme) Color(0xFFD4AF37) else chalkBorder),
                        shadowElevation = 6.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.verticalGradient(colors = cardGradientColors))
                                .padding(14.dp)
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
                                        imageVector = Icons.Outlined.Translate,
                                        contentDescription = "Review",
                                        tint = if (isSthapatyaTheme) Color(0xFFD4AF37) else MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "DUE NOW",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isSthapatyaTheme) Color(0xFFD4AF37) else MaterialTheme.colorScheme.secondary
                                    )
                                }
                                Column {
                                    Text(
                                        text = "VOCAB REVIEW",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textSecondary
                                    )
                                    Text(
                                        text = "12 WORDS ➔",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        color = mintText
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
}
