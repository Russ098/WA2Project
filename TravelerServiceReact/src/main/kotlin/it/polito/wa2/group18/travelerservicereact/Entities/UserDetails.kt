package it.polito.wa2.group18.travelerservicereact.Entities


import it.polito.wa2.group18.travelerservicereact.DTOs.UserDetailsDTO
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

enum class Role {
    CUSTOMER,
    ADMIN
}

@Table("userDetails")
class UserDetails(
        @Id
        @Column("id")
        var user_id: Long? = null, // Foreign key for Users
        @Column("username")
        var username: String,
        @Column("roles")
        var roles: String
) : Persistable<Long?> {
    fun toDTO(): UserDetailsDTO {
        val rolesList = roles.split(",")
        val rolesEnum: MutableList<Role> = mutableListOf()
        rolesList.forEach { role ->
            rolesEnum.add(Role.valueOf(role))
        }
        return UserDetailsDTO(id, rolesEnum, username)
    }

    override fun getId(): Long? {
        return user_id
    }

    override fun isNew(): Boolean {
        return true
    }
}

