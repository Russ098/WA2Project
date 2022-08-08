package it.polito.wa2.group18.wa2lab4.SecurityPackage

import it.polito.wa2.group18.wa2lab4.DTOs.UserDetailsDTO
import it.polito.wa2.group18.wa2lab4.Filters.JwtTokenFilter
import it.polito.wa2.group18.wa2lab4.services.TravelerLogic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Bean
fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
    return BCryptPasswordEncoder()
}

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(
    val jwtConfig: JwtConfig,
    val jwtUtils: JwtUtils,
    val authEntryPoint: AuthEntryPoint
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.cors().and()
            .csrf().disable()
            .exceptionHandling().authenticationEntryPoint(authEntryPoint)
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no sessions
            .and()
            .authorizeRequests()
            .antMatchers("/my/tickets").hasRole("CUSTOMER") //da testo, solo i customer possono accedere
            .antMatchers("/my/profile").hasAnyRole("CUSTOMER", "ADMIN")
            .antMatchers("/admin/**").hasRole("ADMIN")
            .and()
            .addFilterBefore(JwtTokenFilter(jwtConfig, jwtUtils), UsernamePasswordAuthenticationFilter::class.java)
    }
}
