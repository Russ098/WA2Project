package it.polito.wa2.group18.transitservice.Services

import com.google.zxing.BinaryBitmap
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

@Service
@Transactional
class ReaderLogic : ReaderLayer{
    override fun decodeQRCode(QRCode : ByteArray?) : String {
        val qrCodeReader = QRCodeReader()
        val v = ByteArrayInputStream(QRCode)
        val binBitmap : BinaryBitmap = BinaryBitmap(HybridBinarizer(BufferedImageLuminanceSource(
            ImageIO.read(v)
        )))
        val res = qrCodeReader.decode(binBitmap)
        println("decodeQRCode(): " + res)
        return  res.toString() //jws
    }

    override fun validateTicket(): Boolean {
        TODO("Not yet implemented")
    }
}