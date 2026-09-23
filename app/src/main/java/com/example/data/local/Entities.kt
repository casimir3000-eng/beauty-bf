package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.AppointmentStatus
import com.example.data.model.LocalityType
import com.example.data.model.TravelFeeType

@Entity(tableName = "providers")
data class ProviderEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val salonName: String,
    val profession: String,
    val specialtiesCsv: String,
    val description: String,
    val yearsExperience: Int,
    val phone: String,
    val whatsapp: String,
    val city: String,
    val sector: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val isVerified: Boolean,
    val acceptsHomeService: Boolean,
    val maxTravelRadiusKm: Double,
    val travelFeeType: String,
    val baseTravelFeeFcfa: Int,
    val perKmTravelFeeFcfa: Int,
    val rating: Double,
    val reviewCount: Int,
    val isAvailableToday: Boolean,
    val openingHoursSummary: String,
    val photoUrl: String
)

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey val id: String,
    val providerId: String,
    val name: String,
    val category: String,
    val description: String,
    val priceFcfa: Int,
    val durationMinutes: Int,
    val acceptsSalon: Boolean,
    val acceptsHome: Boolean,
    val homeSurchargeFcfa: Int,
    val isAvailable: Boolean,
    val photoUrl: String
)

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey val id: String,
    val clientUserId: String,
    val clientName: String,
    val clientPhone: String,
    val providerId: String,
    val providerName: String,
    val serviceId: String,
    val serviceName: String,
    val serviceCategory: String,
    val date: String,
    val timeSlot: String,
    val durationMinutes: Int,
    val isHomeService: Boolean,
    val clientAddress: String,
    val clientSector: String,
    val accessInstructions: String,
    val servicePriceFcfa: Int,
    val travelFeeFcfa: Int,
    val totalFcfa: Int,
    val status: String,
    val createdAt: Long
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: String,
    val appointmentId: String,
    val providerId: String,
    val clientUserId: String,
    val clientName: String,
    val rating: Double,
    val comment: String,
    val qualityScore: Int,
    val punctualityScore: Int,
    val welcomeScore: Int,
    val valueScore: Int,
    val createdAt: Long
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestamp: Long,
    val isRead: Boolean,
    val isFromClient: Boolean
)

@Entity(tableName = "localities")
data class LocalityEntity(
    @PrimaryKey val id: String,
    val name: String,
    val code: String,
    val type: String,
    val parentName: String,
    val latitude: Double,
    val longitude: Double
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val providerId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "platform_settings")
data class PlatformSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val commissionPercent: Double,
    val orangeMoneyActive: Boolean,
    val moovMoneyActive: Boolean,
    val cashPaymentActive: Boolean,
    val appNoticeMessage: String
)
