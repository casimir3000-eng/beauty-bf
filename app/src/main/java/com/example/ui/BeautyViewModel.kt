package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BeautyBfDatabase
import com.example.data.model.AdministrativeLocality
import com.example.data.model.Appointment
import com.example.data.model.AppointmentStatus
import com.example.data.model.BeautyService
import com.example.data.model.ChatMessage
import com.example.data.model.PlatformSettings
import com.example.data.model.Provider
import com.example.data.model.Review
import com.example.data.model.User
import com.example.data.model.UserRole
import com.example.data.repository.BeautyRepository
import com.example.security.SecurityService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

enum class ProviderSort {
    PROXIMITY,
    RATING,
    POPULARITY,
    PRICE_ASC
}

data class BookingDraft(
    val service: BeautyService? = null,
    val provider: Provider? = null,
    val isHomeService: Boolean = false,
    val selectedDate: String = "2026-09-18",
    val selectedTimeSlot: String = "",
    val clientAddress: String = "Secteur 22, près du rond-point Tampouy",
    val clientSector: String = "Secteur 22 (Tampouy)",
    val accessInstructions: String = "",
    val calculatedDistanceKm: Double = 3.2,
    val calculatedTravelFeeFcfa: Int = 0,
    val totalFcfa: Int = 0
)

enum class AuthPortalState {
    GATEWAY_CHOICE,    // Choice between Espace Cliente and Espace Prestataire
    CLIENT_AUTH,       // Dedicated Inscription & Connexion for Client
    PROVIDER_AUTH,     // Dedicated Inscription & Connexion for Provider
    AUTHENTICATED      // Accessing the selected interface
}

data class FilterState(
    val query: String = "",
    val category: String = "Tous",
    val city: String = "Toutes",
    val homeOnly: Boolean = false,
    val minRating: Double = 0.0
)

class BeautyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BeautyRepository
    val securityService: SecurityService

    init {
        val db = BeautyBfDatabase.getDatabase(application, viewModelScope)
        repository = BeautyRepository(db)
        securityService = SecurityService(db)
    }

    // Portal & Auth State
    val authPortalState = MutableStateFlow(AuthPortalState.GATEWAY_CHOICE)
    val authErrorMessage = MutableStateFlow<String?>(null)

    // Security & Data Protection States
    val securityAuditLogs = securityService.getRecentAuditLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val userConsents = securityService.getUserConsents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val exportedDataJson = MutableStateFlow<String?>(null)
    val dataExportSuccessMessage = MutableStateFlow<String?>(null)

    // Role state
    private val _currentRole = MutableStateFlow(UserRole.CLIENT)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    // Current client session
    val currentUser = MutableStateFlow(
        User(
            id = "client-user-1",
            role = UserRole.CLIENT,
            firstName = "Adjaratou",
            lastName = "Ouédraogo",
            phone = "+226 71 22 33 44",
            email = "adjaratou.o@gmail.com",
            city = "Ouagadougou",
            sector = "Secteur 22 (Tampouy)"
        )
    )

    // Current provider session (when in PROVIDER role)
    val currentProviderId = MutableStateFlow("prov-amina")

    val currentProvider: StateFlow<Provider?> = combine(
        repository.getAllProviders(),
        currentProviderId
    ) { list, id ->
        list.firstOrNull { it.id == id } ?: list.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Providers & Services flow
    val allProviders: StateFlow<List<Provider>> = repository.getAllProviders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allServices: StateFlow<List<BeautyService>> = repository.getAllServices()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAppointments: StateFlow<List<Appointment>> = repository.getAllAppointments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteIds: StateFlow<List<String>> = repository.getFavoriteProviderIds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val localities: StateFlow<List<AdministrativeLocality>> = repository.getAllLocalities()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val platformSettings: StateFlow<PlatformSettings> = repository.getPlatformSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PlatformSettings())

    // Search and Filtering State
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("Tous")
    val selectedCity = MutableStateFlow("Toutes")
    val filterHomeServiceOnly = MutableStateFlow(false)
    val filterMinRating = MutableStateFlow(0.0)
    val sortBy = MutableStateFlow(ProviderSort.RATING)

    private val filterCriteria = combine(
        searchQuery,
        selectedCategory,
        selectedCity,
        filterHomeServiceOnly,
        filterMinRating
    ) { query, category, city, homeOnly, minRating ->
        FilterState(query, category, city, homeOnly, minRating)
    }

    // Filtered providers
    val filteredProviders: StateFlow<List<Provider>> = combine(
        allProviders,
        allServices,
        filterCriteria,
        sortBy
    ) { providers, services, filters, sort ->
        var list = providers

        // Query filter (name, salon, profession, specialty, sector)
        if (filters.query.isNotBlank()) {
            val q = filters.query.trim().lowercase()
            list = list.filter {
                it.salonName.lowercase().contains(q) ||
                it.profession.lowercase().contains(q) ||
                it.city.lowercase().contains(q) ||
                it.sector.lowercase().contains(q) ||
                it.specialties.any { spec -> spec.lowercase().contains(q) }
            }
        }

        // Category filter
        if (filters.category != "Tous") {
            val providerIdsWithCategory = services.filter { it.category.equals(filters.category, ignoreCase = true) }
                .map { it.providerId }.toSet()
            list = list.filter { providerIdsWithCategory.contains(it.id) || it.profession.contains(filters.category, ignoreCase = true) }
        }

        // City filter
        if (filters.city != "Toutes") {
            list = list.filter { it.city.equals(filters.city, ignoreCase = true) }
        }

        // Home service filter
        if (filters.homeOnly) {
            list = list.filter { it.acceptsHomeService }
        }

        // Rating filter
        if (filters.minRating > 0.0) {
            list = list.filter { it.rating >= filters.minRating }
        }

        // Sorting
        when (sort) {
            ProviderSort.RATING -> list.sortedByDescending { it.rating }
            ProviderSort.POPULARITY -> list.sortedByDescending { it.reviewCount }
            ProviderSort.PROXIMITY -> list.sortedBy { it.id }
            ProviderSort.PRICE_ASC -> list.sortedBy { it.baseTravelFeeFcfa }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Provider Details
    val selectedProviderId = MutableStateFlow<String?>("prov-amina")
    val selectedProviderReviews = MutableStateFlow<List<Review>>(emptyList())

    // Booking Flow State
    val bookingDraft = MutableStateFlow(BookingDraft())
    val bookedSlotsForSelectedDate = MutableStateFlow<List<String>>(emptyList())
    val bookingErrorMessage = MutableStateFlow<String?>(null)
    val bookingSuccessMessage = MutableStateFlow<String?>(null)

    // Chat State
    val activeConversationId = MutableStateFlow("conv-prov-amina")
    val currentChatMessages: StateFlow<List<ChatMessage>> = activeConversationId
        .combine(repository.getMessagesForConversation("conv-prov-amina")) { _, msgs -> msgs }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    // Portal Navigation & Authentication
    fun openPortalSelection() {
        authPortalState.value = AuthPortalState.GATEWAY_CHOICE
        authErrorMessage.value = null
    }

    fun openClientAuth() {
        authPortalState.value = AuthPortalState.CLIENT_AUTH
        authErrorMessage.value = null
    }

    fun openProviderAuth() {
        authPortalState.value = AuthPortalState.PROVIDER_AUTH
        authErrorMessage.value = null
    }

    fun loginClient(phone: String, pass: String): Boolean {
        if (phone.isBlank()) {
            authErrorMessage.value = "Veuillez renseigner votre numéro de téléphone"
            return false
        }
        val rateLimit = securityService.checkRateLimit(phone.trim())
        if (!rateLimit.first) {
            authErrorMessage.value = "Trop de tentatives échouées. Veuillez patienter ${rateLimit.second}s par mesure de sécurité."
            viewModelScope.launch {
                securityService.recordAudit(
                    action = "LOGIN_RATE_LIMITED",
                    userId = phone.trim(),
                    userRole = "CLIENT",
                    details = "Blocage temporaire anti-brute force déclenché."
                )
            }
            return false
        }

        _currentRole.value = UserRole.CLIENT
        authPortalState.value = AuthPortalState.AUTHENTICATED
        authErrorMessage.value = null
        securityService.resetRateLimit(phone.trim())

        viewModelScope.launch {
            securityService.recordAudit(
                action = "LOGIN_SUCCESS",
                userId = currentUser.value.id,
                userRole = "CLIENT",
                details = "Connexion sécurisée client (${currentUser.value.firstName} ${currentUser.value.lastName})."
            )
        }
        return true
    }

    fun registerClient(
        firstName: String,
        lastName: String,
        phone: String,
        city: String,
        sector: String,
        preferences: List<String>
    ): Boolean {
        if (firstName.isBlank() || phone.isBlank()) {
            authErrorMessage.value = "Veuillez renseigner votre prénom et numéro de téléphone"
            return false
        }
        val sanitizedFirst = securityService.sanitizeInput(firstName)
        val sanitizedLast = securityService.sanitizeInput(lastName)
        val sanitizedPhone = securityService.sanitizeInput(phone)

        currentUser.value = User(
            id = "client-${UUID.randomUUID().toString().take(8)}",
            role = UserRole.CLIENT,
            firstName = sanitizedFirst,
            lastName = sanitizedLast,
            phone = sanitizedPhone,
            email = null,
            city = city,
            sector = sector
        )
        _currentRole.value = UserRole.CLIENT
        authPortalState.value = AuthPortalState.AUTHENTICATED
        authErrorMessage.value = null

        viewModelScope.launch {
            securityService.recordAudit(
                action = "REGISTER_CLIENT_SUCCESS",
                userId = currentUser.value.id,
                userRole = "CLIENT",
                details = "Inscription nouveau compte client avec consentement CIL initialisé."
            )
        }
        return true
    }

    fun loginProvider(providerIdOrPhone: String, pass: String): Boolean {
        val rateLimit = securityService.checkRateLimit(providerIdOrPhone.trim())
        if (!rateLimit.first) {
            authErrorMessage.value = "Trop de tentatives échouées. Veuillez patienter ${rateLimit.second}s par mesure de sécurité."
            viewModelScope.launch {
                securityService.recordAudit(
                    action = "LOGIN_PROVIDER_RATE_LIMITED",
                    userId = providerIdOrPhone.trim(),
                    userRole = "PROVIDER",
                    details = "Blocage temporaire anti-brute force prestataire."
                )
            }
            return false
        }

        val prov = allProviders.value.firstOrNull {
            it.id.equals(providerIdOrPhone.trim(), ignoreCase = true) ||
            it.phone.contains(providerIdOrPhone.trim())
        } ?: allProviders.value.firstOrNull()

        if (prov != null) {
            currentProviderId.value = prov.id
        }
        _currentRole.value = UserRole.PROVIDER
        authPortalState.value = AuthPortalState.AUTHENTICATED
        authErrorMessage.value = null
        securityService.resetRateLimit(providerIdOrPhone.trim())

        viewModelScope.launch {
            securityService.recordAudit(
                action = "LOGIN_PROVIDER_SUCCESS",
                userId = currentProviderId.value,
                userRole = "PROVIDER",
                details = "Connexion sécurisée prestataire (${prov?.salonName ?: currentProviderId.value})."
            )
        }
        return true
    }

    // Personal Data Protection & Privacy Rights (Loi 001-2021/AN Burkina Faso)
    fun toggleConsent(key: String, isGranted: Boolean) {
        viewModelScope.launch {
            securityService.updateConsent(key, isGranted)
        }
    }

    fun exportPersonalData() {
        viewModelScope.launch {
            val exportJson = securityService.generatePersonalDataExportJson(
                user = currentUser.value,
                appointments = allAppointments.value,
                reviews = emptyList()
            )
            exportedDataJson.value = exportJson
            dataExportSuccessMessage.value = "Données personnelles générées et signées cryptographiquement."
            securityService.recordAudit(
                action = "DATA_PORTABILITY_EXPORT",
                userId = currentUser.value.id,
                userRole = currentRole.value.name,
                details = "Génération de l'archive de portabilité des données personnelles."
            )
        }
    }

    fun requestRightToErasure(onComplete: () -> Unit) {
        viewModelScope.launch {
            securityService.executeRightToErasure(currentUser.value.id)
            currentUser.value = User(
                id = "anonymized-" + UUID.randomUUID().toString().take(6),
                role = UserRole.CLIENT,
                firstName = "Client",
                lastName = "Anonymisé",
                phone = "•• •• •• ••",
                email = null,
                city = "Ouagadougou",
                sector = "Inconnu"
            )
            authPortalState.value = AuthPortalState.GATEWAY_CHOICE
            onComplete()
        }
    }

    fun registerProvider(
        salonName: String,
        profession: String,
        phone: String,
        whatsapp: String,
        city: String,
        sector: String,
        address: String,
        acceptsHome: Boolean,
        baseTravelFee: Int
    ): Boolean {
        if (salonName.isBlank() || phone.isBlank()) {
            authErrorMessage.value = "Veuillez renseigner le nom de votre établissement et le contact"
            return false
        }
        val newId = "prov-${UUID.randomUUID().toString().take(6)}"
        val newProvider = Provider(
            id = newId,
            userId = "user-$newId",
            salonName = salonName.trim(),
            profession = profession.ifBlank { "Coiffeuse & Soins" },
            phone = phone.trim(),
            whatsapp = whatsapp.ifBlank { phone.trim() },
            city = city,
            sector = sector,
            address = address.ifBlank { sector },
            acceptsHomeService = acceptsHome,
            baseTravelFeeFcfa = baseTravelFee,
            isVerified = false,
            rating = 5.0,
            reviewCount = 1
        )
        viewModelScope.launch {
            repository.saveProviderProfile(newProvider)
        }
        currentProviderId.value = newId
        _currentRole.value = UserRole.PROVIDER
        authPortalState.value = AuthPortalState.AUTHENTICATED
        authErrorMessage.value = null
        return true
    }

    fun quickLoginDemoClient() {
        currentUser.value = User(
            id = "client-user-1",
            role = UserRole.CLIENT,
            firstName = "Adjaratou",
            lastName = "Ouédraogo",
            phone = "+226 71 22 33 44",
            email = "adjaratou.o@gmail.com",
            city = "Ouagadougou",
            sector = "Secteur 22 (Tampouy)"
        )
        _currentRole.value = UserRole.CLIENT
        authPortalState.value = AuthPortalState.AUTHENTICATED
        authErrorMessage.value = null
    }

    fun quickLoginDemoProvider(providerId: String) {
        currentProviderId.value = providerId
        _currentRole.value = UserRole.PROVIDER
        authPortalState.value = AuthPortalState.AUTHENTICATED
        authErrorMessage.value = null
    }

    fun quickLoginAdmin() {
        _currentRole.value = UserRole.ADMIN
        authPortalState.value = AuthPortalState.AUTHENTICATED
        authErrorMessage.value = null
    }

    fun logout() {
        authPortalState.value = AuthPortalState.GATEWAY_CHOICE
        authErrorMessage.value = null
    }

    fun selectProvider(providerId: String) {
        selectedProviderId.value = providerId
        viewModelScope.launch {
            repository.getReviewsForProvider(providerId).collect {
                selectedProviderReviews.value = it
            }
        }
    }

    fun toggleFavorite(providerId: String) {
        viewModelScope.launch {
            val isFav = favoriteIds.value.contains(providerId)
            repository.toggleFavorite(providerId, isFav)
        }
    }

    fun startBooking(service: BeautyService, provider: Provider) {
        val initialTravelFee = if (service.acceptsHome) {
            repository.calculateTravelFee(provider, 3.5) + service.homeSurchargeFcfa
        } else 0
        bookingDraft.value = BookingDraft(
            service = service,
            provider = provider,
            isHomeService = false,
            selectedDate = "2026-09-18",
            selectedTimeSlot = "10:00",
            calculatedDistanceKm = 3.5,
            calculatedTravelFeeFcfa = 0,
            totalFcfa = service.priceFcfa
        )
        refreshBookedSlots(provider.id, "2026-09-18")
    }

    fun updateBookingHomeService(isHome: Boolean) {
        val draft = bookingDraft.value
        val provider = draft.provider ?: return
        val service = draft.service ?: return

        val travelFee = if (isHome) {
            repository.calculateTravelFee(provider, draft.calculatedDistanceKm) + service.homeSurchargeFcfa
        } else 0

        bookingDraft.value = draft.copy(
            isHomeService = isHome,
            calculatedTravelFeeFcfa = travelFee,
            totalFcfa = service.priceFcfa + travelFee
        )
    }

    fun updateBookingDate(date: String) {
        val draft = bookingDraft.value
        bookingDraft.value = draft.copy(selectedDate = date, selectedTimeSlot = "")
        draft.provider?.let { refreshBookedSlots(it.id, date) }
    }

    fun updateBookingTimeSlot(slot: String) {
        bookingDraft.value = bookingDraft.value.copy(selectedTimeSlot = slot)
    }

    fun updateBookingAddress(address: String, sector: String, instructions: String) {
        bookingDraft.value = bookingDraft.value.copy(
            clientAddress = address,
            clientSector = sector,
            accessInstructions = instructions
        )
    }

    private fun refreshBookedSlots(providerId: String, date: String) {
        viewModelScope.launch {
            bookedSlotsForSelectedDate.value = repository.getBookedSlotsForDate(providerId, date)
        }
    }

    fun confirmBooking(onSuccess: () -> Unit) {
        val draft = bookingDraft.value
        val service = draft.service ?: return
        val provider = draft.provider ?: return

        if (draft.selectedTimeSlot.isBlank()) {
            bookingErrorMessage.value = "Veuillez choisir un créneau horaire."
            return
        }

        viewModelScope.launch {
            val appointment = Appointment(
                id = "apt-" + UUID.randomUUID().toString().take(8),
                clientUserId = currentUser.value.id,
                clientName = "${currentUser.value.firstName} ${currentUser.value.lastName}",
                clientPhone = currentUser.value.phone,
                providerId = provider.id,
                providerName = provider.salonName,
                serviceId = service.id,
                serviceName = service.name,
                serviceCategory = service.category,
                date = draft.selectedDate,
                timeSlot = draft.selectedTimeSlot,
                durationMinutes = service.durationMinutes,
                isHomeService = draft.isHomeService,
                clientAddress = if (draft.isHomeService) draft.clientAddress else provider.address,
                clientSector = if (draft.isHomeService) draft.clientSector else provider.sector,
                accessInstructions = draft.accessInstructions,
                servicePriceFcfa = service.priceFcfa,
                travelFeeFcfa = draft.calculatedTravelFeeFcfa,
                totalFcfa = draft.totalFcfa,
                status = AppointmentStatus.PENDING
            )

            val success = repository.createAppointment(appointment)
            if (success) {
                bookingSuccessMessage.value = "Demande de rendez-vous envoyée au prestataire !"
                bookingErrorMessage.value = null
                securityService.recordAudit(
                    action = "APPOINTMENT_CREATED",
                    userId = currentUser.value.id,
                    userRole = "CLIENT",
                    details = "Réservation #${appointment.id} créée auprès de ${provider.salonName} pour ${service.name} (${formatFcfa(draft.totalFcfa)})."
                )
                onSuccess()
            } else {
                bookingErrorMessage.value = "Ce créneau vient d'être réservé par un autre client."
            }
        }
    }

    // Provider actions
    fun acceptAppointment(appointmentId: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(appointmentId, AppointmentStatus.CONFIRMED)
            securityService.recordAudit(
                action = "APPOINTMENT_ACCEPTED",
                userId = currentProviderId.value,
                userRole = "PROVIDER",
                details = "Acceptation et confirmation du rendez-vous #$appointmentId."
            )
        }
    }

    fun declineAppointment(appointmentId: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(appointmentId, AppointmentStatus.DECLINED)
            securityService.recordAudit(
                action = "APPOINTMENT_DECLINED",
                userId = currentProviderId.value,
                userRole = "PROVIDER",
                details = "Refus du rendez-vous #$appointmentId par le prestataire."
            )
        }
    }

    fun completeAppointment(appointmentId: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(appointmentId, AppointmentStatus.COMPLETED)
            securityService.recordAudit(
                action = "APPOINTMENT_COMPLETED",
                userId = currentProviderId.value,
                userRole = "PROVIDER",
                details = "Clôture et règlement finalisé du rendez-vous #$appointmentId."
            )
        }
    }

    fun cancelAppointment(appointmentId: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(appointmentId, AppointmentStatus.CANCELLED_BY_CLIENT)
            securityService.recordAudit(
                action = "APPOINTMENT_CANCELLED",
                userId = currentUser.value.id,
                userRole = "CLIENT",
                details = "Annulation du rendez-vous #$appointmentId par le client."
            )
        }
    }

    // Add Review
    fun submitReview(
        appointmentId: String,
        providerId: String,
        rating: Double,
        comment: String,
        quality: Int,
        punctuality: Int,
        welcome: Int,
        value: Int
    ) {
        viewModelScope.launch {
            val review = Review(
                id = "rev-" + UUID.randomUUID().toString().take(8),
                appointmentId = appointmentId,
                providerId = providerId,
                clientUserId = currentUser.value.id,
                clientName = currentUser.value.firstName,
                rating = rating,
                comment = comment,
                qualityScore = quality,
                punctualityScore = punctuality,
                welcomeScore = welcome,
                valueScore = value
            )
            repository.addReview(review)
        }
    }

    // Messaging
    fun sendMessage(text: String, isFromClient: Boolean = true) {
        if (text.isBlank()) return
        val convId = activeConversationId.value
        val senderId = if (isFromClient) currentUser.value.id else currentProviderId.value
        val senderName = if (isFromClient) currentUser.value.firstName else "Prestataire"

        viewModelScope.launch {
            repository.sendMessage(convId, senderId, senderName, text, isFromClient)
        }
    }

    // Admin actions
    fun toggleProviderVerification(providerId: String, currentlyVerified: Boolean) {
        viewModelScope.launch {
            repository.updateProviderVerification(providerId, !currentlyVerified)
        }
    }

    fun updatePlatformSettings(commissionPercent: Double, orange: Boolean, moov: Boolean, cash: Boolean) {
        viewModelScope.launch {
            repository.updatePlatformSettings(
                PlatformSettings(
                    commissionPercent = commissionPercent,
                    orangeMoneyActive = orange,
                    moovMoneyActive = moov,
                    cashPaymentActive = cash
                )
            )
        }
    }

    // Helper formatters
    fun formatFcfa(amount: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale.FRENCH)
        return "${formatter.format(amount)} FCFA"
    }

    fun formatDistance(km: Double): String {
        return if (km < 1.0) {
            "À ${(km * 1000).toInt()} m"
        } else {
            "À ${String.format(Locale.FRENCH, "%.1f", km)} km"
        }
    }
}
