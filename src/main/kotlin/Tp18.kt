package classesTp

fun main(){
//1)    val capteur = CapteurMoteur()
//    capteur.temperature = 50.0   // Valide -> Notification enregistrement
//    capteur.temperature = 200.0  // Invalide -> Notification d'alerte (mais la valeur est modifiée)

    val reglages = ReglagesAppareil()
    reglages.volume = 99    // Plafonné à 100
    reglages.luminosite = 49   // Plafonné à 50
    println("Volume : ${reglages.volume}, Luminosité : ${reglages.luminosite}")
}