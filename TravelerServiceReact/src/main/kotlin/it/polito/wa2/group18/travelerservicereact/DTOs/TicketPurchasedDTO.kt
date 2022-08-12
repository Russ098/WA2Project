package it.polito.wa2.group18.travelerservicereact.DTOs

import it.polito.wa2.group18.travelerservicereact.Entities.TicketPurchased
import java.sql.Timestamp
import java.util.*

data class TicketPurchasedDTO(
    val sub : Long?,
    val iat : Timestamp,
    val exp : Timestamp,
    val zid : String,
    val validFrom : Timestamp,
    val ticketType : String,
    val jws : String?,
    val userId : Long?
)

fun TicketPurchased.toDTO() : TicketPurchasedDTO{
    return TicketPurchasedDTO(sub, iat, exp, zid, validFrom, ticketType, jws, userId)
}