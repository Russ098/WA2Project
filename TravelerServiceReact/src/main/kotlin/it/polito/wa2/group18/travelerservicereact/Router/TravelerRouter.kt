package it.polito.wa2.group18.travelerservicereact.Router


import it.polito.wa2.group18.travelerservicereact.Handler.TravelerHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration(proxyBeanMethods = false)
class TravelerRouter {

    @Bean
    fun route(travelerHandler: TravelerHandler) : RouterFunction<ServerResponse>{
        return RouterFunctions
                .route(GET("/hello")
                        .and(accept(MediaType.APPLICATION_JSON)), travelerHandler::hello)
    }
}