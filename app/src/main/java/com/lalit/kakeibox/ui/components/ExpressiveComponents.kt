package com.personal.kakeibox.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import com.personal.kakeibox.data.preferences.CardShapePreference
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.kakeibox.util.DateUtils
import com.personal.kakeibox.data.preferences.ThemeStyle
import com.personal.kakeibox.ui.theme.LocalThemeStyle
import com.personal.kakeibox.ui.theme.terminalScanlines
import com.personal.kakeibox.ui.theme.terminalButton
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import com.personal.kakeibox.ui.theme.LocalTouchSynesthesia
import com.personal.kakeibox.data.preferences.TouchSynesthesia
import com.personal.kakeibox.ui.theme.SynthClickGenerator
import com.personal.kakeibox.ui.theme.LocalGlowIntensity
import com.personal.kakeibox.ui.theme.glow
import com.personal.kakeibox.data.preferences.GlowIntensity
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.foundation.layout.RowScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.ui.composed
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.random.Random

import com.personal.kakeibox.ui.theme.ExpressivePhysics

fun Modifier.elasticClick(
    enabled: Boolean = true,
    hapticType: HapticFeedbackType = HapticFeedbackType.LongPress,
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit
): Modifier = composed {
    val haptic = LocalHapticFeedback.current
    val touchSynesthesia = LocalTouchSynesthesia.current
    val actualInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isPressed by actualInteractionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.98f else 1.0f,
        animationSpec = ExpressivePhysics.fluidBouncy(),
        label = "elastic_scale"
    )
    
    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = actualInteractionSource,
            indication = LocalIndication.current,
            enabled = enabled
        ) {
            haptic.performHapticFeedback(hapticType)
            when (touchSynesthesia) {
                TouchSynesthesia.OFF -> {}
                TouchSynesthesia.SUBTLE -> {
                    SynthClickGenerator.playClick(frequency = 800f, durationMs = 30)
                }
                TouchSynesthesia.CASSETTE_CLICK -> {
                    SynthClickGenerator.playClick(frequency = 150f, durationMs = 40)
                }
                TouchSynesthesia.MECHANICAL -> {
                    SynthClickGenerator.playClick(frequency = 1800f, durationMs = 25)
                }
            }
            onClick()
        }
}

