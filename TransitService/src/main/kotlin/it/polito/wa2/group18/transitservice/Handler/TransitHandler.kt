package it.polito.wa2.group18.transitservice.Handler


import it.polito.wa2.group18.transitservice.Kafka.LogController
import it.polito.wa2.group18.transitservice.SecurityPackage.JwtConfig
import it.polito.wa2.group18.transitservice.SecurityPackage.JwtUtils
import it.polito.wa2.group18.transitservice.Repositories.QRCodeReaderRepository
import it.polito.wa2.group18.transitservice.Repositories.TransitRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TransitHandler {
    @Autowired
    lateinit var qrCodeReadersRepo: QRCodeReaderRepository
    @Autowired
    lateinit var transitRepo : TransitRepository
    @Autowired
    lateinit var jwtUtils: JwtUtils
    @Autowired
    lateinit var jwtConfig: JwtConfig
    @Autowired
    lateinit var logController: LogController


}