package com.personal.kakeibox.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 🎭 EditorialPosterLayout
 * M3 Expressive Editorial Poster UI primitives inspired by high-contrast editorial poster designs.
 * Features:
 * 1. Monumental Stacked Condensed Typography (ALL-CAPS stacked lines).
 * 2. Inline Keyword Highlight Badges (Berry/Plum rounded pill badges embedded inline inside sentences).
 * 3. Editorial Quote Footer (Italicized quote section).
 */

@Composable
fun InlineKeywordBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onTertiaryContainer
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(6.dp)),
        color = backgroundColor,
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = text.uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelMedium,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            color = contentColor
        )
    }
}

@Composable
fun StackedCondensedHeader(
    lines: List<String>,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    fontSize: Int = 36,
    lineHeight: Int = 38
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        lines.forEach { line ->
            Text(
                text = line.uppercase(),
                fontSize = fontSize.sp,
                lineHeight = lineHeight.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-1).sp,
                fontFamily = FontFamily.SansSerif,
                color = color
            )
        }
    }
}

/**
 * Renders a flow row of text elements where specific words can be rendered as [InlineKeywordBadge].
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InlineEditorialSentence(
    textSegments: List<EditorialSegment>,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.Center
    ) {
        textSegments.forEach { segment ->
            when (segment) {
                is EditorialSegment.Text -> {
                    Text(
                        text = segment.content,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textColor,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                is EditorialSegment.Badge -> {
                    InlineKeywordBadge(
                        text = segment.keyword,
                        backgroundColor = segment.badgeColor ?: MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = segment.textColor ?: MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

sealed class EditorialSegment {
    data class Text(val content: String) : EditorialSegment()
    data class Badge(
        val keyword: String,
        val badgeColor: Color? = null,
        val textColor: Color? = null
    ) : EditorialSegment()
}

@Composable
fun EditorialQuoteFooter(
    quote: String,
    modifier: Modifier = Modifier,
    author: String? = null,
    quoteColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "\"$quote\"",
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Serif,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp,
                color = quoteColor
            )
            if (!author.isNullOrEmpty()) {
                Text(
                    text = "— $author",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
