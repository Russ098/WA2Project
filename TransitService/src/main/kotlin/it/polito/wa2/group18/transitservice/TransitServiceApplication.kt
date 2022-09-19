package it.polito.wa2.group18.transitservice

import io.r2dbc.spi.ConnectionFactory
import it.polito.wa2.group18.transitservice.Entities.QRCodeReaders
import it.polito.wa2.group18.transitservice.Entities.Transits
import it.polito.wa2.group18.transitservice.Repositories.QRCodeReaderRepository
import it.polito.wa2.group18.transitservice.Repositories.TransitRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant

@SpringBootApplication
class TransitServiceApplication{
    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<TransitServiceApplication>(*args)
        }
    }

    @Bean
    fun initializer(connectionFactory: ConnectionFactory?): ConnectionFactoryInitializer? {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory!!)
        initializer.setDatabasePopulator(ResourceDatabasePopulator(ClassPathResource("schema.sql")))
        return initializer
    }
    @Bean
    fun populator(qrCodeRepo: QRCodeReaderRepository, transitRepo: TransitRepository): CommandLineRunner? {
        return CommandLineRunner { args: Array<String?>? ->
            qrCodeRepo.saveAll(
                listOf(
                    QRCodeReaders(null,"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiY3JlZGVudGlhbHMiOiJRUkNvZGUxIiwicm9sZXMiOiJERVZJQ0UiLCJpYXQiOjE2NTQwODUyOTYsImV4cCI6MjY1NDA4ODg5Nn0.Plz-1fZ1nsau07rhzFNbleuRGppYsCQKRgkj8KBuC0XTAXpKz5-Bgf73Ulb5FPGSBI61JPU-h2Mh_uaLxXvR0A", "A"),
                    QRCodeReaders(null,"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwiY3JlZGVudGlhbHMiOiJRUkNvZGUyIiwicm9sZXMiOiJERVZJQ0UiLCJpYXQiOjE2NTQwODUyOTYsImV4cCI6MjY1NDA4ODg5Nn0.Dljpf9fOBBJwxy2BQSNffPQsA7NvoKvPgYKt6JCnaHkUnkfcncVy2JMKIdvUEtS7EjgtseXWG4dD1ZhCTpLU1g", "B"),
                    QRCodeReaders(null,"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwiY3JlZGVudGlhbHMiOiJRUkNvZGUzIiwicm9sZXMiOiJERVZJQ0UiLCJpYXQiOjE2NTQwODUyOTYsImV4cCI6MjY1NDA4ODg5Nn0.146PowAjHZFumwD7QSqoHoKqtQp3y1zK3G1aV0H2BDszNE8dqnt04wyhCCeaS_vfsVXWSR_tOc0Ob602LErqVQ", "C")
                )
            ).blockLast(Duration.ofSeconds(10))
            /*transitRepo.saveAll(
                listOf(
                        Transits(null, Timestamp.from(Instant.ofEpochMilli(1661159351000)), 1, null,1,true),
                    Transits(null, Timestamp.from(Instant.ofEpochMilli(1661159451000)), 2, null,2,true),
                    Transits(null, Timestamp.from(Instant.ofEpochMilli(1661159651000)), 1, null,3,true)
                )
            ).blockLast(Duration.ofSeconds(10))*/
        }
    }
}
