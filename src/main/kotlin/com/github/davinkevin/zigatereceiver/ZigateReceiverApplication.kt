package com.github.davinkevin.zigatereceiver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ZigateReceiverApplication

fun main(args: Array<String>) {
    runApplication<ZigateReceiverApplication>(*args)
}
