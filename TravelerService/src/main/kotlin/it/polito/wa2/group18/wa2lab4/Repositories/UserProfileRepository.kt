package it.polito.wa2.group18.wa2lab4.Repositories

import it.polito.wa2.group18.wa2lab4.Entities.UserProfile
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserProfileRepository  : CrudRepository<UserProfile, Long>{
    fun getById(id: Long?) : UserProfile
    fun existsById(id : Long?) : Boolean
    fun save(userprofile: UserProfile): UserProfile?
    override fun findAll() : List<UserProfile>
}