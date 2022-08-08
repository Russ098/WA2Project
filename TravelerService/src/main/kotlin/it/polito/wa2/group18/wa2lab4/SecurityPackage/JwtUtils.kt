package it.polito.wa2.group18.wa2lab4.SecurityPackage

import io.jsonwebtoken.Jwts
import it.polito.wa2.group18.wa2lab4.DTOs.UserDetailsDTO
import it.polito.wa2.group18.wa2lab4.DTOs.toDTO
import it.polito.wa2.group18.wa2lab4.Entities.Role
import it.polito.wa2.group18.wa2lab4.Entities.UserDetails
import it.polito.wa2.group18.wa2lab4.Repositories.UserDetailsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtils {
    @Autowired
    lateinit var jwtConfig: JwtConfig

    @Autowired
    lateinit var userDetailsRepo : UserDetailsRepository

    fun validateJwt(authToken : String) : Boolean{
        val key = jwtConfig.key.toByteArray() // secret in application.properties
        try {
//            println(jwtConfig.key)
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken) //tira un'eccezione se non va bene
            return true
        }catch (e: Exception) {
            println(e)
            return false }
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

    fun getDetailsJwt(authToken: String) : UserDetailsDTO? {
        val key = jwtConfig.key.toByteArray() // secret in application.properties
        try {
            val jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)
            val id = jws.body.subject.toLong()
            val username = jws.body["credentials"].toString()
            val roles = jws.body["roles"].toString()

            var details : UserDetails? = null
            details = if (!userDetailsRepo.existsUserDetailsById(id)){
                userDetailsRepo.save(UserDetails(id, emptyList(), username, roles))
            } else {
                userDetailsRepo.getById(id) //what to do if null?
            }
            return details?.toDTO()
        } catch (e:Exception) {
            //includes ExpiredJwtException that manages the case of an expired jwt
            println(e); return null
        }
    }
}