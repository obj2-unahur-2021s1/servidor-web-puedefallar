package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime

// Para no tener los códigos "tirados por ahí", usamos un enum que le da el nombre que corresponde a cada código
// La idea de las clases enumeradas es usar directamente sus objetos: CodigoHTTP.OK, CodigoHTTP.NOT_IMPLEMENTED, etc
enum class CodigoHttp(val codigo: Int) {
  OK(200),
  NOT_IMPLEMENTED(501),
  NOT_FOUND(404),
}
class Pedido(val ip: String, val url: String, val fechaHora: LocalDateTime){

  fun usaProtocoloHttp(): Boolean = url.startsWith("http:", true)

  fun extension() : String = url.substringAfterLast(".")

  fun ruta() :String = url.substringAfter("//") //revisar
}

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido)

class ServidorWeb(){

  val modulos = mutableListOf<Modulo>()
  val analizadores = mutableListOf<Analizador>()

  fun hayModuloParaPedido(pedido: Pedido) = modulos.any{it.aceptaPedido(pedido)}

  fun enviarRespuestaAAnalizadores(respuesta: Respuesta, modulo: Modulo?) = analizadores.forEach{it.respuestasYModulos.put(respuesta,modulo)}

  fun recibirPedido(pedido: Pedido) : Respuesta{
    val respuesta : Respuesta
    val modulo : Modulo?

    if (!pedido.usaProtocoloHttp()){
      modulo = null
      respuesta = Respuesta(CodigoHttp.NOT_IMPLEMENTED,"",10,pedido)
    }
    else if (hayModuloParaPedido(pedido)) {
      modulo = modulos.find{it.aceptaPedido(pedido)}
      respuesta = Respuesta(CodigoHttp.OK, modulo?.body!!,modulo.tiempo,pedido)
    }
    else {
      modulo = null
      respuesta = Respuesta(CodigoHttp.NOT_FOUND,"",10,pedido)
    }
    enviarRespuestaAAnalizadores(respuesta, modulo)
    return respuesta
  }
}