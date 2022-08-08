package it.polito.wa2.group18.ticketcatalogueservice.Repositories

import it.polito.wa2.group18.ticketcatalogueservice.Entities.TicketOrder
import it.polito.wa2.group18.ticketcatalogueservice.Entities.TicketType
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TicketOrderRepository : ReactiveCrudRepository<TicketOrder, Long>
{
    override fun findAll() : Flux<TicketOrder>
    fun findAllByUserId(userId: Long) : Flux<TicketOrder>
    fun findByIdAndUserId (orderId: Long, userId:Long) : Mono<TicketOrder>
}