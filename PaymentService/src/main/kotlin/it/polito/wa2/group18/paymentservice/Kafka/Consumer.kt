package it.polito.wa2.group18.paymentservice.Kafka

import it.polito.wa2.group18.paymentservice.Entities.Payment
import it.polito.wa2.group18.paymentservice.Repositories.PaymentRepository
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate

import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import java.util.*


@Component
class Consumer(
    @Value("\${spring.kafka.template.responseTopic}") val topic: String,
    @Autowired
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var paymentRepo: PaymentRepository

    fun sendMessage(msg: PaymentResponse) {
        kafkaTemplate.send(topic, msg)
    }

    @KafkaListener(topics = ["\${spring.kafka.template.default-topic}"], groupId = "ppr")
    fun listenGroupFoo(consumerRecord: ConsumerRecord<Any, PaymentRequest>, ack: Acknowledgment) {
        logger.info("Message received {}", consumerRecord)
        val id = consumerRecord.value().orderId
        val jwt = consumerRecord.value().userJwt
        paymentRepo.save(Payment(null, consumerRecord.value().userId!!)).subscribe()


        if (Math.random() > 0.5) {// RANDOM GENERATOR
            sendMessage(PaymentResponse(id, true, jwt))
        } else {
            sendMessage(PaymentResponse(id, false, jwt))
        }

        ack.acknowledge()
    }
}