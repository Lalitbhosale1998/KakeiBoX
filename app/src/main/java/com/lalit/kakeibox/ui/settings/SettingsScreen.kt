package com.personal.kakeibox.ui.settings

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Reorder
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.R
import com.personal.kakeibox.data.preferences.AppFont
import com.personal.kakeibox.data.preferences.AppLanguage
import com.personal.kakeibox.data.preferences.DarkThemePreference
import com.personal.kakeibox.data.preferences.TopAppBarBackground
import com.personal.kakeibox.ui.components.ExpressiveSwitch
import com.personal.kakeibox.ui.components.ExpressiveSegmentedControl
import com.personal.kakeibox.ui.components.elasticClick
import com.personal.kakeibox.ui.theme.LocalThemeSettings
import com.personal.kakeibox.ui.theme.expressiveBackground
import com.personal.kakeibox.ui.theme.getAppStrings
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: ThemeViewModel = hiltViewModel()
) {
    val themeSettings by viewModel.themeSettings.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf("visual") }
    var showTabOrderSheet by remember { mutableStateOf(false) }

    // Staggered Entrance Animation States
    var showHero by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }
    var showCategories by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showHero = true
        kotlinx.coroutines.delay(60)
        showSearch = true
        kotlinx.coroutines.delay(60)
        showCategories = true
        kotlinx.coroutines.delay(60)
        showContent = true
    }

    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val backupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream"),
        onResult = { uri ->
            if (uri != null) {
                try {
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        viewModel.backupDatabase(context, outputStream) { success ->
                            if (success) {
                                Toast.makeText(context, "Database backup successful!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Database backup failed.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed to write backup file.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                try {
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        viewModel.restoreDatabase(context, inputStream) { success ->
                            if (success) {
                                Toast.makeText(context, "Database restore successful! Restarting...", Toast.LENGTH_LONG).show()
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                                    intent?.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    context.startActivity(intent)
                                    java.lang.System.exit(0)
                                }, 1500)
                            } else {
                                Toast.makeText(context, "Database restore failed.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed to read backup file.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    val isPrimaryContainer = themeSettings.topAppBarBackground == TopAppBarBackground.PRIMARY_CONTAINER
    val systemDark = isSystemInDarkTheme()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .expressiveBackground(
                isDark = systemDark,
                isPrimaryContainer = isPrimaryContainer,
                primaryColor = MaterialTheme.colorScheme.primary,
                containerColor = Color.Unspecified,
                pattern = themeSettings.backdropPattern,
                backgroundCanvasStyle = themeSettings.backgroundCanvasStyle
            ),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(bottom = 120.dp)
        ) {
            Spacer(modifier = Modifier.height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 68.dp))

            // 1. Hero Expressive System Banner
            AnimatedVisibility(
                visible = showHero,
                enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                        slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
            ) {
                ExpressiveSettingsHeroBanner(
                    themeSettings = themeSettings,
                    onReorderTabsClick = { showTabOrderSheet = true }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Expressive Floating Search Bar
            AnimatedVisibility(
                visible = showSearch,
                enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                        slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
            ) {
                ExpressiveSettingsSearchBar(
                    searchQuery = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Asymmetric Category Selector Carousel (Hidden when searching)
            if (searchQuery.isBlank()) {
                AnimatedVisibility(
                    visible = showCategories,
                    enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                            slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
                ) {
                    ExpressiveCategoryCarousel(
                        activeTab = activeTab,
                        onTabSelected = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            activeTab = it
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 4. Content Area
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                        slideInVertically(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 4 }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    if (searchQuery.isNotBlank()) {
                        SettingsSearchResultsContent(
                            query = searchQuery,
                            themeSettings = themeSettings,
                            viewModel = viewModel,
                            backupLauncher = backupLauncher,
                            restoreLauncher = restoreLauncher,
                            onReorderNav = { showTabOrderSheet = true }
                        )
                    } else {
                        AnimatedContent(
                            targetState = activeTab,
                            transitionSpec = {
                                (fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) +
                                        slideInHorizontally(spring(stiffness = Spring.StiffnessLow)) { it / 6 } +
                                        scaleIn(initialScale = 0.96f, animationSpec = spring(stiffness = Spring.StiffnessLow)))
                                    .togetherWith(
                                        fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow)) +
                                        scaleOut(targetScale = 0.96f, animationSpec = spring(stiffness = Spring.StiffnessLow))
                                    )
                            },
                            label = "settings_tab_switch"
                        ) { tab ->
                            when (tab) {
                                "visual" -> VisualSettingsSection(themeSettings = themeSettings, viewModel = viewModel)
                                "typography" -> TypographySection(themeSettings = themeSettings, viewModel = viewModel)
                                "security" -> SecuritySection(themeSettings = themeSettings, viewModel = viewModel)
                                "data" -> DataSection(
                                    themeSettings = themeSettings,
                                    viewModel = viewModel,
                                    backupLauncher = backupLauncher,
                                    restoreLauncher = restoreLauncher,
                                    onReorderNav = { showTabOrderSheet = true }
                                )
                                "about" -> AboutSection(themeSettings = themeSettings)
                            }
                        }
                    }
                }
            }
        }
    }

    // 5. Navigation Tab Reorder Sheet
    if (showTabOrderSheet) {
        ModalBottomSheet(
            onDismissRequest = { showTabOrderSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .width(36.dp)
                        .height(4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = CircleShape
                        )
                )
            }
        ) {
            ExpressiveTabReorderSheetContent(
                themeSettings = themeSettings,
                onSaveTabOrder = { newOrder ->
                    viewModel.setTabOrder(newOrder)
                }
            )
        }
    }
}

// ==========================================
// 1. HERO SYSTEM BANNER COMPONENT
// ==========================================

@Composable
private fun ExpressiveSettingsHeroBanner(
    themeSettings: com.personal.kakeibox.data.preferences.ThemeSettings,
    onReorderTabsClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shadowElevation = 6.dp,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "環境設定 & データ金庫",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Settings & Vault",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Dynamic Monet Color Palette Extractors Preview
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
                    Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondary))
                    Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(MaterialTheme.colorScheme.tertiary))
                    Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceContainerHigh))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic Chip Status Bar
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HeroStatusChip(
                    icon = Icons.Outlined.Palette,
                    label = if (themeSettings.useDynamicColor) "Monet Dynamic Active" else "Custom Chroma Tint",
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )

                HeroStatusChip(
                    icon = if (themeSettings.privacyModeEnabled) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    label = if (themeSettings.privacyModeEnabled) "Vault Masked" else "Vault Visible",
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )

                HeroStatusChip(
                    icon = Icons.Outlined.Code,
                    label = themeSettings.appFont.name.lowercase().replaceFirstChar { it.uppercase() },
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )
            }
        }
    }
}

