package it.polito.wa2.group18.ticketcatalogueservice.Kafka

import it.polito.wa2.group18.ticketcatalogueservice.DTO.TicketPurchasedDTO
import it.polito.wa2.group18.ticketcatalogueservice.Entities.STATUS
import it.polito.wa2.group18.ticketcatalogueservice.Repositories.TicketOrderRepository
import it.polito.wa2.group18.ticketcatalogueservice.Repositories.TicketTypeRepository
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.*
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.Duration
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import java.util.*
import kotlin.collections.HashMap

@Component
class Consumer(
    @Value("\${spring.kafka.template.default-topic}") val topic: String,
    @Autowired
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var ticketOrderRepo: TicketOrderRepository

    @Autowired
    lateinit var ticketTypeRepository: TicketTypeRepository

    @KafkaListener(topics = ["\${spring.kafka.template.responseTopic}"], groupId = "ppr")
    fun listenGroupFoo(consumerRecord: ConsumerRecord<Any, PaymentResponse>, ack: Acknowledgment) {
        logger.info("Message received {}", consumerRecord)
        ack.acknowledge()
        val orderId = consumerRecord.value().orderId
        val accepted = consumerRecord.value().accepted
        if (orderId != null) {
            println("CHECKPOINT 1")
            ticketOrderRepo.findById(orderId).subscribe {
                println("CHECKPOINT 2")
                if (accepted) {
                    it.status = STATUS.COMPLETE
                    ticketOrderRepo.save(it).subscribe()
                    buildTicket(it.ticketTypeId, it.ticketNumber, consumerRecord.value().userJwt!!)
                    println("OK FATTO!")
                } else {
                    it.status = STATUS.CANCELLED
                    ticketOrderRepo.save(it).subscribe()
                    println("NOT OK  NON FATTO!")
                }
            }
        }
    }

    fun buildTicket(ticketTypeId: Long, ticketNumber: Long, jwt: String) {
        ticketTypeRepository.findById(ticketTypeId).subscribe {
            val new = calculateParams(it.ticketType, it.duration, it.zones)
            sendTicketsToTraveler(jwt, new, ticketNumber)
        }
    }

    fun sendTicketsToTraveler(jwt: String, ticket: TicketPurchasedDTO, numberOfTickets: Long) {
        val url = "http://localhost:8082/my/tickets"

        val headers = HttpHeaders()
        headers.contentType = (MediaType.APPLICATION_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.setBearerAuth(jwt)

        val body: HashMap<String, Any> = hashMapOf()

        body.put("cmd", "buy_tickets")
        body.put("quantity", numberOfTickets)
        body.put("ticket", ticket)

        val request = HttpEntity(body, headers)
        val response: ResponseEntity<Array<TicketPurchasedDTO>> = RestTemplateBuilder().build()
            .exchange(url, HttpMethod.POST, request, Array<TicketPurchasedDTO>::class.java, 0)
        println("RICHIESTA MANDATA")
    }

    fun calculateParams(ticketType : String, duration : Int, zones : String) : TicketPurchasedDTO{
        var exp : Date = Date()
        val c = Calendar.getInstance()
        c.time=Date()

        var validFrom = c.time

        when(ticketType){
            "WeekEnd" -> {
                while(c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
                    c.add(Calendar.HOUR, 24)
                validFrom = c.time
                validFrom.hours = 0
                validFrom.minutes = 1
                c.add(Calendar.HOUR, 24)
                exp = c.time
                exp.hours = 23
                exp.minutes = 59
            }
            "Normal" -> {
                c.add(Calendar.HOUR, 2)
                exp = c.time
            }
            else -> {
                c.add(Calendar.HOUR, 24*duration)
                exp = c.time
            }
        }
        return TicketPurchasedDTO(null, Date(), exp, zones, validFrom, ticketType, null)
    }
}