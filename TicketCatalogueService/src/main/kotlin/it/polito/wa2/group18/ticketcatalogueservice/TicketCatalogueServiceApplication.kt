package it.polito.wa2.group18.ticketcatalogueservice

import io.r2dbc.spi.ConnectionFactory
import it.polito.wa2.group18.ticketcatalogueservice.Entities.TicketOrder
import it.polito.wa2.group18.ticketcatalogueservice.Entities.TicketType
import it.polito.wa2.group18.ticketcatalogueservice.Repositories.TicketOrderRepository
import it.polito.wa2.group18.ticketcatalogueservice.Repositories.TicketTypeRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import java.time.Duration
import java.util.*


@SpringBootApplication
class TicketCatalogueServiceApplication {
companion object{
    @JvmStatic
    fun main(args: Array<String>) {
        runApplication<TicketCatalogueServiceApplication>(*args)
    }
}

    @Bean
    fun initializer(connectionFactory: ConnectionFactory?): ConnectionFactoryInitializer? {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory!!)
        initializer.setDatabasePopulator(ResourceDatabasePopulator(ClassPathResource("schema.sql")))
        return initializer
    }

    /*@Bean
    fun populator(ticketTypeRepo: TicketTypeRepository, ticketOrderRepo: TicketOrderRepository): CommandLineRunner? {
        return CommandLineRunner { args: Array<String?>? ->
            ticketTypeRepo.saveAll(
                listOf(
                    TicketType( null, "Normal", 1f),
                    TicketType( null, "WeekEnd", 6.5f, 27, 2, "ABC"),
                    TicketType( null, "FullWeek", 11.90f, -1, 7, "ABC"),
                    TicketType( null, "Daily", 2.3f, -1, 1, "AB"),
                    TicketType( null, "Monthly", 41.50f, -1, 30, "ABCD"),
                    TicketType( null, "Year", 350.0f, -1, 365, "ABCD")
                )
            ).blockLast(Duration.ofSeconds(10))
        }
    }*/

}