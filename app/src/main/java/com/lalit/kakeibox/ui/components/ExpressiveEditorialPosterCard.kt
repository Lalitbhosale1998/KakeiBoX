package com.personal.kakeibox.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
    modifier: Modifier = Modifier
) {
    val paydayInfo = remember { DateUtils.calculatePaydayProgress() }
    var isMenuExpanded by remember { mutableStateOf(false) }

    // Theme-Adaptive Color Palette
    val chalkBg = MaterialTheme.colorScheme.surfaceContainerLow
    val neonMint = MaterialTheme.colorScheme.primary
    val mintText = MaterialTheme.colorScheme.onSurface
    val flameRed = MaterialTheme.colorScheme.secondary
    val chalkBorder = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp)),
        color = chalkBg,
        border = BorderStroke(1.5.dp, chalkBorder),
        shadowElevation = 12.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF19201C),
                            chalkBg
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── 1. Top Header Bar: Status Tag + Shield Crest + Neon Mint Pill ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(neonMint)
                        )
                        Text(
                            text = "Currently tracking...",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = neonMint
                        )
                    }

                    // Center Shield Crest Logo
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Shield,
                            contentDescription = "FC88 Shield Crest",
                            tint = neonMint,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "🈁 KAKEIBOX",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Neon Mint Menu Pill Button [ ≡ MENU ]
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { isMenuExpanded = !isMenuExpanded },
                        color = neonMint
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isMenuExpanded) Icons.Default.Close else Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "MENU",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // ── Dynamic Month Title Badge ──
                val currentMonthLabel = remember(currentEntry) {
                    val m = currentEntry?.month ?: 1
                    val y = currentEntry?.year ?: 2026
                    val mName = when (m) {
                        1 -> "JANUARY"
                        2 -> "FEBRUARY"
                        3 -> "MARCH"
                        4 -> "APRIL"
                        5 -> "MAY"
                        6 -> "JUNE"
                        7 -> "JULY"
                        8 -> "AUGUST"
                        9 -> "SEPTEMBER"
                        10 -> "OCTOBER"
                        11 -> "NOVEMBER"
                        12 -> "DECEMBER"
                        else -> "THIS MONTH"
                    }
                    "$mName $y SALARY"
                }

                Text(
                    text = currentMonthLabel,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    color = neonMint
                )

                // ── 2. Monumental Hero Display Number (Per-Month Salary) + Overlapping 3D Graphics ──
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Animated Per-Month Salary Amount (Swipes smoothly per month)
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
                        modifier = Modifier.align(Alignment.TopStart)
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
                                .size(50.dp)
                                .rotate(-12f),
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
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        // Card Badge Cutout 2: Wallet
                        Surface(
                            modifier = Modifier
                                .size(56.dp)
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
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        // Card Badge Cutout 3: Flame Fire Badge
                        Surface(
                            modifier = Modifier
                                .size(46.dp)
                                .rotate(-6f),
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
                                    modifier = Modifier.size(24.dp)
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
                                text = "💳 TOTAL CUMULATIVE EARNINGS",
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
                                text = "⚡ LIFETIME",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = neonMint
                            )
                        }
                    }
                }

                // ── 3. Editorial Subtitle Stack ──
                Text(
                    text = "${paydayInfo.daysRemaining} DAYS TILL THE\nNEXT PAYDAY 🔥",
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 28.sp,
                    letterSpacing = (-0.5).sp,
                    color = neonMint
                )

                // ── 4. Handwritten Chalk Annotation Note ──
                Text(
                    text = "BUT BEFORE THE LAUNCH OF $currentMonthLabel, LET'S TAKE GOOD CARE OF YOUR SOON-TO-BE 'OLD' SAVINGS,",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 17.sp,
                    color = mintText
                )

                // ── 5. Handwritten Chalk Accent "RIGHT?!" with Underline & Hearts ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
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
                            text = "Scroll to see more...",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "RIGHT?!",
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
