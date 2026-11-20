import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.isActive
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

@Serializable
data class IssPosition(
    val latitude: String,
    val longitude: String
)

@Serializable
data class ReponseISS(
    @SerialName("iss_position") val issPosition: IssPosition
)


fun main() = runBlocking {

//1)
//    val client = HttpClient(CIO) {
//        install(ContentNegotiation) {
//            json(Json {
//                ignoreUnknownKeys = true
//            })
//        }
//    }
//
//    val meteoStateFlow: MutableStateFlow<ReponseMeteo?> = MutableStateFlow(null)
//
//    // LE PRODUCTEUR
//    launch {
//        while (isActive) {
//            try {
//                    println("Producteur : nouvelle valeur transmise au StateFlow.")
//                    val reponseBrute: ReponseMeteo = client.get("https://api.open-meteo.com/v1/forecast?latitude=48.099998&longitude=-4.333330&current=temperature_2m,weather_code").body()
//                    meteoStateFlow.value = reponseBrute
//            } catch (e: Exception) {
//                println("Erreur lors du chargement : ${e.message}")
//            }
//            delay(10000)
//        }
//    }
//
//    // LE CONSOMMATEUR (L'UI)
//    launch {
//        meteoStateFlow.collect { meteo ->
//            if (meteo == null) {
//                println("UI : En attente de réception des données...")
//            } else {
//                println("--- Météo à Douarnenez ---")
//                println("Température : ${meteo.current.temperature2m}")
//                when {
//                    meteo.current.codeMeteo == 1 -> println("État du ciel : Dégagé")
//                    meteo.current.codeMeteo == 2 -> println("État du ciel : Partiellement nuageux")
//                    meteo.current.codeMeteo == 3 -> println("État du ciel : Nuageux")
//                }
//
//            }
//        }
//    }
//
//    // Attente pour laisser le temps aux coroutines de s'exécuter
//    delay(50000)
    //client.close()
    //println("Client fermé.")

//2)

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    val issStateFlow: MutableStateFlow<ReponseISS?> = MutableStateFlow(null)

    // LE PRODUCTEUR
    launch {
        while (isActive) {
            try {
                println("ISS : ${issStateFlow.value}")
                val reponseBrute: ReponseISS = client.get(" http://api.open-notify.org/iss-now.json").body()
                issStateFlow.value = reponseBrute
            } catch (e: Exception) {
                println("Erreur lors du chargement : ${e.message}")
            }
            delay(5000)
        }
    }

    // LE CONSOMMATEUR (L'UI)
    launch {
        issStateFlow.filterNotNull().collect { iss ->
            println("UI Update -> latitude=${iss.issPosition.latitude}, longitude=${iss.issPosition.longitude}")
        }
    }

    // Attente pour laisser le temps aux coroutines de s'exécuter
    delay(60000)
    client.close()
    println("Client fermé.")

}