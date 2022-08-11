package it.polito.wa2.group18.travelerservicereact.Entities


import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("ticketPurchased")
class TicketPurchased(
    @Id
    @Column("id")
    val sub: Long = 0L,
    @Column("iat")
    val iat: Date = Date(),
    @Column("exp")
    var exp: Date = Date(),
    @Column("zid")
    val zid: String,
    @Column("validFrom")
    var validFrom: Date = Date(),
    @Column("ticketType")
    var ticketType: String = "",
    @Column("jws")
    var jws: String = "",
)

fun generateExp () : Date
{
    val c = Calendar.getInstance()
    c.time=Date()
    c.add(Calendar.HOUR, 1)
    return c.time
}


