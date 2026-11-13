package classesTp

import kotlin.reflect.KProperty

class SeuilMaxDelegate(private var seuilMax : Int) {

    operator fun getValue(thisRef: Any?, propriete: KProperty<*>): Int {
        return seuilMax
    }

    operator fun setValue(thisRef: Any?, propriete: KProperty<*>, nouvelleValeur: Int) {
        if (nouvelleValeur > seuilMax) {
            println("[INFO] Limite atteinte sur '${propriete.name}' : $nouvelleValeur plafonné à $seuilMax.")
        } else {
            seuilMax = nouvelleValeur
        }
    }
}