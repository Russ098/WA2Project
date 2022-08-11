package it.polito.wa2.group18.travelerservicereact.Kafka

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class TravelerRequest (
        @JsonProperty("jws")
        val jws : String,
        @JsonProperty("timestamp")
        val timestamp : Date,
        @JsonProperty("userID")
        val userID : Long?
)