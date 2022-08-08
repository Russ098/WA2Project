package it.polito.wa2.group18.wa2lab3.entities

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
class Activation
    (
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy="uuid2")
    var id: UUID? = null,
    @OneToOne
    var user : User? = null,
    var attempts : Int = 5,
    var activationCode : String? = getActivationCode(6),
    var expirationTime : Date? = getExpirationTime()
)

fun getActivationCode(length: Int) : String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZ0123456789"
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}

fun getExpirationTime() : Date {
    val currentDate = Date()
    // convert date to calendar
    val c = Calendar.getInstance()
    c.time=currentDate
    c.add(Calendar.DATE, 1)
    //c.add(Calendar.SECOND, 2)
    // convert calendar to date
    return c.time
}