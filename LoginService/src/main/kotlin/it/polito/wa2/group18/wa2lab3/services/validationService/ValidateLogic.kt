package it.polito.wa2.group18.wa2lab3.services.validationService

import it.polito.wa2.group18.wa2lab3.dtos.UserValidatedDTO
import it.polito.wa2.group18.wa2lab3.dtos.toValidatedDTO
import it.polito.wa2.group18.wa2lab3.repositories.ActivationRepository
import it.polito.wa2.group18.wa2lab3.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
@Transactional
class ValidateLogic : ValidateLayer {
    @Autowired
    lateinit var activationRepo: ActivationRepository

    @Autowired
    lateinit var userRepo: UserRepository

    override fun validation(provisional_id: UUID, activation_code: String): UserValidatedDTO? {
        // Get row and check id exist
        try {
            val activationRow = activationRepo.getById(provisional_id) ?: return null

            // check exp time
            val rowExpTime = activationRow.expirationTime
            if (rowExpTime!!.before(Date()))
                return null

            // check act code ==
            if (activationRow.activationCode != activation_code) {
                activationRow.attempts -= 1
                if (activationRow.attempts > 0)
                    activationRepo.save(activationRow)
                else {
                    activationRepo.delete(activationRow)
                    val userToDelete = activationRow.user
                    userRepo.delete(userToDelete!!)
                }
                return null
            }

            activationRepo.delete(activationRow)
            val userToUpdate = activationRow.user
            val userRow = userRepo.getById(userToUpdate?.id!!)
            userRow.pending = false
            userRepo.save(userRow)

            return userRow.toValidatedDTO()
        } catch (e: Exception) {
            println(e)
            return null
        }
    }


    @Scheduled(fixedDelay = 2000, initialDelay = 10000)
    fun pruner() {
        val listActivations = activationRepo.findAll()
        val expired = listActivations.filter {
            it.expirationTime!!.before(Date())
        }

        //println("Found ${expired.size} expired activations! ${i++}")
        expired.forEach {
            val user = it.user?.id?.let { it1 -> userRepo.getById(it1) }
            activationRepo.delete(it)
            userRepo.delete(user!!)
        }
    }
}