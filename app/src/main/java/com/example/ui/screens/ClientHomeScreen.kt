package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Provider
import com.example.ui.BeautyViewModel
import com.example.ui.components.DemoDataNotice
import com.example.ui.components.ProviderCard
import com.example.ui.theme.AmberGoldSecondary
import com.example.ui.theme.TerracottaPrimary

data class ServiceCategoryItem(
    val name: String,
    val icon: ImageVector
)

val BEAUTY_CATEGORIES = listOf(
    ServiceCategoryItem("Tous", Icons.Default.Spa),
    ServiceCategoryItem("Coiffure", Icons.Default.ContentCut),
    ServiceCategoryItem("Tresses", Icons.Default.Face),
    ServiceCategoryItem("Barbier", Icons.Default.Face),
    ServiceCategoryItem("Maquillage", Icons.Default.Brush),
    ServiceCategoryItem("Onglerie", Icons.Default.Spa),
    ServiceCategoryItem("Soins", Icons.Default.Spa)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientHomeScreen(
    viewModel: BeautyViewModel,
    onNavigateToProviderProfile: (String) -> Unit,
    onStartBooking: (Provider) -> Unit,
    modifier: Modifier = Modifier
) {
    val providers by viewModel.filteredProviders.collectAsState()
    val allServices by viewModel.allServices.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val filterHomeOnly by viewModel.filterHomeServiceOnly.collectAsState()
    val filterMinRating by viewModel.filterMinRating.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("client_home_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Demo Notice
        item {
            DemoDataNotice()
        }

        // Feminine Hero Welcome Banner
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE5E7EB)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Bonjour, ${currentUser.firstName} ✨",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Sublimez votre beauté aujourd'hui au Burkina Faso",
                            fontSize = 13.sp,
                            color = Color(0xFF4B5563)
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = com.example.ui.theme.TerracottaSoftBg,
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, TerracottaPrimary),
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = null,
                                tint = TerracottaPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        // Top Location bar & Search
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                // Location Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Localisation",
                        tint = TerracottaPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Votre adresse de référence",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4B5563)
                        )
                        Text(
                            text = "${currentUser.city}, ${currentUser.sector}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchQuery.value = it },
                    placeholder = {
                        Text(
                            "Rechercher coiffeuse, tresses, barbier...",
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Recherche",
                            tint = TerracottaPrimary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Effacer", tint = Color(0xFF4B5563))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TerracottaPrimary,
                        unfocusedBorderColor = Color(0xFFD1D5DB),
                        focusedTextColor = Color(0xFF111827),
                        unfocusedTextColor = Color(0xFF111827),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_search_input")
                )
            }
        }

        // Section: "Que souhaitez-vous faire ?" - Category Cards
        item {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(
                    text = "Que souhaitez-vous faire ?",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(BEAUTY_CATEGORIES) { cat ->
                        val isSelected = selectedCategory.equals(cat.name, ignoreCase = true)
                        Surface(
                            color = if (isSelected) TerracottaPrimary else Color.White,
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (isSelected) TerracottaPrimary else Color(0xFFD1D5DB)
                            ),
                            shadowElevation = if (isSelected) 3.dp else 1.dp,
                            modifier = Modifier
                                .clickable { viewModel.selectedCategory.value = cat.name }
                                .testTag("category_chip_${cat.name.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = cat.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else TerracottaPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = cat.name,
                                    fontSize = 13.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isSelected) Color.White else Color(0xFF1F2937)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Filters row (Domicile, 4+ étoiles, Ville)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = filterHomeOnly,
                    onClick = { viewModel.filterHomeServiceOnly.value = !filterHomeOnly },
                    label = { Text("À domicile", fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = com.example.ui.theme.TerracottaSoftBg,
                        selectedLabelColor = TerracottaPrimary,
                        selectedLeadingIconColor = TerracottaPrimary,
                        containerColor = Color.White,
                        labelColor = Color(0xFF374151)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = filterHomeOnly,
                        borderColor = if (filterHomeOnly) TerracottaPrimary else Color(0xFFD1D5DB),
                        borderWidth = 1.2.dp
                    ),
                    modifier = Modifier.testTag("filter_home_chip")
                )

                FilterChip(
                    selected = filterMinRating >= 4.5,
                    onClick = {
                        viewModel.filterMinRating.value = if (filterMinRating >= 4.5) 0.0 else 4.8
                    },
                    label = { Text("★ 4.8+", fontSize = 12.5.sp, fontWeight = FontWeight.Bold) },
                    shape = RoundedCornerShape(10.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = com.example.ui.theme.AmberSoftBg,
                        selectedLabelColor = AmberGoldSecondary,
                        containerColor = Color.White,
                        labelColor = Color(0xFF374151)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = filterMinRating >= 4.5,
                        borderColor = if (filterMinRating >= 4.5) AmberGoldSecondary else Color(0xFFD1D5DB),
                        borderWidth = 1.2.dp
                    ),
                    modifier = Modifier.testTag("filter_rating_chip")
                )

                FilterChip(
                    selected = selectedCity == "Bobo-Dioulasso",
                    onClick = {
                        viewModel.selectedCity.value = if (selectedCity == "Bobo-Dioulasso") "Toutes" else "Bobo-Dioulasso"
                    },
                    label = { Text("Bobo", fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold) },
                    shape = RoundedCornerShape(10.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFF1F5F9),
                        selectedLabelColor = Color(0xFF334155),
                        containerColor = Color.White,
                        labelColor = Color(0xFF374151)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedCity == "Bobo-Dioulasso",
                        borderColor = if (selectedCity == "Bobo-Dioulasso") Color(0xFF64748B) else Color(0xFFD1D5DB),
                        borderWidth = 1.2.dp
                    ),
                    modifier = Modifier.testTag("filter_bobo_chip")
                )
            }
        }

        // Section Title: Prestataires disponibles
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Prestataires (${providers.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "Ouagadougou & Bobo",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4B5563)
                )
            }
        }

        // Providers list
        if (providers.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Aucun prestataire trouvé pour ces critères",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(providers) { provider ->
                ProviderCard(
                    provider = provider,
                    isFavorite = favoriteIds.contains(provider.id),
                    onToggleFavorite = { viewModel.toggleFavorite(provider.id) },
                    onClickProfile = { onNavigateToProviderProfile(provider.id) },
                    onBookClick = { onStartBooking(provider) },
                    formatFcfa = { viewModel.formatFcfa(it) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}
