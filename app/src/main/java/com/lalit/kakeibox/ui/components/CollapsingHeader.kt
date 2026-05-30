package com.personal.kakeibox.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun ExpressiveCollapsingHeader(
    title: String,
    subtitle: String,
    scrollOffset: Float,
    maxOffset: Float,
    containerColor: Color,
    onContainerColor: Color,
    primaryTextAccent: Color,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    val progress = (scrollOffset / maxOffset).coerceIn(0f, 1f)
    
    // Spring animate the height and text scale
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow),
        label = "header_progress"
    )
    
    val density = LocalDensity.current
    val statusBarPadding = with(density) { WindowInsets.statusBars.getTop(this).toDp() }
    val height = (150.dp + statusBarPadding) - (70.dp * animatedProgress)
    val scale = 1f - (0.25f * animatedProgress)
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        color = containerColor,
        tonalElevation = (4.dp * animatedProgress)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        transformOrigin = TransformOrigin(0f, 0.5f)
                    }
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                if (animatedProgress < 0.6f) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = onContainerColor.copy(alpha = (1f - animatedProgress / 0.6f).coerceIn(0f, 1f))
                    )
                }
                Text(
                    text = subtitle,
                    style = if (animatedProgress >= 0.6f) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = primaryTextAccent
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                actions()
            }
        }
    }
}
