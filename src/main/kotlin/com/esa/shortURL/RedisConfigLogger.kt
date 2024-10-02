package com.esa.shortURL

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RedisConfigLogger {

    // Inject Redis properties using @Value
    @Value("\${spring.data.redis.host}")
    lateinit var redisHost: String

    @Value("\${spring.data.redis.port}")
    var redisPort: Int = 6379

    private val logger = LoggerFactory.getLogger(RedisConfigLogger::class.java)

    @PostConstruct
    fun logRedisConfig() {
        // Log Redis host and port at startup
        logger.info("Connecting to Redis at host: $redisHost, port: $redisPort")
    }
}
