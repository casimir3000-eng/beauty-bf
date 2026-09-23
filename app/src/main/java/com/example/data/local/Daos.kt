package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProviderDao {
    @Query("SELECT * FROM providers ORDER BY rating DESC, reviewCount DESC")
    fun getAllProviders(): Flow<List<ProviderEntity>>

    @Query("SELECT * FROM providers WHERE id = :id")
    fun getProviderById(id: String): Flow<ProviderEntity?>

    @Query("SELECT * FROM providers WHERE id = :id")
    suspend fun getProviderByIdDirect(id: String): ProviderEntity?

    @Query("SELECT * FROM providers WHERE city LIKE '%' || :city || '%' ORDER BY rating DESC")
    fun getProvidersByCity(city: String): Flow<List<ProviderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProviders(providers: List<ProviderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvider(provider: ProviderEntity)

    @Update
    suspend fun updateProvider(provider: ProviderEntity)

    @Query("UPDATE providers SET isVerified = :isVerified WHERE id = :id")
    suspend fun setProviderVerified(id: String, isVerified: Boolean)
}

@Dao
interface ServiceDao {
    @Query("SELECT * FROM services ORDER BY priceFcfa ASC")
    fun getAllServices(): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services WHERE providerId = :providerId")
    fun getServicesByProvider(providerId: String): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services WHERE id = :id")
    suspend fun getServiceById(id: String): ServiceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceEntity)

    @Query("DELETE FROM services WHERE id = :id")
    suspend fun deleteService(id: String)
}

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments ORDER BY createdAt DESC")
    fun getAllAppointments(): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE clientUserId = :clientId ORDER BY createdAt DESC")
    fun getAppointmentsForClient(clientId: String): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE providerId = :providerId ORDER BY createdAt DESC")
    fun getAppointmentsForProvider(providerId: String): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE id = :id")
    fun getAppointmentById(id: String): Flow<AppointmentEntity?>

    @Query("SELECT * FROM appointments WHERE providerId = :providerId AND date = :date AND status NOT IN ('DECLINED', 'CANCELLED_BY_CLIENT', 'CANCELLED_BY_PROVIDER')")
    suspend fun getActiveBookingsForDate(providerId: String, date: String): List<AppointmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointments(appointments: List<AppointmentEntity>)

    @Query("UPDATE appointments SET status = :status WHERE id = :id")
    suspend fun updateAppointmentStatus(id: String, status: String)
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE providerId = :providerId ORDER BY createdAt DESC")
    fun getReviewsForProvider(providerId: String): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews ORDER BY createdAt DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesForConversation(conversationId: String): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessageEntity>)
}

@Dao
interface LocalityDao {
    @Query("SELECT * FROM localities ORDER BY name ASC")
    fun getAllLocalities(): Flow<List<LocalityEntity>>

    @Query("SELECT * FROM localities WHERE type = :type ORDER BY name ASC")
    fun getLocalitiesByType(type: String): Flow<List<LocalityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalities(localities: List<LocalityEntity>)
}

@Dao
interface FavoriteDao {
    @Query("SELECT providerId FROM favorites")
    fun getFavoriteProviderIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE providerId = :providerId)")
    fun isFavorite(providerId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE providerId = :providerId")
    suspend fun removeFavorite(providerId: String)
}

@Dao
interface PlatformSettingsDao {
    @Query("SELECT * FROM platform_settings WHERE id = 1")
    fun getSettings(): Flow<PlatformSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSettings(settings: PlatformSettingsEntity)
}
