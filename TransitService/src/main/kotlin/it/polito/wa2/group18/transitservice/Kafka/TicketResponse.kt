package it.polito.wa2.group18.transitservice.Kafka

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

class TicketResponse (
    @JsonProperty("userID")
    var userID : Long?,
    @JsonProperty("timestamp")
    var timestamp : Date?, //timestamp inviato da transitservice quando inizia la comunicazione Kafka
    @JsonProperty("jws") // se diverso da corsa singola sarà uguale NULL
    var jws : String?,
    @JsonProperty("readerID")
    var readerID : Long?,
    @JsonProperty("valid")
    var valid : Boolean?
)