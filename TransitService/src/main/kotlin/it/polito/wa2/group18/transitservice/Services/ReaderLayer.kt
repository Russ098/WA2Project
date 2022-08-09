package it.polito.wa2.group18.transitservice.Services

interface ReaderLayer {
    fun decodeQRCode(QRCode : ByteArray?) : String
    fun validateTicket() : Boolean
}