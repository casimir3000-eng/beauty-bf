package com.example.data.model

enum class UserRole {
    CLIENT,
    PROVIDER,
    ADMIN
}

data class User(
    val id: String,
    val role: UserRole,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String? = null,
    val city: String = "Ouagadougou",
    val sector: String = "Secteur 22",
    val avatarUrl: String = ""
)

enum class TravelFeeType {
    FIXED,
    PER_KM,
    ZONED
}

data class Provider(
    val id: String,
    val userId: String = "user-default",
    val salonName: String,
    val profession: String, // Coiffeuse, Barbier, Esthéticienne, etc.
    val specialties: List<String> = emptyList(),
    val description: String = "",
    val yearsExperience: Int = 3,
    val phone: String = "+226 70 00 00 00",
    val whatsapp: String = "+226 70 00 00 00",
    val city: String = "Ouagadougou",
    val sector: String = "Secteur 22",
    val address: String = "Ouagadougou",
    val latitude: Double = 12.3714,
    val longitude: Double = -1.5197,
    val isVerified: Boolean = false,
    val acceptsHomeService: Boolean = true,
    val maxTravelRadiusKm: Double = 12.0,
    val travelFeeType: TravelFeeType = TravelFeeType.FIXED,
    val baseTravelFeeFcfa: Int = 1500,
    val perKmTravelFeeFcfa: Int = 200,
    val rating: Double = 4.8,
    val reviewCount: Int = 24,
    val isAvailableToday: Boolean = true,
    val openingHoursSummary: String = "Lun - Sam : 08h30 - 19h30",
    val photoUrl: String = ""
)

data class BeautyService(
    val id: String,
    val providerId: String,
    val name: String,
    val category: String, // Coiffure, Tresses, Barbier, Onglerie, Maquillage, Soins
    val description: String,
    val priceFcfa: Int,
    val durationMinutes: Int,
    val acceptsSalon: Boolean = true,
    val acceptsHome: Boolean = true,
    val homeSurchargeFcfa: Int = 1000,
    val isAvailable: Boolean = true,
    val photoUrl: String = ""
)

enum class AppointmentStatus {
    PENDING,
    CONFIRMED,
    DECLINED,
    CANCELLED_BY_CLIENT,
    CANCELLED_BY_PROVIDER,
    IN_PROGRESS,
    COMPLETED,
    NO_SHOW
}

data class Appointment(
    val id: String,
    val clientUserId: String,
    val clientName: String,
    val clientPhone: String,
    val providerId: String,
    val providerName: String,
    val serviceId: String,
    val serviceName: String,
    val serviceCategory: String,
    val date: String, // YYYY-MM-DD
    val timeSlot: String, // e.g. "10:00"
    val durationMinutes: Int,
    val isHomeService: Boolean,
    val clientAddress: String,
    val clientSector: String,
    val accessInstructions: String = "",
    val servicePriceFcfa: Int,
    val travelFeeFcfa: Int,
    val totalFcfa: Int,
    val status: AppointmentStatus = AppointmentStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis()
)

data class Review(
    val id: String,
    val appointmentId: String,
    val providerId: String,
    val clientUserId: String,
    val clientName: String,
    val rating: Double, // 1 to 5
    val comment: String,
    val qualityScore: Int = 5,
    val punctualityScore: Int = 5,
    val welcomeScore: Int = 5,
    val valueScore: Int = 5,
    val createdAt: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = true,
    val isFromClient: Boolean = true
)

enum class LocalityType {
    REGION,
    PROVINCE,
    COMMUNE,
    SECTOR,
    VILLAGE
}

data class AdministrativeLocality(
    val id: String,
    val name: String,
    val code: String,
    val type: LocalityType,
    val parentName: String,
    val latitude: Double = 12.3714,
    val longitude: Double = -1.5197
)

data class PlatformSettings(
    val commissionPercent: Double = 10.0,
    val orangeMoneyActive: Boolean = true,
    val moovMoneyActive: Boolean = true,
    val cashPaymentActive: Boolean = true,
    val appNoticeMessage: String = "Bienvenue sur BEAUTY BF - La 1ère plateforme beauté du Burkina Faso"
)
