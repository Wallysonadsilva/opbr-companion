package com.example.opbr_companion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.opbr_companion.ui.components.BottomNavBar
import com.example.opbr_companion.ui.screen.SupportScreen
import com.example.opbr_companion.ui.theme.OpbrcompanionTheme
import com.example.opbr_companion.viewmodel.SupportViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpbrcompanionTheme{
                MainScreen()
            }
        }
    }
}

@Preview
@Composable
fun MainScreen() {
    var selectedScreen by remember { mutableStateOf("Characters") }
    val supportViewModel: SupportViewModel = viewModel()

    Scaffold(
        bottomBar = { BottomNavBar(selectedScreen) { selectedScreen = it } },
        modifier = Modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center
        ) {
            when (selectedScreen) {
                "Characters" -> Text(text = "This is the Characters screen")
                "Support" -> SupportScreen(viewModel = supportViewModel)
                else -> Text(text = "Unknown screen")
            }
        }
    }
}