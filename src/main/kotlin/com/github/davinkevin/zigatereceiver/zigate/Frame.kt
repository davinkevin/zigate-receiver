package com.github.davinkevin.zigatereceiver.zigate

import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import java.lang.RuntimeException
import java.util.*
import kotlin.experimental.xor

/**
 * Created by kevin on 18/11/2018
 */
data class Frame(
        val msgCodeBytes: ByteArray,
        val msgLengthBytes: ByteArray,
        val checksumBytes: ByteArray,
        val msgPayloadBytes: ByteArray,
        val rssiBytes: ByteArray
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Frame) return false

        if (!msgCodeBytes.contentEquals(other.msgCodeBytes)) return false
        if (!msgLengthBytes.contentEquals(other.msgLengthBytes)) return false
        if (!checksumBytes.contentEquals(other.checksumBytes)) return false
        if (!msgPayloadBytes.contentEquals(other.msgPayloadBytes)) return false
        if (!rssiBytes.contentEquals(other.rssiBytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = msgCodeBytes.contentHashCode()
        result = 31 * result + msgLengthBytes.contentHashCode()
        result = 31 * result + checksumBytes.contentHashCode()
        result = 31 * result + msgPayloadBytes.contentHashCode()
        result = 31 * result + rssiBytes.contentHashCode()
        return result
    }
}

fun createFrame(ab: ByteArray): Mono<Frame> {

    if (!isValid(ab)) {
        return Mono.empty()
    }

    ab
            .toFlux()
            .buffer(2)
            .map { decode(it) }

}

private fun decode(it: List<Byte>) = if (it[0] == 0x2.toByte()) it[1] xor 0x10 else it[0]

private fun isValid(ab: ByteArray) = (ab[0] == (1.toByte()) && ab[ab.size - 1] == 3.toByte())