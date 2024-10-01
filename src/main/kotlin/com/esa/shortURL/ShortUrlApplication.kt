package com.esa.shortURL

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShortUrlApplication

fun main(args: Array<String>) {
	runApplication<ShortUrlApplication>(*args)
}
