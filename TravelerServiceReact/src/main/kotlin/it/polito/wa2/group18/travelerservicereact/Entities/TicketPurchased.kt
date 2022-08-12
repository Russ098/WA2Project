package it.polito.wa2.group18.travelerservicereact.Entities


import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Table("ticketPurchased")
class TicketPurchased(
    @Id
    @Column("id")
    var sub: Long? = null,
    @Column("iat")
    val iat: Timestamp = Timestamp.from(Instant.now()),
    @Column("exp")
    var exp: Timestamp = Timestamp.from(Instant.now()),
    @Column("zid")
    val zid: String,
    @Column("validFrom")
    var validFrom: Timestamp = Timestamp.from(Instant.now()),
    @Column("ticketType")
    var ticketType: String = "",
    @Column("jws")
    var jws: String = "",
    @Column("userId")
    var userId : Long? = 0L
) : Persistable<Long?> {
    override fun getId(): Long? {
        return sub
    }

    override fun isNew(): Boolean {
        return sub == null
    }
}


