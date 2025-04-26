package com.guide.opbr_companion

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.guide.opbr_companion.ui.screen.MainScreen
import com.guide.opbr_companion.ui.theme.OpbrcompanionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        try {
            setContent {
                OpbrcompanionTheme {
                    Surface(
                        modifier = Modifier.statusBarsPadding()
                    ) {
                        MainScreen()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error setting content", e)
            // Optionally show fallback UI or finish activity gracefully
        }
    }
}
