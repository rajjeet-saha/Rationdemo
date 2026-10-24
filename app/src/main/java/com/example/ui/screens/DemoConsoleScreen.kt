package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SupportAgent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CivicGreenContainer
import com.example.ui.theme.CivicGreenPrimary
import com.example.ui.theme.CivicRedRisk
import com.example.ui.theme.DemoModeIndigo
import com.example.ui.theme.DemoModeIndigoContainer
import com.example.ui.theme.OnCivicGreenContainer

@Composable
fun DemoConsoleScreen(
    onResetScenario: () -> Unit,
    onRequestWipeSession: () -> Unit
) {
    val scrollState = rememberScrollState()
    var showAuditLog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("demo_console_screen")
    ) {
        // Console Header
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = DemoModeIndigoContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Assignment, contentDescription = null, tint = DemoModeIndigo, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "RationSaathi Demonstration & Test Console",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DemoModeIndigo
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Control panel for verifying test fixtures, simulated submission adapters, privacy isolation, and repeatable scenario resets.",
                    fontSize = 12.sp,
                    color = Color(0xFF312E81)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scenario Reset Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "End-to-End Demonstration Scenario",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF1F2937)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Resetting repopulates all 6 required evaluation facts:\n" +
                            "• 5 household members (Bihar -> Maharashtra)\n" +
                            "• Absent head of household (Ram Ashray Prasad)\n" +
                            "• Name/Age mismatch on Devi Prasad (Dev Prasad)\n" +
                            "• Stale out-of-stock Wheat at destination shop\n" +
                            "• 21-day unanswered grievance (Token #MH-PDS-2024-88392)\n" +
                            "• 1 unsubmitted offline draft",
                    fontSize = 11.sp,
                    color = Color(0xFF4B5563)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onResetScenario,
                    colors = ButtonDefaults.buttonColors(containerColor = CivicGreenPrimary),
                    modifier = Modifier.fillMaxWidth().testTag("reset_demo_scenario_button")
                ) {
                    Icon(Icons.Default.RestartAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reset Scenario to Initial Seed State")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Shared Phone Session Wipe
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = CivicRedRisk, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Shared-Device Session Wipe & Handover",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1F2937)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Simulates handoff between unrelated households on a shared mobile phone or facilitator tablet. Wipes active member session memory.",
                    fontSize = 11.sp,
                    color = Color(0xFF4B5563)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onRequestWipeSession,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CivicRedRisk),
                    modifier = Modifier.fillMaxWidth().testTag("console_wipe_session_button")
                ) {
                    Icon(Icons.Default.LockReset, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Trigger Session Wipeout")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy-Preserving Public Aggregate Metrics
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Analytics, contentDescription = null, tint = CivicGreenPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Public Service Redressal Aggregates",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1F2937)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Anonymised public view — zero household PII exposed.",
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricPill(title = "ONORC Uptake", value = "94.2%")
                    MetricPill(title = "Avg Redress", value = "17.4 Days")
                    MetricPill(title = "Portability FPS", value = "5,412")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricPill(title = "Biometric Failures", value = "3.8%")
                    MetricPill(title = "Safe Rectifications", value = "1,890")
                    MetricPill(title = "Severed Prevention", value = "100%")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Facilitator Assisted Service Notice
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CivicGreenContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SupportAgent, contentDescription = null, tint = CivicGreenPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Assisted-Service Facilitator Protocol",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = OnCivicGreenContainer
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "When helping a migrant worker at a Seva Kendra, always verify physical identity proofs prior to creating a Form 4A draft. Never perform destructive record deletions.",
                    fontSize = 11.sp,
                    color = Color(0xFF13361E)
                )
            }
        }
    }
}

@Composable
fun MetricPill(title: String, value: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF3F4F6)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CivicGreenPrimary)
            Text(title, fontSize = 10.sp, color = Color.Gray)
        }
    }
}
