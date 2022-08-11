package it.polito.wa2.group18.travelerservicereact.Kafka

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class KafkaConfiguration(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val servers: String,
    @Value("\${spring.kafka.template.default-topic}")
    private val topicRequest: String,
    @Value("\${spring.kafka.template.responseTopic}")
    private val topicResponse: String

){

    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs: MutableMap<String, Any?> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
        return KafkaAdmin(configs)
    }

    @Bean
    fun topicRequest(): NewTopic {
        return NewTopic(topicRequest, 1, 1.toShort())
    }

    @Bean
    fun topicResponse(): NewTopic {
        return NewTopic(topicResponse, 1, 1.toShort())
    }
}