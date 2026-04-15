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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.personal.kakeibox.data.preferences.DarkThemePreference
import com.personal.kakeibox.data.preferences.NavBarStyle
import com.personal.kakeibox.ui.components.ExpressiveTab
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

            // Row 1: Theme Pair (System & Dynamic)
            Row(
                modifier = Modifier.fillMaxWidth().height(160.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Dark Mode Card
                BentoCard(
                    modifier = Modifier.weight(1f),
                    title = "Theme",
                    icon = when(themeSettings.darkThemePreference) {
                        DarkThemePreference.DARK -> Icons.Outlined.DarkMode
                        DarkThemePreference.LIGHT -> Icons.Outlined.LightMode
                        else -> Icons.Outlined.AutoMode
                    },
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        val next = when(themeSettings.darkThemePreference) {
                            DarkThemePreference.SYSTEM -> DarkThemePreference.LIGHT
                            DarkThemePreference.LIGHT -> DarkThemePreference.DARK
                            DarkThemePreference.DARK -> DarkThemePreference.SYSTEM
                        }
                        viewModel.setDarkThemePreference(next)
                    }
                ) {
                    Text(
                        text = themeSettings.darkThemePreference.name.lowercase(Locale.ROOT)
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Dynamic Color Card
                BentoCard(
                    modifier = Modifier.weight(1f),
                    title = "Dynamic",
                    icon = Icons.Outlined.Palette,
                    enabled = dynamicSupported
                ) {
                    Switch(
                        checked = themeSettings.useDynamicColor,
                        onCheckedChange = { viewModel.setUseDynamicColor(it) },
                        enabled = dynamicSupported,
                        modifier = Modifier.graphicsLayer {
                            scaleX = 0.8f
                            scaleY = 0.8f
                        }
                    )
                }
            }

            // Row 2: Nav Style (Wide Bento)
            BentoCard(
                modifier = Modifier.fillMaxWidth(),
                title = "Navigation Layout",
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
                BentoCard(
                    modifier = Modifier.weight(1f),
                    title = "Backups",
                    icon = Icons.Outlined.CloudUpload,
                    onClick = { /* TODO */ }
                )
                BentoCard(
                    modifier = Modifier.weight(1f),
                    title = "Privacy",
                    icon = Icons.Outlined.Security,
                    onClick = { /* TODO */ }
                )
            }
        }
    }
}

@Composable
fun BentoCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .then(if (onClick != null && enabled) Modifier.clickable { onClick() } else Modifier),
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = if (enabled) 1f else 0.4f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                content()
            }
        }
    }
}
