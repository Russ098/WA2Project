package it.polito.wa2.group18.transitservice.Repositories

import it.polito.wa2.group18.transitservice.Entities.QRCodeReaders
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface QRCodeReaderRepository : ReactiveCrudRepository<QRCodeReaders, Long?> {
}