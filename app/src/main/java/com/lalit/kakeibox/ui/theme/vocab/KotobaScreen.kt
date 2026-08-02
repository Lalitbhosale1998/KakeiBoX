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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.personal.kakeibox.data.entity.VocabEntry
import com.personal.kakeibox.ui.components.ExpressiveSwitch
import com.personal.kakeibox.ui.theme.ExpressiveMotion
import com.personal.kakeibox.ui.theme.LocalThemeSettings
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

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    SharedTransitionLayout {
        val sharedTransitionScope = this

        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {},
                floatingActionButton = {
                    if (selectedVocabEntry == null) {
                        FloatingActionButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                showAddSheet = true
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(20.dp),
                            elevation = FloatingActionButtonDefaults.elevation(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add Vocabulary")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isJapanese) "単語追加" else "Add Vocab",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                AnimatedContent(
                    targetState = selectedVocabEntry,
                    transitionSpec = {
                        (fadeIn(tween(500, easing = ExpressiveMotion.EasingEmphasized)) +
                                scaleIn(initialScale = 0.90f, animationSpec = tween(500, easing = ExpressiveMotion.EasingEmphasized)))
                            .togetherWith(
                                fadeOut(tween(400, easing = ExpressiveMotion.EasingEmphasized)) +
                                        scaleOut(targetScale = 0.90f, animationSpec = tween(400, easing = ExpressiveMotion.EasingEmphasized))
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
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            Spacer(modifier = Modifier.height(100.dp)) // Header inset below floating top bar

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
                                shape = CircleShape
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
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
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

    with(sharedTransitionScope) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "vocab_card_${entry.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ -> tween(500, easing = ExpressiveMotion.EasingEmphasized) }
                )
                .clickable {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClickCard()
                },
            shape = RoundedCornerShape(22.dp),
            color = if (entry.isMastered) MaterialTheme.colorScheme.surfaceContainerLow else MaterialTheme.colorScheme.surfaceContainerHigh,
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
                                tint = if (entry.isStarred) Color(0xFFEAB308) else MaterialTheme.colorScheme.onSurfaceVariant
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

    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
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
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
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
                                onToggleStarred()
                            }) {
                                Icon(
                                    imageVector = if (entry.isStarred) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                    contentDescription = "Star",
                                    tint = if (entry.isStarred) Color(0xFFEAB308) else MaterialTheme.colorScheme.onSurfaceVariant
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

                    Spacer(modifier = Modifier.height(12.dp))

                    // Huge Furigana & Kanji Display
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (entry.furiganaReading.isNotBlank()) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                                modifier = Modifier.padding(bottom = 6.dp)
                            ) {
                                Text(
                                    text = entry.furiganaReading,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Text(
                            text = entry.kanjiWord,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = entry.meaning,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Nuance & Category Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
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
                        Spacer(modifier = Modifier.height(8.dp))
                        val isPositive = entry.subCategory.contains("良い")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (isPositive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                            ) {
                                Text(
                                    text = if (isPositive) "🌸 良い意味 (Positive)" else "⚡️ よくない意味 (Negative)",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPositive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }

                    if (entry.studyTag.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
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
    var tag by remember { mutableStateOf("第1週・1日目") }
    var example by remember { mutableStateOf("") }

    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(24.dp)
    ) {
        Text(
            text = "新規単語の追加 (Add JLPT N1 Vocab)",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(18.dp))

        OutlinedTextField(
            value = kanji,
            onValueChange = { kanji = it },
            label = { Text("単語 (Kanji / Word)") },
            placeholder = { Text("例: 幾帳面") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = reading,
            onValueChange = { reading = it },
            label = { Text("読み方 (Furigana Reading)") },
            placeholder = { Text("例: きちょうめん") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = meaning,
            onValueChange = { meaning = it },
            label = { Text("意味 (Meaning)") },
            placeholder = { Text("例: Meticulous, punctual") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category Selection / Creation
        Text(
            text = "カテゴリ (Category)",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))

        if (isCreatingNewCategory) {
            OutlinedTextField(
                value = customCategory,
                onValueChange = { customCategory = it },
                label = { Text("新しいカテゴリ名") },
                placeholder = { Text("例: 人の性格や個性を表す言葉") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { isCreatingNewCategory = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel Custom Category")
                    }
                },
                singleLine = true
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat) }
                    )
                }
                item {
                    FilterChip(
                        selected = false,
                        onClick = { isCreatingNewCategory = true },
                        label = { Text("➕ 新規カテゴリ") },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SubCategory Selection (Nuance)
        Text(
            text = "ニュアンス (SubCategory / Nuance)",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = subCategory == "良い意味で使われる言葉",
                onClick = { subCategory = "良い意味で使われる言葉" },
                label = { Text("🌸 良い意味") }
            )
            FilterChip(
                selected = subCategory == "よくない意味で使われる言葉",
                onClick = { subCategory = "よくない意味で使われる言葉" },
                label = { Text("⚡️ よくない意味") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Study Schedule Tag
        OutlinedTextField(
            value = tag,
            onValueChange = { tag = it },
            label = { Text("学習タグ (Study Schedule Tag)") },
            placeholder = { Text("例: 第1週・1日目") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = example,
            onValueChange = { example = it },
            label = { Text("例文 (Example Sentence)") },
            placeholder = { Text("例: 彼は性格が幾帳面で、提出期限を一度も破ったことがない。") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (kanji.isNotBlank()) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    val finalCategory = if (isCreatingNewCategory && customCategory.isNotBlank()) customCategory else category
                    onAddVocab(kanji, reading, meaning, finalCategory, subCategory, tag, example)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            enabled = kanji.isNotBlank()
        ) {
            Text(
                text = "✓ 登録する (Confirm Entry)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
