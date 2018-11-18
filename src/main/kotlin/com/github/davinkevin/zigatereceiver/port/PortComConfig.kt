package com.github.davinkevin.zigatereceiver.port

import com.fazecast.jSerialComm.SerialPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by kevin on 18/11/2018
 */
@Configuration
class PortComConfig {

    @Bean
    fun port()= (SerialPort
            .getCommPorts()
            .asSequence()
            .filter { "FT232R" in it.descriptivePortName }
            .filter { "Dial-In" !in it.descriptivePortName }
            .first() ?: throw RuntimeException("No port found"))
            .apply { baudRate = 115200 }
}