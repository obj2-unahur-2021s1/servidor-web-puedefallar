package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import io.kotest.matchers.collections.shouldContainExactly

class ServidorWebTest : DescribeSpec({
  describe("Un servidor web") {
    val servidor1 = ServidorWeb()

    val pedido1 = Pedido("192.168.10.1","http://google.com.ar/documentos/doc1.doc",LocalDateTime.of(2021, 5, 22, 19, 35, 0))
    val pedido2 = Pedido("192.168.10.1","https://mercadolibre/documentos/doc1.html",LocalDateTime.of(2021, 7, 30, 11, 30, 22))
    val pedido3 = Pedido("192.168.100.3", "http://videoCasamiento.avi", LocalDateTime.of(2019,10,1,22,10,0) )
    val moduloVideo = Modulo(mutableListOf("mp4", "mov") , "Videito", 5)
    val moduloTexto = Modulo(mutableListOf("txt", "doc", "pdf"), "Texto", 1)
    val moduloImagen = Modulo(mutableListOf("jpg", "png", "gif"), "Foto", 3)


    servidor1.modulos.add(moduloVideo)
    servidor1.modulos.add(moduloTexto)
    servidor1.modulos.add(moduloImagen)

    it("Requerimiento 1") {
      val respuesta1 = servidor1.recibirPedido(pedido1)
      respuesta1.codigo.shouldBe(CodigoHttp.OK)

      val respuesta2 = servidor1.recibirPedido(pedido2)
      respuesta2.codigo.shouldBe(CodigoHttp.NOT_IMPLEMENTED)
    }
    it("Requerimiento 2: Módulos") {

      val respuesta3 = servidor1.recibirPedido(pedido1)
      respuesta3.codigo.shouldBe(CodigoHttp.OK)
      respuesta3.body.shouldBe("Texto")
      respuesta3.tiempo.shouldBe(1)

      val respuesta4 = servidor1.recibirPedido(pedido3)
      respuesta4.codigo.shouldBe(CodigoHttp.NOT_FOUND)
      respuesta4.body.shouldBe("")
      respuesta4.tiempo.shouldBe(10)
    }

    it("Analizador de demora") {
      val analizadorDemora = AnalizadorDemora(3)
      val pedido4 = Pedido("192.168.100.3", "http://videoCoreoFinal.mp4", LocalDateTime.of(2019,10,1,22,10,0) )

      servidor1.analizadores.add(analizadorDemora)
      servidor1.recibirPedido(pedido4) //pedido recibido por modulo video
      servidor1.recibirPedido(pedido1) //pedido recibido por modulo texto

      analizadorDemora.respuestasDemoradasPorModulo(moduloVideo).shouldBe(1)
      analizadorDemora.respuestasDemoradasPorModulo(moduloTexto).shouldBe(0)
      analizadorDemora.respuestasDemoradasPorModulo(moduloImagen).shouldBe(0)
    }
    it("Ip sospechosas"){
      val analizadorIpSospechosa1 = AnalizadorIpSospechosa()
      servidor1.analizadores.add(analizadorIpSospechosa1)
      servidor1.recibirPedido(pedido1)

      analizadorIpSospechosa1.ipSospechosas.add("192.168.10.1")
      analizadorIpSospechosa1.ipSospechosas.add("192.168.10.9")

      analizadorIpSospechosa1.cantidadPedidosRealizadosPor("192.168.10.1").shouldBe(1)
      analizadorIpSospechosa1.cantidadPedidosRealizadosPor("192.168.10.9").shouldBe(0)

      analizadorIpSospechosa1.ipSospechosasQueRequirieron("/google.com.ar/documentos/doc1.doc").shouldContainExactly("192.168.10.1")


    }

  }
})


