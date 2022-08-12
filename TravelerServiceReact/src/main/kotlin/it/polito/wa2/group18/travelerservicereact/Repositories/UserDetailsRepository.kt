package it.polito.wa2.group18.travelerservicereact.Repositories

import it.polito.wa2.group18.travelerservicereact.Entities.UserDetails
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserDetailsRepository : ReactiveCrudRepository<UserDetails, Long> {
    fun getById (id: Long?) : Mono<UserDetails?>
    fun existsUserDetailsById(id : Long?) : Mono<Boolean>
}