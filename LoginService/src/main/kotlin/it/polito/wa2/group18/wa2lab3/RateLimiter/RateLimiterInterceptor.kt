package it.polito.wa2.group18.wa2lab3.RateLimiter

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket4j
import io.github.bucket4j.Refill
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RateLimiterInterceptor : HandlerInterceptor {

    @Autowired
    lateinit var bucket : RateLimiterService

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        val probe = bucket.getBucket()?.tryConsumeAndReturnRemaining(1)
        if (probe != null) {
            return if(probe.isConsumed){
                //println("TOKEN remaining ${probe.remainingTokens}")
                true
            }else {
                response.sendError(HttpStatus.TOO_MANY_REQUESTS.value())
                false
            }
        }
        return false
    }
}