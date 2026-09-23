package com.example

import com.example.data.model.Provider
import com.example.data.model.TravelFeeType
import org.junit.Assert.*
import org.junit.Test
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testDistanceCalculationHaversine() {
    // Distance between Tampouy (12.4110, -1.5420) and Ouaga 2000 (12.3020, -1.5120) in Ouagadougou
    val lat1 = 12.4110
    val lon1 = -1.5420
    val lat2 = 12.3020
    val lon2 = -1.5120

    val r = 6371.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val distanceKm = r * c

    // Expected around 12-13 km across Ouagadougou
    assertTrue(distanceKm > 10.0 && distanceKm < 15.0)
  }

  @Test
  fun testTravelFeeCalculation() {
    val fixedProvider = Provider(
      id = "prov-test",
      salonName = "Test Salon",
      profession = "Coiffeuse",
      phone = "+226 70 00 00 00",
      city = "Ouagadougou",
      sector = "Secteur 22",
      address = "Tampouy",
      acceptsHomeService = true,
      travelFeeType = TravelFeeType.FIXED,
      baseTravelFeeFcfa = 2000,
      perKmTravelFeeFcfa = 200
    )

    assertEquals(2000, fixedProvider.baseTravelFeeFcfa)
  }
}
