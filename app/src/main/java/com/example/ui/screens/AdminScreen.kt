package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import com.example.data.model.AppointmentStatus
import com.example.ui.BeautyViewModel
import com.example.ui.components.DemoDataNotice
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TerracottaPrimary

@Composable
fun AdminScreen(
    viewModel: BeautyViewModel,
    modifier: Modifier = Modifier
) {
    val providers by viewModel.allProviders.collectAsState()
    val appointments by viewModel.allAppointments.collectAsState()
    val localities by viewModel.localities.collectAsState()
    val settings by viewModel.platformSettings.collectAsState()

    val completedBookings = appointments.filter { it.status == AppointmentStatus.COMPLETED }
    val totalVolumeFcfa = completedBookings.sumOf { it.totalFcfa }
    val platformRevenueFcfa = (totalVolumeFcfa * (settings.commissionPercent / 100.0)).toInt()

    var commissionRate by remember(settings.commissionPercent) { mutableDoubleStateOf(settings.commissionPercent) }
    var orangeMoney by remember(settings.orangeMoneyActive) { mutableStateOf(settings.orangeMoneyActive) }
    var moovMoney by remember(settings.moovMoneyActive) { mutableStateOf(settings.moovMoneyActive) }
    var cashPayment by remember(settings.cashPaymentActive) { mutableStateOf(settings.cashPaymentActive) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("admin_screen"),
        contentPadding = PaddingValues(16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            DemoDataNotice()
        }

        // Header
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AdminPanelSettings,
                    contentDescription = null,
                    tint = TerracottaPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Supervision & Administration",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Text(
                        text = "Plateforme Nationale BEAUTY BF • Burkina Faso",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Global KPIs
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KpiCard(
                        title = "Prestataires",
                        value = "${providers.size}",
                        icon = Icons.Default.Storefront,
                        color = TerracottaPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "Rendez-vous",
                        value = "${appointments.size}",
                        icon = Icons.Default.Assessment,
                        color = Color(0xFF1976D2),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KpiCard(
                        title = "Volume d'affaires",
                        value = viewModel.formatFcfa(totalVolumeFcfa),
                        icon = Icons.Default.Money,
                        color = SuccessGreen,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "Commissions (${settings.commissionPercent.toInt()}%)",
                        value = viewModel.formatFcfa(platformRevenueFcfa),
                        icon = Icons.Default.VerifiedUser,
                        color = Color(0xFFE65100),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Section: Validation & Badges Prestataires
        item {
            Text(
                text = "Modération & Badges de vérification (${providers.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(providers) { prov ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = prov.salonName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            VerifiedBadge(isVerified = prov.isVerified)
                        }
                        Text(
                            text = "${prov.city} (${prov.sector}) • ${prov.phone}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { viewModel.toggleProviderVerification(prov.id, prov.isVerified) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (prov.isVerified) ErrorRed else SuccessGreen
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("toggle_verify_${prov.id}")
                    ) {
                        Text(
                            text = if (prov.isVerified) "Dévérifier" else "Vérifier",
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Section: Configuration financière et moyens de paiement
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Paramètres de la Plateforme",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Orange Money Burkina Faso", fontSize = 13.sp)
                        Switch(
                            checked = orangeMoney,
                            onCheckedChange = {
                                orangeMoney = it
                                viewModel.updatePlatformSettings(commissionRate, it, moovMoney, cashPayment)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = TerracottaPrimary)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Moov Money Burkina Faso", fontSize = 13.sp)
                        Switch(
                            checked = moovMoney,
                            onCheckedChange = {
                                moovMoney = it
                                viewModel.updatePlatformSettings(commissionRate, orangeMoney, it, cashPayment)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = TerracottaPrimary)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Paiement Espèces à la livraison", fontSize = 13.sp)
                        Switch(
                            checked = cashPayment,
                            onCheckedChange = {
                                cashPayment = it
                                viewModel.updatePlatformSettings(commissionRate, orangeMoney, moovMoney, it)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = TerracottaPrimary)
                        )
                    }
                }
            }
        }

        // Section: Répertoire administratif INSD Burkina Faso
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Map, contentDescription = null, tint = TerracottaPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Localités INSD RGPH (${localities.size} chargées)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Données officielles : Centre (Kadiogo, Ouagadougou et ses secteurs 1-55), Hauts-Bassins (Houet, Bobo-Dioulasso), Centre-Ouest (Boulkiemdé, Koudougou).",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
