package it.polito.wa2.group18.reportservice.Handler

import it.polito.wa2.group18.reportservice.DTOs.AllRequest
import it.polito.wa2.group18.reportservice.DTOs.ReportResponse
import it.polito.wa2.group18.reportservice.DTOs.UserRequest
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

@Component
class ReportHandler {

    fun hello(req: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue("Hello"))
    }

    fun getAllTransits(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<AllRequest>().flatMap { request ->
            val webClient = WebClient.create("http://localhost:8085")
            webClient.post()
                .uri("/admin/all/transits")
                .header("Authorization", req.headers().header("Authorization")[0])
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(String::class.java).flatMap { allTransits ->
                    ServerResponse.ok().body(BodyInserters.fromValue(allTransits))
                }
        }.onErrorResume { println(it); ServerResponse.notFound().build() }
    }

    fun getUserTransits(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<UserRequest>().flatMap { request ->
            println(request.toString())
            val webClient = WebClient.create("http://localhost:8085")
            webClient.post()
                .uri("/admin/user/transits")
                .header("Authorization", req.headers().header("Authorization")[0])
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(String::class.java).flatMap { allTransits ->
                    ServerResponse.ok().body(BodyInserters.fromValue(allTransits))
                }
        }.onErrorResume { println(it); ServerResponse.notFound().build() }
    }

    fun getAllPurchases(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<AllRequest>().flatMap { request ->
            val webClient = WebClient.create("http://localhost:8082")
            webClient.post()
                .uri("/admin/getAllPurchases")
                .header("Authorization", req.headers().header("Authorization")[0])
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(String::class.java).flatMap { allPurchases ->
                    ServerResponse.ok().body(BodyInserters.fromValue(allPurchases))
                }
        }.onErrorResume { println(it); ServerResponse.notFound().build() }
    }

    fun getUserPurchases(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<UserRequest>().flatMap { request ->
            val webClient = WebClient.create("http://localhost:8082")
            webClient.post()
                .uri("/admin/getUserPurchases")
                .header("Authorization", req.headers().header("Authorization")[0])
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(String::class.java).flatMap { allPurchases ->
                    ServerResponse.ok().body(BodyInserters.fromValue(allPurchases))
                }
        }.onErrorResume { println(it); ServerResponse.notFound().build() }
    }

    fun getAllPurchasesAndTransits(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<AllRequest>().flatMap { request ->
            val webClient = WebClient.create("http://localhost:8082")
            webClient.post()
                .uri("/admin/getAllPurchases")
                .header("Authorization", req.headers().header("Authorization")[0])
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(String::class.java).flatMap { allPurchases ->
                    val webClient2 = WebClient.create("http://localhost:8085")
                    webClient2.post()
                        .uri("/admin/all/transits")
                        .header("Authorization", req.headers().header("Authorization")[0])
                        .body(BodyInserters.fromValue(request))
                        .retrieve()
                        .bodyToMono(String::class.java).flatMap { allTransits ->
                            ServerResponse.ok().body(BodyInserters.fromValue(ReportResponse(allTransits, allPurchases)))
                        }.onErrorResume { println(it); ServerResponse.notFound().build() }
                }
        }.onErrorResume { println(it); ServerResponse.notFound().build() }
    }

    fun getUserPurchasesAndTransits(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<UserRequest>().flatMap { request ->
            val webClient = WebClient.create("http://localhost:8082")
            webClient.post()
                .uri("/admin/getUserPurchases")
                .header("Authorization", req.headers().header("Authorization")[0])
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(String::class.java).flatMap { allPurchases ->
                    val webClient2 = WebClient.create("http://localhost:8085")
                    webClient2.post()
                        .uri("/admin/user/transits")
                        .header("Authorization", req.headers().header("Authorization")[0])
                        .body(BodyInserters.fromValue(request))
                        .retrieve()
                        .bodyToMono(String::class.java).flatMap { allTransits ->
                            ServerResponse.ok().body(BodyInserters.fromValue(ReportResponse(allTransits, allPurchases)))
                        }.onErrorResume { println(it); ServerResponse.notFound().build() }
                }
        }.onErrorResume { println(it); ServerResponse.notFound().build() }
    }
}