package it.polito.wa2.group18.wa2lab3.SecurityPackage

import it.polito.wa2.group18.wa2lab3.services.userService.JwtConfiguration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenFilter(val jwtConfig: JwtConfiguration, val jwtUtils: JwtUtils) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val bearerJwt = request.getHeader(jwtConfig.headerName)
        if (bearerJwt == null) {
            filterChain.doFilter(request, response)
            return
        }
        if (bearerJwt.isEmpty() || !bearerJwt.startsWith(jwtConfig.headerPrefix)) {
            filterChain.doFilter(request, response)
            return
        }
        val jwt = bearerJwt.split(" ")[1]

        if (!jwtUtils.validateJwt(jwt)) {
            filterChain.doFilter(request, response)
            return
        }

        val userDetailsJwt = jwtUtils.getDetailsJwtFilter(jwt)

        val authentication = UsernamePasswordAuthenticationToken(
            userDetailsJwt?.username,
            null,
            userDetailsJwt?.authorities
        )

        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }
}