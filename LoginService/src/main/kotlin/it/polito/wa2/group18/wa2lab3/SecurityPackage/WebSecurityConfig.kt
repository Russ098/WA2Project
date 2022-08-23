package it.polito.wa2.group18.wa2lab3.SecurityPackage

import it.polito.wa2.group18.wa2lab3.entities.Role
import it.polito.wa2.group18.wa2lab3.services.userService.JwtConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Bean
fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
    return BCryptPasswordEncoder()
}

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(
    val jwtConfig: JwtConfiguration,
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
            .antMatchers("/user/**").permitAll()
            .antMatchers("/admin/**").hasRole(Role.SUPER_ADMIN.toString())
            .and()
            .addFilterBefore(JwtTokenFilter(jwtConfig, jwtUtils), UsernamePasswordAuthenticationFilter::class.java)
    }
}
