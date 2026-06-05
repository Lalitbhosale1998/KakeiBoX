package com.personal.kakeibox.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.personal.kakeibox.data.preferences.ThemeStyle
import com.personal.kakeibox.data.preferences.ThemeSettings
import com.personal.kakeibox.ui.settings.ThemeViewModel
import com.personal.kakeibox.ui.theme.LocalThemeStyle
import com.personal.kakeibox.ui.theme.terminalButton
import com.personal.kakeibox.ui.theme.glow
import com.personal.kakeibox.data.preferences.GlowIntensity

@Composable
fun KakeiboXLockScreen(
    themeSettings: ThemeSettings,
    themeViewModel: ThemeViewModel
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val isSpaceTerminal = themeSettings.themeStyle == ThemeStyle.RETRO_SPACE
    
    // Ambient breathing animation for the lock icon/halo
    val infiniteTransition = rememberInfiniteTransition(label = "lock_breathing")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    fun triggerUnlock() {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        val activity = context as? FragmentActivity
        val executor = ContextCompat.getMainExecutor(context)
        if (activity != null) {
            themeViewModel.authenticate(activity, executor)
        }
    }

    // Auto-trigger on creation
    LaunchedEffect(Unit) {
        val activity = context as? FragmentActivity
        val executor = ContextCompat.getMainExecutor(context)
        if (activity != null && !themeViewModel.isAuthenticated.value) {
            themeViewModel.authenticate(activity, executor)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Visual biometric lock halo
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                // Pulsing outer halo circle
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .graphicsLayer {
                            scaleX = pulseScale
                            scaleY = pulseScale
                            alpha = pulseAlpha
                        }
                        .background(
                            color = if (isSpaceTerminal) Color(0xFF46C2B4).copy(alpha = 0.15f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            shape = CircleShape
                        )
                )

                // Glow behind the icon if Retro Space style is active
                val glowModifier = if (isSpaceTerminal) {
                    Modifier.glow(
                        color = Color(0xFF46C2B4),
                        radius = 12.dp,
                        intensity = themeSettings.glowIntensity,
                        shape = CircleShape
                    )
                } else Modifier

                // Central Lock/Fingerprint Circle
                Surface(
                    shape = CircleShape,
                    color = if (isSpaceTerminal) Color(0xFF13182E) else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (isSpaceTerminal) Color(0xFF46C2B4) else MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .size(90.dp)
                        .then(glowModifier),
                    tonalElevation = 6.dp,
                    shadowElevation = if (isSpaceTerminal) 0.dp else 4.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Fingerprint,
                            contentDescription = "Security Unlock",
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Stylized cyber-grid text warnings / standard description text
            if (isSpaceTerminal) {
                Text(
                    text = "SYSTEM SECURED",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFF7E6B),
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "DECRYPTION AUTHENTICATION REQUIRED",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF46C2B4).copy(alpha = 0.7f),
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "App Locked",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Verify your identity using biometrics to access your financial data.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Stylized buttons
            if (isSpaceTerminal) {
                Button(
                    onClick = { triggerUnlock() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(56.dp)
                        .terminalButton(enabled = true, backgroundColor = Color(0xFFFF7E6B))
                ) {
                    Text(
                        text = "[ START DECRYPTION ]",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0C1020),
                        fontSize = 14.sp
                    )
                }
            } else {
                Button(
                    onClick = { triggerUnlock() },
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(56.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Fingerprint,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Unlock App",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
