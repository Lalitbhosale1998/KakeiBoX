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
import androidx.compose.ui.platform.LocalHapticFeedback
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

@OptIn(ExperimentalMaterial3Api::class)
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
    val studyTags by viewModel.availableStudyTags.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedTag by viewModel.selectedStudyTag.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val isJapanese = themeSettings.appLanguage == com.personal.kakeibox.data.preferences.AppLanguage.JAPANESE

    var showAddSheet by remember { mutableStateOf(false) }
    var isQuizMode by remember { mutableStateOf(false) }

    val masteredCount = allEntries.count { it.isMastered }
    val totalCount = allEntries.size
    val progressPct = if (totalCount > 0) (masteredCount.toFloat() / totalCount) else 0f

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {},
        floatingActionButton = {
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Spacer(modifier = Modifier.height(100.dp)) // Header inset below floating top bar

            // ── JLPT N1 Mastery Hero Bento Card ──
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                shadowElevation = 6.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isJapanese) "言葉 • Kotoba Studio" else "Kotoba Studio",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "JLPT N1 Vocabulary Mastery",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isQuizMode = !isQuizMode
                        }) {
                            Icon(
                                imageVector = if (isQuizMode) Icons.Default.ViewList else Icons.Default.Style,
                                contentDescription = "Toggle Quiz Mode",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isJapanese) "進捗状況" else "Mastery Progress",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (isJapanese) "$masteredCount / $totalCount 習得 (${(progressPct * 100).toInt()}%)" else "$masteredCount / $totalCount Mastered (${(progressPct * 100).toInt()}%)",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progressPct },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceContainerLowest
                    )
                }
            }

            // ── Search & Filter Bar ──
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
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

            // ── Category Filter Chips ──
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.setSelectedCategory(category)
                        },
                        label = {
                            Text(
                                text = if (category == "All") (if (isJapanese) "🏷️ すべてのカテゴリ" else "🏷️ All Categories") else "🏷️ $category",
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        shape = CircleShape,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }

            // ── Study Schedule Tag Chips ──
            if (studyTags.size > 1) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(studyTags) { tag ->
                        val isSelected = tag == selectedTag
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.setSelectedStudyTag(tag)
                            },
                            label = {
                                Text(
                                    text = if (tag == "All") (if (isJapanese) "📅 全日程" else "📅 All Schedules") else "📅 $tag",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            shape = CircleShape
                        )
                    }
                }
            }

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
                            onToggleMastered = { viewModel.toggleMasteredStatus(entry) },
                            onToggleStarred = { viewModel.toggleStarredStatus(entry) },
                            onDelete = { viewModel.deleteVocabEntry(entry) }
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
                onAddVocab = { kanji, reading, meaning, category, tag, example ->
                    viewModel.addVocabEntry(kanji, reading, meaning, category, tag, example)
                    showAddSheet = false
                },
                onCancel = { showAddSheet = false }
            )
        }
    }
}

@Composable
fun VocabCardItem(
    entry: VocabEntry,
    onToggleMastered: () -> Unit,
    onToggleStarred: () -> Unit,
    onDelete: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                isExpanded = !isExpanded
            },
        shape = RoundedCornerShape(20.dp),
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

            // Example Sentence (Expanded)
            if (isExpanded && entry.exampleSentence.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                ) {
                    Text(
                        text = "例文: ${entry.exampleSentence}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tags & Mastery Switch Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (entry.isMastered) "習得済み" else "学習中",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (entry.isMastered) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    ExpressiveSwitch(
                        checked = entry.isMastered,
                        onCheckedChange = { onToggleMastered() }
                    )
                }
            }
        }
    }
}

@Composable
fun ExpressiveVocabAddSheet(
    categories: List<String>,
    onAddVocab: (kanji: String, reading: String, meaning: String, category: String, tag: String, example: String) -> Unit,
    onCancel: () -> Unit
) {
    var kanji by remember { mutableStateOf("") }
    var reading by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(if (categories.isNotEmpty()) categories.first() else "人の性格や個性を表す言葉") }
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
                    onAddVocab(kanji, reading, meaning, finalCategory, tag, example)
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
