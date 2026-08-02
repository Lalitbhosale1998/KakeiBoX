package com.personal.kakeibox.ui.setup

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.kakeibox.data.preferences.*
import com.personal.kakeibox.ui.settings.ThemeViewModel
import com.personal.kakeibox.ui.theme.ExpTitleTransform
import kotlinx.coroutines.launch

@Composable
fun SetupScreen(
    themeSettings: ThemeSettings,
    themeViewModel: ThemeViewModel,
    onSetupComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 5 })
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    // Dynamic Corner Morphing for Next FAB Button (5 Pages total):
    // Page 0 (Welcome): Circle (50.dp)
    // Page 1 (Lang/Currency): Squircle (20.dp)
    // Page 2 (Theme/Font): Organic Leaf (14.dp, 36.dp, 14.dp, 36.dp)
    // Page 3 (Security): Clamshell (32.dp, 8.dp, 8.dp, 32.dp)
    // Page 4 (Finish): Full Pill (32.dp)
    val page = pagerState.currentPage

    val targetTopStart = when (page) {
        0 -> 50.dp
        1 -> 20.dp
        2 -> 14.dp
        3 -> 32.dp
        else -> 32.dp
    }
    val targetTopEnd = when (page) {
        0 -> 50.dp
        1 -> 20.dp
        2 -> 36.dp
        3 -> 8.dp
        else -> 32.dp
    }
    val targetBottomStart = when (page) {
        0 -> 50.dp
        1 -> 20.dp
        2 -> 14.dp
        3 -> 8.dp
        else -> 32.dp
    }
    val targetBottomEnd = when (page) {
        0 -> 50.dp
        1 -> 20.dp
        2 -> 36.dp
        3 -> 32.dp
        else -> 32.dp
    }

    val springSpec = spring<Float>(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)

    val topStartAnim by animateDpAsState(targetValue = targetTopStart, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "ts")
    val topEndAnim by animateDpAsState(targetValue = targetTopEnd, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "te")
    val bottomStartAnim by animateDpAsState(targetValue = targetBottomStart, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "bs")
    val bottomEndAnim by animateDpAsState(targetValue = targetBottomEnd, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "be")

    // Continuous float offset for 360° rotation choreography
    val continuousPageOffset = pagerState.currentPage + pagerState.currentPageOffsetFraction
    val animatedRotation by animateFloatAsState(
        targetValue = continuousPageOffset * 360f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "fab_spin"
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                // 🚥 Redesigned M3 Expressive Header Stepper
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surfaceContainerHighest,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "STEP ${pagerState.currentPage + 1} OF 5",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.5.sp
                        )
                    }

                    // Segmented Dots Indicator
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { idx ->
                            val isActive = idx == pagerState.currentPage
                            val dotWidth by animateDpAsState(
                                targetValue = if (isActive) 24.dp else 8.dp,
                                animationSpec = spring(stiffness = Spring.StiffnessLow),
                                label = "dot_w"
                            )
                            Box(
                                modifier = Modifier
                                    .height(8.dp)
                                    .width(dotWidth)
                                    .clip(CircleShape)
                                    .background(
                                        if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                    )
                            )
                        }
                    }
                }

                // Main Onboarding Pager
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f)
                ) { pageIndex ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        when (pageIndex) {
                            0 -> WelcomeStep()
                            1 -> LangCurrencyStep(themeSettings, themeViewModel)
                            2 -> ThemeFontStep(themeSettings, themeViewModel)
                            3 -> SecurityPrivacyStep(themeSettings, themeViewModel)
                            4 -> FinishStep(themeSettings, onSetupComplete)
                        }
                    }
                }

                // 🛸 Floating Glass Pill Dock Capsule
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.94f),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                    shadowElevation = 14.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (pagerState.currentPage > 0) {
                            TextButton(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                }
                            ) {
                                Text("Back", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        } else {
                            Text(
                                text = "Let's Go!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }

                        // 🎬 Shape-Morphing 360° Counter-Rotating FAB
                        Surface(
                            modifier = Modifier
                                .height(54.dp)
                                .rotate(animatedRotation) // Outer container spins 360°
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    if (pagerState.currentPage < 4) {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        }
                                    } else {
                                        themeViewModel.setSetupComplete(true)
                                        onSetupComplete()
                                    }
                                },
                            shape = RoundedCornerShape(
                                topStart = topStartAnim,
                                topEnd = topEndAnim,
                                bottomStart = bottomStartAnim,
                                bottomEnd = bottomEndAnim
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            shadowElevation = 6.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                // 💡 Counter-rotate inner content so arrow stays level and upright!
                                Box(
                                    modifier = Modifier.rotate(-animatedRotation),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AnimatedContent(
                                        targetState = pagerState.currentPage == 4,
                                        transitionSpec = { (fadeIn() + scaleIn()).togetherWith(fadeOut() + scaleOut()) },
                                        label = "icon_completion_morph"
                                    ) { isFinish ->
                                        if (isFinish) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Complete",
                                                    tint = MaterialTheme.colorScheme.onPrimary,
                                                    modifier = Modifier.size(22.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "Enter Vitta",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 15.sp,
                                                    color = MaterialTheme.colorScheme.onPrimary
                                                )
                                            }
                                        } else {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                                contentDescription = "Next Step",
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.size(22.dp)
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

@Composable
private fun WelcomeStep() {
    val infiniteTransition = rememberInfiniteTransition(label = "aura_pulse")
    val auraScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura_scale"
    )
    val auraAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura_alpha"
    )

    // FAB-Style Continuous Looping Morphing Shape Keyframes
    val morphTopStart by infiniteTransition.animateFloat(
        initialValue = 50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                50f at 0 with FastOutSlowInEasing
                20f at 1500 with FastOutSlowInEasing
                14f at 3000 with FastOutSlowInEasing
                36f at 4500 with FastOutSlowInEasing
                50f at 6000 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "m_ts"
    )

    val morphTopEnd by infiniteTransition.animateFloat(
        initialValue = 50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                50f at 0 with FastOutSlowInEasing
                20f at 1500 with FastOutSlowInEasing
                38f at 3000 with FastOutSlowInEasing
                10f at 4500 with FastOutSlowInEasing
                50f at 6000 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "m_te"
    )

    val morphBottomStart by infiniteTransition.animateFloat(
        initialValue = 50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                50f at 0 with FastOutSlowInEasing
                20f at 1500 with FastOutSlowInEasing
                14f at 3000 with FastOutSlowInEasing
                10f at 4500 with FastOutSlowInEasing
                50f at 6000 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "m_bs"
    )

    val morphBottomEnd by infiniteTransition.animateFloat(
        initialValue = 50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                50f at 0 with FastOutSlowInEasing
                20f at 1500 with FastOutSlowInEasing
                38f at 3000 with FastOutSlowInEasing
                36f at 4500 with FastOutSlowInEasing
                50f at 6000 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "m_be"
    )

    val morphRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "m_rot"
    )

    val morphRotationCCW by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "m_rot_ccw"
    )

    // Shape 4 Polymorphic Keyframes (Circle -> Pentagon -> Gem -> Ghost)
    val shape4CornerTopStart by infiniteTransition.animateFloat(
        initialValue = 50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 8000
                50f at 0 with FastOutSlowInEasing     // Circle
                8f at 2000 with FastOutSlowInEasing      // Pentagon
                4f at 4000 with FastOutSlowInEasing      // Gem
                40f at 6000 with FastOutSlowInEasing     // Ghost
                50f at 8000 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "s4_ts"
    )

    val shape4CornerTopEnd by infiniteTransition.animateFloat(
        initialValue = 50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 8000
                50f at 0 with FastOutSlowInEasing
                40f at 2000 with FastOutSlowInEasing
                44f at 4000 with FastOutSlowInEasing
                40f at 6000 with FastOutSlowInEasing
                50f at 8000 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "s4_te"
    )

    val shape4CornerBottomStart by infiniteTransition.animateFloat(
        initialValue = 50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 8000
                50f at 0 with FastOutSlowInEasing
                40f at 2000 with FastOutSlowInEasing
                4f at 4000 with FastOutSlowInEasing
                4f at 6000 with FastOutSlowInEasing
                50f at 8000 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "s4_bs"
    )

    val shape4CornerBottomEnd by infiniteTransition.animateFloat(
        initialValue = 50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 8000
                50f at 0 with FastOutSlowInEasing
                8f at 2000 with FastOutSlowInEasing
                44f at 4000 with FastOutSlowInEasing
                4f at 6000 with FastOutSlowInEasing
                50f at 8000 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "s4_be"
    )

    // Shape 5 N-Sided Cookie Lobe Counter (4 -> 6 -> 7 -> 9 -> 12 Cookie)
    val cookieLobes by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 10000
                4f at 0 with LinearOutSlowInEasing
                6f at 2000 with LinearOutSlowInEasing
                7f at 4000 with LinearOutSlowInEasing
                9f at 6000 with LinearOutSlowInEasing
                12f at 8000 with LinearOutSlowInEasing
                4f at 10000 with LinearOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "cookie_lobes"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 🏛️ Top-Left Aligned Editorial Header Hierarchy
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(
                text = "Welcome to",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Vitta",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Black
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // 🎨 Center Hero Canvas with 5 Separate Non-Overlapping Floating Morphing Shapes
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            // Ambient Pulsing Radial Aura in Center Background
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .graphicsLayer {
                        scaleX = auraScale
                        scaleY = auraScale
                        alpha = auraAlpha
                    }
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // 🌀 Shape 1: Main Hero M3 Morphing Shape (Center - 145.dp)
            Box(
                modifier = Modifier
                    .size(145.dp)
                    .rotate(morphRotation)
                    .clip(
                        RoundedCornerShape(
                            topStart = morphTopStart.dp,
                            topEnd = morphTopEnd.dp,
                            bottomStart = morphBottomStart.dp,
                            bottomEnd = morphBottomEnd.dp
                        )
                    )
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            )
                        )
                    )
            )

            // 🌀 Shape 2: Top-Right Floating Accent (90.dp)
            Box(
                modifier = Modifier
                    .offset(x = 110.dp, y = (-75).dp)
                    .size(90.dp)
                    .rotate(morphRotationCCW)
                    .clip(
                        RoundedCornerShape(
                            topStart = morphBottomEnd.dp,
                            topEnd = morphBottomStart.dp,
                            bottomStart = morphTopEnd.dp,
                            bottomEnd = morphTopStart.dp
                        )
                    )
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.85f),
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.45f)
                            )
                        )
                    )
            )

            // 🌀 Shape 3: Bottom-Left Floating Accent (75.dp)
            Box(
                modifier = Modifier
                    .offset(x = (-105).dp, y = 70.dp)
                    .size(75.dp)
                    .rotate(morphRotation)
                    .clip(
                        RoundedCornerShape(
                            topStart = morphTopEnd.dp,
                            topEnd = morphBottomEnd.dp,
                            bottomStart = morphTopStart.dp,
                            bottomEnd = morphBottomStart.dp
                        )
                    )
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.6f)
                            )
                        )
                    )
            )

            // 🌀 Shape 4: Top-Left Polymorphic Accent (Circle -> Pentagon -> Gem -> Ghost - 65.dp)
            Box(
                modifier = Modifier
                    .offset(x = (-115).dp, y = (-70).dp)
                    .size(65.dp)
                    .rotate(morphRotationCCW)
                    .clip(
                        RoundedCornerShape(
                            topStart = shape4CornerTopStart.dp,
                            topEnd = shape4CornerTopEnd.dp,
                            bottomStart = shape4CornerBottomStart.dp,
                            bottomEnd = shape4CornerBottomEnd.dp
                        )
                    )
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.75f),
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                            )
                        )
                    )
            )

            // 🌀 Shape 5: Bottom-Right N-Sided Scalloped Cookie Engine (4 -> 6 -> 7 -> 9 -> 12 Cookie - 75.dp)
            val cookiePrimaryColor = MaterialTheme.colorScheme.primary
            val cookieTertiaryColor = MaterialTheme.colorScheme.tertiaryContainer
            Canvas(
                modifier = Modifier
                    .offset(x = 110.dp, y = 75.dp)
                    .size(75.dp)
                    .rotate(morphRotation)
            ) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val baseRadius = size.minDimension / 2f * 0.76f
                val amplitude = size.minDimension / 2f * 0.18f
                val numPoints = 120
                val path = Path()
                for (i in 0..numPoints) {
                    val angle = (i.toFloat() / numPoints) * 2f * Math.PI.toFloat()
                    val r = baseRadius + amplitude * kotlin.math.cos(cookieLobes * angle).toFloat()
                    val x = center.x + r * kotlin.math.cos(angle).toFloat()
                    val y = center.y + r * kotlin.math.sin(angle).toFloat()
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
                drawPath(
                    path = path,
                    brush = Brush.linearGradient(
                        listOf(
                            cookiePrimaryColor.copy(alpha = 0.75f),
                            cookieTertiaryColor.copy(alpha = 0.85f)
                        )
                    )
                )
            }
        }

        // Subtitle Prompt Line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Let's get everything set up for you.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LangCurrencyStep(themeSettings: ThemeSettings, viewModel: ThemeViewModel) {
    val haptic = LocalHapticFeedback.current
    val infiniteTransition = rememberInfiniteTransition(label = "lang_hero_morph")
    val morphCorner by infiniteTransition.animateFloat(
        initialValue = 18f,
        targetValue = 46f,
        animationSpec = infiniteRepeatable(
            animation = tween(3400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "l_corner"
    )
    val morphRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(16000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "l_rot"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 🏛️ Top-Left Aligned Editorial Header Hierarchy
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(
                text = "Regional &",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Localization",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Black
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Set your preferred app language & primary currency format.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 🎨 Floating Glass Bento Card Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            // Background Morphing Currency Shape Accent
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .rotate(morphRotation)
                    .clip(RoundedCornerShape(morphCorner.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
                            )
                        )
                    )
            )

            // 💳 Card-in-Card Bento Container
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.95f),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                shadowElevation = 14.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // App Language Section
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🌐", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "APP LANGUAGE",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.2.sp
                            )
                        }

                        val haptic = LocalHapticFeedback.current
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val isEnglish = themeSettings.appLanguage == AppLanguage.ENGLISH
                            val engScale by animateFloatAsState(
                                targetValue = if (isEnglish) 1.04f else 1.0f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                                label = "eng_scale"
                            )
                            val engTopStart by animateDpAsState(targetValue = if (isEnglish) 22.dp else 14.dp, label = "eng_ts")
                            val engBottomEnd by animateDpAsState(targetValue = if (isEnglish) 22.dp else 14.dp, label = "eng_be")

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(54.dp)
                                    .graphicsLayer {
                                        scaleX = engScale
                                        scaleY = engScale
                                    }
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.setAppLanguage(AppLanguage.ENGLISH)
                                    },
                                shape = RoundedCornerShape(
                                    topStart = engTopStart,
                                    topEnd = 12.dp,
                                    bottomStart = 12.dp,
                                    bottomEnd = engBottomEnd
                                ),
                                color = if (isEnglish) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                                border = BorderStroke(
                                    if (isEnglish) 2.dp else 1.dp,
                                    if (isEnglish) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                ),
                                shadowElevation = if (isEnglish) 6.dp else 0.dp
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    AnimatedVisibility(
                                        visible = isEnglish,
                                        enter = fadeIn() + scaleIn(),
                                        exit = fadeOut() + scaleOut()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp).padding(end = 4.dp)
                                        )
                                    }
                                    Text(
                                        text = "English 🇺🇸",
                                        fontWeight = FontWeight.Bold,
                                        color = if (isEnglish) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            val isJapanese = themeSettings.appLanguage == AppLanguage.JAPANESE
                            val japScale by animateFloatAsState(
                                targetValue = if (isJapanese) 1.04f else 1.0f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                                label = "jap_scale"
                            )
                            val japTopStart by animateDpAsState(targetValue = if (isJapanese) 22.dp else 14.dp, label = "jap_ts")
                            val japBottomEnd by animateDpAsState(targetValue = if (isJapanese) 22.dp else 14.dp, label = "jap_be")

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(54.dp)
                                    .graphicsLayer {
                                        scaleX = japScale
                                        scaleY = japScale
                                    }
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.setAppLanguage(AppLanguage.JAPANESE)
                                    },
                                shape = RoundedCornerShape(
                                    topStart = japTopStart,
                                    topEnd = 12.dp,
                                    bottomStart = 12.dp,
                                    bottomEnd = japBottomEnd
                                ),
                                color = if (isJapanese) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                                border = BorderStroke(
                                    if (isJapanese) 2.dp else 1.dp,
                                    if (isJapanese) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                ),
                                shadowElevation = if (isJapanese) 6.dp else 0.dp
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    AnimatedVisibility(
                                        visible = isJapanese,
                                        enter = fadeIn() + scaleIn(),
                                        exit = fadeOut() + scaleOut()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp).padding(end = 4.dp)
                                        )
                                    }
                                    Text(
                                        text = "日本語 🇯🇵",
                                        fontWeight = FontWeight.Bold,
                                        color = if (isJapanese) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                    // Currency Symbol Section
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("💱", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "PRIMARY CURRENCY SYMBOL",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.2.sp
                            )
                        }

                        val currencySymbols = listOf("¥", "$", "€", "₹", "£")
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            currencySymbols.forEach { sym ->
                                val isSelected = themeSettings.currencySymbol == sym
                                val curScale by animateFloatAsState(
                                    targetValue = if (isSelected) 1.08f else 1.0f,
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                                    label = "cur_s_$sym"
                                )
                                val curTopStart by animateDpAsState(targetValue = if (isSelected) 22.dp else 12.dp, label = "cur_ts_$sym")
                                val curBottomEnd by animateDpAsState(targetValue = if (isSelected) 22.dp else 12.dp, label = "cur_be_$sym")

                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(56.dp)
                                        .graphicsLayer {
                                            scaleX = curScale
                                            scaleY = curScale
                                        }
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            viewModel.setCurrencySymbol(sym)
                                        },
                                    shape = RoundedCornerShape(
                                        topStart = curTopStart,
                                        topEnd = 10.dp,
                                        bottomStart = 10.dp,
                                        bottomEnd = curBottomEnd
                                    ),
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerLow,
                                    border = BorderStroke(
                                        if (isSelected) 2.dp else 1.dp,
                                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                    ),
                                    shadowElevation = if (isSelected) 8.dp else 0.dp
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = sym,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 20.sp,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
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

