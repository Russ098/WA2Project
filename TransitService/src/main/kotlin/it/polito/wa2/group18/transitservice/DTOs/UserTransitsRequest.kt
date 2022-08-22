package it.polito.wa2.group18.transitservice.DTOs

class UserTransitsRequest (
    val before : Long,
    val after : Long,
    val user : Long
)