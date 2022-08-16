package it.polito.wa2.group18.travelerservicereact.Kafka

import it.polito.wa2.group18.travelerservicereact.Repositories.TicketPurchasedRepository
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
    lateinit var ticketRepo: TicketPurchasedRepository

    fun sendMessage(msg: TicketResponse) {
        kafkaTemplate.send(topic, msg)
    }

    @KafkaListener(topics = ["\${spring.kafka.template.requestTopic}"], groupId = "ppr")
    fun listenGroupFoo(consumerRecord: ConsumerRecord<Any, TicketRequest>, ack: Acknowledgment) {
         /*dal jws estrae sub
         controlla esistenza sub
        SE ESISTE SUB
        DECIDERE :
         O eliminare biglietto
         O aggiornare un campo "USATO" e fare CHECK biglietto singolo non sia giá stato usato
            comunque: check biglietto é corsa singola dal tickettype
         torna il userID
        risponde sul topic response con il TicketResponse
        ALTRIMENTI
         ritona non é valido*/
        logger.info("Message received {}", consumerRecord)
        val jws = consumerRecord.value().jws
        val readerID = consumerRecord.value().readerID
        val timestamp = consumerRecord.value().timestamp
        ticketRepo.existsByJws(jws).subscribe {
            if (it == true){
                    ticketRepo.getByJws(jws).subscribe {
                        val userID = it.userId
                        // OPERAZIONI X ELEIMINARE BIFGLIETTO O AGGIORNARE CAMPO "USATO"
                        sendMessage(TicketResponse(userID,timestamp,jws,readerID,true))
                    }
            }else{
                sendMessage(TicketResponse(null,timestamp,jws,readerID,false))
            }
        }
        ack.acknowledge()
    }
}