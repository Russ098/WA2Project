package it.polito.wa2.group18.travelerservicereact.Repositories

import it.polito.wa2.group18.travelerservicereact.DTOs.TicketPurchasedDTO
import it.polito.wa2.group18.travelerservicereact.Entities.TicketPurchased
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.sql.Timestamp

@Repository
interface TicketPurchasedRepository : ReactiveCrudRepository<TicketPurchased, String>{
    fun getBySub(ticketID: Long?) : Mono<TicketPurchasedDTO?>

    fun getAllByUserId(userId : Long?) : Flux<TicketPurchasedDTO?>

    fun existsByJws(jws: String?) : Mono<Boolean>

    fun getByJws(jws: String?) : Mono<TicketPurchasedDTO>

    fun existsById (ticketID: Long?) : Mono<Boolean>

    fun countAllByIatBetween(after:Timestamp, before:Timestamp) : Mono<Int>

    fun countAllByIatBetweenAndUserId(after: Timestamp, before: Timestamp, userId: Long?) :Mono<Int>
}