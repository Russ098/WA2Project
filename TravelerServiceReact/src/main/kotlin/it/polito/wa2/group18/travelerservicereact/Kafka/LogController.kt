package it.polito.wa2.group18.travelerservicereact.Kafka

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

//E' il Producer

@RestController
@RequestMapping("/transitResponse")
class LogController(
    @Value("\${spring.kafka.template.responseTopic}") val topic: String,
    @Autowired
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun post(@Validated @RequestBody ticketResponse: TicketResponse): ResponseEntity<Any> {
        return try {
            log.info("Receiving payment request")
            log.info("Sending message to Kafka {}", ticketResponse)
            val message: Message<TicketResponse> = MessageBuilder
                .withPayload(ticketResponse)
                .setHeader(KafkaHeaders.TOPIC, topic)
//                .setHeader("X-Custom-Header", "Custom header here")
                .build()
            kafkaTemplate.send(message)
            log.info("Message sent with success")
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            log.error("Exception: {}",e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error to send message")
        }
    }
}