package it.polito.wa2.group18.wa2lab3.controllers

import it.polito.wa2.group18.wa2lab3.dtos.LoginDataDTO
import it.polito.wa2.group18.wa2lab3.dtos.UserDTO
import it.polito.wa2.group18.wa2lab3.dtos.ValidateParams
import it.polito.wa2.group18.wa2lab3.services.validationService.ValidateLayer
import it.polito.wa2.group18.wa2lab3.services.registrationService.RegistrationLayer
import it.polito.wa2.group18.wa2lab3.services.userService.LoginLayer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class EndPoints {

    @Autowired
    lateinit var service: ValidateLayer

    @Autowired
    lateinit var regService: RegistrationLayer

    @Autowired
    lateinit var loginService : LoginLayer

    @GetMapping("/user/prova")
    fun prova() : String {
        return "Hello !!!"
    }

    @PostMapping("/user/register")
    fun registration(@RequestBody value: UserDTO): ResponseEntity<*>? {
        val dto = regService.registration(value)
        return if(dto ==null)
            ResponseEntity.status(400).body(null)
        else
            ResponseEntity.status(202).body("{ \"provisionalId\": \"${dto.id}\", \"email\": \"${dto.user?.email}\"}")
    }

    @PostMapping("/user/validate")
    fun validation(@RequestBody params : ValidateParams): ResponseEntity<*> {
        try {
            val user = service.validation(params.provisional_id, params.activation_code)
            return if (user != null) {
                ResponseEntity.status(201).body(user)
            } else {
                ResponseEntity.status(404).body(null)
            }
        }
        catch (e:Exception)
        {
            return ResponseEntity.status(404).body(null)
        }
    }

    @PostMapping("/user/login")
    fun login(@RequestBody params : LoginDataDTO): ResponseEntity<*> {
        try {
            val jwt = loginService.login(params.username, params.password)
            return if (jwt != null) {
                ResponseEntity.status(201).body(jwt)
            } else {
                ResponseEntity.status(404).body(null)
            }
        }
        catch (e:Exception)
        {
            return ResponseEntity.status(404).body(null)
        }
    }
}