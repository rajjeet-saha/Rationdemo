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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.data.local.EntitlementEntity
import com.example.data.local.MemberEntity
import com.example.data.local.OfflineDraftEntity
import com.example.data.repository.InitialSeedData
import com.example.domain.RiskAssessment
import com.example.domain.RiskSeverity
import com.example.domain.SafetyCorrectionEngine
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
fun SafetySimulatorScreen(
    members: List<MemberEntity>,
    entitlements: List<EntitlementEntity>,
    onSaveCorrectionDraft: (OfflineDraftEntity) -> Unit
) {
    val scrollState = rememberScrollState()

    // Find mismatched member Devi Prasad
    val mismatchedMember = members.find { it.hasMismatch } ?: members.firstOrNull() ?: return
    val memberEnts = entitlements.filter { it.memberId == mismatchedMember.id }

    var proposedName by remember { mutableStateOf("Devi Prasad") }
    var proposedAge by remember { mutableStateOf("26") }
    var isDeleteAttempt by remember { mutableStateOf(false) }

    // Evaluated risk state
    var riskResult by remember {
        mutableStateOf<RiskAssessment?>(null)
    }

    // Checkbox checklist state
    val checkedMap = remember { mutableStateMapOf<String, Boolean>() }
    var draftSavedNotice by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("safety_simulator_screen")
    ) {
        // Hero Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CivicGreenContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CivicGreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Safety-Critical Correction Simulator",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnCivicGreenContainer
                            )
                        )
                        Text(
                            text = "Zero Entitlement Loss Guarantee • Pre-Submission Gate",
                            fontSize = 11.sp,
                            color = CivicGreenPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Before any record change is sent to the official PDS portal, this engine calculates if the modification risks severing food grain quotas.",
                    fontSize = 12.sp,
                    color = Color(0xFF13361E)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Existing Record Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Official PDS Record on File",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF1F2937)
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Name in State Database:", fontSize = 11.sp, color = Color.Gray)
                        Text(mismatchedMember.officialRecordName, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Recorded Age:", fontSize = 11.sp, color = Color.Gray)
                        Text("${mismatchedMember.age} years", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Active Quota:", fontSize = 11.sp, color = Color.Gray)
                        Text("${memberEnts.sumOf { it.monthlyQuotaKg }.toInt()} kg / month", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CivicGreenPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFEF3C7),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = CivicAmber, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Flagged Conflict: Identity document (Aadhaar) lists '${mismatchedMember.identityDocName}', age 26.",
                            fontSize = 11.sp,
                            color = OnCivicAmberContainer
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Simulator Interaction Section
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
            modifier = Modifier.fillMaxWidth().testTag("simulator_controls_card")
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Simulate Proposed Operation",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF1F2937)
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = proposedName,
                    onValueChange = { proposedName = it },
                    label = { Text("Proposed Name") },
                    modifier = Modifier.fillMaxWidth().testTag("proposed_name_input"),
                    enabled = !isDeleteAttempt
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = proposedAge,
                    onValueChange = { proposedAge = it },
                    label = { Text("Proposed Age") },
                    modifier = Modifier.fillMaxWidth().testTag("proposed_age_input"),
                    enabled = !isDeleteAttempt
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Toggle Unsafe Deletion attempt
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isDeleteAttempt,
                        onCheckedChange = { isDeleteAttempt = it },
                        modifier = Modifier.testTag("delete_member_checkbox")
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Attempt naive member deletion to re-add later (Dangerous!)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDeleteAttempt) CivicRedRisk else Color(0xFF374151)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Run Simulation Button
                Button(
                    onClick = {
                        val ageInt = proposedAge.toIntOrNull() ?: mismatchedMember.age
                        val result = SafetyCorrectionEngine.evaluateCorrectionProposal(
                            existingMember = mismatchedMember,
                            proposedName = proposedName,
                            proposedAge = ageInt,
                            isDeletionAttempt = isDeleteAttempt,
                            memberEntitlements = memberEnts
                        )
                        riskResult = result
                        draftSavedNotice = null
                    },
                    modifier = Modifier.fillMaxWidth().testTag("run_simulation_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CivicGreenPrimary)
                ) {
                    Icon(Icons.Default.CompareArrows, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Evaluate Correction Risk", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Risk Result Display
        riskResult?.let { result ->
            Spacer(modifier = Modifier.height(16.dp))

            val bannerColor = if (result.isBlocked) CivicRedContainer else CivicGreenContainer
            val textColor = if (result.isBlocked) OnCivicRedContainer else OnCivicGreenContainer
            val iconTint = if (result.isBlocked) CivicRedRisk else CivicGreenPrimary

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = bannerColor),
                modifier = Modifier.fillMaxWidth().testTag("simulation_result_card")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (result.isBlocked) Icons.Default.Block else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = result.headline,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = result.explanation,
                        fontSize = 12.sp,
                        color = if (result.isBlocked) Color(0xFF991B1B) else Color(0xFF13361E)
                    )

                    if (result.affectedEntitlementKg > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Entitlement Threat: ${result.affectedEntitlementKg.toInt()} kg / month (${result.affectedCommodities.joinToString(", ")})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CivicRedRisk,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Safe Recommendation: ${result.safeAlternativeRecommendation}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1F2937)
                    )
                }
            }

            // Evidence Checklist for Safe Rectification
            if (result.evidenceChecklist.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().testTag("evidence_checklist_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = CivicGreenPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Required Evidence Dossier (Prevents Quota Cancellation)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF1F2937)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        result.evidenceChecklist.forEach { item ->
                            val isChecked = checkedMap[item.title] ?: false
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checkedMap[item.title] = it }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Column {
                                    Text(
                                        text = item.title,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = if (item.isMandatory) Color.Black else Color.Gray
                                    )
                                    Text(
                                        text = item.purpose,
                                        fontSize = 10.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Button to create Safe Offline Draft
                        Button(
                            onClick = {
                                val draft = OfflineDraftEntity(
                                    id = "DRAFT-${System.currentTimeMillis()}",
                                    memberId = mismatchedMember.id,
                                    title = "Form 4A Safe Clerical Rectification: $proposedName",
                                    draftType = "Safe Mismatch Rectification",
                                    description = "Dossier prepared with Form 4A and identity proofs to update '${mismatchedMember.officialRecordName}' to '$proposedName' preserving 5kg monthly entitlement.",
                                    payloadJson = """{"oldName":"${mismatchedMember.officialRecordName}","newName":"$proposedName","proposedAge":$proposedAge}"""
                                )
                                onSaveCorrectionDraft(draft)
                                draftSavedNotice = "Safe correction dossier saved as local offline draft! Ready for review in Offline Drafts tab."
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CivicGreenPrimary),
                            modifier = Modifier.fillMaxWidth().testTag("save_safe_draft_button")
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Create Safe Rectification Draft")
                        }

                        draftSavedNotice?.let { msg ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFDCFCE7),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = msg,
                                    color = Color(0xFF166534),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
