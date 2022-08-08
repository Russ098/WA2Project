package it.polito.wa2.group18.paymentservice.Entities

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class PaymentInfo(
        @JsonProperty("creditCardNumber")
        val creditCardNumber : String?,
        @JsonProperty("expirationDate")
        val expirationDate : Date?,
        @JsonProperty("cvv")
        val cvv : String?,
        @JsonProperty("cardHolder")
        val cardHolder : String?
)