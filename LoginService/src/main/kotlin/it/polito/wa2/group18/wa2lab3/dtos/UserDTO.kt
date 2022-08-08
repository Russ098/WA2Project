package it.polito.wa2.group18.wa2lab3.dtos

import it.polito.wa2.group18.wa2lab3.entities.Role
import it.polito.wa2.group18.wa2lab3.entities.User

data class UserDTO(val username:String, val email:String, val password:String, val roles : String = Role.CUSTOMER.toString())

fun User.toDTO() : UserDTO {
    return UserDTO(username,email,password, roles)
}
