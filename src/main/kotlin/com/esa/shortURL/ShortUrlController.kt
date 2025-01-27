package com.esa.shortURL

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/")
class ShortUrlController(val service: ShortUrlService) {

    private val logger: Logger = LoggerFactory.getLogger(ShortUrlController::class.java)

    @PostMapping("/shorten")
    fun shorten(@RequestBody request: ShortUrlRequest): ShortUrlResponse  {
        return ShortUrlResponse(service.shortener(request.url))
    }

    @PostMapping("/decode")
    fun decode(@RequestBody request: UrlRequest): UrlResponse  {
        return UrlResponse(service.decode(request.encodedUrl))
    }

    @GetMapping("/{shortCode}")
    fun resolve(@PathVariable shortCode: String): ResponseEntity<HttpStatus>{
        val target = service.resolve(shortCode)
        logger.info("ShortUrlService - Redirecting to $target")
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
            .location(URI.create(target))
            .header(HttpHeaders.CONNECTION, "close")
            .build()
    }

    data class ShortUrlRequest(val url: String)

    data class ShortUrlResponse(val shortUrl: String)

    data class UrlRequest(val encodedUrl: String)

    data class UrlResponse(val fullUrl: String)


}