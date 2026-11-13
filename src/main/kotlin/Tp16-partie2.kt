import classesTp.EtatDrone
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.Channel


fun main() = runBlocking {
  val fluxBatterie = MutableStateFlow(100)

    val jobSonde = launch {
        val valeurs = listOf(80, 50, 20, 5)
        for (batterie in valeurs) {
            delay(1000)
            fluxBatterie.value = batterie
            println("--- Sonde [Ares-1] : Nouveau niveau -> $batterie%")
        }
    }

    val jobConsole = launch {
        fluxBatterie.collect { batterie ->
            println(">>>Console : État reçu -> $batterie%")
        }
    }

    delay(5000)
    if (fluxBatterie.value <= 20) println("!!! ALERTE CRITIQUE : Énergie faible sur Ares-1 !!!")

    jobSonde.cancel()
    jobConsole.cancel()
    println("--- Transmission terminée ---")

    val fluxMonitorage = MutableStateFlow(EtatDrone(0,"Au sol"))

    val jobVol = launch {
        val valeurs = listOf(EtatDrone(50,"En vol"), EtatDrone(0,"Livré"))
        for (etat in valeurs) {
            delay(1000)
            fluxMonitorage.value = etat
            println(">>>Moniteur : Altitude=${etat.altitude}m | Statut=${etat.statut}")
        }
    }

    val jobDecollage = launch {
        fluxMonitorage.collect { etat ->
            when(etat.statut) {
                "Au sol" -> println("--- Pilote : Drone au sol ---")
                "En vol" -> println("--- Pilote : Décollage effectué ---")
                "Livré" -> println("--- Pilote : Colis déposé ---")
            }
        }
    }

    delay(5000)

    jobVol.cancel()
    jobDecollage.cancel()
    println("--- Mission terminée ---")
}
