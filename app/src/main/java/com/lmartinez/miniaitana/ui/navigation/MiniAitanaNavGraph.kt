package com.lmartinez.miniaitana.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lmartinez.miniaitana.feature.model.ModelScreen
import com.lmartinez.miniaitana.feature.prompt.PromptScreen
import com.lmartinez.miniaitana.feature.whitelist.WhitelistScreen
import com.lmartinez.miniaitana.ui.theme.Charcoal
import com.lmartinez.miniaitana.ui.theme.DeepCarbon
import com.lmartinez.miniaitana.ui.theme.HackerCyan
import com.lmartinez.miniaitana.ui.theme.MutedGray
import com.lmartinez.miniaitana.ui.theme.TextPrimary
import com.lmartinez.miniaitana.ui.theme.TextSecondary

private data class NavItem(val screen: Screen, val label: String, val icon: ImageVector)

private val navItems = listOf(
    NavItem(Screen.Whitelist, "Whitelist", Icons.AutoMirrored.Filled.List),
    NavItem(Screen.Prompt,    "Prompt",    Icons.Default.Person),
    NavItem(Screen.Model,     "Model",     Icons.Default.Settings),
)

@Composable
fun MiniAitanaNavGraph() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentDest = backStack?.destination

    Scaffold(
        containerColor = DeepCarbon,
        contentColor = TextPrimary,
        bottomBar = {
            NavigationBar(
                modifier = Modifier.border(BorderStroke(1.dp, MutedGray)),
                containerColor = Charcoal,
                contentColor = TextPrimary,
                tonalElevation = 0.dp,
            ) {
                navItems.forEach { item ->
                    val selected = currentDest?.hierarchy?.any { it.route == item.screen.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick  = {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState    = true
                            }
                        },
                        icon  = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepCarbon,
                            selectedTextColor = HackerCyan,
                            indicatorColor = HackerCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Screen.Whitelist.route,
            modifier         = Modifier.padding(innerPadding),
        ) {
            composable(Screen.Whitelist.route) { WhitelistScreen() }
            composable(Screen.Prompt.route)    { PromptScreen() }
            composable(Screen.Model.route)     { ModelScreen() }
        }
    }
}
