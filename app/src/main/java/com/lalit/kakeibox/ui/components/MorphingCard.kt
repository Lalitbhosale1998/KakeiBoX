package com.personal.kakeibox.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath

class MorphPolygonShape(
    private val morph: Morph,
    private val progress: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val matrix = android.graphics.Matrix()
        matrix.setScale(size.width / 2f, size.height / 2f)
        matrix.postTranslate(size.width / 2f, size.height / 2f)

        val path = morph.toPath(progress = progress).apply {
            transform(matrix)
        }
        return Outline.Generic(path.asComposePath())
    }
}

@Composable
fun MorphingCard(
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    collapsedColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    expandedColor: Color = MaterialTheme.colorScheme.primaryContainer,
    content: @Composable (progress: Float) -> Unit
) {
    // A soft squircle (circle-like) for collapsed state
    val collapsedPolygon = remember {
        RoundedPolygon(
            numVertices = 4,
            rounding = CornerRounding(radius = 0.5f, smoothing = 1f)
        )
    }

    // A rigid rectangle for expanded state
    val expandedPolygon = remember {
        RoundedPolygon(
            numVertices = 4,
            rounding = CornerRounding(radius = 0.1f, smoothing = 0f)
        )
    }

    val morph = remember {
        Morph(collapsedPolygon, expandedPolygon)
    }

    val morphProgress by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "morphProgress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MorphPolygonShape(morph, morphProgress))
            .background(if (isExpanded) expandedColor else collapsedColor)
            .clickable { onClick() }
            .padding(24.dp)
    ) {
        content(morphProgress)
    }
}
