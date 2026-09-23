package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import com.example.ui.BeautyViewModel
import com.example.ui.components.DemoDataNotice
import com.example.ui.components.StatusChip
import com.example.ui.theme.AmberGoldSecondary
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmSurfaceBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    viewModel: BeautyViewModel,
    modifier: Modifier = Modifier
) {
    val allAppointments by viewModel.allAppointments.collectAsState()
    val currentClientId = viewModel.currentUser.value.id
    val clientAppointments = allAppointments.filter { it.clientUserId == currentClientId }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("À venir", "Terminés", "Tous")

    var appointmentToReview by remember { mutableStateOf<Appointment?>(null) }
    var appointmentToCancel by remember { mutableStateOf<Appointment?>(null) }

    val filteredList = when (selectedTabIndex) {
        0 -> clientAppointments.filter {
            it.status == AppointmentStatus.PENDING || it.status == AppointmentStatus.CONFIRMED || it.status == AppointmentStatus.IN_PROGRESS
        }
        1 -> clientAppointments.filter { it.status == AppointmentStatus.COMPLETED }
        else -> clientAppointments
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("appointments_screen")
    ) {
        DemoDataNotice()

        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = TerracottaPrimary
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.SemiBold,
                            fontSize = 14.5.sp,
                            color = if (selectedTabIndex == index) TerracottaPrimary else Color(0xFF4B5563)
                        )
                    },
                    modifier = Modifier.testTag("appointment_tab_$index")
                )
            }
        }

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.EventNote,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = Color(0xFF9CA3AF)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Aucun rendez-vous dans cette catégorie",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4B5563)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredList) { apt ->
                    AppointmentCard(
                        appointment = apt,
                        formatFcfa = { viewModel.formatFcfa(it) },
                        onReviewClick = { appointmentToReview = apt },
                        onCancelClick = { appointmentToCancel = apt }
                    )
                }
            }
        }
    }

    // Review Dialog
    if (appointmentToReview != null) {
        ReviewDialog(
            appointment = appointmentToReview!!,
            onDismiss = { appointmentToReview = null },
            onSubmit = { rating, comment, q, p, w, v ->
                viewModel.submitReview(
                    appointmentId = appointmentToReview!!.id,
                    providerId = appointmentToReview!!.providerId,
                    rating = rating,
                    comment = comment,
                    quality = q,
                    punctuality = p,
                    welcome = w,
                    value = v
                )
                appointmentToReview = null
            }
        )
    }

    // Cancel Confirmation Dialog
    if (appointmentToCancel != null) {
        AlertDialog(
            onDismissRequest = { appointmentToCancel = null },
            title = {
                Text(
                    text = "Annuler le rendez-vous ?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            },
            text = {
                Text(
                    text = "Êtes-vous sûr de vouloir annuler votre rendez-vous chez ${appointmentToCancel!!.providerName} prévu le ${appointmentToCancel!!.date} à ${appointmentToCancel!!.timeSlot} ?",
                    fontSize = 14.sp,
                    color = Color(0xFF374151)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.cancelAppointment(appointmentToCancel!!.id)
                        appointmentToCancel = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Oui, Annuler", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { appointmentToCancel = null }) {
                    Text("Non, garder", fontWeight = FontWeight.SemiBold, color = Color(0xFF4B5563))
                }
            }
        )
    }
}

@Composable
fun AppointmentCard(
    appointment: Appointment,
    formatFcfa: (Int) -> String,
    onReviewClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, WarmSurfaceBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("appointment_card_${appointment.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = appointment.serviceName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.5.sp,
                        color = Color(0xFF111827)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = appointment.providerName,
                        color = TerracottaPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                StatusChip(status = appointment.status)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF3F4F6))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(17.dp),
                    tint = TerracottaPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${appointment.date} à ${appointment.timeSlot} (${appointment.durationMinutes} min)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                val locIcon = if (appointment.isHomeService) Icons.Default.Home else Icons.Default.Business
                val locLabel = if (appointment.isHomeService) "À Domicile : ${appointment.clientAddress}" else "En Salon : ${appointment.clientSector}"
                Icon(
                    locIcon,
                    contentDescription = null,
                    modifier = Modifier.size(17.dp),
                    tint = Color(0xFF4B5563)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = locLabel,
                    fontSize = 13.sp,
                    color = Color(0xFF4B5563),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total à régler",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = formatFcfa(appointment.totalFcfa),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = Color(0xFF111827)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (appointment.status == AppointmentStatus.PENDING || appointment.status == AppointmentStatus.CONFIRMED) {
                        OutlinedButton(
                            onClick = onCancelClick,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.5.dp, ErrorRed),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("cancel_apt_btn_${appointment.id}")
                        ) {
                            Text("Annuler", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ErrorRed)
                        }
                    }

                    if (appointment.status == AppointmentStatus.COMPLETED) {
                        Button(
                            onClick = onReviewClick,
                            colors = ButtonDefaults.buttonColors(containerColor = AmberGoldSecondary),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("rate_apt_btn_${appointment.id}")
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Donner un avis", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Requirement 25: AVIS ET ÉVALUATIONS
 * Note 1 à 5, commentaire, qualité, ponctualité, accueil, rapport qualité/prix
 */
@Composable
fun ReviewDialog(
    appointment: Appointment,
    onDismiss: () -> Unit,
    onSubmit: (Double, String, Int, Int, Int, Int) -> Unit
) {
    var rating by remember { mutableDoubleStateOf(5.0) }
    var comment by remember { mutableStateOf("") }
    var quality by remember { mutableIntStateOf(5) }
    var punctuality by remember { mutableIntStateOf(5) }
    var welcome by remember { mutableIntStateOf(5) }
    var value by remember { mutableIntStateOf(5) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Évaluer ${appointment.providerName}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            Column {
                Text(
                    text = "Prestation : ${appointment.serviceName}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Star Picker
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    (1..5).forEach { star ->
                        IconButton(onClick = { rating = star.toDouble() }) {
                            Icon(
                                imageVector = if (star <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "$star étoiles",
                                tint = AmberGoldSecondary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Detailed ratings
                CriteriaScoreRow("Qualité du service", quality) { quality = it }
                CriteriaScoreRow("Ponctualité", punctuality) { punctuality = it }
                CriteriaScoreRow("Accueil & Convivialité", welcome) { welcome = it }
                CriteriaScoreRow("Rapport qualité / prix", value) { value = it }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Votre commentaire...", fontSize = 12.sp) },
                    maxLines = 3,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmit(rating, comment.ifBlank { "Très bonne prestation !" }, quality, punctuality, welcome, value)
                },
                colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary)
            ) {
                Text("Publier l'avis")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}

@Composable
private fun CriteriaScoreRow(
    label: String,
    currentScore: Int,
    onScoreChanged: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row {
            (1..5).forEach { score ->
                Text(
                    text = "$score",
                    fontSize = 11.sp,
                    fontWeight = if (score == currentScore) FontWeight.Bold else FontWeight.Normal,
                    color = if (score == currentScore) TerracottaPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clickable { onScoreChanged(score) }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
