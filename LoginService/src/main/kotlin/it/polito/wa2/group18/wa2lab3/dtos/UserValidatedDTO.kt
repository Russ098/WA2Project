package it.polito.wa2.group18.wa2lab3.dtos

import it.polito.wa2.group18.wa2lab3.entities.User

data class UserValidatedDTO(val id : Long, val username:String, val email:String)

fun User.toValidatedDTO() : UserValidatedDTO {
    return UserValidatedDTO(id!!, username,email)
}