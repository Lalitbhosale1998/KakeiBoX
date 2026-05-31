package com.personal.kakeibox.ui.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AutoMode
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Dock
import androidx.compose.material.icons.outlined.DragHandle
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Reorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.R
import com.personal.kakeibox.data.preferences.AppLanguage
import com.personal.kakeibox.data.preferences.DarkThemePreference
import com.personal.kakeibox.data.preferences.NavBarStyle
import com.personal.kakeibox.data.preferences.TopAppBarBackground
import com.personal.kakeibox.data.preferences.ThemeStyle
import com.personal.kakeibox.data.preferences.AppFont
import com.personal.kakeibox.ui.theme.LocalThemeStyle
import com.personal.kakeibox.ui.theme.terminalScanlines
import com.personal.kakeibox.ui.theme.NunitoFontFamily
import com.personal.kakeibox.ui.theme.OutfitFontFamily
import com.personal.kakeibox.ui.theme.PlayfairFontFamily
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.BorderStroke
import com.personal.kakeibox.data.entity.BirthdayEntry
import com.personal.kakeibox.ui.components.BentoCard
import com.personal.kakeibox.ui.components.ExpressiveTab
import com.personal.kakeibox.ui.components.ExpressiveOutlinedTextField
import com.personal.kakeibox.ui.components.RetroProgressIndicator
import com.personal.kakeibox.ui.components.ExpressiveButton
import com.personal.kakeibox.ui.settings.ThemeViewModel
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Collections

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: ThemeViewModel = hiltViewModel()
) {
    val themeSettings by viewModel.themeSettings.collectAsStateWithLifecycle()
    val birthdays by viewModel.birthdays.collectAsStateWithLifecycle()
    val showBirthdaySheet by viewModel.showBirthdaySheet.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var activeSection by remember { mutableStateOf<String?>(null) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val haptic = LocalHapticFeedback.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val topAppBarContainerColor by animateColorAsState(
        targetValue = when (themeSettings.topAppBarBackground) {
            TopAppBarBackground.SURFACE -> MaterialTheme.colorScheme.surface
            TopAppBarBackground.PRIMARY_CONTAINER -> MaterialTheme.colorScheme.primaryContainer
        },
        label = "top_app_bar_container_color"
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = topAppBarContainerColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_title),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topAppBarContainerColor,
                    scrolledContainerColor = topAppBarContainerColor,
                ),
                scrollBehavior = scrollBehavior
            )
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
            val context = LocalContext.current

            // ── Expressive Search Bar ──
            // ── Expressive Search Bar ──
            ExpressiveOutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { 
                    Text(
                        "Search settings...",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    ) 
                },
                leadingIcon = { 
                    Icon(
                        Icons.Rounded.Search, 
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                trailingIcon = if (searchQuery.isNotEmpty()) {
                    {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Rounded.Close, contentDescription = "Clear")
                        }
                    }
                } else null,
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )

            // Helper for filtering
            fun shouldShow(title: String, description: String = "", keywords: List<String> = emptyList()): Boolean {
                if (searchQuery.isBlank()) return true
                val terms = (keywords + title + description)
                return terms.any { it.contains(searchQuery, ignoreCase = true) }
            }

            // ── Section Declarations ──
            val showAppearance = shouldShow("App Theme", "Personalize your visual experience", listOf("dark", "light", "mode", "appearance")) ||
                               shouldShow("Dynamic", "Match app colors to your wallpaper", listOf("wallpaper", "appearance", "color")) ||
                               shouldShow("Top App Bar Background", "navigation bar", listOf("appearance", "header")) ||
                               shouldShow("Navigation Layout", "floating island", listOf("navigation", "layout", "bar", "appearance"))

            val showPersonalization = shouldShow("Tab Order", "Long press and drag to reorder", listOf("navigation", "reorder"))

            val showSecurity = shouldShow("Security", "Biometric lock", listOf("fingerprint", "privacy")) ||
                             shouldShow("Privacy Mode", "Mask sensitive financial amounts", listOf("mask", "hide")) ||
                             shouldShow("Data Health", "backed up", listOf("sync", "cloud")) ||
                             shouldShow("Export Data", "CSV file", listOf("download", "backup"))

            val showRegional = shouldShow("Language", "Choose your preferred language", listOf("locale", "regional")) ||
                              shouldShow("Currency", "currency symbol", listOf("money", "symbol"))

            val showAbout = shouldShow("About", keywords = listOf("version", "github", "developer")) ||
                           shouldShow("Version", keywords = listOf("app"))

            @Composable
            fun VisualStyleCard(expanded: Boolean) {
                SettingsCategoryCard(
                    title = "Visual Style",
                    description = "Theme mode, dynamic colors, top bar style, and navigation layout preferences",
                    icon = Icons.Outlined.Palette,
                    isExpanded = expanded,
                    onClick = {
                        activeSection = if (activeSection == "visual") null else "visual"
                    }
                ) {
                    // Interface Aesthetic
                    if (shouldShow("Interface Aesthetic", keywords = listOf("style", "visual", "retro", "expressive", "aesthetic"))) {
                        BentoCard(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Interface Aesthetic",
                            description = "Switch the overall visual identity of the app between modern Material 3 and retro space terminal cockpit styling.",
                            icon = Icons.Outlined.Palette
                        ) {
                            val currentStyle = themeSettings.themeStyle
                            val options = ThemeStyle.entries
                            
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                options.forEach { option ->
                                    val isSelected = currentStyle == option
                                    val weight by animateFloatAsState(
                                        targetValue = if (isSelected) 1.2f else 1f,
                                        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
                                    )

                                    ExpressiveTab(
                                        text = when(option) {
                                            ThemeStyle.M3_EXPRESSIVE -> "M3 Expressive"
                                            ThemeStyle.RETRO_SPACE -> "Retro Space"
                                        },
                                        isSelected = isSelected,
                                        selectedColor = when(option) {
                                            ThemeStyle.M3_EXPRESSIVE -> MaterialTheme.colorScheme.primary
                                            ThemeStyle.RETRO_SPACE -> Color(0xFFFF7E6B) // Electric Coral
                                        },
                                        modifier = Modifier.weight(weight.coerceAtLeast(0.001f)),
                                        selectedTextColor = when(option) {
                                            ThemeStyle.M3_EXPRESSIVE -> MaterialTheme.colorScheme.onPrimary
                                            ThemeStyle.RETRO_SPACE -> Color(0xFF0A0D1A) // Space navy text
                                        },
                                        icon = if (option == ThemeStyle.M3_EXPRESSIVE) Icons.Outlined.Palette else Icons.Outlined.Public,
                                        shapeType = if (option == ThemeStyle.M3_EXPRESSIVE) "pill" else "clamshell",
                                        onClick = { 
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            viewModel.setThemeStyle(option) 
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // App Font Face
                    if (shouldShow("App Font Face", keywords = listOf("font", "typeface", "text", "style"))) {
                        BentoCard(
                            modifier = Modifier.fillMaxWidth(),
                            title = "App Font Face",
                            description = "Change the globally applied typography typeface.",
                            icon = Icons.Outlined.Code
                        ) {
                            val currentFont = themeSettings.appFont
                            val options = AppFont.entries
                            
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                options.chunked(2).forEach { rowOptions ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        rowOptions.forEach { option ->
                                            val isSelected = currentFont == option
                                            val displayName = when(option) {
                                                AppFont.NUNITO -> "Nunito"
                                                AppFont.MONOSPACE -> "Monospace"
                                                AppFont.SYSTEM_SANS -> "System Sans"
                                                AppFont.OUTFIT -> "Outfit"
                                                AppFont.PLAYFAIR -> "Playfair"
                                            }
                                            val optionFontFamily = when(option) {
                                                AppFont.NUNITO -> NunitoFontFamily
                                                AppFont.MONOSPACE -> FontFamily.Monospace
                                                AppFont.SYSTEM_SANS -> FontFamily.SansSerif
                                                AppFont.OUTFIT -> OutfitFontFamily
                                                AppFont.PLAYFAIR -> PlayfairFontFamily
                                            }
                                            
                                            FontOptionCard(
                                                modifier = Modifier.weight(1f),
                                                name = displayName,
                                                isSelected = isSelected,
                                                fontFamily = optionFontFamily,
                                                onClick = { 
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    viewModel.setAppFont(option) 
                                                }
                                            )
                                        }
                                        if (rowOptions.size == 1) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // App Theme
                    if (shouldShow("App Theme", keywords = listOf("dark", "light", "mode"))) {
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
                                            activeContainerColor = MaterialTheme.colorScheme.primary,
                                            activeContentColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Dynamic Color
                    if (shouldShow("Dynamic", keywords = listOf("wallpaper", "color"))) {
                        BentoCard(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Dynamic Color",
                            description = "Match app colors to your system wallpaper dynamically.",
                            icon = Icons.Outlined.Palette,
                            isActive = themeSettings.useDynamicColor,
                            activeContainerColor = MaterialTheme.colorScheme.primary,
                            activeContentColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.setUseDynamicColor(!themeSettings.useDynamicColor)
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (themeSettings.useDynamicColor) "Active" else "Inactive",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (themeSettings.useDynamicColor) 
                                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                    else 
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Switch(
                                    checked = themeSettings.useDynamicColor,
                                    onCheckedChange = { 
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.setUseDynamicColor(it) 
                                    },
                                    modifier = Modifier.graphicsLayer { scaleX = 0.8f; scaleY = 0.8f }
                                )
                            }
                        }
                    }

                    // Top Bar
                    if (shouldShow("Top App Bar Background", keywords = listOf("header", "navigation"))) {
                        BentoCard(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Top App Bar Background",
                            description = "Choose the background color for the top navigation bar.",
                            icon = Icons.Outlined.Palette
                        ) {
                            val currentBackground = themeSettings.topAppBarBackground
                            val options = TopAppBarBackground.entries
                            
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                options.forEach { option ->
                                    val isSelected = currentBackground == option
                                    val weight by animateFloatAsState(
                                        targetValue = if (isSelected) 1.2f else 1f,
                                        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
                                    )

                                    ExpressiveTab(
                                        text = option.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
                                        isSelected = isSelected,
                                        selectedColor = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.weight(weight.coerceAtLeast(0.001f)),
                                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                                        icon = if (option == TopAppBarBackground.SURFACE) Icons.Outlined.Dock else Icons.Outlined.Palette,
                                        shapeType = if (option == TopAppBarBackground.SURFACE) "clamshell" else "pill",
                                        onClick = { viewModel.setTopAppBarBackground(option) }
                                    )
                                }
                            }
                        }
                    }

                    // Nav Style
                    if (shouldShow("Navigation Layout", keywords = listOf("floating", "bar"))) {
                        BentoCard(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Navigation Layout",
                            description = "Choose between a classic full-width bar or a modern floating island.",
                            icon = Icons.Outlined.Dock
                        ) {
                            val currentNavStyle = themeSettings.navBarStyle
                            val fullWidthWeight by animateFloatAsState(
                                targetValue = if (currentNavStyle == NavBarStyle.FULL_WIDTH) 1.5f else 0.8f,
                                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                                label = "nav_full_weight"
                            )
                            val floatingWeight by animateFloatAsState(
                                targetValue = if (currentNavStyle == NavBarStyle.FLOATING) 1.5f else 0.8f,
                                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                                label = "nav_floating_weight"
                            )
                            val splitWeight by animateFloatAsState(
                                targetValue = if (currentNavStyle == NavBarStyle.SPLIT) 1.5f else 0.8f,
                                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                                label = "nav_split_weight"
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ExpressiveTab(
                                    text = "Full",
                                    isSelected = currentNavStyle == NavBarStyle.FULL_WIDTH,
                                    selectedColor = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(fullWidthWeight.coerceAtLeast(0.001f)),
                                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                                    icon = Icons.Outlined.Dock,
                                    shapeType = "arch",
                                    onClick = { viewModel.setNavBarStyle(NavBarStyle.FULL_WIDTH) }
                                )
                                ExpressiveTab(
                                    text = "Floating",
                                    isSelected = currentNavStyle == NavBarStyle.FLOATING,
                                    selectedColor = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.weight(floatingWeight.coerceAtLeast(0.001f)),
                                    selectedTextColor = MaterialTheme.colorScheme.onSecondary,
                                    icon = Icons.Outlined.Wallet,
                                    shapeType = "pill",
                                    onClick = { viewModel.setNavBarStyle(NavBarStyle.FLOATING) }
                                )
                                ExpressiveTab(
                                    text = "Split-Dock",
                                    isSelected = currentNavStyle == NavBarStyle.SPLIT,
                                    selectedColor = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.weight(splitWeight.coerceAtLeast(0.001f)),
                                    selectedTextColor = MaterialTheme.colorScheme.onTertiary,
                                    icon = Icons.Outlined.ShoppingCart,
                                    shapeType = "clamshell",
                                    onClick = { viewModel.setNavBarStyle(NavBarStyle.SPLIT) }
                                )
                            }
                        }
                    }
                }
            }

            @Composable
            fun PersonalizationCard(expanded: Boolean) {
                SettingsCategoryCard(
                    title = "Personalization",
                    description = "Tab layout ordering and navigation customization options",
                    icon = Icons.Outlined.Reorder,
                    isExpanded = expanded,
                    onClick = {
                        activeSection = if (activeSection == "personalization") null else "personalization"
                    }
                ) {
                    if (shouldShow("Tab Order", keywords = listOf("navigation", "reorder"))) {
                        BentoCard(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Tab Order",
                            description = "Long press and drag to reorder navigation tabs.",
                            icon = Icons.Outlined.Reorder
                        ) {
                            val tabOrder = themeSettings.tabOrder
                            var draggingItemIndex by remember { mutableStateOf<Int?>(null) }
                            var deltaY by remember { mutableFloatStateOf(0f) }
                            
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

            @Composable
            fun SecurityCard(expanded: Boolean) {
                SettingsCategoryCard(
                    title = "Security & Privacy",
                    description = "Biometrics lock, screen content masking, and CSV data exports",
                    icon = Icons.Outlined.Fingerprint,
                    isExpanded = expanded,
                    onClick = {
                        activeSection = if (activeSection == "security") null else "security"
                    }
                ) {
                    // Privacy Mode Bento Card
                    if (shouldShow("Privacy Mode", keywords = listOf("mask", "hide", "sensitive"))) {
                        BentoCard(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Privacy Mode",
                            description = "Mask sensitive financial amounts across all screens with '••••'.",
                            icon = if (themeSettings.privacyModeEnabled) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            isActive = themeSettings.privacyModeEnabled,
                            activeContainerColor = MaterialTheme.colorScheme.primary,
                            activeContentColor = MaterialTheme.colorScheme.onPrimary,
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
                                    text = if (themeSettings.privacyModeEnabled) "Active" else "Inactive",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (themeSettings.privacyModeEnabled) 
                                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                    else 
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Switch(
                                    checked = themeSettings.privacyModeEnabled,
                                    onCheckedChange = { 
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.setPrivacyModeEnabled(it) 
                                    },
                                    modifier = Modifier.graphicsLayer { scaleX = 0.7f; scaleY = 0.7f }
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().height(160.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // App Security (Biometric)
                        if (shouldShow("Security", keywords = listOf("fingerprint", "lock"))) {
                            BentoCard(
                                modifier = Modifier.weight(1f),
                                title = "Security",
                                description = "Biometric lock.",
                                icon = if (themeSettings.biometricEnabled) Icons.Filled.Fingerprint else Icons.Outlined.Fingerprint,
                                isActive = themeSettings.biometricEnabled,
                                activeContainerColor = MaterialTheme.colorScheme.primary,
                                activeContentColor = MaterialTheme.colorScheme.onPrimary,
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

                        // Data Health Bento Card
                        if (shouldShow("Data Health", keywords = listOf("sync", "cloud", "backup"))) {
                            var isSyncing by remember { mutableStateOf(false) }
                            val syncScale by animateFloatAsState(
                                targetValue = if (isSyncing) 0.95f else 1f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                            )

                            LaunchedEffect(isSyncing) {
                                if (isSyncing) {
                                    delay(2000)
                                    isSyncing = false
                                }
                            }

                            BentoCard(
                                modifier = Modifier.weight(1f).graphicsLayer(scaleX = syncScale, scaleY = syncScale),
                                title = "Data Health",
                                description = if (isSyncing) "Syncing..." else "Data is backed up.",
                                icon = if (isSyncing) Icons.Outlined.Sync else Icons.Outlined.CloudUpload,
                                isActive = isSyncing,
                                activeContainerColor = MaterialTheme.colorScheme.primary,
                                activeContentColor = MaterialTheme.colorScheme.onPrimary,
                                onClick = { 
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isSyncing = true 
                                }
                            ) {
                                Column(
                                    modifier = Modifier.padding(top = 4.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = if (isSyncing) "Updating..." else "Secure",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSyncing) MaterialTheme.colorScheme.primary else Color(0xFF4CAF50)
                                    )
                                    
                                    if (isSyncing) {
                                        RetroProgressIndicator(
                                            modifier = Modifier.fillMaxWidth().height(8.dp),
                                            color = MaterialTheme.colorScheme.primary,
                                            trackColor = MaterialTheme.colorScheme.primaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Export Data Bento Card
                    if (shouldShow("Export Data", keywords = listOf("download", "csv"))) {
                        BentoCard(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Export Data",
                            description = "Download your spending and salary history as a CSV file.",
                            icon = Icons.Outlined.FileDownload,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.exportToCsv { csvData ->
                                    if (csvData != null) {
                                        val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                            type = "text/csv"
                                            putExtra(android.content.Intent.EXTRA_SUBJECT, "KakeiboX Export")
                                            putExtra(android.content.Intent.EXTRA_TEXT, csvData)
                                        }
                                        context.startActivity(android.content.Intent.createChooser(intent, "Export Data"))
                                    }
                                }
                            }
                        )
                    }
                }
            }

            @Composable
            fun RegionalCard(expanded: Boolean) {
                SettingsCategoryCard(
                    title = "Regional & Locale",
                    description = "Preferred application language and reporting currency symbol configuration",
                    icon = Icons.Outlined.Language,
                    isExpanded = expanded,
                    onClick = {
                        activeSection = if (activeSection == "regional") null else "regional"
                    }
                ) {
                    // Language Selection
                    if (shouldShow("Language", keywords = listOf("locale", "regional"))) {
                        BentoCard(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Language",
                            description = "Choose your preferred language.",
                            icon = Icons.Outlined.Language
                        ) {
                            val languages = AppLanguage.entries
                            val currentLanguage = themeSettings.appLanguage
                            
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                languages.forEach { language ->
                                    val isSelected = currentLanguage == language
                                    val segmentWeight by animateFloatAsState(
                                        targetValue = if (isSelected) 1.5f else 1f,
                                        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
                                    )

                                    ExpressiveTab(
                                        text = language.name.lowercase().replaceFirstChar { it.uppercase() },
                                        isSelected = isSelected,
                                        selectedColor = MaterialTheme.colorScheme.secondaryContainer,
                                        modifier = Modifier.weight(segmentWeight.coerceAtLeast(0.001f)),
                                        selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                        icon = if (language == AppLanguage.ENGLISH) Icons.Outlined.Language else Icons.Outlined.Public,
                                        onClick = { 
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            viewModel.setAppLanguage(language) 
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Currency Section
                    if (shouldShow("Currency Symbol", keywords = listOf("money", "symbol"))) {
                        BentoCard(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Currency Symbol",
                            description = "Set your preferred currency symbol for all reports.",
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
                                    val segmentWeight by animateFloatAsState(
                                        targetValue = if (isSelected) 1.5f else 1f,
                                        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
                                    )

                                    ExpressiveTab(
                                        text = symbol,
                                        isSelected = isSelected,
                                        selectedColor = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.weight(segmentWeight.coerceAtLeast(0.001f)),
                                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                                        icon = when (symbol) {
                                            "₹" -> Icons.Outlined.Payments
                                            "¥" -> Icons.Outlined.Wallet
                                            "$" -> Icons.Outlined.Payments
                                            else -> Icons.Outlined.ShoppingCart
                                        },
                                        shapeType = when (symbol) {
                                            "₹" -> "slanted"
                                            "¥" -> "clamshell"
                                            "$" -> "arch"
                                            else -> "pill"
                                        },
                                        onClick = { 
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            viewModel.setCurrencySymbol(symbol) 
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            @Composable
            fun AboutCard(expanded: Boolean) {
                SettingsCategoryCard(
                    title = stringResource(R.string.settings_section_about),
                    description = "Version metadata, developer profile, and GitHub repository source code",
                    icon = Icons.Outlined.Info,
                    isExpanded = expanded,
                    onClick = {
                        activeSection = if (activeSection == "about") null else "about"
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(140.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (shouldShow("Version")) {
                            BentoCard(
                                modifier = Modifier.weight(1f),
                                title = stringResource(R.string.about_version),
                                description = stringResource(R.string.about_version_desc),
                                icon = Icons.Outlined.Info,
                                isActive = true,
                                activeContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                activeContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }

                        if (shouldShow("Developer")) {
                            BentoCard(
                                modifier = Modifier.weight(1f),
                                title = stringResource(R.string.about_developer),
                                description = stringResource(R.string.about_developer_desc),
                                icon = Icons.Outlined.Code,
                                onClick = { /* Open dev profile */ }
                            )
                        }
                    }

                    if (shouldShow("GitHub")) {
                        BentoCard(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(R.string.about_github),
                            description = stringResource(R.string.about_github_desc),
                            icon = Icons.Outlined.Public,
                            onClick = {
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://github.com/Lalitbhosale1998/KakeiBoX"))
                                context.startActivity(intent)
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.about_description),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // ── Bento Grid to Stacked Column continuous morph transition ──
            val isSearchActive = searchQuery.isNotEmpty()

            if (isSearchActive) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(spring(dampingRatio = 0.55f, stiffness = 300f)),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (showAppearance) {
                        VisualStyleCard(expanded = true)
                    }
                    if (showPersonalization) {
                        PersonalizationCard(expanded = true)
                    }
                    if (showSecurity) {
                        SecurityCard(expanded = true)
                    }
                    if (showRegional) {
                        RegionalCard(expanded = true)
                    }
                    if (showAbout) {
                        AboutCard(expanded = true)
                    }
                }
            } else {
                BentoGridToColumnLayout(
                    isGridMode = activeSection == null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    VisualStyleCard(expanded = activeSection == "visual")
                    PersonalizationCard(expanded = activeSection == "personalization")
                    SecurityCard(expanded = activeSection == "security")
                    RegionalCard(expanded = activeSection == "regional")
                    AboutCard(expanded = activeSection == "about")
                }
            }
        }
    }


}

@Composable
fun SettingsCategoryCard(
    title: String,
    description: String,
    icon: ImageVector,
    isExpanded: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val containerColor by animateColorAsState(
        targetValue = if (isExpanded) 
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f) 
        else 
            MaterialTheme.colorScheme.surfaceContainerLow,
        label = "category_container_color"
    )
    val contentColor = MaterialTheme.colorScheme.onSurface

    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.RETRO_SPACE
    val cardShape = if (isSpaceTerminal) RoundedCornerShape(12.dp) else RoundedCornerShape(28.dp)
    val cardBorder = if (isSpaceTerminal) {
        BorderStroke(1.5.dp, if (isExpanded) Color(0xFFFF7E6B) else Color(0xFF46C2B4).copy(alpha = 0.4f))
    } else {
        null
    }
    val iconShape = if (isSpaceTerminal) RoundedCornerShape(6.dp) else RoundedCornerShape(16.dp)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = 0.55f,
                    stiffness = 300f
                )
            )
            .terminalScanlines(),
        color = containerColor,
        contentColor = contentColor,
        shape = cardShape,
        border = cardBorder
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = iconShape,
                        color = if (isExpanded) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isExpanded) 
                                    MaterialTheme.colorScheme.onPrimary 
                                else 
                                    MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (!isExpanded) {
                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                
                val rotation by animateFloatAsState(
                    targetValue = if (isExpanded) 180f else 0f,
                    animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f),
                    label = "arrow_rotation"
                )
                
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(28.dp)
                        .graphicsLayer { rotationZ = rotation }
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f)) +
                        expandVertically(animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f)),
                exit = fadeOut(animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f)) +
                        shrinkVertically(animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    content()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayManagementContent(
    birthdays: List<BirthdayEntry>,
    onAdd: (String, LocalDate) -> Unit,
    onDelete: (BirthdayEntry) -> Unit,
    onToggleEnabled: (BirthdayEntry) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    var animateIn by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { animateIn = true }

    val slideY by animateDpAsState(
        targetValue = if (animateIn) 0.dp else 100.dp,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow),
        label = "sheet_slide_in"
    )
    val sheetAlpha by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0f,
        animationSpec = tween(300),
        label = "sheet_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(translationY = slideY.value, alpha = sheetAlpha)
            .padding(horizontal = 24.dp)
            .padding(bottom = 48.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Birthdays Hub",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Never miss a celebration",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        ),
                        shape = CircleShape
                    )
            ) {
                Icon(Icons.Outlined.Add, contentDescription = "Add Birthday", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (birthdays.isEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(32.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(MaterialTheme.colorScheme.surface, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Cake,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No birthdays saved",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Tap the '+' to start adding",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                birthdays.forEach { birthday ->
                    BirthdayRow(
                        birthday = birthday,
                        onDelete = { onDelete(birthday) },
                        onToggle = { onToggleEnabled(birthday) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        ModalBottomSheet(
            onDismissRequest = { showAddDialog = false },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 8.dp,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .width(36.dp)
                        .height(4.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant, CircleShape)
                )
            }
        ) {
            var animateInInner by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { animateInInner = true }

            val slideYInner by animateDpAsState(
                targetValue = if (animateInInner) 0.dp else 100.dp,
                animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow),
                label = "sheet_slide_in_inner"
            )
            val sheetAlphaInner by animateFloatAsState(
                targetValue = if (animateInInner) 1f else 0f,
                animationSpec = tween(300),
                label = "sheet_alpha_inner"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(translationY = slideYInner.value, alpha = sheetAlphaInner)
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 40.dp, top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Expressive Header
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.tertiaryContainer
                                )
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .graphicsLayer { rotationZ = -10f },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Cake,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Add New Birthday",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Make sure you never miss a celebration!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Input Section with Custom Styling
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ExpressiveOutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        placeholder = { Text("Who's birthday is it?") },
                        label = { Text("Person's Name") },
                        singleLine = true,
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Surface(
                        onClick = { showDatePicker = !showDatePicker },
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, 
                            if (showDatePicker) MaterialTheme.colorScheme.primary 
                            else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Birthday Date",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = selectedDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Icon(
                                Icons.Outlined.Event,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Inline Date Picker (Animated Visibility with Expressive Wheel Selection)
                    AnimatedVisibility(
                        visible = showDatePicker,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            com.personal.kakeibox.ui.components.ExpressiveDatePicker(
                                selectedDate = selectedDate,
                                onDateChange = { selectedDate = it },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            TextButton(
                                onClick = { showDatePicker = false },
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Text("Done", fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                ExpressiveButton(
                    onClick = {
                        if (newName.isNotBlank()) {
                            onAdd(newName, selectedDate)
                            newName = ""
                            selectedDate = LocalDate.now()
                            showAddDialog = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = newName.isNotBlank()
                ) {
                    Text(
                        "Save Birthday", 
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun BirthdayRow(
    birthday: BirthdayEntry,
    onDelete: () -> Unit,
    onToggle: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val backgroundColor by animateColorAsState(
        targetValue = if (birthday.isEnabled) 
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        else 
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        label = "birthday_row_bg"
    )

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        color = if (birthday.isEnabled) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Cake,
                    contentDescription = null,
                    tint = if (birthday.isEnabled) MaterialTheme.colorScheme.primary 
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = birthday.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (birthday.isEnabled) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = birthday.date.format(DateTimeFormatter.ofPattern("MMMM dd")),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (birthday.isEnabled) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium
                )
            }
            
            Switch(
                checked = birthday.isEnabled,
                onCheckedChange = { 
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onToggle() 
                },
                modifier = Modifier.graphicsLayer { scaleX = 0.75f; scaleY = 0.75f }
            )
            
            Spacer(modifier = Modifier.width(4.dp))
            
            IconButton(onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onDelete()
            }) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun BentoGridToColumnLayout(
    isGridMode: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val transitionProgress by animateFloatAsState(
        targetValue = if (isGridMode) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f),
        label = "grid_to_column_progress"
    )

    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val spacing = 16.dp.roundToPx()
        val width = constraints.maxWidth
        val gridWidth1 = (width - spacing) / 2

        val placeables = measurables.mapIndexed { index, measurable ->
            val targetWidth = when (index) {
                2 -> width
                else -> {
                    val gridW = gridWidth1
                    val colW = width
                    (colW + (gridW - colW) * transitionProgress).toInt()
                }
            }
            measurable.measure(constraints.copy(minWidth = targetWidth, maxWidth = targetWidth))
        }

        val h0 = placeables.getOrNull(0)?.height ?: 0
        val h1 = placeables.getOrNull(1)?.height ?: 0
        val h2 = placeables.getOrNull(2)?.height ?: 0
        val h3 = placeables.getOrNull(3)?.height ?: 0
        val h4 = placeables.getOrNull(4)?.height ?: 0

        // Grid coordinates
        val y0_grid = 0
        val y1_grid = 0
        val y2_grid = maxOf(h0, h1) + spacing
        val y3_grid = y2_grid + h2 + spacing
        val y4_grid = y2_grid + h2 + spacing

        // Column coordinates
        val y0_col = 0
        val y1_col = h0 + spacing
        val y2_col = y1_col + h1 + spacing
        val y3_col = y2_col + h2 + spacing
        val y4_col = y3_col + h3 + spacing

        // Interpolated y coordinates
        val y0 = (y0_col + (y0_grid - y0_col) * transitionProgress).toInt()
        val y1 = (y1_col + (y1_grid - y1_col) * transitionProgress).toInt()
        val y2 = (y2_col + (y2_grid - y2_col) * transitionProgress).toInt()
        val y3 = (y3_col + (y3_grid - y3_col) * transitionProgress).toInt()
        val y4 = (y4_col + (y4_grid - y4_col) * transitionProgress).toInt()

        // Interpolated x coordinates
        val x0_grid = 0
        val x1_grid = gridWidth1 + spacing
        val x2_grid = 0
        val x3_grid = 0
        val x4_grid = gridWidth1 + spacing

        val x0 = (x0_grid * transitionProgress).toInt()
        val x1 = (x1_grid * transitionProgress).toInt()
        val x2 = 0
        val x3 = 0
        val x4 = (x4_grid * transitionProgress).toInt()

        val heightGrid = maxOf(y3_grid + h3, y4_grid + h4)
        val heightCol = y4_col + h4
        val totalHeight = (heightCol + (heightGrid - heightCol) * transitionProgress).toInt()

        layout(width, totalHeight) {
            placeables.getOrNull(0)?.placeRelative(x0, y0)
            placeables.getOrNull(1)?.placeRelative(x1, y1)
            placeables.getOrNull(2)?.placeRelative(x2, y2)
            placeables.getOrNull(3)?.placeRelative(x3, y3)
            placeables.getOrNull(4)?.placeRelative(x4, y4)
        }
    }
}

@Composable
fun FontOptionCard(
    modifier: Modifier = Modifier,
    name: String,
    isSelected: Boolean,
    fontFamily: FontFamily,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.RETRO_SPACE
    
    val shape = if (isSpaceTerminal) {
        RoundedCornerShape(6.dp)
    } else {
        RoundedCornerShape(16.dp)
    }
    
    val animatedBgColor by animateColorAsState(
        targetValue = if (isSelected) {
            if (isSpaceTerminal) Color(0xFFFF7E6B).copy(alpha = 0.15f)
            else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        } else {
            if (isSpaceTerminal) Color(0xFF0F172A).copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
        },
        label = "font_opt_bg"
    )
    
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) {
            if (isSpaceTerminal) Color(0xFFFF7E6B)
            else MaterialTheme.colorScheme.primary
        } else {
            if (isSpaceTerminal) Color(0xFF46C2B4).copy(alpha = 0.3f)
            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        },
        label = "font_opt_border"
    )

    val textColor = if (isSelected) {
        if (isSpaceTerminal) Color(0xFFFF7E6B)
        else MaterialTheme.colorScheme.primary
    } else {
        if (isSpaceTerminal) Color(0xFF46C2B4)
        else MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
        color = animatedBgColor,
        shape = shape,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = animatedBorderColor
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = fontFamily),
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = "Selected",
                        tint = textColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Aa Bb Cc 123",
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = fontFamily),
                color = if (isSpaceTerminal) Color(0xFF46C2B4).copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}


