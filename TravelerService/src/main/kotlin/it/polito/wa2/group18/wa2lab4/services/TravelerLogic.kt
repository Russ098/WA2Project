package it.polito.wa2.group18.wa2lab4.services

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import it.polito.wa2.group18.wa2lab4.DTOs.*
import it.polito.wa2.group18.wa2lab4.Entities.TicketPurchased
import it.polito.wa2.group18.wa2lab4.Entities.UserDetails
import it.polito.wa2.group18.wa2lab4.Entities.UserProfile
import it.polito.wa2.group18.wa2lab4.Entities.toEntity
import it.polito.wa2.group18.wa2lab4.Repositories.TicketPurchasedRepository
import it.polito.wa2.group18.wa2lab4.Repositories.UserDetailsRepository
import it.polito.wa2.group18.wa2lab4.Repositories.UserProfileRepository
import it.polito.wa2.group18.wa2lab4.SecurityPackage.JwtTicketConfig
import it.polito.wa2.group18.wa2lab4.services.userService.JwtConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayOutputStream
import java.security.Key
import java.util.*

@Service
@Transactional
class TravelerLogic : TravelerLayer {

    @Autowired
    lateinit var ticketRepo: TicketPurchasedRepository

    @Autowired
    lateinit var userDetailsRepo: UserDetailsRepository

    @Autowired
    lateinit var userProfileRepo: UserProfileRepository

    @Autowired
    lateinit var jwtTicketConfig : JwtTicketConfig

    override fun getDetails(id: Long?): UserDetailsDTO? {
        if (id == null) return null
        return userDetailsRepo.getById(id)?.toDTO()
    }

    override fun getProfile(id: Long?): UserProfileDTO? {
        if (id == null) return null
        if (!userProfileRepo.existsById(id)) {
            return null
        }
        return userProfileRepo.getById(id).toDTO()
    }

    override fun saveProfile(userprofileDTO: UserProfileDTO): UserProfile? {
        return userProfileRepo.save(userprofileDTO.toEntity())
    }

    override fun getTickets(id: Long?): List<TicketPurchasedDTO>? {
        if (id == null) return null
        if (!userDetailsRepo.existsById(id)) {
            return null
        }
        return userDetailsRepo.getById(id)?.toDTO()?.tickets//?.map{ it.qrcode }
    }

    override fun getQRCode(ticketID: Long?): ByteArray?{
        if (ticketID == null) return null
        val ticket = ticketRepo.getBySub(ticketID);
        return ticket?.jws?.let { QRCodeEncoding(it) };
    }

    override fun buyTickets(id: Long?, params: TicketsRequested): List<TicketPurchasedDTO>? {
        if (id == null) return null
        // check cmd // quantity >0
        if (params.cmd != "buy_tickets" || params.quantity <= 0) {
            return null
        }

        val user: UserDetails? = userDetailsRepo.getById(id)
        val result: MutableList<TicketPurchasedDTO> = emptyList<TicketPurchasedDTO>().toMutableList()

        for (i in 1..params.quantity) {
            var newTicket = TicketPurchased(
                zid = params.ticket.zid, user = user!!,
                exp = params.ticket.exp, validFrom = params.ticket.validFrom, ticketType = params.ticket.ticketType, qrcode = null )
            newTicket = ticketRepo.save(newTicket)

            val key: Key = Keys.hmacShaKeyFor(jwtTicketConfig.key.toByteArray())
            val jws = Jwts.builder()
                .setSubject(newTicket.sub.toString())
                .setIssuedAt(newTicket.iat)
                .setExpiration(newTicket.exp)
                .claim("zid", newTicket.zid)
                .claim("ticketType", newTicket.ticketType)
                .claim("validFrom", newTicket.validFrom)
                .signWith(key).compact()

            newTicket.jws = jws
//            newTicket.qrcode = QRCodeEncoding(jws)//.toString()
            println("NEW TICKET")
            println(newTicket)
            newTicket = ticketRepo.save(newTicket)
            println("NEW TICKET")
            println(newTicket)
            (user.tickets as MutableList<TicketPurchased>).add(newTicket)
            result.add(newTicket.toDTO())
        }
        userDetailsRepo.save(user!!)
        return result
    }

    fun QRCodeEncoding(jws: String): ByteArray{//Resource {
//        val imageOut = ByteArrayOutputStream()
//        QRCode(jws).render().writeImage(imageOut)
//        val imageBytes = imageOut.toByteArray()
//        val resource = ByteArrayResource(imageBytes, IMAGE_PNG_VALUE)
//        return resource.byteArray
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix: BitMatrix = qrCodeWriter.encode(jws, BarcodeFormat.QR_CODE, 500, 500)
        val byteArrayOutputStream = ByteArrayOutputStream()
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun getTicketExpiration (d:Date) : Date
    {
        return Date(d.time+jwtTicketConfig.expirationOffset)
    }

    fun getValidFrom(d : Date) : Date {
        /** To update in case the ticket is not an ordinal one */
        return Date(d.time)
    }

    override fun getTravelers(): List<String>? {
        // user Details populated if tickets bought or profile put
        return userDetailsRepo
            .findAll()
            .map { it.username }
            /*.filter {
                it != "admin" // avoid admin output
            }*/
            .toList()
    }
}































