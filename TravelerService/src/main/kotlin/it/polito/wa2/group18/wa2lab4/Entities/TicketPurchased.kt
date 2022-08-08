package it.polito.wa2.group18.wa2lab4.Entities


import java.util.*
import javax.persistence.*

@Entity
class TicketPurchased(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val sub: Long = 0L,
    val iat: Date = Date(),
    var exp: Date = Date(),
    val zid: String,
    var validFrom: Date = Date(),
    var ticketType: String = "",
    var jws: String = "",
    var qrcode: ByteArray? = "".toByteArray(),
    @ManyToOne
    val user: UserDetails
)

fun generateExp () : Date
{
    val c = Calendar.getInstance()
    c.time=Date()
    c.add(Calendar.HOUR, 1)
    return c.time
}


