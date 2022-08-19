package it.polito.wa2.group18.transitservice.DTOs
import java.sql.Timestamp

class AllTransitsRequest (
    val before : Long,
    val after : Long
)