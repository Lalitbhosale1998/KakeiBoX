package com.personal.kakeibox.ui.settings

import com.personal.kakeibox.ui.theme.getAppStrings
import com.personal.kakeibox.ui.theme.LocalThemeSettings
import com.personal.kakeibox.ui.components.ExpressiveSwitch
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Dock
import androidx.compose.material.icons.outlined.DragHandle
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material.icons.outlined.Tune
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
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
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
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast
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
import com.personal.kakeibox.data.preferences.ColorIntensityPreset
import com.personal.kakeibox.data.preferences.DynamicTonalStyle
import com.personal.kakeibox.data.preferences.AppFont
import com.personal.kakeibox.data.preferences.NavAnimationPreference
import com.personal.kakeibox.ui.theme.LocalThemeStyle
import com.personal.kakeibox.ui.theme.terminalScanlines
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import com.personal.kakeibox.ui.theme.ComfortaaFontFamily
import com.personal.kakeibox.ui.theme.NunitoFontFamily
import com.personal.kakeibox.ui.theme.OutfitFontFamily
import com.personal.kakeibox.ui.theme.PlayfairFontFamily
import androidx.compose.foundation.isSystemInDarkTheme
import com.personal.kakeibox.ui.theme.expressiveBackground
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.draw.shadow
import com.personal.kakeibox.ui.theme.LocalGlowIntensity
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.toPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.personal.kakeibox.ui.theme.glow
import com.personal.kakeibox.data.preferences.GlowIntensity
import com.personal.kakeibox.data.preferences.BackdropPattern
import com.personal.kakeibox.data.preferences.CardShapePreference
import com.personal.kakeibox.data.preferences.TouchSynesthesia
import androidx.compose.ui.text.font.FontFamily
import com.personal.kakeibox.data.entity.BirthdayEntry
import com.personal.kakeibox.ui.components.BentoCard
import com.personal.kakeibox.ui.components.ExpressiveSettingsPosterCard
import com.personal.kakeibox.ui.components.ExpressiveEditorialMenuDrawer
import com.personal.kakeibox.ui.components.ExpressiveTab
import com.personal.kakeibox.ui.components.elasticClick
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
    var activeTab by remember { mutableStateOf("visual") }
    var showTabOrderSheet by remember { mutableStateOf(false) }
    var isMenuExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

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
                                // Restart app after 1.5 seconds to let Toast display
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

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val haptic = LocalHapticFeedback.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val topAppBarContainerColor by animateColorAsState(
        targetValue = when (themeSettings.topAppBarBackground) {
            TopAppBarBackground.SURFACE -> MaterialTheme.colorScheme.primaryContainer
            TopAppBarBackground.PRIMARY_CONTAINER -> MaterialTheme.colorScheme.primaryContainer
        },
        label = "top_app_bar_container_color"
    )

    val scrolledContainerColor by animateColorAsState(
        targetValue = when (themeSettings.topAppBarBackground) {
            TopAppBarBackground.SURFACE -> MaterialTheme.colorScheme.surfaceContainer
            TopAppBarBackground.PRIMARY_CONTAINER -> {
                androidx.compose.ui.graphics.lerp(
                    androidx.compose.ui.graphics.lerp(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.primaryContainer,
                        0.35f
                    ),
                    MaterialTheme.colorScheme.primary,
                    0.08f // 8% tint overlay (simulating 4.dp elevation tint)
                )
            }
        },
        label = "settings_top_bar_scrolled_color"
    )

    val statusBarsPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val isPrimaryContainer = themeSettings.topAppBarBackground == TopAppBarBackground.PRIMARY_CONTAINER

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .expressiveBackground(
                isDark = isSystemInDarkTheme(),
                isPrimaryContainer = isPrimaryContainer,
                primaryColor = MaterialTheme.colorScheme.primary,
                containerColor = topAppBarContainerColor,
                pattern = themeSettings.backdropPattern,
                backgroundCanvasStyle = themeSettings.backgroundCanvasStyle
            ),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {}
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = statusBarsPadding + 64.dp)
                .padding(bottom = 60.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val context = LocalContext.current

            // 🌟 Glassmorphic Floating Search Capsule
            val bentoIdleColor = androidx.compose.ui.graphics.lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.primaryContainer, 0.35f)
            var isSearchFocused by remember { mutableStateOf(false) }
            val searchFocusScale by animateFloatAsState(
                targetValue = if (isSearchFocused) 1.02f else 1f,
                animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessMedium),
                label = "search_focus_scale"
            )
            val searchBorderColor by animateColorAsState(
                targetValue = if (isSearchFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f),
                label = "search_border_color"
            )
            val searchClearRotation by animateFloatAsState(
                targetValue = if (searchQuery.isNotEmpty()) 90f else 0f,
                animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
                label = "search_clear_rot"
            )

            Surface(
                shape = RoundedCornerShape(percent = 50),
                color = bentoIdleColor,
                tonalElevation = 4.dp,
                shadowElevation = 6.dp,
                border = BorderStroke(1.dp, searchBorderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .graphicsLayer {
                        scaleX = searchFocusScale
                        scaleY = searchFocusScale
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged { isSearchFocused = it.isFocused },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        decorationBox = { innerTextField ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "Search settings...",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" },
                            modifier = Modifier
                                .size(32.dp)
                                .graphicsLayer { rotationZ = searchClearRotation }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Text(
                                text = "⌘ K",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            fun shouldShow(title: String, description: String = "", keywords: List<String> = emptyList()): Boolean {
                if (searchQuery.isBlank()) return true
                val terms = (keywords + title + description)
                return terms.any { it.contains(searchQuery, ignoreCase = true) }
            }

            if (searchQuery.isEmpty()) {
                // Horizontal category selector
                SettingsTabRow(
                    selectedTab = activeTab,
                    onTabSelected = { activeTab = it },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )

                // Render content depending on activeTab with spring motion choreography
                AnimatedContent(
                    targetState = activeTab,
                    transitionSpec = {
                        (slideInVertically(
                            animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = 0.75f),
                            initialOffsetY = { height -> height / 5 }
                        ) + fadeIn(animationSpec = tween(220)))
                        .togetherWith(
                            slideOutVertically(
                                animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = 0.75f),
                                targetOffsetY = { height -> -height / 5 }
                            ) + fadeOut(animationSpec = tween(150))
                        )
                    },
                    label = "settings_tab_content_anim"
                ) { targetTab ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        when (targetTab) {
                            "visual" -> {
                                val strings = getAppStrings(themeSettings.appLanguage)
                                // Theme & Color Group
                                SettingsGroup(title = strings.themeColorSettings) {

                                    SettingsSelectorRow(
                                        title = strings.themeFlavorTitle,
                                        description = strings.themeFlavorDesc,
                                        icon = Icons.Outlined.Palette,
                                        selectedValueLabel = when (themeSettings.themeFlavor) {
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.DYNAMIC_MATERIAL -> "Dynamic Material"
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.MIDNIGHT_OBSIDIAN -> "Midnight Obsidian"
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.EMERALD_ZEN -> "Emerald Zen"
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.SUNSET_CORAL -> "Sunset Coral"
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.TOKYO_GLASS -> "Tokyo Glass"
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.SHU_NURI -> "朱塗り Shu-Nuri"
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.O_MIKI -> "御神酒 O-Miki"
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.NEON_BRUTALIST -> "Neon Brutalist ⚡"
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.STHAPATYA -> "स्थापत्य Sthapatya 🛕"
                                        },
                                        options = listOf(
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.DYNAMIC_MATERIAL to "Dynamic Material",
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.MIDNIGHT_OBSIDIAN to "Midnight Obsidian 🌌",
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.EMERALD_ZEN to "Emerald Zen 🌿",
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.SUNSET_CORAL to "Sunset Coral 🌅",
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.TOKYO_GLASS to "Tokyo Glass 💎",
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.SHU_NURI to "朱塗り Shu-Nuri 🩩",
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.O_MIKI to "御神酒 O-Miki 🎏",
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.NEON_BRUTALIST to "Neon Brutalist ⚡",
                                            com.personal.kakeibox.data.preferences.ThemeFlavor.STHAPATYA to "स्थापत्य Sthapatya 🛕"
                                        ),
                                        selectedOption = themeSettings.themeFlavor,
                                        onOptionSelected = { viewModel.setThemeFlavor(it) },
                                        accentColor = Color(0xFF00F2FE)
                                    )
                                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))


                                    SettingsSelectorRow(
                                        title = strings.darkThemeTitle,
                                        description = strings.darkThemeDesc,
                                        icon = Icons.Outlined.DarkMode,
                                        selectedValueLabel = themeSettings.darkThemePreference.name.lowercase().replaceFirstChar { it.uppercase() },
                                        options = listOf(DarkThemePreference.SYSTEM to "System", DarkThemePreference.LIGHT to "Light", DarkThemePreference.DARK to "Dark"),
                                        onOptionSelected = { viewModel.setDarkThemePreference(it) },
                                        accentColor = Color(0xFF3B82F6)
                                    )
                                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
                                    SettingsToggleRow(
                                        title = strings.dynamicColorTint,
                                        description = strings.extractAccentWallpaper,
                                        icon = Icons.Outlined.Palette,
                                        checked = themeSettings.useDynamicColor,
                                        onCheckedChange = { viewModel.setUseDynamicColor(it) },
                                        accentColor = Color(0xFFD946EF)
                                    )

                                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
                                    SettingsActionRow(
                                        title = "Launch Setup Wizard 🪄",
                                        description = "Re-run the M3 Expressive onboarding flow",
                                        icon = Icons.Outlined.Tune,
                                        actionLabel = "Start",
                                        onClick = { viewModel.setSetupComplete(false) },
                                        accentColor = Color(0xFF8B5CF6)
                                    )
                                }

                                // Typography & Layout Group
                                SettingsGroup(title = strings.typographyLayoutSettings) {
                                    SettingsSelectorRow(
                                        title = strings.appFontFamily,
                                        description = strings.chooseGlobalTypeface,
                                        icon = Icons.Outlined.Code,
                                        selectedValueLabel = themeSettings.appFont.name.lowercase().replaceFirstChar { it.uppercase() },
                                        options = listOf(
                                             AppFont.GOOGLE_SANS_FLEX to "Google Sans Rounded 🌟",
                                             AppFont.NUNITO to "Nunito Modern ✒️",
                                             AppFont.CLIMATE_CRISIS to "Climate Crisis 🌋",
                                             AppFont.LUCKIEST_GUY to "Luckiest Guy 🎯",
                                             AppFont.OUTFIT to "Outfit 📐",
                                             AppFont.PLAYFAIR to "Playfair 📜",
                                             AppFont.MONOSPACE to "Monospace 💻",
                                             AppFont.SYSTEM_SANS to "System Sans 📱",
                                             AppFont.DELA_GOTHIC_ONE to "── 🇯🇵 JAPANESE ── Dela Gothic ⛩️",
                                             AppFont.HACHI_MARU_POP to "Hachi Maru Pop 🌸",
                                             AppFont.KOSUGI_MARU to "Kosugi Maru 🍡",
                                             AppFont.MOCHIY_POP_P_ONE to "Mochiy Pop P One 🍡",
                                             AppFont.POTTA_ONE to "Potta One 🍵",
                                             AppFont.RAMPART_ONE to "Rampart One 🏯",
                                             AppFont.WDXL_LUBRIFONT_JPN to "WDXL Lubrifont JPN 🎌"
                                         ),
                                        onOptionSelected = { viewModel.setAppFont(it) },
                                        accentColor = Color(0xFFF59E0B)
                                    )
                                }

                            }
                            "preferences" -> {
                                val strings = getAppStrings(themeSettings.appLanguage)
                                SettingsGroup(title = strings.regionalLocaleOptions) {
                                    SettingsSelectorRow(
                                        title = strings.appLanguage,
                                        description = strings.selectLanguageDesc,
                                        icon = Icons.Outlined.Language,
                                        selectedValueLabel = themeSettings.appLanguage.name.lowercase().replaceFirstChar { it.uppercase() },
                                        options = listOf(AppLanguage.ENGLISH to "English", AppLanguage.JAPANESE to "日本語"),
                                        onOptionSelected = { viewModel.setAppLanguage(it) },
                                        selectedOption = themeSettings.appLanguage,
                                        accentColor = Color(0xFF10B981)
                                    )
                                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
                                    SettingsSelectorRow(
                                        title = strings.currencySymbol,
                                        description = strings.defineCurrencyDesc,
                                        icon = Icons.Outlined.Payments,
                                        selectedValueLabel = themeSettings.currencySymbol,
                                        options = listOf("₹" to "₹ (Rupee)", "¥" to "¥ (Yen)", "$" to "$ (Dollar)", "€" to "€ (Euro)"),
                                        onOptionSelected = { viewModel.setCurrencySymbol(it) },
                                        accentColor = Color(0xFF059669)
                                    )
                                }

                                SettingsGroup(title = strings.appCustomization) {
                                    SettingsActionRow(
                                        title = strings.reorderNavTabs,
                                        description = strings.manageNavTabsDesc,
                                        icon = Icons.Outlined.Reorder,
                                        actionLabel = "Manage",
                                        onClick = { showTabOrderSheet = true },
                                        accentColor = Color(0xFF06B6D4)
                                    )
                                }
                            }
                            "security" -> {
                                val strings = getAppStrings(themeSettings.appLanguage)
                                SettingsGroup(title = strings.privacyAppLock) {
                                    SettingsToggleRow(
                                        title = strings.privacyModeProtection,
                                        description = strings.maskSensitiveTotals,
                                        icon = if (themeSettings.privacyModeEnabled) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        checked = themeSettings.privacyModeEnabled,
                                        onCheckedChange = { viewModel.setPrivacyModeEnabled(it) },
                                        accentColor = Color(0xFF64748B)
                                    )
                                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
                                    SettingsToggleRow(
                                        title = strings.biometricLockGuard,
                                        description = strings.protectAccountBiometrics,
                                        icon = Icons.Outlined.Fingerprint,
                                        checked = themeSettings.biometricEnabled,
                                        onCheckedChange = { viewModel.setBiometricEnabled(it) },
                                        accentColor = Color(0xFFE11D48)
                                    )
                                }

                                SettingsGroup(title = strings.dataManagement) {
                                    SettingsActionRow(
                                        title = strings.backupDatabase,
                                        description = strings.exportLocalDbCopy,
                                        icon = Icons.Outlined.CloudUpload,
                                        actionLabel = "Backup",
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            backupLauncher.launch("kakeibox_backup.db")
                                        },
                                        accentColor = Color(0xFF4F46E5)
                                    )
                                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
                                    SettingsActionRow(
                                        title = strings.restoreDatabase,
                                        description = strings.importBackupDbFile,
                                        icon = Icons.Outlined.FileDownload,
                                        actionLabel = "Restore",
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            restoreLauncher.launch(arrayOf("*/*"))
                                        },
                                        accentColor = Color(0xFF7C3AED)
                                    )
                                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
                                    SettingsActionRow(
                                        title = strings.exportFinancialHistory,
                                        description = strings.downloadCsvHistory,
                                        icon = Icons.Outlined.FileDownload,
                                        actionLabel = "Export",
                                        onClick = {
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
                                        },
                                        accentColor = Color(0xFF10B981)
                                    )
                                }
                            }
                            "about" -> {
                                val strings = getAppStrings(themeSettings.appLanguage)
                                SettingsGroup(title = strings.informationTitle) {
                                    SettingsActionRow(
                                        title = strings.aboutVersion,
                                        description = strings.aboutVersionDesc,
                                        icon = Icons.Outlined.Info,
                                        onClick = {},
                                        accentColor = Color(0xFF475569)
                                    )
                                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
                                    SettingsActionRow(
                                        title = strings.aboutDeveloper,
                                        description = strings.aboutDeveloperDesc,
                                        icon = Icons.Outlined.Code,
                                        onClick = {},
                                        accentColor = Color(0xFFF59E0B)
                                    )
                                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
                                    SettingsActionRow(
                                        title = strings.aboutGithub,
                                        description = strings.aboutGithubDesc,
                                        icon = Icons.Outlined.Public,
                                        onClick = {
                                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://github.com/Lalitbhosale1998/KakeiBoX"))
                                            context.startActivity(intent)
                                        },
                                        accentColor = Color(0xFF0284C7)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // If search query is not empty, display search results
                val hasVisualMatches = shouldShow("Interface Aesthetic", keywords = listOf("style", "visual", "retro", "expressive", "aesthetic")) ||
                        shouldShow("App Font Face", keywords = listOf("font", "typeface", "text", "style")) ||
                        shouldShow("CRT", keywords = listOf("crt", "distortion", "scanline", "curve")) ||
                        shouldShow("App Theme", keywords = listOf("dark", "light", "mode")) ||
                        shouldShow("Dynamic", keywords = listOf("wallpaper", "color")) ||
                        shouldShow("Background Style", keywords = listOf("header", "navigation", "surface", "container")) ||
                        shouldShow("Navigation Layout", keywords = listOf("floating", "bar"))
                        
                if (hasVisualMatches) {
                    SettingsGroup("🎨 Visual Style") {

                        if (shouldShow("App Theme", keywords = listOf("dark", "light", "mode"))) {
                            SettingsSelectorRow(
                                title = "App Dark Theme",
                                description = "Choose light, dark, or system-adaptive dark preference.",
                                icon = Icons.Outlined.DarkMode,
                                selectedValueLabel = themeSettings.darkThemePreference.name.lowercase().replaceFirstChar { it.uppercase() },
                                options = listOf(DarkThemePreference.SYSTEM to "System", DarkThemePreference.LIGHT to "Light", DarkThemePreference.DARK to "Dark"),
                                onOptionSelected = { viewModel.setDarkThemePreference(it) },
                                accentColor = Color(0xFF3B82F6)
                            )
                        }
                        if (shouldShow("Dynamic Color", keywords = listOf("wallpaper", "color"))) {
                            SettingsToggleRow(
                                title = "Dynamic Wallpaper Color",
                                description = "Tint the application based on your device background.",
                                icon = Icons.Outlined.Palette,
                                checked = themeSettings.useDynamicColor,
                                onCheckedChange = { viewModel.setUseDynamicColor(it) },
                                accentColor = Color(0xFFD946EF)
                            )
                        }

                        if (shouldShow("App Font Face", keywords = listOf("font", "typeface", "text", "style"))) {
                            SettingsSelectorRow(
                                title = "App Font Family",
                                description = "Global typography typeface settings.",
                                icon = Icons.Outlined.Code,
                                selectedValueLabel = themeSettings.appFont.name.lowercase().replaceFirstChar { it.uppercase() },
                                options = listOf(
                                    AppFont.NUNITO to "Nunito",
                                    AppFont.GOOGLE_SANS_FLEX to "Google Sans Rounded 🌟",
                                    AppFont.MONOSPACE to "Monospace",
                                    AppFont.SYSTEM_SANS to "System Sans",
                                    AppFont.OUTFIT to "Outfit",
                                    AppFont.PLAYFAIR to "Playfair"
                                ),
                                onOptionSelected = { viewModel.setAppFont(it) },
                                accentColor = Color(0xFFF59E0B)
                            )
                        }

                    }
                }
                
                val hasPreferencesMatches = shouldShow("Language", keywords = listOf("locale", "regional")) ||
                        shouldShow("Currency", keywords = listOf("money", "symbol")) ||
                        shouldShow("Tab Order", keywords = listOf("navigation", "reorder"))
                        
                if (hasPreferencesMatches) {
                     SettingsGroup("⚙️ Preferences") {
                        if (shouldShow("Language", keywords = listOf("locale", "regional"))) {
                            SettingsSelectorRow(
                                title = "App Language",
                                description = "Switch system localization language.",
                                icon = Icons.Outlined.Language,
                                selectedValueLabel = themeSettings.appLanguage.name.lowercase().replaceFirstChar { it.uppercase() },
                                options = listOf(AppLanguage.ENGLISH to "English", AppLanguage.JAPANESE to "Japanese"),
                                onOptionSelected = { viewModel.setAppLanguage(it) },
                                accentColor = Color(0xFF10B981)
                            )
                        }
                        if (shouldShow("Currency", keywords = listOf("money", "symbol"))) {
                            SettingsSelectorRow(
                                title = "Currency Symbol",
                                description = "Define standard symbol applied to financial records.",
                                icon = Icons.Outlined.Payments,
                                selectedValueLabel = themeSettings.currencySymbol,
                                options = listOf("₹" to "₹ (Rupee)", "¥" to "¥ (Yen)", "$" to "$ (Dollar)", "€" to "€ (Euro)"),
                                onOptionSelected = { viewModel.setCurrencySymbol(it) },
                                accentColor = Color(0xFF059669)
                            )
                        }
                        if (shouldShow("Tab Order", keywords = listOf("navigation", "reorder"))) {
                            SettingsActionRow(
                                title = "Reorder Navigation Tabs",
                                description = "Arrange the hierarchy order of home screen tabs.",
                                icon = Icons.Outlined.Reorder,
                                actionLabel = "Manage",
                                onClick = { showTabOrderSheet = true },
                                accentColor = Color(0xFF06B6D4)
                            )
                        }
                    }
                }

                val hasSecurityMatches = shouldShow("Security", keywords = listOf("fingerprint", "privacy", "lock")) ||
                        shouldShow("Privacy Mode", keywords = listOf("mask", "hide")) ||
                        shouldShow("Data Health", keywords = listOf("sync", "cloud")) ||
                        shouldShow("Export Data", keywords = listOf("download", "backup"))
                        
                if (hasSecurityMatches) {
                    SettingsGroup("🔒 Security & Data") {
                        if (shouldShow("Privacy Mode", keywords = listOf("mask", "hide"))) {
                            SettingsToggleRow(
                                title = "Privacy Mode Protection",
                                description = "Redact/mask sensitive financial figures with dots.",
                                icon = if (themeSettings.privacyModeEnabled) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                checked = themeSettings.privacyModeEnabled,
                                onCheckedChange = { viewModel.setPrivacyModeEnabled(it) },
                                accentColor = Color(0xFF64748B)
                            )
                        }
                        if (shouldShow("Security", keywords = listOf("fingerprint", "lock"))) {
                            SettingsToggleRow(
                                title = "Biometric Lock Guard",
                                description = "Require fingerprint authentication to access details.",
                                icon = Icons.Outlined.Fingerprint,
                                checked = themeSettings.biometricEnabled,
                                onCheckedChange = { viewModel.setBiometricEnabled(it) },
                                accentColor = Color(0xFFE11D48)
                            )
                        }
                        if (shouldShow("Data Health", keywords = listOf("sync", "cloud"))) {
                            var isSyncing by remember { mutableStateOf(false) }
                            LaunchedEffect(isSyncing) {
                                if (isSyncing) {
                                    delay(2000)
                                    isSyncing = false
                                }
                            }
                            SettingsActionRow(
                                title = "Data Backup Health",
                                description = if (isSyncing) "Syncing database backup securely..." else "All database assets successfully synchronized.",
                                icon = Icons.Outlined.CloudUpload,
                                actionLabel = if (isSyncing) "Syncing" else "Backup Now",
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isSyncing = true
                                },
                                accentColor = Color(0xFF4F46E5)
                            )
                        }
                        if (shouldShow("Export Data", keywords = listOf("download", "csv"))) {
                            val context = LocalContext.current
                            SettingsActionRow(
                                title = "Export Financial Records",
                                description = "Export transactions, wages, and travel as CSV file.",
                                icon = Icons.Outlined.FileDownload,
                                actionLabel = "Export",
                                onClick = {
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
                                },
                                accentColor = Color(0xFF10B981)
                            )
                        }
                    }
                }

                val hasAboutMatches = shouldShow("About") || shouldShow("Version") || shouldShow("Developer") || shouldShow("GitHub")
                if (hasAboutMatches) {
                    SettingsGroup("ℹ️ About KakeiboX") {
                        if (shouldShow("Version")) {
                            SettingsActionRow(
                                title = stringResource(R.string.about_version),
                                description = stringResource(R.string.about_version_desc),
                                icon = Icons.Outlined.Info,
                                onClick = {},
                                accentColor = Color(0xFF475569)
                            )
                        }
                        if (shouldShow("Developer")) {


                            SettingsActionRow(
                                title = stringResource(R.string.about_developer),
                                description = stringResource(R.string.about_developer_desc),
                                icon = Icons.Outlined.Code,
                                onClick = {},
                                accentColor = Color(0xFFF59E0B)
                            )
                        }
                        if (shouldShow("GitHub")) {
                            if (shouldShow("Version") || shouldShow("Developer")) {
                                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
                            }
                            SettingsActionRow(
                                title = stringResource(R.string.about_github),
                                description = stringResource(R.string.about_github_desc),
                                icon = Icons.Outlined.Public,
                                onClick = {
                                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://github.com/Lalitbhosale1998/KakeiBoX"))
                                    context.startActivity(intent)
                                },
                                accentColor = Color(0xFF0284C7)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showTabOrderSheet) {
        val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.M3_EXPRESSIVE && false
        val sheetBgColor = if (isSpaceTerminal) Color(0xFF0F172A) else MaterialTheme.colorScheme.surfaceContainerHigh
        val sheetShape = if (isSpaceTerminal) MaterialTheme.shapes.medium.copy(bottomStart = androidx.compose.foundation.shape.CornerSize(0.dp), bottomEnd = androidx.compose.foundation.shape.CornerSize(0.dp)) else MaterialTheme.shapes.extraLarge.copy(bottomStart = androidx.compose.foundation.shape.CornerSize(0.dp), bottomEnd = androidx.compose.foundation.shape.CornerSize(0.dp))

        ModalBottomSheet(
            onDismissRequest = { showTabOrderSheet = false },
            sheetState = sheetState,
            containerColor = sheetBgColor,
            shape = sheetShape,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .width(36.dp)
                        .height(4.dp)
                        .background(
                            color = if (isSpaceTerminal) Color(0xFF46C2B4).copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant,
                            shape = CircleShape
                        )
                )
            }
        ) {
            val tabOrder = themeSettings.tabOrder
            var draggingItemIndex by remember { mutableStateOf<Int?>(null) }
            var deltaY by remember { mutableFloatStateOf(0f) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 48.dp, top = 8.dp)
            ) {
                Text(
                    text = "Reorder Navigation Tabs",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = if (isSpaceTerminal) Color(0xFF46C2B4) else MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Long press and drag to change the order of tabs in your navigation bar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSpaceTerminal) Color(0xFF94A3B8) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tabOrder.forEachIndexed { index, route ->
                        val isDragging = draggingItemIndex == index
                        val elevation by animateDpAsState(if (isDragging) 8.dp else 0.dp)
                        val scale by animateFloatAsState(if (isDragging) 1.05f else 1f)

                        val itemBg = if (isDragging) {
                            if (isSpaceTerminal) Color(0xFFFF7E6B).copy(alpha = 0.25f)
                            else MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            if (isSpaceTerminal) Color(0xFF1E293B).copy(alpha = 0.6f)
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        }

                        val itemBorder = if (isSpaceTerminal) {
                            BorderStroke(1.dp, if (isDragging) Color(0xFFFF7E6B) else Color(0xFF46C2B4).copy(alpha = 0.2f))
                        } else {
                            null
                        }

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
                            shape = if (isSpaceTerminal) MaterialTheme.shapes.small else MaterialTheme.shapes.medium,
                            color = itemBg,
                            border = itemBorder,
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
                                        "exercise" -> Icons.Outlined.FitnessCenter
                                        "kotoba", "journeys" -> Icons.Outlined.Translate
                                        else -> Icons.Outlined.Settings
                                    },
                                    contentDescription = null,
                                    tint = if (isDragging) {
                                        if (isSpaceTerminal) Color(0xFFFF7E6B) else MaterialTheme.colorScheme.onSecondaryContainer
                                    } else {
                                        if (isSpaceTerminal) Color(0xFF46C2B4) else MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = when(route) {
                                        "salary" -> "Salary"
                                        "exercise" -> "Exercise"
                                        "kotoba", "journeys" -> "Kotoba"
                                        "settings" -> "Settings"
                                        else -> route.replaceFirstChar { it.uppercase() }
                                    },
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDragging) {
                                        if (isSpaceTerminal) Color(0xFFFF7E6B) else MaterialTheme.colorScheme.onSecondaryContainer
                                    } else {
                                        if (isSpaceTerminal) Color(0xFFE2E8F0) else MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                                Spacer(modifier = Modifier.weight(1f))

                                val isHidden = themeSettings.hiddenTabs.contains(route)
                                val isLocked = route == "settings" || route == "home"

                                if (isLocked) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.surfaceContainerHigh
                                    ) {
                                        Text(
                                            text = "ALWAYS ON",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                } else {
                                    Surface(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                viewModel.toggleTabVisibility(route)
                                            },
                                        color = if (isHidden) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isHidden) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                                contentDescription = if (isHidden) "Hidden" else "Visible",
                                                modifier = Modifier.size(14.dp),
                                                tint = if (isHidden) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                text = if (isHidden) "HIDDEN" else "VISIBLE",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black,
                                                color = if (isHidden) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                }

                                Icon(
                                    imageVector = Icons.Outlined.DragHandle,
                                    contentDescription = "Drag to reorder",
                                    tint = if (isSpaceTerminal) Color(0xFF46C2B4).copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // ── Expressive Full-Bleed FC88 Navigation Menu Drawer ──
    ExpressiveEditorialMenuDrawer(
        isOpen = isMenuExpanded,
        onDismiss = { isMenuExpanded = false },
        onNavigateTab = { _ -> isMenuExpanded = false },
        onTogglePrivacyMode = { viewModel.setPrivacyModeEnabled(!themeSettings.privacyModeEnabled) },
        onAddEntry = {},
        onOpenThemeSettings = { isMenuExpanded = false },
        themeSettings = themeSettings
    )
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

    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.M3_EXPRESSIVE && false
    val cardShape = if (isSpaceTerminal) MaterialTheme.shapes.medium else MaterialTheme.shapes.extraLarge
    val cardBorder = if (isSpaceTerminal) {
        BorderStroke(1.5.dp, if (isExpanded) Color(0xFFFF7E6B) else Color(0xFF46C2B4).copy(alpha = 0.4f))
    } else {
        null
    }
    val iconShape = if (isSpaceTerminal) MaterialTheme.shapes.small else MaterialTheme.shapes.medium

    val glowIntensity = LocalGlowIntensity.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .glow(
                color = if (isSpaceTerminal) Color(0xFFFF7E6B) else MaterialTheme.colorScheme.primary,
                intensity = if (isExpanded) glowIntensity else GlowIntensity.OFF,
                shape = cardShape
            )
            .elasticClick(
                onClick = onClick
            )
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
        border = cardBorder,
        shadowElevation = 8.dp,
        tonalElevation = 4.dp
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
                shape = MaterialTheme.shapes.extraLarge
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
                            shape = MaterialTheme.shapes.large
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
                        shape = MaterialTheme.shapes.large,
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
        shape = MaterialTheme.shapes.large,
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
                        shape = MaterialTheme.shapes.medium
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
            
            ExpressiveSwitch(
                checked = birthday.isEnabled,
                onCheckedChange = { onToggle() }
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
    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.M3_EXPRESSIVE && false
    
    val shape = if (isSpaceTerminal) {
        MaterialTheme.shapes.small
    } else {
        MaterialTheme.shapes.medium
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

    val glowIntensity = LocalGlowIntensity.current

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .glow(
                color = if (isSpaceTerminal) Color(0xFFFF7E6B) else MaterialTheme.colorScheme.primary,
                intensity = if (isSelected) glowIntensity else GlowIntensity.OFF,
                shape = shape
            )
            .elasticClick(
                onClick = onClick
            ),
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

class SettingsTabPolygonShape(
    private val morph: Morph,
    private val progress: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val matrix = android.graphics.Matrix()
        matrix.setScale(size.width / 2f, size.height / 2f)
        matrix.postTranslate(size.width / 2f, size.height / 2f)

        val path = morph.toPath(progress = progress).apply {
            transform(matrix)
        }
        return Outline.Generic(path.asComposePath())
    }
}

@Composable
fun SettingsTabRow(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val strings = getAppStrings(LocalThemeSettings.current.appLanguage)

    val tabs = remember(strings) {
        listOf(
            BentoTabInfo("visual", strings.visuals, strings.themesAndStyles, Icons.Outlined.Palette),
            BentoTabInfo("preferences", strings.preferences, strings.currencyAndMode, Icons.Outlined.Settings),
            BentoTabInfo("security", strings.security, strings.biometricsAndLock, Icons.Outlined.Fingerprint),
            BentoTabInfo("about", strings.about, strings.version, Icons.Outlined.Info)
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Row 1: Visuals & Preferences
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BentoNavTile(
                tab = tabs[0],
                isSelected = selectedTab == tabs[0].key,
                onClick = { onTabSelected(tabs[0].key) },
                modifier = Modifier.weight(1f)
            )
            BentoNavTile(
                tab = tabs[1],
                isSelected = selectedTab == tabs[1].key,
                onClick = { onTabSelected(tabs[1].key) },
                modifier = Modifier.weight(1f)
            )
        }

        // Row 2: Security & About
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BentoNavTile(
                tab = tabs[2],
                isSelected = selectedTab == tabs[2].key,
                onClick = { onTabSelected(tabs[2].key) },
                modifier = Modifier.weight(1f)
            )
            BentoNavTile(
                tab = tabs[3],
                isSelected = selectedTab == tabs[3].key,
                onClick = { onTabSelected(tabs[3].key) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BentoNavTile(
    tab: BentoTabInfo,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else if (isSelected) 1.02f else 1.0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 400f),
        label = "bento_tile_scale_${tab.key}"
    )

    val containerColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "bento_tile_color_${tab.key}"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        },
        label = "bento_tile_border_${tab.key}"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        label = "bento_tile_content_${tab.key}"
    )

    val iconColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
        },
        label = "bento_tile_icon_${tab.key}"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.25f else 1.0f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
        label = "bento_tile_icon_scale_${tab.key}"
    )

    val iconRotation by animateFloatAsState(
        targetValue = if (isSelected) 360f else 0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "bento_tile_icon_rot_${tab.key}"
    )

    Surface(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
        shape = RoundedCornerShape(22.dp),
        color = containerColor,
        border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor),
        shadowElevation = if (isSelected) 6.dp else 2.dp,
        tonalElevation = if (isSelected) 4.dp else 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.label,
                    tint = iconColor,
                    modifier = Modifier
                        .size(20.dp)
                        .graphicsLayer {
                            scaleX = iconScale
                            scaleY = iconScale
                            rotationZ = iconRotation
                        }
                )
                Text(
                    text = tab.label,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                    color = contentColor,
                    maxLines = 1
                )
            }

            Text(
                text = tab.subtitle,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

private data class BentoTabInfo(
    val key: String,
    val label: String,
    val subtitle: String,
    val icon: ImageVector
)


@Composable
fun SettingsGroup(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.M3_EXPRESSIVE && false
    val cardShape = if (isSpaceTerminal) MaterialTheme.shapes.medium else MaterialTheme.shapes.large
    val cardBg = if (isSpaceTerminal) Color(0xFF0F172A).copy(alpha = 0.6f) else MaterialTheme.colorScheme.surfaceContainerLow
    val cardBorder = if (isSpaceTerminal) {
        BorderStroke(1.dp, Color(0xFF46C2B4).copy(alpha = 0.3f))
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = if (isSpaceTerminal) Color(0xFF46C2B4) else MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = cardBg,
            shape = cardShape,
            border = cardBorder,
            shadowElevation = 8.dp,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                content = content
            )
        }
    }
}

@Composable
fun SettingsToggleRow(
    title: String,
    description: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color? = null
) {
    val haptic = LocalHapticFeedback.current
    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.M3_EXPRESSIVE && false
    val iconColor = if (isSpaceTerminal) Color(0xFF46C2B4) else (accentColor ?: MaterialTheme.colorScheme.primary)
    val iconBgColor = if (isSpaceTerminal) Color(0xFF46C2B4).copy(alpha = 0.1f) else (accentColor ?: MaterialTheme.colorScheme.primary).copy(alpha = 0.15f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onCheckedChange(!checked)
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = iconBgColor,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (isSpaceTerminal) Color(0xFFE2E8F0) else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSpaceTerminal) Color(0xFF94A3B8) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        ExpressiveSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SettingsSelectorRow(
    title: String,
    description: String,
    icon: ImageVector,
    selectedValueLabel: String,
    options: List<Pair<T, String>>,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color? = null,
    selectedOption: T? = null
) {
    val haptic = LocalHapticFeedback.current
    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.M3_EXPRESSIVE && false
    val glowIntensity = LocalGlowIntensity.current
    val iconColor = if (isSpaceTerminal) Color(0xFF46C2B4) else (accentColor ?: MaterialTheme.colorScheme.primary)
    val iconBgColor = if (isSpaceTerminal) Color(0xFF46C2B4).copy(alpha = 0.1f) else (accentColor ?: MaterialTheme.colorScheme.primary).copy(alpha = 0.15f)
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                expanded = true
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = iconBgColor,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (isSpaceTerminal) Color(0xFFE2E8F0) else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSpaceTerminal) Color(0xFF94A3B8) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Box {
            Surface(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    expanded = true
                },
                color = if (isSpaceTerminal) Color(0xFFFF7E6B).copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                shape = if (isSpaceTerminal) MaterialTheme.shapes.small else MaterialTheme.shapes.medium,
                border = if (isSpaceTerminal) BorderStroke(1.dp, Color(0xFFFF7E6B)) else null
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = selectedValueLabel,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = if (isSpaceTerminal) Color(0xFFFF7E6B) else MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = null,
                        tint = if (isSpaceTerminal) Color(0xFFFF7E6B) else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            if (expanded) {
                val sheetBgColor = if (isSpaceTerminal) Color(0xFF0C1020) else MaterialTheme.colorScheme.surfaceContainerLow
                val sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                val selectorSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

                ModalBottomSheet(
                    onDismissRequest = { expanded = false },
                    sheetState = selectorSheetState,
                    containerColor = sheetBgColor,
                    shape = sheetShape,
                    modifier = Modifier.statusBarsPadding(),
                    dragHandle = {
                        Box(
                            modifier = Modifier
                                .padding(top = 12.dp, bottom = 8.dp)
                                .width(36.dp)
                                .height(4.dp)
                                .background(
                                    color = if (isSpaceTerminal) Color(0xFF46C2B4).copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f),
                                    shape = CircleShape
                                )
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                            .verticalScroll(rememberScrollState())
                            .navigationBarsPadding()
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (isSpaceTerminal) Color(0xFFE2E8F0) else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = if (description.isNotEmpty()) 4.dp else 16.dp)
                        )
                        if (description.isNotEmpty()) {
                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isSpaceTerminal) Color(0xFF94A3B8) else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        options.forEach { (value, label) ->
                            val normSelected = selectedValueLabel.trim().replace("_", " ").lowercase()
                            val normLabel = label.trim().replace("_", " ").lowercase()

                            val isSelected = when {
                                selectedOption != null && value == selectedOption -> true
                                selectedValueLabel == label -> true
                                normSelected == normLabel -> true
                                normLabel.startsWith(normSelected) || normSelected.startsWith(normLabel) -> true
                                normLabel.contains(normSelected) || normSelected.contains(normLabel) -> true
                                else -> false
                            }
                            val optionBg = if (isSelected) {
                                if (isSpaceTerminal) Color(0xFF46C2B4).copy(alpha = 0.25f) else MaterialTheme.colorScheme.primaryContainer
                            } else {
                                if (isSpaceTerminal) Color(0xFF1E293B).copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.45f)
                            }

                            val optionTextColor = if (isSelected) {
                                if (isSpaceTerminal) Color(0xFF46C2B4) else MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                if (isSpaceTerminal) Color(0xFF94A3B8) else MaterialTheme.colorScheme.onSurface
                            }

                            val itemShape = RoundedCornerShape(20.dp)

                            Surface(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onOptionSelected(value)
                                    expanded = false
                                },
                                shape = itemShape,
                                color = optionBg,
                                border = if (isSelected && isSpaceTerminal) BorderStroke(1.dp, Color(0xFF46C2B4)) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = optionTextColor
                                    )
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Outlined.Check,
                                            contentDescription = "Selected",
                                            tint = optionTextColor,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsActionRow(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    accentColor: Color? = null
) {
    val haptic = LocalHapticFeedback.current
    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.M3_EXPRESSIVE && false
    val iconColor = if (isSpaceTerminal) Color(0xFF46C2B4) else (accentColor ?: MaterialTheme.colorScheme.primary)
    val iconBgColor = if (isSpaceTerminal) Color(0xFF46C2B4).copy(alpha = 0.1f) else (accentColor ?: MaterialTheme.colorScheme.primary).copy(alpha = 0.15f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = iconBgColor,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (isSpaceTerminal) Color(0xFFE2E8F0) else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSpaceTerminal) Color(0xFF94A3B8) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (actionLabel != null) {
            Spacer(modifier = Modifier.width(12.dp))
            Surface(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                },
                color = if (isSpaceTerminal) Color(0xFFFF7E6B).copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                shape = if (isSpaceTerminal) MaterialTheme.shapes.small else MaterialTheme.shapes.medium,
                border = if (isSpaceTerminal) BorderStroke(1.dp, Color(0xFFFF7E6B)) else null
            ) {
                Text(
                    text = actionLabel,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = if (isSpaceTerminal) Color(0xFFFF7E6B) else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun PresetVisualPreview(preset: StylePreset, isSpaceTerminal: Boolean) {
    val previewShape = MaterialTheme.shapes.medium
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(previewShape)
            .border(
                1.dp,
                if (isSpaceTerminal) Color(0xFF46C2B4).copy(alpha = 0.2f)
                else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                previewShape
            )
    ) {
        when (preset.name) {
            "Clean Material" -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFEDE9FE),
                                    Color(0xFFE0E7FF),
                                    Color(0xFFF5F3FF)
                                )
                            )
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF8B5CF6),
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.Palette,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
                            Box(modifier = Modifier.width(60.dp).height(8.dp).background(Color(0xFF1E1B4B), MaterialTheme.shapes.extraSmall))
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(modifier = Modifier.width(40.dp).height(6.dp).background(Color(0xFF1E1B4B).copy(alpha = 0.4f), MaterialTheme.shapes.extraSmall))
                        }
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = Color(0xFFD946EF),
                            modifier = Modifier.width(50.dp).height(18.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("Active", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            "Tactical Cockpit" -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0C1020))
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeColor = Color(0xFF46C2B4).copy(alpha = 0.12f)
                        val gridSpace = 12.dp.toPx()
                        var x = 0f
                        while (x < size.width) {
                            drawLine(strokeColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
                            x += gridSpace
                        }
                        var y = 0f
                        while (y < size.height) {
                            drawLine(strokeColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
                            y += gridSpace
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xFF46C2B4).copy(alpha = 0.08f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "[ MECHA_SYS: OK ]",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF46C2B4)
                        )
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color(0xFFFF7E6B), CircleShape)
                        )
                    }
                }
            }
            "Blueprint Modern" -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0F52BA))
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeColor = Color(0xFF60A5FA).copy(alpha = 0.25f)
                        val gridSpace = 12.dp.toPx()
                        var x = 0f
                        while (x < size.width) {
                            drawLine(strokeColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 0.7f)
                            x += gridSpace
                        }
                        var y = 0f
                        while (y < size.height) {
                            drawLine(strokeColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 0.7f)
                            y += gridSpace
                        }
                    }
                    Canvas(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                        val drawColor = Color(0xFF93C5FD)
                        drawCircle(drawColor, radius = 20f, style = Stroke(width = 1.dp.toPx()))
                        drawRect(drawColor, topLeft = Offset(size.width / 2 - 24f, size.height / 2 - 12f), size = androidx.compose.ui.geometry.Size(48f, 24f), style = Stroke(width = 1.dp.toPx()))
                        drawLine(drawColor, Offset(0f, size.height / 2), Offset(size.width, size.height / 2), strokeWidth = 0.8f)
                        drawLine(drawColor, Offset(size.width / 2, 0f), Offset(size.width / 2, size.height), strokeWidth = 0.8f)
                    }
                }
            }
            "Serif Editorial" -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFAF7F2))
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Aa",
                            fontFamily = FontFamily.Serif,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF292524)
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(modifier = Modifier.fillMaxWidth(0.9f).height(2.dp).background(Color(0xFF78716C)))
                            Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFF78716C)))
                            Box(modifier = Modifier.fillMaxWidth(0.6f).height(2.dp).background(Color(0xFF78716C)))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PresetSpecsTags(preset: StylePreset, isSpaceTerminal: Boolean) {
    val tagBg = if (isSpaceTerminal) {
        Color(0xFF1E293B).copy(alpha = 0.4f)
    } else {
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
    }
    val tagBorderColor = if (isSpaceTerminal) {
        Color(0xFF46C2B4).copy(alpha = 0.2f)
    } else {
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
    }
    val tagTextColor = if (isSpaceTerminal) {
        Color(0xFF46C2B4)
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }
    val tagShape = MaterialTheme.shapes.small

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .background(tagBg, tagShape)
                .border(1.dp, tagBorderColor, tagShape)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = preset.appFont.name.lowercase().replaceFirstChar { it.uppercase() },
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = tagTextColor
            )
        }

        Box(
            modifier = Modifier
                .background(tagBg, tagShape)
                .border(1.dp, tagBorderColor, tagShape)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = when (preset.glowIntensity) {
                    GlowIntensity.OFF -> "No Glow"
                    GlowIntensity.SUBTLE -> "Subtle"
                    GlowIntensity.NEON -> "Neon Glow"
                    GlowIntensity.PULSING -> "Pulse Glow"
                },
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = tagTextColor
            )
        }

        if (preset.touchSynesthesia != TouchSynesthesia.OFF) {
            Box(
                modifier = Modifier
                    .background(tagBg, tagShape)
                    .border(1.dp, tagBorderColor, tagShape)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = when (preset.touchSynesthesia) {
                        TouchSynesthesia.SUBTLE -> "Haptics"
                        TouchSynesthesia.MECHANICAL -> "Mech Tap"
                        TouchSynesthesia.CASSETTE_CLICK -> "Tape Click"
                        else -> ""
                    },
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = tagTextColor
                )
            }
        }
    }
}

