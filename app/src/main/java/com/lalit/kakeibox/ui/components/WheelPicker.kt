package com.personal.kakeibox.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpressiveWheelPicker(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 3
) {
    val haptic = LocalHapticFeedback.current
    val itemHeight = 48.dp
    val density = LocalDensity.current
    val itemHeightPx = with(density) { itemHeight.toPx() }
    
    // Snapping states
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState)
    
    // Compute the center index dynamically
    val centerItemIndex by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) 0
            else {
                val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                visibleItemsInfo.minByOrNull { Math.abs((it.offset + it.size / 2) - center) }?.index ?: 0
            }
        }
    }
    
    // Scroll to external updates
    LaunchedEffect(selectedIndex) {
        if (selectedIndex >= 0 && selectedIndex < items.size) {
            lazyListState.animateScrollToItem(selectedIndex)
        }
    }
    
    // Trigger callbacks and haptics on selection changes
    LaunchedEffect(centerItemIndex) {
        if (centerItemIndex != selectedIndex && centerItemIndex in items.indices) {
            onItemSelected(centerItemIndex)
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }
    
    Box(
        modifier = modifier
            .height(itemHeight * visibleItemsCount)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Selection overlay beam
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08.toFloat())
        ) {}
        
        val centerPadding = itemHeight * (visibleItemsCount / 2)
        
        LazyColumn(
            state = lazyListState,
            flingBehavior = snapFlingBehavior,
            contentPadding = PaddingValues(top = centerPadding, bottom = centerPadding),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items.size) { index ->
                val scale by remember {
                    derivedStateOf {
                        val layoutInfo = lazyListState.layoutInfo
                        val visibleItemsInfo = layoutInfo.visibleItemsInfo
                        val itemInfo = visibleItemsInfo.find { it.index == index }
                        if (itemInfo == null) 0.8f
                        else {
                            val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                            val distance = Math.abs((itemInfo.offset + itemInfo.size / 2) - center)
                            (1f - (distance.toFloat() / center).coerceIn(0f, 0.3f))
                        }
                    }
                }
                
                val alpha by remember {
                    derivedStateOf {
                        val layoutInfo = lazyListState.layoutInfo
                        val visibleItemsInfo = layoutInfo.visibleItemsInfo
                        val itemInfo = visibleItemsInfo.find { it.index == index }
                        if (itemInfo == null) 0.3f
                        else {
                            val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                            val distance = Math.abs((itemInfo.offset + itemInfo.size / 2) - center)
                            (1f - (distance.toFloat() / center).coerceIn(0f, 0.7f))
                        }
                    }
                }

                val rotation by remember {
                    derivedStateOf {
                        val layoutInfo = lazyListState.layoutInfo
                        val visibleItemsInfo = layoutInfo.visibleItemsInfo
                        val itemInfo = visibleItemsInfo.find { it.index == index }
                        if (itemInfo == null) 0f
                        else {
                            val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                            val offsetFromCenter = (itemInfo.offset + itemInfo.size / 2) - center
                            -(offsetFromCenter.toFloat() / center) * 45f
                        }
                    }
                }
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                            rotationX = rotation
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = items[index],
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = if (index == centerItemIndex) FontWeight.Black else FontWeight.Bold,
                        color = if (index == centerItemIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun ExpressiveDatePicker(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val months = remember {
        (1..12).map { 
            LocalDate.of(2024, it, 1).month.getDisplayName(TextStyle.SHORT, Locale.getDefault()) 
        }
    }
    
    val currentYear = LocalDate.now().year
    val years = remember { (currentYear - 100..currentYear + 5).map { it.toString() } }
    
    var tempYearIndex by remember(selectedDate) {
        val index = years.indexOf(selectedDate.year.toString())
        mutableStateOf(if (index >= 0) index else years.size - 1)
    }
    
    var tempMonthIndex by remember(selectedDate) {
        mutableStateOf(selectedDate.monthValue - 1)
    }
    
    val days = remember(tempYearIndex, tempMonthIndex) {
        val y = years[tempYearIndex].toInt()
        val m = tempMonthIndex + 1
        val length = LocalDate.of(y, m, 1).lengthOfMonth()
        (1..length).map { it.toString() }
    }
    
    var tempDayIndex by remember(selectedDate, days) {
        val dayStr = selectedDate.dayOfMonth.toString()
        val index = days.indexOf(dayStr)
        mutableStateOf(if (index >= 0) index else days.size - 1)
    }
    
    // Propagate updates to outer selection
    fun triggerDateUpdate() {
        val y = years[tempYearIndex].toInt()
        val m = tempMonthIndex + 1
        val maxDays = LocalDate.of(y, m, 1).lengthOfMonth()
        val d = (tempDayIndex + 1).coerceIn(1, maxDays)
        
        onDateChange(LocalDate.of(y, m, d))
    }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Month Selector
        ExpressiveWheelPicker(
            items = months,
            selectedIndex = tempMonthIndex,
            onItemSelected = {
                tempMonthIndex = it
                triggerDateUpdate()
            },
            modifier = Modifier.weight(1.2f)
        )
        
        // Day Selector
        ExpressiveWheelPicker(
            items = days,
            selectedIndex = tempDayIndex,
            onItemSelected = {
                tempDayIndex = it
                triggerDateUpdate()
            },
            modifier = Modifier.weight(0.8f)
        )
        
        // Year Selector
        ExpressiveWheelPicker(
            items = years,
            selectedIndex = tempYearIndex,
            onItemSelected = {
                tempYearIndex = it
                triggerDateUpdate()
            },
            modifier = Modifier.weight(1f)
        )
    }
}
