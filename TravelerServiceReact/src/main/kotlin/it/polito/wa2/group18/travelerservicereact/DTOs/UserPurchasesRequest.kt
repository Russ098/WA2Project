package it.polito.wa2.group18.travelerservicereact.DTOs

data class UserPurchasesRequest(
    val before:Long,
    val after:Long,
    val userId:Long
)
