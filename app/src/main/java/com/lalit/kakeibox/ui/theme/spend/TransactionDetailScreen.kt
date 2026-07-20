package com.personal.kakeibox.ui.theme.spend

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.personal.kakeibox.ui.spend.SpendViewModel

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.TransactionDetailScreen(
    transactionId: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit,
    viewModel: SpendViewModel = hiltViewModel()
) {
    // In a real app we would load the transaction from DB. For now, we just mock UI.
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction Details", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // The Hero Shared Element
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(key = "transaction_$transactionId"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(32.dp))
                                .background(MaterialTheme.colorScheme.tertiaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "T",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Supermarket",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "-$24.50",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "This beautiful layout was physically morphed from the list item you just tapped using Compose 1.7 Shared Elements!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