@Composable
fun ExpressivePeriodSelector(
    selectedMonth: Int,
    selectedYear: Int,
    onMonthChange: (Int) -> Unit,
    onYearChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val months = remember {
        (1..12).map { DateUtils.getShortMonthName(it) }
    }
    val years = remember {
        DateUtils.getYearRange().map { it.toString() }
    }

    val selectedMonthIndex = (selectedMonth - 1).coerceIn(0, 11)
    val selectedYearIndex = years.indexOf(selectedYear.toString()).coerceAtLeast(0)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Month Wheel Picker
        ExpressiveWheelPicker(
            items = months,
            selectedIndex = selectedMonthIndex,
            onItemSelected = { index ->
                onMonthChange(index + 1)
            },
            modifier = Modifier.weight(1.2f)
        )

        // Year Wheel Picker
        ExpressiveWheelPicker(
            items = years,
            selectedIndex = selectedYearIndex,
            onItemSelected = { index ->
                onYearChange(years[index].toInt())
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ExpressiveSnackbarHost(hostState: SnackbarHostState) {
    val haptic = LocalHapticFeedback.current

    SnackbarHost(hostState) { data ->
        LaunchedEffect(data) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }

        val isError = data.visuals.message.contains("Error", ignoreCase = true) || 
                      data.visuals.message.contains("Failed", ignoreCase = true)
        val isDelete = data.visuals.message.contains("Delete", ignoreCase = true) || 
                       data.visuals.message.contains("Removed", ignoreCase = true)

        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = when {
                    isError -> MaterialTheme.colorScheme.errorContainer
                    isDelete -> MaterialTheme.colorScheme.surfaceVariant
                    else -> MaterialTheme.colorScheme.primaryContainer
                },
                contentColor = when {
                    isError -> MaterialTheme.colorScheme.onErrorContainer
                    isDelete -> MaterialTheme.colorScheme.onSurfaceVariant
                    else -> MaterialTheme.colorScheme.onPrimaryContainer
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = when {
                        isError -> Icons.Outlined.ErrorOutline
                        isDelete -> Icons.Outlined.Delete
                        else -> Icons.Outlined.CheckCircle
                    },
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = when {
                        isError -> MaterialTheme.colorScheme.error
                        isDelete -> MaterialTheme.colorScheme.onSurfaceVariant
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
                Text(
                    text = data.visuals.message,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                data.visuals.actionLabel?.let { action ->
                    TextButton(onClick = { data.performAction() }) {
                        Text(
                            text = action,
                            fontWeight = FontWeight.Black,
                            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpressiveCategoryToggle(
    selectedCategory: String, // "NEED" or "WANT"
    onCategoryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val isNeed = selectedCategory == "NEED"
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(8.dp)
    ) {
        // Need Button
        val needWeight by animateFloatAsState(
            targetValue = if (isNeed) 1.5f else 1f,
            animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
            label = "need_weight"
        )
        val needBg by animateColorAsState(
            targetValue = if (isNeed) MaterialTheme.colorScheme.error else Color.Transparent,
            label = "need_bg"
        )
        
        Box(
            modifier = Modifier
                .weight(needWeight)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(needBg)
                .clickable { 
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onCategoryChange("NEED") 
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "NEED",
                fontWeight = FontWeight.Black,
                color = if (isNeed) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Want Button
        val wantWeight by animateFloatAsState(
            targetValue = if (!isNeed) 1.5f else 1f,
            animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
            label = "want_weight"
        )
        val wantBg by animateColorAsState(
            targetValue = if (!isNeed) MaterialTheme.colorScheme.tertiary else Color.Transparent,
            label = "want_bg"
        )

        Box(
            modifier = Modifier
                .weight(wantWeight)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(wantBg)
                .clickable { 
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onCategoryChange("WANT") 
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "WANT",
                fontWeight = FontWeight.Black,
                color = if (!isNeed) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ExpressiveTab(
    text: String,
    isSelected: Boolean,
    selectedColor: Color,
    modifier: Modifier = Modifier,
    selectedTextColor: Color = Color.Unspecified,
    icon: ImageVector? = null,
    shapeType: String = "pill",
    fontFamily: FontFamily? = null,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) selectedColor else MaterialTheme.colorScheme.surfaceContainerHigh,
        label = "bgColor"
    )
    val txtColor by animateColorAsState(
        targetValue = if (isSelected) selectedTextColor else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "txtColor"
    )
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        label = "scale"
    )

    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.M3_EXPRESSIVE && false
    val targetRadius = if (isSpaceTerminal) {
        if (isSelected) 12 else 8
    } else {
        if (isSelected) 24 else 12 // Maps to large (24) and medium (12)
    }

    // Morphing Shape logic: Squircle (16dp) to Custom Expressive Shapes
    val cornerRadius by animateIntAsState(
        targetValue = targetRadius,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "corner_morph"
    )

    val shape = when (shapeType.lowercase()) {
        "slanted" -> RoundedCornerShape(
            topStart = cornerRadius.dp,
            bottomEnd = cornerRadius.dp,
            topEnd = if (isSelected) 4.dp else cornerRadius.dp,
            bottomStart = if (isSelected) 4.dp else cornerRadius.dp
        )
        "arch" -> RoundedCornerShape(
            topStart = cornerRadius.dp,
            topEnd = cornerRadius.dp,
            bottomStart = if (isSelected) 4.dp else cornerRadius.dp,
            bottomEnd = if (isSelected) 4.dp else cornerRadius.dp
        )
        "clamshell" -> RoundedCornerShape(
            topStart = cornerRadius.dp,
            bottomStart = cornerRadius.dp,
            topEnd = if (isSelected) 4.dp else cornerRadius.dp,
            bottomEnd = if (isSelected) 4.dp else cornerRadius.dp
        )
        else -> RoundedCornerShape(cornerRadius.dp) // Pill
    }

    val border = if (isSpaceTerminal) {
        BorderStroke(
            width = 1.5.dp,
            color = if (isSelected) Color(0xFFFF7E6B) else Color(0xFF46C2B4).copy(alpha = 0.5f)
        )
    } else {
        null
    }

    Surface(
        modifier = modifier
            .height(56.dp)
            .elasticClick(
                hapticType = HapticFeedbackType.TextHandleMove,
                onClick = onClick
            )
            .graphicsLayer(scaleX = scale, scaleY = scale),
        shape = shape,
        color = bgColor,
        contentColor = txtColor,
        border = border
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                AnimatedVisibility(
                    visible = isSelected,
                    enter = fadeIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                            expandHorizontally(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy), expandFrom = Alignment.Start),
                    exit = fadeOut(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) +
                           shrinkHorizontally(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy), shrinkTowards = Alignment.Start)
                ) {
                    Text(
                        text = text,
                        style = if (fontFamily != null) MaterialTheme.typography.labelLarge.copy(fontFamily = fontFamily) else MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(start = 6.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                }
            } else {
                Text(
                    text = text,
                    style = if (fontFamily != null) MaterialTheme.typography.labelLarge.copy(fontFamily = fontFamily) else MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun ExpressiveEmptyState(
    message: String,
    modifier: Modifier = Modifier,
    icon: String = "✨",
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var x by remember { mutableStateOf(100f) }
    var y by remember { mutableStateOf(100f) }
    var vx by remember { mutableStateOf(0f) }
    var vy by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    
    val haptic = LocalHapticFeedback.current
    
    // Tap particle burst list
    var particles by remember { mutableStateOf(listOf<Particle>()) }
    
    // Radius of our bouncing emoji
    val radiusPx = 50f
    
    // Physics game loop
    LaunchedEffect(isDragging, size) {
        if (size.width == 0 || size.height == 0) return@LaunchedEffect
        // Center initially if x, y are defaults
        if (x == 100f && y == 100f) {
            x = size.width / 2f
            y = size.height / 3f
        }
        
        while (true) {
            if (!isDragging) {
                // Apply gravity
                vy += 0.4f
                // Apply air resistance (friction)
                vx *= 0.98f
                vy *= 0.98f
                
                // Update position
                x += vx
                y += vy
                
                // Left border collision
                if (x - radiusPx < 0) {
                    x = radiusPx
                    vx = -vx * 0.75f
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
                // Right border collision
                if (x + radiusPx > size.width) {
                    x = size.width - radiusPx
                    vx = -vx * 0.75f
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
                // Top border collision
                if (y - radiusPx < 0) {
                    y = radiusPx
                    vy = -vy * 0.75f
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
                // Bottom border collision (ground)
                if (y + radiusPx > size.height) {
                    y = size.height - radiusPx
                    vy = -vy * 0.75f
                    // If moving very slow vertically, stop gravity pull
                    if (Math.abs(vy) < 1f) vy = 0f
                    vx *= 0.9f // extra friction on ground
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
            }
            
            // Update particles
            if (particles.isNotEmpty()) {
                particles = particles.mapNotNull { p ->
                    val newLife = p.life - 0.05f
                    if (newLife <= 0f) null
                    else p.copy(
                        x = p.x + p.vx,
                        y = p.y + p.vy,
                        vy = p.vy + 0.1f, // gravity on particles
                        life = newLife
                    )
                }
            }
            
            delay(16) // ~60fps
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Dynamic Canvas for physics play
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .onSizeChanged { size = it }
                .pointerInput(size) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val dx = offset.x - x
                            val dy = offset.y - y
                            if (dx * dx + dy * dy < (radiusPx * 3) * (radiusPx * 3)) {
                                isDragging = true
                                vx = 0f
                                vy = 0f
                            }
                        },
                        onDrag = { change, dragAmount ->
                            if (isDragging) {
                                change.consume()
                                x = (x + dragAmount.x).coerceIn(radiusPx, size.width.toFloat() - radiusPx)
                                y = (y + dragAmount.y).coerceIn(radiusPx, size.height.toFloat() - radiusPx)
                                vx = dragAmount.x * 0.8f
                                vy = dragAmount.y * 0.8f
                            }
                        },
                        onDragEnd = {
                            isDragging = false
                        },
                        onDragCancel = {
                            isDragging = false
                        }
                    )
                }
                .pointerInput(size) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.type == PointerEventType.Press) {
                                val pressPoint = event.changes.first().position
                                val dx = pressPoint.x - x
                                val dy = pressPoint.y - y
                                if (dx * dx + dy * dy < (radiusPx * 2) * (radiusPx * 2)) {
                                    // Kick emoji upwards & sidewards
                                    vx = Random.nextFloat() * 12f - 6f
                                    vy = -15f
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    // Generate particles
                                    val newParticles = (1..15).map {
                                        val angle = Random.nextFloat() * 2 * Math.PI
                                        val speed = Random.nextFloat() * 6f + 2f
                                        Particle(
                                            x = x,
                                            y = y,
                                            vx = (Math.cos(angle) * speed).toFloat(),
                                            vy = (Math.sin(angle) * speed).toFloat(),
                                            color = color,
                                            life = 1.0f
                                        )
                                    }
                                    particles = particles + newParticles
                                }
                            }
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Draw particles
                particles.forEach { p ->
                    drawCircle(
                        color = p.color.copy(alpha = p.life),
                        radius = 6f * p.life,
                        center = Offset(p.x, p.y)
                    )
                }
                
                // Draw floating emoji / icon
                drawIntoCanvas { canvas ->
                    val paint = android.graphics.Paint().apply {
                        textSize = 100f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    val textHeightOffset = (paint.descent() + paint.ascent()) / 2
                    canvas.nativeCanvas.drawText(icon, x, y - textHeightOffset, paint)
                }
                
                // Draw a soft physical shadow bubble below the ball if it is near the ground
                if (size.height > 0) {
                    val groundDist = size.height - radiusPx - y
                    if (groundDist < 80f) {
                        val shadowScale = (1f - (groundDist / 80f)).coerceIn(0f, 1f)
                        drawOval(
                            color = color.copy(alpha = 0.15f * shadowScale),
                            topLeft = Offset(x - radiusPx * shadowScale, size.height - 8f),
                            size = androidx.compose.ui.geometry.Size(radiusPx * 2 * shadowScale, 6f)
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            color = color
        )
    }
}

data class Particle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val color: Color,
    val life: Float
)

@Composable
fun BentoCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: ImageVector,
    enabled: Boolean = true,
    isActive: Boolean = false,
    activeContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    activeContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    idleContainerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    idleContentColor: Color = MaterialTheme.colorScheme.onSurface,
    cardShapePreference: CardShapePreference = CardShapePreference.PILL,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current
    
    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> idleContainerColor.copy(alpha = 0.6f)
            isActive -> activeContainerColor
            else -> idleContainerColor
        },
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "bento_bg"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            !enabled -> idleContentColor.copy(alpha = 0.38f)
            isActive -> activeContentColor
            else -> idleContentColor
        },
        label = "bento_content"
    )

    // Advanced Micro-interactions: Dynamic icon behavior
    val infiniteTransition = rememberInfiniteTransition(label = "bento_icon_infinite")
    
    // Jiggle for reminders/notifications
    val jiggleRotation by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "jiggle_anim"
    )

    // Continuous rotation for settings/gears
    val continuousRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation_anim"
    )

    val finalIconRotation by animateFloatAsState(
        targetValue = when {
            !isActive -> 0f
            title.contains("Reminder", ignoreCase = true) -> jiggleRotation
            title.contains("App Theme", ignoreCase = true) || title.contains("Settings", ignoreCase = true) -> continuousRotation
            else -> 12f // Default slight tilt for active state
        },
        animationSpec = if (!isActive) spring(Spring.DampingRatioMediumBouncy) else tween(0), // Direct follow if active
        label = "icon_rotation"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isActive) 1.2f else 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        label = "icon_scale"
    )

    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.M3_EXPRESSIVE && false
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val targetRadius = if (isSpaceTerminal) {
        if (isPressed) 8 else 12
    } else {
        if (isPressed) 12 else 32 // Maps to medium (12) and extraLarge (32)
    }

    val cornerRadius by animateIntAsState(
        targetValue = targetRadius,
        animationSpec = ExpressivePhysics.fluidSnappy(),
        label = "bento_corner_morph"
    )

    val cardShape = cardShapePreference.toShape(isPressed = isPressed)
    val cardBorder = if (isSpaceTerminal) {
        BorderStroke(1.5.dp, if (isActive) Color(0xFFFF7E6B) else Color(0xFF46C2B4).copy(alpha = 0.4f))
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    }
    val iconShape = if (isSpaceTerminal) RoundedCornerShape(6.dp) else RoundedCornerShape(16.dp)

    val glowIntensity = LocalGlowIntensity.current

    Surface(
        modifier = modifier
            .glow(
                color = if (isSpaceTerminal) Color(0xFFFF7E6B) else MaterialTheme.colorScheme.primary,
                intensity = if (isActive) glowIntensity else GlowIntensity.OFF,
                shape = cardShape
            )
            .then(if (onClick != null && enabled) Modifier.elasticClick(
                enabled = enabled,
                hapticType = HapticFeedbackType.LongPress,
                interactionSource = interactionSource,
                onClick = onClick
            ) else Modifier)
            .terminalScanlines(),
        color = backgroundColor,
        contentColor = contentColor,
        shape = cardShape,
        border = cardBorder,
        shadowElevation = 8.dp,
        tonalElevation = 4.dp
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
                    shape = iconShape,
                    color = (if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
                        .copy(alpha = if (enabled) 1f else 0.4f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(8.dp).graphicsLayer {
                            rotationZ = finalIconRotation
                            scaleX = iconScale
                            scaleY = iconScale
                        }
                    )
                }
            }
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Black,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (description != null) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = contentColor.copy(alpha = 0.7f),
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(if (description != null) 8.dp else 4.dp))
                content()
            }
        }
    }
}

@Composable
fun ExpressiveOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh
) {
    val haptic = LocalHapticFeedback.current
    var isFocused by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.03f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "field_scale"
    )
    val elevation by animateDpAsState(
        targetValue = if (isFocused) 8.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "field_elevation"
    )

    Surface(
        shape = shape,
        tonalElevation = elevation,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                shadowElevation = elevation.toPx()
                clip = false
                this.shape = shape
            },
        color = Color.Transparent
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            shape = shape,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { 
                    if (it.isFocused != isFocused) {
                        isFocused = it.isFocused 
                        if (it.isFocused) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    }
                },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = containerColor,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
fun ExpressiveChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    selectedTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    unselectedColor: Color = Color.Unspecified,
    unselectedTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shapeType: String = "pill",
    leadingIcon: ImageVector? = null
) {
    val haptic = LocalHapticFeedback.current
    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.M3_EXPRESSIVE && false
    val glowIntensity = LocalGlowIntensity.current
    
    val resolvedUnselectedColor = if (unselectedColor == Color.Transparent || unselectedColor == Color.Unspecified) {
        if (isSpaceTerminal) {
            Color(0xFF0F1424).copy(alpha = 0.5f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        }
    } else {
        unselectedColor
    }
    
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) selectedColor else resolvedUnselectedColor,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "chip_bg_color"
    )
    val txtColor by animateColorAsState(
        targetValue = if (isSelected) selectedTextColor else unselectedTextColor,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "chip_txt_color"
    )
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1.0f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        label = "chip_scale"
    )
    val cornerRadius by animateIntAsState(
        targetValue = if (isSelected) 24 else 12,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        label = "chip_corner_morph"
    )

    val shape = when (shapeType.lowercase()) {
        "slanted" -> RoundedCornerShape(
            topStart = cornerRadius.dp,
            bottomEnd = cornerRadius.dp,
            topEnd = if (isSelected) 4.dp else cornerRadius.dp,
            bottomStart = if (isSelected) 4.dp else cornerRadius.dp
        )
        "arch" -> RoundedCornerShape(
            topStart = cornerRadius.dp,
            topEnd = cornerRadius.dp,
            bottomStart = if (isSelected) 4.dp else cornerRadius.dp,
            bottomEnd = if (isSelected) 4.dp else cornerRadius.dp
        )
        "clamshell" -> RoundedCornerShape(
            topStart = cornerRadius.dp,
            bottomStart = cornerRadius.dp,
            topEnd = if (isSelected) 4.dp else cornerRadius.dp,
            bottomEnd = if (isSelected) 4.dp else cornerRadius.dp
        )
        else -> RoundedCornerShape(cornerRadius.dp) // Pill
    }

    val borderStroke = if (!isSelected) {
        val strokeColor = if (isSpaceTerminal) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.30f)
        } else {
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        }
        BorderStroke(1.dp, strokeColor)
    } else {
        null
    }

    val chipModifier = modifier
        .height(38.dp)
        .elasticClick(
            hapticType = HapticFeedbackType.TextHandleMove,
            onClick = onClick
        )
        .graphicsLayer(scaleX = scale, scaleY = scale)
        .then(
            if (isSelected && glowIntensity != GlowIntensity.OFF && isSpaceTerminal) {
                Modifier.glow(
                    color = selectedColor,
                    radius = 8.dp,
                    intensity = glowIntensity,
                    shape = shape
                )
            } else Modifier
        )

    Surface(
        modifier = chipModifier,
        shape = shape,
        color = bgColor,
        contentColor = txtColor,
        border = borderStroke
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                val iconScale by animateFloatAsState(
                    targetValue = if (isSelected) 1f else 0f,
                    animationSpec = spring(
                        dampingRatio = 0.6f,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "chip_icon_scale"
                )
                if (iconScale > 0.01f) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .graphicsLayer(scaleX = iconScale, scaleY = iconScale)
                            .size((18 * iconScale).dp)
                            .padding(end = (6 * iconScale).dp),
                        tint = txtColor
                    )
                }
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ExpressiveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    content: @Composable RowScope.() -> Unit
) {
    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.M3_EXPRESSIVE && false
    if (isSpaceTerminal) {
        Surface(
            modifier = modifier
                .height(52.dp)
                .then(if (enabled) Modifier.elasticClick(
                    enabled = enabled,
                    hapticType = HapticFeedbackType.LongPress,
                    onClick = onClick
                ) else Modifier)
                .terminalButton(enabled, backgroundColor),
            color = Color.Transparent,
            shape = MaterialTheme.shapes.medium
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides if (enabled) Color(0xFF0C1020) else Color(0xFF0C1020).copy(alpha = 0.4f)
                ) {
                    content()
                }
            }
        }
    } else {
        Button(
            onClick = onClick,
            modifier = modifier.height(56.dp),
            enabled = enabled,
            shape = MaterialTheme.shapes.large,
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = backgroundColor
            ),
            content = content
        )
    }
}

