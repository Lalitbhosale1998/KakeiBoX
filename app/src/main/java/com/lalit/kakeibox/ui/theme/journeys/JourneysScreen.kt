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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import android.os.Build
import android.graphics.RuntimeShader
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.ShaderBrush
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

import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import com.personal.kakeibox.ui.components.ExpressiveCollapsingHeader
import com.personal.kakeibox.ui.settings.ThemeViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.data.preferences.TopAppBarBackground

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun JourneysScreen(
    themeViewModel: ThemeViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val themeSettings by themeViewModel.themeSettings.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(JourneysTab.Memories) }
    var showAddTripSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    
    // Shared Element state
    var selectedMemoryId by remember { mutableStateOf<String?>(null) }
    var selectedHorizonId by remember { mutableStateOf<String?>(null) }
    
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
    
    val horizonsData = remember {
        listOf(
            HorizonItem("h1", "Golden Week: Tobu-Nikko", "May 2 - May 5", listOf("Avoid Crowds", "Nature"), 25000f, 40000f),
            HorizonItem("h2", "Fuji Summer Climb", "Aug 10 - Aug 12", listOf("Stamina", "Group"), 10000f, 50000f),
            HorizonItem("h3", "Osaka Weekend", "Oct 1 - Oct 3", listOf("Food", "City"), 5000f, 30000f)
        )
    }

    LaunchedEffect(Unit) {
        themeViewModel.onAddActionButtonClicked.collect { route ->
            if (route == "journeys") {
                showAddTripSheet = true
            }
        }
    }

    val memoriesListState = rememberLazyStaggeredGridState()
    val horizonsScrollState = rememberScrollState()

    val density = LocalDensity.current
    val maxOffsetPx = with(density) { 70.dp.toPx() }

    val scrollOffset by remember {
        derivedStateOf {
            when (selectedTab) {
                JourneysTab.Memories -> {
                    if (memoriesListState.firstVisibleItemIndex > 0) maxOffsetPx
                    else memoriesListState.firstVisibleItemScrollOffset.toFloat().coerceAtMost(maxOffsetPx)
                }
                JourneysTab.Horizons -> {
                    horizonsScrollState.value.toFloat().coerceAtMost(maxOffsetPx)
                }
            }
        }
    }
    
    val isPrimaryContainer = themeSettings.topAppBarBackground == TopAppBarBackground.PRIMARY_CONTAINER
    val onContainerColor = if (isPrimaryContainer) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    val primaryTextAccent = if (isPrimaryContainer) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary

    val topAppBarContainerColor by animateColorAsState(
        targetValue = when (themeSettings.topAppBarBackground) {
            TopAppBarBackground.SURFACE -> MaterialTheme.colorScheme.background
            TopAppBarBackground.PRIMARY_CONTAINER -> androidx.compose.ui.graphics.lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.primaryContainer, 0.35f)
        },
        label = "top_app_bar_container_color"
    )

    SharedTransitionLayout {
        val sharedTransitionScope = this
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            Column(modifier = Modifier.fillMaxSize()) {
                ExpressiveCollapsingHeader(
                    title = "My",
                    subtitle = "Journeys",
                    scrollOffset = scrollOffset,
                    maxOffset = maxOffsetPx,
                    containerColor = topAppBarContainerColor,
                    onContainerColor = onContainerColor,
                    primaryTextAccent = primaryTextAccent,
                    actions = {}
                )
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
                        val slideDir = if (targetState.ordinal > initialState.ordinal) {
                            AnimatedContentTransitionScope.SlideDirection.Left
                        } else {
                            AnimatedContentTransitionScope.SlideDirection.Right
                        }
                        (slideIntoContainer(slideDir, tween(400)) + fadeIn(tween(400)))
                            .togetherWith(slideOutOfContainer(slideDir, tween(400)) + fadeOut(tween(400)))
                    },
                    label = "tab_content"
                ) { tab ->
                    when (tab) {
                        JourneysTab.Memories -> {
                            LazyVerticalStaggeredGrid(
                                state = memoriesListState,
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
                                    .verticalScroll(horizonsScrollState)
                            ) {
                                Spacer(modifier = Modifier.height(16.dp))
                                horizonsData.forEach { horizon ->
                                    BoardingPassCard(
                                        horizon = horizon,
                                        sharedTransitionScope = sharedTransitionScope,
                                        animatedVisibilityScope = this@AnimatedContent,
                                        onClick = { selectedHorizonId = horizon.id }
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                }
                                Spacer(modifier = Modifier.height(100.dp))
                            }
                        }
                    }
                }
            } // Closes Column

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
            
            AnimatedVisibility(
                visible = selectedHorizonId != null,
                enter = fadeIn(tween(300)),
                exit = fadeOut(tween(300))
            ) {
                selectedHorizonId?.let { id ->
                    val horizon = horizonsData.find { it.id == id }
                    if (horizon != null) {
                        HorizonDetailScreen(
                            horizon = horizon,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = this@AnimatedVisibility,
                            onBack = { selectedHorizonId = null }
                        )
                    }
                }
            }
        } // Closes Box
    } // Closes SharedTransitionLayout

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

    // Expressive Shape Morphing: Asymmetrical 'leaf/cookie' shape when selected, perfect pill when unselected
    val cornerTS by animateIntAsState(if (isSelected) 20 else 50, label = "ts")
    val cornerTE by animateIntAsState(if (isSelected) 100 else 50, label = "te")
    val cornerBE by animateIntAsState(if (isSelected) 20 else 50, label = "be")
    val cornerBS by animateIntAsState(if (isSelected) 100 else 50, label = "bs")

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(topStartPercent = cornerTS, topEndPercent = cornerTE, bottomEndPercent = cornerBE, bottomStartPercent = cornerBS),
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
data class HorizonItem(val id: String, val destination: String, val dates: String, val tags: List<String>, val saved: Float, val target: Float)

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
    
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // M3 Expressive "Touch Bloom" physics
    val cornerRadius by animateDpAsState(
        targetValue = if (isPressed) 8.dp else 24.dp,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "corner_radius"
    )
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "card_scale"
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
                    .graphicsLayer {
                        scaleX = cardScale
                        scaleY = cardScale
                    }
                    .clickable(interactionSource = interactionSource, indication = LocalIndication.current) { onClick() },
                shape = RoundedCornerShape(cornerRadius),
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

