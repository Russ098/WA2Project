package it.polito.wa2.group18.ticketcatalogueservice.Entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("tickettype")
class TicketType(
    @Id
    @Column("id")
    var id : Long?,
    @Column("tickettype")
    var ticketType :String,
    @Column("price")
    var price : Float,
    @Column("ageBelow")
    var ageBelow : Int = -1, //se -1 non ci sono restrizioni
    @Column("duration")
    var duration : Int = -1, //se -1 2H
    @Column("zones")
    var zones : String = "A"
)