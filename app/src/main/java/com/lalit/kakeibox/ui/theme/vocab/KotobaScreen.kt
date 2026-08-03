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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

    val lazyListState = rememberLazyListState()
    val isFabExpanded by remember {
        derivedStateOf {
            !lazyListState.isScrollInProgress || lazyListState.firstVisibleItemIndex == 0
        }
    }

    val systemDark = isSystemInDarkTheme()
    val isDark = themeSettings.darkThemePreference.isDark(systemDark)
    val isPrimaryContainer = themeSettings.topAppBarBackground == TopAppBarBackground.PRIMARY_CONTAINER
    val topAppBarContainerColor = MaterialTheme.colorScheme.primaryContainer

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
                    pattern = themeSettings.backdropPattern
                )
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = {},
                floatingActionButton = {
                    if (selectedVocabEntry == null) {
                        ExtendedFloatingActionButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                showAddSheet = true
                            },
                            expanded = isFabExpanded,
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.Translate,
                                    contentDescription = "Add Vocabulary"
                                )
                            },
                            text = {
                                Text(
                                    text = if (isJapanese) "単語追加" else "Add Vocab",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(24.dp),
                            elevation = FloatingActionButtonDefaults.elevation(8.dp)
                        )
                    }
                }
            ) { innerPadding ->
                AnimatedContent(
                    targetState = selectedVocabEntry,
                    transitionSpec = {
                        (fadeIn(ExpressivePhysics.fluidBouncy()) +
                                scaleIn(initialScale = 0.90f, animationSpec = ExpressivePhysics.fluidBouncy()))
                            .togetherWith(
                                fadeOut(ExpressivePhysics.fluidBouncy()) +
                                        scaleOut(targetScale = 0.90f, animationSpec = ExpressivePhysics.fluidBouncy())
                            )
                    },
                    label = "kotoba_container_transform"
                ) { targetEntry ->
                    if (targetEntry == null) {
                        // ── Main List View ──
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

                            Spacer(modifier = Modifier.height(8.dp))

                            // ── Vocab Entry List / Cards ──
                            if (filteredEntries.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.Translate,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                            modifier = Modifier.size(64.dp)
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = if (isJapanese) "単語がありません" else "No Vocabulary Entries",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = if (isJapanese) "右下の「単語追加」ボタンからJLPT N1単語を登録しましょう！" else "Tap '+ Add Vocab' at the bottom right to register JLPT N1 words!",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            } else {
                                LazyColumn(
                                    state = lazyListState,
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = innerPadding.calculateBottomPadding() + 130.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(filteredEntries, key = { it.id }) { entry ->
                                        VocabCardItem(
                                            entry = entry,
                                            sharedTransitionScope = sharedTransitionScope,
                                            animatedVisibilityScope = this@AnimatedContent,
                                            onClickCard = { selectedVocabEntry = entry },
                                            onToggleMastered = { viewModel.toggleMasteredStatus(entry) },
                                            onToggleStarred = { viewModel.toggleStarredStatus(entry) },
                                            onDelete = { viewModel.deleteVocabEntry(entry) }
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // ── Expanded Container Detail View ──
                        val liveEntry = allEntries.find { it.id == targetEntry.id } ?: targetEntry
                        ExpressiveVocabDetailView(
                            entry = liveEntry,
                            isJapanese = isJapanese,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = this@AnimatedContent,
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

    // ── Add Vocab Bottom Sheet ──
    if (showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddSheet = false },
            sheetState = sheetState,
            modifier = Modifier.statusBarsPadding()
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

                // Tags Row
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item {
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
                        item {
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
                    }
                    if (entry.studyTag.isNotBlank()) {
                        item {
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
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(key = "vocab_card_${entry.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ -> tween(500, easing = ExpressiveMotion.EasingEmphasized) }
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
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
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
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
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
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
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
