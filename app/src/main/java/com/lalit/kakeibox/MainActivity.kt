package com.personal.kakeibox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.personal.kakeibox.ui.KakeiboXAppRoot
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Android 16 edge-to-edge is default — this enables it properly
        setContent {
            KakeiboXAppRoot()
        }
    }
}