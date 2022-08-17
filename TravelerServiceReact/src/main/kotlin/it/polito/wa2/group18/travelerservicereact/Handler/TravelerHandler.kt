package it.polito.wa2.group18.travelerservicereact.Handler

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import it.polito.wa2.group18.travelerservicereact.DTOs.TicketPurchasedDTO
import it.polito.wa2.group18.travelerservicereact.DTOs.TicketsRequested
import it.polito.wa2.group18.travelerservicereact.DTOs.UserProfileDTO
import it.polito.wa2.group18.travelerservicereact.DTOs.toDTO
import it.polito.wa2.group18.travelerservicereact.Entities.TicketPurchased
import it.polito.wa2.group18.travelerservicereact.Entities.toEntity
import it.polito.wa2.group18.travelerservicereact.Repositories.TicketPurchasedRepository
import it.polito.wa2.group18.travelerservicereact.Repositories.UserDetailsRepository
import it.polito.wa2.group18.travelerservicereact.Repositories.UserProfileRepository
import it.polito.wa2.group18.travelerservicereact.SecurityPackage.JwtConfig
import it.polito.wa2.group18.travelerservicereact.SecurityPackage.JwtTicketConfig
import it.polito.wa2.group18.travelerservicereact.SecurityPackage.JwtUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import java.security.Key

@Component
class TravelerHandler {

    @Autowired
    lateinit var ticketRepo: TicketPurchasedRepository

    @Autowired
    lateinit var userProfileRepo: UserProfileRepository

    @Autowired
    lateinit var userDetailsRepo: UserDetailsRepository

    @Autowired
    lateinit var jwtConfig: JwtConfig

    @Autowired
    lateinit var jwtTicketConfig: JwtTicketConfig

    @Autowired
    lateinit var jwtUtils: JwtUtils

    private val log = LoggerFactory.getLogger(javaClass)

    fun hello(req: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue("{\"${SecurityContextHolder.getContext().authentication.principal}\"}"))
    }

    fun getProfile(request: ServerRequest): Mono<ServerResponse> {
        val jwt = request.headers().firstHeader(jwtConfig.headerName)!!.split(" ")[1]
        val userId: Long? = jwtUtils.getUserIdFromJwt(jwt)
        return userProfileRepo.existsById(userId).flatMap { exists ->
            if (exists) {
                userProfileRepo.getById(userId).flatMap {
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(it))
                }
            } else
                ServerResponse.notFound().build()
        }
            .onErrorResume { println(it); ServerResponse.notFound().build() }
    }

    fun saveProfile(request: ServerRequest): Mono<ServerResponse> {
        val jwt = request.headers().firstHeader(jwtConfig.headerName)!!.split(" ")[1]

        return request.bodyToMono<UserProfileDTO>().flatMap { profile ->
            return@flatMap jwtUtils.getDetailsJwt(jwt).doOnSuccess {
                profile.id = it?.id
                userProfileRepo.save(profile.toEntity()).subscribe()
            }.flatMap {
                if (it != null) ServerResponse.ok().build()
                else ServerResponse.badRequest().build()
            }
                .onErrorResume { println(it); ServerResponse.badRequest().build() }
        }
    }

    fun getTickets(request: ServerRequest): Mono<ServerResponse> {
        val jwt = request.headers().firstHeader(jwtConfig.headerName)!!.split(" ")[1]
        val userId: Long? = jwtUtils.getUserIdFromJwt(jwt)
        log.info("sub $userId")
        return ticketRepo.getAllByUserId(userId).collectList()
            .flatMap { ticketList ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(ticketList))
            }
            .onErrorResume { println(it); ServerResponse.badRequest().build() }
    }

    fun buyTickets(request: ServerRequest): Mono<ServerResponse> {
        val jwt = request.headers().firstHeader(jwtConfig.headerName)!!.split(" ")[1]
        val result: MutableList<TicketPurchasedDTO> = mutableListOf()

        return request.bodyToMono<TicketsRequested>().flatMap { params ->
            if (params.cmd != "buy_tickets" || params.quantity <= 0) {
                ServerResponse.badRequest()
            }
            return@flatMap jwtUtils.getDetailsJwt(jwt).flatMap { userDetails ->
                for (i in 1..params.quantity) {
                    val newTicket = TicketPurchased(
                        zid = params.ticket.zid, exp = params.ticket.exp, iat = params.ticket.iat,
                        validFrom = params.ticket.validFrom, ticketType = params.ticket.ticketType,
                        userId = userDetails?.id
                    )
                    ticketRepo.save(newTicket).flatMap { ticket ->
                        val key: Key = Keys.hmacShaKeyFor(jwtTicketConfig.key.toByteArray())
                        val jws = Jwts.builder()
                            .setSubject(ticket.sub.toString())
                            .setIssuedAt(ticket.iat)
                            .setExpiration(ticket.exp)
                            .claim("zid", ticket.zid)
                            .claim("ticketType", ticket.ticketType)
                            .claim("validFrom", ticket.validFrom)
                            .signWith(key).compact()
                        ticket.jws = jws
                        ticket.userId = userDetails?.id
                        Mono.just(ticketRepo.save(ticket).subscribe()).flatMap {
                            Mono.just(result.add(ticket.toDTO()))
                        }
                    }.subscribe()
                }
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(result))
            }
        }
            .onErrorResume { println(it); ServerResponse.badRequest().build() }
    }

    fun getTravelers(request: ServerRequest): Mono<ServerResponse> {
        return userDetailsRepo.findAll().collectList()
            .flatMap { usersDetailsList ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(usersDetailsList))
            }
            .onErrorResume { println(it); ServerResponse.badRequest().build() }
    }

    fun getTicketsByUserID(request: ServerRequest) : Mono<ServerResponse> {
        val userID: Long = request.pathVariable("userID").toLong()
        return ticketRepo.getAllByUserId(userID).collectList()
            .flatMap { userTickets ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(userTickets))
            }
            .onErrorResume { println(it); ServerResponse.badRequest().build() }
    }

    fun getProfileByUserID(request: ServerRequest) : Mono<ServerResponse> {
        val userID: Long = request.pathVariable("userID").toLong()
        return userProfileRepo.getById(userID)
            .flatMap { userProfile ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(userProfile))
            }
            .onErrorResume { println(it); ServerResponse.badRequest().build() }
    }

    fun getSecret(request : ServerRequest) : Mono<ServerResponse>
    {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(jwtConfig.ticketKey))
    }
}