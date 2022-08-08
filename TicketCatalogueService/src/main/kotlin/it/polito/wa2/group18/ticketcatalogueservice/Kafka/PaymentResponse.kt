package it.polito.wa2.group18.ticketcatalogueservice.Kafka

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.group18.ticketcatalogueservice.Entities.PaymentInfo

data class PaymentResponse(
        @JsonProperty("orderId")
        val orderId: Long?,
        @JsonProperty("accepted")
        val accepted: Boolean,
        @JsonProperty("userJwt")
        val userJwt: String?
)