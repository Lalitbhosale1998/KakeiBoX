package com.personal.kakeibox.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.kakeibox.util.DateUtils

@Composable
fun ExpressivePeriodSelector(
    selectedMonth: Int,
    selectedYear: Int,
    onMonthChange: (Int) -> Unit,
    onYearChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Selection Period",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Month Selection
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items((1..12).toList()) { month ->
                    val isSelected = selectedMonth == month
                    
                    val backgroundColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                        label = "month_bg"
                    )
                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                        label = "month_content"
                    )
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.15f else 1f,
                        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                        label = "month_scale"
                    )

                    Surface(
                        onClick = { 
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onMonthChange(month) 
                        },
                        color = backgroundColor,
                        contentColor = contentColor,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .size(width = 72.dp, height = 48.dp)
                            .graphicsLayer(scaleX = scale, scaleY = scale)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = DateUtils.getShortMonthName(month),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Year Selection
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(DateUtils.getYearRange()) { year ->
                    val isSelected = selectedYear == year
                    
                    val backgroundColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent,
                        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                        label = "year_bg"
                    )
                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant,
                        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                        label = "year_content"
                    )
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.15f else 1f,
                        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                        label = "year_scale"
                    )

                    Surface(
                        onClick = { 
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onYearChange(year) 
                        },
                        color = backgroundColor,
                        contentColor = contentColor,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .graphicsLayer(scaleX = scale, scaleY = scale)
                    ) {
                        Text(
                            text = year.toString(),
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
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
    val isNeed = selectedCategory == "NEED"
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(24.dp))
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
                .clickable { onCategoryChange("NEED") },
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
                .clickable { onCategoryChange("WANT") },
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
    onClick: () -> Unit
) {
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

    Surface(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale),
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
        contentColor = txtColor
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
            )
        }
    }
}

@Composable
fun ExpressiveEmptyState(
    message: String,
    modifier: Modifier = Modifier,
    icon: String = "✨"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = icon,
            fontSize = 64.sp,
            modifier = Modifier.graphicsLayer {
                rotationZ = -10f
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Time to add something new!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BentoCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    enabled: Boolean = true,
    isActive: Boolean = false,
    activeContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    activeContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    idleContainerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    idleContentColor: Color = MaterialTheme.colorScheme.onSurface,
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

    // Icon rotation micro-interaction
    val iconRotation by animateFloatAsState(
        targetValue = if (isActive) 12f else 0f,
        animationSpec = spring(Spring.DampingRatioHighBouncy, Spring.StiffnessLow),
        label = "icon_rotation"
    )

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .then(if (onClick != null && enabled) Modifier.clickable { 
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick() 
            } else Modifier),
        color = backgroundColor,
        contentColor = contentColor,
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
                    color = (if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
                        .copy(alpha = if (enabled) 1f else 0.4f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(8.dp).graphicsLayer {
                            rotationZ = iconRotation
                        }
                    )
                }
            }
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Black,
                    color = contentColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                content()
            }
        }
    }
}
