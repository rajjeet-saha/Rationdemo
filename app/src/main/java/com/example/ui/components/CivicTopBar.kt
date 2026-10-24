package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MemberEntity
import com.example.ui.state.AppLanguage
import com.example.ui.state.LanguageManager
import com.example.ui.theme.CivicGreenPrimary

@Composable
fun CivicTopBar(
    currentLanguage: AppLanguage,
    activeMember: MemberEntity?,
    allMembers: List<MemberEntity>,
    onOpenDrawer: () -> Unit,
    onSelectMember: (MemberEntity?) -> Unit,
    onOpenLanguageDialog: () -> Unit,
    onEndSessionClick: () -> Unit
) {
    var memberMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        color = CivicGreenPrimary,
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Drawer Hamburger & Brand Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.testTag("app_branding_header")
                ) {
                    IconButton(
                        onClick = onOpenDrawer,
                        modifier = Modifier.testTag("drawer_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Navigation Menu",
                            tint = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2E6A41)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "RationSaathi Emblem",
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = LanguageManager.getString("app_title", currentLanguage),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Text(
                            text = LanguageManager.getString("tagline", currentLanguage),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFD4EAD8),
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                // Action Controls: Language & End Session
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Language Switcher
                    IconButton(
                        onClick = onOpenLanguageDialog,
                        modifier = Modifier.testTag("language_switch_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Change Language",
                            tint = Color.White
                        )
                    }

                    // End Session / Privacy Wipe
                    IconButton(
                        onClick = onEndSessionClick,
                        modifier = Modifier.testTag("end_session_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.LockReset,
                            contentDescription = "End Session & Wipe Data",
                            tint = Color(0xFFFECACA)
                        )
                    }
                }
            }

            // Member Switcher Strip (Shared Phone Privacy Isolation)
            Spacer(modifier = Modifier.padding(top = 6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF13361E),
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { memberMenuExpanded = true }
                            .testTag("active_member_selector")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Active Profile",
                                tint = Color(0xFF86D89B),
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (activeMember != null) {
                                    "Viewing: ${activeMember.displayName.split(" ")[0]} (${activeMember.relationship.split(" ")[0]})"
                                } else {
                                    "Viewing: Full Household (Shared)"
                                },
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = memberMenuExpanded,
                        onDismissRequest = { memberMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Full Household Overview (Shared)") },
                            onClick = {
                                onSelectMember(null)
                                memberMenuExpanded = false
                            }
                        )
                        allMembers.forEach { member ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(member.displayName, fontWeight = FontWeight.SemiBold)
                                        Text(
                                            "${member.relationship} • ${member.age} yrs",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                },
                                onClick = {
                                    onSelectMember(member)
                                    memberMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                // Indic Language Code Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF2E6A41)
                ) {
                    Text(
                        text = currentLanguage.nativeName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFFBBF24),
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}
