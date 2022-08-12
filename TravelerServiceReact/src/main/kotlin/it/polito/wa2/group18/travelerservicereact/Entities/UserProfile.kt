package it.polito.wa2.group18.travelerservicereact.Entities

import it.polito.wa2.group18.travelerservicereact.DTOs.UserProfileDTO
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.sql.Timestamp

@Table("userProfile")
data class UserProfile (
    @Id
    @Column("id")
    var user_id : Long? = null,
    @Column("name")
    var name : String,
    @Column("address")
    var address : String,
    @Column("date_of_birth")
    var date_of_birth : Timestamp,
    @Column("telephone_number")
    var telephone_number : String,
) : Persistable<Long?> {
    override fun getId(): Long? {
        return user_id
    }

    override fun isNew(): Boolean {
        return true
    }
}

fun UserProfileDTO.toEntity(): UserProfile{
    return UserProfile(id, name, address, date_of_birth, telephone_number)
}