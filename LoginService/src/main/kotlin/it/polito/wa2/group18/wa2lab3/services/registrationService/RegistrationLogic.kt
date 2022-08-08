package it.polito.wa2.group18.wa2lab3.services.registrationService

import it.polito.wa2.group18.wa2lab3.dtos.ActivationDTO
import it.polito.wa2.group18.wa2lab3.dtos.UserDTO
import it.polito.wa2.group18.wa2lab3.dtos.toDTO
import it.polito.wa2.group18.wa2lab3.entities.Activation
import it.polito.wa2.group18.wa2lab3.entities.Role
import it.polito.wa2.group18.wa2lab3.entities.User
import it.polito.wa2.group18.wa2lab3.repositories.ActivationRepository
import it.polito.wa2.group18.wa2lab3.repositories.UserRepository
import it.polito.wa2.group18.wa2lab3.services.validationService.MailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class RegistrationLogic : RegistrationLayer {

    @Autowired
    lateinit var userRepo: UserRepository
    @Autowired
    lateinit var activationRepo : ActivationRepository

    @Transactional
    override fun registration (user: UserDTO) : ActivationDTO?
    {
            if(user.username=="" || user.password=="" || user.email=="") {
                println("CHECKPOINT: Missing field")
                return null
            }
            if(userRepo.existsByEmail(user.email) || userRepo.existsByUsername(user.username)) {
                println("CHECKPOINT: ALREADY EXISTS")
                return null
            }
            if(!strongPassword(user.password)) {
                println("CHECKPOINT: WEAK PASSWORD")
                return null
            }
            if(!validEmail(user.email))
            {
                println("CHECKPOINT: INVALID EMAIL")
                return null
            }

            val passEncoder = BCryptPasswordEncoder()
            val encodedPsw = passEncoder.encode(user.password)

            val addedUser : User = userRepo.save(User(username = user.username, email= user.email, password = encodedPsw, roles = user.roles ))
            val addedActivation : Activation = activationRepo.save(Activation(user=addedUser))

            /** Send email **/
            emailSender(addedActivation.toDTO())
            //println(addedUser.toDTO())
            //println(addedActivation.toDTO())
            return addedActivation.toDTO()
    }

    fun strongPassword (password:String):Boolean
    {
        if(password.length<8 ||
                password.contains("\\s".toRegex()) ||
                !password.contains("[0-9]".toRegex()) ||
                !password.contains("[A-Z]".toRegex()) ||
                !password.contains("[a-z]".toRegex()) ||
                !password.contains("[^a-zA-Z\\d\\s]".toRegex()))
            return false
        return true
    }

    fun validEmail(email:String):Boolean
    {
        return email.matches("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$".toRegex())
    }

    fun emailSender(activation: ActivationDTO) {
        val message = SimpleMailMessage()
        val sender = MailService().mailSender()

        message.setFrom("g18.wa2.poliTo2k22@gmail.com")
        message.setTo(activation.user?.email)
        message.setSubject("Complete registration !!")
        message.setText("This is your activation code: ${activation.activationCode}.")
        sender.send(message)
        println("Message sent to ${activation.user?.email}")
    }
}