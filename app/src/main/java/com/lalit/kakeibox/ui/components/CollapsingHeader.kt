package com.personal.kakeibox.ui.components

import androidx.compose.animation.core.spring
import androidx.compose.animation.core.animateFloatAsState
import com.personal.kakeibox.ui.theme.ExpressivePhysics
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clipToBounds

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
        animationSpec = ExpressivePhysics.fluidSnappy(),
        label = "header_progress"
    )
    
    val density = LocalDensity.current
    val statusBarPadding = with(density) { WindowInsets.statusBars.getTop(this).toDp() }
    val height = (180.dp + statusBarPadding) - (100.dp * animatedProgress)
    
    // Hyper-expressive typography scaling
    val scale = 1f - (0.4f * animatedProgress)
    val letterSpacing = (-4f + (4f * animatedProgress)).sp
    
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
                    .weight(1f)
                    .clipToBounds(),
                verticalArrangement = Arrangement.Center
            ) {
                if (animatedProgress < 0.6f) {
                    Text(
                        text = title.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp,
                        color = onContainerColor.copy(alpha = (1f - animatedProgress / 0.6f).coerceIn(0f, 1f))
                    )
                }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = if (animatedProgress >= 0.6f) 24.sp else 68.sp,
                        letterSpacing = letterSpacing
                    ),
                    fontWeight = FontWeight.Black,
                    color = primaryTextAccent,
                    maxLines = 1
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
