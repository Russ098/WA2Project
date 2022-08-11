package it.polito.wa2.group18.transitservice.Kafka

import it.polito.wa2.group18.transitservice.Entities.Transits
import it.polito.wa2.group18.transitservice.Repositories.TransitRepository
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate

import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component


@Component
class Consumer(
    @Value("\${spring.kafka.template.responseTopic}") val topic: String,
    @Autowired
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var transitRepo : TransitRepository

    /*fun sendMessage(msg: TicketRequest) {
        kafkaTemplate.send(topic, msg)
    }*/

    @KafkaListener(topics = ["\${spring.kafka.template.responseTopic}"], groupId = "ppr")
    fun listenGroupFoo(consumerRecord: ConsumerRecord<Any, TicketResponse>, ack: Acknowledgment) {
        logger.info("Message received {}", consumerRecord)
        val timestamp = consumerRecord.value().timestamp
        val userID = consumerRecord.value().userID
        val jws = consumerRecord.value().jws
        val key = consumerRecord.value().key
        // Validazione
        // Puoi vedere il tipo di biglietto
        transitRepo.save(Transits(null, timestamp!!, userID, jws)).subscribe()

        ack.acknowledge()
    }
}