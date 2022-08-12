package it.polito.wa2.group18.travelerservicereact.DTOs

import it.polito.wa2.group18.travelerservicereact.Entities.UserProfile
import java.sql.Timestamp

data class UserProfileDTO(var id : Long?, val name : String, val address : String, val date_of_birth : Timestamp, val telephone_number : String)

fun UserProfile.toDTO() : UserProfileDTO{
    return UserProfileDTO(id, name, address, date_of_birth, telephone_number)
}
