package it.polito.wa2.group18.transitservice.SecurityPackage

import io.jsonwebtoken.Jwts
import it.polito.wa2.group18.transitservice.DTOs.UserDetailsDTO
import it.polito.wa2.group18.transitservice.Entities.Role
import it.polito.wa2.group18.transitservice.Entities.Transits
import it.polito.wa2.group18.transitservice.Repositories.TransitRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.relational.core.mapping.Column
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Component
class JwtUtils {

    @Autowired
    lateinit var jwtConfig: JwtConfig

    @Autowired
    lateinit var transitRepo : TransitRepository

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

    fun validateJwsZoneExp(jws:String, readerID:Long, currentZone:String, ticketSecret:String) : Boolean {
        try {
            val key = ticketSecret.toByteArray()
            val jwsDecoded = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws)
            //exp è controllato automaticamente e -in caso il token sia scaduto- fallisce senza dover far controlli
            val zid = jwsDecoded.body["zid"].toString()
            val validFrom = jwsDecoded.body["validFrom"].toString().toLong()
            val now = System.currentTimeMillis()/1000;
            if(now < validFrom )
            {
                println("jwt not valid yet")
                return false
            }
            else if (!zid.contains(currentZone))
            {
                println("wrong zone")
                return false
            }
            //se è valido ed è corsa semplice, confrontare i transiti
            println("validation is going well")
            println("TYPE: "+jwsDecoded.body["ticketType"]!!.equals("Normal"))
            if(jwsDecoded.body["ticketType"] == "Normal")
            {
                println("ticket is normal")
                val exists = transitRepo.existsByJws(jws).toFuture().get()
                if(exists) {
                    println("this ticket has already been used")
                    return false
                }
            }
            //TODO sistemare il timestamp per includere anche l'orario
            val newtransit = transitRepo.save(Transits(id=null, timestamp= Timestamp.from(Instant.now()), userId = jwsDecoded.body["sub"].toString().toLong(),jws=jws,readerID=readerID,valid = true)).toFuture().get()
            println(newtransit.toString())
            return true
            } catch (e:Exception) { println(e); return false}
    }
}