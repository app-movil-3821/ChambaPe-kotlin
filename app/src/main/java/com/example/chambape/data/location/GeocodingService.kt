package com.example.chambape.data.location

import android.content.Context
import android.location.Geocoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * Resultado de geocodificar una dirección a coordenadas reales.
 */
data class GeoResult(
    val latitude: Double,
    val longitude: Double,
    /** Dirección legible que se guardará en el campo `address` del backend. */
    val formattedAddress: String
)

/**
 * Resultado de geocodificar al revés: de un punto del mapa a campos de dirección.
 * Cualquier campo puede venir vacío si el Geocoder no lo resuelve.
 */
data class ReverseGeoResult(
    val departamento: String,
    val provincia: String,
    val distrito: String,
    val direccion: String
)

/**
 * Convierte una dirección escrita (Departamento / Provincia / Distrito / Dirección)
 * en coordenadas reales usando el Geocoder de Android.
 *
 * Compatible con minSdk 24: usa la API bloqueante de Geocoder en un hilo de fondo
 * (Dispatchers.IO), porque la variante con callback solo existe desde API 33.
 *
 * Estrategia en dos niveles:
 *   1. Intenta geocodificar la dirección completa (lo más preciso).
 *   2. Si falla, cae al centro del distrito (al menos el pin queda en la zona correcta).
 * Si ambos fallan, devuelve null y el ViewModel muestra un error al usuario.
 */
class GeocodingService(context: Context) {

    private val appContext = context.applicationContext

    suspend fun geocode(
        departamento: String,
        provincia: String,
        distrito: String,
        direccion: String
    ): GeoResult? = withContext(Dispatchers.IO) {

        // El texto legible que mostraremos y guardaremos como `address`.
        val formatted = listOf(direccion, distrito, provincia, departamento)
            .filter { it.isNotBlank() }
            .joinToString(", ")

        // 1) Intento preciso: dirección completa + país.
        val fullQuery = "$formatted, Perú"
        resolve(fullQuery)?.let { (lat, lng) ->
            return@withContext GeoResult(lat, lng, formatted)
        }

        // 2) Fallback: solo distrito/provincia/departamento (centro de la zona).
        val areaQuery = listOf(distrito, provincia, departamento, "Perú")
            .filter { it.isNotBlank() }
            .joinToString(", ")
        resolve(areaQuery)?.let { (lat, lng) ->
            return@withContext GeoResult(lat, lng, formatted)
        }

        null
    }

    /**
     * Convierte un punto del mapa (lat/lng) en campos de dirección legibles,
     * para autocompletar el formulario cuando el contratante elige en el mapa.
     */
    @Suppress("DEPRECATION")
    suspend fun reverseGeocode(
        latitude: Double,
        longitude: Double
    ): ReverseGeoResult? = withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(appContext, Locale("es", "PE"))
            val address = geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()
                ?: return@withContext null

            // Mapeo de campos de Android Address al esquema peruano.
            val departamento = address.adminArea ?: ""
            val provincia    = address.subAdminArea ?: ""
            val distrito     = address.locality ?: address.subLocality ?: ""

            // Dirección = calle + número; si no hay, usamos el nombre del lugar.
            val calle  = address.thoroughfare ?: ""
            val numero = address.subThoroughfare ?: ""
            val direccion = listOf(calle, numero)
                .filter { it.isNotBlank() }
                .joinToString(" ")
                .ifBlank { address.featureName ?: "" }

            ReverseGeoResult(departamento, provincia, distrito, direccion)
        } catch (e: Exception) {
            null
        }
    }

    /** Devuelve (lat, lng) para una consulta, o null si no se encontró nada. */
    @Suppress("DEPRECATION")
    private fun resolve(query: String): Pair<Double, Double>? {
        return try {
            val geocoder = Geocoder(appContext, Locale("es", "PE"))
            val results = geocoder.getFromLocationName(query, 1)
            val first = results?.firstOrNull() ?: return null
            Pair(first.latitude, first.longitude)
        } catch (e: Exception) {
            // Sin red, sin backend de geocoding en el dispositivo, etc.
            null
        }
    }
}