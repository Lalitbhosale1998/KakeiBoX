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
import androidx.compose.foundation.shape.GenericShape
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
import androidx.compose.ui.zIndex
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
import androidx.compose.foundation.gestures.detectTapGestures
import com.personal.kakeibox.ui.components.elasticClick
import androidx.compose.ui.input.pointer.pointerInput
import com.personal.kakeibox.data.entity.VocabEntry
import com.personal.kakeibox.data.preferences.ThemeStyle
import com.personal.kakeibox.data.preferences.TopAppBarBackground
import com.personal.kakeibox.ui.components.ExpressiveElasticToggle
import com.personal.kakeibox.ui.components.RoundedPolygonShape
import com.personal.kakeibox.ui.components.rememberExpressiveCardShape
import com.personal.kakeibox.ui.components.AGSLFluidMeshBackground
import com.personal.kakeibox.ui.theme.rememberPixelHaptics
import com.personal.kakeibox.ui.exercise.ExerciseViewModel
import com.personal.kakeibox.ui.salary.SalaryViewModel
import com.personal.kakeibox.ui.settings.ThemeViewModel
import com.personal.kakeibox.ui.vocab.VocabViewModel
import com.personal.kakeibox.ui.theme.ExpressivePhysics
import com.personal.kakeibox.ui.theme.LocalThemeSettings
import com.personal.kakeibox.ui.theme.expressiveBackground
import com.personal.kakeibox.ui.theme.getAppStrings
import com.personal.kakeibox.ui.components.EditorialQuoteFooter
import com.personal.kakeibox.ui.components.EditorialSegment
import com.personal.kakeibox.ui.components.InlineEditorialSentence
import com.personal.kakeibox.ui.components.StackedCondensedHeader
import com.personal.kakeibox.util.CurrencyUtils

