package ar.edu.unahur.obj2.servidorWeb

abstract class Analizador {
    val respuestas = mutableMapOf<Respuesta, Modulo>()
}

class AnalizadorDemora(val demoraMinima : Int) : Analizador(){

    fun esRespuestaDemorada(respuesta: Respuesta) : Boolean = respuesta.tiempo > demoraMinima

    fun respuestasPorModulo(modulo: Modulo): Map<Respuesta, Modulo> {
        return respuestas.filter { it.value == modulo }
    }

    fun respuestasDemoradasPorModulo(modulo: Modulo) : Int{
        return respuestasPorModulo(modulo).count { this.esRespuestaDemorada(it.key) }
    }
}