@Composable
private fun HeroStatusChip(
    icon: ImageVector,
    label: String,
    containerColor: Color
) {
    Surface(
        shape = CircleShape,
        color = containerColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// ==========================================
// 2. SEARCH BAR COMPONENT
// ==========================================

@Composable
private fun ExpressiveSettingsSearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        placeholder = {
            Text(
                text = "Search settings, security, or data...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Settings",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        singleLine = true,
        shape = CircleShape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    )
}

// ==========================================
// 3. CATEGORY CAROUSEL SELECTOR
// ==========================================

@Composable
private fun ExpressiveCategoryCarousel(
    activeTab: String,
    onTabSelected: (String) -> Unit
) {
    val categories = listOf(
        Triple("visual", "Visual Style", Icons.Outlined.Palette),
        Triple("typography", "Fonts & Region", Icons.Outlined.Code),
        Triple("security", "Security Vault", Icons.Outlined.Shield),
        Triple("data", "Data & Sync", Icons.Outlined.Storage),
        Triple("about", "About App", Icons.Outlined.Info)
    )

    val listState = rememberLazyListState()
    val selectedIndex = categories.indexOfFirst { it.first == activeTab }.coerceAtLeast(0)

    LaunchedEffect(activeTab) {
        if (selectedIndex >= 0) {
            listState.animateScrollToItem(selectedIndex)
        }
    }

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(categories, key = { it.first }) { (id, label, icon) ->
            val isSelected = activeTab == id
            val containerColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh,
                animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "cat_color"
            )
            val contentColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                label = "cat_content_color"
            )
            val itemScale by animateFloatAsState(
                targetValue = if (isSelected) 1.05f else 1.0f,
                animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "cat_scale"
            )

            Surface(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = itemScale
                        scaleY = itemScale
                    }
                    .clip(CircleShape)
                    .elasticClick { onTabSelected(id) },
                shape = CircleShape,
                color = containerColor,
                border = BorderStroke(
                    1.dp,
                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = contentColor
                    )
                }
            }
        }
    }
}

// ==========================================
// 4. SETTINGS SECTION CARDS
// ==========================================

