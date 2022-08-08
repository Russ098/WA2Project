package it.polito.wa2.group18.wa2lab4.DTOs

import it.polito.wa2.group18.wa2lab4.Entities.TicketPurchased
import java.util.*

data class TicketPurchasedDTO(
    val sub : Long,
    val iat : Date,
    val exp : Date,
    val zid : String,
    val validFrom : Date,
    val ticketType : String,
    val jws : String?,
//    val qrcode : ByteArray?
)

fun TicketPurchased.toDTO() : TicketPurchasedDTO{
    return TicketPurchasedDTO(sub, iat, exp, zid, validFrom, ticketType, jws)//, qrcode)
}