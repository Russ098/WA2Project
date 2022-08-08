package it.polito.wa2.group18.wa2lab3.services.userService

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import it.polito.wa2.group18.wa2lab3.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Key
import java.util.*


@Service
@Transactional
class LoginLogic : LoginLayer {
    @Autowired
    lateinit var userRepo: UserRepository

    @Autowired
    lateinit var jwtConfig : JwtConfiguration

    override fun login(username : String, password : String): String? {
        // leggere dal db se esiste username
        val userData = userRepo.getByUsername(username) //?: return null

        if(userData == null){
            println("USER NOT FOUND")
            return null
        }

        if(userData.pending) {
            println("USER PENDING")
            return null
        }

        // e se la password coincide
        /*val passEncoder = BCryptPasswordEncoder()
        val encodedPsw = passEncoder.encode(password)

        println("Encoded PSW: "+encodedPsw)*/

        if (!BCrypt.checkpw(password, userData.password)) {
            println("WRONG PASSWORD")
            return null
        }
        // generare jwt
        val key: Key = Keys.hmacShaKeyFor(jwtConfig.key.toByteArray())

        val c = Calendar.getInstance()
        c.time=Date()
        c.add(Calendar.HOUR, 1)

        val jws = Jwts.builder()
                    .setSubject(userData.id.toString())
                    .claim("credentials", userData.username)
                    .claim("roles", userData.roles)
                    .setIssuedAt(Date())
                    .setExpiration(c.time)
                    .signWith(key).compact()

        //println(jws)

        return jws
    }
}