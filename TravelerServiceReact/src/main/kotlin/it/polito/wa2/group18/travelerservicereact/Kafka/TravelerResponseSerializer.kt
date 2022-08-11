package it.polito.wa2.group18.travelerservicereact.Kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Serializer
import org.slf4j.LoggerFactory

class TravelerResponseSerializer : Serializer<TravelerResponse> {
    private val objectMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(javaClass)

    override fun serialize(topic: String?, data: TravelerResponse?): ByteArray? {
        log.info("Serializing...")
        return objectMapper.writeValueAsBytes(
                data ?: throw SerializationException("Error when serializing Product to ByteArray[]")
        )
    }

    override fun close() {}
}