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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.ui.theme.AmberGoldDark
import com.example.ui.theme.AmberGoldSecondary
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.TerracottaPrimaryDark
import com.example.ui.theme.WarmCreamSurface

@Composable
fun ProviderAuthScreen(
    viewModel: BeautyViewModel,
    onBackToPortals: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Connexion Pro, 1: Inscription Pro
    val errorMessage by viewModel.authErrorMessage.collectAsState()

    // Connexion Pro state
    var proPhone by remember { mutableStateOf("+226 70 12 34 56") }
    var proPassword by remember { mutableStateOf("••••••••") }
    var proPasswordVisible by remember { mutableStateOf(false) }

    // Inscription Pro state
    var salonName by remember { mutableStateOf("") }
    var managerName by remember { mutableStateOf("") }
    var profession by remember { mutableStateOf("Coiffeuse & Tresses Afro") }
    var phone by remember { mutableStateOf("+226 ") }
    var whatsapp by remember { mutableStateOf("+226 ") }
    var city by remember { mutableStateOf("Ouagadougou") }
    var sector by remember { mutableStateOf("Secteur 22 (Tampouy)") }
    var address by remember { mutableStateOf("") }
    var acceptsHomeService by remember { mutableStateOf(true) }
    var travelFeeFcfa by remember { mutableStateOf("1500") }
    var regPassword by remember { mutableStateOf("") }
    var regPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamSurface)
            .testTag("provider_auth_screen")
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
                        modifier = Modifier.testTag("provider_auth_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = AmberGoldDark
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Retour aux espaces",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberGoldDark
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
                    color = com.example.ui.theme.AmberSoftBg,
                    border = BorderStroke(1.5.dp, AmberGoldSecondary),
                    modifier = Modifier.size(72.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = null,
                            tint = AmberGoldSecondary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Espace Professionnel BEAUTY BF",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Développez votre clientèle, organisez votre agenda pro et gérez vos tarifs en FCFA",
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
                        contentColor = AmberGoldDark,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = AmberGoldDark,
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
                                    "Se Connecter Pro",
                                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    color = if (selectedTab == 0) AmberGoldDark else Color(0xFF4B5563)
                                )
                            },
                            modifier = Modifier.testTag("tab_provider_login")
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = {
                                selectedTab = 1
                                viewModel.authErrorMessage.value = null
                            },
                            text = {
                                Text(
                                    "Inscrire mon salon",
                                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    color = if (selectedTab == 1) AmberGoldDark else Color(0xFF4B5563)
                                )
                            },
                            modifier = Modifier.testTag("tab_provider_register")
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
                            // --- CONNEXION PRO FORM ---
                            Text(
                                text = "Accédez à votre tableau de bord Pro",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )

                            OutlinedTextField(
                                value = proPhone,
                                onValueChange = { proPhone = it },
                                label = { Text("Numéro Pro ou Identifiant Salon", fontWeight = FontWeight.Medium) },
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = AmberGoldDark)
                                },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AmberGoldDark,
                                    unfocusedBorderColor = Color(0xFFD1D5DB),
                                    focusedTextColor = Color(0xFF111827),
                                    unfocusedTextColor = Color(0xFF111827)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_provider_login_phone")
                            )

                            OutlinedTextField(
                                value = proPassword,
                                onValueChange = { proPassword = it },
                                label = { Text("Mot de passe Pro", fontWeight = FontWeight.Medium) },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = AmberGoldDark)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { proPasswordVisible = !proPasswordVisible }) {
                                        Icon(
                                            imageVector = if (proPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = null,
                                            tint = Color(0xFF4B5563)
                                        )
                                    }
                                },
                                visualTransformation = if (proPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AmberGoldDark,
                                    unfocusedBorderColor = Color(0xFFD1D5DB),
                                    focusedTextColor = Color(0xFF111827),
                                    unfocusedTextColor = Color(0xFF111827)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_provider_login_password")
                            )

                            Button(
                                onClick = { viewModel.loginProvider(proPhone, proPassword) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AmberGoldDark,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(vertical = 13.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_submit_provider_login")
                            ) {
                                Text(
                                    text = "Accéder à mon Espace Pro",
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
                                        text = "Vous n'êtes pas encore inscrite ? Inscrire mon salon",
                                        fontSize = 13.5.sp,
                                        color = AmberGoldDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Quick Demo Access Selector
                            Text(
                                text = "Comptes de démonstration immédiats :",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = { viewModel.quickLoginDemoProvider("prov-amina") },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.5.dp, Color(0xFFD1D5DB)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF111827)),
                                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("btn_demo_prov_amina")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = AmberGoldDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Amina Coiffure Glamour (Tampouy, Ouaga)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                OutlinedButton(
                                    onClick = { viewModel.quickLoginDemoProvider("prov-fatim") },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.5.dp, Color(0xFFD1D5DB)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF111827)),
                                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("btn_demo_prov_fatim")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = AmberGoldDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Fatim Onglerie & Spa (Secteur 5, Bobo)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                OutlinedButton(
                                    onClick = { viewModel.quickLoginDemoProvider("prov-zara") },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.5.dp, Color(0xFFD1D5DB)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF111827)),
                                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("btn_demo_prov_zara")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = AmberGoldDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Zara Make-up Pro (Ouaga 2000)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        } else {
                            // --- INSCRIPTION PRO FORM ---
                            Text(
                                text = "Enregistrement de votre Salon / Atelier",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )

                            OutlinedTextField(
                                value = salonName,
                                onValueChange = { salonName = it },
                                label = { Text("Nom du Salon ou Atelier", fontWeight = FontWeight.Medium) },
                                leadingIcon = {
                                    Icon(Icons.Default.Business, contentDescription = null, tint = AmberGoldDark)
                                },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AmberGoldDark,
                                    unfocusedBorderColor = Color(0xFFD1D5DB),
                                    focusedTextColor = Color(0xFF111827),
                                    unfocusedTextColor = Color(0xFF111827)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_prov_reg_salon_name")
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = managerName,
                                    onValueChange = { managerName = it },
                                    label = { Text("Nom Gérante", fontWeight = FontWeight.Medium) },
                                    leadingIcon = {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = AmberGoldDark)
                                    },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = AmberGoldDark,
                                        unfocusedBorderColor = Color(0xFFD1D5DB),
                                        focusedTextColor = Color(0xFF111827),
                                        unfocusedTextColor = Color(0xFF111827)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_prov_reg_manager")
                                )

                                OutlinedTextField(
                                    value = profession,
                                    onValueChange = { profession = it },
                                    label = { Text("Activité / Métier", fontWeight = FontWeight.Medium) },
                                    leadingIcon = {
                                        Icon(Icons.Default.ContentCut, contentDescription = null, tint = AmberGoldDark)
                                    },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = AmberGoldDark,
                                        unfocusedBorderColor = Color(0xFFD1D5DB),
                                        focusedTextColor = Color(0xFF111827),
                                        unfocusedTextColor = Color(0xFF111827)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_prov_reg_profession")
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = phone,
                                    onValueChange = { phone = it },
                                    label = { Text("Téléphone (+226)", fontWeight = FontWeight.Medium) },
                                    leadingIcon = {
                                        Icon(Icons.Default.Phone, contentDescription = null, tint = AmberGoldDark)
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = AmberGoldDark,
                                        unfocusedBorderColor = Color(0xFFD1D5DB),
                                        focusedTextColor = Color(0xFF111827),
                                        unfocusedTextColor = Color(0xFF111827)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_prov_reg_phone")
                                )

                                OutlinedTextField(
                                    value = whatsapp,
                                    onValueChange = { whatsapp = it },
                                    label = { Text("WhatsApp Pro", fontWeight = FontWeight.Medium) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = AmberGoldDark,
                                        unfocusedBorderColor = Color(0xFFD1D5DB),
                                        focusedTextColor = Color(0xFF111827),
                                        unfocusedTextColor = Color(0xFF111827)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_prov_reg_whatsapp")
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = city,
                                    onValueChange = { city = it },
                                    label = { Text("Ville", fontWeight = FontWeight.Medium) },
                                    leadingIcon = {
                                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = AmberGoldDark)
                                    },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = AmberGoldDark,
                                        unfocusedBorderColor = Color(0xFFD1D5DB),
                                        focusedTextColor = Color(0xFF111827),
                                        unfocusedTextColor = Color(0xFF111827)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_prov_reg_city")
                                )

                                OutlinedTextField(
                                    value = sector,
                                    onValueChange = { sector = it },
                                    label = { Text("Secteur INSD", fontWeight = FontWeight.Medium) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = AmberGoldDark,
                                        unfocusedBorderColor = Color(0xFFD1D5DB),
                                        focusedTextColor = Color(0xFF111827),
                                        unfocusedTextColor = Color(0xFF111827)
                                    ),
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .testTag("input_prov_reg_sector")
                                )
                            }

                            OutlinedTextField(
                                value = address,
                                onValueChange = { address = it },
                                label = { Text("Adresse physique ou repère (ex: Face Pharmacie)", fontWeight = FontWeight.Medium) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AmberGoldDark,
                                    unfocusedBorderColor = Color(0xFFD1D5DB),
                                    focusedTextColor = Color(0xFF111827),
                                    unfocusedTextColor = Color(0xFF111827)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_prov_reg_address")
                            )

                            // Prestation à domicile
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Home, contentDescription = null, tint = AmberGoldDark)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text("Prestations à domicile", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF111827))
                                        Text("Pouvez-vous vous déplacer chez la cliente ?", fontSize = 12.sp, color = Color(0xFF4B5563))
                                    }
                                }
                                Switch(
                                    checked = acceptsHomeService,
                                    onCheckedChange = { acceptsHomeService = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = AmberGoldDark
                                    ),
                                    modifier = Modifier.testTag("switch_prov_home_service")
                                )
                            }

                            if (acceptsHomeService) {
                                OutlinedTextField(
                                    value = travelFeeFcfa,
                                    onValueChange = { travelFeeFcfa = it },
                                    label = { Text("Frais de déplacement de base (FCFA)", fontWeight = FontWeight.Medium) },
                                    leadingIcon = {
                                        Icon(Icons.Default.Money, contentDescription = null, tint = AmberGoldDark)
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = AmberGoldDark,
                                        unfocusedBorderColor = Color(0xFFD1D5DB),
                                        focusedTextColor = Color(0xFF111827),
                                        unfocusedTextColor = Color(0xFF111827)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("input_prov_travel_fee")
                                )
                            }

                            OutlinedTextField(
                                value = regPassword,
                                onValueChange = { regPassword = it },
                                label = { Text("Mot de passe Pro", fontWeight = FontWeight.Medium) },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = AmberGoldDark)
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
                                    focusedBorderColor = AmberGoldDark,
                                    unfocusedBorderColor = Color(0xFFD1D5DB),
                                    focusedTextColor = Color(0xFF111827),
                                    unfocusedTextColor = Color(0xFF111827)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_prov_reg_password")
                            )

                            Button(
                                onClick = {
                                    val validSalon = if (salonName.isBlank()) "Mon Salon Beauté" else salonName
                                    val validPhone = if (phone.length < 5) "+226 70 00 00 00" else phone
                                    val fee = travelFeeFcfa.toIntOrNull() ?: 1500
                                    viewModel.registerProvider(
                                        salonName = validSalon,
                                        profession = profession,
                                        phone = validPhone,
                                        whatsapp = if (whatsapp.length > 5) whatsapp else validPhone,
                                        city = city,
                                        sector = sector,
                                        address = if (address.isBlank()) sector else address,
                                        acceptsHome = acceptsHomeService,
                                        baseTravelFee = fee
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AmberGoldDark,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(vertical = 13.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_submit_provider_register")
                            ) {
                                Text(
                                    text = "Valider et Ouvrir mon Salon Pro",
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
                                        text = "Déjà un compte professionnel ? Se connecter",
                                        fontSize = 13.5.sp,
                                        color = AmberGoldDark,
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
