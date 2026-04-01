package com.conekt.suite.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.conekt.suite.feature.canvas.CanvasScreen
import com.conekt.suite.feature.library.MusicScreen
import com.conekt.suite.feature.profile.ProfileScreen
import com.conekt.suite.feature.pulse.PulseScreen
import com.conekt.suite.feature.vault.VaultScreen

@Composable
fun ConektNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.PULSE,
        modifier = modifier
    ) {
        composable(Routes.PULSE) { PulseScreen() }
        composable(Routes.VAULT) { VaultScreen() }
        composable(Routes.CANVAS) { CanvasScreen() }
        composable(Routes.MUSIC) { MusicScreen() }
        composable(Routes.PROFILE) { ProfileScreen() }
    }
}