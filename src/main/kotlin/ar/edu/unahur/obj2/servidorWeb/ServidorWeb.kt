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
  fun usaProtocoloHttp(): Boolean {
    return url.startsWith("http:", true)
  }
  fun extension() : String = this.url.split(".").last()

}

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido)

class ServidorWeb(){

  val modulos = mutableListOf<Modulo>()

  fun recibirPedido(pedido: Pedido) : Respuesta{
    if (!pedido.usaProtocoloHttp()){
      return Respuesta(CodigoHttp.NOT_IMPLEMENTED,"",10,pedido)
    }
    else if (modulos.any{it.aceptaPedido(pedido)}) {
      val modulo = modulos.find{it.aceptaPedido(pedido)}
      return Respuesta(CodigoHttp.OK, modulo?.body!!,modulo.tiempo,pedido)
    }
    else {
      return Respuesta(CodigoHttp.NOT_FOUND,"",10,pedido)
    }


  }
}