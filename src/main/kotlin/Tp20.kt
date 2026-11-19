import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName

@Serializable
data class DonneesActuelles(
    @SerialName("temperature_2m") val temperature2m: Double,
    @SerialName("weather_code") val codeMeteo: Int
)


@Serializable
data class ReponseMeteo(
    val current: DonneesActuelles
)

// Nota bene : seuls deux champs sont repris…


fun main() = runBlocking {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    //val meteoDz = Json { ignoreUnknownKeys = true }.decodeFromString<DonneesActuelles>(reponseBrute)

    val meteoStateFlow: MutableStateFlow<DonneesActuelles?> = MutableStateFlow(null)

    // LE PRODUCTEUR
    launch {
        println("Producteur : nouvelle valeur transmise au StateFlow.")
        try {
            val reponseBrute: DonneesActuelles = client.get("https://api.open-meteo.com/v1/forecast?latitude=48.099998&longitude=-4.333330&current=temperature_2m,weather_code").body()
            meteoStateFlow.value = reponseBrute
        } catch (e: Exception) {
            println("Erreur lors du chargement : ${e.message}")
        }
    }

    // 4. LE CONSOMMATEUR (L'UI)
    launch {
        meteoStateFlow.collect { meteo ->
            if (meteo == null) {
                println("UI : En attente de réception des données...")
            } else {
                println("--- Météo à Saint-Brieuc ---")
                println("Température : ${meteo.temperature2m}")
                println("État du ciel : ${meteo.codeMeteo}")
            }
        }
    }

    // Attente pour laisser le temps aux coroutines de s'exécuter
    delay(10000)
    client.close()
    println("Client fermé.")
}