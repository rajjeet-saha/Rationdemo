package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellWifi
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SignalCellularConnectedNoInternet0Bar
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.state.AppLanguage
import com.example.ui.state.LanguageManager
import com.example.ui.theme.DemoModeIndigo
import com.example.ui.theme.DemoModeIndigoContainer

@Composable
fun DemoModeBanner(
    currentLanguage: AppLanguage,
    isOnline: Boolean,
    onToggleNetwork: () -> Unit
) {
    Surface(
        color = DemoModeIndigoContainer,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("demo_status_bar_indicator")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Demo Mode",
                    tint = DemoModeIndigo,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "DEMO SIMULATION • NOT LIVE GOVERNMENT PORTAL",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DemoModeIndigo,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Text(
                        text = "Synthetic Corridor: Saran, Bihar ➔ Hadapsar, Pune • NFSA-1092-8821",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF3730A3),
                            fontSize = 10.sp
                        ),
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Simulated Network State Toggle
            OutlinedButton(
                onClick = onToggleNetwork,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isOnline) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                    contentColor = if (isOnline) Color(0xFF166534) else Color(0xFF991B1B)
                ),
                modifier = Modifier.testTag("network_simulation_toggle"),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Icon(
                    imageVector = if (isOnline) Icons.Default.CellWifi else Icons.Default.SignalCellularConnectedNoInternet0Bar,
                    contentDescription = if (isOnline) "Simulated Online" else "Simulated Offline",
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isOnline) "Net: Online" else "Net: Offline",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
