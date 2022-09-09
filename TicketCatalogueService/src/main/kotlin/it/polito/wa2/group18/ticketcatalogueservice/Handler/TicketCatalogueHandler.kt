package it.polito.wa2.group18.ticketcatalogueservice.Handler

import it.polito.wa2.group18.ticketcatalogueservice.DTO.UserProfileDTO
import it.polito.wa2.group18.ticketcatalogueservice.Entities.AttemptedOrder
import it.polito.wa2.group18.ticketcatalogueservice.Entities.TicketOrder
import it.polito.wa2.group18.ticketcatalogueservice.Entities.TicketType
import it.polito.wa2.group18.ticketcatalogueservice.Kafka.LogController
import it.polito.wa2.group18.ticketcatalogueservice.Kafka.PaymentRequest
import it.polito.wa2.group18.ticketcatalogueservice.Repositories.TicketOrderRepository
import it.polito.wa2.group18.ticketcatalogueservice.Repositories.TicketTypeRepository
import it.polito.wa2.group18.ticketcatalogueservice.SecurityPackage.JwtConfig
import it.polito.wa2.group18.ticketcatalogueservice.SecurityPackage.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.*
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Component
class TicketCatalogueHandler {

    @Autowired
    lateinit var ticketTypeRepo: TicketTypeRepository

    @Autowired
    lateinit var ticketOrderRepo: TicketOrderRepository

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

    fun getAllTickets(req: ServerRequest): Mono<ServerResponse> {
        return ticketTypeRepo.findAll()
            .collectList()
            .flatMap {
                if (it.isEmpty()) {
                    ServerResponse.notFound().build(); } else {
                    ServerResponse.ok().body(BodyInserters.fromValue(it)); }
            }
            .onErrorResume { ServerResponse.notFound().build() }
    }

    fun getAllOrders(req: ServerRequest): Mono<ServerResponse> {
        return ticketOrderRepo.findAll()
            .collectList()
            .flatMap {
                ServerResponse.ok().body(BodyInserters.fromValue(it))
            }
            .onErrorResume { ServerResponse.notFound().build() }
    }

    fun getUserOrders(req: ServerRequest): Mono<ServerResponse> {
        val jwt = req.headers().firstHeader(jwtConfig.headerName)!!.split(" ")[1]
        val userId: Long? = jwtUtils.getUserIdFromJwt(jwt)

        return ticketOrderRepo.findAllByUserId(userId!!)
            //.switchIfEmpty{ServerResponse.ok().body(BodyInserters.fromValue("[]"))}
            .collectList()
            .flatMap {
                if (it.isEmpty()) { ServerResponse.notFound().build(); } else {
                    ServerResponse.ok().body(BodyInserters.fromValue(it)); }
            }
            .onErrorResume { ServerResponse.notFound().build() }
    }

