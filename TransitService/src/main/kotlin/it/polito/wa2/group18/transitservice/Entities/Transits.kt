package it.polito.wa2.group18.transitservice.Entities

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.Date

@Table("transits")
class Transits (
    @Id
    @Column("id")
    var id: Long?,
    @Column("timestamp")
    var timestamp : Date,
    @Column("userId")
    var userId : Long?,
    @Column("jws")
    var jws : String?,
    @Column("readerID")
    var readerID : Long?,
    @Column("valid")
    var valid : Boolean?
    )