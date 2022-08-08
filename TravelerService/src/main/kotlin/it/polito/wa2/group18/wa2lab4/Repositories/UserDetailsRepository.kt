package it.polito.wa2.group18.wa2lab4.Repositories

import it.polito.wa2.group18.wa2lab4.Entities.UserDetails
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserDetailsRepository : CrudRepository<UserDetails, Long>
{
    fun getById (id: Long?) : UserDetails?
    fun existsUserDetailsById(id : Long?) : Boolean
//    override fun findAll() : List<UserDetails>
}