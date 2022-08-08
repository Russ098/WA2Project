package it.polito.wa2.group18.wa2lab3.services.registrationService

import it.polito.wa2.group18.wa2lab3.dtos.ActivationDTO
import it.polito.wa2.group18.wa2lab3.dtos.UserDTO

interface RegistrationLayer {
    fun registration (user : UserDTO) : ActivationDTO?
}