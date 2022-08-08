package it.polito.wa2.group18.paymentservice.Router

import it.polito.wa2.group18.paymentservice.Handler.PaymentHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class PaymentRouter {

    @Bean
    fun route (ph : PaymentHandler) : RouterFunction<ServerResponse>
    {
        return RouterFunctions
            .route(GET("/hello")
                .and(accept(MediaType.APPLICATION_JSON)),ph::hello )
            .andRoute(GET("/admin/transactions")
                .and(accept(MediaType.APPLICATION_JSON)),ph::getTransactions)
            .andRoute(GET("/transactions")
                .and(accept(MediaType.APPLICATION_JSON)),ph::getTransactionByUser)
    }
}