@Composable
fun RetroProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.primaryContainer
) {
    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.M3_EXPRESSIVE && false
    if (isSpaceTerminal) {
        val infiniteTransition = rememberInfiniteTransition(label = "retro_progress_inf")
        val progressPercent by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "progress_percent"
        )
        Canvas(modifier = modifier.height(8.dp)) {
            val totalSegments = 12
            val spacing = 3.dp.toPx()
            val segmentWidth = (size.width - (totalSegments - 1) * spacing) / totalSegments
            val activeCount = (progressPercent * totalSegments).toInt()
            
            for (i in 0 until totalSegments) {
                val isSegmentActive = i < activeCount
                val segmentColor = if (isSegmentActive) {
                    if (i > totalSegments * 0.8f) Color(0xFFFF7E6B) else color
                } else {
                    trackColor.copy(alpha = 0.15f)
                }
                val x = i * (segmentWidth + spacing)
                drawRoundRect(
                    color = segmentColor,
                    topLeft = Offset(x, 0f),
                    size = androidx.compose.ui.geometry.Size(segmentWidth, size.height),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(1.dp.toPx())
                )
            }
        }
    } else {
        LinearProgressIndicator(
            modifier = modifier,
            color = color,
            trackColor = trackColor
        )
    }
}

@Composable
fun ExpressiveAnimatedIcon(
    icon: ImageVector,
    selectedIcon: ImageVector,
    isSelected: Boolean,
    tint: Color,
    unselectedTint: Color,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "icon_fill_progress"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Draw outlined (unselected) icon with fading out when selected
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = unselectedTint.copy(alpha = (1f - progress).coerceIn(0f, 1f)),
            modifier = Modifier.fillMaxSize()
        )

        // Draw filled (selected) icon with clipping circle reveal and scaling
        Icon(
            imageVector = selectedIcon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = 0.6f + progress * 0.4f
                    scaleY = 0.6f + progress * 0.4f
                    alpha = progress
                    
                    // Circular reveal clip from center
                    clip = true
                    shape = object : Shape {
                        override fun createOutline(
                            size: androidx.compose.ui.geometry.Size,
                            layoutDirection: androidx.compose.ui.unit.LayoutDirection,
                            density: androidx.compose.ui.unit.Density
                        ): androidx.compose.ui.graphics.Outline {
                            val path = androidx.compose.ui.graphics.Path().apply {
                                val centerX = size.width / 2f
                                val centerY = size.height / 2f
                                val r = (size.width * 0.8f) * progress
                                addOval(
                                    androidx.compose.ui.geometry.Rect(
                                        left = centerX - r,
                                        top = centerY - r,
                                        right = centerX + r,
                                        bottom = centerY + r
                                    )
                                )
                            }
                            return androidx.compose.ui.graphics.Outline.Generic(path)
                        }
                    }
                }
        )
    }
}

