package com.github.davinkevin.zigatereceiver.boot

import com.fazecast.jSerialComm.SerialPort
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import java.time.Duration.ofMillis

/**
 * Created by kevin on 18/11/2018
 */
@Component
class StartUpInformation(val port: SerialPort) : CommandLineRunner {

    override fun run(vararg args: String?) {
        println("port: ${port.descriptivePortName}")
        port.openPort()
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 12)



        Flux.interval(ofMillis(20))
                .filter { port.bytesAvailable() != 0 }
                .flatMap { read().toFlux() }
                .bufferUntil { it == 3.toByte() }
                .subscribe { println("receive message $it") }

    }

    fun read(): ByteArray {
        val b = ByteArray(port.bytesAvailable())
        port.readBytes(b, b.size.toLong())
        return b
    }
}