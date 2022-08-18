package it.polito.wa2.group18.transitservice.Repositories

import it.polito.wa2.group18.transitservice.Entities.Transits
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface TransitRepository : ReactiveCrudRepository<Transits, Long?> {
    fun existsByJws(jws:String) : Mono<Boolean>
}