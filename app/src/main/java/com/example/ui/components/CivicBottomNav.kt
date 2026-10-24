package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import com.example.ui.state.AppLanguage
import com.example.ui.state.LanguageManager
import com.example.ui.state.ScreenTab
import com.example.ui.theme.CivicGreenContainer
import com.example.ui.theme.CivicGreenPrimary
import com.example.ui.theme.OnCivicGreenContainer

@Composable
fun CivicBottomNav(
    selectedTab: ScreenTab,
    currentLanguage: AppLanguage,
    onTabSelected: (ScreenTab) -> Unit
) {
    NavigationBar(
        windowInsets = WindowInsets.navigationBars,
        containerColor = Color.White,
        modifier = Modifier.testTag("civic_bottom_navigation")
    ) {
        // Core Household screen (Overview / Entitlements)
        val isHouseholdSelected = selectedTab == ScreenTab.OVERVIEW || selectedTab == ScreenTab.ENTITLEMENTS
        NavigationBarItem(
            selected = isHouseholdSelected,
            onClick = { onTabSelected(ScreenTab.OVERVIEW) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Household") },
            label = { Text("Household", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OnCivicGreenContainer,
                indicatorColor = CivicGreenContainer,
                selectedTextColor = CivicGreenPrimary
            ),
            modifier = Modifier.testTag("tab_household")
        )

        // Core Portability screen
        NavigationBarItem(
            selected = selectedTab == ScreenTab.PORTABILITY,
            onClick = { onTabSelected(ScreenTab.PORTABILITY) },
            icon = { Icon(Icons.Default.NearMe, contentDescription = "Portability") },
            label = { Text(LanguageManager.getString("nav_portability", currentLanguage), fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OnCivicGreenContainer,
                indicatorColor = CivicGreenContainer,
                selectedTextColor = CivicGreenPrimary
            ),
            modifier = Modifier.testTag("tab_portability")
        )

        // Core Safety Simulator screen
        NavigationBarItem(
            selected = selectedTab == ScreenTab.SAFETY_SIMULATOR,
            onClick = { onTabSelected(ScreenTab.SAFETY_SIMULATOR) },
            icon = { Icon(Icons.Default.BuildCircle, contentDescription = "Safety Sim") },
            label = { Text("Safety Sim", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OnCivicGreenContainer,
                indicatorColor = CivicGreenContainer,
                selectedTextColor = CivicGreenPrimary
            ),
            modifier = Modifier.testTag("tab_simulator")
        )

        // Core Grievance screen
        NavigationBarItem(
            selected = selectedTab == ScreenTab.GRIEVANCES,
            onClick = { onTabSelected(ScreenTab.GRIEVANCES) },
            icon = { Icon(Icons.Default.Gavel, contentDescription = "Grievances") },
            label = { Text("Grievance", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OnCivicGreenContainer,
                indicatorColor = CivicGreenContainer,
                selectedTextColor = CivicGreenPrimary
            ),
            modifier = Modifier.testTag("tab_grievances")
        )

        // Testing & Demo Console
        NavigationBarItem(
            selected = selectedTab == ScreenTab.DEMO_CONSOLE || selectedTab == ScreenTab.OFFLINE_DRAFTS,
            onClick = { onTabSelected(ScreenTab.DEMO_CONSOLE) },
            icon = { Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = "Console") },
            label = { Text("Console", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OnCivicGreenContainer,
                indicatorColor = CivicGreenContainer,
                selectedTextColor = CivicGreenPrimary
            ),
            modifier = Modifier.testTag("tab_console")
        )
    }
}
