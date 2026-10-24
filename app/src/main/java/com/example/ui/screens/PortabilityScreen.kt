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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Refresh
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
import com.example.data.local.ShopStockEntity
import com.example.data.model.StockState
import com.example.ui.theme.CivicAmber
import com.example.ui.theme.CivicAmberContainer
import com.example.ui.theme.CivicGreenPrimary
import com.example.ui.theme.CivicRedContainer
import com.example.ui.theme.CivicRedRisk
import com.example.ui.theme.OnCivicAmberContainer

@Composable
fun PortabilityScreen(
    shopStocks: List<ShopStockEntity>,
    onToggleStock: (String, String) -> Unit
) {
    val destinationStocks = shopStocks.filter { it.isDestinationShop }
    val alternativeStocks = shopStocks.filter { !it.isDestinationShop }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("portability_screen")
    ) {
        item {
            // One Nation One Ration Card (ONORC) Banner
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD4EAD8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.NearMe, contentDescription = null, tint = CivicGreenPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Interstate Portability Active (ONORC)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF092A13)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Migrant households can lift their NFSA quota at any Fair Price Shop across India using biometric or OTP authentication.",
                        fontSize = 12.sp,
                        color = Color(0xFF13361E)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Stock Uncertainty Warning
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
                        text = "Stock Uncertainty Notice: PDS stock reports may change rapidly. Destination shop Wheat status is 18 hours stale. Do not travel without verifying local availability.",
                        fontSize = 11.sp,
                        color = OnCivicAmberContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Primary Destination Shop (Assigned)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF1F2937)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        items(destinationStocks) { stock ->
            ShopStockCard(stock = stock, onToggle = { onToggleStock(stock.id, stock.commodityName) })
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DirectionsWalk, contentDescription = null, tint = CivicGreenPrimary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Nearby Authorised Alternative Shops (Portability Allowed)",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF1F2937)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        items(alternativeStocks) { stock ->
            ShopStockCard(stock = stock, onToggle = { onToggleStock(stock.id, stock.commodityName) })
        }
    }
}

@Composable
fun ShopStockCard(
    stock: ShopStockEntity,
    onToggle: () -> Unit
) {
    val isOutOfStock = stock.stockState == StockState.OUT_OF_STOCK_REPORTED || stock.stockState == StockState.OUT_OF_STOCK_VERIFIED

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .testTag("shop_stock_${stock.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stock.shopName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF111827)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${stock.locationAddress} • ${stock.distanceKm} km",
                            fontSize = 11.sp,
                            color = Color(0xFF4B5563)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isOutOfStock) CivicRedContainer else Color(0xFFD4EAD8)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isOutOfStock) Icons.Default.Warning else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (isOutOfStock) CivicRedRisk else CivicGreenPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stock.stockState.displayName,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isOutOfStock) CivicRedRisk else CivicGreenPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Commodity: ${stock.commodityName}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Reported: ${stock.lastUpdatedHoursAgo} hrs ago • Feed: ${stock.provenance.label}",
                        fontSize = 10.sp,
                        color = if (stock.lastUpdatedHoursAgo > 12) CivicAmber else Color.Gray
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (isOutOfStock) "0 kg" else "${stock.stockKg.toInt()} kg",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isOutOfStock) CivicRedRisk else CivicGreenPrimary
                    )
                    // Toggle status for simulation testing
                    OutlinedButton(
                        onClick = onToggle,
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.testTag("toggle_stock_button_${stock.id}")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Simulate Restock", fontSize = 9.sp)
                    }
                }
            }
        }
    }
}
