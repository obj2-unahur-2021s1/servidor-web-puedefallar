package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime

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

class AnalizadorDeEstadistica() : Analizador(){
    val listaRespuestas = respuestas.keys

    fun tiempoDeRespuestaPromedio() : Int{
        return respuestas.keys.sumBy { it.tiempo } / respuestas.size
    }

    fun pedidosEntreFechas(fecha1 : LocalDateTime, fecha2 : LocalDateTime): Int{
        val listaPedidos = respuestas.keys.map{it.pedido}
        return listaPedidos.filter{ it.fechaHora.isAfter(fecha1) && it.fechaHora.isBefore(fecha2)}.size
    }

    fun respuestasConStringIncluido(string : String): Int{
        return listaRespuestas.filter{ it.body.contains(string)}.size
    }

    fun cantidadRespuestasExitosas() = listaRespuestas.filter{it.codigo.equals(CodigoHttp.OK)}.size
    fun porcentajeRespuestasExitosas() : Double{

        return ((cantidadRespuestasExitosas() / listaRespuestas.size) * 100).toDouble()
    }

}

