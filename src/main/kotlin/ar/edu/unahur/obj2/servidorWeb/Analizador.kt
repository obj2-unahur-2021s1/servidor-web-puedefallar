package ar.edu.unahur.obj2.servidorWeb

import java.lang.Math.ceil
import java.time.LocalDateTime

abstract class Analizador {
    val respuestasYModulos = mutableMapOf<Respuesta, Modulo?>()
}

class AnalizadorDemora(val demoraMinima : Int) : Analizador(){

    fun esRespuestaDemorada(respuesta: Respuesta) : Boolean = respuesta.tiempo > demoraMinima

    fun respuestasPorModulo(modulo: Modulo): Map<Respuesta, Modulo?> {
        return respuestasYModulos.filter { it.value == modulo }
    }

    fun respuestasDemoradasPorModulo(modulo: Modulo) : Int{
        return respuestasPorModulo(modulo).count { this.esRespuestaDemorada(it.key) }
    }
}

class AnalizadorIpSospechosa() : Analizador() {

    val ipSospechosas = mutableListOf<String>()

    fun pedidoIPSospechosa(pedido:Pedido) : Boolean = ipSospechosas.contains(pedido.ip)

    fun pedidosDeIpSospechosa() = respuestasYModulos.filter{this.pedidoIPSospechosa(it.key.pedido)}


    fun cantidadPedidosRealizadosPor(ip : String ) : Int{
        return pedidosDeIpSospechosa().filter{ it.key.pedido.ip == ip}.size
    }

    fun modulosConsultadosPorIpSospechosas() = respuestasYModulos.filter{(key, value) -> ipSospechosas.contains(key.pedido.ip) && value != null}.values

    fun cantidadConsultasAModulo(modulo: Modulo) = this.modulosConsultadosPorIpSospechosas().filter{it == modulo}.size

    //requerimiento incompleto

    fun moduloMasConsultado() {
        val consultaMayor = modulosConsultadosPorIpSospechosas().map{this.cantidadConsultasAModulo(it!!)}
        return
    }

    fun ipSospechosasQueRequirieron(ruta : String) : Set<String>{
        val pedidosQueRequirieronRuta = pedidosDeIpSospechosa().filter{ it.key.pedido.ruta().equals(ruta)}
        return pedidosQueRequirieronRuta.map{it.key.pedido.ip}.toSet()
    }
}

class AnalizadorDeEstadistica() : Analizador(){
    val listaRespuestas = respuestasYModulos.keys

    fun tiempoDeRespuestaPromedio() : Int{
        return respuestasYModulos.keys.sumBy { it.tiempo } / respuestasYModulos.size
    }

    fun pedidosEntreFechas(fecha1 : LocalDateTime, fecha2 : LocalDateTime): Int{
        val listaPedidos = respuestasYModulos.keys.map{it.pedido}
        return listaPedidos.filter{ it.fechaHora.isAfter(fecha1) && it.fechaHora.isBefore(fecha2)}.size
    }

    fun respuestasConStringIncluido(string : String): Int{
        return listaRespuestas.filter{ it.body.contains(string)}.size
    }

    fun cantidadRespuestasExitosas() = listaRespuestas.filter{it.codigo.equals(CodigoHttp.OK)}.size

    fun porcentajeRespuestasExitosas() : Int{
        return ((this.cantidadRespuestasExitosas().toDouble() / this.listaRespuestas.size.toDouble()) * 100).toInt()
    }

}

