package it.polito.wa2.group18.transitservice.Repositories

import it.polito.wa2.group18.transitservice.DTOs.TransitsDTO
import it.polito.wa2.group18.transitservice.Entities.Transits
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.sql.Timestamp

interface TransitRepository : ReactiveCrudRepository<Transits, Long?> {
    fun existsByJws(jws:String) : Mono<Boolean>
    fun getAllByTimestampBetween(after: Timestamp, before: Timestamp) : Flux<TransitsDTO>
    fun countAllByTimestampBetweenAndUserId(after: Timestamp,before: Timestamp,userId:Long) : Mono<Int>
}