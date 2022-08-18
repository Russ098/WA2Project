package it.polito.wa2.group18.transitservice.Entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("qrCodeReaders")
class QRCodeReaders (
    @Id
    @Column("id")
    var id : Long?,
    //self-generated to identify itself
    @Column("jwt")
    var jwt : String?,
    //zone to compare with tickets validity zones
    @Column("zone")
    var zone : String
)