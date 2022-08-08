package it.polito.wa2.group18.ticketcatalogueservice.Entities

import java.util.*

class AttemptedOrder (
    var ticketId : Long, //which ticket type
    var price : Float,
    var ticketNumber : Long, //how many
    var card : PaymentInfo
    )

class PaymentInfo(
    val creditCardNumber : String,
    val expirationDate : Date,
    val cvv : String,
    val cardHolder : String
)