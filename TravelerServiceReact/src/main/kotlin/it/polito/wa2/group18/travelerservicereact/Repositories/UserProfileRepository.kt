package it.polito.wa2.group18.travelerservicereact.Repositories

import it.polito.wa2.group18.travelerservicereact.Entities.UserProfile
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface UserProfileRepository  : ReactiveCrudRepository<UserProfile, Long> {
    fun getById(id: Long?) : Mono<UserProfile>
    fun existsById(id : Long?) : Mono<Boolean>
    fun save(userprofile: UserProfile): Mono<UserProfile?>
    override fun findAll() : Flux<UserProfile>
}