package it.polito.wa2.group18.paymentservice

import io.r2dbc.spi.ConnectionFactory
import it.polito.wa2.group18.paymentservice.Entities.Payment
import it.polito.wa2.group18.paymentservice.Repositories.PaymentRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@SpringBootApplication
class PaymentServiceApplication {
companion object
{@JvmStatic
    fun main(args: Array<String>) {
        runApplication<PaymentServiceApplication>(*args)
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
    fun populator(repository: PaymentRepository): CommandLineRunner? {
        return CommandLineRunner { args: Array<String?>? ->
            repository.saveAll(
                listOf(
                    Payment( null, 1),
                    Payment( null, 1),
                    Payment( null, 2)
                )
            ).subscribe()
        }
    }*/
}