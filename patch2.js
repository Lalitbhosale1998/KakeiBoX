const fs = require('fs');

const path = 'app/src/main/java/com/lalit/kakeibox/ui/theme/KakeiboXApp.kt';
let content = fs.readFileSync(path, 'utf8');

// 1. Add missing imports
const importsToAdd = `
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.width
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
`;

if (!content.includes('import androidx.compose.ui.draw.shadow')) {
    const importInsertPos = content.indexOf('import androidx.compose.foundation.background');
    content = content.substring(0, importInsertPos) + importsToAdd + content.substring(importInsertPos);
}

// 2. Replace the bottom bar block
const startIndex = content.indexOf('if (!isExpandedScreen) {');
if (startIndex === -1) {
    console.error("Could not find start index");
    process.exit(1);
}

// We need to find the matching '}' for 'if (!isExpandedScreen) {'
// Let's do it by counting braces
let openBraces = 0;
let endIndex = -1;
let foundStart = false;

for (let i = startIndex; i < content.length; i++) {
    if (content[i] === '{') {
        openBraces++;
        foundStart = true;
    } else if (content[i] === '}') {
        openBraces--;
        if (foundStart && openBraces === 0) {
            endIndex = i + 1;
            break;
        }
    }
}

if (endIndex === -1) {
    console.error("Could not find matching closing brace");
    process.exit(1);
}

const before = content.substring(0, startIndex);
const after = content.substring(endIndex);

