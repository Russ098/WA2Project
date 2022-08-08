package it.polito.wa2.group18.wa2lab3.dtos

import java.util.UUID

data class ValidateParams(val provisional_id : UUID, val activation_code: String)

