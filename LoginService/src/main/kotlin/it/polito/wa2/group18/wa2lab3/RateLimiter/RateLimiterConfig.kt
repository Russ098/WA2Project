package it.polito.wa2.group18.wa2lab3.RateLimiter

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class RateLimiterConfig : WebMvcConfigurer {

    @Autowired
    lateinit var interceptor : RateLimiterInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(interceptor).addPathPatterns("/user/**")
    }
}