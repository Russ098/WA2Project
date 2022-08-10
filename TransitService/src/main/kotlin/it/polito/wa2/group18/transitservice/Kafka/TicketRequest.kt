package it.polito.wa2.group18.transitservice.Kafka

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class TicketRequest (
    @JsonProperty("jws")
    val jws : String,
    @JsonProperty("timestamp")
    val timestamp : Date
)