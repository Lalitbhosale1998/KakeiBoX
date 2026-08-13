package com.personal.kakeibox.ui.home

import androidx.activity.ComponentActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.data.entity.VocabEntry
import com.personal.kakeibox.data.preferences.ThemeStyle
import com.personal.kakeibox.data.preferences.TopAppBarBackground
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
    val currentSalaryEntry by salaryViewModel.currentEntry.collectAsStateWithLifecycle()
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
            meaning = "Continuous Improvement",
            category = "Vocabulary",
            studyTag = "JLPT N1",
            exampleSentence = "毎日少しずつ改善していく。"
        )
    }

    // Dynamic Slate Navy Atmospheric Canvas (100% Identical to SalaryScreen background)
    val isPrimaryContainer = themeSettings.topAppBarBackground == TopAppBarBackground.PRIMARY_CONTAINER
    val topAppBarContainerColor by animateColorAsState(
        targetValue = when (themeSettings.topAppBarBackground) {
            TopAppBarBackground.SURFACE -> MaterialTheme.colorScheme.primaryContainer
            TopAppBarBackground.PRIMARY_CONTAINER -> MaterialTheme.colorScheme.primaryContainer
        },
        label = "home_top_app_bar_color"
    )

    // Theme-Adaptive Color Palette & Gradient (100% Identical to ExpressiveEditorialPosterCard)
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
            // ── 1. Hero Zine Welcome Banner (32.dp Curves & Dual-Tone Gradient Depth) ──
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp)),
                color = chalkBg,
                border = BorderStroke(1.5.dp, chalkBorder),
                shadowElevation = 12.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(colors = cardGradientColors))
                        .padding(24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                                    contentDescription = "VITTA Crest",
                                    tint = neonMint,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "VITTA COMMAND LAB",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontSize = 15.sp,
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
                                    text = "⚡ ACTIVE OS",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    color = neonMint
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "KONNICHIWA!\nMAKE TODAY LEGENDARY.",
                            style = MaterialTheme.typography.headlineLarge,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 32.sp,
                            letterSpacing = (-0.5).sp,
                            color = mintText
                        )

                        Text(
                            text = "今日を最高の一日にしよう (Continuous self-refinement)",
                            style = MaterialTheme.typography.labelMedium,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = neonMint
                        )
                    }
                }
            }

            // ── 2. Financial Pulse Card (32.dp Poster Card) ──
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onNavigateTab(1) // Jump to Salary
                    },
                color = chalkBg,
                border = BorderStroke(1.5.dp, chalkBorder),
                shadowElevation = 12.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(colors = cardGradientColors))
                        .padding(24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
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
                                    imageVector = Icons.Outlined.AccountBalanceWallet,
                                    contentDescription = "Salary Pulse",
                                    tint = neonMint,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "FINANCIAL PULSE",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.5.sp,
                                    color = neonMint
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Go to Salary",
                                tint = neonMint,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "TOTAL CUMULATIVE EARNINGS",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = neonMint
                            )
                            Text(
                                text = formattedTotalEarnings,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-1.5).sp,
                                color = mintText
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = neonMint.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, neonMint.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "⚡ 14 DAYS TILL NEXT PAYDAY",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = neonMint
                                )
                            }

                            Text(text = "🔥", fontSize = 22.sp)
                        }
                    }
                }
            }

            // ── 3. Japanese Kotoba Word of the Day Card ──
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onNavigateTab(3) // Jump to Kotoba
                    },
                color = chalkBg,
                border = BorderStroke(1.5.dp, chalkBorder),
                shadowElevation = 12.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(colors = cardGradientColors))
                        .padding(24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                                    imageVector = Icons.Outlined.Translate,
                                    contentDescription = "Kotoba Word",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "WORD OF THE DAY",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.5.sp,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = featuredWord.studyTag.ifEmpty { "JLPT N1" },
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = featuredWord.furiganaReading,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textSecondary
                                )
                                Text(
                                    text = featuredWord.kanjiWord,
                                    fontSize = 44.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 2.sp,
                                    color = mintText
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = featuredWord.meaning.uppercase(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = mintText,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // ── 4. Workout & Fitness Streak Card ──
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onNavigateTab(2) // Jump to Exercise
                    },
                color = chalkBg,
                border = BorderStroke(1.5.dp, chalkBorder),
                shadowElevation = 12.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(colors = cardGradientColors))
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Outlined.FitnessCenter,
                                        contentDescription = "Workout Streak",
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "WORKOUT STREAK",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                                Text(
                                    text = "🔥 14 DAY STREAK",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = mintText
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "LOG GYM ➔",
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }

            // ── 5. High-Chroma Quick Launchpad Action Pills ──
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "QUICK LAUNCHPAD",
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
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
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
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
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
                        color = neonMint,
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
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
