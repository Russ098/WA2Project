package it.polito.wa2.group18.travelerservicereact.Repositories

import it.polito.wa2.group18.travelerservicereact.DTOs.TicketPurchasedDTO
import it.polito.wa2.group18.travelerservicereact.Entities.TicketPurchased
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketPurchasedRepository : ReactiveCrudRepository<TicketPurchased, String>{
    fun getBySub(ticketID: Long?) : TicketPurchasedDTO?
}