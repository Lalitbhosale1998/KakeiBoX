package com.personal.kakeibox.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.kakeibox.data.entity.SalaryEntry
import com.personal.kakeibox.data.preferences.ThemeSettings
import com.personal.kakeibox.util.DateUtils
import com.personal.kakeibox.util.CurrencyUtils

/**
 * ⚡ ExpressiveEditorialPosterCard
 * 1:1 Neo-Brutalist Editorial Poster Card inspired by FC88 high-energy web design.
 * Features monumental condensed typography, overlapping 3D item cutouts, chalk doodles,
 * strike-through quotes, and vibrant mint menu pills.
 */
@Composable
fun ExpressiveEditorialPosterCard(
    totalSalary: Long,
    thisMonthSalary: Long,
    currentEntry: SalaryEntry?,
    isPrivacyMode: Boolean = false,
    onEdit: () -> Unit,
    themeSettings: ThemeSettings,
    onNavigateTab: ((String) -> Unit)? = null,
    onTogglePrivacyMode: (() -> Unit)? = null,
    onOpenThemeSettings: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val paydayInfo = remember { DateUtils.calculatePaydayProgress() }
    var isMenuExpanded by remember { mutableStateOf(false) }

    val strings = com.personal.kakeibox.ui.theme.getAppStrings(themeSettings.appLanguage)
    val isJapanese = themeSettings.appLanguage == com.personal.kakeibox.data.preferences.AppLanguage.JAPANESE

    // FC88 Full-Bleed Electric Mint Menu Drawer
    ExpressiveEditorialMenuDrawer(
        isOpen = isMenuExpanded,
        onDismiss = { isMenuExpanded = false },
        onNavigateTab = { tab -> onNavigateTab?.invoke(tab) },
        onTogglePrivacyMode = { onTogglePrivacyMode?.invoke() },
        onAddEntry = onEdit,
        onOpenThemeSettings = { onOpenThemeSettings?.invoke() },
        themeSettings = themeSettings
    )

    // Theme-Adaptive Color Palette (Crisp High-Contrast Zine for Light & Dark Mode)
    val isDark = isSystemInDarkTheme()
    val chalkBg = MaterialTheme.colorScheme.surfaceContainerLow
    val mintText = MaterialTheme.colorScheme.onSurface
    val neonMint = MaterialTheme.colorScheme.primary
    val flameRed = MaterialTheme.colorScheme.secondary

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp)),
        color = chalkBg,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── 1. Top Header Bar: 2-Row Spacious Layout ──
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Row 1: Brand Logo + [ ✏️ EDIT ] Pill
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Brand Shield Crest Logo
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Shield,
                                contentDescription = "VITTA Shield Crest",
                                tint = neonMint,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "VITTA",
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Neon Edit Pill Button [ ✏️ EDIT ]
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { onEdit() },
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Month",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = strings.editSalary.uppercase(),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }

                    // Row 2: Status Sub-Tag Metadata
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(neonMint)
                        )
                        Text(
                            text = strings.currentlyTracking,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = neonMint
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // ── Dynamic Month Title Badge ──
                val currentMonthLabel = remember(currentEntry, isJapanese) {
                    val m = currentEntry?.month ?: 1
                    val y = currentEntry?.year ?: 2026
                    val mName = when (m) {
                        1 -> strings.monthJan
                        2 -> strings.monthFeb
                        3 -> strings.monthMar
                        4 -> strings.monthApr
                        5 -> strings.monthMay
                        6 -> strings.monthJun
                        7 -> strings.monthJul
                        8 -> strings.monthAug
                        9 -> strings.monthSep
                        10 -> strings.monthOct
                        11 -> strings.monthNov
                        12 -> strings.monthDec
                        else -> strings.thisMonth
                    }
                    if (isJapanese) "${y}年${mName}の${strings.salary}" else "$mName $y SALARY"
                }

                Text(
                    text = currentMonthLabel,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    color = neonMint,
                    modifier = Modifier.clickable { onEdit() }
                )

                // ── 2. Monumental Hero Display Number (Per-Month Salary) + Overlapping 3D Graphics ──
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Animated Per-Month Salary Amount (Swipes smoothly per month, Tap-to-Edit)
                    val formattedMonthSalary = remember(thisMonthSalary, isPrivacyMode) {
                        CurrencyUtils.formatAmount(thisMonthSalary, themeSettings.currencySymbol, isPrivacyMode, compact = false)
                    }

                    AnimatedContent(
                        targetState = formattedMonthSalary,
                        transitionSpec = {
                            (slideInVertically { height -> height } + fadeIn())
                                .togetherWith(slideOutVertically { height -> -height } + fadeOut())
                        },
                        label = "month_salary_odometer",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onEdit() }
                    ) { targetSalary ->
                        Text(
                            text = targetSalary,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.SansSerif,
                            letterSpacing = (-2.0).sp,
                            color = mintText,
                            lineHeight = 48.sp
                        )
                    }

                    // Overlapping Floating 3D Graphic Cutouts Layered Over Number
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .offset(x = 10.dp, y = (-10).dp),
                        horizontalArrangement = Arrangement.spacedBy((-12).dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Card Badge Cutout 1: Credit Card
                        Surface(
                            modifier = Modifier
                                .size(46.dp)
                                .rotate(-14f),
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF0284C7),
                            border = BorderStroke(1.5.dp, Color.White),
                            shadowElevation = 8.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.CreditCard,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        // Card Badge Cutout 2: Wallet
                        Surface(
                            modifier = Modifier
                                .size(50.dp)
                                .rotate(8f),
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFEA580C),
                            border = BorderStroke(1.5.dp, Color.White),
                            shadowElevation = 10.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        // Card Badge Cutout 3: Gold Yen Coin Badge (五円)
                        Surface(
                            modifier = Modifier
                                .size(44.dp)
                                .rotate(-4f),
                            shape = CircleShape,
                            color = Color(0xFFEAB308),
                            border = BorderStroke(1.5.dp, Color.White),
                            shadowElevation = 7.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "五円",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF451A03)
                                )
                            }
                        }

                        // Card Badge Cutout 4: Flame Fire Badge
                        Surface(
                            modifier = Modifier
                                .size(42.dp)
                                .rotate(10f),
                            shape = CircleShape,
                            color = flameRed,
                            border = BorderStroke(1.5.dp, Color.White),
                            shadowElevation = 6.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }

                // ── Prominent Secondary Editorial Tile (Total Cumulative Salary) ──
                val formattedTotalSalary = remember(totalSalary, isPrivacyMode) {
                    CurrencyUtils.formatAmount(totalSalary, themeSettings.currencySymbol, isPrivacyMode, compact = false)
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    border = BorderStroke(1.5.dp, neonMint.copy(alpha = 0.4f)),
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "💳 ${strings.totalCumulativeEarnings}",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = neonMint
                            )
                            Text(
                                text = formattedTotalSalary,
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-0.5).sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = neonMint.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, neonMint.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "⚡ ${strings.lifetime}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = neonMint
                            )
                        }
                    }
                }

                // ── 3. Editorial Subtitle Stack with Pulsing Payday Flame ──
                val infiniteTransition = rememberInfiniteTransition(label = "flame_pulse")
                val flameScaleState = infiniteTransition.animateFloat(
                    initialValue = 1.0f,
                    targetValue = 1.18f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "flame_scale"
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (isJapanese) "${strings.daysTillNextPayday}\n${paydayInfo.daysRemaining}日" else "${paydayInfo.daysRemaining} DAYS TILL THE\nNEXT PAYDAY",
                        style = MaterialTheme.typography.headlineLarge,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 28.sp,
                        letterSpacing = (-0.5).sp,
                        color = neonMint
                    )
                    Text(
                        text = "🔥",
                        fontSize = 32.sp,
                        modifier = Modifier.graphicsLayer {
                            val scale = flameScaleState.value
                            scaleX = scale
                            scaleY = scale
                        }
                    )
                }

                // ── 4. Handwritten Chalk Annotation Note ──
                Text(
                    text = if (isJapanese) "今月の給与支給前に、これまでの貯蓄を大切に管理しましょう。" else "BUT BEFORE THE LAUNCH OF $currentMonthLabel, LET'S TAKE GOOD CARE OF YOUR SOON-TO-BE 'OLD' SAVINGS,",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 17.sp,
                    color = mintText
                )

                // ── 5. Handwritten Chalk Accent & Japanese Proverb Annotation (四字熟語) ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "継続は力なり (${strings.continuityIsPower})",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = neonMint.copy(alpha = 0.85f)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "↓",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Text(
                                text = strings.scrollToSeeMore,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isJapanese) "ね？！" else "RIGHT?!",
                                style = MaterialTheme.typography.headlineMedium,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                color = neonMint
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "♡♡",
                                fontSize = 14.sp,
                                color = flameRed
                            )
                        }
                        // Underline Doodle
                        Box(
                            modifier = Modifier
                                .width(90.dp)
                                .height(3.dp)
                                .background(neonMint, RoundedCornerShape(2.dp))
                        )
                    }
                }
            }
        }
    }
}
