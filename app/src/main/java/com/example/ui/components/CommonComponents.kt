package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppointmentStatus
import com.example.data.model.Provider
import com.example.data.model.UserRole
import com.example.ui.theme.AmberGoldSecondary
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.WarningAmber

@Composable
fun DemoDataNotice(modifier: Modifier = Modifier) {
    Surface(
        color = com.example.ui.theme.AmberSoftBg,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.AmberBorderSoft),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("demo_data_notice")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Shield,
                contentDescription = null,
                tint = AmberGoldSecondary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Données de démonstration fictives • Burkina Faso (INSD RGPH 2019)",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = com.example.ui.theme.TextSecondaryDark,
                lineHeight = 16.sp
            )
        }
    }
}


@Composable
fun RoleSwitcherBar(
    currentRole: UserRole,
    onSelectRole: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("role_switcher_bar")
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            RoleChip(
                title = "Client",
                icon = Icons.Default.Person,
                isSelected = currentRole == UserRole.CLIENT,
                onClick = { onSelectRole(UserRole.CLIENT) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            RoleChip(
                title = "Prestataire",
                icon = Icons.Default.Storefront,
                isSelected = currentRole == UserRole.PROVIDER,
                onClick = { onSelectRole(UserRole.PROVIDER) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            RoleChip(
                title = "Admin",
                icon = Icons.Default.AdminPanelSettings,
                isSelected = currentRole == UserRole.ADMIN,
                onClick = { onSelectRole(UserRole.ADMIN) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RoleChip(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isSelected) TerracottaPrimary else Color.Transparent,
        contentColor = if (isSelected) Color.White else TextPrimaryDark,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .clickable { onClick() }
            .testTag("role_chip_${title.lowercase()}")
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                fontSize = 13.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun VerifiedBadge(isVerified: Boolean, modifier: Modifier = Modifier) {
    if (isVerified) {
        Surface(
            color = Color(0xFFDCFCE7),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC)),
            modifier = modifier.testTag("verified_badge")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Vérifié",
                    tint = SuccessGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Vérifié",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
            }
        }
    }
}

@Composable
fun StatusChip(status: AppointmentStatus, modifier: Modifier = Modifier) {
    val (bgColor, textColor, label) = when (status) {
        AppointmentStatus.PENDING -> Triple(Color(0xFFFEF3C7), Color(0xFF92400E), "En attente")
        AppointmentStatus.CONFIRMED -> Triple(Color(0xFFDCFCE7), Color(0xFF166534), "Confirmé")
        AppointmentStatus.DECLINED -> Triple(Color(0xFFFEE2E2), Color(0xFF991B1B), "Refusé")
        AppointmentStatus.CANCELLED_BY_CLIENT -> Triple(Color(0xFFFEE2E2), Color(0xFF991B1B), "Annulé (Client)")
        AppointmentStatus.CANCELLED_BY_PROVIDER -> Triple(Color(0xFFFEE2E2), Color(0xFF991B1B), "Annulé (Salon)")
        AppointmentStatus.IN_PROGRESS -> Triple(Color(0xFFDBEAFE), Color(0xFF1E40AF), "En cours")
        AppointmentStatus.COMPLETED -> Triple(Color(0xFFDCFCE7), Color(0xFF166534), "Terminé")
        AppointmentStatus.NO_SHOW -> Triple(Color(0xFFF3E8FF), Color(0xFF6B21A8), "Non présenté")
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.3f)),
        modifier = modifier.testTag("status_chip_${status.name}")
    ) {
        Text(
            text = label,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}


/**
 * Requirement 41: DESIGN DES CARTES PRESTATAIRES
 * Photo, Nom du salon, Stars 4.8, Spécialités, 📍 Ouagadougou • Secteur XX,
 * À partir de 5 000 FCFA, 🏠 À domicile, 🏢 En salon, [VOIR LE PROFIL]
 */
@Composable
fun ProviderCard(
    provider: Provider,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClickProfile: () -> Unit,
    onBookClick: () -> Unit,
    formatFcfa: (Int) -> String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickProfile() }
            .testTag("provider_card_${provider.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE5E7EB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Provider Avatar/Icon
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(com.example.ui.theme.TerracottaSoftBg),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when {
                        provider.profession.contains("Barbier", ignoreCase = true) -> Icons.Default.Face
                        provider.profession.contains("Ongle", ignoreCase = true) || provider.profession.contains("Spa", ignoreCase = true) -> Icons.Default.Spa
                        else -> Icons.Default.ContentCut
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = TerracottaPrimary,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = provider.salonName,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        VerifiedBadge(isVerified = provider.isVerified)
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AmberGoldSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${provider.rating}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF111827)
                        )
                        Text(
                            text = " (${provider.reviewCount} avis)",
                            fontSize = 13.sp,
                            color = Color(0xFF4B5563)
                        )
                    }
                }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.testTag("favorite_btn_${provider.id}")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favori",
                        tint = if (isFavorite) TerracottaPrimary else Color(0xFF6B7280)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Specialties
            Text(
                text = provider.specialties.take(3).joinToString(" • "),
                fontSize = 14.sp,
                color = TerracottaPrimary,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Location: Ouagadougou • Secteur XX
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF4B5563),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "${provider.city} • ${provider.sector}",
                    fontSize = 13.5.sp,
                    color = Color(0xFF374151),
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Badges: Salon / Domicile & Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        color = Color(0xFFF3F4F6),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Business,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = Color(0xFF374151)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("En salon", fontSize = 12.sp, color = Color(0xFF374151), fontWeight = FontWeight.SemiBold)
                        }
                    }

                    if (provider.acceptsHomeService) {
                        Surface(
                            color = com.example.ui.theme.TerracottaSoftBg,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.TerracottaBorderSoft)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = null,
                                    modifier = Modifier.size(13.dp),
                                    tint = TerracottaPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("À domicile", fontSize = 12.sp, color = TerracottaPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Text(
                    text = "Dès ${formatFcfa(provider.baseTravelFeeFcfa * 2)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF111827)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onClickProfile,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFD1D5DB)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF111827)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("see_profile_btn_${provider.id}")
                ) {
                    Text("Voir profil", fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onBookClick,
                    colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("book_provider_btn_${provider.id}")
                ) {
                    Text("Réserver", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

/**
 * Requirement 24: Interactive Visual Map Preview
 * Shows provider location, client location, and itinerary route
 */
@Composable
fun InteractiveMapPreview(
    providerName: String,
    providerSector: String,
    clientSector: String,
    distanceKm: Double,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE5E7EB)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_map_preview")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = TerracottaPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Itinéraire & Distance estimée",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF111827)
                    )
                }
                Surface(
                    color = TerracottaPrimary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "À ${String.format("%.1f", distanceKm)} km",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Stylized vector map canvas with nodes and dotted path
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE2E8F0))
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Draw map grid lines
                drawLine(
                    color = Color(0xFFCBD5E1),
                    start = Offset(0f, canvasHeight * 0.33f),
                    end = Offset(canvasWidth, canvasHeight * 0.33f),
                    strokeWidth = 2f
                )
                drawLine(
                    color = Color(0xFFCBD5E1),
                    start = Offset(0f, canvasHeight * 0.66f),
                    end = Offset(canvasWidth, canvasHeight * 0.66f),
                    strokeWidth = 2f
                )
                drawLine(
                    color = Color(0xFFCBD5E1),
                    start = Offset(canvasWidth * 0.5f, 0f),
                    end = Offset(canvasWidth * 0.5f, canvasHeight),
                    strokeWidth = 2f
                )

                val p1 = Offset(canvasWidth * 0.2f, canvasHeight * 0.65f)
                val p2 = Offset(canvasWidth * 0.8f, canvasHeight * 0.35f)

                // Dotted route line
                drawLine(
                    color = TerracottaPrimary,
                    start = p1,
                    end = p2,
                    strokeWidth = 4f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                )

                // Client Pin
                drawCircle(color = Color(0xFF386591), radius = 13f, center = p1)
                drawCircle(color = Color.White, radius = 5f, center = p1)

                // Provider Pin
                drawCircle(color = TerracottaPrimary, radius = 15f, center = p2)
                drawCircle(color = Color.White, radius = 6f, center = p2)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Client: $clientSector",
                    fontSize = 12.5.sp,
                    color = Color(0xFF1D4ED8),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Salon: $providerSector",
                    fontSize = 12.5.sp,
                    color = TerracottaPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

