package com.personal.kakeibox.ui.setup

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 6 })
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    // Dynamic Corner Morphing for Next FAB Button:
    // Page 0 (Welcome): Circle (50.dp)
    // Page 1 (Lang/Currency): Squircle (20.dp)
    // Page 2 (Theme/Font): Organic Leaf (14.dp, 36.dp, 14.dp, 36.dp)
    // Page 3 (Work/Rest): Pill Asymmetric (28.dp, 10.dp, 28.dp, 10.dp)
    // Page 4 (Security): Clamshell (32.dp, 8.dp, 8.dp, 32.dp)
    // Page 5 (Finish): Full Pill (32.dp)
    val page = pagerState.currentPage

    val targetTopStart = when (page) {
        0 -> 50.dp
        1 -> 20.dp
        2 -> 14.dp
        3 -> 28.dp
        4 -> 32.dp
        else -> 32.dp
    }
    val targetTopEnd = when (page) {
        0 -> 50.dp
        1 -> 20.dp
        2 -> 36.dp
        3 -> 10.dp
        4 -> 8.dp
        else -> 32.dp
    }
    val targetBottomStart = when (page) {
        0 -> 50.dp
        1 -> 20.dp
        2 -> 14.dp
        3 -> 28.dp
        4 -> 8.dp
        else -> 32.dp
    }
    val targetBottomEnd = when (page) {
        0 -> 50.dp
        1 -> 20.dp
        2 -> 36.dp
        3 -> 10.dp
        4 -> 32.dp
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
                            text = "STEP ${pagerState.currentPage + 1} OF 6",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.5.sp
                        )
                    }

                    // Segmented Dots Indicator
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        repeat(6) { idx ->
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
                            3 -> WorkRestStep(themeSettings, themeViewModel)
                            4 -> SecurityPrivacyStep(themeSettings, themeViewModel)
                            5 -> FinishStep(themeSettings, onSetupComplete)
                        }
                    }
                }

                // Bottom Action Bar with Counter-Rotating Shape-Morphing FAB
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (pagerState.currentPage > 0) {
                        OutlinedButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Text("Back", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(80.dp))
                    }

                    // 🎬 Shape-Morphing 360° Counter-Rotating FAB
                    Surface(
                        modifier = Modifier
                            .height(60.dp)
                            .rotate(animatedRotation) // Outer container spins 360°
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (pagerState.currentPage < 5) {
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
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // 💡 Counter-rotate inner content so arrow stays level and upright!
                            Box(
                                modifier = Modifier.rotate(-animatedRotation),
                                contentAlignment = Alignment.Center
                            ) {
                                AnimatedContent(
                                    targetState = pagerState.currentPage == 5,
                                    transitionSpec = { (fadeIn() + scaleIn()).togetherWith(fadeOut() + scaleOut()) },
                                    label = "icon_completion_morph"
                                ) { isFinish ->
                                    if (isFinish) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Complete",
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Enter KakeiboX",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        }
                                    } else {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = "Next Step",
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(24.dp)
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
private fun WelcomeStep() {
    val infiniteTransition = rememberInfiniteTransition(label = "aura_pulse")
    val auraScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura_scale"
    )
    val auraAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura_alpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Ambient Pulsing Gradient Aura
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .graphicsLayer {
                        scaleX = auraScale
                        scaleY = auraScale
                        alpha = auraAlpha
                    }
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Glassmorphic Vitta (वित्त) Hero Crest Card
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                shadowElevation = 12.dp,
                modifier = Modifier.size(140.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "VITTA",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 3.sp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "वित्त",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "KakeiboX",
            style = MaterialTheme.typography.displayLarge.copy(
                textGeometricTransform = ExpTitleTransform,
                fontWeight = FontWeight.Black
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        ) {
            Text(
                text = "Expressive Personal Finance, Salary Analytics & Life Engine",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
private fun LangCurrencyStep(themeSettings: ThemeSettings, viewModel: ThemeViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Language & Currency", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Customize your regional localization and currency symbol.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(12.dp))

        Text("App Language", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilterChip(
                selected = themeSettings.appLanguage == AppLanguage.ENGLISH,
                onClick = { viewModel.setAppLanguage(AppLanguage.ENGLISH) },
                label = { Text("English 🇺🇸") }
            )
            FilterChip(
                selected = themeSettings.appLanguage == AppLanguage.JAPANESE,
                onClick = { viewModel.setAppLanguage(AppLanguage.JAPANESE) },
                label = { Text("日本語 🇯🇵") }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Currency Symbol", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("¥", "$", "€", "₹", "£").forEach { sym ->
                FilterChip(
                    selected = themeSettings.currencySymbol == sym,
                    onClick = { viewModel.setCurrencySymbol(sym) },
                    label = { Text(sym, fontWeight = FontWeight.Bold, fontSize = 18.sp) }
                )
            }
        }
    }
}

@Composable
private fun ThemeFontStep(themeSettings: ThemeSettings, viewModel: ThemeViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Theme & Typography", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Pick your favorite Material 3 design flavor & font family.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Design Flavor", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = themeSettings.themeFlavor == ThemeFlavor.DYNAMIC_MATERIAL,
                onClick = { viewModel.setThemeFlavor(ThemeFlavor.DYNAMIC_MATERIAL) },
                label = { Text("Dynamic Wallpaper") }
            )
            FilterChip(
                selected = themeSettings.themeFlavor == ThemeFlavor.SHU_NURI,
                onClick = { viewModel.setThemeFlavor(ThemeFlavor.SHU_NURI) },
                label = { Text("朱塗り Shu-Nuri") }
            )
            FilterChip(
                selected = themeSettings.themeFlavor == ThemeFlavor.O_MIKI,
                onClick = { viewModel.setThemeFlavor(ThemeFlavor.O_MIKI) },
                label = { Text("御神酒 O-Miki") }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Font Family", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = themeSettings.appFont == AppFont.GOOGLE_SANS_FLEX,
                onClick = { viewModel.setAppFont(AppFont.GOOGLE_SANS_FLEX) },
                label = { Text("Google Sans Rounded 🌟") }
            )
            FilterChip(
                selected = themeSettings.appFont == AppFont.NUNITO,
                onClick = { viewModel.setAppFont(AppFont.NUNITO) },
                label = { Text("Nunito") }
            )
        }
    }
}

@Composable
private fun WorkRestStep(themeSettings: ThemeSettings, viewModel: ThemeViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Work & Rest Days", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Configure your weekly rest days for salary and habit tracking.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday").take(4).forEach { day ->
                val selected = themeSettings.restDays.contains(day)
                FilterChip(
                    selected = selected,
                    onClick = {
                        val newDays = if (selected) themeSettings.restDays - day else themeSettings.restDays + day
                        viewModel.setTabOrder(themeSettings.tabOrder) // Keeps settings synced
                    },
                    label = { Text(day.take(3)) }
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Saturday", "Sunday").forEach { day ->
                val selected = themeSettings.restDays.contains(day)
                FilterChip(
                    selected = selected,
                    onClick = {
                        val newDays = if (selected) themeSettings.restDays - day else themeSettings.restDays + day
                    },
                    label = { Text(day) }
                )
            }
        }
    }
}

@Composable
private fun SecurityPrivacyStep(themeSettings: ThemeSettings, viewModel: ThemeViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Security & Privacy", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Protect your financial data with biometric authentication.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Fingerprint, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Biometric Lock", fontWeight = FontWeight.Bold)
                }
                Switch(
                    checked = themeSettings.biometricEnabled,
                    onCheckedChange = { viewModel.setBiometricEnabled(it) }
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VisibilityOff, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Privacy Mode", fontWeight = FontWeight.Bold)
                }
                Switch(
                    checked = themeSettings.privacyModeEnabled,
                    onCheckedChange = { viewModel.setPrivacyModeEnabled(it) }
                )
            }
        }
    }
}

@Composable
private fun FinishStep(themeSettings: ThemeSettings, onSetupComplete: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "You're All Set! 🎉",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Your setup preferences have been applied. Enjoy managing your salary, habits, and journeys with KakeiboX!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}
