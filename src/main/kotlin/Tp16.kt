import kotlinx.coroutines.*


suspend fun apiMeteo(): String {
    delay(3000)
    return "37 °C, canicule !"
}

suspend fun telechargerPochette() {

    delay(2000)
    println("Pochette téléchargée !")
}

suspend fun recupererDonneesCamera(): String{
    println("Début caméra")
    delay(2000)
    println("Fin caméra (après 2s)")
    return "Image Infrarouge"
}

suspend fun recupererDonneesSpectro(): String{
    println("Début spectro")
    delay(2000)
    println("Fin spectro (après 2s)")
    return "Analyse Spectrale"
}


fun main() = runBlocking {

//1)  println("--- Application Météo ---")
//    println("Action : L'utilisateur clique sur le bouton 'Actualiser'")
//    val job = launch{
//        println("... Chargement des données météo en cours ...")
//        println(apiMeteo())
//    }
//    println("L'interface reste réactive : l'utilisateur peut toujours cliquer ailleurs.")
//    job.join()
//    println("--- Fin de l'application ---")
//
//2)  println("Ouverture de l'application")
//    val job2 = launch {
//        println("Début du téléchargement de la pochette...")
//        telechargerPochette()
//    }
//    println("Menu principal (disponible, pendant le téléchargement)")
//    job2.join()
//    println("Fin de l'application")
//
//3)    println("Début de la mission ---")
//    val promesseResCamera = async {
//        recupererDonneesCamera()
//    }
//    val promesseResSpetro = async {
//        recupererDonneesSpectro()
//    }
//    println("Récupération des données en cours...")
//    val resultCamera = promesseResCamera.await()
//    val resultSpectro = promesseResSpetro.await()
//    println("$resultCamera  $resultSpectro")
//
//    println("Mission terminée")
//
//4)  println("--- Début de la maintenance station ---")
//    val job = launch {
//        try {
//            repeat(1000) { i ->
//                println("Réparation panneau solaire $i/10")
//                delay(500)
//            }
//        } catch (e: CancellationException) {
//            println(">>> ALERTE : Annulation d'urgence !")
//        }
//    }
//    delay(2500)
//    job.cancel()
//    job.join()
//    println("--- Mission terminée ---")

    println("--- Centre de contrôle : Début de la séquence ---")

    val scopeLancement = CoroutineScope(Dispatchers.Default)
    val job = scopeLancement.launch {
        try {
           while (isActive) {
                repeat(5) { i ->
                    println("Étape ${i + 1} : Vérification des systèmes...")
                    delay(1000)
                }
           }
        } catch (e: CancellationException) {
            println(">>>Capteur : Anomalie détectée ! Annulation du lancement !")
        }
    }
    delay(2500)
    scopeLancement.cancel()
    job.join()
    println("--- Fin de mission ---")
}