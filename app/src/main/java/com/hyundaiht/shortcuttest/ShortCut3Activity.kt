package com.hyundaiht.shortcuttest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.hyundaiht.shortcuttest.ui.theme.ShortCutTestTheme

class ShortCut3Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShortCutTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize().background(Color.Magenta)) { innerPadding ->
                    Text(modifier = Modifier.padding(innerPadding), text = "ShortCut3Activity")
                }
            }
        }
    }
}