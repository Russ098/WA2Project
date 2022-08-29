package it.polito.wa2.group18.travelerservicereact.DTOs

import it.polito.wa2.group18.travelerservicereact.Entities.Role

class UserDetailsDTOResponse (val id: Long?, val roles: List<Role>, val username : String)