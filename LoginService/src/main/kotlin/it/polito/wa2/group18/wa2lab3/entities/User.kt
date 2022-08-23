package it.polito.wa2.group18.wa2lab3.entities

import javax.persistence.*

enum class Role{
    CUSTOMER,
    ADMIN,
    SUPER_ADMIN
}

@Entity
@Table(name = "users")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id : Long? = null,
    var username:String,
    var email:String,
    var password:String,
    var pending: Boolean = true,
    var roles : String
)
