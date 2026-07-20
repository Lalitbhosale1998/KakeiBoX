package com.personal.kakeibox.ui.theme.journeys

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.kakeibox.R
import kotlinx.coroutines.launch
import kotlin.math.sin

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun JourneysScreen() {
    var selectedTab by remember { mutableStateOf(JourneysTab.Memories) }
    var showAddTripSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    
    // Shared Element state
    var selectedMemoryId by remember { mutableStateOf<String?>(null) }
    
    // Sample Data
    val memories = remember {
        listOf(
            MemoryItem("1", "Kyoto Autumn", "Nov 2024"),
            MemoryItem("2", "Sapporo Snow", "Feb 2025"),
            MemoryItem("3", "Tokyo Bay", "Dec 2024"),
            MemoryItem("4", "Osaka Food Tour", "Oct 2024"),
            MemoryItem("5", "Okinawa Beach", "Aug 2024")
        )
    }

    SharedTransitionLayout {
        val sharedTransitionScope = this
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Tab Selector (Expressive ButtonGroup style)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    JourneysTabButton(
                        title = "Memories",
                        isSelected = selectedTab == JourneysTab.Memories,
                        onClick = { selectedTab = JourneysTab.Memories }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    JourneysTabButton(
                        title = "Horizons",
                        isSelected = selectedTab == JourneysTab.Horizons,
                        onClick = { selectedTab = JourneysTab.Horizons }
                    )
                }

                // Content
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(300)) + slideInVertically(initialOffsetY = { 50 }))
                            .togetherWith(fadeOut(animationSpec = tween(300)))
                    },
                    label = "tab_content"
                ) { tab ->
                    when (tab) {
                        JourneysTab.Memories -> {
                            LazyVerticalStaggeredGrid(
                                columns = StaggeredGridCells.Fixed(2),
                                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                contentPadding = PaddingValues(bottom = 100.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalItemSpacing = 16.dp
                            ) {
                                items(memories, key = { it.id }) { memory ->
                                    MemoryCard(
                                        memory = memory,
                                        sharedTransitionScope = sharedTransitionScope,
                                        animatedVisibilityScope = this@AnimatedContent,
                                        onClick = { selectedMemoryId = memory.id }
                                    )
                                }
                            }
                        }
                        JourneysTab.Horizons -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Spacer(modifier = Modifier.height(16.dp))
                                BoardingPassCard(
                                    destination = "Golden Week: Tobu-Nikko",
                                    dates = "May 2 - May 5",
                                    tags = listOf("Avoid Crowds", "Nature"),
                                    saved = 25000f,
                                    target = 40000f
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                BoardingPassCard(
                                    destination = "Fuji Summer Climb",
                                    dates = "Aug 10 - Aug 12",
                                    tags = listOf("Stamina", "Group"),
                                    saved = 10000f,
                                    target = 50000f
                                )
                                Spacer(modifier = Modifier.height(100.dp))
                            }
                        }
                    }
                }
            }

            // HorizontalFloatingToolbar (Bottom)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .wrapContentSize()
            ) {
                FloatingActionButton(
                    onClick = { showAddTripSheet = true },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.height(64.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Trip")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add Trip",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
        
        // Fullscreen details via Shared Element
        AnimatedVisibility(
            visible = selectedMemoryId != null,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300))
        ) {
            selectedMemoryId?.let { id ->
                val memory = memories.find { it.id == id }
                if (memory != null) {
                    MemoryDetailScreen(
                        memory = memory,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = this@AnimatedVisibility,
                        onBack = { selectedMemoryId = null }
                    )
                }
            }
        }
    }

    if (showAddTripSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddTripSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            AddTripSheetContent {
                coroutineScope.launch {
                    sheetState.hide()
                    showAddTripSheet = false
                }
            }
        }
    }
}

enum class JourneysTab { Memories, Horizons }

@Composable
fun JourneysTabButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        label = "tab_color"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "tab_content_color"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "tab_scale"
    )

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (!isSelected) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            },
        shadowElevation = if (isSelected) 8.dp else 0.dp
    ) {
        Text(
            text = title,
            color = contentColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
        )
    }
}

data class MemoryItem(val id: String, val title: String, val date: String)

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MemoryCard(
    memory: MemoryItem,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: () -> Unit
) {
    var isDismissed by remember { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                isDismissed = true
                true
            } else false
        }
    )
    
    if (isDismissed) return

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(24.dp))
                    .padding(end = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) {
        with(sharedTransitionScope) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(if (memory.id.toInt() % 2 == 0) 0.8f else 1.2f)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = "memory_card_${memory.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ -> spring(dampingRatio = 0.7f, stiffness = 200f) }
                    )
                    .clickable { onClick() },
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                tonalElevation = 4.dp
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Decorative placeholder content
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f)),
                                start = Offset(0f, size.height * 0.5f),
                                end = Offset(0f, size.height)
                            )
                        )
                    }
                    
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = memory.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = memory.date,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MemoryDetailScreen(
    memory: MemoryItem,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit
) {
    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = "memory_card_${memory.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ -> spring(dampingRatio = 0.7f, stiffness = 200f) }
                )
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable { onBack() }
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = memory.title,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = memory.date,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun BoardingPassCard(
    destination: String,
    dates: String,
    tags: List<String>,
    saved: Float,
    target: Float
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.FlightTakeoff,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "UPCOMING",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                }
                Text(
                    text = dates,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = destination,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                tags.forEach { tag ->
                    Surface(
                        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            WaveformProgressIndicator(saved = saved, target = target)
        }
    }
}

@Composable
fun WaveformProgressIndicator(saved: Float, target: Float) {
    val progress = (saved / target).coerceIn(0f, 1f)
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_offset"
    )
    
    val activeColor = MaterialTheme.colorScheme.primary
    val trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "¥${saved.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = activeColor
            )
            Text(
                text = "¥${target.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.fillMaxWidth().height(24.dp)) {
            val width = size.width
            val height = size.height
            
            // Draw background track
            drawRoundRect(
                color = trackColor,
                size = Size(width, height),
                cornerRadius = CornerRadius(height / 2)
            )
            
            // Draw waveform progress
            val activeWidth = width * progress
            if (activeWidth > 0) {
                clipRect(right = activeWidth) {
                    val path = Path()
                    val waveAmp = height * 0.15f
                    val waveFreq = 20f
                    
                    path.moveTo(0f, height / 2f)
                    for (x in 0..width.toInt() step 5) {
                        val normalizedX = x / width
                        val yOffset = sin(normalizedX * waveFreq + waveOffset) * waveAmp
                        path.lineTo(x.toFloat(), height / 2f + yOffset)
                    }
                    
                    drawPath(
                        path = path,
                        color = activeColor,
                        style = Stroke(width = height, cap = StrokeCap.Round)
                    )
                }
            }
        }
    }
}

@Composable
fun AddTripSheetContent(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = "New Horizon",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Destination") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Target Budget (¥)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Create Trip", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
