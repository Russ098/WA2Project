package it.polito.wa2.group18.travelerservicereact.Kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer
import org.slf4j.LoggerFactory

class TravelerRequestDeserializer : Deserializer<TravelerRequest> {
    private val objectMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(javaClass)

    override fun deserialize(topic: String?, data: ByteArray?): TravelerRequest? {
        log.info("Deserializing...")
        return objectMapper.readValue(
                String(
                        data ?: throw SerializationException("Error when deserializing byte[] to Product"), Charsets.UTF_8
                ), TravelerRequest::class.java
        )
    }

    override fun close() {}
}