package classesTp
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class CapteurMoteur{
    var temperature:Double by Delegates.observable(0.0){ _, temperatureInitiale, newTemperature  ->
        if(newTemperature < -20.0 || newTemperature > 150.0){
            println("[ALERTE] Température invalide détectée : ${newTemperature}°C. (Plage autorisée: -20 à 150)")
        } else {
            println("[CAPTEUR] Nouvelle température enregistrée : temperature : $newTemperature")
        }
    }

    operator fun setValue(thisRef: Any?, propriete: KProperty<*>, newTemperature: Double) {
        temperature = newTemperature
    }
}