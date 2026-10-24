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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.OfflineDraftEntity
import com.example.data.model.DraftStatus
import com.example.ui.theme.CivicAmber
import com.example.ui.theme.CivicAmberContainer
import com.example.ui.theme.CivicGreenContainer
import com.example.ui.theme.CivicGreenPrimary
import com.example.ui.theme.DemoModeIndigo
import com.example.ui.theme.OnCivicAmberContainer
import com.example.ui.theme.OnCivicGreenContainer

@Composable
fun OfflineDraftsScreen(
    drafts: List<OfflineDraftEntity>,
    isOnline: Boolean,
    onSyncDraft: (String) -> Unit,
    onSubmitDraft: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("offline_drafts_screen")
    ) {
        item {
            // Header Banner
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CivicGreenContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Drafts, contentDescription = null, tint = CivicGreenPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Offline & Assisted Draft Manager",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = OnCivicGreenContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Forms prepared offline at facilitator desks remain local drafts. Synchronisation alone does not constitute an official government submission.",
                        fontSize = 12.sp,
                        color = Color(0xFF13361E)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Sync Truth Warning
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CivicAmberContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = CivicAmber, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Submission Integrity Rule: An official acknowledgment receipt (ACK Token) is only generated once the authorized portal validates and accepts the dossier.",
                        fontSize = 11.sp,
                        color = OnCivicAmberContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Prepared Drafts (${drafts.size})",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF1F2937)
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isOnline) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isOnline) Icons.Default.CloudDone else Icons.Default.CloudOff,
                            contentDescription = null,
                            tint = if (isOnline) Color(0xFF166534) else Color(0xFF991B1B),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isOnline) "Simulated: Online" else "Simulated: Offline",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isOnline) Color(0xFF166534) else Color(0xFF991B1B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        items(drafts) { draft ->
            DraftItemCard(
                draft = draft,
                isOnline = isOnline,
                onSync = { onSyncDraft(draft.id) },
                onSubmit = { onSubmitDraft(draft.id) }
            )
        }
    }
}

@Composable
fun DraftItemCard(
    draft: OfflineDraftEntity,
    isOnline: Boolean,
    onSync: () -> Unit,
    onSubmit: () -> Unit
) {
    val statusColor = when (draft.status) {
        DraftStatus.LOCAL_DRAFT -> CivicAmber
        DraftStatus.SYNCED_UNSUBMITTED -> DemoModeIndigo
        DraftStatus.SUBMITTED_SIMULATED -> CivicGreenPrimary
    }

    val statusBg = when (draft.status) {
        DraftStatus.LOCAL_DRAFT -> CivicAmberContainer
        DraftStatus.SYNCED_UNSUBMITTED -> Color(0xFFE0E7FF)
        DraftStatus.SUBMITTED_SIMULATED -> Color(0xFFD4EAD8)
    }

    val statusLabel = when (draft.status) {
        DraftStatus.LOCAL_DRAFT -> "DRAFT — NOT SUBMITTED"
        DraftStatus.SYNCED_UNSUBMITTED -> "SYNCED TO CLOUD — UNCONFIRMED"
        DraftStatus.SUBMITTED_SIMULATED -> "OFFICIALLY SUBMITTED (SIMULATED)"
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("draft_card_${draft.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = draft.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF111827)
                    )
                    Text(
                        text = "Type: ${draft.draftType}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusBg
                ) {
                    Text(
                        text = statusLabel,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = statusColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = draft.description,
                fontSize = 12.sp,
                color = Color(0xFF374151)
            )

            if (draft.officialAckToken != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFDCFCE7),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = CivicGreenPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Official Ack Token: ${draft.officialAckToken}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF166534)
                            )
                            Text(
                                text = "Simulated submission receipt stored in local device ledger.",
                                fontSize = 10.sp,
                                color = Color(0xFF166534)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (draft.status == DraftStatus.LOCAL_DRAFT) {
                    OutlinedButton(
                        onClick = onSync,
                        modifier = Modifier.weight(1f).testTag("sync_draft_button_${draft.id}"),
                        enabled = isOnline
                    ) {
                        Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isOnline) "Sync to Staging" else "Offline", fontSize = 11.sp)
                    }
                }

                if (draft.status != DraftStatus.SUBMITTED_SIMULATED) {
                    Button(
                        onClick = onSubmit,
                        colors = ButtonDefaults.buttonColors(containerColor = CivicGreenPrimary),
                        modifier = Modifier.weight(1f).testTag("submit_draft_button_${draft.id}"),
                        enabled = isOnline
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Simulate Submission", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