data class StylePreset(
    val emoji: String,
    val name: String,
    val description: String,
    val themeStyle: ThemeStyle,
    val appFont: AppFont,
    val backdropPattern: BackdropPattern,
    val glowIntensity: GlowIntensity,
    val crtFilterEnabled: Boolean,
    val touchSynesthesia: TouchSynesthesia,
    val darkThemePreference: DarkThemePreference,
    val useDynamicColor: Boolean
)

val stylePresets = listOf(
    StylePreset(
        emoji = "🎭",
        name = "Clean Material",
        description = "Modern Material 3 styling with Nunito typeface, subtle glow, and dynamic wallpaper color tints.",
        themeStyle = ThemeStyle.M3_EXPRESSIVE,
        appFont = AppFont.NUNITO,
        backdropPattern = BackdropPattern.NONE,
        glowIntensity = GlowIntensity.SUBTLE,
        crtFilterEnabled = false,
        touchSynesthesia = TouchSynesthesia.SUBTLE,
        darkThemePreference = DarkThemePreference.SYSTEM,
        useDynamicColor = true
    ),

    StylePreset(
        emoji = "📐",
        name = "Blueprint Modern",
        description = "Architectural style featuring Outfit typography, Blueprint Grid backdrop, pulsing glow aura, and mechanical click.",
        themeStyle = ThemeStyle.M3_EXPRESSIVE,
        appFont = AppFont.OUTFIT,
        backdropPattern = BackdropPattern.BLUEPRINT_GRID,
        glowIntensity = GlowIntensity.PULSING,
        crtFilterEnabled = false,
        touchSynesthesia = TouchSynesthesia.MECHANICAL,
        darkThemePreference = DarkThemePreference.SYSTEM,
        useDynamicColor = false
    ),
    StylePreset(
        emoji = "📜",
        name = "Serif Editorial",
        description = "Classic print layout using Playfair typography, clean background, dark mode preference, and no glow.",
        themeStyle = ThemeStyle.M3_EXPRESSIVE,
        appFont = AppFont.PLAYFAIR,
        backdropPattern = BackdropPattern.NONE,
        glowIntensity = GlowIntensity.OFF,
        crtFilterEnabled = false,
        touchSynesthesia = TouchSynesthesia.OFF,
        darkThemePreference = DarkThemePreference.DARK,
        useDynamicColor = false
    )
)



