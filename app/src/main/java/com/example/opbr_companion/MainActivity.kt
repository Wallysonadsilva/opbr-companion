package com.example.opbr_companion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.opbr_companion.ui.screen.MainScreen
import com.example.opbr_companion.ui.theme.OpbrcompanionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpbrcompanionTheme{
                Surface(
                    modifier = Modifier.statusBarsPadding()
                ) {
                    MainScreen()
                }
            }
        }
    }
}
