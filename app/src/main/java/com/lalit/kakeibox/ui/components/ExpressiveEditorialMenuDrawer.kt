package com.personal.kakeibox.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Translate
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.personal.kakeibox.data.preferences.ThemeSettings
import com.personal.kakeibox.ui.theme.ExpressivePhysics
import com.personal.kakeibox.ui.theme.getAppStrings

/**
 * ⚡ ExpressiveEditorialMenuDrawer
 * Full-bleed Avant-Garde M3 Expressive Kinetic Navigation Portal.
 */
@Composable
fun ExpressiveEditorialMenuDrawer(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onNavigateTab: (String) -> Unit,
    onTogglePrivacyMode: () -> Unit,
    onAddEntry: () -> Unit,
    onOpenThemeSettings: () -> Unit,
    themeSettings: ThemeSettings
) {
    if (!isOpen) return

    val strings = getAppStrings(themeSettings.appLanguage)
    val haptic = LocalHapticFeedback.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        val containerBg = MaterialTheme.colorScheme.surfaceContainerLowest
        val primaryColor = MaterialTheme.colorScheme.primary
        val tertiaryColor = MaterialTheme.colorScheme.tertiary
        val onSurfaceColor = MaterialTheme.colorScheme.onSurface

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.18f),
                            tertiaryColor.copy(alpha = 0.08f),
                            containerBg
                        ),
                        radius = 1800f
                    )
                )
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // ── 1. Top Avant-Garde Header Dock ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = strings.portalHeader,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = onSurfaceColor.copy(alpha = 0.6f)
                    )

                    Surface(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onDismiss()
                            },
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Menu",
                                tint = onSurfaceColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── 2. Master Monolithic Navigation Slate Container (Snug Wrap Content, 0.dp Shadow) ──
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val hiddenTabs = themeSettings.hiddenTabs

                        EditorialBentoCard(
                            index = "01",
                            kanjiHero = "統括",
                            title = strings.homeOverviewSub,
                            subtitle = strings.cmdDashboardSub,
                            icon = Icons.Default.Home,
                            onClick = {
                                onNavigateTab("home")
                                onDismiss()
                            }
                        )

                        if (!hiddenTabs.contains("salary")) {
                            EditorialBentoCard(
                                index = "02",
                                kanjiHero = "資産",
                                title = strings.salSavingsSub,
                                subtitle = strings.finNexusSub,
                                icon = Icons.Default.AccountBalance,
                                onClick = {
                                    onNavigateTab("salary")
                                    onDismiss()
                                }
                            )
                        }

                        if (!hiddenTabs.contains("exercise")) {
                            EditorialBentoCard(
                                index = "03",
                                kanjiHero = "鍛錬",
                                title = strings.workGymSub,
                                subtitle = strings.athForgeSub,
                                icon = Icons.Default.FitnessCenter,
                                onClick = {
                                    onNavigateTab("exercise")
                                    onDismiss()
                                }
                            )
                        }

                        if (!hiddenTabs.contains("kotoba")) {
                            EditorialBentoCard(
                                index = "04",
                                kanjiHero = "言葉",
                                title = strings.japKotobaSub,
                                subtitle = strings.vocabDeckSub,
                                icon = Icons.Default.Translate,
                                onClick = {
                                    onNavigateTab("kotoba")
                                    onDismiss()
                                }
                            )
                        }

                        EditorialBentoCard(
                            index = "05",
                            kanjiHero = "美学",
                            title = strings.themeStudioSub,
                            subtitle = strings.aesCustomSub,
                            icon = Icons.Default.Palette,
                            onClick = {
                                onOpenThemeSettings()
                                onDismiss()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── 3. Japandi Flat Control Slate with Dynamic Shape Morphing (0.dp Shadow) ──
                var isPrivacyPressed by remember { mutableStateOf(false) }
                var isClosePressed by remember { mutableStateOf(false) }

                val privacyTopStart by animateDpAsState(
                    targetValue = when {
                        isPrivacyPressed -> 28.dp
                        themeSettings.privacyModeEnabled -> 26.dp
                        else -> 18.dp
                    },
                    animationSpec = ExpressivePhysics.fluidSnappy(),
                    label = "privacy_top_start"
                )
                val privacyTopEnd by animateDpAsState(
                    targetValue = when {
                        isPrivacyPressed -> 10.dp
                        themeSettings.privacyModeEnabled -> 10.dp
                        else -> 18.dp
                    },
                    animationSpec = ExpressivePhysics.fluidSnappy(),
                    label = "privacy_top_end"
                )
                val privacyBottomStart by animateDpAsState(
                    targetValue = when {
                        isPrivacyPressed -> 10.dp
                        themeSettings.privacyModeEnabled -> 10.dp
                        else -> 18.dp
                    },
                    animationSpec = ExpressivePhysics.fluidSnappy(),
                    label = "privacy_bottom_start"
                )
                val privacyBottomEnd by animateDpAsState(
                    targetValue = when {
                        isPrivacyPressed -> 28.dp
                        themeSettings.privacyModeEnabled -> 26.dp
                        else -> 18.dp
                    },
                    animationSpec = ExpressivePhysics.fluidSnappy(),
                    label = "privacy_bottom_end"
                )

                val privacyScale by animateFloatAsState(
                    targetValue = if (isPrivacyPressed) 0.96f else 1.0f,
                    animationSpec = ExpressivePhysics.fluidSnappy(),
                    label = "privacy_scale"
                )

                val privacyShape = RoundedCornerShape(
                    topStart = privacyTopStart,
                    topEnd = privacyTopEnd,
                    bottomStart = privacyBottomStart,
                    bottomEnd = privacyBottomEnd
                )

                val closeTopStart by animateDpAsState(
                    targetValue = if (isClosePressed) 26.dp else 18.dp,
                    animationSpec = ExpressivePhysics.fluidSnappy(),
                    label = "close_top_start"
                )
                val closeBottomEnd by animateDpAsState(
                    targetValue = if (isClosePressed) 8.dp else 18.dp,
                    animationSpec = ExpressivePhysics.fluidSnappy(),
                    label = "close_bottom_end"
                )

                val closeScale by animateFloatAsState(
                    targetValue = if (isClosePressed) 0.95f else 1.0f,
                    animationSpec = ExpressivePhysics.fluidSnappy(),
                    label = "close_scale"
                )

                val closeShape = RoundedCornerShape(
                    topStart = closeTopStart,
                    topEnd = 18.dp,
                    bottomStart = 18.dp,
                    bottomEnd = closeBottomEnd
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Privacy Mode Switch Button with Shape Morphing
                        Surface(
                            shape = privacyShape,
                            color = if (themeSettings.privacyModeEnabled) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                            border = BorderStroke(1.dp, if (themeSettings.privacyModeEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .weight(1f)
                                .graphicsLayer {
                                    scaleX = privacyScale
                                    scaleY = privacyScale
                                }
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            isPrivacyPressed = true
                                            tryAwaitRelease()
                                            isPrivacyPressed = false
                                        },
                                        onTap = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            onTogglePrivacyMode()
                                        }
                                    )
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = if (themeSettings.privacyModeEnabled) Icons.Default.Lock else Icons.Default.LockOpen,
                                    contentDescription = null,
                                    tint = if (themeSettings.privacyModeEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = if (themeSettings.privacyModeEnabled) "🔒 匿名 (Privacy: ON)" else "🔓 匿名 (Privacy: OFF)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (themeSettings.privacyModeEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Close Portal Pill with Kinetic Morphing
                        Surface(
                            shape = closeShape,
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = closeScale
                                    scaleY = closeScale
                                }
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            isClosePressed = true
                                            tryAwaitRelease()
                                            isClosePressed = false
                                        },
                                        onTap = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            onDismiss()
                                        }
                                    )
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "閉じる",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditorialBentoCard(
    index: String,
    kanjiHero: String,
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        animationSpec = ExpressivePhysics.fluidSnappy(),
        label = "bento_card_scale"
    )

    val topStartRadius by animateDpAsState(
        targetValue = if (isPressed) 34.dp else 24.dp,
        animationSpec = ExpressivePhysics.fluidSnappy(),
        label = "bento_top_start"
    )
    val bottomEndRadius by animateDpAsState(
        targetValue = if (isPressed) 12.dp else 24.dp,
        animationSpec = ExpressivePhysics.fluidSnappy(),
        label = "bento_bottom_end"
    )

    val cardShape = RoundedCornerShape(
        topStart = topStartRadius,
        topEnd = 24.dp,
        bottomStart = 24.dp,
        bottomEnd = bottomEndRadius
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onClick()
                    }
                )
            },
        shape = cardShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
        shadowElevation = 0.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Standalone Monumental Kanji Typographic Art (No Circle Enclosure)
                Text(
                    text = kanjiHero,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 4.dp)
                )

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$index • ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = subtitle,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.3).sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Surface(
                modifier = Modifier.size(38.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
