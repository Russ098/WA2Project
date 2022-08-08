package it.polito.wa2.group18.wa2lab4.DTOs

data class TicketsRequested(val cmd : String, val quantity : Long, val ticket : TicketPurchasedDTO)

