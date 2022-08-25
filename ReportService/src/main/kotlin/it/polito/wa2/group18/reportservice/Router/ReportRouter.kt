package it.polito.wa2.group18.reportservice.Router

import it.polito.wa2.group18.reportservice.Handler.ReportHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.RequestPredicates.*

@Configuration(proxyBeanMethods = false)
class ReportRouter {
    @Bean
    fun route(reportHandler : ReportHandler):RouterFunction<ServerResponse>
    {
        return RouterFunctions
            .route(GET("/hello")
                .and(accept(MediaType.APPLICATION_JSON)), reportHandler::hello)
    }
}