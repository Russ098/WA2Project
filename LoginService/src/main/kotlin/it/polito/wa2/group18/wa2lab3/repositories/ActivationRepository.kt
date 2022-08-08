package it.polito.wa2.group18.wa2lab3.repositories

import it.polito.wa2.group18.wa2lab3.entities.Activation
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ActivationRepository : CrudRepository<Activation, UUID>{
    fun getById(id : UUID) : Activation?
    override fun findAll() : List<Activation>
    override fun count() : Long
}