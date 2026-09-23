package com.example.data.repository

import com.example.data.local.AppointmentEntity
import com.example.data.local.BeautyBfDatabase
import com.example.data.local.ChatMessageEntity
import com.example.data.local.FavoriteEntity
import com.example.data.local.LocalityEntity
import com.example.data.local.PlatformSettingsEntity
import com.example.data.local.ProviderEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.ServiceEntity
import com.example.data.model.AdministrativeLocality
import com.example.data.model.Appointment
import com.example.data.model.AppointmentStatus
import com.example.data.model.BeautyService
import com.example.data.model.ChatMessage
import com.example.data.model.LocalityType
import com.example.data.model.PlatformSettings
import com.example.data.model.Provider
import com.example.data.model.Review
import com.example.data.model.TravelFeeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class BeautyRepository(private val database: BeautyBfDatabase) {

    // Providers
    fun getAllProviders(): Flow<List<Provider>> {
        return database.providerDao().getAllProviders().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getProviderById(id: String): Flow<Provider?> {
        return database.providerDao().getProviderById(id).map { it?.toDomain() }
    }

    suspend fun updateProviderVerification(id: String, isVerified: Boolean) {
        database.providerDao().setProviderVerified(id, isVerified)
    }

    suspend fun saveProviderProfile(provider: Provider) {
        database.providerDao().insertProvider(provider.toEntity())
    }

    // Services
    fun getAllServices(): Flow<List<BeautyService>> {
        return database.serviceDao().getAllServices().map { list -> list.map { it.toDomain() } }
    }

    fun getServicesForProvider(providerId: String): Flow<List<BeautyService>> {
        return database.serviceDao().getServicesByProvider(providerId).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun addOrUpdateService(service: BeautyService) {
        database.serviceDao().insertService(service.toEntity())
    }

    suspend fun deleteService(id: String) {
        database.serviceDao().deleteService(id)
    }

    // Appointments & Availability Engine
    fun getAllAppointments(): Flow<List<Appointment>> {
        return database.appointmentDao().getAllAppointments().map { list -> list.map { it.toDomain() } }
    }

    fun getAppointmentsForClient(clientId: String): Flow<List<Appointment>> {
        return database.appointmentDao().getAppointmentsForClient(clientId).map { list ->
            list.map { it.toDomain() }
        }
    }

    fun getAppointmentsForProvider(providerId: String): Flow<List<Appointment>> {
        return database.appointmentDao().getAppointmentsForProvider(providerId).map { list ->
            list.map { it.toDomain() }
        }
    }

    /**
     * Requirement 16: Moteur de disponibilité
     * Returns true if the slot is free, false if a conflict exists.
     */
    suspend fun isSlotAvailable(providerId: String, date: String, timeSlot: String): Boolean {
        val existingBookings = database.appointmentDao().getActiveBookingsForDate(providerId, date)
        return existingBookings.none { it.timeSlot == timeSlot }
    }

    suspend fun getBookedSlotsForDate(providerId: String, date: String): List<String> {
        return database.appointmentDao().getActiveBookingsForDate(providerId, date).map { it.timeSlot }
    }

    suspend fun createAppointment(appointment: Appointment): Boolean {
        // Double check availability
        val isFree = isSlotAvailable(appointment.providerId, appointment.date, appointment.timeSlot)
        if (!isFree) return false

        database.appointmentDao().insertAppointment(appointment.toEntity())
        return true
    }

    suspend fun updateAppointmentStatus(appointmentId: String, newStatus: AppointmentStatus) {
        database.appointmentDao().updateAppointmentStatus(appointmentId, newStatus.name)
    }

    // Reviews
    fun getReviewsForProvider(providerId: String): Flow<List<Review>> {
        return database.reviewDao().getReviewsForProvider(providerId).map { list -> list.map { it.toDomain() } }
    }

    suspend fun addReview(review: Review) {
        database.reviewDao().insertReview(review.toEntity())
    }

    // Messaging
    fun getMessagesForConversation(conversationId: String): Flow<List<ChatMessage>> {
        return database.chatMessageDao().getMessagesForConversation(conversationId).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun sendMessage(
        conversationId: String,
        senderId: String,
        senderName: String,
        text: String,
        isFromClient: Boolean
    ) {
        val msg = ChatMessageEntity(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            senderId = senderId,
            senderName = senderName,
            text = text,
            timestamp = System.currentTimeMillis(),
            isRead = true,
            isFromClient = isFromClient
        )
        database.chatMessageDao().insertMessage(msg)
    }

    // Favorites
    fun getFavoriteProviderIds(): Flow<List<String>> = database.favoriteDao().getFavoriteProviderIds()

    suspend fun toggleFavorite(providerId: String, isCurrentlyFavorite: Boolean) {
        if (isCurrentlyFavorite) {
            database.favoriteDao().removeFavorite(providerId)
        } else {
            database.favoriteDao().addFavorite(FavoriteEntity(providerId))
        }
    }

    // Localities
    fun getAllLocalities(): Flow<List<AdministrativeLocality>> {
        return database.localityDao().getAllLocalities().map { list -> list.map { it.toDomain() } }
    }

    // Platform Settings
    fun getPlatformSettings(): Flow<PlatformSettings> {
        return database.platformSettingsDao().getSettings().map { entity ->
            entity?.toDomain() ?: PlatformSettings()
        }
    }

    suspend fun updatePlatformSettings(settings: PlatformSettings) {
        database.platformSettingsDao().updateSettings(settings.toEntity())
    }

    // Distance calculation helper (Haversine formula in KM)
    fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Radius of Earth in KM
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    // Travel fee calculation (Requirement 22 & 23)
    fun calculateTravelFee(provider: Provider, distanceKm: Double): Int {
        if (!provider.acceptsHomeService) return 0
        return when (provider.travelFeeType) {
            TravelFeeType.FIXED -> provider.baseTravelFeeFcfa
            TravelFeeType.PER_KM -> {
                val variableFee = (distanceKm * provider.perKmTravelFeeFcfa).toInt()
                provider.baseTravelFeeFcfa + variableFee
            }
            TravelFeeType.ZONED -> {
                when {
                    distanceKm <= 3.0 -> 1000
                    distanceKm <= 7.0 -> 1500
                    else -> 2500
                }
            }
        }
    }

    // Extensions for Entity to Domain conversions
    private fun ProviderEntity.toDomain() = Provider(
        id = id,
        userId = userId,
        salonName = salonName,
        profession = profession,
        specialties = specialtiesCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() },
        description = description,
        yearsExperience = yearsExperience,
        phone = phone,
        whatsapp = whatsapp,
        city = city,
        sector = sector,
        address = address,
        latitude = latitude,
        longitude = longitude,
        isVerified = isVerified,
        acceptsHomeService = acceptsHomeService,
        maxTravelRadiusKm = maxTravelRadiusKm,
        travelFeeType = try { TravelFeeType.valueOf(travelFeeType) } catch (e: Exception) { TravelFeeType.FIXED },
        baseTravelFeeFcfa = baseTravelFeeFcfa,
        perKmTravelFeeFcfa = perKmTravelFeeFcfa,
        rating = rating,
        reviewCount = reviewCount,
        isAvailableToday = isAvailableToday,
        openingHoursSummary = openingHoursSummary,
        photoUrl = photoUrl
    )

    private fun Provider.toEntity() = ProviderEntity(
        id = id,
        userId = userId,
        salonName = salonName,
        profession = profession,
        specialtiesCsv = specialties.joinToString(","),
        description = description,
        yearsExperience = yearsExperience,
        phone = phone,
        whatsapp = whatsapp,
        city = city,
        sector = sector,
        address = address,
        latitude = latitude,
        longitude = longitude,
        isVerified = isVerified,
        acceptsHomeService = acceptsHomeService,
        maxTravelRadiusKm = maxTravelRadiusKm,
        travelFeeType = travelFeeType.name,
        baseTravelFeeFcfa = baseTravelFeeFcfa,
        perKmTravelFeeFcfa = perKmTravelFeeFcfa,
        rating = rating,
        reviewCount = reviewCount,
        isAvailableToday = isAvailableToday,
        openingHoursSummary = openingHoursSummary,
        photoUrl = photoUrl
    )

    private fun ServiceEntity.toDomain() = BeautyService(
        id = id,
        providerId = providerId,
        name = name,
        category = category,
        description = description,
        priceFcfa = priceFcfa,
        durationMinutes = durationMinutes,
        acceptsSalon = acceptsSalon,
        acceptsHome = acceptsHome,
        homeSurchargeFcfa = homeSurchargeFcfa,
        isAvailable = isAvailable,
        photoUrl = photoUrl
    )

    private fun BeautyService.toEntity() = ServiceEntity(
        id = id,
        providerId = providerId,
        name = name,
        category = category,
        description = description,
        priceFcfa = priceFcfa,
        durationMinutes = durationMinutes,
        acceptsSalon = acceptsSalon,
        acceptsHome = acceptsHome,
        homeSurchargeFcfa = homeSurchargeFcfa,
        isAvailable = isAvailable,
        photoUrl = photoUrl
    )

    private fun AppointmentEntity.toDomain() = Appointment(
        id = id,
        clientUserId = clientUserId,
        clientName = clientName,
        clientPhone = clientPhone,
        providerId = providerId,
        providerName = providerName,
        serviceId = serviceId,
        serviceName = serviceName,
        serviceCategory = serviceCategory,
        date = date,
        timeSlot = timeSlot,
        durationMinutes = durationMinutes,
        isHomeService = isHomeService,
        clientAddress = clientAddress,
        clientSector = clientSector,
        accessInstructions = accessInstructions,
        servicePriceFcfa = servicePriceFcfa,
        travelFeeFcfa = travelFeeFcfa,
        totalFcfa = totalFcfa,
        status = try { AppointmentStatus.valueOf(status) } catch (e: Exception) { AppointmentStatus.PENDING },
        createdAt = createdAt
    )

    private fun Appointment.toEntity() = AppointmentEntity(
        id = id,
        clientUserId = clientUserId,
        clientName = clientName,
        clientPhone = clientPhone,
        providerId = providerId,
        providerName = providerName,
        serviceId = serviceId,
        serviceName = serviceName,
        serviceCategory = serviceCategory,
        date = date,
        timeSlot = timeSlot,
        durationMinutes = durationMinutes,
        isHomeService = isHomeService,
        clientAddress = clientAddress,
        clientSector = clientSector,
        accessInstructions = accessInstructions,
        servicePriceFcfa = servicePriceFcfa,
        travelFeeFcfa = travelFeeFcfa,
        totalFcfa = totalFcfa,
        status = status.name,
        createdAt = createdAt
    )

    private fun ReviewEntity.toDomain() = Review(
        id = id,
        appointmentId = appointmentId,
        providerId = providerId,
        clientUserId = clientUserId,
        clientName = clientName,
        rating = rating,
        comment = comment,
        qualityScore = qualityScore,
        punctualityScore = punctualityScore,
        welcomeScore = welcomeScore,
        valueScore = valueScore,
        createdAt = createdAt
    )

    private fun Review.toEntity() = ReviewEntity(
        id = id,
        appointmentId = appointmentId,
        providerId = providerId,
        clientUserId = clientUserId,
        clientName = clientName,
        rating = rating,
        comment = comment,
        qualityScore = qualityScore,
        punctualityScore = punctualityScore,
        welcomeScore = welcomeScore,
        valueScore = valueScore,
        createdAt = createdAt
    )

    private fun ChatMessageEntity.toDomain() = ChatMessage(
        id = id,
        conversationId = conversationId,
        senderId = senderId,
        senderName = senderName,
        text = text,
        timestamp = timestamp,
        isRead = isRead,
        isFromClient = isFromClient
    )

    private fun LocalityEntity.toDomain() = AdministrativeLocality(
        id = id,
        name = name,
        code = code,
        type = try { LocalityType.valueOf(type) } catch (e: Exception) { LocalityType.SECTOR },
        parentName = parentName,
        latitude = latitude,
        longitude = longitude
    )

    private fun PlatformSettingsEntity.toDomain() = PlatformSettings(
        commissionPercent = commissionPercent,
        orangeMoneyActive = orangeMoneyActive,
        moovMoneyActive = moovMoneyActive,
        cashPaymentActive = cashPaymentActive,
        appNoticeMessage = appNoticeMessage
    )

    private fun PlatformSettings.toEntity() = PlatformSettingsEntity(
        id = 1,
        commissionPercent = commissionPercent,
        orangeMoneyActive = orangeMoneyActive,
        moovMoneyActive = moovMoneyActive,
        cashPaymentActive = cashPaymentActive,
        appNoticeMessage = appNoticeMessage
    )
}
