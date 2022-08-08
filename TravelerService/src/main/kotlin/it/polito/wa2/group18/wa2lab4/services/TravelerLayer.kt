package it.polito.wa2.group18.wa2lab4.services

import it.polito.wa2.group18.wa2lab4.DTOs.TicketPurchasedDTO
import it.polito.wa2.group18.wa2lab4.DTOs.TicketsRequested
import it.polito.wa2.group18.wa2lab4.DTOs.UserDetailsDTO
import it.polito.wa2.group18.wa2lab4.DTOs.UserProfileDTO
import it.polito.wa2.group18.wa2lab4.Entities.UserDetails
import it.polito.wa2.group18.wa2lab4.Entities.UserProfile

interface TravelerLayer {
    fun getDetails(id: Long?) : UserDetailsDTO?
    fun getProfile(id: Long?) : UserProfileDTO?
    fun saveProfile(userprofile : UserProfileDTO) : UserProfile?
    fun getTickets(id : Long?) : List<TicketPurchasedDTO>?
    fun buyTickets(id : Long?, params : TicketsRequested) : List<TicketPurchasedDTO>?
    fun getTravelers() : List<String>?
    fun getQRCode(ticketID: Long?): ByteArray?
}