@Composable
private fun VisualSettingsSection(
    themeSettings: com.personal.kakeibox.data.preferences.ThemeSettings,
    viewModel: ThemeViewModel
) {
    val haptic = LocalHapticFeedback.current

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Theme Mode Segmented Card
        ExpressiveSettingsCard(title = "App Theme Mode", icon = Icons.Outlined.DarkMode) {
            ExpressiveSegmentedControl(
                options = listOf(
                    DarkThemePreference.SYSTEM to "System",
                    DarkThemePreference.LIGHT to "Light",
                    DarkThemePreference.DARK to "Dark"
                ),
                selectedOption = themeSettings.darkThemePreference,
                onOptionSelected = { viewModel.setDarkThemePreference(it) },
                activeColor = MaterialTheme.colorScheme.primaryContainer,
                onActiveColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(16.dp),
                height = 48.dp
            )
        }

        // Dynamic Monet Tint Switch Card
        ExpressiveSettingsCard(title = "Dynamic Wallpaper Tinting", icon = Icons.Outlined.Palette) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Android 17 Monet Extraction",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Extract high-chroma secondary and tertiary color roles from your wallpaper.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                ExpressiveSwitch(
                    checked = themeSettings.useDynamicColor,
                    onCheckedChange = { viewModel.setUseDynamicColor(it) }
                )
            }

            if (!themeSettings.useDynamicColor) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Custom Chroma Intensity: ${(themeSettings.dynamicColorChromaScale * 100).roundToInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Slider(
                    value = themeSettings.dynamicColorChromaScale,
                    onValueChange = { viewModel.setDynamicColorChromaScale(it) },
                    valueRange = 0.1f..2.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                )
            }
        }

        // Top App Bar Style Card
        ExpressiveSettingsCard(title = "Top Navigation Bar Surface", icon = Icons.Outlined.Tune) {
            ExpressiveSegmentedControl(
                options = listOf(
                    TopAppBarBackground.SURFACE to "Surface Flat",
                    TopAppBarBackground.PRIMARY_CONTAINER to "Primary Overlay"
                ),
                selectedOption = themeSettings.topAppBarBackground,
                onOptionSelected = { viewModel.setTopAppBarBackground(it) },
                activeColor = MaterialTheme.colorScheme.secondaryContainer,
                onActiveColor = MaterialTheme.colorScheme.onSecondaryContainer,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(16.dp),
                height = 48.dp
            )
        }
    }
}

@Composable
private fun TypographySection(
    themeSettings: com.personal.kakeibox.data.preferences.ThemeSettings,
    viewModel: ThemeViewModel
) {
    val haptic = LocalHapticFeedback.current

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // App Language Card
        ExpressiveSettingsCard(title = "App Language / 言語", icon = Icons.Outlined.Language) {
            ExpressiveSegmentedControl(
                options = listOf(
                    AppLanguage.ENGLISH to "English",
                    AppLanguage.JAPANESE to "日本語 (Japanese)"
                ),
                selectedOption = themeSettings.appLanguage,
                onOptionSelected = { viewModel.setAppLanguage(it) },
                activeColor = MaterialTheme.colorScheme.primaryContainer,
                onActiveColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(16.dp),
                height = 50.dp
            )
        }

        // Currency Symbol Card
        ExpressiveSettingsCard(title = "Financial Currency Symbol", icon = Icons.Outlined.Payments) {
            ExpressiveSegmentedControl(
                options = listOf(
                    "₹" to "₹",
                    "¥" to "¥",
                    "$" to "$",
                    "€" to "€"
                ),
                selectedOption = themeSettings.currencySymbol,
                onOptionSelected = { viewModel.setCurrencySymbol(it) },
                activeColor = MaterialTheme.colorScheme.tertiaryContainer,
                onActiveColor = MaterialTheme.colorScheme.onTertiaryContainer,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                shape = CircleShape,
                height = 48.dp
            )
        }

        // Font Face Visual Selector & Live Preview
        ExpressiveSettingsCard(title = "Typeface Family Preview", icon = Icons.Outlined.Code) {
            val fonts = listOf(
                AppFont.NUNITO to "Nunito ✒️",
                AppFont.GOOGLE_SANS_FLEX to "Google Sans Rounded 🌟",
                AppFont.OUTFIT to "Outfit ✨",
                AppFont.PLAYFAIR to "Playfair 📖",
                AppFont.MONOSPACE to "Monospace 💻",
                AppFont.SYSTEM_SANS to "System Sans 📱",
                AppFont.CLIMATE_CRISIS to "Climate Crisis 🌋",
                AppFont.LUCKIEST_GUY to "Luckiest Guy 🎯",
                AppFont.DELA_GOTHIC_ONE to "Dela Gothic ⛩️",
                AppFont.HACHI_MARU_POP to "Hachi Maru Pop 🌸",
                AppFont.KOSUGI_MARU to "Kosugi Maru 🍡",
                AppFont.MOCHIY_POP_P_ONE to "Mochiy Pop 🍡",
                AppFont.POTTA_ONE to "Potta One 🍵",
                AppFont.RAMPART_ONE to "Rampart One 🏯",
                AppFont.WDXL_LUBRIFONT_JPN to "WDXL JPN 🎌"
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                fonts.forEach { (font, name) ->
                    val isSelected = themeSettings.appFont == font
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .elasticClick {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.setAppFont(font)
                            },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainer,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                            )

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Outlined.Check,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(18.dp)
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
private fun SecuritySection(
    themeSettings: com.personal.kakeibox.data.preferences.ThemeSettings,
    viewModel: ThemeViewModel
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ExpressiveSettingsCard(title = "Privacy Mode Protection", icon = Icons.Outlined.Shield) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Mask Financial Totals",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Redact all currency figures with secret asterisks across dashboard and widgets.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                ExpressiveSwitch(
                    checked = themeSettings.privacyModeEnabled,
                    onCheckedChange = { viewModel.setPrivacyModeEnabled(it) }
                )
            }
        }

        ExpressiveSettingsCard(title = "Biometric Lock Guard", icon = Icons.Outlined.Fingerprint) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Biometric Authentication",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Require fingerprint scan on app launch to unlock records.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                ExpressiveSwitch(
                    checked = themeSettings.biometricEnabled,
                    onCheckedChange = { viewModel.setBiometricEnabled(it) }
                )
            }
        }
    }
}

