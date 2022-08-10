package it.polito.wa2.group18.transitservice.Controller

import it.polito.wa2.group18.transitservice.Services.ReaderLayer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class RESTController {
    @Autowired
    lateinit var readerLayer : ReaderLayer

    @GetMapping("readers/validate")
    fun validateJws(req: HttpServletRequest, @RequestBody qrCode : ByteArray?) : ResponseEntity<*>?{
        val jws = readerLayer.decodeQRCode(qrCode)


        return ResponseEntity.status(200).body(jws)
    }

    @GetMapping("/user/prova")
    fun prova() : String {
        return "Hello !!!"
    }
}