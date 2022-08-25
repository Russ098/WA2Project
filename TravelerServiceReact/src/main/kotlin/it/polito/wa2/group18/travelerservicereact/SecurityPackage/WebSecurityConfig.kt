package it.polito.wa2.group18.travelerservicereact.SecurityPackage

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig(
    val jwtConfig: JwtConfig,
    val jwtUtils: JwtUtils,
    val authEntryPoint: AuthEntryPoint
) {

    @Bean
    fun springSecurityFilterChain(http : ServerHttpSecurity): SecurityWebFilterChain? {
        return http.cors().and()
            .csrf().disable()
            .exceptionHandling().authenticationEntryPoint(authEntryPoint)
            .and()
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // see Comment below
            .authorizeExchange()
            .pathMatchers("/hello").hasAnyRole("CUSTOMER", "ADMIN","SUPER_ADMIN")
            .pathMatchers("/my/qrcode/**").permitAll()
            .pathMatchers("/my/**").hasAnyRole("CUSTOMER", "ADMIN","SUPER_ADMIN")
            .pathMatchers("/admin/**").hasAnyRole("ADMIN","SUPER_ADMIN")
            .pathMatchers("/secret").hasAnyRole("DEVICE")
            .and()
            .addFilterBefore(JwtTokenFilter(jwtConfig, jwtUtils), SecurityWebFiltersOrder.HTTP_BASIC).build()
    }
}

/**
 * The security context in a WebFlux application is stored in a ServerSecurityContextRepository.
 * Its WebSessionServerSecurityContextRepository implementation, which is used by default, stores the context in session.
 * Configuring a NoOpServerSecurityContextRepository instead would make our application stateless
 *
 * https://ard333.medium.com/authentication-and-authorization-using-jwt-on-spring-webflux-29b81f813e78
 * */