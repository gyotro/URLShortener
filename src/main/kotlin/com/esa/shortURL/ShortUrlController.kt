package com.esa.shortURL

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

    @PostMapping("/shorten")
    fun shorten(@RequestBody request: ShortUrlRequest): ShortUrlResponse  {
        return ShortUrlResponse(service.shortener(request.url))
    }

    @GetMapping("/{shortCode}")
    fun resolve(@PathVariable shortCode: String): ResponseEntity<HttpStatus>{
        val target = service.resolve(shortCode)
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
            .location(URI.create(target))
            .header(HttpHeaders.CONNECTION, "close")
            .build()
    }

    data class ShortUrlRequest(val url: String)

    data class ShortUrlResponse(val shortUrl: String)


}