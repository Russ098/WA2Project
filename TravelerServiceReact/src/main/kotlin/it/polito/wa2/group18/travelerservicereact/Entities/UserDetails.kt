package it.polito.wa2.group18.travelerservicereact.Entities


import it.polito.wa2.group18.travelerservicereact.DTOs.TicketPurchasedDTO
import it.polito.wa2.group18.travelerservicereact.DTOs.UserDetailsDTO
import it.polito.wa2.group18.travelerservicereact.DTOs.toDTO
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

enum class Role{
    CUSTOMER,
    ADMIN
}

@Table("userDetails")
class UserDetails (
    @Id
    @Column("id")
    var id : Long? = null, // Foreign key for Users
    @Column("tickets")
    var tickets : List<TicketPurchased>? = emptyList(),
    @Column("username")
    var username : String,
    @Column("roles")
    var roles : String
){
    fun toDTO(): UserDetailsDTO {
        val ticketsDTO: MutableList<TicketPurchasedDTO> = mutableListOf()
        tickets?.stream()?.forEach { t: TicketPurchased -> ticketsDTO.add(t.toDTO()) }
        val rolesList = roles.split(",")
        val rolesEnum : MutableList<Role> = mutableListOf()
        rolesList.forEach { role ->
            rolesEnum.add(Role.valueOf(role))
        }
        return UserDetailsDTO(id, ticketsDTO, rolesEnum, username)
    }
}

