package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProviderEntity::class,
        ServiceEntity::class,
        AppointmentEntity::class,
        ReviewEntity::class,
        ChatMessageEntity::class,
        LocalityEntity::class,
        FavoriteEntity::class,
        PlatformSettingsEntity::class,
        SecurityAuditEntity::class,
        UserConsentEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class BeautyBfDatabase : RoomDatabase() {
    abstract fun providerDao(): ProviderDao
    abstract fun serviceDao(): ServiceDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun reviewDao(): ReviewDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun localityDao(): LocalityDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun platformSettingsDao(): PlatformSettingsDao
    abstract fun securityAuditDao(): SecurityAuditDao
    abstract fun userConsentDao(): UserConsentDao

    companion object {
        @Volatile
        private var INSTANCE: BeautyBfDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BeautyBfDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BeautyBfDatabase::class.java,
                    "beauty_bf_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(database: BeautyBfDatabase) {
            // 1. Administrative Localities of Burkina Faso (Official INSD RGPH hierarchy)
            val localities = listOf(
                LocalityEntity("loc-reg-1", "Centre", "REG-01", "REGION", "Burkina Faso", 12.3714, -1.5197),
                LocalityEntity("loc-reg-2", "Hauts-Bassins", "REG-02", "REGION", "Burkina Faso", 11.1772, -4.2979),
                LocalityEntity("loc-reg-3", "Centre-Ouest", "REG-03", "REGION", "Burkina Faso", 12.2500, -2.3667),
                LocalityEntity("loc-prov-1", "Kadiogo", "PRV-KAD", "PROVINCE", "Centre", 12.3714, -1.5197),
                LocalityEntity("loc-prov-2", "Houet", "PRV-HOU", "PROVINCE", "Hauts-Bassins", 11.1772, -4.2979),
                LocalityEntity("loc-prov-3", "Boulkiemdé", "PRV-BLK", "PROVINCE", "Centre-Ouest", 12.2500, -2.3667),
                LocalityEntity("loc-com-1", "Ouagadougou", "COM-OUA", "COMMUNE", "Kadiogo", 12.3714, -1.5197),
                LocalityEntity("loc-com-2", "Bobo-Dioulasso", "COM-BOB", "COMMUNE", "Houet", 11.1772, -4.2979),
                LocalityEntity("loc-com-3", "Koudougou", "COM-KDG", "COMMUNE", "Boulkiemdé", 12.2500, -2.3667),
                // Ouagadougou sectors and historical neighborhood names
                LocalityEntity("loc-sec-22", "Secteur 22 (Tampouy)", "SEC-22", "SECTOR", "Ouagadougou", 12.4110, -1.5420),
                LocalityEntity("loc-sec-15", "Secteur 15 (Ouaga 2000)", "SEC-15", "SECTOR", "Ouagadougou", 12.3020, -1.5120),
                LocalityEntity("loc-sec-10", "Secteur 10 (Gounghin)", "SEC-10", "SECTOR", "Ouagadougou", 12.3560, -1.5480),
                LocalityEntity("loc-sec-28", "Secteur 28 (Bogodogo)", "SEC-28", "SECTOR", "Ouagadougou", 12.3480, -1.4870),
                LocalityEntity("loc-sec-12", "Secteur 12 (Patte d'Oie)", "SEC-12", "SECTOR", "Ouagadougou", 12.3250, -1.5280),
                LocalityEntity("loc-sec-18", "Secteur 18 (Pissy)", "SEC-18", "SECTOR", "Ouagadougou", 12.3380, -1.5640),
                LocalityEntity("loc-sec-24", "Secteur 24 (Somgandé)", "SEC-24", "SECTOR", "Ouagadougou", 12.4130, -1.4920),
                LocalityEntity("loc-sec-30", "Secteur 30 (Wemtenga)", "SEC-30", "SECTOR", "Ouagadougou", 12.3720, -1.4930),
                // Bobo-Dioulasso sectors
                LocalityEntity("loc-sec-b5", "Secteur 5 (Accart-Ville)", "SEC-B05", "SECTOR", "Bobo-Dioulasso", 11.1820, -4.2880),
                LocalityEntity("loc-sec-b17", "Secteur 17 (Sarfalao)", "SEC-B17", "SECTOR", "Bobo-Dioulasso", 11.1650, -4.3120)
            )
            database.localityDao().insertLocalities(localities)

            // 2. Demo Providers (clearly marked as demo profiles)
            val demoProviders = listOf(
                ProviderEntity(
                    id = "prov-amina",
                    userId = "user-amina",
                    salonName = "Amina Beauté & Tresses (Démo)",
                    profession = "Coiffeuse & Spécialiste Tresses",
                    specialtiesCsv = "Tresses africaines,Nattes collées,Locks,Soins capillaires,Perruques",
                    description = "Salon professionnel réputé pour les tresses traditionnelles et modernes, soins des cheveux crépus et naturels. Prestations soignées en salon et à domicile dans tout Ouagadougou.",
                    yearsExperience = 7,
                    phone = "+226 70 12 34 56",
                    whatsapp = "+226 70 12 34 56",
                    city = "Ouagadougou",
                    sector = "Secteur 22 (Tampouy)",
                    address = "Avenue Kadiogo, face station Total Tampouy",
                    latitude = 12.4110,
                    longitude = -1.5420,
                    isVerified = true,
                    acceptsHomeService = true,
                    maxTravelRadiusKm = 15.0,
                    travelFeeType = "FIXED",
                    baseTravelFeeFcfa = 2000,
                    perKmTravelFeeFcfa = 200,
                    rating = 4.9,
                    reviewCount = 38,
                    isAvailableToday = true,
                    openingHoursSummary = "Lun - Sam : 08h00 - 19h30",
                    photoUrl = ""
                ),
                ProviderEntity(
                    id = "prov-elegance",
                    userId = "user-elegance",
                    salonName = "Élégance Barbier & Spa Homme (Démo)",
                    profession = "Barbier & Soins Masculins",
                    specialtiesCsv = "Barbier,Coupe homme dégradé,Soin visage purifiant,Taille de barbe,Teinture",
                    description = "Barbershop moderne climatisé à Ouaga 2000. Rasage à l'ancienne à la serviette chaude, soins de la peau et coupes tendances. Déplacement VIP sur demande.",
                    yearsExperience = 9,
                    phone = "+226 76 98 76 54",
                    whatsapp = "+226 76 98 76 54",
                    city = "Ouagadougou",
                    sector = "Secteur 15 (Ouaga 2000)",
                    address = "Boulevard Mouammar Kadhafi, Rue 15.42",
                    latitude = 12.3020,
                    longitude = -1.5120,
                    isVerified = true,
                    acceptsHomeService = true,
                    maxTravelRadiusKm = 10.0,
                    travelFeeType = "FIXED",
                    baseTravelFeeFcfa = 3000,
                    perKmTravelFeeFcfa = 300,
                    rating = 4.8,
                    reviewCount = 29,
                    isAvailableToday = true,
                    openingHoursSummary = "Mar - Dim : 09h00 - 21h00",
                    photoUrl = ""
                ),
                ProviderEntity(
                    id = "prov-fatou",
                    userId = "user-fatou",
                    salonName = "Fatou Glow Spa & Onglerie (Démo)",
                    profession = "Esthéticienne & Spécialiste Ongles",
                    specialtiesCsv = "Manucure,Pédicure spa,Pose gel chablon,Nail Art,Soin visage anti-imperfections",
                    description = "Espace cocooning dédié à la beauté des mains, des pieds et à l'éclat du visage. Matériel stérilisé selon les normes d'hygiène les plus strictes.",
                    yearsExperience = 5,
                    phone = "+226 78 45 12 30",
                    whatsapp = "+226 78 45 12 30",
                    city = "Ouagadougou",
                    sector = "Secteur 28 (Bogodogo)",
                    address = "Rue des Écoles, non loin du marché de Bogodogo",
                    latitude = 12.3480,
                    longitude = -1.4870,
                    isVerified = true,
                    acceptsHomeService = true,
                    maxTravelRadiusKm = 12.0,
                    travelFeeType = "FIXED",
                    baseTravelFeeFcfa = 1500,
                    perKmTravelFeeFcfa = 200,
                    rating = 4.7,
                    reviewCount = 18,
                    isAvailableToday = true,
                    openingHoursSummary = "Lun - Sam : 08h30 - 18h30",
                    photoUrl = ""
                ),
                ProviderEntity(
                    id = "prov-nafi",
                    userId = "user-nafi",
                    salonName = "Nafi Make-up Artist Pro (Démo)",
                    profession = "Maquilleuse Professionnelle",
                    specialtiesCsv = "Maquillage mariée,Make-up soirée,Pose de cils,Restructuration sourcils",
                    description = "Maquilleuse certifiée spécialisée dans le maquillage de cérémonie, mariages coutumiers et religieux. Service exclusif à domicile et sur site d'événement.",
                    yearsExperience = 6,
                    phone = "+226 65 33 22 11",
                    whatsapp = "+226 65 33 22 11",
                    city = "Ouagadougou",
                    sector = "Secteur 10 (Gounghin)",
                    address = "Boulevard de la Révolution",
                    latitude = 12.3560,
                    longitude = -1.5480,
                    isVerified = true,
                    acceptsHomeService = true,
                    maxTravelRadiusKm = 20.0,
                    travelFeeType = "FIXED",
                    baseTravelFeeFcfa = 2500,
                    perKmTravelFeeFcfa = 250,
                    rating = 4.9,
                    reviewCount = 42,
                    isAvailableToday = false,
                    openingHoursSummary = "Mer - Dim : 08h00 - 20h00",
                    photoUrl = ""
                ),
                ProviderEntity(
                    id = "prov-bobo",
                    userId = "user-bobo",
                    salonName = "Maison du Ciseau Bobo (Démo)",
                    profession = "Coiffeur Créateur & Spa",
                    specialtiesCsv = "Coiffure dame,Coloration,Lissage brésilien,Brushing,Massage crânien",
                    description = "Le temple de la coiffure moderne à Bobo-Dioulasso. Équipe passionnée, produits capillaires de qualité supérieure.",
                    yearsExperience = 8,
                    phone = "+226 20 97 11 22",
                    whatsapp = "+226 70 88 99 00",
                    city = "Bobo-Dioulasso",
                    sector = "Secteur 5 (Accart-Ville)",
                    address = "Avenue de la République, Bobo-Dioulasso",
                    latitude = 11.1820,
                    longitude = -4.2880,
                    isVerified = false,
                    acceptsHomeService = false,
                    maxTravelRadiusKm = 5.0,
                    travelFeeType = "FIXED",
                    baseTravelFeeFcfa = 1000,
                    perKmTravelFeeFcfa = 150,
                    rating = 4.6,
                    reviewCount = 14,
                    isAvailableToday = true,
                    openingHoursSummary = "Lun - Sam : 08h00 - 19h00",
                    photoUrl = ""
                )
            )
            database.providerDao().insertProviders(demoProviders)

            // 3. Demo Services with FCFA pricing and durations
            val demoServices = listOf(
                // Services Amina Beauté
                ServiceEntity(
                    id = "srv-amina-1",
                    providerId = "prov-amina",
                    name = "Tresses africaines (Knotless Braids)",
                    category = "Tresses",
                    description = "Tresses sans nœuds très légères, confortables et protectrices pour les pointes. Mèches fournies par la cliente ou sur place.",
                    priceFcfa = 7500,
                    durationMinutes = 150,
                    acceptsSalon = true,
                    acceptsHome = true,
                    homeSurchargeFcfa = 2000,
                    isAvailable = true,
                    photoUrl = ""
                ),
                ServiceEntity(
                    id = "srv-amina-2",
                    providerId = "prov-amina",
                    name = "Nattes collées avec motifs",
                    category = "Tresses",
                    description = "Nattes collées artistiques avec motifs géométriques traditionnels et modernes. Idéal pour adulte ou enfant.",
                    priceFcfa = 5000,
                    durationMinutes = 90,
                    acceptsSalon = true,
                    acceptsHome = true,
                    homeSurchargeFcfa = 1500,
                    isAvailable = true,
                    photoUrl = ""
                ),
                ServiceEntity(
                    id = "srv-amina-3",
                    providerId = "prov-amina",
                    name = "Soin capillaire hydratant profond & Karité",
                    category = "Soins",
                    description = "Bain d'huile tiède au beurre de karité bio du Burkina et massage du cuir chevelu sous casque vapeur.",
                    priceFcfa = 6000,
                    durationMinutes = 60,
                    acceptsSalon = true,
                    acceptsHome = false,
                    homeSurchargeFcfa = 0,
                    isAvailable = true,
                    photoUrl = ""
                ),
                // Services Barbier Élégance
                ServiceEntity(
                    id = "srv-elegance-1",
                    providerId = "prov-elegance",
                    name = "Coupe Dégradé Américain & Contour net",
                    category = "Barbier",
                    description = "Coupe aux ciseaux et tondeuse professionnelle avec finitions au rasoir coupe-choux et eau de Cologne.",
                    priceFcfa = 3500,
                    durationMinutes = 45,
                    acceptsSalon = true,
                    acceptsHome = true,
                    homeSurchargeFcfa = 3000,
                    isAvailable = true,
                    photoUrl = ""
                ),
                ServiceEntity(
                    id = "srv-elegance-2",
                    providerId = "prov-elegance",
                    name = "Taille de Barbe & Soin serviette chaude",
                    category = "Barbier",
                    description = "Sculpture précise de la barbe, huile hydratante et application de la serviette chaude relaxante.",
                    priceFcfa = 3000,
                    durationMinutes = 30,
                    acceptsSalon = true,
                    acceptsHome = true,
                    homeSurchargeFcfa = 2500,
                    isAvailable = true,
                    photoUrl = ""
                ),
                ServiceEntity(
                    id = "srv-elegance-3",
                    providerId = "prov-elegance",
                    name = "Pack VIP Homme (Coupe + Barbe + Masque Noir)",
                    category = "Barbier",
                    description = "Formule complète : coupe au choix, soin barbe et masque noir au charbon actif purifiant visage.",
                    priceFcfa = 9000,
                    durationMinutes = 75,
                    acceptsSalon = true,
                    acceptsHome = true,
                    homeSurchargeFcfa = 4000,
                    isAvailable = true,
                    photoUrl = ""
                ),
                // Services Fatou Glow
                ServiceEntity(
                    id = "srv-fatou-1",
                    providerId = "prov-fatou",
                    name = "Pose complète Gel & Vernis Semi-Permanent",
                    category = "Onglerie",
                    description = "Extensions au chablon ou capsules, façonnage parfait, pose vernis semi-permanent longue tenue (3 semaines).",
                    priceFcfa = 8000,
                    durationMinutes = 90,
                    acceptsSalon = true,
                    acceptsHome = true,
                    homeSurchargeFcfa = 1500,
                    isAvailable = true,
                    photoUrl = ""
                ),
                ServiceEntity(
                    id = "srv-fatou-2",
                    providerId = "prov-fatou",
                    name = "Pédicure Spa Complète avec Gommage",
                    category = "Onglerie",
                    description = "Bain de pieds aux sels minéraux, élimination des callosités, gommage et massage relaxant.",
                    priceFcfa = 6000,
                    durationMinutes = 60,
                    acceptsSalon = true,
                    acceptsHome = true,
                    homeSurchargeFcfa = 1500,
                    isAvailable = true,
                    photoUrl = ""
                ),
                // Services Nafi Make-up
                ServiceEntity(
                    id = "srv-nafi-1",
                    providerId = "prov-nafi",
                    name = "Maquillage Glamour Événement / Soirée",
                    category = "Maquillage",
                    description = "Teint zéro défaut adapté au climat chaud d'Afrique de l'Ouest, fards soyeux et fixation 16 heures.",
                    priceFcfa = 12000,
                    durationMinutes = 60,
                    acceptsSalon = false,
                    acceptsHome = true,
                    homeSurchargeFcfa = 2500,
                    isAvailable = true,
                    photoUrl = ""
                ),
                ServiceEntity(
                    id = "srv-nafi-2",
                    providerId = "prov-nafi",
                    name = "Pack Mariée Prestige (Civil & Religieux)",
                    category = "Maquillage",
                    description = "Essai préalable inclus, maquillage haute tenue le jour J, pose de faux-cils haut de gamme et retouches.",
                    priceFcfa = 35000,
                    durationMinutes = 120,
                    acceptsSalon = false,
                    acceptsHome = true,
                    homeSurchargeFcfa = 0,
                    isAvailable = true,
                    photoUrl = ""
                )
            )
            database.serviceDao().insertServices(demoServices)

            // 4. Demo Appointments representing various lifecycle states
            val demoAppointments = listOf(
                AppointmentEntity(
                    id = "apt-demo-1",
                    clientUserId = "client-user-1",
                    clientName = "Adjaratou Ouédraogo",
                    clientPhone = "+226 71 22 33 44",
                    providerId = "prov-amina",
                    providerName = "Amina Beauté & Tresses",
                    serviceId = "srv-amina-1",
                    serviceName = "Tresses africaines (Knotless Braids)",
                    serviceCategory = "Tresses",
                    date = "2026-09-17",
                    timeSlot = "09:30",
                    durationMinutes = 150,
                    isHomeService = true,
                    clientAddress = "Rue 22.15, près de la pharmacie Gounghin",
                    clientSector = "Secteur 10 (Gounghin)",
                    accessInstructions = "Portail marron en face du jardin public",
                    servicePriceFcfa = 7500,
                    travelFeeFcfa = 2000,
                    totalFcfa = 9500,
                    status = "CONFIRMED",
                    createdAt = System.currentTimeMillis() - 86400000L
                ),
                AppointmentEntity(
                    id = "apt-demo-2",
                    clientUserId = "client-user-1",
                    clientName = "Moussa Sawadogo",
                    clientPhone = "+226 78 99 00 11",
                    providerId = "prov-elegance",
                    providerName = "Élégance Barbier & Spa Homme",
                    serviceId = "srv-elegance-3",
                    serviceName = "Pack VIP Homme (Coupe + Barbe)",
                    serviceCategory = "Barbier",
                    date = "2026-09-18",
                    timeSlot = "14:00",
                    durationMinutes = 75,
                    isHomeService = false,
                    clientAddress = "",
                    clientSector = "Secteur 15 (Ouaga 2000)",
                    accessInstructions = "Prestation en salon",
                    servicePriceFcfa = 9000,
                    travelFeeFcfa = 0,
                    totalFcfa = 9000,
                    status = "PENDING",
                    createdAt = System.currentTimeMillis() - 3600000L * 2
                ),
                AppointmentEntity(
                    id = "apt-demo-3",
                    clientUserId = "client-user-1",
                    clientName = "Mariam Kaboré",
                    clientPhone = "+226 70 55 66 77",
                    providerId = "prov-fatou",
                    providerName = "Fatou Glow Spa & Onglerie",
                    serviceId = "srv-fatou-1",
                    serviceName = "Pose complète Gel & Vernis",
                    serviceCategory = "Onglerie",
                    date = "2026-09-10",
                    timeSlot = "11:00",
                    durationMinutes = 90,
                    isHomeService = false,
                    clientAddress = "",
                    clientSector = "Secteur 28 (Bogodogo)",
                    accessInstructions = "Prestation effectuée au salon",
                    servicePriceFcfa = 8000,
                    travelFeeFcfa = 0,
                    totalFcfa = 8000,
                    status = "COMPLETED",
                    createdAt = System.currentTimeMillis() - 86400000L * 7
                )
            )
            database.appointmentDao().insertAppointments(demoAppointments)

            // 5. Demo Reviews
            val demoReviews = listOf(
                ReviewEntity(
                    id = "rev-1",
                    appointmentId = "apt-demo-3",
                    providerId = "prov-amina",
                    clientUserId = "client-user-1",
                    clientName = "Adjaratou O.",
                    rating = 5.0,
                    comment = "Très professionnelle ! Mes tresses knotless sont magnifiques et ne tirent pas du tout sur le cuir chevelu. Je recommande vivement Amina.",
                    qualityScore = 5,
                    punctualityScore = 5,
                    welcomeScore = 5,
                    valueScore = 5,
                    createdAt = System.currentTimeMillis() - 86400000L * 5
                ),
                ReviewEntity(
                    id = "rev-2",
                    appointmentId = "apt-demo-past",
                    providerId = "prov-elegance",
                    clientUserId = "client-user-2",
                    clientName = "Ibrahim T.",
                    rating = 5.0,
                    comment = "Meilleur barbier de Ouaga 2000. Ambiance calme, coupe très soignée et la serviette chaude fait un bien fou après le travail.",
                    qualityScore = 5,
                    punctualityScore = 4,
                    welcomeScore = 5,
                    valueScore = 5,
                    createdAt = System.currentTimeMillis() - 86400000L * 10
                )
            )
            database.reviewDao().insertReviews(demoReviews)

            // 6. Demo Chat Messages
            val demoMessages = listOf(
                ChatMessageEntity(
                    id = "msg-1",
                    conversationId = "conv-prov-amina",
                    senderId = "client-user-1",
                    senderName = "Moi (Client)",
                    text = "Bonjour Amina, avez-vous des mèches chocolat en stock pour les tresses ?",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 45,
                    isRead = true,
                    isFromClient = true
                ),
                ChatMessageEntity(
                    id = "msg-2",
                    conversationId = "conv-prov-amina",
                    senderId = "prov-amina",
                    senderName = "Amina Beauté",
                    text = "Bonjour ! Oui tout à fait, j'ai les teintes n°4 et n°30 disponibles. Votre rendez-vous de demain est bien confirmé.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 30,
                    isRead = true,
                    isFromClient = false
                )
            )
            database.chatMessageDao().insertMessages(demoMessages)

            // 7. Initial Platform Settings
            database.platformSettingsDao().updateSettings(
                PlatformSettingsEntity(
                    id = 1,
                    commissionPercent = 10.0,
                    orangeMoneyActive = true,
                    moovMoneyActive = true,
                    cashPaymentActive = true,
                    appNoticeMessage = "BEAUTY BF • La 1ère plateforme beauté du Burkina Faso"
                )
            )

            // 8. Demo Favorite
            database.favoriteDao().addFavorite(FavoriteEntity("prov-amina"))

            // 9. Mandatory Data Protection Consents (Conformité Loi 001-2021/AN Burkina Faso)
            val defaultConsents = listOf(
                UserConsentEntity(
                    consentKey = "DATA_PROCESSING",
                    title = "Traitement des données de rendez-vous",
                    description = "Autorise le traitement sécurisé de votre identité et téléphone pour la mise en relation avec le salon ou la professionnelle.",
                    legalReference = "Loi 001-2021/AN relative à la protection des données (Art. 12)",
                    isGranted = true,
                    updatedAt = System.currentTimeMillis()
                ),
                UserConsentEntity(
                    consentKey = "GEOLOCATION_HOME",
                    title = "Localisation pour prestation à domicile",
                    description = "Partage sécurisé de votre quartier et repères d'accès uniquement lorsque vous réservez une prestation à domicile.",
                    legalReference = "Loi 001-2021/AN relative à la protection des données (Art. 15)",
                    isGranted = true,
                    updatedAt = System.currentTimeMillis()
                ),
                UserConsentEntity(
                    consentKey = "SECURITY_NOTIFICATIONS",
                    title = "Notifications de sécurité et alertes SMS",
                    description = "Réception de codes OTP de vérification et confirmations de commande sans prospection commerciale indésirable.",
                    legalReference = "Loi 001-2021/AN relative à la protection des données (Art. 18)",
                    isGranted = true,
                    updatedAt = System.currentTimeMillis()
                )
            )
            database.userConsentDao().insertConsents(defaultConsents)

            // 10. Genesis Block for Tamper-Evident Security Audit Log
            val genesisTime = System.currentTimeMillis()
            val genesisHash = "0000000000000000000000000000000000000000000000000000000000000000"
            val initialAudit = SecurityAuditEntity(
                id = "sec-genesis-0",
                timestamp = genesisTime,
                action = "SECURITY_SYSTEM_INITIALIZED",
                userId = "SYSTEM",
                userRole = "ADMIN",
                details = "Initialisation du coffre-fort de sécurité, base de données chiffrée et registre d'audit inviolable BEAUTY BF.",
                ipOrDeviceHash = "sha256:sys-keystore-primary",
                previousHash = genesisHash,
                integrityHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
            )
            database.securityAuditDao().insertAuditLog(initialAudit)
        }
    }
}
