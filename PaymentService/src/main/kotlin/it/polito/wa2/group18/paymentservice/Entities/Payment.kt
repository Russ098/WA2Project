package it.polito.wa2.group18.paymentservice.Entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("Payment")
class Payment (
    @Id
    @Column("id")
    var id: Long?,
    @Column("userId")
    var userId : Long
)