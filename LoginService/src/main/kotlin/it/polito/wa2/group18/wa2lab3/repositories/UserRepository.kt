package it.polito.wa2.group18.wa2lab3.repositories

import it.polito.wa2.group18.wa2lab3.entities.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository <User, Long>  {
    fun existsByEmail(email:String) : Boolean
    fun existsByUsername(username:String) : Boolean
    fun getById(id : Long) : User
    fun getByEmail(email: String) : User
    fun getByUsername(username : String) : User?
}