package it.polito.wa2.group18.transitservice.SecurityPackage

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


@Component
class JwtTokenFilter(val jwtConfig: JwtConfig, val jwtUtils : JwtUtils) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val bearerJwt = exchange.request.headers.getFirst(jwtConfig.headerName)
        if (bearerJwt != null) {
            if(bearerJwt.isEmpty() || !bearerJwt.startsWith(jwtConfig.headerPrefix)){
                return chain.filter(exchange)
            }
        }
        val jwt = bearerJwt?.split(" ")?.get(1)
        if(jwt==null)
            return chain.filter(exchange)
        if(!jwt?.let { jwtUtils.validateJwt(it) }!!){
            return chain.filter(exchange)
        }

        val userDetailsJwt = jwtUtils.getDetailsJwtFilter(jwt)

        val authentication = UsernamePasswordAuthenticationToken(
            userDetailsJwt?.username,
            null,
            userDetailsJwt?.authorities
        )
        SecurityContextHolder.getContext().authentication = authentication

        return chain.filter(exchange)
            .subscriberContext(ReactiveSecurityContextHolder
                .withAuthentication(authentication))
    }

}