package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.ui.BeautyViewModel
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarningAmber

val AVAILABLE_TIME_SLOTS = listOf(
    "08:30", "09:30", "10:30", "11:30", "14:00", "15:30", "17:00", "18:30"
)

val DATES_LIST = listOf(
    Pair("2026-09-17", "Aujourd'hui (17 Sept)"),
    Pair("2026-09-18", "Demain (18 Sept)"),
    Pair("2026-09-19", "Samedi (19 Sept)"),
    Pair("2026-09-20", "Dimanche (20 Sept)"),
    Pair("2026-09-21", "Lundi (21 Sept)")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingBottomSheet(
    viewModel: BeautyViewModel,
    onDismiss: () -> Unit,
    onBookingConfirmed: () -> Unit
) {
    val draft by viewModel.bookingDraft.collectAsState()
    val bookedSlots by viewModel.bookedSlotsForSelectedDate.collectAsState()
    val errorMessage by viewModel.bookingErrorMessage.collectAsState()
    val successMessage by viewModel.bookingSuccessMessage.collectAsState()

    val service = draft.service ?: return
    val provider = draft.provider ?: return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White,
        modifier = Modifier.testTag("booking_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 36.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Demande de rendez-vous",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = provider.salonName,
                        fontSize = 14.sp,
                        color = TerracottaPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Fermer", tint = Color(0xFF111827))
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = Color(0xFFE5E7EB))

            // Step 1: Prestation Récap
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, Color(0xFFE5E7EB)),
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
                        Text(service.name, fontWeight = FontWeight.Bold, fontSize = 15.5.sp, color = Color(0xFF111827))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("${service.durationMinutes} min • ${service.category}", fontSize = 13.sp, color = Color(0xFF4B5563), fontWeight = FontWeight.Medium)
                    }
                    Text(viewModel.formatFcfa(service.priceFcfa), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = TerracottaPrimary)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Step 2: Choisir Salon vs Domicile
            Text(
                text = "1. Lieu de la prestation",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF111827)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Salon option
                Surface(
                    color = if (!draft.isHomeService) com.example.ui.theme.TerracottaSoftBg else Color.White,
                    border = BorderStroke(2.dp, if (!draft.isHomeService) TerracottaPrimary else Color(0xFFD1D5DB)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.updateBookingHomeService(false) }
                        .testTag("option_salon_btn")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = null,
                            tint = if (!draft.isHomeService) TerracottaPrimary else Color(0xFF4B5563),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "En Salon",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp,
                            color = if (!draft.isHomeService) TerracottaPrimary else Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = provider.sector,
                            fontSize = 12.sp,
                            color = Color(0xFF4B5563),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Domicile option
                if (provider.acceptsHomeService && service.acceptsHome) {
                    Surface(
                        color = if (draft.isHomeService) com.example.ui.theme.TerracottaSoftBg else Color.White,
                        border = BorderStroke(2.dp, if (draft.isHomeService) TerracottaPrimary else Color(0xFFD1D5DB)),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.updateBookingHomeService(true) }
                            .testTag("option_home_btn")
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                                tint = if (draft.isHomeService) TerracottaPrimary else Color(0xFF4B5563),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "À Domicile",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp,
                                color = if (draft.isHomeService) TerracottaPrimary else Color(0xFF111827)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "+${viewModel.formatFcfa(draft.calculatedTravelFeeFcfa)}",
                                fontSize = 12.sp,
                                color = TerracottaPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // If Domicile: address & sector fields
            AnimatedVisibility(visible = draft.isHomeService) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    OutlinedTextField(
                        value = draft.clientAddress,
                        onValueChange = { viewModel.updateBookingAddress(it, draft.clientSector, draft.accessInstructions) },
                        label = { Text("Votre adresse à domicile (Quartier/Rue)", fontSize = 13.5.sp, fontWeight = FontWeight.Medium) },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = TerracottaPrimary) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TerracottaPrimary,
                            unfocusedBorderColor = Color(0xFFD1D5DB)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_address_input")
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = draft.accessInstructions,
                        onValueChange = { viewModel.updateBookingAddress(draft.clientAddress, draft.clientSector, it) },
                        label = { Text("Instructions d'accès (repères, porte, etc.)", fontSize = 13.5.sp, fontWeight = FontWeight.Medium) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TerracottaPrimary,
                            unfocusedBorderColor = Color(0xFFD1D5DB)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Step 3: Date selection
            Text(
                text = "2. Date souhaitée",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF111827)
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(DATES_LIST) { (dateStr, label) ->
                    val isSelected = draft.selectedDate == dateStr
                    Surface(
                        color = if (isSelected) TerracottaPrimary else Color.White,
                        border = BorderStroke(1.5.dp, if (isSelected) TerracottaPrimary else Color(0xFFD1D5DB)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .clickable { viewModel.updateBookingDate(dateStr) }
                            .testTag("date_chip_$dateStr")
                    ) {
                        Text(
                            text = label,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                            color = if (isSelected) Color.White else Color(0xFF374151),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Step 4: Time Slot selection with AVAILABILITY ENGINE
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "3. Heure du créneau",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "Vérification en direct",
                    fontSize = 12.sp,
                    color = Color(0xFF4B5563),
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Slots Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AVAILABLE_TIME_SLOTS.take(4).forEach { slot ->
                    val isBooked = bookedSlots.contains(slot)
                    val isSelected = draft.selectedTimeSlot == slot

                    Surface(
                        color = when {
                            isBooked -> Color(0xFFF3F4F6)
                            isSelected -> TerracottaPrimary
                            else -> Color.White
                        },
                        border = BorderStroke(
                            1.5.dp,
                            when {
                                isBooked -> Color(0xFFE5E7EB)
                                isSelected -> TerracottaPrimary
                                else -> Color(0xFFD1D5DB)
                            }
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable(enabled = !isBooked) {
                                viewModel.updateBookingTimeSlot(slot)
                            }
                            .testTag("slot_btn_$slot")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = slot,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    isBooked -> Color(0xFF9CA3AF)
                                    isSelected -> Color.White
                                    else -> Color(0xFF111827)
                                }
                            )
                            if (isBooked) {
                                Text("Occupé", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ErrorRed)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AVAILABLE_TIME_SLOTS.drop(4).forEach { slot ->
                    val isBooked = bookedSlots.contains(slot)
                    val isSelected = draft.selectedTimeSlot == slot

                    Surface(
                        color = when {
                            isBooked -> Color(0xFFF3F4F6)
                            isSelected -> TerracottaPrimary
                            else -> Color.White
                        },
                        border = BorderStroke(
                            1.5.dp,
                            when {
                                isBooked -> Color(0xFFE5E7EB)
                                isSelected -> TerracottaPrimary
                                else -> Color(0xFFD1D5DB)
                            }
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable(enabled = !isBooked) {
                                viewModel.updateBookingTimeSlot(slot)
                            }
                            .testTag("slot_btn_$slot")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = slot,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    isBooked -> Color(0xFF9CA3AF)
                                    isSelected -> Color.White
                                    else -> Color(0xFF111827)
                                }
                            )
                            if (isBooked) {
                                Text("Occupé", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ErrorRed)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Step 6: Récapitulatif
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, Color(0xFFE5E7EB)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Récapitulatif de la commande", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF111827))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Prestation : ${service.name}", fontSize = 13.5.sp, color = Color(0xFF4B5563))
                        Text(viewModel.formatFcfa(service.priceFcfa), fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                    }

                    if (draft.isHomeService) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Déplacement à domicile", fontSize = 13.5.sp, color = Color(0xFF4B5563))
                            Text(viewModel.formatFcfa(draft.calculatedTravelFeeFcfa), fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Date & Heure", fontSize = 13.5.sp, color = Color(0xFF4B5563))
                        Text("${draft.selectedDate} à ${draft.selectedTimeSlot.ifEmpty { "--:--" }}", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF3F4F6))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("TOTAL À PAYER", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color(0xFF111827))
                        Text(viewModel.formatFcfa(draft.totalFcfa), fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = TerracottaPrimary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Règlement sur place après confirmation : Espèces ou Orange/Moov Money", fontSize = 12.sp, color = Color(0xFF4B5563), fontWeight = FontWeight.Medium)
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = errorMessage ?: "",
                    color = ErrorRed,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Step 7: Submit Button
            Button(
                onClick = {
                    viewModel.confirmBooking(onSuccess = {
                        onBookingConfirmed()
                    })
                },
                enabled = draft.selectedTimeSlot.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_appointment_request_btn")
            ) {
                Text(
                    text = "CONFIRMER MA DEMANDE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }
    }
}
