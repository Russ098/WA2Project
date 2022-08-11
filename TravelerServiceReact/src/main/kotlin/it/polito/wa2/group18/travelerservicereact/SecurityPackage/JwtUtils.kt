package it.polito.wa2.group18.travelerservicereact.SecurityPackage

import io.jsonwebtoken.Jwts
import it.polito.wa2.group18.travelerservicereact.DTOs.UserDetailsDTO
import it.polito.wa2.group18.travelerservicereact.Entities.Role
import it.polito.wa2.group18.travelerservicereact.Entities.UserDetails
import it.polito.wa2.group18.travelerservicereact.Repositories.UserDetailsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtUtils {

    @Autowired
    lateinit var jwtConfig: JwtConfig

    @Autowired
    lateinit var userDetailsRepo : UserDetailsRepository

    fun validateJwt(authToken : String) : Boolean{
        val key = jwtConfig.key.toByteArray()
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)
            true
        }catch (e: Exception) {
            println(e)
            false
        }
    }

    fun getDetailsJwtFilter(authToken: String) : UserDetailsDTO? {
        val key = jwtConfig.key.toByteArray() // secret in application.properties
        try {
            val jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)

            val roles = jws.body["roles"].toString()
            val rolesList = roles.split(",")
            val rolesEnum : MutableList<Role> = mutableListOf()
            rolesList.forEach { role ->
                rolesEnum.add(Role.valueOf(role))
            }

            val username = jws.body["credentials"].toString()

            return UserDetailsDTO(null, null, rolesEnum, username)
        } catch (e:Exception) { println(e); return null}
    }

    fun getUserIdFromJwt(authToken : String) : Long? {
        val key = jwtConfig.key.toByteArray() // secret in application.properties
        try {
            val jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)

            val userId = jws.body["sub"].toString().toLong()

            return userId
        } catch (e:Exception) { println(e); return null}
    }

    fun getDetailsJwt(authToken: String) : UserDetailsDTO? {
        val key = jwtConfig.key.toByteArray() // secret in application.properties
        try {
            val jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)
            val id = jws.body.subject.toLong()
            val username = jws.body["credentials"].toString()
            val roles = jws.body["roles"].toString()

            val details = UserDetails(id, emptyList(), username, roles)
            if (!userDetailsRepo.existsUserDetailsById(id)){
                Mono.just(userDetailsRepo.save(details).subscribe())
                        .flatMap {
                            Mono.just(details)
                        }
            } else {
                userDetailsRepo.getById(id) //what to do if null?
            }
            return details.toDTO()
        } catch (e:Exception) {
            //includes ExpiredJwtException that manages the case of an expired jwt
            println(e); return null
        }
    }
}