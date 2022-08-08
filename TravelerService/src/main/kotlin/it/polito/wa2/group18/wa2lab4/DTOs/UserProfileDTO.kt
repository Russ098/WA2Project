package it.polito.wa2.group18.wa2lab4.DTOs

import it.polito.wa2.group18.wa2lab4.Entities.UserProfile
import java.util.*

data class UserProfileDTO(var id : Long?, val name : String, val address : String, val date_of_birth : Date, val telephone_number : String)

fun UserProfile.toDTO() : UserProfileDTO{
    return UserProfileDTO(id, name, address, date_of_birth, telephone_number)
}
