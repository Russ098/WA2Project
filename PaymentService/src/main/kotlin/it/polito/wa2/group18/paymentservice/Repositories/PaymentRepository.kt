package it.polito.wa2.group18.paymentservice.Repositories

import it.polito.wa2.group18.paymentservice.Entities.Payment
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface PaymentRepository : ReactiveCrudRepository<Payment, Long> {
    override fun findAll() : Flux<Payment>
    fun findAllByUserId(userId : Long) : Flux<Payment>
}