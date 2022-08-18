package it.polito.wa2.group18.transitservice.SecurityPackage

import io.jsonwebtoken.Jwts
import it.polito.wa2.group18.transitservice.DTOs.UserDetailsDTO
import it.polito.wa2.group18.transitservice.Entities.Role
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.relational.core.mapping.Column
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Component
class JwtUtils {

    @Autowired
    lateinit var jwtConfig: JwtConfig

    private val log = LoggerFactory.getLogger(javaClass)

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

            return UserDetailsDTO(rolesEnum, username)
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

    fun validateJwsZoneExp(jws:String, currentZone:String, ticketSecret:String) : Boolean {
        try {
            val key = ticketSecret.toByteArray()
            val jwsDecoded = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws)
            //exp è controllato automaticamente e -in caso il token sia scaduto- fallisce senza dover far controlli
            val zid = jwsDecoded.body["zid"].toString()
            val validFrom = jwsDecoded.body["validFrom"].toString().toLong()
            val now = System.currentTimeMillis()/1000;
            println("VALID FROM: "+validFrom + " vs NOW: "+now)
            println("ZONE:"+zid+" vs CURRENT:"+currentZone)
            if(validFrom < now || !zid.contains(currentZone))
                return false
            return true
            } catch (e:Exception) { println(e); return false}
    }
}