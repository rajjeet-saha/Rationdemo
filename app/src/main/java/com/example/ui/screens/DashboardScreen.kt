package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.domain.TodayActionSummary
import com.example.ui.state.AppLanguage
import com.example.ui.state.LanguageManager
import com.example.ui.state.ScreenTab
import com.example.ui.theme.CivicAmber
import com.example.ui.theme.CivicAmberContainer
import com.example.ui.theme.CivicGreenContainer
import com.example.ui.theme.CivicGreenPrimary
import com.example.ui.theme.CivicRedContainer
import com.example.ui.theme.CivicRedRisk
import com.example.ui.theme.OnCivicAmberContainer
import com.example.ui.theme.OnCivicGreenContainer
import com.example.ui.theme.OnCivicRedContainer

@Composable
fun DashboardScreen(
    currentLanguage: AppLanguage,
    household: HouseholdEntity?,
    members: List<MemberEntity>,
    activeMember: MemberEntity?,
    todaySummary: TodayActionSummary?,
    onNavigate: (ScreenTab) -> Unit,
    onSelectMember: (MemberEntity?) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("dashboard_screen")
    ) {
        // Core Civic Mission Header: What can I safely do today?
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CivicGreenContainer),
            modifier = Modifier.fillMaxWidth().testTag("today_summary_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CivicGreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = LanguageManager.getString("what_can_i_do_today", currentLanguage),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnCivicGreenContainer
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = todaySummary?.headline ?: "Evaluating today's safe portability actions...",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF13361E)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Rice Status Row
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Rice Status",
                        tint = CivicGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Rice (15 kg Quota) — Ready for Collection",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF13361E)
                        )
                        Text(
                            text = todaySummary?.riceAdvice ?: "Verified in stock at destination shop FPS-204.",
                            fontSize = 12.sp,
                            color = Color(0xFF2E6A41)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Wheat Status Row (Amber Warning for Stale/Depleted stock)
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Wheat Stock Alert",
                        tint = CivicAmber,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Wheat (10 kg Quota) — Destination Out of Stock",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = OnCivicAmberContainer
                        )
                        Text(
                            text = todaySummary?.wheatAdvice ?: "Depleted at FPS-204. Visit alternative FPS-118 nearby (1.4 km).",
                            fontSize = 12.sp,
                            color = Color(0xFF78350F)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Responsibility
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = CivicGreenPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = todaySummary?.actingRepresentativeAdvice ?: "Sunita Devi (Adult Spouse) can present Aadhaar e-KYC.",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF13361E)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interstate Migrant Household Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Ration Card: ${household?.syntheticCardNo ?: "NFSA-1092-8821-4401"}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = "Category: ${household?.category ?: "PHH"}",
                            fontSize = 12.sp,
                            color = Color(0xFF4B5563)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CivicAmberContainer
                    ) {
                        Text(
                            text = LanguageManager.getString("head_absent_badge", currentLanguage),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = OnCivicAmberContainer,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text("Home State", fontSize = 10.sp, color = Color.Gray)
                            Text("${household?.homeDistrict ?: "Saran"}, ${household?.homeState ?: "Bihar"}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Store, contentDescription = null, tint = CivicGreenPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text("Current Destination", fontSize = 10.sp, color = Color.Gray)
                            Text("${household?.destinationDistrict ?: "Pune"}, ${household?.destinationState ?: "Maharashtra"}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Safety Warnings Section: Mismatch Flag & Grievance Alert
        Text(
            text = "Active Alerts & Interventions",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 1. Mismatch Warning Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CivicRedContainer),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigate(ScreenTab.SAFETY_SIMULATOR) }
                .testTag("mismatch_alert_card")
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ErrorOutline,
                    contentDescription = "Mismatch Alert",
                    tint = CivicRedRisk,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Member Record Mismatch: Devi Prasad",
                        fontWeight = FontWeight.Bold,
                        color = OnCivicRedContainer,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "PDS record name 'Dev Prasad' (age 24) conflicts with Aadhaar 'Devi Prasad' (age 26). Direct deletion triggers 5 kg quota loss!",
                        fontSize = 11.sp,
                        color = Color(0xFF991B1B)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Button(
                    onClick = { onNavigate(ScreenTab.SAFETY_SIMULATOR) },
                    colors = ButtonDefaults.buttonColors(containerColor = CivicRedRisk),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("simulate_safety_button")
                ) {
                    Text("Simulate", fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Grievance 21-Day Exceeded Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CivicAmberContainer),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigate(ScreenTab.GRIEVANCES) }
                .testTag("grievance_alert_card")
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Grievance Alert",
                    tint = CivicAmber,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Grievance #MH-PDS-2024-88392: 21 Days Unanswered",
                        fontWeight = FontWeight.Bold,
                        color = OnCivicAmberContainer,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Statutory citizen charter resolution deadline (15 days) exceeded by 6 days. Ready for formal DGRO escalation.",
                        fontSize = 11.sp,
                        color = Color(0xFF78350F)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Button(
                    onClick = { onNavigate(ScreenTab.GRIEVANCES) },
                    colors = ButtonDefaults.buttonColors(containerColor = CivicAmber),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("escalate_grievance_button")
                ) {
                    Text("Escalate", fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Navigation Grid
        Text(
            text = "Civic Distribution Services",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { onNavigate(ScreenTab.ENTITLEMENTS) },
                modifier = Modifier.weight(1f).testTag("quick_nav_entitlements")
            ) {
                Icon(Icons.Default.Grain, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Entitlements", fontSize = 11.sp)
            }

            OutlinedButton(
                onClick = { onNavigate(ScreenTab.PORTABILITY) },
                modifier = Modifier.weight(1f).testTag("quick_nav_portability")
            ) {
                Icon(Icons.Default.Store, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Shop Stocks", fontSize = 11.sp)
            }
        }
    }
}
