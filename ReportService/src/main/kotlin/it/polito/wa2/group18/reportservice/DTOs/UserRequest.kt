package it.polito.wa2.group18.reportservice.DTOs

data class UserRequest(
    val before:Long,
    val after:Long,
    val userId:Long
)
