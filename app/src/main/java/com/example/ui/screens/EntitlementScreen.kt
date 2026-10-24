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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.EntitlementEntity
import com.example.data.local.MemberEntity
import com.example.data.model.VerificationState
import com.example.ui.theme.CivicAmber
import com.example.ui.theme.CivicAmberContainer
import com.example.ui.theme.CivicGreenPrimary
import com.example.ui.theme.OnCivicAmberContainer

@Composable
fun EntitlementScreen(
    members: List<MemberEntity>,
    entitlements: List<EntitlementEntity>,
    activeMember: MemberEntity?
) {
    // Member-level privacy filtering: if a member is active, show only their entitlement!
    val filteredMembers = if (activeMember != null) {
        listOf(activeMember)
    } else {
        members
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("entitlement_screen")
    ) {
        item {
            // Provenance & Legal Disclaimer Banner
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF4B5563),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Configurable Demonstration Fixtures: Individual allocations under PMGKAY/NFSA. Always verified at Point-of-Sale (e-PoS).",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF374151),
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Total Allocation Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (activeMember != null) {
                            "Individual Entitlement: ${activeMember.displayName}"
                        } else {
                            "Household Entitlement (5 Members)"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "Allocation Month: October 2026 • Subsidized (₹0/kg)",
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // List each member and their individual commodities
        items(filteredMembers) { member ->
            val memberEnts = entitlements.filter { it.memberId == member.id }
            val totalMemberKg = memberEnts.sumOf { it.remainingKg }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .testTag("member_entitlement_card_${member.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = member.displayName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF111827)
                            )
                            Text(
                                text = "${member.relationship} • ${member.age} yrs • ID: ${member.id}",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (member.hasMismatch) CivicAmberContainer else Color(0xFFD4EAD8)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (member.hasMismatch) Icons.Default.Warning else Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (member.hasMismatch) CivicAmber else CivicGreenPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (member.hasMismatch) "Mismatch Flag" else "Verified Active",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (member.hasMismatch) OnCivicAmberContainer else Color(0xFF092A13)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Commodity Rows
                    memberEnts.forEach { ent ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Grain, contentDescription = null, tint = CivicGreenPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(ent.commodityName, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${ent.remainingKg.toInt()} kg / ${ent.monthlyQuotaKg.toInt()} kg",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF1F2937)
                                )
                                Text(
                                    text = ent.provenance.label,
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFE5E7EB))
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Member Monthly Quota:", fontSize = 11.sp, color = Color.Gray)
                        Text("${totalMemberKg.toInt()} kg (Remaining)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CivicGreenPrimary)
                    }
                }
            }
        }
    }
}
