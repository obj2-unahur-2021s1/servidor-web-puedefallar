package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({
  describe("Un servidor web") {
    val servidor1 = ServidorWeb()

    val pedido1 = Pedido("192.168.10.1","http://google.com.ar/documentos/doc1.html",LocalDateTime.of(2021, 5, 22, 19, 35, 0))
    val pedido2 = Pedido("192.168.10.1","https://mercadolibre/documentos/doc1.html",LocalDateTime.of(2021, 7, 30, 11, 30, 22))

    it("Requerimiento 1") {
      val respuesta1 = servidor1.recibirPedido(pedido1)
      respuesta1.codigo.shouldBe(CodigoHttp.OK)

      val respuesta2 = servidor1.recibirPedido(pedido2)
      respuesta2.codigo.shouldBe(CodigoHttp.NOT_IMPLEMENTED)
    }



  }
})