@Composable
private fun DataSection(
    themeSettings: com.personal.kakeibox.data.preferences.ThemeSettings,
    viewModel: ThemeViewModel,
    backupLauncher: androidx.activity.result.ActivityResultLauncher<String>,
    restoreLauncher: androidx.activity.result.ActivityResultLauncher<Array<String>>,
    onReorderNav: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ExpressiveSettingsCard(title = "Database Backup & Restore", icon = Icons.Outlined.Storage) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .elasticClick {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            backupLauncher.launch("kakeibox_backup.db")
                        },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 14.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CloudUpload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Backup DB",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .elasticClick {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            restoreLauncher.launch(arrayOf("*/*"))
                        },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 14.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FileDownload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Restore DB",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }

        ExpressiveSettingsCard(title = "Navigation Module Hierarchy", icon = Icons.Outlined.Reorder) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Reorder Home Tabs",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Drag and drop to rearrange order of navigation bar modules.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    modifier = Modifier
                        .clip(CircleShape)
                        .elasticClick { onReorderNav() },
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.tertiary
                ) {
                    Text(
                        text = "Manage",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AboutSection(
    themeSettings: com.personal.kakeibox.data.preferences.ThemeSettings
) {
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ExpressiveSettingsCard(title = "About KakeiBoX", icon = Icons.Outlined.Info) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "App Version",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "3.0.0 • Android 17 (API 36)",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Developer",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Lalit Bhosale",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .elasticClick {
                            val intent = android.content.Intent(
                                android.content.Intent.ACTION_VIEW,
                                android.net.Uri.parse("https://github.com/Lalitbhosale1998/KakeiBoX")
                            )
                            context.startActivity(intent)
                        },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 14.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Public,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "GitHub Repository",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 5. CARD & SEARCH RESULT HELPERS
// ==========================================

@Composable
private fun ExpressiveSettingsCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        shadowElevation = 6.dp,
        tonalElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}

@Composable
private fun SettingsSearchResultsContent(
    query: String,
    themeSettings: com.personal.kakeibox.data.preferences.ThemeSettings,
    viewModel: ThemeViewModel,
    backupLauncher: androidx.activity.result.ActivityResultLauncher<String>,
    restoreLauncher: androidx.activity.result.ActivityResultLauncher<Array<String>>,
    onReorderNav: () -> Unit
) {
    val q = query.lowercase()
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (q.contains("theme") || q.contains("dark") || q.contains("light") || q.contains("color") || q.contains("tint")) {
            VisualSettingsSection(themeSettings = themeSettings, viewModel = viewModel)
        }
        if (q.contains("lang") || q.contains("font") || q.contains("currency") || q.contains("symbol")) {
            TypographySection(themeSettings = themeSettings, viewModel = viewModel)
        }
        if (q.contains("sec") || q.contains("priv") || q.contains("bio") || q.contains("lock") || q.contains("mask")) {
            SecuritySection(themeSettings = themeSettings, viewModel = viewModel)
        }
        if (q.contains("data") || q.contains("back") || q.contains("rest") || q.contains("tab") || q.contains("order")) {
            DataSection(
                themeSettings = themeSettings,
                viewModel = viewModel,
                backupLauncher = backupLauncher,
                restoreLauncher = restoreLauncher,
                onReorderNav = onReorderNav
            )
        }
        if (q.contains("about") || q.contains("dev") || q.contains("ver") || q.contains("git")) {
            AboutSection(themeSettings = themeSettings)
        }
    }
}

// ==========================================
// 6. TAB REORDER SHEET CONTENT
// ==========================================

@Composable
private fun ExpressiveTabReorderSheetContent(
    themeSettings: com.personal.kakeibox.data.preferences.ThemeSettings,
    onSaveTabOrder: (List<String>) -> Unit
) {
    var tabOrder by remember(themeSettings.tabOrder) { mutableStateOf(themeSettings.tabOrder) }
    var draggingIndex by remember { mutableStateOf<Int?>(null) }
    var deltaY by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "Reorder Navigation Tabs",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Long press and drag vertically to change module priority.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            tabOrder.forEachIndexed { index, route ->
                val isDragging = draggingIndex == index
                val scale by animateFloatAsState(if (isDragging) 1.04f else 1.0f, label = "reorder_scale")
                val elevation by animateDpAsState(if (isDragging) 8.dp else 0.dp, label = "reorder_elev")

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            translationY = if (isDragging) deltaY else 0f
                            scaleX = scale
                            scaleY = scale
                        }
                        .pointerInput(index) {
                            detectDragGesturesAfterLongPress(
                                onDragStart = {
                                    draggingIndex = index
                                    deltaY = 0f
                                },
                                onDragEnd = {
                                    draggingIndex = null
                                    deltaY = 0f
                                    onSaveTabOrder(tabOrder)
                                },
                                onDragCancel = {
                                    draggingIndex = null
                                    deltaY = 0f
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    deltaY += dragAmount.y
                                    val threshold = 60f
                                    if (deltaY > threshold && index < tabOrder.size - 1) {
                                        val mutable = tabOrder.toMutableList()
                                        java.util.Collections.swap(mutable, index, index + 1)
                                        tabOrder = mutable
                                        draggingIndex = index + 1
                                        deltaY = 0f
                                    } else if (deltaY < -threshold && index > 0) {
                                        val mutable = tabOrder.toMutableList()
                                        java.util.Collections.swap(mutable, index, index - 1)
                                        tabOrder = mutable
                                        draggingIndex = index - 1
                                        deltaY = 0f
                                    }
                                }
                            )
                        },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isDragging) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainer,
                    border = BorderStroke(
                        1.dp,
                        if (isDragging) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    ),
                    shadowElevation = elevation
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = route.uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isDragging) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                        )

                        Icon(
                            imageVector = Icons.Outlined.Reorder,
                            contentDescription = "Drag to reorder",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BirthdayManagementContent(
    birthdays: List<com.personal.kakeibox.data.entity.BirthdayEntry>,
    onAdd: (name: String, date: java.time.LocalDate) -> Unit,
    onDelete: (com.personal.kakeibox.data.entity.BirthdayEntry) -> Unit,
    onToggleEnabled: (com.personal.kakeibox.data.entity.BirthdayEntry) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newDay by remember { mutableStateOf("1") }
    var newMonth by remember { mutableStateOf("1") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Birthday Reminders",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Manage recurring birthday alerts & notifications.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                modifier = Modifier
                    .clip(CircleShape)
                    .elasticClick { showAddDialog = true },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Add Birthday",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Add",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (birthdays.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No birthday reminders set yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                birthdays.forEach { entry ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = entry.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Date: ${entry.date.monthValue}/${entry.date.dayOfMonth}/${entry.date.year}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ExpressiveSwitch(
                                    checked = entry.isEnabled,
                                    onCheckedChange = { onToggleEnabled(entry) }
                                )

                                IconButton(onClick = { onDelete(entry) }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    text = "Add Birthday Reminder",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newMonth,
                            onValueChange = { newMonth = it },
                            label = { Text("Month (1-12)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = newDay,
                            onValueChange = { newDay = it },
                            label = { Text("Day (1-31)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        val d = newDay.toIntOrNull() ?: 1
                        val m = newMonth.toIntOrNull() ?: 1
                        val y = java.time.LocalDate.now().year
                        if (newName.isNotBlank()) {
                            onAdd(newName, java.time.LocalDate.of(y, m.coerceIn(1, 12), d.coerceIn(1, 31)))
                            newName = ""
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
