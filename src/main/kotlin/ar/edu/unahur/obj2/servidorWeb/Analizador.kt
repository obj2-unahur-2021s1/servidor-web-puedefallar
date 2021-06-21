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

class AnalizadorIpSospechosa() : Analizador() {

    val ipSospechosas = mutableListOf<String>()
    fun pedidoIPSospechosa(pedido:Pedido) : Boolean = ipSospechosas.contains(pedido.ip)
    fun pedidosDeIpSospechosa() = respuestas.filter{this.pedidoIPSospechosa(it.key.pedido)}


    fun cantidadPedidosRealizadosPor(ip : String ) : Int{
        return pedidosDeIpSospechosa().filter{ it.key.pedido.ip == ip}.size
    }

    /* fun moduloMasConsultado() : Modulo {

     }*/

    fun ipSospechosasQueRequirieron(ruta : String) : Set<String>{
        val pedidosQueRequirieronRuta = pedidosDeIpSospechosa().filter{ it.key.pedido.url.contains(ruta)}
        return pedidosQueRequirieronRuta.map{it.key.pedido.ip}.toSet()
    }
}

