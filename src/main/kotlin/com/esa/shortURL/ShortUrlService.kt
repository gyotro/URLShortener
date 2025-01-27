package com.esa.shortURL

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

@Service
class ShortUrlService(private val redis: StringRedisTemplate)
{

    private val logger: Logger = LoggerFactory.getLogger(ShortUrlService::class.java)

    private val digest = MessageDigest.getInstance("SHA-256")

    fun shortener(url: String): String {

        logger.info("ShortUrlService - entering URL $url")
        val shortHash = hash(url)
        // verify whether the URL it's already stored
        if(redis.opsForValue().get(shortHash) == null){
            redis.opsForValue().set(shortHash, url)
        }
        logger.info("ShortUrlService - Returning encoded URL $shortHash")
        return shortHash
    }

    fun resolve(hash: String): String {
        return redis.opsForValue().get(hash) ?: throw HashUnknown(hash)
    }

    private fun hash(url: String, length: Int = 8): String {
        val hashBytes = digest.digest(url.toByteArray(Charsets.UTF_8))
        return Base64.getUrlEncoder().encodeToString(hashBytes).substring(0, length) // Truncate to 8 chars

    }

    fun decode(encodedUrl: String): String {
        var sFullUrl: String? = null
        logger.info("ShortUrlService - entering encoded URL $encodedUrl")
        // verify whether the URL it's already stored
        if (redis.opsForValue().get(encodedUrl) != null){
            sFullUrl = redis.opsForValue().get(encodedUrl)!!
            logger.info("ShortUrlService - Returning full URL $sFullUrl")
            return sFullUrl
        } else {
            throw UrlUnknown(encodedUrl)
        }
    }

    fun dencodeUrl(url: String): String {
        return URLDecoder.decode(url, StandardCharsets.UTF_8.toString())
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class HashUnknown(hash: String) : RuntimeException("$hash not found")


    @ResponseStatus(HttpStatus.NOT_FOUND)
    class UrlUnknown(url: String) : RuntimeException("$url not found")

}