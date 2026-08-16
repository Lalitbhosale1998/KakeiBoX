package com.personal.kakeibox.ui.theme.vocab

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.hilt.navigation.compose.hiltViewModel
import com.personal.kakeibox.data.entity.VocabEntry
import com.personal.kakeibox.data.preferences.TopAppBarBackground
import com.personal.kakeibox.ui.components.ExpressiveSwitch
import com.personal.kakeibox.ui.theme.ExpressiveMotion
import com.personal.kakeibox.ui.theme.ExpressivePhysics
import com.personal.kakeibox.ui.theme.LocalThemeSettings
import com.personal.kakeibox.ui.theme.expressiveBackground
import com.personal.kakeibox.ui.theme.getAppStrings
import com.personal.kakeibox.ui.vocab.VocabViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun KotobaScreen(
    viewModel: VocabViewModel = hiltViewModel()
) {
    val themeSettings = LocalThemeSettings.current
    val strings = getAppStrings(themeSettings.appLanguage)
    val haptic = LocalHapticFeedback.current

    val allEntries by viewModel.allEntries.collectAsState()
    val filteredEntries by viewModel.filteredEntries.collectAsState()
    val categories by viewModel.availableCategories.collectAsState()
    val subCategories by viewModel.availableSubCategories.collectAsState()
    val studyTags by viewModel.availableStudyTags.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val isJapanese = themeSettings.appLanguage == com.personal.kakeibox.data.preferences.AppLanguage.JAPANESE

    var showAddSheet by remember { mutableStateOf(false) }
    var selectedVocabEntry by remember { mutableStateOf<VocabEntry?>(null) }

    // Curriculum Navigation State
    var selectedWeek by remember { mutableStateOf<String?>(null) }
    var selectedDay by remember { mutableStateOf<String>("1日目") }
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }
    var showOnlyStarred by remember { mutableStateOf(false) }

    // Dynamically group entries by Week and Day
    val weeksList = remember(allEntries) {
        val extracted = allEntries.mapNotNull { entry ->
            val tag = entry.studyTag
            if (tag.startsWith("第") && tag.contains("週")) {
                val weekPart = tag.substringBefore("・").trim()
                if (weekPart.isNotEmpty()) weekPart else null
            } else null
        }.distinct().sorted()

        if (extracted.isEmpty()) listOf("第1週") else extracted
    }

    val entriesByWeek = remember(allEntries) {
        allEntries.groupBy { entry ->
            val tag = entry.studyTag
            if (tag.startsWith("第") && tag.contains("週")) {
                tag.substringBefore("・").trim()
            } else {
                "その他"
            }
        }
    }

    val lazyListState = rememberLazyListState()
    val isFabExpanded by remember {
        derivedStateOf {
            !lazyListState.isScrollInProgress || lazyListState.firstVisibleItemIndex == 0
        }
    }

    val systemDark = isSystemInDarkTheme()
    val isDark = themeSettings.darkThemePreference.isDark(systemDark)
    val isPrimaryContainer = themeSettings.topAppBarBackground == TopAppBarBackground.PRIMARY_CONTAINER
    val currentColorScheme = MaterialTheme.colorScheme
    val sheetColorScheme = remember(currentColorScheme) {
        val blendedSurface = androidx.compose.ui.graphics.lerp(
            currentColorScheme.surface,
            currentColorScheme.primaryContainer,
            0.35f
        )
        val blendedSurfaceHigh = androidx.compose.ui.graphics.lerp(
            currentColorScheme.surfaceContainerHigh,
            currentColorScheme.primaryContainer,
            0.35f
        )
        val blendedSurfaceLow = androidx.compose.ui.graphics.lerp(
            currentColorScheme.surfaceContainerLow,
            currentColorScheme.primaryContainer,
            0.35f
        )
        val blendedSurfaceLowest = androidx.compose.ui.graphics.lerp(
            currentColorScheme.surfaceContainerLowest,
            currentColorScheme.primaryContainer,
            0.35f
        )
        currentColorScheme.copy(
            surface = blendedSurface,
            surfaceContainer = blendedSurface,
            surfaceContainerHigh = blendedSurfaceHigh,
            surfaceContainerLow = blendedSurfaceLow,
            surfaceContainerLowest = blendedSurfaceLowest
        )
    }
    val topAppBarContainerColor = sheetColorScheme.primaryContainer

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    MaterialTheme(colorScheme = sheetColorScheme) {
    SharedTransitionLayout {
        val sharedTransitionScope = this

        Box(
            modifier = Modifier
                .fillMaxSize()
                .expressiveBackground(
                    isDark = isDark,
                    isPrimaryContainer = isPrimaryContainer,
                    primaryColor = MaterialTheme.colorScheme.primary,
                    containerColor = topAppBarContainerColor,
                    pattern = themeSettings.backdropPattern,
                    backgroundCanvasStyle = themeSettings.backgroundCanvasStyle
                )
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = {},
                floatingActionButton = {
                    if (selectedVocabEntry == null) {
                        com.personal.kakeibox.ui.components.ExpressiveScrollableFab(
                            extended = isFabExpanded,
                            text = if (isJapanese) "単語追加" else "Add Vocab",
                            icon = Icons.Outlined.Translate,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                showAddSheet = true
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            ) { innerPadding ->
                AnimatedVisibility(visible = true) {
                    val rootAnimatedVisibilityScope = this
                    Box(modifier = Modifier.fillMaxSize()) {
                        // ── Main Curriculum / List View (Always Mounted) ──
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                            Spacer(modifier = Modifier.height(statusBarPadding + 96.dp)) // Generous header inset below floating top nav bar

                            val searchBarBgColor = MaterialTheme.colorScheme.surfaceContainerHigh

                            // ── Search Bar ──
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { viewModel.setSearchQuery(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                placeholder = { Text(if (isJapanese) "単語・読み・意味を検索..." else "Search word, reading, or meaning...") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(24.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = searchBarBgColor,
                                    unfocusedContainerColor = searchBarBgColor,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // ── NAVIGATION CONTROLLER (SEARCH OVERRIDE vs CURRICULUM LEVELS) ──
                            if (searchQuery.isNotEmpty()) {
                                // 🔍 SEARCH MODE: Display Search Results across all entries
                                Text(
                                    text = if (isJapanese) "検索結果 (${filteredEntries.size}件)" else "Search Results (${filteredEntries.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                                )
                                if (filteredEntries.isEmpty()) {
                                    Box(
                                        modifier = Modifier.fillMaxSize().padding(32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (isJapanese) "該当する単語が見つかりません" else "No matching words found",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                } else {
                                    LazyColumn(
                                        state = lazyListState,
                                        modifier = Modifier.fillMaxSize(),
                                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = innerPadding.calculateBottomPadding() + 130.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        items(filteredEntries, key = { it.id }) { entry ->
                                            VocabCardItem(
                                                entry = entry,
                                                sharedTransitionScope = sharedTransitionScope,
                                                animatedVisibilityScope = rootAnimatedVisibilityScope,
                                                onClickCard = { selectedVocabEntry = entry },
                                                onToggleMastered = { viewModel.toggleMasteredStatus(entry) },
                                                onToggleStarred = { viewModel.toggleStarredStatus(entry) },
                                                onDelete = { viewModel.deleteVocabEntry(entry) }
                                            )
                                        }
                                    }
                                }
                            } else {
                                // ── M3 EXPRESSIVE CURRICULUM DRILL-DOWN ANIMATEDCONTENT (LEVEL 1 ⇄ LEVEL 2/3) ──
                                AnimatedContent(
                                    targetState = selectedWeek,
                                    transitionSpec = {
                                        if (targetState != null) {
                                            // Drilling down into a Week (Level 1 -> Level 2/3)
                                            (slideInHorizontally(ExpressivePhysics.fluidBouncy()) { width -> width / 3 } + fadeIn(ExpressivePhysics.fluidBouncy()))
                                                .togetherWith(slideOutHorizontally(ExpressivePhysics.fluidBouncy()) { width -> -width / 3 } + fadeOut(ExpressivePhysics.fluidBouncy()))
                                        } else {
                                            // Going back to Weeks Hub (Level 2/3 -> Level 1)
                                            (slideInHorizontally(ExpressivePhysics.fluidBouncy()) { width -> -width / 3 } + fadeIn(ExpressivePhysics.fluidBouncy()))
                                                .togetherWith(slideOutHorizontally(ExpressivePhysics.fluidBouncy()) { width -> width / 3 } + fadeOut(ExpressivePhysics.fluidBouncy()))
                                        }
                                    },
                                    label = "curriculum_week_transition"
                                ) { activeWeek ->
                                    if (showOnlyStarred) {
                                        // ── STARRED FLASHCARDS DEDICATED VIEW ──
                                        val starredEntries = remember(allEntries) { allEntries.filter { it.isStarred } }
                                        Column(modifier = Modifier.fillMaxSize()) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                IconButton(
                                                    onClick = {
                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                        showOnlyStarred = false
                                                    }
                                                ) {
                                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                                }
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = if (isJapanese) "⭐ スター単語 (${starredEntries.size})" else "⭐ Starred Words (${starredEntries.size})",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(
                                                        text = if (isJapanese) "お気に入り登録した単語一覧" else "Your saved favorite vocabulary cards",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }

                                            if (starredEntries.isEmpty()) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(32.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = if (isJapanese) "スター登録された単語はありません" else "No starred words yet",
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            } else {
                                                LazyColumn(
                                                    state = lazyListState,
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = innerPadding.calculateBottomPadding() + 130.dp),
                                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                                ) {
                                                    items(starredEntries, key = { it.id }) { entry ->
                                                        VocabCardItem(
                                                            entry = entry,
                                                            sharedTransitionScope = sharedTransitionScope,
                                                            animatedVisibilityScope = rootAnimatedVisibilityScope,
                                                            onClickCard = { selectedVocabEntry = entry },
                                                            onToggleMastered = { viewModel.toggleMasteredStatus(entry) },
                                                            onToggleStarred = { viewModel.toggleStarredStatus(entry) },
                                                            onDelete = { viewModel.deleteVocabEntry(entry) }
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    } else if (activeWeek == null) {
                                        // ── LEVEL 1: WEEKS SELECTION HUB (週一覧) ──
                                        Column(modifier = Modifier.fillMaxSize()) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 20.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column {
                                                    Text(
                                                        text = if (isJapanese) "週別学習カリキュラム" else "Weekly Curriculum",
                                                        style = MaterialTheme.typography.titleLarge,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(
                                                        text = if (isJapanese) "学習する週を選択してください" else "Select a week to begin study",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }

                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    // ⭐ OPTION 1: TOP HEADER QUICK ACTION PILL FOR STARRED WORDS
                                                    val starredCount = remember(allEntries) { allEntries.count { it.isStarred } }
                                                    if (starredCount > 0) {
                                                        Surface(
                                                            onClick = {
                                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                                showOnlyStarred = true
                                                            },
                                                            shape = CircleShape,
                                                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
                                                        ) {
                                                            Row(
                                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                                                verticalAlignment = Alignment.CenterVertically,
                                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                            ) {
                                                                Icon(
                                                                    imageVector = Icons.Default.Star,
                                                                    contentDescription = null,
                                                                    tint = MaterialTheme.colorScheme.secondary,
                                                                    modifier = Modifier.size(16.dp)
                                                                )
                                                                Text(
                                                                    text = "$starredCount",
                                                                    style = MaterialTheme.typography.labelSmall,
                                                                    fontWeight = FontWeight.ExtraBold,
                                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                                )
                                                            }
                                                        }
                                                    }

                                                    Surface(
                                                        shape = RoundedCornerShape(16.dp),
                                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                                                    ) {
                                                        Text(
                                                            text = "${weeksList.size} Weeks",
                                                            style = MaterialTheme.typography.labelMedium,
                                                            fontWeight = FontWeight.Bold,
                                                            color = MaterialTheme.colorScheme.primary,
                                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                                        )
                                                    }
                                                }
                                            }

                                            LazyColumn(
                                                state = lazyListState,
                                                modifier = Modifier.fillMaxSize(),
                                                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = innerPadding.calculateBottomPadding() + 130.dp),
                                                verticalArrangement = Arrangement.spacedBy(14.dp)
                                            ) {
                                                // 🍱 BENTO ITEM 1: FEATURED ACTIVE WEEK HERO CARD (TALL VARIED HEIGHT)
                                                if (weeksList.isNotEmpty()) {
                                                    val heroWeekName = weeksList.first()
                                                    val weekEntries = entriesByWeek[heroWeekName] ?: emptyList()
                                                    val totalCount = weekEntries.size
                                                    val masteredCount = weekEntries.count { it.isMastered }
                                                    val remainingCount = totalCount - masteredCount
                                                    val progress = if (totalCount > 0) masteredCount.toFloat() / totalCount.toFloat() else 0f
                                                    val progressPercent = (progress * 100).toInt()
                                                    val heroNumStr = heroWeekName.filter { it.isDigit() }.ifBlank { "1" }

                                                    item {
                                                        Surface(
                                                            onClick = {
                                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                                selectedWeek = heroWeekName
                                                                selectedDay = "1日目"
                                                            },
                                                            shape = RoundedCornerShape(32.dp),
                                                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                                            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                                                            modifier = Modifier.fillMaxWidth()
                                                        ) {
                                                            Column(modifier = Modifier.padding(24.dp)) {
                                                                Row(
                                                                    modifier = Modifier.fillMaxWidth(),
                                                                    verticalAlignment = Alignment.CenterVertically,
                                                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                                                ) {
                                                                    Box(
                                                                        contentAlignment = Alignment.Center,
                                                                        modifier = Modifier
                                                                            .size(76.dp)
                                                                            .background(
                                                                                color = MaterialTheme.colorScheme.primary,
                                                                                shape = RoundedCornerShape(24.dp)
                                                                            )
                                                                    ) {
                                                                        Text(
                                                                            text = heroNumStr,
                                                                            style = MaterialTheme.typography.displayLarge,
                                                                            fontSize = 48.sp,
                                                                            fontWeight = FontWeight.Black,
                                                                            color = MaterialTheme.colorScheme.onPrimary
                                                                        )
                                                                    }

                                                                    Column(modifier = Modifier.weight(1f)) {
                                                                        Surface(
                                                                            shape = CircleShape,
                                                                            color = MaterialTheme.colorScheme.primary
                                                                        ) {
                                                                            Text(
                                                                                text = "ACTIVE STUDY WEEK",
                                                                                style = MaterialTheme.typography.labelSmall,
                                                                                fontWeight = FontWeight.Black,
                                                                                fontSize = 9.sp,
                                                                                color = MaterialTheme.colorScheme.onPrimary,
                                                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                                                            )
                                                                        }
                                                                        Spacer(modifier = Modifier.height(4.dp))
                                                                        Text(
                                                                            text = heroWeekName,
                                                                            style = MaterialTheme.typography.headlineSmall,
                                                                            fontWeight = FontWeight.Black,
                                                                            color = MaterialTheme.colorScheme.onSurface
                                                                        )
                                                                        Text(
                                                                            text = "$totalCount Words  •  $masteredCount Mastered",
                                                                            style = MaterialTheme.typography.bodyMedium,
                                                                            fontWeight = FontWeight.Bold,
                                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                                        )
                                                                    }
                                                                }

                                                                Spacer(modifier = Modifier.height(20.dp))

                                                                Row(
                                                                    modifier = Modifier.fillMaxWidth(),
                                                                    verticalAlignment = Alignment.CenterVertically,
                                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                                ) {
                                                                    Text(
                                                                        text = if (progressPercent == 100) "🎉 ALL MASTERED!" else "🔥 $remainingCount WORDS REMAINING",
                                                                        style = MaterialTheme.typography.labelMedium,
                                                                        fontWeight = FontWeight.ExtraBold,
                                                                        color = MaterialTheme.colorScheme.primary
                                                                    )
                                                                    Text(
                                                                        text = "$progressPercent%",
                                                                        style = MaterialTheme.typography.titleMedium,
                                                                        fontWeight = FontWeight.Black,
                                                                        color = MaterialTheme.colorScheme.primary
                                                                    )
                                                                }
                                                                Spacer(modifier = Modifier.height(8.dp))
                                                                LinearProgressIndicator(
                                                                    progress = { progress },
                                                                    modifier = Modifier
                                                                        .fillMaxWidth()
                                                                        .height(12.dp)
                                                                        .clip(CircleShape),
                                                                    color = MaterialTheme.colorScheme.primary,
                                                                    trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
                                                                )

                                                                Spacer(modifier = Modifier.height(18.dp))

                                                                Button(
                                                                    onClick = {
                                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                                        selectedWeek = heroWeekName
                                                                        selectedDay = "1日目"
                                                                    },
                                                                    shape = RoundedCornerShape(20.dp),
                                                                    modifier = Modifier.fillMaxWidth()
                                                                ) {
                                                                    Row(
                                                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                                        verticalAlignment = Alignment.CenterVertically
                                                                    ) {
                                                                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                                                                        Text(
                                                                            text = if (isJapanese) "学習を始める (1日目)" else "Continue Study (Day 1)",
                                                                            fontWeight = FontWeight.Bold
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                // 🍱 ALL REMAINING WEEKS TILED IN BENTO GRID
                                                val remainingWeeksList = weeksList.drop(1)
                                                itemsIndexed(remainingWeeksList) { idx, weekName ->
                                                    val weekEntries = entriesByWeek[weekName] ?: emptyList()
                                                    val totalCount = weekEntries.size
                                                    val masteredCount = weekEntries.count { it.isMastered }
                                                    val remainingCount = totalCount - masteredCount
                                                    val progress = if (totalCount > 0) masteredCount.toFloat() / totalCount.toFloat() else 0f
                                                    val progressPercent = (progress * 100).toInt()
                                                    val weekNumStr = weekName.filter { it.isDigit() }.ifBlank { "${idx + 2}" }

                                                    Surface(
                                                        onClick = {
                                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                            selectedWeek = weekName
                                                            selectedDay = "1日目"
                                                        },
                                                        shape = RoundedCornerShape(28.dp),
                                                        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.85f),
                                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        Column(modifier = Modifier.padding(20.dp)) {
                                                            Row(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                verticalAlignment = Alignment.CenterVertically,
                                                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                                                            ) {
                                                                Box(
                                                                    contentAlignment = Alignment.Center,
                                                                    modifier = Modifier
                                                                        .size(64.dp)
                                                                        .background(
                                                                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                                                                            shape = RoundedCornerShape(20.dp)
                                                                        )
                                                                ) {
                                                                    Text(
                                                                        text = weekNumStr,
                                                                        style = MaterialTheme.typography.displayMedium,
                                                                        fontSize = 38.sp,
                                                                        fontWeight = FontWeight.Black,
                                                                        color = MaterialTheme.colorScheme.primary
                                                                    )
                                                                }

                                                                Column(modifier = Modifier.weight(1f)) {
                                                                    Text(
                                                                        text = weekName,
                                                                        style = MaterialTheme.typography.titleMedium,
                                                                        fontWeight = FontWeight.ExtraBold,
                                                                        color = MaterialTheme.colorScheme.onSurface
                                                                    )
                                                                    Text(
                                                                        text = "$totalCount Words • $masteredCount Mastered",
                                                                        style = MaterialTheme.typography.bodySmall,
                                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                                    )
                                                                }

                                                                Icon(
                                                                    imageVector = Icons.Default.ChevronRight,
                                                                    contentDescription = null,
                                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        // ── LEVEL 2 & 3: WEEK DETAIL VIEW WITH DAY SELECTOR BAR & TARGETED DAY VOCAB LIST ──
                                        val weekName = activeWeek
                                        val weekEntries = entriesByWeek[weekName] ?: emptyList()

                                        Column(modifier = Modifier.fillMaxSize()) {
                                            // Top Header with Back Button
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                IconButton(
                                                    onClick = {
                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                        selectedWeek = null
                                                    }
                                                ) {
                                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Weeks")
                                                }
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = weekName,
                                                        style = MaterialTheme.typography.titleMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(
                                                        text = if (isJapanese) "日別の学習単語" else "Daily Vocab List",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }

                                            // ── LEVEL 2: HORIZONTAL DAY SELECTOR BAR WITH ANIMATED SPRING SCALE & AUTO-SCROLL ──
                                            val dayOptions = listOf("1日目", "2日目", "3日目", "4日目", "5日目", "6日目", "7日目 (復習)")
                                            val dayLazyRowState = rememberLazyListState()
                                            val coroutineScope = rememberCoroutineScope()

                                            LazyRow(
                                                state = dayLazyRowState,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 8.dp),
                                                contentPadding = PaddingValues(horizontal = 16.dp),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                itemsIndexed(dayOptions) { index, dayName ->
                                                    val countForDay = weekEntries.count { it.studyTag.contains(dayName.take(3)) }
                                                    val isSelected = selectedDay == dayName || selectedDay.startsWith(dayName.take(3))

                                                    val pillBg by animateColorAsState(
                                                        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f),
                                                        animationSpec = tween(250),
                                                        label = "pill_bg"
                                                    )
                                                    val pillTextColor by animateColorAsState(
                                                        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                                        animationSpec = tween(250),
                                                        label = "pill_text"
                                                    )
                                                    val pillScale by animateFloatAsState(
                                                        targetValue = if (isSelected) 1.0f else 0.94f,
                                                        animationSpec = ExpressivePhysics.fluidBouncy(),
                                                        label = "pill_scale"
                                                    )

                                                    Surface(
                                                        onClick = {
                                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                            selectedDay = dayName
                                                            coroutineScope.launch {
                                                                dayLazyRowState.animateScrollToItem(index)
                                                            }
                                                        },
                                                        shape = RoundedCornerShape(20.dp),
                                                        color = pillBg,
                                                        border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                                                        modifier = Modifier.graphicsLayer {
                                                            scaleX = pillScale
                                                            scaleY = pillScale
                                                        }
                                                    ) {
                                                        Row(
                                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                        ) {
                                                            Text(
                                                                text = dayName,
                                                                style = MaterialTheme.typography.labelMedium,
                                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                                color = pillTextColor
                                                            )
                                                            if (countForDay > 0) {
                                                                Surface(
                                                                    shape = CircleShape,
                                                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHighest
                                                                ) {
                                                                    Text(
                                                                        text = "$countForDay",
                                                                        style = MaterialTheme.typography.labelSmall,
                                                                        fontSize = 10.sp,
                                                                        fontWeight = FontWeight.Bold,
                                                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            // ── LEVEL 3: TARGETED DAY VOCAB LIST WITH DIRECTIONAL ANIMATEDCONTENT SWITCH ──
                                            AnimatedContent(
                                                targetState = selectedDay,
                                                transitionSpec = {
                                                    (slideInHorizontally(ExpressivePhysics.fluidBouncy()) { width -> width / 4 } + fadeIn(ExpressivePhysics.fluidBouncy()))
                                                        .togetherWith(slideOutHorizontally(ExpressivePhysics.fluidBouncy()) { width -> -width / 4 } + fadeOut(ExpressivePhysics.fluidBouncy()))
                                                },
                                                label = "curriculum_day_transition"
                                            ) { currentDay ->
                                                LaunchedEffect(activeWeek, currentDay) {
                                                    selectedCategoryFilter = null
                                                }

                                                val selectedDayPrefix = currentDay.take(3) // e.g. "1日目" -> "1日目"
                                                val dayEntries = remember(weekEntries, selectedDayPrefix) {
                                                    weekEntries.filter { it.studyTag.contains(selectedDayPrefix) }
                                                }

                                                // Auto-discover dynamic Categories & Subcategories for this Day
                                                val dynamicDayFilters = remember(dayEntries) {
                                                    val catList = dayEntries.mapNotNull { it.category.ifBlank { null } }.distinct()
                                                    val subCatList = dayEntries.mapNotNull { it.subCategory.ifBlank { null } }.distinct()
                                                    (catList + subCatList).distinct()
                                                }

                                                val finalFilteredDayEntries = remember(dayEntries, selectedCategoryFilter) {
                                                    if (selectedCategoryFilter == null) {
                                                        dayEntries
                                                    } else {
                                                        dayEntries.filter { it.category == selectedCategoryFilter || it.subCategory == selectedCategoryFilter }
                                                    }
                                                }

                                                Column(modifier = Modifier.fillMaxSize()) {
                                                    // 🏷️ Dynamic Category & Subcategory Filter Chips Row
                                                    if (dayEntries.isNotEmpty() && dynamicDayFilters.isNotEmpty()) {
                                                        val filterLazyRowState = rememberLazyListState()
                                                        LazyRow(
                                                            state = filterLazyRowState,
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(bottom = 6.dp),
                                                            contentPadding = PaddingValues(horizontal = 16.dp),
                                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                        ) {
                                                            item {
                                                                val isAllSelected = selectedCategoryFilter == null
                                                                val allScale by animateFloatAsState(
                                                                    targetValue = if (isAllSelected) 1.0f else 0.94f,
                                                                    animationSpec = ExpressivePhysics.fluidBouncy(),
                                                                    label = "all_chip_scale"
                                                                )
                                                                Surface(
                                                                    onClick = {
                                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                                        selectedCategoryFilter = null
                                                                        coroutineScope.launch {
                                                                            filterLazyRowState.animateScrollToItem(0)
                                                                        }
                                                                    },
                                                                    shape = CircleShape,
                                                                    color = if (isAllSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
                                                                    modifier = Modifier.graphicsLayer {
                                                                        scaleX = allScale
                                                                        scaleY = allScale
                                                                    }
                                                                ) {
                                                                    Text(
                                                                        text = if (isJapanese) "すべて (${dayEntries.size})" else "All (${dayEntries.size})",
                                                                        style = MaterialTheme.typography.labelSmall,
                                                                        fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Medium,
                                                                        color = if (isAllSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                                                    )
                                                                }
                                                            }

                                                            itemsIndexed(dynamicDayFilters) { fIndex, filterTag ->
                                                                val countForTag = dayEntries.count { it.category == filterTag || it.subCategory == filterTag }
                                                                val isSelected = selectedCategoryFilter == filterTag
                                                                val chipBg = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.6f)
                                                                val chipColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                                                val chipScale by animateFloatAsState(
                                                                    targetValue = if (isSelected) 1.0f else 0.94f,
                                                                    animationSpec = ExpressivePhysics.fluidBouncy(),
                                                                    label = "chip_scale"
                                                                )

                                                                Surface(
                                                                    onClick = {
                                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                                        selectedCategoryFilter = if (isSelected) null else filterTag
                                                                        coroutineScope.launch {
                                                                            filterLazyRowState.animateScrollToItem(fIndex + 1)
                                                                        }
                                                                    },
                                                                    shape = CircleShape,
                                                                    color = chipBg,
                                                                    border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                                                                    modifier = Modifier.graphicsLayer {
                                                                        scaleX = chipScale
                                                                        scaleY = chipScale
                                                                    }
                                                                ) {
                                                                    Row(
                                                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                                                        verticalAlignment = Alignment.CenterVertically,
                                                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                                    ) {
                                                                        Text(
                                                                            text = filterTag,
                                                                            style = MaterialTheme.typography.labelSmall,
                                                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                                            color = chipColor
                                                                        )
                                                                        Text(
                                                                            text = "($countForTag)",
                                                                            style = MaterialTheme.typography.labelSmall,
                                                                            fontSize = 10.sp,
                                                                            color = chipColor.copy(alpha = 0.8f)
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                    if (finalFilteredDayEntries.isEmpty()) {
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxSize()
                                                                .padding(32.dp),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                                Icon(
                                                                    imageVector = Icons.Outlined.EventAvailable,
                                                                    contentDescription = null,
                                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                                                    modifier = Modifier.size(56.dp)
                                                                )
                                                                Spacer(modifier = Modifier.height(12.dp))
                                                                Text(
                                                                    text = if (isJapanese) "$currentDay の単語はありません" else "No words registered for $currentDay",
                                                                    style = MaterialTheme.typography.titleMedium,
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                                )
                                                                Text(
                                                                    text = if (isJapanese) "右下の「単語追加」ボタンから登録できます" else "Tap '+ Add Vocab' to register words for this day",
                                                                    style = MaterialTheme.typography.bodySmall,
                                                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                                                )
                                                            }
                                                        }
                                                    } else {
                                                        LazyColumn(
                                                            state = lazyListState,
                                                            modifier = Modifier.fillMaxSize(),
                                                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = innerPadding.calculateBottomPadding() + 130.dp),
                                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                                        ) {
                                                            items(finalFilteredDayEntries, key = { it.id }) { entry ->
                                                                VocabCardItem(
                                                                    entry = entry,
                                                                    sharedTransitionScope = sharedTransitionScope,
                                                                    animatedVisibilityScope = rootAnimatedVisibilityScope,
                                                                    onClickCard = { selectedVocabEntry = entry },
                                                                    onToggleMastered = { viewModel.toggleMasteredStatus(entry) },
                                                                    onToggleStarred = { viewModel.toggleStarredStatus(entry) },
                                                                    onDelete = { viewModel.deleteVocabEntry(entry) }
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // ── Expanded Container Detail View Overlay (Root Level) ──
                        AnimatedVisibility(
                            visible = selectedVocabEntry != null,
                            enter = fadeIn(tween(300)),
                            exit = fadeOut(tween(200))
                        ) {
                            val activeOverlayScope = this
                            selectedVocabEntry?.let { targetEntry ->
                                val liveEntry = allEntries.find { it.id == targetEntry.id } ?: targetEntry
                                ExpressiveVocabDetailView(
                                    entry = liveEntry,
                                    isJapanese = isJapanese,
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = rootAnimatedVisibilityScope,
                                    onClose = { selectedVocabEntry = null },
                                    onToggleMastered = { viewModel.toggleMasteredStatus(liveEntry) },
                                    onToggleStarred = { viewModel.toggleStarredStatus(liveEntry) },
                                    onDelete = {
                                        viewModel.deleteVocabEntry(liveEntry)
                                        selectedVocabEntry = null
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // ── Add Vocab Bottom Sheet ──
    if (showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddSheet = false },
            sheetState = sheetState,
            modifier = Modifier.statusBarsPadding().padding(horizontal = 12.dp, vertical = 8.dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 12.dp, bottomStart = 24.dp, bottomEnd = 24.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            dragHandle = {
                Surface(
                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                ) {
                    Box(modifier = Modifier.size(width = 36.dp, height = 5.dp))
                }
            }
        ) {
            ExpressiveVocabAddSheet(
                categories = categories.filter { it != "All" },
                onAddVocab = { kanji, reading, meaning, category, subCategory, tag, example ->
                    viewModel.addVocabEntry(kanji, reading, meaning, category, subCategory, tag, example)
                    showAddSheet = false
                },
                onCancel = { showAddSheet = false }
            )
        }
    }
}
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun VocabCardItem(
    entry: VocabEntry,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClickCard: () -> Unit,
    onToggleMastered: () -> Unit,
    onToggleStarred: () -> Unit,
    onDelete: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val themeSettings = LocalThemeSettings.current
    val isPrimaryContainer = themeSettings.topAppBarBackground == TopAppBarBackground.PRIMARY_CONTAINER
    val cardBgColor = if (entry.isMastered) MaterialTheme.colorScheme.surfaceContainerLow else MaterialTheme.colorScheme.surfaceContainerHigh

    with(sharedTransitionScope) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "vocab_card_${entry.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ -> ExpressivePhysics.fluidBouncy() }
                )
                .clickable {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClickCard()
                },
            shape = RoundedCornerShape(22.dp),
            color = cardBgColor,
            border = BorderStroke(
                if (entry.isStarred) 2.dp else 1.dp,
                if (entry.isStarred) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            ),
            shadowElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        // Furigana Float Above Kanji
                        if (entry.furiganaReading.isNotBlank()) {
                            Text(
                                text = entry.furiganaReading,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = entry.kanjiWord,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Mastery Toggle Chip
                        FilterChip(
                            selected = entry.isMastered,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onToggleMastered()
                            },
                            label = {
                                Text(
                                    text = if (entry.isMastered) "習得済み" else "学習中",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (entry.isMastered) Icons.Default.Check else Icons.Outlined.RadioButtonUnchecked,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            shape = CircleShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onToggleStarred()
                        }) {
                            Icon(
                                imageVector = if (entry.isStarred) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = "Star",
                                tint = if (entry.isStarred) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onDelete()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Meaning
                Text(
                    text = entry.meaning,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Spacer(modifier = Modifier.height(10.dp))

                // Tags FlowRow (Wraps cleanly without horizontal overflow)
                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (entry.category.isNotBlank()) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = "🏷️ ${entry.category}",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    if (entry.subCategory.isNotBlank()) {
                        val isPositive = entry.subCategory.contains("良い")
                        Surface(
                            shape = CircleShape,
                            color = if (isPositive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                        ) {
                            Text(
                                text = if (isPositive) "🌸 良い意味" else "⚡️ よくない意味",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontWeight = FontWeight.SemiBold,
                                color = if (isPositive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                    if (entry.studyTag.isNotBlank()) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.tertiaryContainer
                        ) {
                            Text(
                                text = "📅 ${entry.studyTag}",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ExpressiveVocabDetailView(
    entry: VocabEntry,
    isJapanese: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClose: () -> Unit,
    onToggleMastered: () -> Unit,
    onToggleStarred: () -> Unit,
    onDelete: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current
    var showInfo by remember { mutableStateOf(false) }

    val themeSettings = LocalThemeSettings.current
    val isPrimaryContainer = themeSettings.topAppBarBackground == TopAppBarBackground.PRIMARY_CONTAINER
    val detailCardBgColor = if (isPrimaryContainer) {
        androidx.compose.ui.graphics.lerp(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            MaterialTheme.colorScheme.primaryContainer,
            0.35f
        )
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }

    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.55f))
                .clickable { onClose() },
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .padding(vertical = 32.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(key = "vocab_card_${entry.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ -> ExpressivePhysics.fluidBouncy() }
                    )
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(32.dp),
                color = detailCardBgColor,
                border = BorderStroke(
                    if (entry.isStarred) 2.dp else 1.dp,
                    if (entry.isStarred) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                ),
                shadowElevation = 16.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    // Top Action Dock
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onClose()
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                showInfo = !showInfo
                            }) {
                                Icon(
                                    imageVector = if (showInfo) Icons.Filled.Info else Icons.Outlined.Info,
                                    contentDescription = "Toggle Metadata Info",
                                    tint = if (showInfo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onToggleStarred()
                            }) {
                                Icon(
                                    imageVector = if (entry.isStarred) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                    contentDescription = "Star",
                                    tint = if (entry.isStarred) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onDelete()
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // ── Monumental 95.sp Kanji Hero Display ──
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (entry.furiganaReading.isNotBlank()) {
                            Text(
                                text = entry.furiganaReading,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        // Gigantic Monumental Kanji Display (95.sp)
                        Text(
                            text = entry.kanjiWord,
                            fontSize = 95.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.SansSerif,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 90.sp,
                            textAlign = TextAlign.Center,
                            letterSpacing = (-4).sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = entry.meaning.uppercase(),
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }

                    // ── Handwritten Chalk Annotation Challenge Quote ──
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "\"IS THIS KANJI HARD TO REMEMBER?\"",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "~~YES IT IS.~~ NO. WAY! IT'S A LIFESTYLE. 🌸",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    // On-Demand Metadata Info Row (Toggled via ℹ️ Info Icon)
                    AnimatedVisibility(
                        visible = showInfo,
                        enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                        exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                            ) {
                                item {
                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.secondaryContainer
                                    ) {
                                        Text(
                                            text = "🏷️ ${entry.category}",
                                            style = MaterialTheme.typography.labelMedium,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                                if (entry.subCategory.isNotBlank()) {
                                    item {
                                        val isPositive = entry.subCategory.contains("良い")
                                        Surface(
                                            shape = CircleShape,
                                            color = if (isPositive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                                        ) {
                                            Text(
                                                text = if (isPositive) "🌸 良い意味" else "⚡️ よくない意味",
                                                style = MaterialTheme.typography.labelMedium,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                                fontWeight = FontWeight.Bold,
                                                color = if (isPositive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
                                            )
                                        }
                                    }
                                }
                                if (entry.studyTag.isNotBlank()) {
                                    item {
                                        Surface(
                                            shape = CircleShape,
                                            color = MaterialTheme.colorScheme.tertiaryContainer
                                        ) {
                                            Text(
                                                text = "📅 ${entry.studyTag}",
                                                style = MaterialTheme.typography.labelMedium,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Example Sentence Bento Card
                    if (entry.exampleSentence.isNotBlank()) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLowest,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "例文 (Example Sentence)",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    IconButton(onClick = {
                                        clipboardManager.setText(AnnotatedString(entry.exampleSentence))
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copy Example",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = entry.exampleSentence,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 24.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Mastery Status Toggle Bar
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceContainer
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (entry.isMastered) Icons.Default.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = if (entry.isMastered) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (entry.isMastered) (if (isJapanese) "習得済み (Mastered)" else "Mastered") else (if (isJapanese) "学習中 (Learning)" else "Learning"),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (entry.isMastered) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            ExpressiveSwitch(
                                checked = entry.isMastered,
                                onCheckedChange = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onToggleMastered()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpressiveVocabAddSheet(
    categories: List<String>,
    onAddVocab: (kanji: String, reading: String, meaning: String, category: String, subCategory: String, tag: String, example: String) -> Unit,
    onCancel: () -> Unit
) {
    var kanji by remember { mutableStateOf("") }
    var reading by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(if (categories.isNotEmpty()) categories.first() else "人の性格や個性を表す言葉") }
    var subCategory by remember { mutableStateOf("良い意味で使われる言葉") }
    var customCategory by remember { mutableStateOf("") }
    var isCreatingNewCategory by remember { mutableStateOf(false) }
    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }
    var customSubCategory by remember { mutableStateOf("") }
    var isCreatingNewSubCategory by remember { mutableStateOf(false) }
    var isSubCategoryDropdownExpanded by remember { mutableStateOf(false) }
    val defaultSubCategories = remember {
        listOf(
            "良い意味で使われる言葉",
            "よくない意味で使われる言葉",
            "どちらの意味でも使われる言葉"
        )
    }
    var tag by remember { mutableStateOf("第1週・1日目") }
    var example by remember { mutableStateOf("") }

    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Expressive Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Translate,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "新規単語の追加",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Add JLPT N1 Vocabulary Entry",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onCancel) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── Bento Card 1: Core Word Inputs ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🈁 単語・読み・意味",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = kanji,
                    onValueChange = { kanji = it },
                    label = { Text("単語 (Kanji / Word)") },
                    placeholder = { Text("例: 幾帳面") },
                    leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = reading,
                    onValueChange = { reading = it },
                    label = { Text("読み方 (Furigana Reading)") },
                    placeholder = { Text("例: きちょうめん") },
                    leadingIcon = { Icon(Icons.Outlined.RecordVoiceOver, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = meaning,
                    onValueChange = { meaning = it },
                    label = { Text("意味 (Meaning)") },
                    placeholder = { Text("例: Meticulous, punctual") },
                    leadingIcon = { Icon(Icons.Outlined.Lightbulb, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Bento Card 2: Classification & Nuance ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🏷️ カテゴリ & ニュアンス",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = isCategoryDropdownExpanded,
                    onExpandedChange = { isCategoryDropdownExpanded = !isCategoryDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = if (isCreatingNewCategory) customCategory else category,
                        onValueChange = {
                            if (isCreatingNewCategory) customCategory = it
                        },
                        readOnly = !isCreatingNewCategory,
                        label = { Text("カテゴリ (Category)") },
                        leadingIcon = { Icon(Icons.Outlined.Folder, contentDescription = null) },
                        trailingIcon = {
                            if (isCreatingNewCategory) {
                                IconButton(onClick = { isCreatingNewCategory = false }) {
                                    Icon(Icons.Default.Close, contentDescription = "Cancel Custom Category")
                                }
                            } else {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownExpanded)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    ExposedDropdownMenu(
                        expanded = isCategoryDropdownExpanded && !isCreatingNewCategory,
                        onDismissRequest = { isCategoryDropdownExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text("🏷️ $cat", fontWeight = if (category == cat) FontWeight.Bold else FontWeight.Normal) },
                                onClick = {
                                    category = cat
                                    isCreatingNewCategory = false
                                    isCategoryDropdownExpanded = false
                                }
                            )
                        }
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("➕ 新規カテゴリを作成...", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
                            onClick = {
                                isCreatingNewCategory = true
                                isCategoryDropdownExpanded = false
                            }
                        )
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = isSubCategoryDropdownExpanded,
                    onExpandedChange = { isSubCategoryDropdownExpanded = !isSubCategoryDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = if (isCreatingNewSubCategory) customSubCategory else subCategory,
                        onValueChange = {
                            if (isCreatingNewSubCategory) customSubCategory = it
                        },
                        readOnly = !isCreatingNewSubCategory,
                        label = { Text("ニュアンス (SubCategory / Nuance)") },
                        leadingIcon = { Icon(Icons.Outlined.Psychology, contentDescription = null) },
                        trailingIcon = {
                            if (isCreatingNewSubCategory) {
                                IconButton(onClick = { isCreatingNewSubCategory = false }) {
                                    Icon(Icons.Default.Close, contentDescription = "Cancel Custom Nuance")
                                }
                            } else {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isSubCategoryDropdownExpanded)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    ExposedDropdownMenu(
                        expanded = isSubCategoryDropdownExpanded && !isCreatingNewSubCategory,
                        onDismissRequest = { isSubCategoryDropdownExpanded = false }
                    ) {
                        defaultSubCategories.forEach { subCat ->
                            val iconStr = when {
                                subCat.contains("良い") -> "🌸 "
                                subCat.contains("よくない") -> "⚡️ "
                                else -> "⚖️ "
                            }
                            DropdownMenuItem(
                                text = { Text("$iconStr$subCat", fontWeight = if (subCategory == subCat) FontWeight.Bold else FontWeight.Normal) },
                                onClick = {
                                    subCategory = subCat
                                    isCreatingNewSubCategory = false
                                    isSubCategoryDropdownExpanded = false
                                }
                            )
                        }
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("➕ 新規ニュアンスを作成...", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
                            onClick = {
                                isCreatingNewSubCategory = true
                                isSubCategoryDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Bento Card 3: Context & Example ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📝 学習コンテキスト & 例文",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = tag,
                    onValueChange = { tag = it },
                    label = { Text("学習タグ (Study Schedule Tag)") },
                    placeholder = { Text("例: 第1週・1日目") },
                    leadingIcon = { Icon(Icons.Outlined.CalendarToday, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = example,
                    onValueChange = { example = it },
                    label = { Text("例文 (Example Sentence)") },
                    placeholder = { Text("例: 彼は性格が幾帳面で、提出期限を一度も破ったことがない。") },
                    leadingIcon = { Icon(Icons.Outlined.MenuBook, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    maxLines = 3
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Vibrant Primary Action Hero Button
        Button(
            onClick = {
                if (kanji.isNotBlank()) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    val finalCategory = if (isCreatingNewCategory && customCategory.isNotBlank()) customCategory else category
                    val finalSubCategory = if (isCreatingNewSubCategory && customSubCategory.isNotBlank()) customSubCategory else subCategory
                    onAddVocab(kanji, reading, meaning, finalCategory, finalSubCategory, tag, example)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
            enabled = kanji.isNotBlank()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "登録する (Confirm Vocab Entry)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
