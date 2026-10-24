package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.HouseholdEntity
import com.example.data.local.MemberEntity
import com.example.ui.state.AppLanguage
import com.example.ui.state.LanguageManager
import com.example.ui.state.ScreenTab
import com.example.ui.theme.CivicGreenContainer
import com.example.ui.theme.CivicGreenPrimary
import com.example.ui.theme.CivicRedRisk
import com.example.ui.theme.OnCivicGreenContainer

@Composable
fun CivicDrawer(
    selectedTab: ScreenTab,
    currentLanguage: AppLanguage,
    household: HouseholdEntity?,
    activeMember: MemberEntity?,
    onTabSelected: (ScreenTab) -> Unit,
    onEndSessionClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    ModalDrawerSheet(
        modifier = Modifier
            .width(320.dp)
            .fillMaxHeight()
            .testTag("civic_navigation_drawer")
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(scrollState)
        ) {
            // Drawer Header
            Surface(
                color = CivicGreenPrimary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2E6A41)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = LanguageManager.getString("app_title", currentLanguage),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = LanguageManager.getString("tagline", currentLanguage),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFFD4EAD8),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF13361E),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Card: ${household?.syntheticCardNo ?: "NFSA-1092-8821"}",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Corridor: ${household?.homeState ?: "Bihar"} ➔ ${household?.destinationState ?: "Maharashtra"}",
                                color = Color(0xFF86D89B),
                                fontSize = 11.sp
                            )
                            Text(
                                text = if (activeMember != null) {
                                    "Profile: ${activeMember.displayName} (${activeMember.relationship})"
                                } else {
                                    "Profile: Shared Household View"
                                },
                                color = Color(0xFFD1D5DB),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Navigation Items
            NavigationDrawerItem(
                label = { Text("Today's Overview & Actions", fontWeight = FontWeight.Medium) },
                icon = { Icon(Icons.Default.Today, contentDescription = null) },
                selected = selectedTab == ScreenTab.OVERVIEW,
                onClick = { onTabSelected(ScreenTab.OVERVIEW) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = CivicGreenContainer,
                    selectedIconColor = OnCivicGreenContainer,
                    selectedTextColor = CivicGreenPrimary
                ),
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding).testTag("drawer_item_overview")
            )

            NavigationDrawerItem(
                label = { Text("Member Entitlements", fontWeight = FontWeight.Medium) },
                icon = { Icon(Icons.Default.Grain, contentDescription = null) },
                selected = selectedTab == ScreenTab.ENTITLEMENTS,
                onClick = { onTabSelected(ScreenTab.ENTITLEMENTS) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = CivicGreenContainer,
                    selectedIconColor = OnCivicGreenContainer,
                    selectedTextColor = CivicGreenPrimary
                ),
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding).testTag("drawer_item_entitlements")
            )

            NavigationDrawerItem(
                label = { Text("Portability & Shop Stocks", fontWeight = FontWeight.Medium) },
                icon = { Icon(Icons.Default.NearMe, contentDescription = null) },
                selected = selectedTab == ScreenTab.PORTABILITY,
                onClick = { onTabSelected(ScreenTab.PORTABILITY) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = CivicGreenContainer,
                    selectedIconColor = OnCivicGreenContainer,
                    selectedTextColor = CivicGreenPrimary
                ),
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding).testTag("drawer_item_portability")
            )

            NavigationDrawerItem(
                label = { Text("Safety Correction Simulator", fontWeight = FontWeight.Medium) },
                icon = { Icon(Icons.Default.BuildCircle, contentDescription = null) },
                selected = selectedTab == ScreenTab.SAFETY_SIMULATOR,
                onClick = { onTabSelected(ScreenTab.SAFETY_SIMULATOR) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = CivicGreenContainer,
                    selectedIconColor = OnCivicGreenContainer,
                    selectedTextColor = CivicGreenPrimary
                ),
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding).testTag("drawer_item_simulator")
            )

            NavigationDrawerItem(
                label = { Text("Offline & Assisted Drafts", fontWeight = FontWeight.Medium) },
                icon = { Icon(Icons.Default.Drafts, contentDescription = null) },
                selected = selectedTab == ScreenTab.OFFLINE_DRAFTS,
                onClick = { onTabSelected(ScreenTab.OFFLINE_DRAFTS) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = CivicGreenContainer,
                    selectedIconColor = OnCivicGreenContainer,
                    selectedTextColor = CivicGreenPrimary
                ),
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding).testTag("drawer_item_drafts")
            )

            NavigationDrawerItem(
                label = { Text("Grievance Redressal (21d)", fontWeight = FontWeight.Medium) },
                icon = { Icon(Icons.Default.Gavel, contentDescription = null) },
                selected = selectedTab == ScreenTab.GRIEVANCES,
                onClick = { onTabSelected(ScreenTab.GRIEVANCES) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = CivicGreenContainer,
                    selectedIconColor = OnCivicGreenContainer,
                    selectedTextColor = CivicGreenPrimary
                ),
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding).testTag("drawer_item_grievances")
            )

            NavigationDrawerItem(
                label = { Text("Public Service Aggregates", fontWeight = FontWeight.Medium) },
                icon = { Icon(Icons.Default.Analytics, contentDescription = null) },
                selected = selectedTab == ScreenTab.PUBLIC_METRICS,
                onClick = { onTabSelected(ScreenTab.PUBLIC_METRICS) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = CivicGreenContainer,
                    selectedIconColor = OnCivicGreenContainer,
                    selectedTextColor = CivicGreenPrimary
                ),
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding).testTag("drawer_item_public")
            )

            NavigationDrawerItem(
                label = { Text("Demonstration & Test Console", fontWeight = FontWeight.Medium) },
                icon = { Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null) },
                selected = selectedTab == ScreenTab.DEMO_CONSOLE,
                onClick = { onTabSelected(ScreenTab.DEMO_CONSOLE) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = CivicGreenContainer,
                    selectedIconColor = OnCivicGreenContainer,
                    selectedTextColor = CivicGreenPrimary
                ),
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding).testTag("drawer_item_console")
            )

            Spacer(modifier = Modifier.weight(1f))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Footer Session Wipeout
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedButton(
                    onClick = onEndSessionClick,
                    modifier = Modifier.fillMaxWidth().testTag("drawer_end_session_button")
                ) {
                    Icon(Icons.Default.LockReset, contentDescription = null, tint = CivicRedRisk, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("End Session & Clear Data", color = CivicRedRisk, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Zero-leakage shared-device protection active.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
        }
    }
}