@Composable
fun CardShapePreference.toShape(isPressed: Boolean): Shape {
    return when (this) {
        CardShapePreference.DEFAULT -> {
            val radius by animateIntAsState(
                targetValue = if (isPressed) 12 else 32,
                animationSpec = ExpressivePhysics.fluidSnappy(), label = "corner_morph"
            )
            RoundedCornerShape(radius.dp)
        }
        CardShapePreference.SEMICIRCLE -> {
            val topRadius by animateIntAsState(targetValue = if (isPressed) 30 else 50, animationSpec = ExpressivePhysics.fluidSnappy(), label = "corner_morph")
            val bottomRadius by animateIntAsState(targetValue = if (isPressed) 12 else 8, animationSpec = ExpressivePhysics.fluidSnappy(), label = "corner_morph")
            RoundedCornerShape(
                topStart = androidx.compose.foundation.shape.CornerSize(topRadius.dp), 
                topEnd = androidx.compose.foundation.shape.CornerSize(topRadius.dp), 
                bottomEnd = androidx.compose.foundation.shape.CornerSize(bottomRadius.dp), 
                bottomStart = androidx.compose.foundation.shape.CornerSize(bottomRadius.dp)
            )
        }
        CardShapePreference.PILL -> {
            val radius by animateIntAsState(targetValue = if (isPressed) 16 else 28, animationSpec = ExpressivePhysics.fluidSnappy(), label = "corner_morph")
            RoundedCornerShape(radius.dp)
        }
        CardShapePreference.CLAMSHELL -> {
            val lg by animateIntAsState(targetValue = if (isPressed) 20 else 40, animationSpec = ExpressivePhysics.fluidSnappy(), label = "corner_morph")
            val sm by animateIntAsState(targetValue = if (isPressed) 12 else 8, animationSpec = ExpressivePhysics.fluidSnappy(), label = "corner_morph")
            RoundedCornerShape(
                topStart = androidx.compose.foundation.shape.CornerSize(lg.dp), 
                topEnd = androidx.compose.foundation.shape.CornerSize(lg.dp), 
                bottomEnd = androidx.compose.foundation.shape.CornerSize(lg.dp), 
                bottomStart = androidx.compose.foundation.shape.CornerSize(sm.dp)
            )
        }
        CardShapePreference.SLANTED -> {
            val cut by animateIntAsState(targetValue = if (isPressed) 12 else 24, animationSpec = ExpressivePhysics.fluidSnappy(), label = "corner_morph")
            androidx.compose.foundation.shape.CutCornerShape(topStart = cut.dp, bottomEnd = cut.dp)
        }
        CardShapePreference.SQUARE -> {
            val radius by animateIntAsState(targetValue = if (isPressed) 4 else 8, animationSpec = ExpressivePhysics.fluidSnappy(), label = "corner_morph")
            RoundedCornerShape(radius.dp)
        }
    }
}
