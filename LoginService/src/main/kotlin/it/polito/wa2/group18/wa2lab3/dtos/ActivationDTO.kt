package it.polito.wa2.group18.wa2lab3.dtos

import it.polito.wa2.group18.wa2lab3.entities.Activation
import it.polito.wa2.group18.wa2lab3.entities.User
import java.util.*

data class ActivationDTO(val id: UUID?, val user: User?, val attempts:Int, val activationCode : String?, val expirationTime : Date?)

fun Activation.toDTO():ActivationDTO {
    return ActivationDTO(id, user, attempts, activationCode, expirationTime)
}