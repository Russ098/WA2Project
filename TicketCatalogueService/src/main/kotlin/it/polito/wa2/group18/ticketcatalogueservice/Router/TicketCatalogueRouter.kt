package it.polito.wa2.group18.ticketcatalogueservice.Router

import it.polito.wa2.group18.ticketcatalogueservice.Handler.TicketCatalogueHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse


@Configuration (proxyBeanMethods = false)
class TicketCatalogueRouter
{
    @Bean
    fun route (tch : TicketCatalogueHandler): RouterFunction<ServerResponse>
    {
        return RouterFunctions
            .route(GET("/hello")
                .and(accept(MediaType.APPLICATION_JSON)), tch::hello)
            .andRoute(GET("/tickets")
                .and(accept(MediaType.APPLICATION_JSON)), tch::getAllTickets)
            .andRoute(GET("/admin/orders")
                .and(accept(MediaType.APPLICATION_JSON)), tch::getAllOrders)
            .andRoute(GET("/orders")
                .and(accept(MediaType.APPLICATION_JSON)), tch::getUserOrders)
            .andRoute(GET("/orders/{orderId}")
                .and(accept(MediaType.APPLICATION_JSON)), tch::getUserOrder)
            .andRoute(GET("/admin/orders/{userId}")
                .and(accept(MediaType.APPLICATION_JSON)), tch::getAdminOrder)
            .andRoute(POST("/admin/tickets")
                .and(accept(MediaType.APPLICATION_JSON)), tch::addNewTicket)
            /*Body di POST /admin/tickets
                {
                    "ticketType" : "nuovo_ticket",
                    "price": 100.0,
                    "ageBelow" : -1,
                    "duration": 15,
                    "zones": "ABC"
                }
             */
            .andRoute(PUT("/admin/tickets")
                .and(accept(MediaType.APPLICATION_JSON)), tch::addNewTicket)
            /*Body di PUT /admin/tickets
            {
                "id" : 1
                "ticketType" : "nuovo_ticket",
                "price": 100.0,
                "ageBelow" : -1,
                "duration": 15,
                "zones": "ABC"
            }
         */
            .andRoute(POST("/shop")
                .and(accept(MediaType.APPLICATION_JSON)), tch::buyTickets)
            /*Body di POST /shop/{ticketId}
                 {
                    "ticketId": 2,
                    "ticketNumber": 3,
                    "card":{
	                    "creditCardNumber": "somenumber",
                        "expirationDate": "2012-04-23T18:25:43.511Z",
                        "cvv": "123",
                        "cardHolder": "CapsuleCorp"
	                    }
                   }
             */
    }
}