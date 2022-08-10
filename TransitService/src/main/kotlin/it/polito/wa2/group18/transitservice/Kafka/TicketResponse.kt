package it.polito.wa2.group18.transitservice.Kafka

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

class TicketResponse (
    @JsonProperty("userID")
    var userID : Long?,
    @JsonProperty("timestamp")
    var timestamp : Date? //timestamp inviato da transitservice quando inizia la comunicazione Kafka
)