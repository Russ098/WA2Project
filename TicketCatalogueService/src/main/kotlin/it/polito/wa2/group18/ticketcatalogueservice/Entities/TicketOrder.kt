package it.polito.wa2.group18.ticketcatalogueservice.Entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

enum class STATUS {
    PENDING,
    COMPLETE,
    CANCELLED
}

@Table("ticketOrder")
class TicketOrder (
    @Id
    @Column("id")
    var id : Long?,
    @Column("userId")
    var userId : Long,
    @Column("ticketTypeId")
    var ticketTypeId : Long,
    @Column("ticketNumber")
    var ticketNumber : Long,
    @Column("status")
    var status : STATUS = STATUS.PENDING
)