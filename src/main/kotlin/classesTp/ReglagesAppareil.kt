package classesTp
import kotlin.properties.Delegates
import kotlin.reflect.KProperty
class ReglagesAppareil {
    var volume: Int by SeuilMaxDelegate(100)
    var luminosite: Int by SeuilMaxDelegate(50)
}