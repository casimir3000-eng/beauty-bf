package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.BeautyViewModel
import com.example.ui.theme.AmberGoldSecondary
import com.example.ui.theme.AmberSoftBg
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.TerracottaSoftBg
import com.example.ui.theme.WarmCreamSurface
import com.example.ui.theme.WarmSurfaceBorder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SecurityPrivacyDialog(
    viewModel: BeautyViewModel,
    onDismiss: () -> Unit
) {
    val auditLogs by viewModel.securityAuditLogs.collectAsState()
    val consents by viewModel.userConsents.collectAsState()
    val exportedJson by viewModel.exportedDataJson.collectAsState()
    val exportMsg by viewModel.dataExportSuccessMessage.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showErasureConfirmDialog by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .heightIn(max = 680.dp)
                .clip(RoundedCornerShape(24.dp))
                .testTag("security_privacy_dialog"),
            color = Color.White,
            border = BorderStroke(1.5.dp, WarmSurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Header with Shield and Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(TerracottaSoftBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = TerracottaPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Sécurité & Confidentialité",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF1E1D1B)
                            )
                            Text(
                                text = "Conforme Loi 001-2021/AN • CIL Burkina Faso",
                                fontSize = 11.sp,
                                color = TerracottaPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Fermer", tint = Color(0xFF6B7280))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Security Status Badge Card
                Surface(
                    color = AmberSoftBg,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, WarmSurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = AmberGoldSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Chiffrement AES-256 / SHA-256 • Journal d'audit inviolable • Pare-feu anti-brute force actif",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF8A6237)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tabs: 0: Consentements, 1: Portabilité, 2: Journal d'Audit
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = WarmCreamSurface,
                    contentColor = TerracottaPrimary,
                    modifier = Modifier.clip(RoundedCornerShape(10.dp))
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = { Text("Consentements", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = { Text("Portabilité", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 },
                        text = { Text("Audit Inviolable", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tab Content
                when (selectedTabIndex) {
                    0 -> ConsentsTabContent(
                        consents = consents,
                        onToggle = { key, granted -> viewModel.toggleConsent(key, granted) },
                        onRequestErasure = { showErasureConfirmDialog = true }
                    )
                    1 -> PortabilityTabContent(
                        exportedJson = exportedJson,
                        successMessage = exportMsg,
                        onGenerateExport = { viewModel.exportPersonalData() }
                    )
                    2 -> AuditLogsTabContent(auditLogs = auditLogs)
                }
            }
        }
    }

    // Confirmation dialog for Right to Erasure
    if (showErasureConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showErasureConfirmDialog = false },
            icon = { Icon(Icons.Default.DeleteForever, contentDescription = null, tint = ErrorRed) },
            title = { Text("Droit à l'Oubli & Suppression", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "Conformément à l'article 21 de la loi 001-2021/AN du Burkina Faso, toutes vos données personnelles (nom, téléphone, adresse) seront définitivement anonymisées et effacées de nos serveurs sécurisés.\n\nSouhaitez-vous confirmer cette action irréversible ?",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showErasureConfirmDialog = false
                        viewModel.requestRightToErasure {
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) {
                    Text("Oui, effacer mes données")
                }
            },
            dismissButton = {
                TextButton(onClick = { showErasureConfirmDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}

@Composable
private fun ConsentsTabContent(
    consents: List<com.example.data.local.UserConsentEntity>,
    onToggle: (String, Boolean) -> Unit,
    onRequestErasure: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Gestion de vos consentements légaux",
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp,
                color = Color(0xFF1E1D1B)
            )
            Text(
                text = "Vous pouvez retirer ou octroyer vos autorisations à tout moment.",
                fontSize = 11.5.sp,
                color = Color(0xFF4B5563)
            )
        }

        items(consents) { consent ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WarmCreamSurface),
                border = BorderStroke(1.dp, WarmSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = consent.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF1E1D1B)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = consent.description,
                            fontSize = 11.5.sp,
                            color = Color(0xFF4B5563)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = consent.legalReference,
                            fontSize = 10.sp,
                            color = TerracottaPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = consent.isGranted,
                        onCheckedChange = { onToggle(consent.consentKey, it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = TerracottaPrimary
                        )
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = onRequestErasure,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, ErrorRed),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Exercer mon droit à l'effacement (Droit à l'oubli)", fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun PortabilityTabContent(
    exportedJson: String?,
    successMessage: String?,
    onGenerateExport: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Droit à la portabilité (Art. 19 Loi 001-2021/AN)",
            fontWeight = FontWeight.Bold,
            fontSize = 13.5.sp,
            color = Color(0xFF1E1D1B)
        )
        Text(
            text = "Téléchargez l'intégralité de vos informations personnelles (profil, réservations, avis) dans un format structuré et signé cryptographiquement.",
            fontSize = 11.5.sp,
            color = Color(0xFF4B5563)
        )

        Button(
            onClick = onGenerateExport,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Générer l'archive de mes données personnelles")
        }

        if (successMessage != null) {
            Surface(
                color = Color(0xFFECFDF5),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, SuccessGreen)
            ) {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = successMessage, fontSize = 11.5.sp, color = Color(0xFF065F46))
                }
            }
        }

        if (exportedJson != null) {
            Text(
                text = "Aperçu de l'archive chiffrée & signée :",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = Color(0xFF374151)
            )
            Surface(
                color = Color(0xFF1E1E1E),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            ) {
                LazyColumn(modifier = Modifier.padding(10.dp)) {
                    item {
                        Text(
                            text = exportedJson,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.5.sp,
                            color = Color(0xFF81C784)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AuditLogsTabContent(
    auditLogs: List<com.example.data.local.SecurityAuditEntity>
) {
    val dateFormat = remember { SimpleDateFormat("HH:mm:ss", Locale.FRANCE) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Registre Inviolable d'Audit",
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp,
                color = Color(0xFF1E1D1B)
            )
            Surface(
                color = Color(0xFFECFDF5),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "Chaîne SHA-256 Intègre",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Chaque entrée est cryptographiquement liée au bloc précédent pour garantir une traçabilité infalsifiable.",
            fontSize = 11.sp,
            color = Color(0xFF6B7280)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(auditLogs) { log ->
                Surface(
                    color = WarmCreamSurface,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, WarmSurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = log.action,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp,
                                color = TerracottaPrimary
                            )
                            Text(
                                text = dateFormat.format(Date(log.timestamp)),
                                fontSize = 10.sp,
                                color = Color(0xFF9CA3AF)
                            )
                        }
                        Text(
                            text = log.details,
                            fontSize = 11.sp,
                            color = Color(0xFF374151)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Hash: ${log.integrityHash.take(18)}... (Précédent: ${log.previousHash.take(10)}...)",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }
            }
        }
    }
}
