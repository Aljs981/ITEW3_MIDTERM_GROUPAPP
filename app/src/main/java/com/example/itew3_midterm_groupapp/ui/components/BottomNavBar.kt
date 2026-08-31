package com.example.itew3_midterm_groupapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.itew3_midterm_groupapp.ui.theme.SulyapGreenSelected
import com.example.itew3_midterm_groupapp.ui.theme.SulyapHeaderBg

enum class SulyapTab(val route: String, val label: String) {
    HOME("home", "HOME"),
    LOGS("history_logs", "LOGS")
}

@Composable
fun BottomNavBar(
    currentTab: SulyapTab,
    onTabSelected: (SulyapTab) -> Unit
) {
    // "Glass" look: translucent background.
    // Lower the alpha (e.g., 0.5f - 0.7f) for a more transparent glass effect.
    NavigationBar(
        containerColor = SulyapHeaderBg.copy(alpha = 0.65f),
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBarItem(
            selected = currentTab == SulyapTab.HOME,
            onClick = { onTabSelected(SulyapTab.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SulyapGreenSelected, // Solid white when selected
                selectedTextColor = SulyapGreenSelected,
                unselectedIconColor = Color.White.copy(alpha = 0.6f), // Slightly transparent when not selected
                unselectedTextColor = Color.White.copy(alpha = 0.6f),
                indicatorColor = Color.Transparent // Keeps the background clean behind the icon
            )
        )
        NavigationBarItem(
            selected = currentTab == SulyapTab.LOGS,
            onClick = { onTabSelected(SulyapTab.LOGS) },
            icon = { Icon(Icons.Default.List, contentDescription = "Logs") },
            label = { Text("Logs", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SulyapGreenSelected, // Solid white when selected
                selectedTextColor = SulyapGreenSelected,
                unselectedIconColor = Color.White.copy(alpha = 0.6f), // Slightly transparent when not selected
                unselectedTextColor = Color.White.copy(alpha = 0.6f),
                indicatorColor = Color.Transparent // Keeps the background clean behind the icon
            )
        )
    }
}