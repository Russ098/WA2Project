package it.polito.wa2.group18.ticketcatalogueservice.Repositories

import it.polito.wa2.group18.ticketcatalogueservice.Entities.TicketType
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TicketTypeRepository : ReactiveCrudRepository<TicketType, Long>
{
    override fun findAll() : Flux<TicketType>
    override fun findById(id :Long) : Mono<TicketType>
}