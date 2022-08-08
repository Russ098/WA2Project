package it.polito.wa2.group18.wa2lab4.controllers

import it.polito.wa2.group18.wa2lab4.DTOs.TicketsRequested
import it.polito.wa2.group18.wa2lab4.DTOs.UserProfileDTO
import it.polito.wa2.group18.wa2lab4.SecurityPackage.JwtConfig
import it.polito.wa2.group18.wa2lab4.SecurityPackage.JwtUtils
import it.polito.wa2.group18.wa2lab4.services.TravelerLayer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.mail.SimpleMailMessage
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


/* Authorization : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwiY3JlZGVudGlhbHMiOiJNb25rZXkgZC4gTHVmZnkiLCJyb2xlcyI6IkNVU1RPTUVSIiwiaWF0IjoxNjUyNzk2NDU2LCJleHAiOjE2NTI4MDAwNTZ9.o9HaX5GRdW6ZBZLZnWfh-3e4vekFfzrfVXQvQZargmWkiJXqiH1IU_2yGF8Qis5f5Y285m2cGu3OVKuMhRBzPg
{
  "sub": "2",
  "credentials": "Monkey d. Luffy",
  "roles": "CUSTOMER",
  "iat": 1652796456,
  "exp": 1652800056
}
*/
/* Authorization Admin : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiY3JlZGVudGlhbHMiOiJhZG1pbiIsInJvbGVzIjoiQ1VTVE9NRVIsQURNSU4iLCJpYXQiOjE2NTI3OTY2NzYsImV4cCI6MTY1MjgwMDI3Nn0.PG3vByo6oWa-yw7vD69N6U7a9mynoVnmMNdfCvdnN7VNiz8pEoIFhNdVEoXIIRvKa-nx1kIMqDxTse7kwI5PRg
{
  "sub": "1",
  "credentials": "admin",
  "roles": "CUSTOMER,ADMIN",
  "iat": 1652796676,
  "exp": 1652800276
}
*/

// esempio post profile
/*
{
    "name": "Francesco",
    "address": "via verdi 12",
    "date_of_birth": "2022-05-08T10:20:26.000+00:00",
    "telephone_number": "Rossi"
}
*/
//ESEMPIO BUY TICKETS
/*
{
    "cmd" : "buy_tickets",
    "quantity" : 2,
    "zones" : "ABC"
}
 */

@RestController
class Endpoint {
    @Autowired
    lateinit var service: TravelerLayer

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @Autowired
    lateinit var jwtConfig: JwtConfig

    @GetMapping("/my/profile")
    private fun getMyProfile(req: HttpServletRequest): ResponseEntity<*>? {
        val bearerJwt = req.getHeader(jwtConfig.headerName)
        if (bearerJwt.isEmpty() || !bearerJwt.startsWith(jwtConfig.headerPrefix)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        }
        val jwt = bearerJwt.split(" ")[1]

        val userDetailsDto =
            jwtUtils.getDetailsJwt(jwt) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)

        val res = service.getProfile(userDetailsDto.id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        return ResponseEntity.status(200).body(res)
    }

    @PutMapping("/my/profile")
    fun putMyProfile(req: HttpServletRequest, @RequestBody profile: UserProfileDTO): ResponseEntity<*>? {
        val bearerJwt = req.getHeader(jwtConfig.headerName)
        if (bearerJwt.isEmpty() || !bearerJwt.startsWith(jwtConfig.headerPrefix)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        }
        val jwt = bearerJwt.split(" ")[1]

        val userDetailsDto = jwtUtils.getDetailsJwt(jwt) ?: return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        profile.id = userDetailsDto.id

        service.saveProfile(profile) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.status(HttpStatus.CREATED).body(null)
    }


    @GetMapping("/my/tickets")
    fun getMyTickets(req: HttpServletRequest): ResponseEntity<*>? {
        val bearerJwt = req.getHeader(jwtConfig.headerName)
        if (bearerJwt.isEmpty() || !bearerJwt.startsWith(jwtConfig.headerPrefix)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        }
        val jwt = bearerJwt.split(" ")[1]

        val userDetailsDto =
            jwtUtils.getDetailsJwt(jwt) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        val res = service.getTickets(userDetailsDto.id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        return ResponseEntity.status(200).body(res)//.tickets) //.contentType(MediaType.IMAGE_PNG)
    }

    @PostMapping("/my/tickets")
    fun postMyTickets(req: HttpServletRequest, @RequestBody params: TicketsRequested): ResponseEntity<*> {
        val bearerJwt = req.getHeader(jwtConfig.headerName)
        if (bearerJwt.isEmpty() || !bearerJwt.startsWith(jwtConfig.headerPrefix)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        }
        val jwt = bearerJwt.split(" ")[1]

        val userDetailsDto =
            jwtUtils.getDetailsJwt(jwt) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        val res = service.buyTickets(userDetailsDto.id, params) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(null)
        return ResponseEntity.status(201).body(res) // lista tickets ritornati
    }

    @GetMapping("/my/qrcode/{id}")
    fun getMyQRCode(req: HttpServletRequest, @PathVariable("id") ticketID: Long): ResponseEntity<*>? {
        val bearerJwt = req.getHeader(jwtConfig.headerName)
        if (bearerJwt.isEmpty() || !bearerJwt.startsWith(jwtConfig.headerPrefix)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        }
        val jwt = bearerJwt.split(" ")[1]

        val userDetailsDto =
            jwtUtils.getDetailsJwt(jwt) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        val res = service.getQRCode(ticketID) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        return ResponseEntity.status(200).body(res)//.tickets) //.contentType(MediaType.IMAGE_PNG)
    }


    @GetMapping("/admin/travelers")
    fun getTravelers(): ResponseEntity<*>? {
        val res = service.getTravelers() ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.status(200).body(res)
    }


    @GetMapping("/admin/traveler/{userID}/profile")
    fun getUserProfile(req: HttpServletRequest, @PathVariable("userID") userID: Long): ResponseEntity<*>? {
        val res = service.getProfile(userID) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        return ResponseEntity.status(200).body(res)
    }

    @GetMapping("/admin/traveler/{userID}/tickets")
    fun getMyTickets(@PathVariable("userID") userID: Long): ResponseEntity<*>? {
        val res = service.getTickets(userID) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.status(200).body(res)//.tickets)
    }

}