package it.polito.wa2.group18.ticketcatalogueservice.Kafka

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.group18.ticketcatalogueservice.Entities.PaymentInfo
import java.util.*

data class PaymentRequest(
        @JsonProperty("price")
        val price: Float,
        @JsonProperty("paymentInfo")
        val paymentInfo: PaymentInfo?,
        @JsonProperty("orderId")
        val orderId: Long?,
        @JsonProperty("userId")
        val userId: Long?,
        @JsonProperty("userJwt")
        val userJwt: String?
)