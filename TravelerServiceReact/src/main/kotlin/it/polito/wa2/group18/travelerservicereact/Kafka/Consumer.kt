package it.polito.wa2.group18.travelerservicereact.Kafka

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
    @Value("\${spring.kafka.template.requestTopic}") val topic: String,
    @Autowired
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    private val logger = LoggerFactory.getLogger(javaClass)


    @KafkaListener(topics = ["\${spring.kafka.template.requestTopic}"], groupId = "ppr")
    fun listenGroupFoo(consumerRecord: ConsumerRecord<Any, *>, ack: Acknowledgment) {
        logger.info("Message received {}", consumerRecord)
        ack.acknowledge()

    }
}