class TicketShape(private val topCornerRadius: Float, private val bottomCornerRadius: Float, private val cutoutRadius: Float) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: androidx.compose.ui.unit.Density): Outline {
        val path = Path().apply {
            val w = size.width
            val h = size.height
            val tcr = topCornerRadius
            val bcr = bottomCornerRadius
            val cutR = cutoutRadius
            
            moveTo(0f, tcr)
            if (tcr > 0f) quadraticTo(0f, 0f, tcr, 0f) else lineTo(0f, 0f)
            lineTo(w - tcr, 0f)
            if (tcr > 0f) quadraticTo(w, 0f, w, tcr) else lineTo(w, 0f)
            
            lineTo(w, (h / 2) - cutR)
            arcTo(
                rect = Rect(w - cutR, (h / 2) - cutR, w + cutR, (h / 2) + cutR),
                startAngleDegrees = 270f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )
            
            lineTo(w, h - bcr)
            if (bcr > 0f) quadraticTo(w, h, w - bcr, h) else lineTo(w, h)
            lineTo(bcr, h)
            if (bcr > 0f) quadraticTo(0f, h, 0f, h - bcr) else lineTo(0f, h)
            
            lineTo(0f, (h / 2) + cutR)
            arcTo(
                rect = Rect(-cutR, (h / 2) - cutR, cutR, (h / 2) + cutR),
                startAngleDegrees = 90f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )
            
            lineTo(0f, tcr)
            close()
        }
        return Outline.Generic(path)
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BoardingPassCard(
    horizon: HorizonItem,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: () -> Unit = {},
    onBack: () -> Unit = {},
    isHeader: Boolean = false // If true, disable touch bloom/click
) {
    val density = LocalDensity.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed && !isHeader) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "card_scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isHeader) 0.dp else if (isPressed) 8.dp else 2.dp,
        label = "elevation"
    )
    
    val topCornerRadius by animateFloatAsState(
        targetValue = if (isHeader) 0f else with(density) { 32.dp.toPx() },
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 200f),
        label = "top_cr"
    )

    val statusBarsPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val topPadding by animateDpAsState(
        targetValue = if (isHeader) statusBarsPadding + 24.dp else 24.dp,
        label = "top_padding"
    )

    with(sharedTransitionScope) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = "horizon_card_${horizon.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ -> spring(dampingRatio = 0.7f, stiffness = 200f) }
                )
                .graphicsLayer {
                    scaleX = cardScale
                    scaleY = cardScale
                }
                .clickable(
                    enabled = !isHeader,
                    interactionSource = interactionSource, 
                    indication = LocalIndication.current
                ) { onClick() },
            shape = TicketShape(
                topCornerRadius = topCornerRadius,
                bottomCornerRadius = with(density) { 32.dp.toPx() },
                cutoutRadius = with(density) { 16.dp.toPx() }
            ),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shadowElevation = elevation
        ) {
            Column(modifier = Modifier.padding(top = topPadding, bottom = 24.dp, start = 24.dp, end = 24.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isHeader) {
                            IconButton(onClick = onBack, modifier = Modifier.offset(x = (-12).dp)) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.primary)
                            }
                        } else {
                            Icon(
                                Icons.Default.FlightTakeoff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        Text(
                            text = if (isHeader) "ITINERARY" else "UPCOMING",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                    }
                    Text(
                        text = horizon.dates,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = horizon.destination,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    horizon.tags.forEach { tag ->
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
                WaveformProgressIndicator(saved = horizon.saved, target = horizon.target)
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HorizonDetailScreen(
    horizon: HorizonItem,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit
) {
    var subDestinations by remember { 
        mutableStateOf(
            if (horizon.destination.contains("Osaka")) 
                listOf("Osaka Castle", "Universal Studios Japan", "Dotombori District") 
            else listOf()
        ) 
    }
    var isAdding by remember { mutableStateOf(false) }
    var newDestination by remember { mutableStateOf("") }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            BoardingPassCard(
                horizon = horizon,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                onBack = onBack,
                isHeader = true
            )
            
            Text(
                text = "Timeline",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                subDestinations.forEachIndexed { index, dest ->
                    Row(
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            if (index < subDestinations.size - 1 || isAdding) {
                                Canvas(modifier = Modifier.fillMaxHeight().width(2.dp).padding(top = 24.dp)) {
                                    drawLine(
                                        color = Color.Gray.copy(alpha = 0.5f),
                                        start = Offset(size.width/2, 0f),
                                        end = Offset(size.width/2, size.height),
                                        strokeWidth = 4f,
                                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .size(16.dp)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Surface(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            tonalElevation = 2.dp
                        ) {
                            Text(
                                text = dest,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
                
                AnimatedContent(
                    targetState = isAdding,
                    transitionSpec = {
                        (fadeIn() + slideInVertically()).togetherWith(fadeOut() + slideOutVertically())
                    },
                    label = "add_node"
                ) { adding ->
                    if (adding) {
                        Row(
                            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.width(24.dp).fillMaxHeight(),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 24.dp)
                                        .size(16.dp)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), CircleShape)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            OutlinedTextField(
                                value = newDestination,
                                onValueChange = { newDestination = it },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                placeholder = { Text("Enter destination...") },
                                trailingIcon = {
                                    IconButton(onClick = { 
                                        if (newDestination.isNotBlank()) {
                                            subDestinations = subDestinations + newDestination
                                        }
                                        newDestination = ""
                                        isAdding = false 
                                    }) {
                                        Icon(Icons.Default.Add, contentDescription = "Add")
                                    }
                                },
                                shape = RoundedCornerShape(16.dp)
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(40.dp))
                            OutlinedButton(
                                onClick = { isAdding = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Add Stop", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

const val WAVE_SHADER_CODE = """
    uniform float2 u_resolution;
    uniform float u_time;
    uniform float u_progress;
    uniform half4 u_activeColor;
    
    half4 main(float2 fragCoord) {
        float normalizedX = fragCoord.x / u_resolution.x;
        if (normalizedX > u_progress) return half4(0.0);
        
        float waveAmp = u_resolution.y * 0.15;
        float waveFreq = 20.0;
        
        float yOffset = sin(normalizedX * waveFreq + u_time) * waveAmp;
        float centerY = (u_resolution.y / 2.0) + yOffset;
        
        float halfStroke = u_resolution.y / 2.0;
        if (fragCoord.y >= centerY - halfStroke && fragCoord.y <= centerY + halfStroke) {
            return u_activeColor;
        }
        return half4(0.0);
    }
"""

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
    
    val runtimeShader = remember(activeColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RuntimeShader(WAVE_SHADER_CODE).apply {
                setFloatUniform("u_activeColor", activeColor.red, activeColor.green, activeColor.blue, activeColor.alpha)
            }
        } else null
    }
    
    val shaderBrush = remember(runtimeShader) {
        runtimeShader?.let { ShaderBrush(it) }
    }

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
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && runtimeShader != null && shaderBrush != null) {
                runtimeShader.setFloatUniform("u_resolution", width, height)
                runtimeShader.setFloatUniform("u_time", waveOffset)
                runtimeShader.setFloatUniform("u_progress", progress)
                drawRect(brush = shaderBrush, size = Size(width, height))
            } else {
                // Fallback CPU Path Rendering
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
