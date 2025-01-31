package com.esa.shortURL

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import java.security.MessageDigest
import java.util.*

@Service
class ShortUrlService(private val redis: StringRedisTemplate)
{

    private val digest = MessageDigest.getInstance("SHA-256")

    fun shortener(url: String): String {
        val shortHash = hash(url)
        // verify whether the URL it's already stored
        if(redis.opsForValue().get(shortHash) == null){
            redis.opsForValue().set(shortHash, url)
        }
        return shortHash
    }

    fun resolve(hash: String): String {
        return redis.opsForValue().get(hash) ?: throw HashUnknown(hash)
    }

    private fun hash(url: String, length: Int = 8): String {
        val hashBytes = digest.digest(url.toByteArray(Charsets.UTF_8))
        return Base64.getUrlEncoder().encodeToString(hashBytes).substring(0, length) // Truncate to 8 chars

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class HashUnknown(hash: String) : RuntimeException("$hash not found")

}