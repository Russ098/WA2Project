package it.polito.wa2.group18.reportservice.SecurityPackage

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
            .pathMatchers("/admin/**").hasAnyRole("ADMIN","SUPER_ADMIN")
            .and()
            .addFilterBefore(JwtTokenFilter(jwtConfig, jwtUtils), SecurityWebFiltersOrder.HTTP_BASIC).build()
    }
}
