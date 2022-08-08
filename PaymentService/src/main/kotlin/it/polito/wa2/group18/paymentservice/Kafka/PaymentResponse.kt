package it.polito.wa2.group18.paymentservice.Kafka

import com.fasterxml.jackson.annotation.JsonProperty

data class PaymentResponse(
    @JsonProperty("orderId")
    val orderId: Long?,
    @JsonProperty("accepted")
    val accepted: Boolean,
    @JsonProperty("userJwt")
    val userJwt: String?
)