package it.polito.wa2.group18.ticketcatalogueservice.SecurityPackage

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Component
class AuthEntryPoint : ServerAuthenticationEntryPoint {
    override fun commence(exchange: ServerWebExchange?, ex: AuthenticationException?): Mono<Void> {
        return Mono.fromRunnable{
            exchange!!.response.statusCode = HttpStatus.UNAUTHORIZED
        }
    }
}