    fun getUserOrder(req: ServerRequest): Mono<ServerResponse> {
        val jwt = req.headers().firstHeader(jwtConfig.headerName)!!.split(" ")[1]
        val userId: Long? = jwtUtils.getUserIdFromJwt(jwt)
        val orderID: Long = req.pathVariable("orderId").toLong()

        return ticketOrderRepo.findByIdAndUserId(orderID, userId!!)
            .flatMap { c ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(c))
            }
            .switchIfEmpty { ServerResponse.notFound().build() }
            .onErrorResume { ServerResponse.notFound().build() }
    }

    fun getAdminOrder(req: ServerRequest): Mono<ServerResponse> {
        val userID: Long = req.pathVariable("userId").toLong()

        return ticketOrderRepo.findAllByUserId(userID)
            .collectList()
            .flatMap {
                if (it.isEmpty()) {
                    ServerResponse.notFound().build(); } else {
                    ServerResponse.ok().body(BodyInserters.fromValue(it)); }
            }
            .onErrorResume { ServerResponse.notFound().build() }
    }

    fun addNewTicket(request: ServerRequest): Mono<ServerResponse> {
        /*return request.bodyToMono<TicketType>()
            .flatMap {
                Mono.just(ticketTypeRepo.save(it).subscribe())
            }
            .flatMap {
                if (it != null) ServerResponse.ok().build()
                else ServerResponse.badRequest().build()
            }
            .onErrorResume {println(it); ServerResponse.badRequest().build() }*/
        return request.bodyToMono<TicketType>()
            .flatMap { newTicket ->
                if(newTicket.price < 0 || newTicket.ageBelow < -1 || newTicket.duration < 1 || newTicket.zones.isNullOrEmpty())
                    ServerResponse.badRequest().build()
                if(newTicket.id!=null) {
                    println("TICKET ID NOT NULL")
                    ticketTypeRepo.existsById(newTicket.id!!).flatMap { exists ->
                        println("EXISTS: "+exists)
                        if (exists) {
                            ticketTypeRepo.save(newTicket).subscribe()
                            ServerResponse.ok().build()
                        } else
                            ServerResponse.badRequest().build()
                    }.onErrorResume { println(it); ServerResponse.badRequest().build() }
                }
                else {
                    ticketTypeRepo.save(newTicket).subscribe()
                    ServerResponse.ok().build()
                }
            }.onErrorResume {println(it); ServerResponse.badRequest().build() }
    }

    fun buyTickets(request: ServerRequest): Mono<ServerResponse> {
        val jwt = request.headers().firstHeader(jwtConfig.headerName)!!.split(" ")[1]
        val userId: Long? = jwtUtils.getUserIdFromJwt(jwt)
        println("USERID: " + userId)
        return request.bodyToMono<AttemptedOrder>().flatMap {
                // Check on tickets number > 0
                if(it.ticketNumber <= 0){
                    println("bad ticket number")
                    return@flatMap ServerResponse.badRequest().build()
                }
                ticketTypeRepo.findById(it.ticketId).flatMap { ticket ->
                    //vedere se ci sono age restrictions
                    if (ticket!!.ageBelow != -1) {
                        val profile = getUserProfile(jwt)
                        if ((Date().time - profile?.date_of_birth?.time!!) > (ticket.ageBelow * 31556952000))
                        {   println("bad age"); return@flatMap ServerResponse.badRequest().build()}
                    }
                    //salvare come pending

                    if(isValidDate(it.card.expirationDate) &&
                           LocalDate.parse(it.card.expirationDate) >
                            Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()){
                        ticketOrderRepo.save(TicketOrder(null, userId!!, it.ticketId, it.ticketNumber))
                                .flatMap { order ->
                                    val totalPrice = order.ticketNumber * ticket.price
                                    val cardData = it.card
                                    println("totalPrice ${totalPrice}")
                                    val paymentRequest = PaymentRequest(totalPrice,cardData, order.id, userId, jwt)
                                    logController.post(paymentRequest)

                                    ServerResponse.ok().body(BodyInserters.fromValue("{ \"order_id\" : ${order.id} }"))
                                }
                    }
                    else{
                        println("CHECKPOINT: Credit Card expired or invalid date format")
                        ServerResponse.badRequest().build()
                    }

                }.switchIfEmpty {
                    println(it); ServerResponse.badRequest().build() }
            }
            .onErrorResume { println(it); ServerResponse.badRequest().build() }
    }

    fun getUserProfile(authToken: String): UserProfileDTO? {
        val url = "http://localhost:8082/my/profile"

        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.setBearerAuth(authToken)

        val request = HttpEntity<String>(headers)

        val response: ResponseEntity<UserProfileDTO> = RestTemplateBuilder().build()
            .exchange(url, HttpMethod.GET, request, UserProfileDTO::class.java, 0)

        if (response.statusCode == HttpStatus.OK) {
            println("Response from traveler service: " + response.body)
            return response.body
        }
        return null
    }

    fun isValidDate(inDate: String?): Boolean {
        if (inDate == null || !inDate.matches(Regex( "\\d{4}-[01]\\d-[0-3]\\d"))) return false
        val df = SimpleDateFormat("yyyy-MM-dd")
        df.isLenient = false
        return try {
            df.parse(inDate)
            true
        } catch (ex: ParseException) {
            false
        }
    }

}


















