package it.polito.wa2.group18.transitservice.DTOs

import it.polito.wa2.group18.transitservice.Entities.Transits
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.sql.Timestamp

data class TransitsDTO (
    val id: Long?,
    val timestamp : Timestamp,
    val userId : Long?,
    val jws : String?,
    val readerID : Long?,
    val valid : Boolean?
    )

fun Transits.toDTO() : TransitsDTO{
    return TransitsDTO(id, timestamp, userId, jws, readerID, valid)
}