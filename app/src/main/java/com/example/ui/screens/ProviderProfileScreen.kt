package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BeautyService
import com.example.data.model.Provider
import com.example.ui.BeautyViewModel
import com.example.ui.components.InteractiveMapPreview
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.AmberGoldSecondary
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.TerracottaSoftBg
import com.example.ui.theme.WarmSurfaceBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderProfileScreen(
    providerId: String,
    viewModel: BeautyViewModel,
    onBack: () -> Unit,
    onStartBooking: (BeautyService, Provider) -> Unit,
    onOpenChat: (Provider) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val providers by viewModel.allProviders.collectAsState()
    val provider = providers.find { it.id == providerId } ?: providers.firstOrNull()

    val allServices by viewModel.allServices.collectAsState()
    val providerServices = allServices.filter { it.providerId == providerId }

    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val isFavorite = provider?.let { favoriteIds.contains(it.id) } ?: false

    if (provider == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Prestataire introuvable")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(provider.salonName, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleFavorite(provider.id) },
                        modifier = Modifier.testTag("profile_favorite_btn")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favori",
                            tint = if (isFavorite) TerracottaPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            // Sticky booking bar if services exist
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, WarmSurfaceBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sticky_booking_bar")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Prestations dès",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4B5563)
                        )
                        Text(
                            text = viewModel.formatFcfa(providerServices.minOfOrNull { it.priceFcfa } ?: 5000),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TerracottaPrimary
                        )
                    }

                    Button(
                        onClick = {
                            val defaultService = providerServices.firstOrNull()
                            if (defaultService != null) {
                                onStartBooking(defaultService, provider)
                            } else {
                                Toast.makeText(context, "Aucune prestation disponible", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
                        modifier = Modifier.testTag("primary_reserve_btn")
                    ) {
                        Text("RÉSERVER UN SOIN", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("provider_profile_scroll"),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            // Profile Header Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, WarmSurfaceBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(com.example.ui.theme.AmberSoftBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = null,
                                    tint = AmberGoldSecondary,
                                    modifier = Modifier.size(38.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = provider.salonName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color(0xFF111827)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    VerifiedBadge(isVerified = provider.isVerified)
                                }
                                Text(
                                    text = provider.profession,
                                    color = TerracottaPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${provider.yearsExperience} ans d'expérience",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF4B5563)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Rating & Reviews row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = AmberGoldSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "${provider.rating} / 5.0",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = Color(0xFF111827)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "(${provider.reviewCount} avis vérifiés)",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF4B5563)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Description
                        Text(
                            text = provider.description,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = Color(0xFF374151)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action Buttons: MESSAGE, ITINERAIRE, APPELER, PARTAGER
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onOpenChat(provider) },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.5.dp, Color(0xFFD1D5DB)),
                                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("chat_action_btn")
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Chat, contentDescription = "Message", tint = Color(0xFF111827), modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("Message", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:${provider.latitude},${provider.longitude}?q=${Uri.encode(provider.salonName)}"))
                                    context.startActivity(Intent.createChooser(intent, "Itinéraire"))
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.5.dp, Color(0xFFD1D5DB)),
                                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("itinerary_action_btn")
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Directions, contentDescription = "Itinéraire", tint = Color(0xFF111827), modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("Itinéraire", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${provider.phone.replace(" ", "")}"))
                                    context.startActivity(intent)
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.5.dp, Color(0xFFD1D5DB)),
                                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("call_action_btn")
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Call, contentDescription = "Appeler", tint = Color(0xFF111827), modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("Appeler", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, "Découvrez ${provider.salonName} sur BEAUTY BF")
                                        putExtra(Intent.EXTRA_TEXT, "Réservez vos soins beauté chez ${provider.salonName} (${provider.city}, ${provider.sector}) sur BEAUTY BF : https://beautybf.bf/providers/${provider.id}")
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Partager le profil"))
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.5.dp, Color(0xFFD1D5DB)),
                                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("share_action_btn")
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Share, contentDescription = "Partager", tint = Color(0xFF111827), modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("Partager", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                                }
                            }
                        }
                    }
                }
            }

            // Working hours & Location summary
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, WarmSurfaceBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccessTime, contentDescription = null, tint = TerracottaPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Horaires : ${provider.openingHoursSummary}",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1F2937)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = TerracottaPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Adresse : ${provider.address} (${provider.sector}, ${provider.city})",
                                fontSize = 13.5.sp,
                                color = Color(0xFF374151)
                            )
                        }

                        if (provider.acceptsHomeService) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Home, contentDescription = null, tint = TerracottaPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Prestations à domicile : Rayon max ${provider.maxTravelRadiusKm.toInt()} km • Déplacement ${viewModel.formatFcfa(provider.baseTravelFeeFcfa)}",
                                    fontSize = 13.5.sp,
                                    color = TerracottaPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Interactive Map Visualizer
            item {
                InteractiveMapPreview(
                    providerName = provider.salonName,
                    providerSector = provider.sector,
                    clientSector = "Secteur 22 (Tampouy)",
                    distanceKm = 3.5,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            // Service Catalog Header
            item {
                Text(
                    text = "Catalogue des prestations (${providerServices.size})",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            // Services list
            items(providerServices) { service ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("service_item_${service.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, WarmSurfaceBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = service.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF111827)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${service.durationMinutes} min • ${service.category}",
                                fontSize = 13.sp,
                                color = TerracottaPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = service.description,
                                fontSize = 12.5.sp,
                                color = Color(0xFF4B5563),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (service.acceptsSalon) {
                                    Surface(
                                        color = Color(0xFFF3F4F6),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text("Salon", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151), modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                    }
                                }
                                if (service.acceptsHome) {
                                    Surface(
                                        color = TerracottaSoftBg,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text("Domicile (+${viewModel.formatFcfa(service.homeSurchargeFcfa)})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TerracottaPrimary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = viewModel.formatFcfa(service.priceFcfa),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = Color(0xFF111827)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { onStartBooking(service, provider) },
                                colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                                modifier = Modifier.testTag("book_service_${service.id}")
                            ) {
                                Text("Choisir", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
