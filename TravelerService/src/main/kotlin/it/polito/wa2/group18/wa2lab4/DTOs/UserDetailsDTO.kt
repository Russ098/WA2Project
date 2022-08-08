package it.polito.wa2.group18.wa2lab4.DTOs

import it.polito.wa2.group18.wa2lab4.Entities.Role
import it.polito.wa2.group18.wa2lab4.Entities.TicketPurchased
import it.polito.wa2.group18.wa2lab4.Entities.UserDetails
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class UserDetailsDTO(val id: Long?,
                     val tickets: List<TicketPurchasedDTO>?,
                     private val roles: List<Role>,
                     private val username : String)
    : org.springframework.security.core.userdetails.UserDetails{
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = mutableListOf<GrantedAuthority>()
        roles.forEach { role ->
            authorities.add(SimpleGrantedAuthority("ROLE_${role.name}"))
        }
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isAccountNonLocked(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCredentialsNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEnabled(): Boolean {
        TODO("Not yet implemented")
    }

}