@Composable
fun JapaneseRedHankoStamp(
    text: String,
    modifier: Modifier = Modifier,
    rotation: Float = -10f
) {
    val stampRed = Color(0xFFD32F2F)
    Surface(
        modifier = modifier.graphicsLayer { rotationZ = rotation },
        shape = RoundedCornerShape(8.dp),
        color = stampRed.copy(alpha = 0.12f),
        border = BorderStroke(1.8.dp, stampRed.copy(alpha = 0.85f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = stampRed,
            letterSpacing = 1.sp
        )
    }
}

val TicketNotchShape = GenericShape { size, _ ->
    val corner = 24f
    val notchR = 12f
    val notchY = size.height / 2f

    moveTo(corner, 0f)
    lineTo(size.width - corner, 0f)
    quadraticTo(size.width, 0f, size.width, corner)

    lineTo(size.width, notchY - notchR)
    arcTo(
        rect = androidx.compose.ui.geometry.Rect(
            size.width - notchR,
            notchY - notchR,
            size.width + notchR,
            notchY + notchR
        ),
        startAngleDegrees = -90f,
        sweepAngleDegrees = -180f,
        forceMoveTo = false
    )
    lineTo(size.width, size.height - corner)
    quadraticTo(size.width, size.height, size.width - corner, size.height)

    lineTo(corner, size.height)
    quadraticTo(0f, size.height, 0f, size.height - corner)

    lineTo(0f, notchY + notchR)
    arcTo(
        rect = androidx.compose.ui.geometry.Rect(
            -notchR,
            notchY - notchR,
            notchR,
            notchY + notchR
        ),
        startAngleDegrees = 90f,
        sweepAngleDegrees = -180f,
        forceMoveTo = false
    )
    lineTo(0f, corner)
    quadraticTo(0f, 0f, corner, 0f)
    close()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    salaryViewModel: SalaryViewModel = hiltViewModel(),
    exerciseViewModel: ExerciseViewModel = hiltViewModel(),
    vocabViewModel: VocabViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    onNavigateTab: (Int) -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current
    val pixelHaptics = rememberPixelHaptics()
    val themeSettings by themeViewModel.themeSettings.collectAsStateWithLifecycle()
    val strings = getAppStrings(themeSettings.appLanguage)
    val isJapanese = themeSettings.appLanguage == com.personal.kakeibox.data.preferences.AppLanguage.JAPANESE
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

    val exerciseUiState by exerciseViewModel.uiState.collectAsStateWithLifecycle()
    val currentSalaryEntry by salaryViewModel.currentEntry.collectAsStateWithLifecycle()

    // Interactive Press States for Liquid Bento Matrix
    var isHeroPressed by remember { mutableStateOf(false) }
    var isKanjiCardFlipped by remember { mutableStateOf(false) }
    var isWorkoutLoggedToday by remember { mutableStateOf(false) }

    val todayExerciseCount = exerciseUiState.exercises.size
    val isRestDay = exerciseUiState.isRestDay

    val workoutDisplayText = remember(isRestDay, todayExerciseCount) {
        when {
            isRestDay -> "休 息 日"
            todayExerciseCount > 0 -> "🔥 $todayExerciseCount 種目"
            else -> "0 種目"
        }
    }

    val workoutStampText = remember(isRestDay, isWorkoutLoggedToday, todayExerciseCount) {
        when {
            isWorkoutLoggedToday -> "済"
            isRestDay -> "休 息"
            todayExerciseCount > 0 -> "鍛 錬"
            else -> "休 憩"
        }
    }

    val (savedBudgetText, budgetStampText) = remember(currentSalaryEntry, strings.saved) {
        val entry = currentSalaryEntry
        if (entry != null && entry.salaryAmount > 0) {
            val ratio = (entry.savingsAmount.toDouble() / entry.salaryAmount.toDouble()).coerceIn(0.0, 1.0)
            val percent = (ratio * 100).toInt()
            val text = "$percent% ${strings.saved}"
            val stamp = when {
                entry.remainingAmount < 0 -> "超 過"
                percent >= 50 -> "適 正"
                percent in 20..49 -> "節 約"
                percent in 1..19 -> "注 意"
                else -> "警 告"
            }
            Pair(text, stamp)
        } else {
            Pair("0% ${strings.saved}", "未 設定")
        }
    }

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
    val sheetColorScheme = currentColorScheme

    MaterialTheme(colorScheme = sheetColorScheme) {
        val chalkBg = MaterialTheme.colorScheme.surfaceContainerHigh
        val neonMint = MaterialTheme.colorScheme.primary
        val mintText = MaterialTheme.colorScheme.onSurface
        val textSecondary = MaterialTheme.colorScheme.onSurfaceVariant
        val chalkBorder = Color.Transparent
        val cardGradientColors = listOf(chalkBg, chalkBg)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 🎌 Vertical Japanese Margin Watermark (縦書き - Tate-gaki)
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 4.dp, top = statusBarsPadding + 90.dp)
                    .zIndex(0f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                "家計簿Ｘ・日常の極み".forEach { char ->
                    Text(
                        text = char.toString(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.28f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = statusBarsPadding + 64.dp)
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (themeSettings.themeStyle == com.personal.kakeibox.data.preferences.ThemeStyle.EDITORIAL_POSTER) {
                    // 🎭 FULL-BLEED EDITORIAL POSTER LAYOUT (Inspired by media_1789372937227.png)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(28.dp)
                    ) {
                        // 1. Monumental Stacked Condensed Title Header
                        StackedCondensedHeader(
                            lines = listOf("KAKEIBOX,", "EXPENSES AND", "SAVINGS"),
                            fontSize = 42,
                            lineHeight = 44,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // 2. Subtitle with Inline Keyword Highlight Pill Badges
                        InlineEditorialSentence(
                            textSegments = listOf(
                                EditorialSegment.Text("THE FATES OF YOUR"),
                                EditorialSegment.Badge(
                                    "FINANCES",
                                    badgeColor = MaterialTheme.colorScheme.tertiaryContainer,
                                    textColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onNavigateTab(1)
                                    }
                                ),
                                EditorialSegment.Text("THIS MONTH")
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // 3. Hero Financial Number Block (Poster Typography - Interactive)
                        Column(
                            modifier = Modifier.elasticClick {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onNavigateTab(1)
                            },
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = strings.totalCumulativeEarnings.uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = formattedTotalEarnings,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-2).sp,
                                lineHeight = 52.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // 4. Author / Category Metadata Block with Inline Pill Badges
                        InlineEditorialSentence(
                            textSegments = listOf(
                                EditorialSegment.Text("PAYDAY IN"),
                                EditorialSegment.Badge(
                                    "${paydayInfo.daysRemaining} DAYS",
                                    badgeColor = MaterialTheme.colorScheme.primaryContainer,
                                    textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onNavigateTab(1)
                                    }
                                ),
                                EditorialSegment.Text("SAVINGS"),
                                EditorialSegment.Badge(
                                    savedBudgetText,
                                    badgeColor = MaterialTheme.colorScheme.tertiaryContainer,
                                    textColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onNavigateTab(1)
                                    }
                                )
                            )
                        )

                        // 5. Rich Information Block A: Monthly Financial Allocation & Status
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "FINANCIAL ALLOCATION & STATUS",
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            InlineEditorialSentence(
                                textSegments = listOf(
                                    EditorialSegment.Text("BUDGET STATUS"),
                                    EditorialSegment.Badge(
                                        budgetStampText,
                                        badgeColor = MaterialTheme.colorScheme.secondaryContainer,
                                        textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            onNavigateTab(1)
                                        }
                                    ),
                                    EditorialSegment.Text("WORKOUT LOG"),
                                    EditorialSegment.Badge(
                                        workoutStampText,
                                        badgeColor = MaterialTheme.colorScheme.primaryContainer,
                                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            onNavigateTab(2)
                                        }
                                    )
                                )
                            )
                        }

                        // 6. Rich Information Block B: Daily Kotoba & Fitness Discipline
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "DAILY KOTOBA & FITNESS DISCIPLINE",
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            InlineEditorialSentence(
                                textSegments = listOf(
                                    EditorialSegment.Text("FEATURED KANJI"),
                                    EditorialSegment.Badge(
                                        featuredWord.kanjiWord,
                                        badgeColor = MaterialTheme.colorScheme.tertiaryContainer,
                                        textColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            onNavigateTab(3)
                                        }
                                    ),
                                    EditorialSegment.Text(featuredWord.furiganaReading.uppercase()),
                                    EditorialSegment.Text("ROUTINE"),
                                    EditorialSegment.Badge(
                                        workoutDisplayText,
                                        badgeColor = MaterialTheme.colorScheme.primaryContainer,
                                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            onNavigateTab(2)
                                        }
                                    )
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 7. Italicized Serif Editorial Quote Section
                        EditorialQuoteFooter(
                            quote = if (isJapanese) "継続は力なり。毎日少しずつ資産を積み重ねましょう。" else "Continuity is power. Manage your daily budget and savings with steady discipline.",
                            author = "KakeiBoX Editorial",
                            quoteColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    // ── STANDARD MONOLITHIC CARD SLATE ──
                    val heroCardShape = rememberExpressiveCardShape(isHeroPressed)

                    val heroScale by animateFloatAsState(
                        targetValue = if (isHeroPressed) 0.98f else 1.0f,
                        animationSpec = ExpressivePhysics.fluidSnappy(),
                        label = "hero_card_scale"
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        // ── Overhanging Floating Header Badge ──
                        var isLiveGaugePressed by remember { mutableStateOf(false) }
                        val liveGaugeShape = rememberExpressiveCardShape(isLiveGaugePressed, defaultCorner = 18.dp, pressedCorner = 30.dp)

                        Surface(
                            shape = liveGaugeShape,
                            color = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            shadowElevation = 0.dp,
                            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-16).dp, y = (-14).dp)
                                .zIndex(2f)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            isLiveGaugePressed = true
                                            tryAwaitRelease()
                                            isLiveGaugePressed = false
                                        }
                                    )
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "⚡ ${strings.liveGauge}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        // ── Main Monolithic Slate Surface Container ──
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    scaleX = heroScale
                                    scaleY = heroScale
                                },
                            shape = heroCardShape,
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            shadowElevation = 6.dp,
                            tonalElevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 22.dp, vertical = 24.dp),
                                verticalArrangement = Arrangement.spacedBy(26.dp)
                            ) {
                                // ── MODULE 1: PAYDAY COMMAND CORE ──
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
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
                                            imageVector = Icons.Outlined.Shield,
                                            contentDescription = "Shield",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = strings.heroCommandGauge,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 2.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    JapaneseRedHankoStamp(
                                        text = if (paydayInfo.daysRemaining == 0L) "完 済" else "給 料",
                                        rotation = -10f
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    var isPaydayBadgePressed by remember { mutableStateOf(false) }
                                    val paydayBadgeShape = rememberExpressiveCardShape(isPaydayBadgePressed, defaultCorner = 24.dp, pressedCorner = 36.dp)

                                    Surface(
                                        shape = paydayBadgeShape,
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                                        shadowElevation = 0.dp,
                                        modifier = Modifier
                                            .offset(x = (-10).dp)
                                            .zIndex(2f)
                                            .pointerInput(Unit) {
                                                detectTapGestures(
                                                    onPress = {
                                                        isPaydayBadgePressed = true
                                                        tryAwaitRelease()
                                                        isPaydayBadgePressed = false
                                                    }
                                                )
                                            }
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
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
                                                    fontSize = 58.sp,
                                                    fontWeight = FontWeight.Black,
                                                    lineHeight = 58.sp,
                                                    letterSpacing = (-2).sp,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    }

                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = strings.daysTillPayday,
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
                                            text = strings.remainingCommandBalance,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

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
                                            text = "⚡ ${strings.paydayCountdownTimeline}",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 1.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "${(paydayInfo.progressRatio * 100).toInt()}% ${strings.elapsed}",
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

                            // ── MODULE 2: INTERACTIVE 3D KOTOBA FLASHCARD (MIDDLE 35%) ──
                            val cardRotationY by animateFloatAsState(
                                targetValue = if (isKanjiCardFlipped) 180f else 0f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                                label = "kanji_flip_rotation"
                            )

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(340.dp)
                                    .graphicsLayer {
                                        rotationY = cardRotationY
                                        cameraDistance = 12 * density.density
                                    },
                                shape = RoundedCornerShape(24.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.6f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            isKanjiCardFlipped = !isKanjiCardFlipped
                                        }
                                        .padding(18.dp)
                                ) {
                                    if (cardRotationY <= 90f) {
                                        // Front Side
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
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
                                                        imageVector = Icons.Outlined.Translate,
                                                        contentDescription = "Kotoba",
                                                        tint = MaterialTheme.colorScheme.secondary,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                    Text(
                                                        text = "⛩️ 日本語カード",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Black,
                                                        letterSpacing = 1.sp,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                                Surface(
                                                    shape = RoundedCornerShape(16.dp),
                                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
                                                ) {
                                                    Text(
                                                        text = featuredWord.studyTag.ifEmpty { "JLPT N1" },
                                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
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
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = textSecondary
                                                )
                                                Text(
                                                    text = featuredWord.kanjiWord,
                                                    fontSize = 46.sp,
                                                    fontWeight = FontWeight.Black,
                                                    letterSpacing = 2.sp,
                                                    lineHeight = 50.sp,
                                                    color = mintText
                                                )
                                            }

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = featuredWord.meaning.uppercase(),
                                                    style = MaterialTheme.typography.titleSmall,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = mintText,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.weight(1f)
                                                )
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
                                        // Back Side
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .graphicsLayer { rotationY = 180f },
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Text(
                                                    text = "例文 EXAMPLE",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = MaterialTheme.colorScheme.secondary
                                                )
                                                Text(
                                                    text = featuredWord.exampleSentence.ifEmpty { "毎日少しずつ業務を改善していく。" },
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    lineHeight = 20.sp,
                                                    color = mintText
                                                )
                                            }

                                            Surface(
                                                shape = RoundedCornerShape(20.dp),
                                                color = MaterialTheme.colorScheme.secondary,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { onNavigateTab(3) }
                                            ) {
                                                Text(
                                                    text = "OPEN DECK ➔",
                                                    modifier = Modifier.padding(vertical = 12.dp),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black,
                                                    textAlign = TextAlign.Center,
                                                    color = MaterialTheme.colorScheme.onSecondary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            // ── MODULE 3: FITNESS, BUDGET & COMMUTER TICKET LAUNCHPAD (BOTTOM 35%) ──
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                isWorkoutLoggedToday = !isWorkoutLoggedToday
                                                onNavigateTab(3)
                                            },
                                        shape = RoundedCornerShape(20.dp),
                                        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.6f),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text(
                                                    text = strings.workout,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    letterSpacing = 1.sp,
                                                    color = MaterialTheme.colorScheme.tertiary
                                                )
                                                Text(
                                                    text = workoutDisplayText,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = mintText
                                                )
                                            }
                                            JapaneseRedHankoStamp(
                                                text = workoutStampText,
                                                rotation = -8f
                                            )
                                        }
                                    }

                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                onNavigateTab(1)
                                            },
                                        shape = RoundedCornerShape(20.dp),
                                        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.6f),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text(
                                                    text = strings.monthlyBudget,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    letterSpacing = 1.sp,
                                                    color = neonMint
                                                )
                                                Text(
                                                    text = savedBudgetText,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = mintText
                                                )
                                            }
                                            JapaneseRedHankoStamp(text = budgetStampText, rotation = -6f)
                                        }
                                    }
                                }

                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "🎫 ${strings.commandLaunchpad}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.5.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(TicketNotchShape)
                                                .clickable {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    onNavigateTab(1)
                                                },
                                            shape = TicketNotchShape,
                                            color = MaterialTheme.colorScheme.secondaryContainer,
                                            shadowElevation = 0.dp
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Wallet,
                                                    contentDescription = "Salary",
                                                    modifier = Modifier.size(16.dp),
                                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = strings.salary.uppercase(),
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                            }
                                        }

                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(TicketNotchShape)
                                                .clickable {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    onNavigateTab(3)
                                                },
                                            shape = TicketNotchShape,
                                            color = MaterialTheme.colorScheme.secondaryContainer,
                                            shadowElevation = 0.dp
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Translate,
                                                    contentDescription = "Kotoba",
                                                    modifier = Modifier.size(16.dp),
                                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = strings.kotoba.uppercase(),
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                            }
                                        }

                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(TicketNotchShape)
                                                .clickable {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    onNavigateTab(4)
                                                },
                                            shape = TicketNotchShape,
                                            color = neonMint,
                                            shadowElevation = 0.dp
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Settings,
                                                    contentDescription = "Settings",
                                                    modifier = Modifier.size(16.dp),
                                                    tint = MaterialTheme.colorScheme.onPrimary
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = strings.settings.uppercase(),
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = MaterialTheme.colorScheme.onPrimary
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
    }
}
}

