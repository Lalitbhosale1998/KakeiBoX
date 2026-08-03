package com.personal.kakeibox.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.personal.kakeibox.data.preferences.ThemeSettings

/**
 * ⚡ ExpressiveEditorialMenuDrawer
 * Full-bleed FC88 Neo-Brutalist Navigation & Action Center Drawer.
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

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        // Full-Bleed Electric Mint Background
        val mintBg = MaterialTheme.colorScheme.primary
        val darkText = MaterialTheme.colorScheme.onPrimary
        val pillBg = MaterialTheme.colorScheme.primaryContainer

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mintBg)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // ── 1. Top Bar: Header Title + Close Button ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "↓ WHERE TO?",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        color = darkText
                    )

                    Surface(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .clickable { onDismiss() },
                        color = darkText.copy(alpha = 0.15f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Menu",
                                tint = darkText,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // ── 2. Monumental Navigation Links ──
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    EditorialNavItem(
                        title = "SALARY & SAVINGS",
                        subtitle = "FINANCIAL DASHBOARD",
                        onClick = {
                            onNavigateTab("salary")
                            onDismiss()
                        },
                        textColor = darkText
                    )

                    Divider(color = darkText.copy(alpha = 0.25f), thickness = 1.5.dp)

                    EditorialNavItem(
                        title = "WORKOUT & GYM",
                        subtitle = "FITNESS & HABIT TRACKER",
                        onClick = {
                            onNavigateTab("exercise")
                            onDismiss()
                        },
                        textColor = darkText
                    )

                    Divider(color = darkText.copy(alpha = 0.25f), thickness = 1.5.dp)

                    EditorialNavItem(
                        title = "JAPANESE KOTOBA",
                        subtitle = "VOCABULARY DECK",
                        onClick = {
                            onNavigateTab("kotoba")
                            onDismiss()
                        },
                        textColor = darkText
                    )

                    Divider(color = darkText.copy(alpha = 0.25f), thickness = 1.5.dp)

                    EditorialNavItem(
                        title = "THEME STUDIO",
                        subtitle = "CUSTOMIZE AESTHETICS",
                        onClick = {
                            onOpenThemeSettings()
                            onDismiss()
                        },
                        textColor = darkText
                    )
                }

                // ── 3. Quick Action Pills & Footer ──
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Quick Action Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Privacy Mode Pill
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onTogglePrivacyMode() },
                            color = pillBg,
                            border = BorderStroke(1.dp, darkText.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = if (themeSettings.privacyModeEnabled) Icons.Default.Lock else Icons.Default.LockOpen,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = if (themeSettings.privacyModeEnabled) "PRIVACY: ON" else "PRIVACY: OFF",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        // Add Entry Pill
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    onAddEntry()
                                    onDismiss()
                                },
                            color = pillBg,
                            border = BorderStroke(1.dp, darkText.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "ADD RECORD",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    // Bottom Brand Footer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "KAKEIBOX 2026 • M3 EXPRESSIVE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = darkText.copy(alpha = 0.7f)
                        )

                        Text(
                            text = "🈁 EDITION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = darkText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EditorialNavItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp,
                color = textColor
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = textColor.copy(alpha = 0.7f)
            )
        }

        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = textColor.copy(alpha = 0.15f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
