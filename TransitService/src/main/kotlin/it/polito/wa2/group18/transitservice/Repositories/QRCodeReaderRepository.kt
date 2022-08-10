package it.polito.wa2.group18.transitservice.Repositories

import it.polito.wa2.group18.transitservice.Entities.QRCodeReaders
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface QRCodeReaderRepository : ReactiveCrudRepository<QRCodeReaders, Long?> {

}