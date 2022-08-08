package it.polito.wa2.group18.ticketcatalogueservice.DTO

import java.util.*

data class UserProfileDTO(var id : Long?, val name : String, val address : String, val date_of_birth : Date, val telephone_number : String)
