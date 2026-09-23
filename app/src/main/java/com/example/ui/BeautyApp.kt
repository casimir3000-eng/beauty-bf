package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.BeautyService
import com.example.data.model.Provider
import com.example.data.model.UserRole
import com.example.ui.components.RoleSwitcherBar
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AppointmentsScreen
import com.example.ui.screens.BookingBottomSheet
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.ClientAuthScreen
import com.example.ui.screens.ClientHomeScreen
import com.example.ui.screens.ClientProfileScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.PortalSelectionScreen
import com.example.ui.screens.ProviderAuthScreen
import com.example.ui.screens.ProviderDashboardScreen
import com.example.ui.screens.ProviderProfileScreen
import com.example.ui.theme.AmberGoldDark
import com.example.ui.theme.AmberGoldSecondary
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.TerracottaPrimaryDark
import com.example.ui.theme.TerracottaPrimaryLight
import kotlinx.coroutines.launch

@Composable
fun BeautyApp(
    viewModel: BeautyViewModel = viewModel()
) {
    val authPortalState by viewModel.authPortalState.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val currentProvider by viewModel.currentProvider.collectAsState()
    val allProviders by viewModel.allProviders.collectAsState()
    val allServices by viewModel.allServices.collectAsState()

    var clientNavIndex by remember { mutableIntStateOf(0) }
    var viewingProviderId by remember { mutableStateOf<String?>(null) }
    var isBookingOpen by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val bookingSuccessMessage by viewModel.bookingSuccessMessage.collectAsState()

    LaunchedEffect(bookingSuccessMessage) {
        bookingSuccessMessage?.let { msg ->
            scope.launch {
                snackbarHostState.showSnackbar(msg)
                viewModel.bookingSuccessMessage.value = null
            }
        }
    }

    // Portal Gateway and Authentication Flows
    when (authPortalState) {
        AuthPortalState.GATEWAY_CHOICE -> {
            PortalSelectionScreen(
                viewModel = viewModel,
                onSelectClientPortal = { viewModel.openClientAuth() },
                onSelectProviderPortal = { viewModel.openProviderAuth() },
                onSelectAdmin = { viewModel.quickLoginAdmin() }
            )
            return
        }
        AuthPortalState.CLIENT_AUTH -> {
            ClientAuthScreen(
                viewModel = viewModel,
                onBackToPortals = { viewModel.openPortalSelection() }
            )
            return
        }
        AuthPortalState.PROVIDER_AUTH -> {
            ProviderAuthScreen(
                viewModel = viewModel,
                onBackToPortals = { viewModel.openPortalSelection() }
            )
            return
        }
        AuthPortalState.AUTHENTICATED -> {
            // Proceed to Authenticated Screen below
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 3.dp
            ) {
                when (currentRole) {
                    UserRole.CLIENT -> {
                        // Feminine Client Top Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFFFE3EC),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Spa,
                                            contentDescription = null,
                                            tint = TerracottaPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "BEAUTY",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 16.sp,
                                            color = TerracottaPrimaryDark,
                                            letterSpacing = 1.sp
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "BF",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = AmberGoldSecondary
                                        )
                                    }
                                    Text(
                                        text = "Espace Cliente • ${currentUser.firstName}",
                                        fontSize = 11.sp,
                                        color = TerracottaPrimary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            // Switch or Logout Button
                            OutlinedButton(
                                onClick = { viewModel.logout() },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFFF9CCD9)),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = TerracottaPrimary
                                ),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("btn_switch_space_client")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Espaces",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    UserRole.PROVIDER -> {
                        // Professional Provider Top Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFFFF6E5),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Storefront,
                                            contentDescription = null,
                                            tint = AmberGoldDark,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = currentProvider?.salonName ?: "Mon Espace Beauté",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = TerracottaPrimaryDark
                                    )
                                    Text(
                                        text = "Espace Pro • ${currentProvider?.profession ?: "Salon"}",
                                        fontSize = 11.sp,
                                        color = AmberGoldDark,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            OutlinedButton(
                                onClick = { viewModel.logout() },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFFDEBA67)),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = AmberGoldDark
                                ),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("btn_switch_space_provider")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Espaces",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    UserRole.ADMIN -> {
                        // Admin Top Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "BEAUTY BF • Supervision Administrateur",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = TerracottaPrimaryDark
                            )
                            OutlinedButton(
                                onClick = { viewModel.logout() },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                                modifier = Modifier.testTag("btn_switch_space_admin")
                            ) {
                                Text(
                                    text = "Déconnexion",
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (currentRole == UserRole.CLIENT && viewingProviderId == null) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("client_bottom_navigation")
                ) {
                    NavigationBarItem(
                        selected = clientNavIndex == 0,
                        onClick = { clientNavIndex = 0 },
                        icon = { Icon(Icons.Default.Explore, contentDescription = "Explorer") },
                        label = { Text("Explorer", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TerracottaPrimary,
                            selectedTextColor = TerracottaPrimary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_item_explore")
                    )

                    NavigationBarItem(
                        selected = clientNavIndex == 1,
                        onClick = { clientNavIndex = 1 },
                        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Rendez-vous") },
                        label = { Text("Rendez-vous", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TerracottaPrimary,
                            selectedTextColor = TerracottaPrimary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_item_appointments")
                    )

                    NavigationBarItem(
                        selected = clientNavIndex == 2,
                        onClick = { clientNavIndex = 2 },
                        icon = { Icon(Icons.Default.Chat, contentDescription = "Messages") },
                        label = { Text("Messages", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TerracottaPrimary,
                            selectedTextColor = TerracottaPrimary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_item_chat")
                    )

                    NavigationBarItem(
                        selected = clientNavIndex == 3,
                        onClick = { clientNavIndex = 3 },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoris") },
                        label = { Text("Favoris", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TerracottaPrimary,
                            selectedTextColor = TerracottaPrimary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_item_favorites")
                    )

                    NavigationBarItem(
                        selected = clientNavIndex == 4,
                        onClick = { clientNavIndex = 4 },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Mon Profil") },
                        label = { Text("Mon Profil", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TerracottaPrimary,
                            selectedTextColor = TerracottaPrimary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_item_profile")
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRole) {
                UserRole.CLIENT -> {
                    if (viewingProviderId != null) {
                        ProviderProfileScreen(
                            providerId = viewingProviderId!!,
                            viewModel = viewModel,
                            onBack = { viewingProviderId = null },
                            onStartBooking = { service, provider ->
                                viewModel.startBooking(service, provider)
                                isBookingOpen = true
                            },
                            onOpenChat = {
                                viewingProviderId = null
                                clientNavIndex = 2
                            }
                        )
                    } else {
                        when (clientNavIndex) {
                            0 -> ClientHomeScreen(
                                viewModel = viewModel,
                                onNavigateToProviderProfile = { id -> viewingProviderId = id },
                                onStartBooking = { provider ->
                                    val service = allServices.firstOrNull { it.providerId == provider.id }
                                        ?: allServices.firstOrNull()
                                    if (service != null) {
                                        viewModel.startBooking(service, provider)
                                        isBookingOpen = true
                                    }
                                }
                            )
                            1 -> AppointmentsScreen(viewModel = viewModel)
                            2 -> ChatScreen(viewModel = viewModel)
                            3 -> FavoritesScreen(
                                viewModel = viewModel,
                                onNavigateToProviderProfile = { id -> viewingProviderId = id },
                                onStartBooking = { provider ->
                                    val service = allServices.firstOrNull { it.providerId == provider.id }
                                        ?: allServices.firstOrNull()
                                    if (service != null) {
                                        viewModel.startBooking(service, provider)
                                        isBookingOpen = true
                                    }
                                }
                            )
                            4 -> ClientProfileScreen(
                                viewModel = viewModel,
                                onLogout = { viewModel.logout() }
                            )
                        }
                    }
                }

                UserRole.PROVIDER -> {
                    ProviderDashboardScreen(viewModel = viewModel)
                }

                UserRole.ADMIN -> {
                    AdminScreen(viewModel = viewModel)
                }
            }

            // Booking Modal Bottom Sheet
            if (isBookingOpen) {
                BookingBottomSheet(
                    viewModel = viewModel,
                    onDismiss = { isBookingOpen = false },
                    onBookingConfirmed = {
                        isBookingOpen = false
                        clientNavIndex = 1 // Switch to Appointments tab
                    }
                )
            }
        }
    }
}

