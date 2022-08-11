package it.polito.wa2.group18.travelerservicereact.Handler

import it.polito.wa2.group18.travelerservicereact.DTOs.UserProfileDTO
import it.polito.wa2.group18.travelerservicereact.Entities.toEntity
import it.polito.wa2.group18.travelerservicereact.Repositories.TicketPurchasedRepository
import it.polito.wa2.group18.travelerservicereact.Repositories.UserDetailsRepository
import it.polito.wa2.group18.travelerservicereact.Repositories.UserProfileRepository
import it.polito.wa2.group18.travelerservicereact.SecurityPackage.JwtConfig
import it.polito.wa2.group18.travelerservicereact.SecurityPackage.JwtTicketConfig
import it.polito.wa2.group18.travelerservicereact.SecurityPackage.JwtUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class TravelerHandler {

    @Autowired
    lateinit var ticketRepo: TicketPurchasedRepository

    @Autowired
    lateinit var userDetailsRepo: UserDetailsRepository

    @Autowired
    lateinit var userProfileRepo: UserProfileRepository

    @Autowired
    lateinit var jwtConfig: JwtConfig

    @Autowired
    lateinit var jwtTicketConfig : JwtTicketConfig

    @Autowired
    lateinit var jwtUtils : JwtUtils

    private val log = LoggerFactory.getLogger(javaClass)

    fun hello(req: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{\"${SecurityContextHolder.getContext().authentication.principal}\"}"))
    }

    fun getProfile(request : ServerRequest) : Mono<ServerResponse> {
        val jwt = request.headers().firstHeader(jwtConfig.headerName)!!.split(" ")[1]
        val userId: Long? = jwtUtils.getUserIdFromJwt(jwt)
        return userProfileRepo.existsById(userId).flatMap { exists ->
            if(exists){
                userProfileRepo.getById(userId).flatMap {
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(it))
                }
            }else
                ServerResponse.notFound().build()
        }
        .onErrorResume { println(it); ServerResponse.notFound().build() }
    }

    fun saveProfile(request : ServerRequest) : Mono<ServerResponse> {
        val jwt = request.headers().firstHeader(jwtConfig.headerName)!!.split(" ")[1]
        val userDetails = jwtUtils.getDetailsJwt(jwt)

        return request.bodyToMono<UserProfileDTO>().flatMap { profile ->
            profile.id = userDetails?.id
            Mono.just(userProfileRepo.save(profile.toEntity()).subscribe())
        }.flatMap {
            if (it != null) ServerResponse.ok().build()
            else ServerResponse.badRequest().build()
        }
        .onErrorResume {println(it); ServerResponse.badRequest().build() }
    }

}