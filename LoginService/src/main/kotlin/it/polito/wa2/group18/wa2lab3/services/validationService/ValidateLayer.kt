package it.polito.wa2.group18.wa2lab3.services.validationService

import it.polito.wa2.group18.wa2lab3.dtos.UserValidatedDTO
import java.util.UUID

interface ValidateLayer {
    fun validation(provisional_id : UUID, activation_code : String) : UserValidatedDTO?
}