@Composable
private fun ThemeFontStep(themeSettings: ThemeSettings, viewModel: ThemeViewModel) {
    val haptic = LocalHapticFeedback.current
    val infiniteTransition = rememberInfiniteTransition(label = "theme_hero_morph")
    val morphCorner by infiniteTransition.animateFloat(
        initialValue = 24f,
        targetValue = 54f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "t_corner"
    )
    val morphRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "t_rot"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 🏛️ Top-Left Aligned Editorial Header Hierarchy
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(
                text = "Theme &",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Expressive Style",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Black
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Pick your Material 3 design flavor & font family.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 🎨 Open Canvas Floating Options (No Bento Box Container!)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            // Background Floating Morphing Palette Accent Shape
            Box(
                modifier = Modifier
                    .size(210.dp)
                    .rotate(morphRotation)
                    .clip(RoundedCornerShape(morphCorner.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                            )
                        )
                    )
            )

            // 🌟 Open Canvas Floating Selection List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section 1: Design Flavor
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🎨", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "DESIGN FLAVOR",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.2.sp
                        )
                    }

                    val flavors = listOf(
                        Triple(ThemeFlavor.DYNAMIC_MATERIAL, "Dynamic Wallpaper", "🎨"),
                        Triple(ThemeFlavor.SHU_NURI, "朱塗り Shu-Nuri", "⛩️"),
                        Triple(ThemeFlavor.O_MIKI, "御神酒 O-Miki", "🍶")
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        flavors.forEach { (flavor, title, icon) ->
                            val isSelected = themeSettings.themeFlavor == flavor
                            val scale by animateFloatAsState(
                                targetValue = if (isSelected) 1.05f else 1.0f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                                label = "flv_s_$title"
                            )
                            val topStart by animateDpAsState(targetValue = if (isSelected) 22.dp else 12.dp, label = "flv_ts_$title")
                            val bottomEnd by animateDpAsState(targetValue = if (isSelected) 22.dp else 12.dp, label = "flv_be_$title")

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp)
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                    }
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.setThemeFlavor(flavor)
                                    },
                                shape = RoundedCornerShape(
                                    topStart = topStart,
                                    topEnd = 10.dp,
                                    bottomStart = 10.dp,
                                    bottomEnd = bottomEnd
                                ),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                                border = BorderStroke(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                ),
                                shadowElevation = if (isSelected) 8.dp else 2.dp
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    AnimatedVisibility(
                                        visible = isSelected,
                                        enter = fadeIn() + scaleIn(),
                                        exit = fadeOut() + scaleOut()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp).padding(end = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = "$icon $title",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))

                // Section 2: Font Family
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🔤", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "FONT FAMILY",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.2.sp
                        )
                    }

                    val fonts = listOf(
                        Pair(AppFont.GOOGLE_SANS_FLEX, "Google Sans 🌟"),
                        Pair(AppFont.NUNITO, "Nunito ✒️"),
                        Pair(AppFont.CLIMATE_CRISIS, "Climate 🌋"),
                        Pair(AppFont.LUCKIEST_GUY, "Luckiest 🎯"),
                        Pair(AppFont.DELA_GOTHIC_ONE, "Dela Gothic ⛩️"),
                        Pair(AppFont.HACHI_MARU_POP, "Hachi Maru 🌸"),
                        Pair(AppFont.KOSUGI_MARU, "Kosugi Maru 🍡"),
                        Pair(AppFont.MOCHIY_POP_P_ONE, "Mochiy Pop 🍡"),
                        Pair(AppFont.POTTA_ONE, "Potta One 🍵"),
                        Pair(AppFont.RAMPART_ONE, "Rampart One 🏯"),
                        Pair(AppFont.WDXL_LUBRIFONT_JPN, "WDXL JPN 🎌")
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        fonts.forEach { (font, title) ->
                            val isSelected = themeSettings.appFont == font
                            val scale by animateFloatAsState(
                                targetValue = if (isSelected) 1.05f else 1.0f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                                label = "fnt_s_$title"
                            )
                            val topStart by animateDpAsState(targetValue = if (isSelected) 22.dp else 12.dp, label = "fnt_ts_$title")
                            val bottomEnd by animateDpAsState(targetValue = if (isSelected) 22.dp else 12.dp, label = "fnt_be_$title")

                            Surface(
                                modifier = Modifier
                                    .height(54.dp)
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                    }
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.setAppFont(font)
                                    },
                                shape = RoundedCornerShape(
                                    topStart = topStart,
                                    topEnd = 10.dp,
                                    bottomStart = 10.dp,
                                    bottomEnd = bottomEnd
                                ),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                                border = BorderStroke(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                ),
                                shadowElevation = if (isSelected) 8.dp else 2.dp
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    AnimatedVisibility(
                                        visible = isSelected,
                                        enter = fadeIn() + scaleIn(),
                                        exit = fadeOut() + scaleOut()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp).padding(end = 4.dp)
                                        )
                                    }
                                    Text(
                                        text = title,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
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
private fun SecurityPrivacyStep(themeSettings: ThemeSettings, viewModel: ThemeViewModel) {
    val haptic = LocalHapticFeedback.current
    val infiniteTransition = rememberInfiniteTransition(label = "sec_hero_morph")
    val morphCorner by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = 48f,
        animationSpec = infiniteRepeatable(
            animation = tween(3600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "s_corner"
    )
    val morphRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(16000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "s_rot"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 🏛️ Top-Left Aligned Editorial Header Hierarchy
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(
                text = "Security &",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Privacy",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Black
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Protect your financial data with biometric authentication.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 🎨 Floating Morphing Canvas & Controls
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            // Background Floating Morphing Shield Accent Shape
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .rotate(morphRotation)
                    .clip(RoundedCornerShape(morphCorner.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)
                            )
                        )
                    )
            )

            // 🔐 Security Toggle Cards
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Biometric Lock Card
                val isBio = themeSettings.biometricEnabled
                val bioCorner by animateDpAsState(targetValue = if (isBio) 24.dp else 16.dp, label = "bio_c")
                val bioScale by animateFloatAsState(
                    targetValue = if (isBio) 1.03f else 1.0f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "bio_s"
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            scaleX = bioScale
                            scaleY = bioScale
                        },
                    shape = RoundedCornerShape(
                        topStart = bioCorner,
                        topEnd = 14.dp,
                        bottomStart = 14.dp,
                        bottomEnd = bioCorner
                    ),
                    color = if (isBio) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                    border = BorderStroke(
                        if (isBio) 2.dp else 1.dp,
                        if (isBio) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    ),
                    shadowElevation = if (isBio) 8.dp else 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = null,
                                tint = if (isBio) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Biometric Lock",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isBio) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Unlock app with fingerprint or Face ID",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = isBio,
                            onCheckedChange = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.setBiometricEnabled(it)
                            }
                        )
                    }
                }

                // Privacy Mode Card
                val isPriv = themeSettings.privacyModeEnabled
                val privCorner by animateDpAsState(targetValue = if (isPriv) 24.dp else 16.dp, label = "priv_c")
                val privScale by animateFloatAsState(
                    targetValue = if (isPriv) 1.03f else 1.0f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "priv_s"
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            scaleX = privScale
                            scaleY = privScale
                        },
                    shape = RoundedCornerShape(
                        topStart = privCorner,
                        topEnd = 14.dp,
                        bottomStart = 14.dp,
                        bottomEnd = privCorner
                    ),
                    color = if (isPriv) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                    border = BorderStroke(
                        if (isPriv) 2.dp else 1.dp,
                        if (isPriv) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    ),
                    shadowElevation = if (isPriv) 8.dp else 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = if (isPriv) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Privacy Mode",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPriv) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Mask account balances on dashboard",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = isPriv,
                            onCheckedChange = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.setPrivacyModeEnabled(it)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FinishStep(themeSettings: ThemeSettings, onSetupComplete: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "finish_hero_morph")
    val morphCorner by infiniteTransition.animateFloat(
        initialValue = 28f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "f_corner"
    )
    val morphRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "f_rot"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 🏛️ Top-Left Aligned Editorial Header Hierarchy
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(
                text = "Ready for",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Vitta 🎉",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Black
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Your personalized financial workspace is completely configured.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 🎨 Celebration Morphing Canvas with Config Badges
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            // Background Pulsing Celebration Aura & Morphing Shape
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .rotate(morphRotation)
                    .clip(RoundedCornerShape(morphCorner.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
                                MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = "All Set",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(72.dp)
                )
            }

            // Summary Configuration Chips Floating Below
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "YOUR SETUP PREFERENCES",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.5.sp
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val langLabel = if (themeSettings.appLanguage == AppLanguage.ENGLISH) "🇺🇸 English" else "🇯🇵 日本語"
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    ) {
                        Text(langLabel, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    ) {
                        Text("Symbol ${themeSettings.currencySymbol}", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    ) {
                        val secText = if (themeSettings.biometricEnabled) "🔐 Biometrics" else "🔓 Standard"
                        Text(secText, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
