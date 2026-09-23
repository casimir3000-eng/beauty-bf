package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BeautyViewModel
import com.example.ui.components.DemoDataNotice
import com.example.ui.theme.AmberGoldSecondary
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.TerracottaPrimaryDark
import com.example.ui.theme.TerracottaPrimaryLight
import com.example.ui.theme.WarmCreamSurface

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClientAuthScreen(
    viewModel: BeautyViewModel,
    onBackToPortals: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Connexion, 1: Inscription
    val errorMessage by viewModel.authErrorMessage.collectAsState()

    // Connexion state
    var loginPhone by remember { mutableStateOf("+226 71 22 33 44") }
    var loginPassword by remember { mutableStateOf("••••••••") }
    var loginPasswordVisible by remember { mutableStateOf(false) }

    // Inscription state
    var regFirstName by remember { mutableStateOf("") }
    var regLastName by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("+226 ") }
    var regCity by remember { mutableStateOf("Ouagadougou") }
    var regSector by remember { mutableStateOf("Secteur 22 (Tampouy)") }
    var regPassword by remember { mutableStateOf("") }
    var regPasswordVisible by remember { mutableStateOf(false) }
    val selectedBeautyInterests = remember {
        mutableStateListOf("Tresses & Nattes", "Coiffure & Brushing", "Maquillage")
    }

    val availableInterests = listOf(
        "Tresses & Nattes",
        "Coiffure & Brushing",
        "Maquillage",
        "Onglerie & Gel",
        "Soins du Visage",
        "Pédicure Spa",
        "Coloration & Mèches"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamSurface)
            .testTag("client_auth_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackToPortals,
                        modifier = Modifier.testTag("client_auth_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = TerracottaPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Retour aux espaces",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TerracottaPrimary
                    )
                }
            }

            item {
                DemoDataNotice()
            }

            // Header Banner
            item {
                Surface(
                    shape = CircleShape,
                    color = com.example.ui.theme.TerracottaSoftBg,
                    border = BorderStroke(1.5.dp, TerracottaPrimary),
                    modifier = Modifier.size(72.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = TerracottaPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Espace Cliente BEAUTY BF",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sublimez votre beauté, trouvez vos salons préférés et réservez en toute sérénité",
                    fontSize = 13.5.sp,
                    color = Color(0xFF4B5563),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Error display if any
            if (errorMessage != null) {
                item {
                    Surface(
                        color = Color(0xFFFEE2E2),
                        border = BorderStroke(1.5.dp, ErrorRed),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = Color(0xFF991B1B),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Tab Row
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, Color(0xFFE5E7EB)),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.White,
                        contentColor = TerracottaPrimary,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = TerracottaPrimary,
                                height = 3.dp
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = {
                                selectedTab = 0
                                viewModel.authErrorMessage.value = null
                            },
                            text = {
                                Text(
                                    "Se Connecter",
                                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    color = if (selectedTab == 0) TerracottaPrimary else Color(0xFF4B5563)
                                )
                            },
                            modifier = Modifier.testTag("tab_client_login")
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = {
                                selectedTab = 1
                                viewModel.authErrorMessage.value = null
                            },
                            text = {
                                Text(
                                    "Créer un compte",
                                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    color = if (selectedTab == 1) TerracottaPrimary else Color(0xFF4B5563)
                                )
                            },
                            modifier = Modifier.testTag("tab_client_register")
                        )
                    }
                }
            }

            // Form Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.5.dp, Color(0xFFE5E7EB)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (selectedTab == 0) {
                            // --- CONNEXION FORM ---
                            Text(
                                text = "Bon retour parmi nous !",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )

                            OutlinedTextField(
                                value = loginPhone,
                                onValueChange = { loginPhone = it },
                                label = { Text("Numéro de téléphone (+226)", fontWeight = FontWeight.Medium) },
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = TerracottaPrimary)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = TerracottaPrimary,
                                    unfocusedBorderColor = Color(0xFFD1D5DB),
                                    focusedTextColor = Color(0xFF111827),
                                    unfocusedTextColor = Color(0xFF111827)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_client_login_phone")
                            )

                            OutlinedTextField(
                                value = loginPassword,
                                onValueChange = { loginPassword = it },
                                label = { Text("Mot de passe", fontWeight = FontWeight.Medium) },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = TerracottaPrimary)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { loginPasswordVisible = !loginPasswordVisible }) {
                                        Icon(
                                            imageVector = if (loginPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = null,
                                            tint = Color(0xFF4B5563)
                                        )
                                    }
                                },
                                visualTransformation = if (loginPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = TerracottaPrimary,
                                    unfocusedBorderColor = Color(0xFFD1D5DB),
                                    focusedTextColor = Color(0xFF111827),
                                    unfocusedTextColor = Color(0xFF111827)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_client_login_password")
                            )

                            Button(
                                onClick = { viewModel.loginClient(loginPhone, loginPassword) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TerracottaPrimary,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(vertical = 13.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_submit_client_login")
                            ) {
                                Text(
                                    text = "Me Connecter",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                TextButton(onClick = { selectedTab = 1 }) {
                                    Text(
                                        text = "Pas encore de compte ? M'inscrire",
                                        fontSize = 13.5.sp,
                                        color = TerracottaPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Quick Demo Access Button
                            OutlinedButton(
                                onClick = { viewModel.quickLoginDemoClient() },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.5.dp, Color(0xFFD1D5DB)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF111827)),
                                contentPadding = PaddingValues(vertical = 11.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_demo_client_direct")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = AmberGoldSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Accès Démo Immédiat (Adjaratou)",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        } else {
                            // --- INSCRIPTION FORM ---
                            Text(
                                text = "Création de votre compte Cliente",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = regFirstName,
                                    onValueChange = { regFirstName = it },
                                    label = { Text("Prénom", fontWeight = FontWeight.Medium) },
                                    leadingIcon = {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = TerracottaPrimary)
                                    },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = TerracottaPrimary,
                                        unfocusedBorderColor = Color(0xFFD1D5DB),
                                        focusedTextColor = Color(0xFF111827),
                                        unfocusedTextColor = Color(0xFF111827)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_client_reg_firstname")
                                 )
                                OutlinedTextField(
                                    value = regLastName,
                                    onValueChange = { regLastName = it },
                                    label = { Text("Nom", fontWeight = FontWeight.Medium) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = TerracottaPrimary,
                                        unfocusedBorderColor = Color(0xFFD1D5DB),
                                        focusedTextColor = Color(0xFF111827),
                                        unfocusedTextColor = Color(0xFF111827)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_client_reg_lastname")
                                )
                            }

                            OutlinedTextField(
                                value = regPhone,
                                onValueChange = { regPhone = it },
                                label = { Text("Numéro WhatsApp / Téléphone", fontWeight = FontWeight.Medium) },
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = TerracottaPrimary)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = TerracottaPrimary,
                                    unfocusedBorderColor = Color(0xFFD1D5DB),
                                    focusedTextColor = Color(0xFF111827),
                                    unfocusedTextColor = Color(0xFF111827)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_client_reg_phone")
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = regCity,
                                    onValueChange = { regCity = it },
                                    label = { Text("Ville", fontWeight = FontWeight.Medium) },
                                    leadingIcon = {
                                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = TerracottaPrimary)
                                    },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = TerracottaPrimary,
                                        unfocusedBorderColor = Color(0xFFD1D5DB),
                                        focusedTextColor = Color(0xFF111827),
                                        unfocusedTextColor = Color(0xFF111827)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_client_reg_city")
                                )
                                OutlinedTextField(
                                    value = regSector,
                                    onValueChange = { regSector = it },
                                    label = { Text("Secteur / Quartier", fontWeight = FontWeight.Medium) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = TerracottaPrimary,
                                        unfocusedBorderColor = Color(0xFFD1D5DB),
                                        focusedTextColor = Color(0xFF111827),
                                        unfocusedTextColor = Color(0xFF111827)
                                    ),
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .testTag("input_client_reg_sector")
                                )
                            }

                            OutlinedTextField(
                                value = regPassword,
                                onValueChange = { regPassword = it },
                                label = { Text("Mot de passe", fontWeight = FontWeight.Medium) },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = TerracottaPrimary)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { regPasswordVisible = !regPasswordVisible }) {
                                        Icon(
                                            imageVector = if (regPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = null,
                                            tint = Color(0xFF4B5563)
                                        )
                                    }
                                },
                                visualTransformation = if (regPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = TerracottaPrimary,
                                    unfocusedBorderColor = Color(0xFFD1D5DB),
                                    focusedTextColor = Color(0xFF111827),
                                    unfocusedTextColor = Color(0xFF111827)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_client_reg_password")
                            )

                            // Prestations préférées
                            Text(
                                text = "Vos prestations de beauté préférées :",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                availableInterests.forEach { interest ->
                                    val isSelected = selectedBeautyInterests.contains(interest)
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            if (isSelected) {
                                                selectedBeautyInterests.remove(interest)
                                            } else {
                                                selectedBeautyInterests.add(interest)
                                            }
                                        },
                                        label = {
                                            Text(
                                                interest,
                                                fontSize = 12.5.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                        },
                                        leadingIcon = if (isSelected) {
                                            {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        } else null,
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFFFFE4EE),
                                            selectedLabelColor = TerracottaPrimary,
                                            selectedLeadingIconColor = TerracottaPrimary,
                                            containerColor = Color(0xFFF3F4F6),
                                            labelColor = Color(0xFF374151)
                                        ),
                                        border = FilterChipDefaults.filterChipBorder(
                                            enabled = true,
                                            selected = isSelected,
                                            borderColor = if (isSelected) TerracottaPrimary else Color(0xFFD1D5DB),
                                            borderWidth = 1.2.dp
                                        )
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    val validName = if (regFirstName.isBlank()) "Cliente" else regFirstName
                                    val validPhone = if (regPhone.length < 5) "+226 70 00 00 00" else regPhone
                                    viewModel.registerClient(
                                        firstName = validName,
                                        lastName = regLastName,
                                        phone = validPhone,
                                        city = regCity,
                                        sector = regSector,
                                        preferences = selectedBeautyInterests.toList()
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TerracottaPrimary,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(vertical = 13.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_submit_client_register")
                            ) {
                                Text(
                                    text = "Finaliser mon Inscription Cliente",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                TextButton(onClick = { selectedTab = 0 }) {
                                    Text(
                                        text = "Déjà un compte ? Se connecter",
                                        fontSize = 13.5.sp,
                                        color = TerracottaPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
