package it.polito.wa2.group18.travelerservicereact.Kafka

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class TravelerResponse (
        @JsonProperty("userID")
        var userID : Long?,
        @JsonProperty("timestamp")
        var timestamp : Date?, //timestamp inviato da transitservice quando inizia la comunicazione Kafka
        @JsonProperty("jws") // se diverso da corsa singola sarà uguale NULL
        var jws : String?,
        @JsonProperty("key") // se diverso da corsa singola sarà uguale NULL
        var key : String?
)