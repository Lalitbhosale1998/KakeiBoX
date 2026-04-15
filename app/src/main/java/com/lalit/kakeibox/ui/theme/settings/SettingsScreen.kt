package com.personal.kakeibox.ui.settings

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.R
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import com.personal.kakeibox.data.preferences.AppLanguage
import com.personal.kakeibox.data.preferences.DarkThemePreference
import com.personal.kakeibox.data.preferences.NavBarStyle
import com.personal.kakeibox.ui.components.BentoCard
import com.personal.kakeibox.ui.components.ExpressiveTab
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.ui.input.pointer.pointerInput
import java.util.Collections
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: ThemeViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val themeSettings by viewModel.themeSettings.collectAsStateWithLifecycle()
    val dynamicSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val haptic = LocalHapticFeedback.current

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            Box(modifier = Modifier.background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.surfaceContainerLow
                    )
                )
            )) {
                LargeTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.settings_title),
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Black
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    scrollBehavior = scrollBehavior
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .padding(bottom = 120.dp), // Space for floating bar
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            // ── Section: Appearance Bento Grid ──
            Text(
                text = stringResource(R.string.settings_section_appearance),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp, start = 4.dp)
            )

            // Row 1: Theme selection (Full width with Segmented Buttons)
            BentoCard(
                modifier = Modifier.fillMaxWidth(),
                title = "App Theme",
                description = "Personalize your visual experience with light, dark, or system-adaptive modes.",
                icon = when(themeSettings.darkThemePreference) {
                    DarkThemePreference.DARK -> Icons.Outlined.DarkMode
                    DarkThemePreference.LIGHT -> Icons.Outlined.LightMode
                    else -> Icons.Outlined.AutoMode
                }
            ) {
                val options = listOf(DarkThemePreference.SYSTEM, DarkThemePreference.LIGHT, DarkThemePreference.DARK)
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                ) {
                    options.forEachIndexed { index, preference ->
                        val isSelected = themeSettings.darkThemePreference == preference
                        SegmentedButton(
                            selected = isSelected,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.setDarkThemePreference(preference)
                            },
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                            label = {
                                Text(
                                    preference.name.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }

            // Row 2: Dynamic Color & Daily Reminders (Interactive Bento Pair)
            Row(
                modifier = Modifier.fillMaxWidth().height(160.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Dynamic Color Card
                BentoCard(
                    modifier = Modifier.weight(1f),
                    title = "Dynamic",
                    description = "Match app colors to your wallpaper.",
                    icon = Icons.Outlined.Palette,
                    enabled = dynamicSupported,
                    isActive = themeSettings.useDynamicColor && dynamicSupported,
                    onClick = {
                        if (dynamicSupported) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.setUseDynamicColor(!themeSettings.useDynamicColor)
                        }
                    }
                ) {
                    Switch(
                        checked = themeSettings.useDynamicColor,
                        onCheckedChange = { 
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.setUseDynamicColor(it) 
                        },
                        enabled = dynamicSupported,
                        modifier = Modifier.graphicsLayer {
                            scaleX = 0.8f
                            scaleY = 0.8f
                        }
                    )
                }

                // Daily Reminders Card (State-Aware example)
                BentoCard(
                    modifier = Modifier.weight(1f),
                    title = "Reminders",
                    description = "Daily alerts to log spends.",
                    icon = if (themeSettings.remindersEnabled) Icons.Outlined.NotificationsActive else Icons.Outlined.NotificationsNone,
                    isActive = themeSettings.remindersEnabled,
                    onClick = { 
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.setRemindersEnabled(!themeSettings.remindersEnabled)
                    }
                ) {
                    Text(
                        text = if (themeSettings.remindersEnabled) "Everyday 9PM" else "Disabled",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = if (themeSettings.remindersEnabled) 
                            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Row 2: Nav Style (Wide Bento)
            BentoCard(
                modifier = Modifier.fillMaxWidth(),
                title = "Navigation Layout",
                description = "Choose between a classic full-width bar or a modern floating island.",
                icon = Icons.Outlined.Dock
            ) {
                val currentNavStyle = themeSettings.navBarStyle
                val fullWidthWeight by animateFloatAsState(
                    targetValue = if (currentNavStyle == NavBarStyle.FULL_WIDTH) 1.5f else 1f,
                    animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                    label = "nav_weight_full"
                )
                val floatingWeight by animateFloatAsState(
                    targetValue = if (currentNavStyle == NavBarStyle.FLOATING) 1.5f else 1f,
                    animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                    label = "nav_weight_floating"
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExpressiveTab(
                        text = "Full",
                        isSelected = currentNavStyle == NavBarStyle.FULL_WIDTH,
                        selectedColor = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.weight(fullWidthWeight),
                        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        onClick = { viewModel.setNavBarStyle(NavBarStyle.FULL_WIDTH) }
                    )
                    ExpressiveTab(
                        text = "Floating",
                        isSelected = currentNavStyle == NavBarStyle.FLOATING,
                        selectedColor = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.weight(floatingWeight),
                        selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        onClick = { viewModel.setNavBarStyle(NavBarStyle.FLOATING) }
                    )
                }
            }

            // Row 3: Utility Pair
            Row(
                modifier = Modifier.fillMaxWidth().height(140.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var showBackupDetails by remember { mutableStateOf(false) }

                BentoCard(
                    modifier = Modifier.weight(1f),
                    title = "Backups",
                    description = "Sync your data.",
                    icon = Icons.Outlined.CloudUpload,
                    isActive = showBackupDetails,
                    onClick = { showBackupDetails = !showBackupDetails }
                ) {
                    if (showBackupDetails) {
                        Text(
                            "Last: Today, 10:00 AM",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                
                // App Security (Biometric)
                BentoCard(
                    modifier = Modifier.weight(1f),
                    title = "Security",
                    description = "Biometric lock.",
                    icon = if (themeSettings.biometricEnabled) Icons.Filled.Fingerprint else Icons.Outlined.Fingerprint,
                    isActive = themeSettings.biometricEnabled,
                    onClick = { 
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.setBiometricEnabled(!themeSettings.biometricEnabled)
                    }
                ) {
                   Text(
                        text = if (themeSettings.biometricEnabled) "Enabled" else "Disabled",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = if (themeSettings.biometricEnabled) 
                            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Row 4: Privacy Mode (New)
            BentoCard(
                modifier = Modifier.fillMaxWidth(),
                title = "Privacy Mode",
                description = "Mask currency balances on main screens to keep your finances private in public.",
                icon = if (themeSettings.privacyModeEnabled) Icons.Filled.VisibilityOff else Icons.Outlined.Visibility,
                isActive = themeSettings.privacyModeEnabled,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.setPrivacyModeEnabled(!themeSettings.privacyModeEnabled)
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (themeSettings.privacyModeEnabled) "Balances Masked" else "Balances Visible",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (themeSettings.privacyModeEnabled) 
                            MaterialTheme.colorScheme.onPrimaryContainer 
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Switch(
                        checked = themeSettings.privacyModeEnabled,
                        onCheckedChange = { 
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.setPrivacyModeEnabled(it) 
                        },
                        modifier = Modifier.scale(0.8f)
                    )
                }
            }

            // ── Section: Localization ──
            Text(
                text = "Locale & Currency",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp, start = 4.dp)
            )

            // Language Selection
            BentoCard(
                modifier = Modifier.fillMaxWidth(),
                title = "Language",
                description = "Choose your preferred language.",
                icon = Icons.Outlined.Language
            ) {
                val languages = AppLanguage.values()
                val currentLanguage = themeSettings.appLanguage
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    languages.forEach { language ->
                        val isSelected = currentLanguage == language
                        
                        val segmentWeight by animateFloatAsState(
                            targetValue = if (isSelected) 1.5f else 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy, 
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "lang_weight_${language.name}"
                        )

                        ExpressiveTab(
                            text = language.name.lowercase().replaceFirstChar { it.uppercase() },
                            isSelected = isSelected,
                            selectedColor = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.weight(segmentWeight),
                            selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            onClick = { 
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.setAppLanguage(language) 
                            }
                        )
                    }
                }
            }

            // Currency Section
            BentoCard(
                modifier = Modifier.fillMaxWidth(),
                title = "Currency Symbol",
                description = "Set your preferred currency symbol for all reports and inputs.",
                icon = Icons.Outlined.Payments
            ) {
                val currencies = listOf("₹", "¥", "$", "€")
                val currentSymbol = themeSettings.currencySymbol
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    currencies.forEach { symbol ->
                        val isSelected = currentSymbol == symbol
                        
                        // Adaptive weight animation similar to Category Tabs
                        val segmentWeight by animateFloatAsState(
                            targetValue = if (isSelected) 1.5f else 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy, 
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "currency_weight_$symbol"
                        )

                        ExpressiveTab(
                            text = symbol,
                            isSelected = isSelected,
                            selectedColor = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.weight(segmentWeight),
                            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            onClick = { 
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.setCurrencySymbol(symbol) 
                            }
                        )
                    }
                }
            }

            // ── Section: Navigation Customization ──
            Text(
                text = "Navigation Customization",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp, start = 4.dp)
            )

            BentoCard(
                modifier = Modifier.fillMaxWidth(),
                title = "Tab Order",
                description = "Long press and drag to reorder navigation tabs.",
                icon = Icons.Outlined.Reorder
            ) {
                val tabOrder = themeSettings.tabOrder
                var draggingItemIndex by remember { mutableStateOf<Int?>(null) }
                var deltaY by remember { mutableStateOf(0f) }
                
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tabOrder.forEachIndexed { index, route ->
                        val isDragging = draggingItemIndex == index
                        val elevation by animateDpAsState(if (isDragging) 8.dp else 0.dp)
                        val scale by animateFloatAsState(if (isDragging) 1.05f else 1f)

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    translationY = if (isDragging) deltaY else 0f
                                    scaleX = scale
                                    scaleY = scale
                                }
                                .pointerInput(Unit) {
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = { 
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            draggingItemIndex = index 
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            deltaY += dragAmount.y
                                            
                                            val newIndex = (index + (deltaY / 60).toInt()).coerceIn(0, tabOrder.size - 1)
                                            if (newIndex != index && draggingItemIndex != null) {
                                                val newList = tabOrder.toMutableList()
                                                Collections.swap(newList, index, newIndex)
                                                viewModel.setTabOrder(newList)
                                                draggingItemIndex = newIndex
                                                deltaY = 0f
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            }
                                        },
                                        onDragEnd = { 
                                            draggingItemIndex = null
                                            deltaY = 0f
                                        },
                                        onDragCancel = { 
                                            draggingItemIndex = null
                                            deltaY = 0f
                                        }
                                    )
                                },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isDragging) MaterialTheme.colorScheme.secondaryContainer 
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            tonalElevation = elevation
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = when(route) {
                                        "salary" -> Icons.Outlined.Wallet
                                        "spend" -> Icons.Outlined.ShoppingCart
                                        "commute" -> Icons.Outlined.DirectionsBus
                                        else -> Icons.Outlined.Settings
                                    },
                                    contentDescription = null,
                                    tint = if (isDragging) MaterialTheme.colorScheme.onSecondaryContainer 
                                           else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = route.replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isDragging) MaterialTheme.colorScheme.onSecondaryContainer 
                                           else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = Icons.Outlined.DragHandle,
                                    contentDescription = "Drag to reorder",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

