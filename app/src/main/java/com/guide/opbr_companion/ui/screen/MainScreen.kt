package com.guide.opbr_companion.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.guide.opbr_companion.R
import com.guide.opbr_companion.ui.components.BottomNavBar
import com.guide.opbr_companion.ui.components.DialogAlertInfo
import com.guide.opbr_companion.ui.components.TopBar
import com.guide.opbr_companion.ui.viewmodel.CharacterViewModel
import com.guide.opbr_companion.ui.viewmodel.MedalViewModel
import com.guide.opbr_companion.ui.viewmodel.SupportViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: ""

    val showTopBar =
        currentRoute == "character_list" || currentRoute == "support" || currentRoute == "medal_sets"
    val showBottomBar = showTopBar

    var showFilterBar by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val supportViewModel: SupportViewModel = viewModel()
    val characterViewModel: CharacterViewModel = viewModel()
    val medalViewModel: MedalViewModel = viewModel()

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopBar(
                    title = when (currentRoute) {
                        "support" -> "Support"
                        "medal_sets" -> "Medal Sets"
                        else -> "Characters"
                    },
                    onLeftIconClick = { showFilterBar = !showFilterBar },
                    onRightIconClick = { showInfoDialog = true }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(currentRoute) { route ->
                    showFilterBar = false
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {

            if (showInfoDialog) {
                val alertMessage = when (currentRoute) {
                    "support" -> R.string.support_info_alert
                    "character_list" -> R.string.character_info_alert
                    else -> R.string.medals_info_alert
                }

                DialogAlertInfo(
                    message = stringResource(alertMessage),
                    onDismiss = { showInfoDialog = false }
                )
            }

            NavHost(navController, startDestination = "character_list") {
                composable("character_list") {
                    CharacterScreen(
                        viewModel = characterViewModel,
                        showFilterBar = showFilterBar,
                        onCharacterClick = { id ->
                            navController.navigate("character_detail/$id")
                        }
                    )
                }

                composable(
                    "character_detail/{characterId}",
                    arguments = listOf(navArgument("characterId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("characterId")
                    if (id != null) {
                        CharacterDetailScreen(
                            characterId = id,
                            viewModel = characterViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    } else {
                        Text("Character not found", color = Color.Red)
                    }
                }

                composable("support") {
                    SupportScreen(
                        viewModel = supportViewModel,
                        showFilterBar = showFilterBar
                    )
                }

                composable("medal_sets") {
                    MedalScreen(
                        viewModel = medalViewModel,
                        showFilterBar = showFilterBar,
                    )
                }
            }
        }
    }
}

