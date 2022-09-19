package it.polito.wa2.group18.wa2lab3

import it.polito.wa2.group18.wa2lab3.entities.Activation
import it.polito.wa2.group18.wa2lab3.entities.Role
import it.polito.wa2.group18.wa2lab3.entities.User
import it.polito.wa2.group18.wa2lab3.repositories.ActivationRepository
import it.polito.wa2.group18.wa2lab3.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

import org.springframework.security.config.web.server.ServerHttpSecurity.http
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
class Wa2Lab3Application{
    /*@Bean
    fun createAdmin(@Autowired userRepository: UserRepository, @Autowired activationRepository: ActivationRepository) : CommandLineRunner{
        return CommandLineRunner {
            val passEncoder = BCryptPasswordEncoder()
            val encodedPsw = passEncoder.encode("admin")
            val admin = User(
                username = "admin",
                email= "admin@admin.com",
                password = encodedPsw,
                pending = false,
                roles = "CUSTOMER,ADMIN,SUPER_ADMIN"
            )
            val activationRow = Activation()

            userRepository.save(admin)
            activationRepository.save(activationRow)
        }
    }*/
}

fun main(args: Array<String>) {
    runApplication<Wa2Lab3Application>(*args)
}
