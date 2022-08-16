package it.polito.wa2.group18.transitservice.Router

import it.polito.wa2.group18.transitservice.Handler.TransitHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration(proxyBeanMethods = false)
class TransitRouter {

    @Bean
    fun route(transitHandler: TransitHandler) : RouterFunction<ServerResponse>{
        return RouterFunctions
                .route(GET("/hello")
                        .and(accept(MediaType.APPLICATION_JSON)), transitHandler::hello)
                .andRoute(POST("/readers/validate")
                    .and(accept(MediaType.IMAGE_PNG)), transitHandler::validateTicket)
    }
}