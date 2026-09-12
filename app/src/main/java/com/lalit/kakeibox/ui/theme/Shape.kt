package com.personal.kakeibox.ui.theme

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Material 3 Expressive Shape scale for KakeiboX.
 * Employs asymmetric continuous curves and exaggerated corner radii for anti-bento organic surfaces.
 */
val KakeiboXShapes = Shapes(
    extraSmall = RoundedCornerShape(CornerSize(8.dp)),
    small = RoundedCornerShape(topStart = CornerSize(16.dp), bottomEnd = CornerSize(16.dp), topEnd = CornerSize(6.dp), bottomStart = CornerSize(6.dp)),
    medium = RoundedCornerShape(topStart = CornerSize(28.dp), bottomEnd = CornerSize(28.dp), topEnd = CornerSize(12.dp), bottomStart = CornerSize(12.dp)),
    large = RoundedCornerShape(CornerSize(36.dp)),
    extraLarge = RoundedCornerShape(topStart = CornerSize(48.dp), topEnd = CornerSize(48.dp), bottomStart = CornerSize(16.dp), bottomEnd = CornerSize(16.dp))
)
