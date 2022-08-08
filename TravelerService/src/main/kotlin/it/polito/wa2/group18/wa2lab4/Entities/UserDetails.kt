package it.polito.wa2.group18.wa2lab4.Entities


import it.polito.wa2.group18.wa2lab4.DTOs.TicketPurchasedDTO
import it.polito.wa2.group18.wa2lab4.DTOs.UserDetailsDTO
import it.polito.wa2.group18.wa2lab4.DTOs.toDTO
import org.springframework.security.core.authority.SimpleGrantedAuthority
import javax.persistence.*

enum class Role{
    CUSTOMER,
    ADMIN
}

@Entity
class UserDetails (
    @Id
    var id : Long? = null, // Foreign key for Users
    @OneToMany
    var tickets : List<TicketPurchased>? = emptyList(),
    var username : String,
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

