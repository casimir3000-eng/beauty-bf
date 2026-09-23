package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.Appointment
import com.example.data.model.AppointmentStatus
import com.example.data.model.BeautyService
import com.example.ui.BeautyViewModel
import com.example.ui.components.DemoDataNotice
import com.example.ui.components.StatusChip
import com.example.ui.theme.AmberGoldSecondary
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmSurfaceBorder
import com.example.ui.theme.WarningAmber
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderDashboardScreen(
    viewModel: BeautyViewModel,
    modifier: Modifier = Modifier
) {
    val currentProviderId by viewModel.currentProviderId.collectAsState()
    val allProviders by viewModel.allProviders.collectAsState()
    val provider = allProviders.find { it.id == currentProviderId } ?: allProviders.firstOrNull()

    val allAppointments by viewModel.allAppointments.collectAsState()
    val providerAppointments = allAppointments.filter { it.providerId == currentProviderId }

    val pendingRequests = providerAppointments.filter { it.status == AppointmentStatus.PENDING }
    val confirmedAppointments = providerAppointments.filter { it.status == AppointmentStatus.CONFIRMED }
    val completedAppointments = providerAppointments.filter { it.status == AppointmentStatus.COMPLETED }

    val totalTurnover = completedAppointments.sumOf { it.totalFcfa }

    val allServices by viewModel.allServices.collectAsState()
    val providerServices = allServices.filter { it.providerId == currentProviderId }

    var showAddServiceDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddServiceDialog = true },
                containerColor = TerracottaPrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_service_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter une prestation")
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("provider_dashboard_scroll"),
            contentPadding = PaddingValues(16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                DemoDataNotice()
            }

            // Provider Banner & Salon Identity
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, WarmSurfaceBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(com.example.ui.theme.AmberSoftBg, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Spa, contentDescription = null, tint = AmberGoldSecondary, modifier = Modifier.size(28.dp))
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = provider?.salonName ?: "Mon Espace Beauté",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF111827)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Espace Gestionnaire Prestataire",
                                    fontSize = 13.sp,
                                    color = AmberGoldSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Counters: Aujourd'hui, RDV confirmés, Chiffre d'affaires
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiCard(
                        title = "En attente",
                        value = "${pendingRequests.size}",
                        icon = Icons.Default.NotificationsActive,
                        color = WarningAmber,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "Confirmés",
                        value = "${confirmedAppointments.size}",
                        icon = Icons.Default.CalendarToday,
                        color = SuccessGreen,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "Revenus",
                        value = viewModel.formatFcfa(totalTurnover),
                        icon = Icons.Default.MonetizationOn,
                        color = TerracottaPrimary,
                        modifier = Modifier.weight(1.3f)
                    )
                }
            }

            // Section: Demandes de rendez-vous en attente (Direct accept/refuse)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Demandes à traiter (${pendingRequests.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color(0xFF111827)
                    )
                    if (pendingRequests.isNotEmpty()) {
                        Text(
                            text = "Nécessite votre validation",
                            fontSize = 12.5.sp,
                            color = WarningAmber,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (pendingRequests.isEmpty()) {
                item {
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, WarmSurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Aucune nouvelle demande de rendez-vous en attente.",
                            fontSize = 13.5.sp,
                            color = Color(0xFF4B5563),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(pendingRequests) { apt ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.5.dp, WarningAmber),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("pending_apt_request_${apt.id}")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Cliente : ${apt.clientName}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color(0xFF111827)
                                )
                                Text(
                                    text = viewModel.formatFcfa(apt.totalFcfa),
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TerracottaPrimary,
                                    fontSize = 15.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Soin : ${apt.serviceName} (${apt.durationMinutes} min)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1F2937)
                            )
                            Text(
                                text = "Date : ${apt.date} à ${apt.timeSlot}",
                                fontSize = 13.sp,
                                color = Color(0xFF4B5563),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = if (apt.isHomeService) "Lieu : À domicile (${apt.clientAddress})" else "Lieu : En salon",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (apt.isHomeService) TerracottaPrimary else Color(0xFF4B5563)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.declineAppointment(apt.id) },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.5.dp, ErrorRed),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("decline_apt_btn_${apt.id}")
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(17.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Refuser", color = ErrorRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { viewModel.acceptAppointment(apt.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("accept_apt_btn_${apt.id}")
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(17.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Accepter", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Section: Planning & Rendez-vous confirmés
            item {
                Text(
                    text = "Planning des prestations confirmées (${confirmedAppointments.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (confirmedAppointments.isEmpty()) {
                item {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Aucune prestation confirmée en cours.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            } else {
                items(confirmedAppointments) { apt ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${apt.clientName} • ${apt.serviceName}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "${apt.date} à ${apt.timeSlot} • ${apt.clientPhone}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (apt.isHomeService) "Domicile (${apt.clientAddress})" else "En salon",
                                    fontSize = 11.sp,
                                    color = TerracottaPrimary
                                )
                            }

                            Button(
                                onClick = { viewModel.completeAppointment(apt.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("complete_apt_btn_${apt.id}")
                            ) {
                                Text("Terminer", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Section: Mes prestations au catalogue
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Catalogue de vos prestations (${providerServices.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            items(providerServices) { srv ->
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
                            Text(text = srv.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "${srv.durationMinutes} min • ${srv.category}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            text = viewModel.formatFcfa(srv.priceFcfa),
                            fontWeight = FontWeight.Bold,
                            color = TerracottaPrimary,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }

    // Add Service Dialog
    if (showAddServiceDialog) {
        AddServiceDialog(
            providerId = currentProviderId,
            onDismiss = { showAddServiceDialog = false },
            onAdd = { newService ->
                // will save to database via repository
                showAddServiceDialog = false
            }
        )
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, WarmSurfaceBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = Color(0xFF111827),
                maxLines = 1
            )
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4B5563),
                maxLines = 1
            )
        }
    }
}

@Composable
fun AddServiceDialog(
    providerId: String,
    onDismiss: () -> Unit,
    onAdd: (BeautyService) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Coiffure") }
    var priceText by remember { mutableStateOf("5000") }
    var durationText by remember { mutableStateOf("60") }
    var description by remember { mutableStateOf("") }
    var acceptsSalon by remember { mutableStateOf(true) }
    var acceptsHome by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajouter une prestation", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom de la prestation", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Catégorie (Tresses, Soins, Barbier...)", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Prix (FCFA)", fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = durationText,
                        onValueChange = { durationText = it },
                        label = { Text("Durée (min)", fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description", fontSize = 12.sp) },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = acceptsSalon, onCheckedChange = { acceptsSalon = it })
                    Text("En salon", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Checkbox(checked = acceptsHome, onCheckedChange = { acceptsHome = it })
                    Text("À domicile", fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = priceText.toIntOrNull() ?: 5000
                    val duration = durationText.toIntOrNull() ?: 60
                    val srv = BeautyService(
                        id = "srv-" + UUID.randomUUID().toString().take(8),
                        providerId = providerId,
                        name = name.ifBlank { "Nouveau soin" },
                        category = category,
                        description = description.ifBlank { "Prestation professionnelle" },
                        priceFcfa = price,
                        durationMinutes = duration,
                        acceptsSalon = acceptsSalon,
                        acceptsHome = acceptsHome,
                        homeSurchargeFcfa = if (acceptsHome) 1500 else 0
                    )
                    onAdd(srv)
                },
                colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary)
            ) {
                Text("Enregistrer")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Annuler") }
        }
    )
}
