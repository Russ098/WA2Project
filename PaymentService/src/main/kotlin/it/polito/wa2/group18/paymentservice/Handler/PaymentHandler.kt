package it.polito.wa2.group18.paymentservice.Handler

import it.polito.wa2.group18.paymentservice.Entities.Payment
import it.polito.wa2.group18.paymentservice.Repositories.PaymentRepository
import it.polito.wa2.group18.paymentservice.SecurityPackage.JwtConfig
import it.polito.wa2.group18.paymentservice.SecurityPackage.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class PaymentHandler {

    @Autowired
    lateinit var paymentRepo: PaymentRepository
    @Autowired
    lateinit var jwtUtils: JwtUtils

    @Autowired
    lateinit var jwtConfig: JwtConfig

    fun hello (req: ServerRequest) : Mono<ServerResponse>
    {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue("Hello"))
    }
    fun getTransactions (req: ServerRequest) : Mono<ServerResponse>
    {
        return paymentRepo.findAll()
            .collectList()
            .flatMap{
                if(it.isEmpty()) { ServerResponse.notFound().build(); }
                else { ServerResponse.ok().body(BodyInserters.fromValue(it)); }
            }
            .onErrorResume { ServerResponse.notFound().build() }
    }

    fun getTransactionByUser(request: ServerRequest) : Mono<ServerResponse>
    {
        val jwt = request.headers().firstHeader(jwtConfig.headerName)!!.split(" ")[1]
        val userId: Long? = jwtUtils.getUserIdFromJwt(jwt)
        return paymentRepo.findAllByUserId(userId!!)
            .collectList()
            .flatMap{
                if(it.isEmpty()) { ServerResponse.notFound().build(); }
                else { ServerResponse.ok().body(BodyInserters.fromValue(it)); }
            }
            .onErrorResume { ServerResponse.notFound().build() }
    }
}