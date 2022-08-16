package it.polito.wa2.group18.transitservice.Handler


import com.google.zxing.BinaryBitmap
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import it.polito.wa2.group18.transitservice.Kafka.LogController
import it.polito.wa2.group18.transitservice.Kafka.TicketRequest
import it.polito.wa2.group18.transitservice.SecurityPackage.JwtConfig
import it.polito.wa2.group18.transitservice.SecurityPackage.JwtUtils
import it.polito.wa2.group18.transitservice.Repositories.QRCodeReaderRepository
import it.polito.wa2.group18.transitservice.Repositories.TransitRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import java.io.ByteArrayInputStream
import java.time.Instant
import java.util.*
import javax.imageio.ImageIO

@Component
class TransitHandler {
    @Autowired
    lateinit var qrCodeReadersRepo: QRCodeReaderRepository
    @Autowired
    lateinit var transitRepo : TransitRepository
    @Autowired
    lateinit var jwtUtils: JwtUtils
    @Autowired
    lateinit var jwtConfig: JwtConfig
    @Autowired
    lateinit var logController: LogController

    fun hello(req: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{\"${SecurityContextHolder.getContext().authentication.principal}\"}"))
    }

    fun decodeQRCode(QRCode : ByteArray?) : String { // Get JWS from QRCode
        val qrCodeReader = QRCodeReader()
        val v = ByteArrayInputStream(QRCode)
        val binBitmap = BinaryBitmap(HybridBinarizer(BufferedImageLuminanceSource(
                ImageIO.read(v)
        )))
        val jws = qrCodeReader.decode(binBitmap)
        return  jws.toString()
    }

    /*fun decodeQRCode2(request: ServerRequest) : Mono<ServerResponse> { // Get JWS from QRCode
        return request.bodyToMono<ByteArray>().flatMap { QRCode ->
            val qrCodeReader = QRCodeReader()
            val v = ByteArrayInputStream(QRCode)
            val binBitmap = BinaryBitmap(HybridBinarizer(BufferedImageLuminanceSource(
                    ImageIO.read(v)
            )))
            val jws = qrCodeReader.decode(binBitmap).toString()
            ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(jws))
        }
        .onErrorResume { println(it); ServerResponse.badRequest().build() }
    }*/

    fun validateTicket(request: ServerRequest) : Mono<ServerResponse> {
        println("EnterValidate")
        val jwt = request.headers().firstHeader(jwtConfig.headerName)!!.split(" ")[1]
        val credentials = jwtUtils.getReaderIDFromJwt(jwt)
        //TO DO: controllare che effettivamente validi un reader e la sua pwd
        return qrCodeReadersRepo.existsByIdAndPwdEquals(credentials?.first, credentials?.second).flatMap{ it ->
            println(credentials)
            if (it == false) {
                ServerResponse.badRequest().body(BodyInserters.fromValue("Bad Authentication"))
            }else{
                request.bodyToMono<ByteArray>().flatMap { qrCode ->
                    val jws = decodeQRCode(qrCode)
                    logController.getValidationKey(TicketRequest(jws, Date(), credentials?.first))
                    ServerResponse.ok().body(BodyInserters.fromValue("Processing"))
                }
            }
            .onErrorResume { println(it); ServerResponse.badRequest().build() }
        }
    }


    // FUN X CONTROLLO READER DAL JWT CHECK READERID E PASSWORD
}