package it.polito.wa2.group18.transitservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@SpringBootApplication
class TransitServiceApplication

@Configuration
@EnableWebSecurity
class KotlinSecurityConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.
        authorizeRequests()
            .mvcMatchers("/**")
            .permitAll()
            .and()
            .csrf().disable()
    }
}

fun main(args: Array<String>) {
    runApplication<TransitServiceApplication>(*args)
}
