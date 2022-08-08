package it.polito.wa2.group18.ticketcatalogueservice.DTO

import java.util.*

data class TicketPurchasedDTO(
    val sub : Long?,
    val iat : Date,
    val exp : Date,
    val zid : String,
    val validFrom : Date,
    val ticketType : String,
    val jws : String?
)