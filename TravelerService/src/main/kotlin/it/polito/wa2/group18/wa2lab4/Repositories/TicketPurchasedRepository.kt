package it.polito.wa2.group18.wa2lab4.Repositories

import it.polito.wa2.group18.wa2lab4.DTOs.TicketPurchasedDTO
import it.polito.wa2.group18.wa2lab4.Entities.TicketPurchased
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketPurchasedRepository : CrudRepository<TicketPurchased, String>{
    fun getBySub(ticketID: Long?) : TicketPurchasedDTO?
}