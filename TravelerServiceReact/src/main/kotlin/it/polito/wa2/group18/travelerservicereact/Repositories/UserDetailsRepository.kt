package it.polito.wa2.group18.travelerservicereact.Repositories

import it.polito.wa2.group18.travelerservicereact.Entities.UserDetails
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserDetailsRepository : ReactiveCrudRepository<UserDetails, Long>
{
    fun getById (id: Long?) : UserDetails?
    fun existsUserDetailsById(id : Long?) : Boolean
//    override fun findAll() : List<UserDetails>
}