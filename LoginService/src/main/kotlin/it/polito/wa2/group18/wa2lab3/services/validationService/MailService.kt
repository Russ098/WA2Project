package it.polito.wa2.group18.wa2lab3.services.validationService

import org.springframework.context.annotation.Bean
import org.springframework.mail.MailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Component
import java.util.*

@Component
class MailService {
    private val sender = JavaMailSenderImpl()
    @Bean
    fun mailSender() : MailSender {
        sender.host = "smtp.gmail.com"
        sender.port = 587
        sender.username = "g18.wa2.poliTo2k22@gmail.com"
        sender.password = "P4\$\$word"

        configureJavaMailProps(sender.javaMailProperties)
        return sender
    }
    private fun configureJavaMailProps(properties: Properties){
        properties["mail.transport.protocol"] = "smtp"
        properties["mail.smtp.auth"] = true
        properties["mail.smtp.starttls.enable"] = true
    }
}