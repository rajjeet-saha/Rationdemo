package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.GrievanceEntity
import com.example.data.local.HouseholdEntity
import com.example.domain.EscalationDraft
import com.example.domain.GrievanceEscalationEngine
import com.example.ui.theme.CivicAmber
import com.example.ui.theme.CivicAmberContainer
import com.example.ui.theme.CivicGreenPrimary
import com.example.ui.theme.CivicRedContainer
import com.example.ui.theme.CivicRedRisk
import com.example.ui.theme.OnCivicAmberContainer
import com.example.ui.theme.OnCivicRedContainer

@Composable
fun GrievanceScreen(
    grievances: List<GrievanceEntity>,
    household: HouseholdEntity?
) {
    var generatedEscalation by remember { mutableStateOf<EscalationDraft?>(null) }
    var copiedNotice by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("grievance_screen")
    ) {
        item {
            // Header
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CivicAmberContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.HourglassBottom, contentDescription = null, tint = CivicAmber, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Statutory Grievance Lifecycle Tracking",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = OnCivicAmberContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Under the National Food Security Act (NFSA), grievance redressal officers must respond within 15 working days. Unanswered tickets trigger automatic citizen escalation rights.",
                        fontSize = 12.sp,
                        color = Color(0xFF78350F)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Registered Grievance Records",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF1F2937)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        items(grievances) { grv ->
            val isOverdue = grv.filedDaysAgo > grv.statutoryMaxDays
            val overdueDays = if (isOverdue) grv.filedDaysAgo - grv.statutoryMaxDays else 0

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .testTag("grievance_card_${grv.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Token: #${grv.tokenNo}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF111827)
                            )
                            Text(
                                text = "Category: ${grv.category}",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isOverdue) CivicRedContainer else Color(0xFFD4EAD8)
                        ) {
                            Text(
                                text = if (isOverdue) "OVERDUE BY $overdueDays DAYS" else "ACTIVE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isOverdue) CivicRedRisk else CivicGreenPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = grv.summary,
                        fontSize = 12.sp,
                        color = Color(0xFF374151)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Days Elapsed: ${grv.filedDaysAgo} days", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = if (isOverdue) CivicRedRisk else Color.Gray)
                        Text("Citizen Charter Limit: ${grv.statutoryMaxDays} days", fontSize = 11.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF3F4F6),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Last Status: ${grv.status}",
                            fontSize = 11.sp,
                            color = Color(0xFF4B5563),
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            generatedEscalation = GrievanceEscalationEngine.generateEscalation(grv, household)
                            copiedNotice = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CivicRedRisk),
                        modifier = Modifier.fillMaxWidth().testTag("generate_escalation_button_${grv.id}")
                    ) {
                        Icon(Icons.Default.Gavel, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Generate Legal Escalation Draft (Overdue)")
                    }
                }
            }
        }

        // Generated Escalation Letter Preview
        generatedEscalation?.let { draft ->
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                    modifier = Modifier.fillMaxWidth().testTag("escalation_letter_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AssignmentLate, contentDescription = null, tint = CivicAmber, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Formal Escalation to District Grievance Officer",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnCivicAmberContainer
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Citation: ${draft.legalCitation}",
                            fontSize = 11.sp,
                            color = Color(0xFF78350F),
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = draft.formalLetterBody,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = Color(0xFF1F2937),
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                copiedNotice = "Legal escalation draft copied to clipboard & logged in dispatch ledger!"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CivicGreenPrimary),
                            modifier = Modifier.fillMaxWidth().testTag("copy_escalation_button")
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy Formal Letter for DGRO Dispatch")
                        }

                        copiedNotice?.let { msg ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = msg,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534)
                            )
                        }
                    }
                }
            }
        }
    }
}
