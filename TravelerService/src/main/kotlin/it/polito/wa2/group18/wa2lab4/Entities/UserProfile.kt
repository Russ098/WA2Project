package it.polito.wa2.group18.wa2lab4.Entities

import it.polito.wa2.group18.wa2lab4.DTOs.UserProfileDTO
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class UserProfile (
    @Id
    var id : Long? = null,
    var name : String,
    var address : String,
    var date_of_birth : Date,
    var telephone_number : String,
)

fun UserProfileDTO.toEntity(): UserProfile{
    return UserProfile(id, name, address, date_of_birth, telephone_number)
}