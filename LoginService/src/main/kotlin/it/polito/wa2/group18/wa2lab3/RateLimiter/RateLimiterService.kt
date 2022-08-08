package it.polito.wa2.group18.wa2lab3.RateLimiter

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Bucket4j
import io.github.bucket4j.Refill
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RateLimiterService {

    private var bucket: Bucket? = null

    fun getBucket() : Bucket? {
        if(bucket == null)
            newBucket()
        return bucket
    }
    fun newBucket() {
        val refill = Refill.intervally(10, Duration.ofSeconds(1))
        val limit = Bandwidth.classic(10, refill)
        bucket = Bucket4j.builder().addLimit(limit).build()
    }
}