const newNavCode = `if (!isExpandedScreen) {
                    if (themeSettings.navBarStyle == NavBarStyle.FULL_WIDTH) {
                        // Concept A: The Bumping Balloons
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .windowInsetsPadding(WindowInsets.navigationBars)
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectableGroup(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val midIndex = (bottomNavItems.size + 1) / 2
                                bottomNavItems.forEachIndexed { index, item ->
                                    if (index == midIndex) {
                                        CentralActionButton(
                                            currentRoute = currentRoute,
                                            themeViewModel = viewModel,
                                            haptic = haptic,
                                            scale = actionButtonScale,
                                            weight = actionButtonWeight,
                                            brush = Brush.linearGradient(listOf(actionColorStart, actionColorEnd)),
                                            iconColor = actionIconColor
                                        )
                                    }
                                    
                                    val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                                    
                                    val itemWeight by animateFloatAsState(
                                        targetValue = if (isSelected) 3.5f else 1.0f,
                                        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessLow),
                                        label = "balloon_weight"
                                    )
                                    val containerScale by animateFloatAsState(
                                        targetValue = if (isSelected) 1.15f else 0.9f,
                                        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow),
                                        label = "balloon_scale"
                                    )
                                    
                                    val bgColor = if (isSelected) {
                                        Brush.linearGradient(listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6)))
                                    } else {
                                        Brush.linearGradient(listOf(MaterialTheme.colorScheme.surfaceContainerHighest, MaterialTheme.colorScheme.surfaceContainerHighest))
                                    }
                                    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    
                                    Box(
                                        modifier = Modifier
                                            .weight(itemWeight)
                                            .height(if (isSelected) 64.dp else 56.dp)
                                            .graphicsLayer {
                                                scaleX = containerScale
                                                scaleY = containerScale
                                            }
                                            .shadow(if (isSelected) 12.dp else 4.dp, RoundedCornerShape(50))
                                            .clip(RoundedCornerShape(50))
                                            .background(bgColor)
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null,
                                                onClick = {
                                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                                    navController.navigate(item.route) {
                                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = if (isSelected) item.selectedIcon else item.icon,
                                                contentDescription = stringResource(item.labelRes),
                                                tint = contentColor,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            if (isSelected) {
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = stringResource(item.labelRes),
                                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                                    color = contentColor,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (themeSettings.navBarStyle == NavBarStyle.GOOEY) {
                        // Concept B: The Gooey Slingshot
                        val tabBounds = remember { mutableStateListOf<Pair<Float, Float>>() }
                        val selectedIndex = remember(currentDestination, bottomNavItems) {
                            bottomNavItems.indexOfFirst { item ->
                                currentDestination?.hierarchy?.any { it.route == item.route } == true
                            }.coerceAtLeast(0)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .windowInsetsPadding(WindowInsets.navigationBars)
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                .height(72.dp)
                                .shadow(8.dp, RoundedCornerShape(percent = 50))
                                .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(percent = 50))
                        ) {
                            // The Gooey Indicator
                            if (tabBounds.size == bottomNavItems.size) {
                                val targetBounds = tabBounds.getOrNull(selectedIndex) ?: Pair(0f, 0f)
                                val animatedX by animateFloatAsState(
                                    targetValue = targetBounds.first,
                                    animationSpec = spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessLow),
                                    label = "gooey_x"
                                )
                                val animatedWidth by animateFloatAsState(
                                    targetValue = targetBounds.second,
                                    animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow),
                                    label = "gooey_width"
                                )
                                val distance = targetBounds.first - animatedX
                                val absDistance = Math.abs(distance)
                                val stretchX = 1f + (absDistance / 100f).coerceAtMost(1.5f)
                                val squashY = 1f - (absDistance / 300f).coerceAtMost(0.4f)
                                
                                Box(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .offset { androidx.compose.ui.unit.IntOffset(animatedX.toInt(), 0) }
                                        .width(with(androidx.compose.ui.platform.LocalDensity.current) { animatedWidth.toDp() })
                                        .fillMaxHeight()
                                        .graphicsLayer {
                                            scaleX = stretchX
                                            scaleY = squashY
                                            transformOrigin = TransformOrigin(if (distance > 0) 1f else 0f, 0.5f)
                                        }
                                        .background(Brush.linearGradient(listOf(Color(0xFFFF3366), Color(0xFFFF9933))), RoundedCornerShape(percent = 50))
                                )
                            }
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .selectableGroup()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val midIndex = (bottomNavItems.size + 1) / 2
                                bottomNavItems.forEachIndexed { index, item ->
                                    if (index == midIndex) {
                                        CentralActionButton(
                                            currentRoute = currentRoute,
                                            themeViewModel = viewModel,
                                            haptic = haptic,
                                            scale = actionButtonScale,
                                            weight = actionButtonWeight,
                                            brush = Brush.linearGradient(listOf(actionColorStart, actionColorEnd)),
                                            iconColor = actionIconColor
                                        )
                                    }
                                    
                                    val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                                    val iconTranslationY by animateFloatAsState(
                                        targetValue = if (isSelected) -12f else 0f,
                                        animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessLow),
                                        label = "gooey_icon_y"
                                    )
                                    val iconRotation by animateFloatAsState(
                                        targetValue = if (isSelected) 360f else 0f,
                                        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
                                        label = "gooey_icon_rot"
                                    )
                                    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(56.dp)
                                            .onGloballyPositioned { coordinates ->
                                                val parent = coordinates.parentLayoutCoordinates
                                                if (parent != null) {
                                                    val localPos = parent.localPositionOf(coordinates, androidx.compose.ui.geometry.Offset.Zero)
                                                    val newBounds = Pair(localPos.x, coordinates.size.width.toFloat())
                                                    if (index >= tabBounds.size) {
                                                        tabBounds.add(newBounds)
                                                    } else if (tabBounds[index] != newBounds) {
                                                        tabBounds[index] = newBounds
                                                    }
                                                }
                                            }
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null,
                                                onClick = {
                                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                                    navController.navigate(item.route) {
                                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                }
                                            ),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isSelected) item.selectedIcon else item.icon,
                                            contentDescription = stringResource(item.labelRes),
                                            tint = contentColor,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .graphicsLayer {
                                                    translationY = iconTranslationY
                                                    rotationZ = iconRotation
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }`;

fs.writeFileSync(path, before + newNavCode + after);
console.log("Patched successfully!");
