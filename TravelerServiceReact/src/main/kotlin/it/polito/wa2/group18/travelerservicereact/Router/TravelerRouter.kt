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
                .route(GET("/my/profile")
                        .and(accept(MediaType.APPLICATION_JSON)), travelerHandler::getProfile)
                .andRoute(PUT("/my/profile")
                        .and(accept(MediaType.APPLICATION_JSON)), travelerHandler::saveProfile)
                .andRoute(GET("/my/tickets")
                        .and(accept(MediaType.APPLICATION_JSON)), travelerHandler::getTickets)
                .andRoute(POST("/my/tickets")
                        .and(accept(MediaType.APPLICATION_JSON)), travelerHandler::buyTickets)
                .andRoute(GET("/admin/travelers")
                    .and(accept(MediaType.APPLICATION_JSON)), travelerHandler::getTravelers)
                .andRoute(GET("/admin/traveler/{userID}/tickets")
                    .and(accept(MediaType.APPLICATION_JSON)), travelerHandler::getTicketsByUserID)
                .andRoute(GET("/admin/traveler/{userID}/profile")
                    .and(accept(MediaType.APPLICATION_JSON)), travelerHandler::getProfileByUserID)
                .andRoute(GET("/secret")
                    .and(accept(MediaType.APPLICATION_JSON)),travelerHandler::getSecret)
                .andRoute(GET("/my/qrcode/{ticketID}")
                    .and(accept(MediaType.APPLICATION_JSON)),travelerHandler::getQRCode)
                .andRoute(POST("/admin/getAllPurchases")
                    .and(accept(MediaType.APPLICATION_JSON)),travelerHandler::getAllPurchases)
                .andRoute(POST("/admin/getUserPurchases")
                    .and(accept(MediaType.APPLICATION_JSON)),travelerHandler::getUserPurchases)
    }
}